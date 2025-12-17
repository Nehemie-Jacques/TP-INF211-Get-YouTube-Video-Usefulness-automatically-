TP-INF211 - YouTube Video Usefulness Analyzer

Ce dépôt contient trois implémentations (un exemple pédagogique) :
- c++/ : code C++
- java/ : code Java
- python/ : code Python

But simple d'utilisation

C++
-----
1. Placez-vous dans le dossier `c++` :

   cd c++

2. Compilez le fichier (exemple) :

   g++ -std=c++11 youtube_analyzer.cpp -o youtube_analyzer

3. Exécutez :

   ./youtube_analyzer

Java
----
1. Placez-vous dans le dossier `java` :

   cd java

2. Compilez tous les fichiers `.java` du dossier :

   javac *.java

   - Si votre programme principal est dans une classe nommée `YouTubeAnalyzerSystem`, lancez :

     java YouTubeAnalyzerSystem

   - Si le fichier principal se nomme autrement (par exemple `Main`), remplacez `YouTubeAnalyzerSystem` par le nom de la classe contenant `public static void main(String[] args)`.

Python
------
1. Placez-vous dans le dossier `python` :

   cd python

2. Exécutez le script :

   python3 youtube_analyzer.py

Remarques
--------
- Pour Java, si `javac` ou `java` ne sont pas trouvés, installez un JDK (ex. OpenJDK 17) :

  sudo apt update
  sudo apt install -y openjdk-17-jdk-headless

- Pour C++ vous avez besoin de `g++` (généralement fourni par `build-essential` sur Debian/Ubuntu) :

  sudo apt update
  sudo apt install -y build-essential
