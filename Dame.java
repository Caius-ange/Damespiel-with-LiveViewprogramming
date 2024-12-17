import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class Dame {

}

enum Stein {
    WEISS_STEIN(1, 'w'),
    SCHWARTZ_STEIN(-1, 's'),
    WEISS_KOENIGEN(2, 'W'),
    SCHWARTZ_KOENIGEN(-2, 'S');

    private final int wert;
    private final char symbol;

    Stein(int wert, char symbol) {
        this.wert = wert;
        this.symbol = symbol;
    }

    public int getWert() {
        return wert;
    }

    public char getSymbol() {
        return symbol;
    }
}

class BestScoreTracken {
    int bestScore;
    String file = "bestScore.txt";

    BestScoreTracken(String file) {
        this.file = file;
        bestScore = 0;
        scoreLesen();
    }

    int scoreLesen() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String zeile = br.readLine();
            br.close();
            if (zeile != null && !zeile.isEmpty()) {
                bestScore = Integer.parseInt(zeile);
            } else {
                bestScore = 0;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return bestScore;

    }

    void scoreSpeichern(int newScore) {
        if (newScore > bestScore) {
            bestScore = newScore;

            try {
                BufferedWriter wr = new BufferedWriter(new FileWriter(file));
                wr.write(Integer.toString(bestScore));
                wr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    void scoreSpeichernSpieler(Spieler gewinner) {
        int newScore = gewinner.getPunkte();
        scoreSpeichern(newScore);
    }
}

class Spieler {
    private String name;
    private int punkte;
    private int anzahlStein;
    private Stein farbe; // Couleur des pions associés au joueur

    public Spieler(String name, Stein farbe) {
        this.name = name;
        this.punkte = 0;
        this.farbe = farbe; // Stein.WEISS_STEIN ou Stein.SCHWARTZ_STEIN
        this.anzahlStein = 0;
    }

    public int getPunkte() {
        return punkte;
    }

    public Stein getFarbe(){
        return farbe;
    }

    public int getAnzahlStein() {
        return anzahlStein;
    }

    void setAnzahlStein(int anzahlStein) {
        this.anzahlStein = anzahlStein;
    }

    public void erhoehePunkte(int punkte) {
        this.punkte += 2;
    }

    @Override
    public String toString() {
        return name + " " + "(" + farbe.getSymbol() + ") -- Punkte : " + punkte + " " + "-- Anzahlstein: "
                + anzahlStein;
    }
}

class Spielfeld {
    private final int[] feld;
    private Spieler spieler1;
    private Spieler spieler2;
    private Spieler aktuellerSpieler;

    public Spielfeld() {
        feld = new int[64];
        spieler1 = new Spieler("spieler1", Stein.WEISS_STEIN);
        spieler2 = new Spieler("spieler2", Stein.SCHWARTZ_STEIN);
        aktuellerSpieler = spieler1;
        initialisieren();
    }

    private void initialisieren() {
        spieler1.setAnzahlStein(12);
        spieler2.setAnzahlStein(12);
        for (int i = 0; i < 64; i++) {
            if (i / 8 < 3 && (i + i / 8) % 2 == 1) {
                feld[i] = Stein.WEISS_STEIN.getWert();
            } else if (i / 8 > 4 && (i + i / 8) % 2 == 1) {
                feld[i] = Stein.SCHWARTZ_STEIN.getWert();
            } else {
                feld[i] = 0; // Leeres Feld
            }
        }
    }

    boolean spielen(int startIndex, int zielIndex) {
        if (istSpielAmEnde()) {
            System.out.println("Spiel ist vorbei! Gewinner: " + getGewinner());
            return false;
        }

        steinBewegen(startIndex, zielIndex);
        wechselSpieler(); // Change de joueur après chaque mouvement
        return true;
    }

    public void steinBewegen(int startIndex, int zielIndex) {
        if (!istImSpielfeld(startIndex) || !istImSpielfeld(zielIndex)) {
            throw new IllegalArgumentException("Start- oder Zielindex ist außerhalb des Spielfelds.");
        }

        int stein = feld[startIndex];

        // Vérifie si le joueur actif contrôle le pion qu'il essaie de déplacer
        if (!istSteinDesSpielers(aktuellerSpieler, stein)) {
            throw new IllegalArgumentException("Sie sind nicht dran oder das ausgewaelte Feld ist falsch .");
        }

        // Vérifie la validité du mouvement
        int[] moeglicheZuege = zeigeZugMoeglichkeit(startIndex);
        boolean gueltig = false;
        for (int zug : moeglicheZuege) {
            if (zug == zielIndex) {
                gueltig = true;
                break;
            }
        }

        if (!gueltig) {
            throw new IllegalArgumentException("Der Zug ist nicht erlaubt.");
        }

        if (Math.abs(startIndex - zielIndex) > 9) {
            steinschlagen(startIndex, zielIndex);
        } else {
            // Déplacement simple
            feld[zielIndex] = feld[startIndex];
            feld[startIndex] = 0;
        }

        // Promotion en dame
        if ((zielIndex / 8 == 0 && stein == Stein.SCHWARTZ_STEIN.getWert()) ||
                (zielIndex / 8 == 7 && stein == Stein.WEISS_STEIN.getWert())) {
            feld[zielIndex] = stein > 0 ? Stein.WEISS_KOENIGEN.getWert() : Stein.SCHWARTZ_KOENIGEN.getWert();
        }

    }

    int[] zeigeZugMoeglichkeit(int index) {
        ArrayList<Integer> moeglicheBewegung = new ArrayList<>();

        // Vérification si l'index est dans le plateau
        if (!istImSpielfeld(index)) {
            throw new IllegalArgumentException("Der ausgewählte Stein ist außerhalb des Spielfelds.");
        }

        int stein = feld[index];

        // Vérification si la case contient un pion
        if (stein == 0) {
            throw new IllegalArgumentException("Das ausgewaelteFeld ist leer.");
        }

        boolean istWeiss = stein > 0; // Détecte si c'est un pion blanc
        boolean istKoenigen = Math.abs(stein) == 2; // Vérifie si c'est une dame

        if (istKoenigen) {
            // Logique pour les dames
            int[] diagonalen = { 9, 7, -9, -7 }; // Diagonales dans toutes les directions
            for (int diagonale : diagonalen) {
                int zielIndex = index + diagonale;
                while (istImSpielfeld(zielIndex) && feld[zielIndex] == 0
                        && istGleicheZeileOderDiagonal(index, zielIndex)) {
                    moeglicheBewegung.add(zielIndex);
                    // Continue d'explorer dans cette direction
                    zielIndex += diagonale;
                }

                // Vérifie si un ennemi est présent sur la case suivante
                int gegnerIndex = zielIndex;
                zielIndex += diagonale;
                if (istImSpielfeld(gegnerIndex) && istImSpielfeld(zielIndex)
                        && feld[gegnerIndex] * stein < 0 && feld[zielIndex] == 0) {
                    moeglicheBewegung.add(zielIndex);
                }
            }
        } else {
            // Logique pour les pions normaux
            int richtung = istWeiss ? 8 : -8; // Blancs : avancent vers le bas (+8), Noirs : avancent vers le haut (-8)

            // Déplacements possibles selon la couleur
            int[] diagonalen = { richtung - 1, richtung + 1 }; // Gauche et droite en diagonale

            for (int diagonale : diagonalen) {
                int zielIndex = index + diagonale;

                // Vérification des conditions de validité
                if (istImSpielfeld(zielIndex) // Le déplacement reste dans le plateau
                        && feld[zielIndex] == 0 // La case cible est vide
                        && ((zielIndex / 8 + zielIndex % 8) % 2 == 1) // La case cible est noire
                        && istGleicheZeileOderDiagonal(index, zielIndex)) { // Garde la validité de la diagonale
                    moeglicheBewegung.add(zielIndex);
                }
            }

            int[] diagonalenSchlag = { richtung - 1, richtung + 1 };
            for (int diagonale : diagonalenSchlag) {
                int gegnerIndex = index + diagonale;
                int zielIndex = index + 2 * diagonale;

                if (istImSpielfeld(gegnerIndex) && istImSpielfeld(zielIndex) // Déplacement valide
                        && feld[gegnerIndex] != 0 && feld[gegnerIndex] * stein < 0 // Ennemi à capturer
                        && feld[zielIndex] == 0 // Case après l'ennemi est libre
                        && ((zielIndex / 8 + zielIndex % 8) % 2 == 1)) { // Case noire
                    moeglicheBewegung.add(zielIndex);
                }
            }
        }
        return moeglicheBewegung.stream().mapToInt(i -> i).toArray();

    }

    public boolean steinschlagen(int startIndex, int zielIndex) {
        if (!istImSpielfeld(startIndex) || !istImSpielfeld(zielIndex)) {
            throw new IllegalArgumentException("Start- oder Zielindex ist außerhalb des Spielfelds.");
        }

        int stein = feld[startIndex];
        if (stein == 0) {
            throw new IllegalArgumentException("Das Startfeld ist leer.");
        }

        // Vérifier la case entre le départ et l'arrivée
        int gegnerIndex = (startIndex + zielIndex) / 2;

        if (feld[gegnerIndex] != 0 && feld[gegnerIndex] * stein < 0 && feld[zielIndex] == 0) {
            // Effectuer la capture
            feld[zielIndex] = feld[startIndex]; // Déplacer le pion
            feld[startIndex] = 0; // Vider la case de départ
            feld[gegnerIndex] = 0; // Supprimer le pion capturé

            // Décrémenter le nombre de pions du joueur adverse
            Spieler gegner = getGegnerSpieler();
            gegner.setAnzahlStein(gegner.getAnzahlStein() - 1);

            // Augmenter les points du joueur actuel
            aktuellerSpieler.erhoehePunkte(2);

            // Vérifier les captures supplémentaires possibles
            int[] diagonalen = { 8 - 1, 8 + 1, -8 - 1, -8 + 1 };
            for (int diagonale : diagonalen) {
                int neuerZielIndex = zielIndex + 2 * diagonale;
                int neuerGegnerIndex = zielIndex + diagonale;

                if (istImSpielfeld(neuerZielIndex) && istImSpielfeld(neuerGegnerIndex)
                        && feld[neuerGegnerIndex] != 0 && feld[neuerGegnerIndex] * stein < 0
                        && feld[neuerZielIndex] == 0) {
                    // Capture en chaîne possible
                    steinschlagen(zielIndex, neuerZielIndex); // Appel récursif
                }
            }
            return true;
        }

        return false; // Capture invalide
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Aktueller Spieler : ").append(aktuellerSpieler.toString()).append("\n");
        for (int i = 0; i < 64; i++) {
            // builder.append(i);
            if (i % 8 == 0 && i != 0) {
                builder.append("\n");
            }
            // builder.append(i);
            char symbol;
            switch (feld[i]) {
                case 1:
                    symbol = Stein.WEISS_STEIN.getSymbol();
                    break;
                case -1:
                    symbol = Stein.SCHWARTZ_STEIN.getSymbol();
                    break;
                case 2:
                    symbol = Stein.WEISS_KOENIGEN.getSymbol();
                    break;
                case -2:
                    symbol = Stein.SCHWARTZ_KOENIGEN.getSymbol();
                    break;
                default:
                    symbol = '.'; // Leeres Feld
            }
            builder.append(symbol).append(' ');
        }
        return builder.toString();
    }

    boolean istImSpielfeld(int index) {
        return index >= 0 && index < 64;
    }

    /*
     * int[] indexZuKoordinaten(int index) {
     * if (!istImSpielfeld(index)) {
     * throw new IllegalArgumentException("Index ist außerhalb des Spielfelds.");
     * }
     * 
     * int y = index % 8; // Colonne (0 à 7)
     * int x = index / 8; // Ligne (0 à 7)
     * 
     * return new int[] { x, y };
     * }
     */
    /*
     * int koordinatenZuIndex(int x, int y) {
     * if (x < 0 || x >= 8 || y < 0 || y >= 8) {
     * throw new
     * IllegalArgumentException("Koordinaten sind außerhalb des Spielfelds.");
     * }
     * 
     * return x * 8 + y;
     * }
     */

    boolean istSpielAmEnde() {
        return spieler1.getAnzahlStein() == 0 || spieler2.getAnzahlStein() == 0;
    }

    // Méthode auxiliaire : Vérifie si le déplacement reste dans une même ligne
    // diagonale valide
    private boolean istGleicheZeileOderDiagonal(int start, int ziel) {
        int startZeile = start / 8;
        int zielZeile = ziel / 8;

        return Math.abs(startZeile - zielZeile) == 1; // Doit être une seule ligne plus haut ou plus bas
    }

    private boolean istSteinDesSpielers(Spieler spieler, int stein) {
        // Vérifie si le joueur contrôle le pion
        if (spieler.getFarbe() == Stein.WEISS_STEIN) {
            return stein > 0; // Pions blancs ou dames blanches
        } else {
            return stein < 0; // Pions noirs ou dames noires
        }
    }

    public Spieler getAktuellerSpieler() {
        return aktuellerSpieler;
    }

    public void wechselSpieler() {
        aktuellerSpieler = (aktuellerSpieler == spieler1) ? spieler2 : spieler1;
    }

    private Spieler getGegnerSpieler() {
        return (aktuellerSpieler == spieler1) ? spieler2 : spieler1;
    }

    Spieler getGewinner() {
        if (spieler1.getAnzahlStein() == 0) {
            return spieler2;
        } else {
            return spieler1;
        }
    }

}
