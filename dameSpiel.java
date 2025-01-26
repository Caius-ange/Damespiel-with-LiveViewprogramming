Clerk.markdown(
    Text.fillOut(
   """

   # Dame-Spiel Programmierung


   _Caius-Ange Tawotchuen_ , _Technische Hochschule Mittelhessen_
   
   
   ## Inhaltsverzeichnis
   
1. [Allgemeine Einführung](#1-allgemeine-einfuehrung)
2. [Beschreibung der Hauptkomponenten](#2-beschreibung-der-hauptkomponenten)
    - [Spiellogik: Hauptklasse Spielfeld](#a-spiellogik-hauptklasse-spielfeld)
    - [Grafische Darstellung: Klasse DameView](#b-grafische-darstellung-klasse-dameview)
    - [Spielsteuerung: Klasse DameSpiel](#c-spielsteuerung-klasse-damespiel)
3. [Erklärung der wichtigsten Methoden](#3-erklärung-der-wichtigsten-methoden)
    - [Methoden in DameSpiel](#a-methoden-in-damespiel)
    - [Methoden in Spielfeld](#b-methoden-in-spielfeld)
    - [Methoden in DameView](#c-methoden-in-dameview)
4. [Datenstruktur und Spielmechanik](#4-datenstruktur-und-spielmechanik)
5. [KI und Minimax-Strategie](#5-ki-und-minimax-strategie)
6. [Ausführung und Benutzerinteraktion](#6-ausführung-und-benutzerinteraktion)
7. [Features](#7-features)
  
---
   
## 1. Allgemeine Einführung
Dieses Projekt simuliert ein Dame-Spiel mit zwei Hauptaspekten:  

**Spiellogik (Regeln, Bewegung, Punktestand)**: Implementiert in der Klasse Spielfeld.

**Grafische Darstellung (Brett und Spielfiguren)**: Implementiert in der Klasse DameView, die Turtle-Grafik verwendet.

**Steuerung und Integration**: Die Klasse DameSpiel verbindet die Spiellogik mit der Darstellung und ermöglicht Interaktionen.
   
## 2. Beschreibung der Hauptkomponenten
   
### a. Spiellogik: Hauptklasse Spielfeld
Zentrale Verwaltung des Spielfeldes und der Spielregeln.
#### Attribute :
**feld**: Ein 1D-Array, das die Felder des Spielfeldes darstellt (64 Felder). 

**spieler1, spieler2**: Die beiden Spieler.  

**turn**: Bestimmt, welcher Spieler am Zug ist (1 für weiß, -1 für schwarz).
   
#### Wichtige Methoden:
**initialisieren()**: Initialisiert das Spielfeld. 

**bewegenAusuehren(int startIndex, int zielIndex)**: Führt eine Bewegung aus. 
   
**steinBewegen(int startIndex, int zielIndex)**: Bewegt eine Spielfigur oder führt einen Schlagzug aus. 

**zeigeZugMoeglichkeit(int startIndex, int zielIndex)**: Listet alle möglichen Züge auf. 

**wechselSpieler()**: Wechselt den aktiven Spieler.  

**istSpielAmEnde()**: Überprüft, ob das Spiel beendet ist.
   
### b. Grafische Darstellung: Klasse DameView
Zeichnet das Spielfeld und die Spielfiguren mithilfe einer Turtle-Grafikbibliothek.

**Hauptfunktionen**:

-Zeichnen des Spielfelds mit hellbraunen und dunklen Quadraten.

-Darstellung von Spielfiguren (Steine und Damen) in schwarz und weiß. 

-Dynamische Aktualisierung des Bretts nach jeder Bewegung."""));
        
DameSpiel d = new DameSpiel();
    
Clerk.markdown(
    Text.fillOut(
   """
### c. Spielsteuerung: Klasse DameSpiel
Verbindet die Spiellogik (Spielfeld) mit der Darstellung (DameView). 

**Verantwortlich für**:  

-Initialisierung des Spiels.  

-Verarbeitung von Spielerbewegungen.  

-Aktualisierung der grafischen Darstellung basierend auf Änderungen im Spielfeld.
   
## 3. Erklärung der wichtigsten Methoden
 #### a.  Methoden in DameSpiel
**steinBewegenAusfuehren(int startIndex, int zielIndex)**:Führt eine Bewegung auf dem Spielfeld aus,ruft bewegenAusuehren aus Spielfeld auf, um die Logik zu validieren.
 und aktualisiert anschließend die grafische Darstellung durch druckeBrett
```java
${0}
```
""", Text.cutOut("./DameSpiel.java", "// a1")));

DameSpiel c = new DameSpiel();
  
Clerk.markdown(
    Text.fillOut(
        """
#### b. Methoden in Spielfeld
#### -Initialisierung
**initialisieren()**: Platziert die Figuren auf dem Spielfeld. Die ersten drei und letzten drei Reihen enthalten die jeweiligen Figuren.
#### -Bewegungsverwaltung

**bewegenAusuehren(int startIndex, int zielIndex)**: Überprüft und führt eine Bewegung aus.  
Wenn die KI am Zug ist, wird die Methode MinimaxSpieler.findeBestenZug(Spielfeld spielfeld) aufgerufen.
Handhabt auch Mehrfachschläge.
```java
${0}
```
""", Text.cutOut("./Spielfeld.java", "// a2")));

Clerk.markdown(
    Text.fillOut(
   """
#### -Schlagverwaltung
**steinSchlagen(int startIndex, int zielIndex)** : ist dafür verantwortlich, zu prüfen und auszuführen, ob eine Spielfigur (ein Stein oder eine Dame) einen gültigen Schlagzug durchführen kann. Ein Schlagzug bedeutet, dass eine gegnerische Figur übersprungen und entfernt wird.
```java
${0}
```
""", Text.cutOut("./Spielfeld.java", "// a3")));

Clerk.markdown(
    Text.fillOut(
   """
#### -Dame-Beförderung
**dameBeforderungPruefen(int zielIndex)**: Überprüft, ob eine Figur das gegenüberliegende Ende des Spielfelds erreicht und zur Dame befördert wird.
"""));
              
DameSpiel a = new DameSpiel();

Clerk.markdown(
    Text.fillOut(
   """
#### c. Methoden in DameView
Verwendet zeichneStein und zeichneKoenigen, um normale Figuren und Damen darzustellen.
#### -zeichneBrett(Turtle t, int quadratGroesse)
Zeichnet ein 8x8-Brett mit abwechselnd hellbraunen und dunklen Quadraten.
Setzt das Farbschema für die Quadrate: Hellbraun (255, 228, 196) und Dunkelbraun (139, 69, 19).
#### -zeichneStein(Turtle t, int index, int quadratGroesse, int r, int g, int b) und zeichneKoenigen(Turtle t, int index, int quadratGroesse, int r, int g, int b)
Zeichnen Spielfiguren als Kreise und Farbparameter definieren, ob die Figur schwarz oder weiß ist.
#### -druckeBrett(int[] brettZumDrucken)
Zeichnet das Spielfeld und die Spielfiguren basierend auf dem aktuellen Spielstatus.
Unterscheidet zwischen:
Schwarzen Figuren (-1, -2).
Weißen Figuren (1, 2).
```java
${0}
```
""", Text.cutOut("./DameView.java", "// a4")));

Clerk.markdown(
    Text.fillOut(
   """
## 4. KI und Minimax-Strategie
#### Der Minimax-Algorithmus:
Durchsucht alle möglichen Spielzüge bis zu einer vorgegebenen Tiefe (MAX_TIEFE).
Bewertet Spielzustände mithilfe der Methode bewerteStellung, wobei berücksichtigt wird:
Anzahl verbleibender Figuren.
Nähe zu Beförderungen.
Schutz durch verbündete Figuren.
- ### Klasse MinimaxSpieler
Implementiert die KI mithilfe des Minimax-Algorithmus.
#### Attribute:
**MAX_TIEFE**: Maximale Suchtiefe.
#### Wichtige Methoden:
**findeBestenZug(Spielfeld)**: Findet den besten möglichen Zug für die KI. 

**minimax(Spielfeld spielfeld, int tiefe, boolean istMaximierend, int alpha, int beta)**: Bewertet Spielzustände rekursiv.

**bewerteStellung(Spielfeld)**: Bewertet eine gegebene Spielstellung.
```java
${0}
```
""", Text.cutOut("./Spielfeld.java", "// a5")));

Clerk.markdown(
    Text.fillOut(
   """
## 5. Ausführung und Benutzerinteraktion
**Initialisierung**:
Die Klasse DameSpiel erstellt ein Spielfeld-Objekt und ein DameView-Objekt.
Das Spielfeld wird mit DameView gerendert.   

**Spielerbewegungen**:
Bewegungen werden in DameSpiel ausgelöst.
Die Spiellogik in Spielfeld überprüft die Gültigkeit und führt Bewegungen aus.
Nach jeder Bewegung aktualisiert DameView das Spielfeld grafisch.

## 6. Datenstruktur und Spielmechanik
**Spielfeld**: Ein 1D-Array (feld) mit 64 Feldern. Indizes von 0 bis 63 repräsentieren die Felder linear. 

**Figuren**:Positive Werte für weiße Figuren, negative Werte für schwarze Figuren.    

**Normale Steine** haben absolute Werte von 1 und **Damen** haben absolute Werte von 2.

"""));

Clerk.markdown(
    Text.fillOut(
   """
## 7. Features
### Die Klasse BestenPunktestandVerwalten
 verwaltet das Lesen, Schreiben und Aktualisieren des höchsten Punktestands eines Spiels durch Interaktion mit einer Textdatei, die in der Regel als "BestScore.txt" benannt ist. Bei ihrer Erstellung initialisiert sie den höchsten Punktestand (besterPunktestand) auf 0 und versucht, einen vorhandenen Punktestand über die Methode punktestandLesen() aus der Datei zu lesen. Wenn ein neuer Punktestand den aktuellen höchsten Punktestand übertrifft, aktualisiert die Methode punktestandSpeichern(int) die Datei und den Punktestand im Speicher. Die robuste Fehlerbehandlung stellt sicher, dass die Anwendung auch dann funktioniert, wenn die Datei fehlt oder beschädigt ist. Diese Klasse ist essenziell, um die höchsten Punktestände zwischen Spielsessions zu speichern und zu vergleichen.
```java
${0}
```
""", Text.cutOut("./Spielfeld.java", "// a6")));

Clerk.markdown(
    Text.fillOut(
   """
 ### Die Methode zeigeZugMoeglichkeit
 berechnet alle möglichen Züge für eine Spielfigur an einem bestimmten Index auf dem Spielfeld. Sie überprüft zunächst, ob der Index gültig ist und eine Figur auf dem angegebenen Feld steht. Danach unterscheidet sie zwischen normalen Steinen und Damen: Damen können sich diagonal in alle Richtungen bewegen, während normale Steine sich je nach Farbe nur vorwärts oder rückwärts diagonal bewegen können. Zusätzlich prüft die Methode auf Schlagzüge, bei denen gegnerische Figuren übersprungen werden können, falls das nächste Feld hinter dem Gegner frei ist. Die möglichen Zielpositionen werden gesammelt und als Array zurückgegeben. Bei ungültigen Eingaben wird eine IllegalArgumentException ausgelöst. Diese Methode dient zur Anzeige und Validierung von Zügen im Spiel.
 ```java
${0}
```
""", Text.cutOut("./Spielfeld.java", "// a7")));

public class DameSpiel {
    
    private Spielfeld spielfeld;
    private DameView view;


    public DameSpiel() {
        this.spielfeld = new Spielfeld();
        this.view = new DameView(this.spielfeld);
    }
    // a1
    public void steinBewegenAusfuehren(String startIndex, String zielIndex) {
        int start = konvertierePosition(startIndex );
        int ziel = konvertierePosition(zielIndex);
    
        spielfeld.bewegenAusfuehren(start, ziel);  
        this.view.druckeBrett(this.spielfeld);
    }

    public int konvertierePosition(String position) {
        if (position == null || position.length() < 2) {
            throw new IllegalArgumentException("Ungültige Position: " + position);
        }
        
        char spalteChar = position.charAt(0); // Buschstabe
        char zeileChar = position.charAt(1); // Zahl
        
        int spalte = Character.toLowerCase(spalteChar) - 'a';
        if (spalte < 0 || spalte >= 8) {
            throw new IllegalArgumentException("Ungültige Spalte: " + spalteChar);
        }
        
        int zeile = Character.getNumericValue(zeileChar) - 1;
        if (zeile < 0 || zeile >= 8) { 
            throw new IllegalArgumentException("Ungültige Zeile: " + zeileChar);
        }
        
        return zeile * 8 + spalte;
    }

    public String toString() {
        return spielfeld.toString();
    }
 // a1

}

