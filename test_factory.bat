@echo off
echo === Test du Pattern Factory Method ===
cd /d "c:\Users\Lenovo\Desktop\untitled"

echo Compilation des classes...
javac -cp "." src/factory/*.java src/model/*.java src/test/TestFactoryPattern.java

if %errorlevel% equ 0 (
    echo Compilation réussie !
    echo.
    echo Exécution du test...
    java -cp "." test.TestFactoryPattern
) else (
    echo Erreur de compilation !
)

pause
