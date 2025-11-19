package com.cifrador.database;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Properties;

public class DatabaseHelper {
    private final String url;
    private final Properties props;

    public DatabaseHelper(String host, int port, String dbName, String user, String password) {
        this.url = String.format("jdbc:mysql://%s:%d/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", host, port, dbName);

        this.props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", password);
    }

    private Connection getConn() throws SQLException {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }
    return DriverManager.getConnection(url, props);
}


    public void insertOperacion(String nombreArchivo, String claveAesCifradaBase64, String usuario) throws SQLException {
        String sql = "INSERT INTO operaciones (nombre_archivo, fecha, clave_aes_cifrada, usuario) VALUES (?, ?, ?, ?)";
        try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nombreArchivo);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC"))));
            ps.setString(3, claveAesCifradaBase64);
            ps.setString(4, usuario);
            ps.executeUpdate();
        }
    }
}
