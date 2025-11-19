package com.cifrador.ui;

import com.cifrador.database.DatabaseHelper;
import com.cifrador.encryptor.HybridCipher;
import com.cifrador.utils.RSAUtil;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class MainApp extends Application {

    private File selectedFileEncrypt;
    private File selectedFileDecrypt;
    private PublicKey loadedPublicKey;
    private PrivateKey loadedPrivateKey;

    private Label status;
    private Label publicKeyLabel, privateKeyLabel, fileLabelEncrypt, fileLabelDecrypt;
    private TextField userField;

    // Ajusta estos datos si difieren
    private final String DB_HOST = "localhost";
    private final int DB_PORT = 3306;
    private final String DB_NAME = "cifrador";
    private final String DB_USER = "malaga";
    private final String DB_PASSWORD = "malaga";

    private DatabaseHelper dbHelper;

    @Override
    public void start(Stage primaryStage) {
        dbHelper = new DatabaseHelper(DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD);
        primaryStage.setTitle("Cifrador Híbrido RSA-AES");

        status = new Label("Estado: listo.");

        // ===================== COLUMNA 1: Usuario Nuevo =====================
        Label titulo1 = new Label("1.- Usuario Nuevo");
        titulo1.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label txtNuevoUser = new Label("¿Eres usuario nuevo? Regístrate aquí");
        userField = new TextField();
        userField.setPromptText("Ingresa nombre de usuario");

        Button genKeysBtn = new Button("Generar par RSA y guardar");
        genKeysBtn.setOnAction(e -> handleGenerateKeys(primaryStage));

        VBox col1 = new VBox(10, titulo1, txtNuevoUser, userField, genKeysBtn);
        col1.setAlignment(Pos.TOP_CENTER);
        col1.setPadding(new Insets(20));


        // ================== COLUMNA 2: Cifrar Archivo =====================
        Label titulo2 = new Label("2.- Cifrar Archivo");
        titulo2.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label lblPub = new Label("Carga tu clave pública");
        Button loadPubBtn = new Button("Seleccionar clave pública");
        publicKeyLabel = new Label("Ninguna clave cargada");
        loadPubBtn.setOnAction(e -> handleLoadPublicKey(primaryStage));

        Label lblFileEnc = new Label("Selecciona tu archivo");
        Button chooseFileEncBtn = new Button("Seleccionar archivo");
        fileLabelEncrypt = new Label("Ningún archivo seleccionado");
        chooseFileEncBtn.setOnAction(e -> selectedFileEncrypt = handleSelectFile(primaryStage, fileLabelEncrypt));

        Label lblCifrar = new Label("Dale en Cifrar archivo");
        Button encryptBtn = new Button("Cifrar archivo");
        encryptBtn.setOnAction(e -> handleEncrypt());

        VBox col2 = new VBox(10, titulo2, lblPub, loadPubBtn, publicKeyLabel,
                lblFileEnc, chooseFileEncBtn, fileLabelEncrypt, lblCifrar, encryptBtn);
        col2.setAlignment(Pos.TOP_CENTER);
        col2.setPadding(new Insets(20));


        // ================== COLUMNA 3: Descifrar Archivo =====================
        Label titulo3 = new Label("3.- Descifrar Archivo");
        titulo3.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label lblPriv = new Label("Carga tu clave privada");
        Button loadPrivBtn = new Button("Seleccionar clave privada");
        privateKeyLabel = new Label("Ninguna clave cargada");
        loadPrivBtn.setOnAction(e -> handleLoadPrivateKey(primaryStage));

        Label lblFileDec = new Label("Selecciona tu archivo cifrado");
        Button chooseFileDecBtn = new Button("Seleccionar archivo");
        fileLabelDecrypt = new Label("Ningún archivo seleccionado");
        chooseFileDecBtn.setOnAction(e -> selectedFileDecrypt = handleSelectFile(primaryStage, fileLabelDecrypt));

        Label lblDescifrar = new Label("Dale en Descifrar archivo");
        Button decryptBtn = new Button("Descifrar archivo");
        decryptBtn.setOnAction(e -> handleDecrypt());

        VBox col3 = new VBox(10, titulo3, lblPriv, loadPrivBtn, privateKeyLabel,
                lblFileDec, chooseFileDecBtn, fileLabelDecrypt, lblDescifrar, decryptBtn);
        col3.setAlignment(Pos.TOP_CENTER);
        col3.setPadding(new Insets(20));


        // ================== GRID GENERAL =====================
        GridPane root = new GridPane();
        root.setPadding(new Insets(20));
        root.setHgap(30);
        root.setAlignment(Pos.CENTER);

        root.add(col1, 0, 0);
        root.add(col2, 1, 0);
        root.add(col3, 2, 0);

        VBox mainLayout = new VBox(10, root, status);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(10));

        Scene scene = new Scene(mainLayout, 950, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // ===================== MÉTODOS DE ACCIÓN =====================

    private void handleGenerateKeys(Stage stage) {
        try {
            KeyPair kp = RSAUtil.generateKeyPair(2048);

            FileChooser fc = new FileChooser();
            fc.setTitle("Guardar clave pública");
            fc.setInitialFileName("public_key.pem");
            File pubFile = fc.showSaveDialog(stage);
            if (pubFile != null) RSAUtil.savePublicKey(kp.getPublic(), pubFile);

            FileChooser fc2 = new FileChooser();
            fc2.setTitle("Guardar clave privada");
            fc2.setInitialFileName("private_key.pem");
            File privFile = fc2.showSaveDialog(stage);
            if (privFile != null) RSAUtil.savePrivateKey(kp.getPrivate(), privFile);

            status.setText("Par de claves RSA generado y guardado.");
        } catch (Exception ex) {
            status.setText("Error generando claves: " + ex.getMessage());
        }
    }

    private void handleLoadPublicKey(Stage stage) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Seleccionar clave pública (PEM)");
        File f = fc.showOpenDialog(stage);
        if (f == null) return;
        try {
            loadedPublicKey = RSAUtil.loadPublicKey(f);
            publicKeyLabel.setText("Clave pública cargada: " + f.getName());
        } catch (Exception ex) {
            status.setText("Error al cargar clave pública: " + ex.getMessage());
        }
    }

    private File handleSelectFile(Stage stage, Label fileLabel) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Seleccionar archivo");
        File f = fc.showOpenDialog(stage);
        if (f != null) fileLabel.setText("Archivo seleccionado: " + f.getName());
        return f;
    }

    private void handleLoadPrivateKey(Stage stage) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Seleccionar clave privada (PEM)");
        File f = fc.showOpenDialog(stage);
        if (f == null) return;
        try {
            loadedPrivateKey = RSAUtil.loadPrivateKey(f);
            privateKeyLabel.setText("Clave privada cargada: " + f.getName());
        } catch (Exception ex) {
            status.setText("Error al cargar clave privada: " + ex.getMessage());
        }
    }

    private void handleEncrypt() {
        if (selectedFileEncrypt == null || loadedPublicKey == null) {
            status.setText("Selecciona archivo y clave pública antes de cifrar.");
            return;
        }
        try {
            File outEnc = new File(selectedFileEncrypt.getParentFile(), selectedFileEncrypt.getName() + ".enc");
            File outKey = new File(selectedFileEncrypt.getParentFile(), selectedFileEncrypt.getName() + ".key.enc");

            String claveCifradaB64 = HybridCipher.encryptFile(selectedFileEncrypt, outEnc, outKey, loadedPublicKey);
            String usuario = userField.getText().trim();
            dbHelper.insertOperacion(selectedFileEncrypt.getName(), claveCifradaB64, usuario.isEmpty() ? null : usuario);

            status.setText("Archivo cifrado correctamente: " + outEnc.getName());
        } catch (Exception ex) {
            status.setText("Error cifrando: " + ex.getMessage());
        }
    }

    private void handleDecrypt() {
        if (selectedFileDecrypt == null || loadedPrivateKey == null) {
            status.setText("Selecciona archivo cifrado y clave privada.");
            return;
        }
        try {
            File keyFile = new File(selectedFileDecrypt.getParentFile(),
                    selectedFileDecrypt.getName().replaceAll("\\.enc$", "") + ".key.enc");
            File out = new File(selectedFileDecrypt.getParentFile(),
                    selectedFileDecrypt.getName().replaceAll("\\.enc$", "") + ".dec");

            HybridCipher.decryptFile(selectedFileDecrypt, keyFile, loadedPrivateKey, out);
            status.setText("Archivo descifrado correctamente: " + out.getName());
        } catch (Exception ex) {
            status.setText("Error descifrando: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}