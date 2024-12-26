public class Print {
    // Déclaration des variables
    Turtle turtle;
    int tailleCase = 60; // Taille d'une case
    int lignes = 8; // Nombre de lignes
    int colonnes = 8; // Nombre de colonnes
    int[] board;
    //private Spielfeld spielfeld;

    public Print() {
        // Position initiale pour commencer au coin supérieur gauche
        turtle = initTurtle();

        // Initialisation du tableau
        //board = spielfeld.initializeBoardswithPawn();
        board = initializeBoardswithPawns();

        // Dessiner le damier
        drawBoard(turtle, tailleCase);

        // Afficher le damier
        initialisation(board);

        // Déplacer un pion
        //simulateMove(board, 17, 24);
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

    
    //////////////////////////////////////////////////////////////////
    //TEMPORAIRE
    public int[] initializeBoardswithPawn() {
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
    void simulateMove(int from, int to) {
        this.board[to] =  this.board[from];
        this.board[from] = 0;
        printBoard(this.board);
    }
/////////////////////////////////////////////////////////////////


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
        t.forward(tailleCase);
    }

    // method to draw a Pawn with a given color
    public void drawPawn(Turtle t, int indexZuKoordinaten, int tailleCase, int r, int g, int b) {

        t.penUp();
        int[] coordinates = indexToCoordinates(indexZuKoordinaten);
        t.moveTo(coordinates[0] * tailleCase, coordinates[1] * tailleCase);

        t.forward(tailleCase / 2); // Aller au centre de la case
        t.right(90); // Tourner pour se positionner au centre
        t.forward(tailleCase / 3 - 5); // S'ajuster légèrement pour éviter de dessiner sur les bords
        t.left(90); // Tourner à nouveau pour dessiner dans la bonne direction
        t.color(r, g, b); // Couleur du pion

        drawCircle(t, tailleCase / 4, r, g, b); 

        // Revenir à la position initiale
        t.backward(tailleCase / 2 - 5); // Revenir au centre de la case
        t.left(90); // Tourner pour retourner à la position initiale
        t.backward(tailleCase / 2); // Revenir au départ
        t.right(90); // Tourner pour rétablir l'orientation initiale
    }
    
    int[] indexToCoordinates(int index) {
        int[] coordinates = new int[2];
        coordinates[0] = index % 8;
        coordinates[1] = index / 8;
        System.out.println("Index: " + index + " -> Coords: " + coordinates[0] + ", " + coordinates[1]);
        return coordinates;
    }

    void drawCircle(Turtle t, int tailleCercle, int r, int g, int b) {
        t.penDown();
        for (int i = 0; i < 36; i++) {
            t.forward(2 * Math.PI * tailleCercle / 36);
            t.right(10);
        }

        // draw the circle with a given color
        for (int i = 0; i < 36; i++) {
            t.color(r, g, b);
            t.penUp();
            t.right(10);
            t.forward(2 * Math.PI * tailleCercle / 36);
            t.penDown();
            t.forward(2 * Math.PI * tailleCercle / 36);
            t.penUp();
            t.backward(2 * Math.PI * tailleCercle / 36);
        }
        t.penUp();
    }

    int[] initializeBoardswithPawns() {
        int[] board = new int[64];
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
        return board;
    }

    void drawBoard(Turtle t, int tailleCase) {
        boolean isStartWithLightBrown = true;

        for (int i = 0; i < 64; i++) {
            if (i % 8 == 0 && i != 0) {
                isStartWithLightBrown = !isStartWithLightBrown;
                t.penUp();
                t.backward(8 * tailleCase);
                t.right(90);
                t.forward(tailleCase);
                t.left(90);
                t.penDown();
            }
            if (isStartWithLightBrown) {
                //drawSquareAndFill(t, tailleCase, 255, 228, 196);
                drawSquareAndFill(t, tailleCase, 0, 0, 0);
                isStartWithLightBrown = false;
            } else {
                //drawSquareAndFill(t, tailleCase, 139, 69, 19);
                drawSquareAndFill(t, tailleCase, 255, 255, 255);
                isStartWithLightBrown = true;
            }
        }
    }

    void removePawn(Turtle t, int index) {
        int[] coordinates = indexToCoordinates(index);
        t.moveTo(coordinates[0] * tailleCase, coordinates[1] * tailleCase);
        //drawSquareAndFill(t, tailleCase, 255, 255, 255);

        drawSquareAndFill(t, tailleCase, 255, 255, 255);
    }

    void initialisation(int[] boardToPrint) {
        for (int i = 0; i < 64; i++) {
            switch (boardToPrint[i]) {
                case -1:
                    drawPawn(turtle, i, tailleCase, 0, 0, 255);
                    break;
                case 1:
                    drawPawn(turtle, i, tailleCase, 255, 0, 0);
                    break;
                default:
                    break;
            }
        }
    }

    void printBoard(int[] boardToPrint) {
        Turtle newTurtle = initTurtle();

        drawBoard(newTurtle, tailleCase);

        

        for (int i = 0; i < 64; i++) {
            switch (boardToPrint[i]) {

                case 0:
                System.out.println("0 found" + i);
                    removePawn(newTurtle, i);
                    break;
                case -1:
                    drawPawn(newTurtle, i, tailleCase, 0, 0, 255);
                    break;
                case 1:
                    drawPawn(newTurtle, i, tailleCase, 255, 0, 0);
                    break;
                // king case 1
                case 2:
                    break;
                // king case -1
                case -2:
                    break;
                default:
                    break;
            }

        }
    }
}
