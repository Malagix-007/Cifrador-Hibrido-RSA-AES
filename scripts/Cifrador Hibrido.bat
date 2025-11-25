@echo off
title Cifrador Hibrido RSA-AES - Ejecutor Portatil
color 0A

:: RUTA DEL PROYECTO (puedes moverla si cambia de lugar)
set "PROJECT_PATH=C:\Users\malag\OneDrive\Desktop\Carpetas escritorio\ITSSAT\Decimo Primer Semestre\Criptografia\CifradorHibrido"

:: VERIFICAR QUE LA RUTA EXISTE
if not exist "%PROJECT_PATH%" (
    echo ERROR: No se encuentra la carpeta del proyecto.
    echo Ruta configurada: %PROJECT_PATH%
    echo Verifica si moviste tu carpeta o cambia la ruta en este .bat
    pause
    exit /b
)

cd /d "%PROJECT_PATH%"

:: VARIABLES OBLIGATORIAS
set "JAVAFX=C:\Users\malag\OneDrive\Desktop\Carpetas escritorio\Aplicaciones y programas\openjfx-21.0.9_windows-x64_bin-sdk\javafx-sdk-21.0.9\lib"
set "MYSQLJAR=libs\mysql-connector-j-9.5.0.jar"
set "SRC=src"
set "OUT=out"

:: MENU INTERACTIVO
:MENU
cls
echo ============================================================
echo  Cifrador Hibrido RSA-AES - EJECUTABLE PORTATIL
echo ============================================================
echo.
echo    [1] Compilar el proyecto
echo    [2] Ejecutar la aplicacion
echo    [3] Compilar y Ejecutar
echo    [4] Salir
echo.
set /p opcion=" Escribe una opcion y presiona ENTER: "

if "%opcion%"=="1" goto COMPILAR
if "%opcion%"=="2" goto EJECUTAR
if "%opcion%"=="3" goto COMPILE_RUN
if "%opcion%"=="4" exit
goto MENU

:COMPILAR
echo ============================================================
echo Compilando...
echo ============================================================

if not exist "%OUT%" mkdir "%OUT%"

javac --module-path "%JAVAFX%" --add-modules=javafx.controls,javafx.fxml ^
 -cp "%MYSQLJAR%;." -d "%OUT%" ^
 "%SRC%\com\cifrador\utils\AESUtil.java" ^
 "%SRC%\com\cifrador\utils\RSAUtil.java" ^
 "%SRC%\com\cifrador\encryptor\HybridCipher.java" ^
 "%SRC%\com\cifrador\database\DatabaseHelper.java" ^
 "%SRC%\com\cifrador\ui\MainApp.java"

if %errorlevel% neq 0 (
  echo Error: Fallo la compilacion.
  pause
  goto MENU
)

echo Compilacion exitosa.
pause
goto MENU


:EJECUTAR
echo ============================================================
echo Ejecutando la aplicacion...
echo ============================================================

java --module-path "%JAVAFX%" --add-modules=javafx.controls,javafx.fxml ^
 -cp "%OUT%;%MYSQLJAR%" com.cifrador.ui.MainApp

echo ------------------------------------------------------------
pause
goto MENU


:COMPILE_RUN
call :COMPILAR
call :EJECUTAR
goto MENU
