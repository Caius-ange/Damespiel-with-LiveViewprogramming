public class DameView {
    Turtle turtle;
    int quadratGroesse = 60;
    Spielfeld spielfeld;

    public DameView(Spielfeld spielfeld) {
        // Initialisation du tableau
        this.spielfeld = spielfeld;
        // Afficher le damier
        druckeBrett(this.spielfeld);
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

        for (int i = 0; i < (quadratGroesse / 3 - 5); i++) {
            zeichneKreis(t, (quadratGroesse / 3 - 5) - i / 3, r, g, b); // Appel pour dessiner un cercle plein
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

        for (int i = 0; i < (quadratGroesse / 3 - 5); i++) {
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
    // a4
    void druckeBrett(Spielfeld spielfeld) {
        Turtle newTurtle = initTurtle();

        zeichneBrett(newTurtle, quadratGroesse);

        for (int i = 0; i < 64; i++) {
            switch (spielfeld.getFeld()[i]) {

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

        zeichneInfo(spielfeld);
    }
     // a4

    void zeichneInfo(Spielfeld spielfeld) {

        int besterpunktestand = spielfeld.getBestenPunktestandVerwalten().getBesterPunktestand();
        int spPunkte = spielfeld.getSpieler1().getPunkte(); 
        int spSteine = spielfeld.getSpieler1().getAnzahlStein();
        int kiPunkte = spielfeld.getSpieler2().getPunkte(); 
        int kiSteine = spielfeld.getSpieler2().getAnzahlStein();

        // si le jeu est terminé, afficher le message de fin
        if (spielfeld.istSpielAmEnde()) {
            Turtle newInfoTurtle = new Turtle(480, 100);

            String text = "Der Gewinner ist " + spielfeld.getGewinner();
            String text2 = "Der Besterpunktestand: " + besterpunktestand;
            newInfoTurtle.penUp();
            newInfoTurtle.backward(229);
            newInfoTurtle.left(90);
            newInfoTurtle.forward(18);
            newInfoTurtle.right(90);
            newInfoTurtle.right(270);
            newInfoTurtle.text("Das Spiel ist bereits beendet.", Font.SANSSERIF, 20, Font.Align.LEFT);

            newInfoTurtle.backward(30);
            newInfoTurtle.right(90);
            newInfoTurtle.left(90);
            newInfoTurtle.text(text, Font.SANSSERIF, 20, Font.Align.LEFT);

            newInfoTurtle.backward(30);
            newInfoTurtle.right(90);
            newInfoTurtle.left(90);
            newInfoTurtle.text(text2, Font.SANSSERIF, 20, Font.Align.LEFT);

            return;

        } else {
            Turtle infoTurtle = new Turtle(480, 60);

            String spInfoText = "Spieler 1: Mensch      Punkte: " + spPunkte + "    Steine: " + spSteine;
            String kiInfoText = "Spieler 2: KI               Punkte: " + kiPunkte + "    Steine: " + kiSteine;
            infoTurtle.penUp();

            infoTurtle.backward(229);
            infoTurtle.left(90);
            infoTurtle.forward(8);
            infoTurtle.right(90);
            infoTurtle.right(270);
            infoTurtle.text(spInfoText, Font.SANSSERIF, 20, Font.Align.LEFT);

            infoTurtle.backward(30);
            infoTurtle.right(90);
            infoTurtle.left(90);
            infoTurtle.text(kiInfoText, Font.SANSSERIF, 20, Font.Align.LEFT);
        }
    }
}
