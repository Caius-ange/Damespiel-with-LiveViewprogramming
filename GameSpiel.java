public class GameSpiel {
    
    private Spielfeld spielfeld;
    private Print print;

    public GameSpiel() {
        this.spielfeld = new Spielfeld();
        this.print = new Print(this.spielfeld.getFeld());
    }

    public void makeMove(int from, int to) {

        spielfeld.steinBewegenAusfuehren(from, to);

        // Afficher le damier
        this.print.printBoard(this.spielfeld.getFeld());
    }
}
