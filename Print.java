public class Print {
    // Déclaration des variables
    Turtle turtle = new Turtle(480, 480);
    int tailleCase = 60; // Taille d'une case
    int lignes = 8; // Nombre de lignes
    int colonnes = 8; // Nombre de colonnes
    int[] board = new int[64];

    public Print() {
        // Position initiale pour commencer au coin supérieur gauche
        turtle.penUp();
        turtle.backward(240); // Déplacement à gauche
        turtle.left(90);
        turtle.forward(240); // Déplacement en haut
        turtle.right(90);
        turtle.penDown();

        // initialiser le damier
        initializeBoardswithPawns();
        // Afficher le damier
        printBoard();

    }

    public void drawSquareAndFill(Turtle t, int tailleCase, int r, int g, int b) {
        t.penDown();

        for (int i = 0; i < 4; i++) {
            t.forward(tailleCase);
            t.right(90);
        }

        for (int i = 0; i < tailleCase; i++) {
            t.color(r, g, b);
            t.penUp();
            t.right(90);
            t.forward(1);
            t.left(90);
            t.penDown();
            t.forward(tailleCase);
            t.penUp();
            t.backward(tailleCase);
        }
        t.penUp();
        t.right(90);
        t.backward(tailleCase);
        t.left(90);
        // t.forward(tailleCase);
    }

    // method to draw a Pawn with a given color
    public void drawPawn(Turtle t, int indexZuKoordinaten, int tailleCase, int r, int g, int b) {

        // Aller premièrement à la la position de la case donnes par l'index
        t.penUp();
        t.forward(tailleCase * (indexZuKoordinaten % 8)); // Aller à la colonne
        t.right(90); // Tourner pour se positionner à la ligne
        t.forward(tailleCase * (indexZuKoordinaten / 8)); // Aller à la ligne
        t.left(90); // Tourner à nouveau pour dessiner dans la bonne direction

        
        t.forward(tailleCase / 2); // Aller au centre de la case
        t.right(90); // Tourner pour se positionner au centre
        t.forward(tailleCase / 2 - 5); // S'ajuster légèrement pour éviter de dessiner sur les bords
        t.left(90); // Tourner à nouveau pour dessiner dans la bonne direction
        t.color(r, g, b); // Couleur du pion

        drawCircle(t, tailleCase / 4); // Dessiner un cercle plus précis

        // Revenir à la position initiale
        t.backward(tailleCase / 2 - 5); // Revenir au centre de la case
        t.left(90); // Tourner pour retourner à la position initiale
        t.backward(tailleCase / 2); // Revenir au départ
        t.right(90); // Tourner pour rétablir l'orientation initiale
    }

    void drawCircle(Turtle t, int tailleCercle) {
        t.penDown();
        for (int i = 0; i < 36; i++) {
            t.forward(2 * Math.PI * tailleCercle / 36);
            t.right(10);
        }
        t.penUp();
    }

    void initializeBoardswithPawns() {
        for (int i = 0; i < 64; i++) {
            if (i / 8 < 3 && (i + i / 8) % 2 == 1) {
                board[i] = -1;
            } else if (i / 8 > 4 && (i + i / 8) % 2 == 1) {
                board[i] = 1;

            }
            // for usable cases but empty
            else if (i / 8 > 2 && i / 8 < 5 && (i + i / 8) % 2 == 1) {
                board[i] = 0;
            } else {
                // for usable cases
                board[i] = 3;
            }
        }
    }

    void printBoard() {
        for (int i = 0; i < 64; i++) {
            if (i % 8 == 0 && i != 0) {
                turtle.penUp();
                turtle.backward(8 * tailleCase);
                turtle.right(90);
                turtle.forward(tailleCase);
                turtle.left(90);
                turtle.penDown();
            }
            switch (board[i]) {

                case 0:
                    drawSquareAndFill(turtle, tailleCase, 139, 69, 19);
                    break;
                case -1:
                    drawSquareAndFill(turtle, tailleCase, 139, 69, 19);
                    // drawPawn(turtle, tailleCase, 0, 0, 0);
                    break;
                case 1:
                    drawSquareAndFill(turtle, tailleCase, 139, 69, 19);
                    // drawPawn(turtle, tailleCase, 255, 255, 255);
                    break;
                // king case 1
                case 2:
                    drawSquareAndFill(turtle, tailleCase, 139, 69, 19);
                    // draw king with red color
                    // drawPawn(turtle, tailleCase, 255, 0, 0);
                    break;
                // king case -1
                case -2:
                    drawSquareAndFill(turtle, tailleCase, 139, 69, 19);
                    // pawn with blue color
                    // drawPawn(turtle, tailleCase, 0, 0, 255);
                    break;
                default:
                    // case vide de couleur marron clair
                    drawSquareAndFill(turtle, tailleCase, 255, 228, 196);
                    break;
            }

            turtle.forward(tailleCase);

        }
    }

}
