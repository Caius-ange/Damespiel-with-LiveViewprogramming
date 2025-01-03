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
    private Stein farbe; // Farbe der Spielfiguren, die dem Spieler zugeordnet sind

    public Spieler(String name, Stein farbe, boolean isKI) {
        this.name = name;
        this.isKI = isKI;
        this.punkte = 0;
        this.farbe = farbe; // Stein.WEISS_STEIN oder Stein.SCHWARTZ_STEIN
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

    public void setAnzahlStein(int anzahlStein) {
        this.anzahlStein = anzahlStein;
    }

    public void erhoehePunkte(int punkte) {
        this.punkte += punkte;
    }

    public boolean isKI() {
        return isKI;
    }

    public void setKI(boolean isKI) {
        this.isKI = isKI;
    }

    @Override
    public String toString() {
        return name + "   " + "Punkte : " + punkte + "   " + "Steine: "
                + anzahlStein;
    }
}

public class Spielfeld {
    private final int[] feld;
    private Spieler spieler1;
    private Spieler spieler2;
    private int turn; // 1 für Weiß, -1 für Schwarz
    BestenPunktestandVerwalten best;

    public Spielfeld() {
        spieler1 = new Spieler("spieler1", Stein.WEISS_STEIN, false);
        spieler2 = new Spieler("spieler2", Stein.SCHWARTZ_STEIN, true);
        turn = 1; // Der weiße Spieler beginnt.
        best = new BestenPunktestandVerwalten("bestScore.txt");
        // Das Feld initialiseren
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

    // a2
    public boolean bewegenAusfuehren(int startIndex, int zielIndex) {

        // Ki ist dran
        if (getAktuellerSpieler().isKI()) {
            System.out.println("KI ist dran...");
            int[] besterZug = MinimaxSpieler.findeBestenZug(this);

            if (besterZug != null) {
                try {
                    steinBewegen(besterZug[0], besterZug[1]);
                    System.out.println("KI zieht von " + besterZug[0] + " nach " + besterZug[1]);
                    wechselSpieler();

                    // ruf toString() Method auf
                    System.out.println(this);

                    if (istSpielAmEnde()) {
                        System.out.println("Das Spiel ist bereits beendet. Gewinner: " + getGewinner());

                        if (getGewinner().getPunkte() > best.getBesterPunktestand()) {
                            best.punktestandSpeichern(getGewinner().getPunkte());
                            System.out.println("Herzlichen Glueckwunsch, Sie haben den besten aktuellen Punktestand!");
                        }
                    }

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
        // Mensch ist dran
        else {
            try {
                steinBewegen(startIndex, zielIndex);
                wechselSpieler();

                // Nach dem Zug des menschlichen Spielers soll die KI spielen, falls sie an der Reihe ist.
                if (!istSpielAmEnde() && getAktuellerSpieler().isKI()) {
                    return bewegenAusfuehren(0, 0); // Die Parameter werden für die KI ignoriert
                }

                // ruf toString() Method auf
                System.out.println(this);

                if (istSpielAmEnde()) {
                    System.out.println("Das Spiel ist bereits beendet. Gewinner: " + getGewinner());

                    if (getGewinner().getPunkte() > best.getBesterPunktestand()) {
                        best.punktestandSpeichern(getGewinner().getPunkte());
                        System.out.println("Herzlichen Glueckwunsch, Sie haben den besten aktuellen Punktestand!");
                    }
                }

                return true;

            } catch (IllegalArgumentException e) {
                System.out.println("Ungueltiger Zug: " + e.getMessage());
                return false;
            }
        }
    }
    // a2

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

    // a3
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

    // a3
    private boolean dameSchlagen(int startIndex, int zielIndex) {
        int stein = feld[startIndex];
        // Richtung berechnen
        int deltaX = (zielIndex / 8) - (startIndex / 8);
        int deltaY = (zielIndex % 8) - (startIndex % 8);

        int schrittX = Integer.compare(deltaX, 0);
        int schrittY = Integer.compare(deltaY, 0);
        int richtung = schrittX * 8 + schrittY;

        int gegnerPosition = -1;
        int aktuell = startIndex + richtung;

        // Position des zu schlagenden Steins finden
        while (istImSpielfeld(aktuell) && aktuell != zielIndex) {
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
            // Schlag ausführen
            schlagAusfuehren(startIndex, zielIndex, gegnerPosition);
            mehrfachschlaegePruefen(zielIndex);

            return true;
        }

        return false;
    }

    private boolean normalerSteinSchlagen(int startIndex, int zielIndex) {
        int stein = feld[startIndex];

        //  der Unterschied zwischen dem Start- und dem Endindex berechnen
        int deltaIndex = zielIndex - startIndex;

        // nach einem möglichen Schlag prüfen
        if (Math.abs(deltaIndex) == 14 || Math.abs(deltaIndex) == 18) {
            int gegnerIndex = startIndex + (deltaIndex / 2);

            // Überprüfen, ob:
            // Die Bewegung auf einer gültigen Diagonale ist
            // Das Ziel-Feld leer ist
            // Ein gegnerischer Stein zu ergreifen ist
            // Die Bewegung innerhalb der Grenzen des Spielfeldes bleibt
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

        // überprüft , ob der geschlagene Stein eine Dame ist
        int gegnerStein = Math.abs(feld[gegnerPosition]);
        if (gegnerStein == Stein.WEISS_KOENIGEN.getWert() || gegnerStein == Stein.SCHWARTZ_KOENIGEN.getWert()) {
            getAktuellerSpieler().erhoehePunkte(6); // 6 Punkte für eine Dame hinzufügen
        } else {
            getAktuellerSpieler().erhoehePunkte(4); // 4 Punkte für einen normalen Stein hinzufügen
        }
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

    // Beforderung zur Dame
    private void dameBeforderungPruefen(int zielIndex) {
        int stein = feld[zielIndex];
        if ((zielIndex / 8 == 0 && stein == Stein.SCHWARTZ_STEIN.getWert()) ||
                (zielIndex / 8 == 7 && stein == Stein.WEISS_STEIN.getWert())) {
            feld[zielIndex] = stein > 0 ? Stein.WEISS_KOENIGEN.getWert() : Stein.SCHWARTZ_KOENIGEN.getWert();
        }
    }

    // a7
    public int[] zeigeZugMoeglichkeit(int index) {
        ArrayList<Integer> moeglicheBewegung = new ArrayList<>();
        // Index innerhalb der Grenzen des Spielfeldes bleibt
        if (!istImSpielfeld(index)) {
            throw new IllegalArgumentException("Der ausgewaehlte Stein ist außerhalb des Spielfelds.");
        }
        
        // überprüft , ob das Feld einen Stein enthält
        int stein = feld[index];
        if (stein == 0) {
            throw new IllegalArgumentException("Das ausgewaelte Feld ist leer.");
        }

        boolean istKoenigen = Math.abs(stein) == 2;

        if (istKoenigen) {
          // Diagonale Richtungen für die Dame (-9: oben-links, -7: oben-rechts, 7: unten-links, 9: unten-rechts
            int[] diagonalen = { -9, -7, 7, 9 };

            for (int diagonale : diagonalen) {
                int zielIndex = index + diagonale;
                int schritte = 1;
                boolean gefundenGegner = false;

                while (istImSpielfeld(zielIndex) && istGueltigeDiagonale(index, zielIndex)) {
                    if (feld[zielIndex] == 0) {
                        // Leeres Feld - Bewegung möglich, wenn noch kein Gegner gefunden wurde
                        if (!gefundenGegner) {
                            moeglicheBewegung.add(zielIndex);
                        }
                    } else if (feld[zielIndex] * stein < 0) {
                        //  wenn einen gegnerischen Stein wird gefunfen
                        if (!gefundenGegner) {
                            gefundenGegner = true;
                            // Überprüft das nächste Feld auf die Ergreifung
                            int nextIndex = zielIndex + diagonale;
                            if (istImSpielfeld(nextIndex) && feld[nextIndex] == 0
                                    && istGueltigeDiagonale(zielIndex, nextIndex)) {
                                moeglicheBewegung.add(nextIndex);
                            }
                        }
                    } else {
                        // wenn einen Stein des aktuellen Spieler gefunden wird
                        break;
                    }
                    schritte++;
                    zielIndex = index + schritte * diagonale;
                }
            }
        } else {
            // für normalen Stein  
            boolean istWeiss = stein > 0;
            int richtung = istWeiss ? 8 : -8;

            int[] diagonalen = { richtung - 1, richtung + 1 };

            for (int diagonale : diagonalen) {
                int zielIndex = index + diagonale;

                if (istImSpielfeld(zielIndex)
                        && feld[zielIndex] == 0
                        && ((zielIndex / 8 + zielIndex % 8) % 2 == 1)
                        && istGleicheZeileOderDiagonal(index, zielIndex)) {
                    moeglicheBewegung.add(zielIndex);
                }
            }

            // mögliche Schlag überprüfen
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
    // a7

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

    // Hilfsmethode: Überprüft, ob die Bewegung in der gleichen gültigen Diagonalen bleibt
    private boolean istGleicheZeileOderDiagonal(int start, int ziel) {
        int startZeile = start / 8;
        int zielZeile = ziel / 8;

        return Math.abs(startZeile - zielZeile) == 1; // Doit être une seule ligne plus haut ou plus bas
    }

    // Aktualisierte Hilfsmethode zur Überprüfung der Gültigkeit einer Diagonalen
    private  boolean istGueltigeDiagonale(int start, int ziel) {
        int startZeile = start / 8;
        int startSpalte = start % 8;
        int zielZeile = ziel / 8;
        int zielSpalte = ziel % 8;

        int deltaZeile = Math.abs(zielZeile - startZeile);
        int deltaSpalte = Math.abs(zielSpalte - startSpalte);

        return deltaZeile == deltaSpalte &&
                ((zielZeile + zielSpalte) % 2 == 1);
    }

    boolean istSteinDesSpielers(int stein) {
        if (turn == 1) {
            return stein > 0; 
        } else {
            return stein < 0; 
        }
    }

    public Spieler getAktuellerSpieler() {
        return (turn == 1) ? spieler1 : spieler2;
    }

    public void wechselSpieler() {
        turn = (turn == 1) ? -1 : 1; 
    }

    public BestenPunktestandVerwalten getBestenPunktestandVerwalten() {
        return this.best;
    }

    public int[] getFeld() {
        return feld;
    }

    Spieler getGegnerSpieler() {
        return (getAktuellerSpieler() == spieler1) ? spieler2 : spieler1;
    }

    Spieler getGewinner() {
        if (spieler1.getAnzahlStein() == 0) {
            return spieler2;
        } else {
            return spieler1;
        }
    }

    Spieler getSpieler1() {
        return spieler1;
    }

    Spieler getSpieler2() {
        return spieler2;
    }

}

class MinimaxSpieler {
    private static final int MAX_TIEFE = 5;

    // a5
    public static int[] findeBestenZug(Spielfeld spielfeld) {
        int besterWert = Integer.MIN_VALUE;
        int besterStart = -1;
        int besterZiel = -1;

        for (int i = 0; i < 64; i++) {
            // überprufen , ob es einen Stein des aktuelles  Spieler
            if ((spielfeld.getAktuellerSpieler().getFarbe() == Stein.SCHWARTZ_STEIN && spielfeld.getFeld()[i] < 0) ||
                    (spielfeld.getAktuellerSpieler().getFarbe() == Stein.WEISS_STEIN && spielfeld.getFeld()[i] > 0)) {

                try {
                    int[] moeglicheZuege = spielfeld.zeigeZugMoeglichkeit(i);

                    for (int zielPosition : moeglicheZuege) {

                        if (!spielfeld.istImSpielfeld(i) || !spielfeld.istImSpielfeld(zielPosition)) {
                            continue; // ungültige Index werden ignoriert
                        }

                        // Erstellung  eine Kopie des Spielfelds für die Simulation
                        Spielfeld testSpielfeld = new Spielfeld();
                        System.arraycopy(spielfeld.getFeld(), 0, testSpielfeld.getFeld(), 0, 64);

                        // Sicherstellen, dass der richtige Spieler am Zug ist
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
                            // ungültige Index werden ignoriert
                            continue;
                        }
                    }
                } catch (IllegalArgumentException e) {
                    // Positionen ohne mögliche Bewegungen ignorieren
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

                            if (!spielfeld.istImSpielfeld(i) || !spielfeld.istImSpielfeld(zielPosition)) {
                                continue; // ungültige Index werden ignoriert
                            }

                            Spielfeld testSpielfeld = new Spielfeld();
                            System.arraycopy(spielfeld.getFeld(), 0, testSpielfeld.getFeld(), 0, 64);

                            // Aktuellen Spieler synchronisieren
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

                            // Aktuellen Spieler synchronisieren
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
    // a5

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

// a6
class BestenPunktestandVerwalten {
    int besterPunktestand;
    String datei = "bestScore.txt";

    BestenPunktestandVerwalten(String datei) {
        this.datei = datei;
        besterPunktestand = 0;
        punktestandLesen();
    }

    // Methode zum Lesen des besten Scores aus der Datei
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

    // Methode zum Speichern eines neuen Scores, wenn es ein besserer Score ist
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

    int getBesterPunktestand() {
        return this.besterPunktestand;
    }
}
// a6