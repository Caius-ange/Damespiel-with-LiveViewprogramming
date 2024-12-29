import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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

class Spieler {
    private String name;
    private int punkte;
    private int anzahlStein;
    private boolean isKI;
    private Stein farbe; // Couleur des pions associés au joueur

    public Spieler(String name, Stein farbe, boolean isKI) {
        this.name = name;
        this.isKI = isKI;
        this.punkte = 0;
        this.farbe = farbe; // Stein.WEISS_STEIN ou Stein.SCHWARTZ_STEIN
        this.anzahlStein = 0;
    }

    public String getName() {
        return name;
    }

    public int getPunkte() {
        return punkte;
    }

    public Stein getFarbe() {
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

    public boolean isKI() {
        return isKI;
    }

    public void setKI(boolean isKI) {
        this.isKI = isKI;
    }

    @Override
    public String toString() {
        return name + " " + "(" + farbe.getSymbol() + ") -- Punkte : " + punkte + " " + "-- Anzahlstein: "
                + anzahlStein + " -- KI: " + (isKI ? "Ja" : "Nein");
    }
}

public class Spielfeld {
    private final int[] feld;
    private Spieler spieler1;
    private Spieler spieler2;
    private int turn; // 1 pour blanc, -1 pour noir


    public Spielfeld() {
        spieler1 = new Spieler("spieler1", Stein.WEISS_STEIN, false);
        spieler2 = new Spieler("spieler2", Stein.SCHWARTZ_STEIN, true);
        turn = 1; // Le joueur blanc commence
       // initialisieren();
       feld = initialisieren();
    }

    public int[] initialisieren() {
        spieler1.setAnzahlStein(12);
        spieler2.setAnzahlStein(12);

        int[] feld = new int[64];
        for (int i = 0; i < 64; i++) { 
            if (i / 8 < 3 && (i + i / 8) % 2 == 1) {
                feld[i] = Stein.WEISS_STEIN.getWert();
            } else if (i / 8 > 4 && (i + i / 8) % 2 == 1) {
                feld[i] = Stein.SCHWARTZ_STEIN.getWert();
            } else {
                feld[i] = 0; // Leeres Feld
            }
        }
        return feld;
    }

    // bewegenAusfuehren
    public boolean bewegenAusfuehren(int startIndex, int zielIndex) {
        // Vérifier si le jeu est terminé
        if (istSpielAmEnde()) {
            System.out.println("Das Spiel ist bereits beendet. Gewinner: " + getGewinner());
            BestenPunktestandVerwalten best = new BestenPunktestandVerwalten("bestScore.txt");
            int besterPunktestand = best.punktestandLesen();

            if (getGewinner().getPunkte() > besterPunktestand) {
                best.punktestandSpeichernSpieler(getGewinner());
                System.out.println("Herzlichen Glueckwunsch, Sie haben den besten aktuellen Punktestand!");
            }
            return false;
        }

        // Si c'est le tour de l'IA
        if (getAktuellerSpieler().isKI()) {
            System.out.println("KI ist dran...");
            int[] besterZug = MinimaxSpieler.findeBestenZug(this);

            if (besterZug != null) {
                try {
                    steinBewegen(besterZug[0], besterZug[1]);
                    System.out.println("KI zieht von " + besterZug[0] + " nach " + besterZug[1]);
                    wechselSpieler();
                    
                   // cal toString() method
                   System.out.println(this);
                    return true;
                } catch (IllegalArgumentException e) {
                    System.out.println("KI-Fehler: " + e.getMessage());
                    return false;
                }
            } else {
                System.out.println("Die KI kann keinen gueltigen Zug finden.");
                
                return false;
            }
        }
        // Si c'est le tour du joueur humain
        else {
            try {
                steinBewegen(startIndex, zielIndex);
                wechselSpieler();

                // Après le mouvement du joueur humain, faire jouer l'IA si c'est son tour
                if (!istSpielAmEnde() && getAktuellerSpieler().isKI()) {
                    return bewegenAusfuehren(0, 0); // Les paramètres sont ignorés pour l'IA
                }

                // cal toString() method
                System.out.println(this);
                return true;
            } catch (IllegalArgumentException e) {
                System.out.println("Ungueltiger Zug: " + e.getMessage());
                return false;
            }
        }
    }
    // bewegenAusfuehren


    public void steinBewegen(int startIndex, int zielIndex) {
        if (!istImSpielfeld(startIndex) || !istImSpielfeld(zielIndex)) {
            throw new IllegalArgumentException("Start- oder Zielindex ist außerhalb des Spielfelds.");
        }

        int stein = feld[startIndex];
        if (!istSteinDesSpielers(stein)) {
            throw new IllegalArgumentException("Sie sind nicht dran oder das ausgewaelte Feld ist falsch.");
        }

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

        // Zuerst einen Schlagzug versuchen
        boolean schlag = steinSchlagen(startIndex, zielIndex);

        // Wenn kein Schlag möglich ist, normale Bewegung durchführen
        if (!schlag) {
            feld[zielIndex] = feld[startIndex];
            feld[startIndex] = 0;
        }

        // Überprüfung auf Beförderung zur Dame
        dameBeforderungPruefen(zielIndex);
    }

    public boolean steinSchlagen(int startIndex, int zielIndex) {
        int stein = feld[startIndex];
        boolean istDame = Math.abs(stein) == 2;
        boolean schlag = false;

        if (istDame) {
            schlag = dameSchlagen(startIndex, zielIndex);
        } else {
            schlag = normalerSteinSchlagen(startIndex, zielIndex);
        }

        return schlag;
    }

    private boolean dameSchlagen(int startIndex, int zielIndex) {
        int stein = feld[startIndex];
        // Calculer la direction du mouvement
        int deltaX = (zielIndex / 8) - (startIndex / 8);
        int deltaY = (zielIndex % 8) - (startIndex % 8);
        // Normaliser pour obtenir les pas unitaires
        int schrittX = Integer.compare(deltaX, 0);
        int schrittY = Integer.compare(deltaY, 0);
        int richtung = schrittX * 8 + schrittY;

        int gegnerPosition = -1;
        int aktuell = startIndex + richtung;

        // Position des zu schlagenden Steins finden
        while (aktuell != zielIndex) {
            if (feld[aktuell] != 0) {
                if (gegnerPosition == -1 && feld[aktuell] * stein < 0) {
                    gegnerPosition = aktuell;
                } else {
                    return false; // Mehr als ein Stein auf dem Weg
                }
            }
            aktuell += richtung;
        }

        // Wenn ein gegnerischer Stein gefunden wurde und das Zielfeld leer ist
        if (gegnerPosition != -1 && feld[zielIndex] == 0) {
            // Effectuer la capture
            schlagAusfuehren(startIndex, zielIndex, gegnerPosition);
            mehrfachschlaegePruefen(zielIndex);

            return true;
        }

        return false;
    }

    private boolean normalerSteinSchlagen(int startIndex, int zielIndex) {
        int stein = feld[startIndex];

        // Calculer la différence entre la index de départ et d'arrivée
        int deltaIndex = zielIndex - startIndex;

        // Vérifier si le mouvement est une capture (distance de 2 cases en diagonal)
        if (Math.abs(deltaIndex) == 14 || Math.abs(deltaIndex) == 18) {
            // Calculer la index du pion à capturer
            int gegnerIndex = startIndex + (deltaIndex / 2);

            // Vérifier si :
            // 1. Le mouvement est sur une diagonale valide
            // 2. La case cible est vide
            // 3. Il y a un pion adverse à capturer
            // 4. Le mouvement reste dans les limites du plateau
            if (istGueltigeDiagonale(startIndex, zielIndex) &&
                    feld[zielIndex] == 0 &&
                    feld[gegnerIndex] != 0 &&
                    feld[gegnerIndex] * stein < 0) {

                schlagAusfuehren(startIndex, zielIndex, gegnerIndex);
                mehrfachschlaegePruefen(zielIndex);
                return true;
            }
        }

        return false;
    }

    private void schlagAusfuehren(int startIndex, int zielIndex, int gegnerPosition) {
        // Bewegung durchführen
        feld[zielIndex] = feld[startIndex];
        feld[startIndex] = 0;
        feld[gegnerPosition] = 0;

        // Punktestand und Steinanzahl aktualisieren
        Spieler gegner = getGegnerSpieler();
        gegner.setAnzahlStein(gegner.getAnzahlStein() - 1);
        getAktuellerSpieler().erhoehePunkte(2);
    }

    private void mehrfachschlaegePruefen(int index) {
        int stein = feld[index];
        int[] diagonalen = { 8 - 1, 8 + 1, -8 - 1, -8 + 1 };

        for (int diagonale : diagonalen) {
            int neuesZielIndex = index + 2 * diagonale;
            int gegnerIndex = index + diagonale;

            if (istImSpielfeld(neuesZielIndex) && istImSpielfeld(gegnerIndex)
                    && feld[gegnerIndex] != 0 && feld[gegnerIndex] * stein < 0
                    && feld[neuesZielIndex] == 0) {
                steinSchlagen(index, neuesZielIndex);
            }
        }
    }

    // Promotion en Dame
    private void dameBeforderungPruefen(int zielIndex) {
        int stein = feld[zielIndex];
        if ((zielIndex / 8 == 0 && stein == Stein.SCHWARTZ_STEIN.getWert()) ||
                (zielIndex / 8 == 7 && stein == Stein.WEISS_STEIN.getWert())) {
            feld[zielIndex] = stein > 0 ? Stein.WEISS_KOENIGEN.getWert() : Stein.SCHWARTZ_KOENIGEN.getWert();
        }
    }

    public int[] zeigeZugMoeglichkeit(int index) {
        ArrayList<Integer> moeglicheBewegung = new ArrayList<>();
        // Vérification si l'index est dans le plateau

        if (!istImSpielfeld(index)) {
            throw new IllegalArgumentException("Der ausgewaehlte Stein ist außerhalb des Spielfelds.");
        }

        int stein = feld[index];
        // Vérification si la case contient un pion

        if (stein == 0) {
            throw new IllegalArgumentException("Das ausgewaelte Feld ist leer.");
        }

        boolean istKoenigen = Math.abs(stein) == 2;

        if (istKoenigen) {
            // Directions diagonales pour la dame (-9: haut-gauche, -7: haut-droite, 7:
            // bas-gauche, 9: bas-droite)
            int[] diagonalen = { -9, -7, 7, 9 };

            for (int diagonale : diagonalen) {
                int zielIndex = index + diagonale;
                int schritte = 1;
                boolean gefundenGegner = false;

                while (istImSpielfeld(zielIndex) && istGueltigeDiagonale(index, zielIndex)) {
                    if (feld[zielIndex] == 0) {
                        // Case vide - mouvement possible si on n'a pas encore trouvé d'adversaire
                        if (!gefundenGegner) {
                            moeglicheBewegung.add(zielIndex);
                        }
                    } else if (feld[zielIndex] * stein < 0) {
                        // Pion adverse trouvé
                        if (!gefundenGegner) {
                            gefundenGegner = true;
                            // Vérifier la case suivante pour la capture
                            int nextIndex = zielIndex + diagonale;
                            if (istImSpielfeld(nextIndex) && feld[nextIndex] == 0
                                    && istGueltigeDiagonale(zielIndex, nextIndex)) {
                                moeglicheBewegung.add(nextIndex);
                            }
                        }
                        break;
                    } else {
                        // Pion allié trouvé
                        break;
                    }
                    schritte++;
                    zielIndex = index + schritte * diagonale;
                }
            }
        } else {
            // Code pour les pions normaux
            boolean istWeiss = stein > 0;
            int richtung = istWeiss ? 8 : -8;
            // Déplacements possibles selon la couleur

            int[] diagonalen = { richtung - 1, richtung + 1 };

            for (int diagonale : diagonalen) {
                int zielIndex = index + diagonale;
                // Vérification des conditions de validité

                if (istImSpielfeld(zielIndex)
                        && feld[zielIndex] == 0
                        && ((zielIndex / 8 + zielIndex % 8) % 2 == 1)
                        && istGleicheZeileOderDiagonal(index, zielIndex)) {
                    moeglicheBewegung.add(zielIndex);
                }
            }

            // Vérification des captures possibles
            int[] diagonalenSchlag = { richtung - 1, richtung + 1 };
            for (int diagonale : diagonalenSchlag) {
                int gegnerIndex = index + diagonale;
                int zielIndex = index + 2 * diagonale;

                if (istImSpielfeld(gegnerIndex) && istImSpielfeld(zielIndex)
                        && feld[gegnerIndex] != 0 && feld[gegnerIndex] * stein < 0
                        && feld[zielIndex] == 0
                        && ((zielIndex / 8 + zielIndex % 8) % 2 == 1)) {
                    moeglicheBewegung.add(zielIndex);
                }
            }
        }

        return moeglicheBewegung.stream().mapToInt(i -> i).toArray();
    }

    // Méthode auxiliaire mise à jour pour vérifier la validité d'une diagonale
    private boolean istGueltigeDiagonale(int start, int ziel) {
        int startZeile = start / 8;
        int startSpalte = start % 8;
        int zielZeile = ziel / 8;
        int zielSpalte = ziel % 8;

        int deltaZeile = Math.abs(zielZeile - startZeile);
        int deltaSpalte = Math.abs(zielSpalte - startSpalte);

        // Le mouvement doit être diagonal (même distance en horizontal et vertical)
        // et la case d'arrivée doit être sur une case noire (somme des coordonnées
        // impaire)
        return deltaZeile == deltaSpalte &&
                ((zielZeile + zielSpalte) % 2 == 1);
    }

    @Override

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(spieler1).append("\n");
        builder.append(spieler2).append("\n");
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

    private boolean istSteinDesSpielers(int stein) {
        // Vérifie si le joueur actuel contrôle le pion
        if (turn == 1) {
            return stein > 0; // Pions blancs ou dames blanches
        } else {
            return stein < 0; // Pions noirs ou dames noires
        }
    }

    public Spieler getAktuellerSpieler() {
        return (turn == 1) ? spieler1 : spieler2;
    }

    public void wechselSpieler() {
        turn = (turn == 1) ? -1 : 1; // Alterne entre 1 et -1
    }

    private Spieler getGegnerSpieler() {
        return (getAktuellerSpieler() == spieler1) ? spieler2 : spieler1;
    }

    Spieler getGewinner() {
        if (spieler1.getAnzahlStein() == 0) {
            return spieler2;
        } else {
            return spieler1;
        }
    }

    public int[] getFeld() {
        return feld;
    }

    void aktualisiereBestenPunktestand() {
        Spieler gewinner = getGewinner();
        Spieler gewinnSpieler = (gewinner.equals(spieler1)) ? spieler1 : spieler2;
        BestenPunktestandVerwalten best = new BestenPunktestandVerwalten("bestScore.txt");
        best.punktestandSpeichernSpieler(gewinnSpieler);

    }

    Spieler getSpieler1() {
        return spieler1;
    }

    Spieler getSpieler2() {
        return spieler2;
    }

}

class BestenPunktestandVerwalten {
    int besterPunktestand;
    String datei = "bestScore.txt";

    BestenPunktestandVerwalten(String datei) {
        this.datei = datei;
        besterPunktestand = 0;
        punktestandLesen();
    }

    // Méthode pour lire le meilleur score à partir du fichier
    int punktestandLesen() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(datei));
            String zeile = br.readLine();
            br.close();
            if (zeile != null && !zeile.isEmpty()) {
                besterPunktestand = Integer.parseInt(zeile);
            } else {
                besterPunktestand = 0;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return besterPunktestand;
    }

    // Méthode pour enregistrer un nouveau score si c'est un meilleur score
    void punktestandSpeichern(int neuerPunktestand) {
        if (neuerPunktestand > besterPunktestand) {
            besterPunktestand = neuerPunktestand;

            try {
                BufferedWriter wr = new BufferedWriter(new FileWriter(datei));
                wr.write(Integer.toString(besterPunktestand));
                wr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Méthode pour enregistrer le score du gagnant
    void punktestandSpeichernSpieler(Spieler gewinner) {
        int neuerPunktestand = gewinner.getPunkte();
        punktestandSpeichern(neuerPunktestand);
    }
}

class MinimaxSpieler {
    private static final int MAX_TIEFE = 5;

    public static int[] findeBestenZug(Spielfeld spielfeld) {
        int besterWert = Integer.MIN_VALUE;
        int besterStart = -1;
        int besterZiel = -1;

        // Parcourir tous les pions du joueur actuel
        for (int i = 0; i < 64; i++) {
            // Vérifier si c'est un pion du joueur actuel
            if ((spielfeld.getAktuellerSpieler().getFarbe() == Stein.SCHWARTZ_STEIN && spielfeld.getFeld()[i] < 0) ||
                    (spielfeld.getAktuellerSpieler().getFarbe() == Stein.WEISS_STEIN && spielfeld.getFeld()[i] > 0)) {

                try {
                    int[] moeglicheZuege = spielfeld.zeigeZugMoeglichkeit(i);

                    for (int zielPosition : moeglicheZuege) {
                        // Créer une copie du plateau pour la simulation
                        Spielfeld testSpielfeld = new Spielfeld();
                        System.arraycopy(spielfeld.getFeld(), 0, testSpielfeld.getFeld(), 0, 64);

                        // S'assurer que c'est le bon joueur qui joue
                        while (testSpielfeld.getAktuellerSpieler().getFarbe() != spielfeld.getAktuellerSpieler()
                                .getFarbe()) {
                            testSpielfeld.wechselSpieler();
                        }

                        try {
                            testSpielfeld.steinBewegen(i, zielPosition);
                            int wert = minimax(testSpielfeld, MAX_TIEFE - 1, false, Integer.MIN_VALUE,
                                    Integer.MAX_VALUE);

                            if (wert > besterWert) {
                                besterWert = wert;
                                besterStart = i;
                                besterZiel = zielPosition;
                            }
                        } catch (IllegalArgumentException e) {
                            // Ignorer les mouvements invalides
                            continue;
                        }
                    }
                } catch (IllegalArgumentException e) {
                    // Ignorer les positions sans mouvements possibles
                    continue;
                }
            }
        }

        if (besterStart == -1 || besterZiel == -1) {
            return null;
        }

        return new int[] { besterStart, besterZiel };
    }

    private static int minimax(Spielfeld spielfeld, int tiefe, boolean istMaximierend, int alpha, int beta) {
        if (tiefe == 0 || spielfeld.istSpielAmEnde()) {
            return bewerteStellung(spielfeld);
        }

        if (istMaximierend) {
            int maxWert = Integer.MIN_VALUE;
            for (int i = 0; i < 64; i++) {
                if ((spielfeld.getAktuellerSpieler().getFarbe() == Stein.SCHWARTZ_STEIN && spielfeld.getFeld()[i] < 0)
                        ||
                        (spielfeld.getAktuellerSpieler().getFarbe() == Stein.WEISS_STEIN
                                && spielfeld.getFeld()[i] > 0)) {
                    try {
                        int[] moeglicheZuege = spielfeld.zeigeZugMoeglichkeit(i);

                        for (int zielPosition : moeglicheZuege) {
                            Spielfeld testSpielfeld = new Spielfeld();
                            System.arraycopy(spielfeld.getFeld(), 0, testSpielfeld.getFeld(), 0, 64);

                            // Synchroniser le joueur actuel
                            while (testSpielfeld.getAktuellerSpieler().getFarbe() != spielfeld.getAktuellerSpieler()
                                    .getFarbe()) {
                                testSpielfeld.wechselSpieler();
                            }

                            try {
                                testSpielfeld.steinBewegen(i, zielPosition);
                                testSpielfeld.wechselSpieler();
                                int wert = minimax(testSpielfeld, tiefe - 1, false, alpha, beta);
                                maxWert = Math.max(maxWert, wert);
                                alpha = Math.max(alpha, maxWert);

                                if (beta <= alpha) {
                                    break;
                                }
                            } catch (IllegalArgumentException e) {
                                continue;
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        continue;
                    }
                }
            }
            return maxWert;
        } else {
            int minWert = Integer.MAX_VALUE;
            for (int i = 0; i < 64; i++) {
                if ((spielfeld.getAktuellerSpieler().getFarbe() == Stein.SCHWARTZ_STEIN && spielfeld.getFeld()[i] < 0)
                        ||
                        (spielfeld.getAktuellerSpieler().getFarbe() == Stein.WEISS_STEIN
                                && spielfeld.getFeld()[i] > 0)) {
                    try {
                        int[] moeglicheZuege = spielfeld.zeigeZugMoeglichkeit(i);

                        for (int zielPosition : moeglicheZuege) {
                            Spielfeld testSpielfeld = new Spielfeld();
                            System.arraycopy(spielfeld.getFeld(), 0, testSpielfeld.getFeld(), 0, 64);

                            // Synchroniser le joueur actuel
                            while (testSpielfeld.getAktuellerSpieler().getFarbe() != spielfeld.getAktuellerSpieler()
                                    .getFarbe()) {
                                testSpielfeld.wechselSpieler();
                            }

                            try {
                                testSpielfeld.steinBewegen(i, zielPosition);
                                testSpielfeld.wechselSpieler();
                                int wert = minimax(testSpielfeld, tiefe - 1, true, alpha, beta);
                                minWert = Math.min(minWert, wert);
                                beta = Math.min(beta, minWert);

                                if (beta <= alpha) {
                                    break;
                                }
                            } catch (IllegalArgumentException e) {
                                continue;
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        continue;
                    }
                }
            }
            return minWert;
        }
    }

    // Funktion zur Bewertung der Stellung
    private static int bewerteStellung(Spielfeld spielfeld) {
        int wert = 0;

        // Jede Position auf dem Brett bewerten
        for (int i = 0; i < 64; i++) {
            int stein = spielfeld.getFeld()[i];

            // Wert der Steine
            switch (stein) {
                case -1: // Schwarzer Stein (KI)
                    wert += 10;
                    // Bonus für Vorwärtsbewegung zur Umwandlung
                    wert += (i / 8);
                    break;
                case -2: // Schwarze Dame (KI)
                    wert += 25;
                    break;
                case 1: // Weißer Stein (Spieler)
                    wert -= 10;
                    // Bonus für Vorwärtsbewegung zur Umwandlung
                    wert -= (7 - (i / 8));
                    break;
                case 2: // Weiße Dame (Spieler)
                    wert -= 25;
                    break;
            }

            // Bonus für geschützte Steine
            if (stein < 0 && istSteinGeschuetzt(spielfeld, i)) {
                wert += 2;
            }
            if (stein > 0 && istSteinGeschuetzt(spielfeld, i)) {
                wert -= 2;
            }
        }

        return wert;
    }

    // Prüft ob ein Stein geschützt ist
    private static boolean istSteinGeschuetzt(Spielfeld spielfeld, int index) {
        int stein = spielfeld.getFeld()[index];
        int reihe = index / 8;
        int spalte = index % 8;

        // Benachbarte Diagonalen prüfen
        int[] reihenOffset = (stein < 0) ? new int[] { 1 } : new int[] { -1 };
        int[] spaltenOffset = { -1, 1 };

        for (int rOffset : reihenOffset) {
            for (int sOffset : spaltenOffset) {
                int neueReihe = reihe + rOffset;
                int neueSpalte = spalte + sOffset;

                if (neueReihe >= 0 && neueReihe < 8 && neueSpalte >= 0 && neueSpalte < 8) {
                    int neuePos = neueReihe * 8 + neueSpalte;
                    if (spielfeld.getFeld()[neuePos] * stein > 0) { // Gleiche Farbe
                        return true;
                    }
                }
            }
        }

        return false;
    }
}