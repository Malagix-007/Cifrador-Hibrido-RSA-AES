@echo off
title Generando JAR para Cifrador Hibrido
echo ---------------------------------------
echo 1. Limpiando y Compilando...
echo ---------------------------------------

if not exist "out" mkdir "out"

:: Compilamos referenciando tu carpeta 'javafx/lib' y 'libs'
javac --module-path "javafx/lib" --add-modules=javafx.controls,javafx.fxml ^
 -cp "libs/mysql-connector-j-9.5.0.jar;." -d "out" ^
 "src/com/cifrador/utils/AESUtil.java" ^
 "src/com/cifrador/utils/RSAUtil.java" ^
 "src/com/cifrador/encryptor/HybridCipher.java" ^
 "src/com/cifrador/database/DatabaseHelper.java" ^
 "src/com/cifrador/ui/MainApp.java"

if %errorlevel% neq 0 (
  echo ❌ Error en compilacion. Revisa tu codigo.
  pause
  exit /b
)

echo.
echo ---------------------------------------
echo 2. Creando MANIFEST y JAR...
echo ---------------------------------------
:: Apuntamos al jar de mysql en la carpeta libs/
echo Main-Class: com.cifrador.ui.MainApp > manifest.txt
echo Class-Path: libs/mysql-connector-j-9.5.0.jar >> manifest.txt

:: Creamos el jar en la raiz
jar cvfm CifradorApp.jar manifest.txt -C out .

del manifest.txt
echo.
echo ✅ EXITO: CifradorApp.jar creado en la raiz.
echo Ahora abre Launch4j.
pause