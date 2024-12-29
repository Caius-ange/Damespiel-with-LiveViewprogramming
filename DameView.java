public class DameView {
    Turtle turtle;
    int quadratGroesse = 60; 
    int[] board;

    public DameView(int[] board) {
        // Initialisation du tableau
        this.board = board;
        // Afficher le damier
        druckeBrett(this.board);
    }

    Turtle initTurtle() {
        Turtle newTurtle = new Turtle(480, 480);
        newTurtle.penUp();
        newTurtle.backward(240);
        newTurtle.left(90);
        newTurtle.forward(240);
        newTurtle.right(90);
        newTurtle.penDown();

        return newTurtle;
    }


    public void zeichneQuadratUndFuelle(Turtle t, int quadratGroesse, int r, int g, int b) {
        t.penDown();

        for (int i = 0; i < 4; i++) {
            t.forward(quadratGroesse);
            t.right(90);
        }

        for (int i = 0; i < quadratGroesse; i++) {
            t.color(r, g, b);
            t.penUp();
            t.right(90);
            t.forward(1);
            t.left(90);
            t.penDown();
            t.forward(quadratGroesse);
            t.penUp();
            t.backward(quadratGroesse);
        }
        t.penUp();
        t.right(90);
        t.backward(quadratGroesse);
        t.left(90);
        t.forward(quadratGroesse);
    }

    // method to draw a Pawn with a given color
    public void zeichneStein(Turtle t, int index, int quadratGroesse, int r, int g, int b) {
        t.penUp();
        int[] koordinat = indexZuKoordinaten(index);
        t.moveTo(koordinat[0] * quadratGroesse, koordinat[1] * quadratGroesse);
    
        t.forward(quadratGroesse / 2); // Aller au centre de la case
        t.right(90); // Tourner pour se positionner au centre
        t.forward(quadratGroesse / 3 - 5); // Ajustement léger
        t.left(90); // Tourner pour dessiner dans la bonne direction

        for(int i = 0; i < (quadratGroesse / 3 - 5); i++) {
            zeichneKreis(t, (quadratGroesse / 3 - 5) - i/3, r, g, b); // Appel pour dessiner un cercle plein
        }
    
    }

    public void zeichneKoenigen(Turtle t, int index, int quadratGroesse, int r, int g, int b) {
        t.penUp();
        int[] koordinat = indexZuKoordinaten(index);
        t.moveTo(koordinat[0] * quadratGroesse, koordinat[1] * quadratGroesse);
    
        t.forward(quadratGroesse / 2); // Aller au centre de la case
        t.right(90); // Tourner pour se positionner au centre
        t.forward(quadratGroesse / 3 - 5); // Ajustement léger
        t.left(90); // Tourner pour dessiner dans la bonne direction

        for(int i = 0; i < (quadratGroesse / 3 - 5); i++) {
            zeichneKreis(t, (quadratGroesse / 3 - 5) - i, r, g, b); // Appel pour dessiner un cercle plein
        }
    
    }

    int[] indexZuKoordinaten(int index) {
        int[] koordinat = new int[2];
        koordinat[0] = index % 8;
        koordinat[1] = index / 8;
        return koordinat;
    }

    void zeichneKreis(Turtle t, int kreisGroesse, int r, int g, int b) {
        t.color(r, g, b); // Définir la couleur
        t.penDown();
        
        // Pour dessiner un cercle plein
        int segments = 36; // Nombre de segments pour le cercle
        double angle = 360.0 / segments; // Angle de chaque segment
        double step = 2 * Math.PI * kreisGroesse / segments; // Longueur de chaque segment
    
        for (int i = 0; i < segments; i++) {
            t.forward(step); // Avancer d'un segment
            t.right(angle); // Tourner pour suivre le cercle
        }
    
        t.penUp();
    }

    

    void zeichneBrett(Turtle t, int quadratGroesse) {
        boolean beginntMitHellBraun = true;

        for (int i = 0; i < 64; i++) {
            if (i % 8 == 0 && i != 0) {
                beginntMitHellBraun = !beginntMitHellBraun;
                t.penUp();
                t.backward(8 * quadratGroesse);
                t.right(90);
                t.forward(quadratGroesse);
                t.left(90);
                t.penDown();
            }
            if (beginntMitHellBraun) {
                zeichneQuadratUndFuelle(t, quadratGroesse, 255, 228, 196);
                beginntMitHellBraun = false;
            } else {
                zeichneQuadratUndFuelle(t, quadratGroesse, 139, 69, 19);
                beginntMitHellBraun = true;
            }
        }
    }


    void druckeBrett(int[] brettZumDrucken) {
        Turtle newTurtle = initTurtle();

        zeichneBrett(newTurtle, quadratGroesse);

        for (int i = 0; i < 64; i++) {
            switch (brettZumDrucken[i]) {

                case -1:
                    zeichneStein(newTurtle, i, quadratGroesse, 0, 0, 0);
                    break;
                case 1:
                    zeichneStein(newTurtle, i, quadratGroesse, 255, 255, 255);
                    break;
                case 2:
                    zeichneKoenigen(newTurtle, i, quadratGroesse, 255, 255, 255);
                    break;
                case -2:
                    zeichneKoenigen(newTurtle, i, quadratGroesse, 0, 0, 0);
                    break;
                default:
                    break;
            }

        }
    }
}
