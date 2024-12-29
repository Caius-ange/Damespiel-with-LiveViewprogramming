public class DameSpiel {
    
    private Spielfeld spielfeld;
    private DameView view;
    Turtle turtle;

    public DameSpiel() {
        this.spielfeld = new Spielfeld();
        this.view = new DameView(this.spielfeld.getFeld());
        this.turtle =  new Turtle(200, 200);
        
    }

    public void steinBewegenAusfuehren(int startIndex, int zielIndex) {

        spielfeld.bewegenAusuehren(startIndex, zielIndex);

        // Afficher le damier
        this.view.druckeBrett(this.spielfeld.getFeld());
    }
}
