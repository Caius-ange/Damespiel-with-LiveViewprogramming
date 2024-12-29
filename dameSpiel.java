Clerk.markdown(
    Text.fillOut(
   """
# Dame-Spiel Programmierung


_Caius-Ange Tawotchuen_ , _Technische Hochschule Mittelhessen_


## Inhaltsverzeichnis

1. Allgemeine Einführung
2. Beschreibung der Hauptkomponenten
- Spiellogik: Spielfeld
- Grafische Darstellung: DameView
- Spielsteuerung: DameSpiel
3. Erklärung der wichtigsten Methoden
- Methoden in DameSpiel
- Methoden in Spielfeld
- Methoden in DameView
4. Datenstruktur und Spielmechanik
5. KI und Minimax-Strategie
6. Ausführung und Benutzerinteraktion

### 1. Allgemeine Einführung
Dieses Projekt simuliert ein Dame-Spiel mit zwei Hauptaspekten:  
**Spiellogik (Regeln, Bewegung, Punktestand)**: Implementiert in der Klasse Spielfeld.  
**Grafische Darstellung (Brett und Spielfiguren)**: Implementiert in der Klasse DameView, die Turtle-Grafik verwendet.  
**Steuerung und Integration**: Die Klasse DameSpiel verbindet die Spiellogik mit der Darstellung und ermöglicht Interaktionen.

## 2. Beschreibung der Hauptkomponenten

- ### Spiellogik: Hauptklasse Spielfeld
Zentrale Verwaltung des Spielfeldes und der Spielregeln.
#### Attribute :
**feld**: Ein 1D-Array, das die Felder des Spielfeldes darstellt (64 Felder).  
**spieler1, spieler2**: Die beiden Spieler.  
**turn**: Bestimmt, welcher Spieler am Zug ist (1 für weiß, -1 für schwarz).

#### Wichtige Methoden:
**initialisieren()**: Initialisiert das Spielfeld.  
**bewegenAusuehren(int startIndex, int zielIndex)**: Führt eine Bewegung aus.

```java
${0}
```

""", Text.cutOut("./Spielfeld.java", "// bewegenAusfuehren")));
  

public class DameSpiel {
    
    private Spielfeld spielfeld;
    private DameView view;
    

    public DameSpiel() {
        this.spielfeld = new Spielfeld();
        this.view = new DameView(this.spielfeld.getFeld());
        
    }

    public void steinBewegenAusfuehren(int startIndex, int zielIndex, boolean c) {

        spielfeld.bewegenAusfuehren(startIndex, zielIndex);

        // Recuperer les informations sur les joueurs
        int spPunkte = this.spielfeld.getSpieler1().getPunkte(); 
        int spSteine = this.spielfeld.getSpieler1().getAnzahlStein();
        int kiPunkte = this.spielfeld.getSpieler2().getPunkte(); 
        int kiSteine = this.spielfeld.getSpieler2().getAnzahlStein();

        

        // Afficher le damier
        this.view.druckeBrett(this.spielfeld.getFeld(), spPunkte, spSteine, kiPunkte, kiSteine, c, this.spielfeld.getGewinner().getName());
    }
}
