@echo off
echo ========================================
echo Téléchargement du driver SQLite JDBC
echo ========================================

REM Créer le dossier lib s'il n'existe pas
if not exist "lib" mkdir lib

echo Téléchargement de sqlite-jdbc-3.40.1.0.jar...

REM Utiliser PowerShell pour télécharger le fichier
powershell -Command "& {Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.40.1.0/sqlite-jdbc-3.40.1.0.jar' -OutFile 'lib\sqlite-jdbc-3.40.1.0.jar'}"

if exist "lib\sqlite-jdbc-3.40.1.0.jar" (
    echo.
    echo ✓ Driver SQLite téléchargé avec succès dans lib\sqlite-jdbc-3.40.1.0.jar
    echo.
    echo Pour utiliser le driver, lancez l'application avec :
    echo java -cp "lib\sqlite-jdbc-3.40.1.0.jar;target\classes" main.Main
    echo.
    echo Ou utilisez Maven avec : mvn clean compile exec:java -Dexec.mainClass="main.Main"
) else (
    echo.
    echo ✗ Échec du téléchargement
    echo Vous pouvez télécharger manuellement depuis :
    echo https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.40.1.0/sqlite-jdbc-3.40.1.0.jar
)

pause
