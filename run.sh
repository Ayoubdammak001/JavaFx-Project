#!/bin/bash

echo "========================================"
echo "Application de Dessin JavaFX"
echo "========================================"

# Vérifier si Maven est installé
if ! command -v mvn &> /dev/null; then
    echo "ERREUR: Maven n'est pas installé ou pas dans le PATH"
    echo "Veuillez installer Maven et réessayer"
    exit 1
fi

echo "Compilation et lancement de l'application..."
echo

# Compiler et lancer l'application
mvn clean compile javafx:run

if [ $? -ne 0 ]; then
    echo
    echo "ERREUR: Échec du lancement de l'application"
    echo "Vérifiez que JavaFX est correctement configuré"
    exit 1
fi

echo
echo "Application fermée"
