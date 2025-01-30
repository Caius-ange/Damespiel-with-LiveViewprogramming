import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
        return name + "   " + "Punkte : " + punkte + "   " + "Steine: " + anzahlStein;
    }
}

public class Spielfeld {
    private final int[] feld;
    private Spieler spieler1;
    private Spieler spieler2;
    int turn; // 1 fuer Weiß, -1 fuer Schwarz
    private BestenPunktestandVerwalten best;

    public Spielfeld() {
        spieler1 = new Spieler("spieler_1", Stein.WEISS_STEIN, false);
        spieler2 = new Spieler("spieler_2", Stein.SCHWARTZ_STEIN, true);
        turn = 1; // Der weiße Spieler beginnt.
        best = new BestenPunktestandVerwalten("Bestscore.txt");
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
                    String startPosition = indexZuString(besterZug[0]);
                    String zielPosition = indexZuString(besterZug[1]);
                    System.out.println("KI zieht von " + startPosition + " nach " + zielPosition);
                    wechselSpieler();
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
                if (!istSpielAmEnde() && getAktuellerSpieler().isKI()) {
                    return bewegenAusfuehren(0, 0);
                }
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
        boolean schlag = steinSchlagen(startIndex, zielIndex);
        if (!schlag) {
            feld[zielIndex] = feld[startIndex];
            feld[startIndex] = 0;
        }
        dameBeforderungPruefen(zielIndex);
    }

    // a3
    public boolean steinSchlagen(int startIndex, int zielIndex) {
        int stein = feld[startIndex];
        boolean istKoenigen = Math.abs(stein) == 2;
        boolean schlag = false;
        if (istKoenigen) {
            schlag = dameSchlagen(startIndex, zielIndex);
        } else {
            schlag = normalerSteinSchlagen(startIndex, zielIndex);
            if (schlag) {
                dameBeforderungPruefen(zielIndex);
            }
        }

        return schlag;
    }

    // a3
    private boolean dameSchlagen(int startIndex, int zielIndex) {
        int stein = feld[startIndex];
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
        if (gegnerPosition != -1 && feld[zielIndex] == 0) {
            schlagAusfuehren(startIndex, zielIndex, gegnerPosition);
            mehrfachschlaegePruefen(zielIndex);
            return true;
        }
        return false;
    }

    private boolean normalerSteinSchlagen(int startIndex, int zielIndex) {
        int stein = feld[startIndex];
        int deltaIndex = zielIndex - startIndex;
        // nach einem möglichen Schlag pruefen
        if (Math.abs(deltaIndex) == 14 || Math.abs(deltaIndex) == 18) {
            int gegnerIndex = startIndex + (deltaIndex / 2);
            if (istGueltigeDiagonale(startIndex, zielIndex) &&
                    feld[zielIndex] == 0 &&
                    feld[gegnerIndex] != 0 &&
                    feld[gegnerIndex] * stein < 0) {
                schlagAusfuehren(startIndex, zielIndex, gegnerIndex);
                try {
                    mehrfachschlaegePruefen(zielIndex);
                } catch (Exception e) {
                }
                return true;
            }
        }
        return false;
    }

    private void schlagAusfuehren(int startIndex, int zielIndex, int gegnerPosition) {
        // Bewegung durchfuehren
        feld[zielIndex] = feld[startIndex];
        feld[startIndex] = 0;
        feld[gegnerPosition] = 0;
        // Punktestand und Steinanzahl aktualisieren
        Spieler gegner = getGegnerSpieler();
        gegner.setAnzahlStein(gegner.getAnzahlStein() - 1);
        // ueberprueft , ob der geschlagene Stein eine Dame ist
        int gegnerStein = Math.abs(feld[gegnerPosition]);
        if (gegnerStein == Stein.WEISS_KOENIGEN.getWert() || gegnerStein == Stein.SCHWARTZ_KOENIGEN.getWert()) {
            getAktuellerSpieler().erhoehePunkte(6);
        } else {
            getAktuellerSpieler().erhoehePunkte(4);
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
        if (!istImSpielfeld(index)) {
            throw new IllegalArgumentException("Der ausgewaehlte Stein ist außerhalb des Spielfelds.");
        }
        int stein = feld[index];
        if (stein == 0) {
            throw new IllegalArgumentException("Das ausgewaelte Feld ist leer.");
        }
        boolean istKoenigen = Math.abs(stein) == 2;
        if (istKoenigen) {
            int[] dx = { -1, 1 };
            int[] dy = { -1, 1 };

            for (int dirX : dx) {
                for (int dirY : dy) {
                    int currentX = index / 8;
                    int currentY = index % 8;

                    while (true) {
                        int nextX = currentX + dirX;
                        int nextY = currentY + dirY;

                        if (nextX < 0 || nextX >= 8 || nextY < 0 || nextY >= 8)
                            break;
                        int nextIndex = nextX * 8 + nextY;
                        if (feld[nextIndex] == 0) {
                            moeglicheBewegung.add(nextIndex);
                            currentX = nextX;
                            currentY = nextY;
                        } else if (feld[nextIndex] * stein < 0) {
                            int sautX = nextX + dirX;
                            int sautY = nextY + dirY;

                            if (sautX >= 0 && sautX < 8 && sautY >= 0 && sautY < 8) {
                                int sautIndex = sautX * 8 + sautY;
                                if (feld[sautIndex] == 0) {
                                    moeglicheBewegung.add(sautIndex);
                                    currentX = sautX;
                                    currentY = sautY;
                                    continue;
                                }
                            }
                            break;
                        } else {
                            break;
                        }
                    }
                }
            }
        } else {
            int richtung = (stein > 0) ? 1 : -1;
            int[] diagonalen = { 7 * richtung, 9 * richtung };

            for (int delta : diagonalen) {
                int zielIndex = index + delta;
                if (istImSpielfeld(zielIndex) && feld[zielIndex] == 0 && istGueltigeDiagonale(index, zielIndex)) {
                    moeglicheBewegung.add(zielIndex);
                } else if (istImSpielfeld(zielIndex) && feld[zielIndex] * stein < 0) {
                    // Moegliche Schlag
                    int ueberspringIndex = zielIndex + delta;
                    if (istImSpielfeld(ueberspringIndex) && feld[ueberspringIndex] == 0) {
                        moeglicheBewegung.add(ueberspringIndex);
                    }
                }
            }
        }
        return moeglicheBewegung.stream().mapToInt(i -> i).toArray();
    }

    // a7

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        builder.append(spieler1).append("\n");
        builder.append(spieler2).append("\n");
        builder.append("\n");

        builder.append(" ");
        for (char col = 'a'; col <= 'h'; col++) {
            builder.append("   ").append(col).append("");
        }
        builder.append("\n");
        builder.append("  +").append("---+".repeat(8)).append("\n");

        for (int i = 0; i < 64; i++) {
            if (i % 8 == 0) {
                if (i != 0) {
                    builder.append("\n  +").append("---+".repeat(8)).append("\n");
                }
                builder.append((i / 8) + 1).append(" |");
            }

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
                    symbol = ' '; // Leeres Feld
            }
            builder.append(symbol).append("  ").append('|');
        }
        builder.append("\n  +").append("---+".repeat(8)).append("\n");
        return builder.toString();
    }

    public boolean istImSpielfeld(int index) {
        return index >= 0 && index < 64;
    }

    boolean istSpielAmEnde() {
        return spieler1.getAnzahlStein() == 0 || spieler2.getAnzahlStein() == 0;
    }

    String indexZuString(int index) {
        int columns = 8;
        char column = (char) ('a' + (index % columns));
        int row = 1 + (index / columns);
        return "" + column + row;
    }

    // Aktualisierte Hilfsmethode zur ueberpruefung der Gueltigkeit einer Diagonalen
    boolean istGueltigeDiagonale(int start, int ziel) {
        int startZeile = start / 8;
        int startSpalte = start % 8;
        int zielZeile = ziel / 8;
        int zielSpalte = ziel % 8;

        int deltaZeile = Math.abs(zielZeile - startZeile);
        int deltaSpalte = Math.abs(zielSpalte - startSpalte);

        return deltaZeile == deltaSpalte && ((zielZeile + zielSpalte) % 2 == 1);
    }

    boolean istSteinDesSpielers(int stein) {
        if (turn == 1) {
            return stein > 0;
        } else {
            return stein < 0;
        }
    }

    Spieler getAktuellerSpieler() {
        return (turn == 1) ? spieler1 : spieler2;
    }

    void wechselSpieler() {
        turn = (turn == 1) ? -1 : 1;
    }

    BestenPunktestandVerwalten getBestenPunktestandVerwalten() {
        return this.best;
    }

    int[] getFeld() {
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
    private static final int DAME_WERT = 25;
    private static final int STEIN_WERT = 10;
    
    public static int[] findeBestenZug(Spielfeld spielfeld) {
        int[] besterZug = null;
        int besterWert = Integer.MIN_VALUE;
        
        for (int i = 0; i < 64; i++) {
            if (spielfeld.getFeld()[i] != 0 && spielfeld.istSteinDesSpielers(spielfeld.getFeld()[i])) {
                int[] zuege;
                try {
                    zuege = spielfeld.zeigeZugMoeglichkeit(i);
                } catch (IllegalArgumentException e) {
                    continue; // Überspringen, wenn keine Züge möglich
                }
                
                for (int ziel : zuege) {
                    // Prüfen, ob der Zug diagonal und innerhalb des Spielfelds ist
                    if (!spielfeld.istGueltigeDiagonale(i, ziel)) {
                        continue;
                    }
                    
                    Spielfeld kopie = kopiereSpielfeld(spielfeld);
                    try {
                        kopie.steinBewegen(i, ziel);
                        int wert = minimax(kopie, MAX_TIEFE - 1, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                        if (wert > besterWert) {
                            besterWert = wert;
                            besterZug = new int[]{i, ziel};
                        }
                    } catch (IllegalArgumentException e) {
                        continue;       // Ungültiger Zug ignorieren

                    }
                }
            }
        }
        
        return besterZug;
    }
    private static boolean istSpielerStein(Spielfeld spielfeld, int pos) {
        boolean istSchwarz = spielfeld.getAktuellerSpieler().getFarbe() == Stein.SCHWARTZ_STEIN;
        return istSchwarz ? spielfeld.getFeld()[pos] < 0 : spielfeld.getFeld()[pos] > 0;
    }

    private static int[] getGueltigeZuege(Spielfeld spielfeld, int pos) {
        try {
            int[] ursprungZuege = spielfeld.zeigeZugMoeglichkeit(pos);
            return Arrays.stream(ursprungZuege)
                .filter(ziel -> {
                    try {
                        Spielfeld testSpielfeld = kopiereSpielfeld(spielfeld);
                        testSpielfeld.steinBewegen(pos, ziel);
                        return true;
                    } catch (IllegalArgumentException e) {
                        return false;
                    }
                })
                .toArray();
        } catch (IllegalArgumentException e) {
            return new int[0];
        }
    }

    private static Spielfeld kopiereSpielfeld(Spielfeld original) {
        Spielfeld kopie = new Spielfeld();
        System.arraycopy(original.getFeld(), 0, kopie.getFeld(), 0, 64);
        kopie.getSpieler1().setAnzahlStein(original.getSpieler1().getAnzahlStein());
        kopie.getSpieler2().setAnzahlStein(original.getSpieler2().getAnzahlStein());
        kopie.turn = original.turn;
        return kopie;
    }

    private static void synchronisiereSpieler(Spielfeld original, Spielfeld kopie) {
        while (kopie.getAktuellerSpieler().getFarbe() != original.getAktuellerSpieler().getFarbe()) {
            kopie.wechselSpieler();
        }
    }
   // a5
    private static int minimax(Spielfeld spielfeld, int tiefe, boolean isMax, int alpha, int beta) {
        if (tiefe == 0 || spielfeld.istSpielAmEnde()) return bewerteStellung(spielfeld);
        
        int bestWert = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int i = 0; i < 64; i++) {
            if (!istSpielerStein(spielfeld, i)) continue;
            for (int ziel : getGueltigeZuege(spielfeld, i)) {
                int wert = berechneMinimax(spielfeld, i, ziel, tiefe, isMax, alpha, beta);
                bestWert = isMax ? Math.max(bestWert, wert) : Math.min(bestWert, wert);
                if (isMax) alpha = Math.max(alpha, wert);
                else beta = Math.min(beta, wert);
                if (beta <= alpha) return bestWert;
            }
        }
        return bestWert;
    }
   // a5
    private static int berechneMinimax(Spielfeld spielfeld, int start, int ziel, int tiefe, boolean isMax, int alpha, int beta) {
        Spielfeld testSpielfeld = new Spielfeld();
        System.arraycopy(spielfeld.getFeld(), 0, testSpielfeld.getFeld(), 0, 64);
        synchronisiereSpieler(spielfeld, testSpielfeld);
        
        try {
            testSpielfeld.steinBewegen(start, ziel);
            testSpielfeld.wechselSpieler();
            return minimax(testSpielfeld, tiefe - 1, !isMax, alpha, beta);
        } catch (IllegalArgumentException e) {
            return isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        }
    }

    private static int bewerteStellung(Spielfeld spielfeld) {
        int wert = 0;
        for (int i = 0; i < 64; i++) {
            wert += bewertePosition(spielfeld, i);
        }
        return wert;
    }

    private static int bewertePosition(Spielfeld spielfeld, int pos) {
        int stein = spielfeld.getFeld()[pos];
        if (stein == 0) return 0;
        
        int basisWert = Math.abs(stein) == 2 ? DAME_WERT : STEIN_WERT;
        int positionsBonus = berechnePositionsBonus(pos, stein);
        int schutzBonus = istSteinGeschuetzt(spielfeld, pos) ? 2 : 0;
        
        return (stein < 0) ? (basisWert + positionsBonus + schutzBonus) : 
                           -(basisWert + positionsBonus + schutzBonus);
    }

    private static int berechnePositionsBonus(int pos, int stein) {
        int reihe = pos / 8;
        return stein < 0 ? reihe : (7 - reihe);
    }

    private static boolean istSteinGeschuetzt(Spielfeld spielfeld, int pos) {
        int stein = spielfeld.getFeld()[pos];
        int reihe = pos / 8;
        int spalte = pos % 8;
        int rOffset = (stein < 0) ? 1 : -1;
        
        for (int sOffset : new int[]{-1, 1}) {
            int neueReihe = reihe + rOffset;
            int neueSpalte = spalte + sOffset;
            if (neueReihe >= 0 && neueReihe < 8 && neueSpalte >= 0 && neueSpalte < 8) {
                int neuePos = neueReihe * 8 + neueSpalte;
                if (spielfeld.getFeld()[neuePos] * stein > 0) return true;
            }
        }
        return false;
    }
}

// a6
class BestenPunktestandVerwalten {
    int besterPunktestand;
    String datei = "Bestscore.txt";

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