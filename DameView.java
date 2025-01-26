public class DameView {
    Turtle turtle = new Turtle(480, 480);
    Turtle infoTurtle = new Turtle(480, 60);

    int quadratGroesse = 60;
    Spielfeld spielfeld;

    public DameView(Spielfeld spielfeld) {
        this.spielfeld = spielfeld;
        druckeBrett(this.spielfeld);
    }

    Turtle initTurtle() {
        turtle.penUp();
        turtle.backward(240);
        turtle.left(90);
        turtle.forward(240);
        turtle.right(90);
        turtle.penDown();

        return turtle;
    }

    public void zeichneQuadratUndFuelle(int quadratGroesse, int r, int g, int b) {
        turtle.penDown();

        for (int i = 0; i < 4; i++) {
            turtle.forward(quadratGroesse);
            turtle.right(90);
        }

        for (int i = 0; i < quadratGroesse; i++) {
            turtle.color(r, g, b);
            turtle.penUp();
            turtle.right(90);
            turtle.forward(1);
            turtle.left(90);
            turtle.penDown();
            turtle.forward(quadratGroesse);
            turtle.penUp();
            turtle.backward(quadratGroesse);
        }
        turtle.penUp();
        turtle.right(90);
        turtle.backward(quadratGroesse);
        turtle.left(90);
        turtle.forward(quadratGroesse);
    }

    public void zeichneStein(int index, int quadratGroesse, int r, int g, int b) {
        turtle.penUp();
        int[] koordinat = indexZuKoordinaten(index);
        turtle.moveTo(koordinat[0] * quadratGroesse, koordinat[1] * quadratGroesse);

        turtle.forward(quadratGroesse / 2);
        turtle.right(90);
        turtle.forward(quadratGroesse / 3 - 5);
        turtle.left(90);

        for (int i = 0; i < (quadratGroesse / 3 - 5); i++) {
            zeichneKreis((quadratGroesse / 3 - 5) - i / 3, r, g, b);
        }

    }

    public void zeichneKoenigen(int index, int quadratGroesse, int r, int g, int b) {
        turtle.penUp();
        int[] koordinat = indexZuKoordinaten(index);
        turtle.moveTo(koordinat[0] * quadratGroesse, koordinat[1] * quadratGroesse);

        turtle.forward(quadratGroesse / 2);
        turtle.right(90);
        turtle.forward(quadratGroesse / 3 - 5);
        turtle.left(90);

        for (int i = 0; i < (quadratGroesse / 3 - 5); i++) {
            zeichneKreis((quadratGroesse / 3 - 5) - i, r, g, b);
        }

    }

    int[] indexZuKoordinaten(int index) {
        int[] koordinat = new int[2];
        koordinat[0] = index % 8;
        koordinat[1] = index / 8;
        return koordinat;
    }

    void zeichneKreis(int kreisGroesse, int r, int g, int b) {
        turtle.color(r, g, b);
        turtle.penDown();

        int segments = 36;
        double angle = 360.0 / segments;
        double step = 2 * Math.PI * kreisGroesse / segments;

        for (int i = 0; i < segments; i++) {
            turtle.forward(step);
            turtle.right(angle);
        }

        turtle.penUp();
    }

    void zeichneBrett() {
        boolean beginntMitHellBraun = true;

        for (int i = 0; i < 64; i++) {
            if (i % 8 == 0 && i != 0) {
                beginntMitHellBraun = !beginntMitHellBraun;
                turtle.penUp();
                turtle.backward(8 * quadratGroesse);
                turtle.right(90);
                turtle.forward(quadratGroesse);
                turtle.left(90);
                turtle.penDown();
            }
            if (beginntMitHellBraun) {
                zeichneQuadratUndFuelle(quadratGroesse, 255, 228, 196);
                beginntMitHellBraun = false;
            } else {
                zeichneQuadratUndFuelle(quadratGroesse, 139, 69, 19);
                beginntMitHellBraun = true;
            }
        }
    }

    // a4
    void druckeBrett(Spielfeld spielfeld) {
        if (turtle == null) {
            turtle = initTurtle();
            zeichneInfo(spielfeld); 

        } else {
            turtle.reset(); 
            turtle = initTurtle();   
        }
        if(!spielfeld.istSpielAmEnde()){
            infoTurtle.reset();
            zeichneInfo(spielfeld); 
        }else{
            zeichneInfo(spielfeld); 
        }
        zeichneBrett();         
        steinSetzen(spielfeld);  
    }
    
    // a4

    void steinSetzen(Spielfeld spielfeld){
        for (int i = 0; i < 64; i++) {
            switch (spielfeld.getFeld()[i]) {

                case -1:
                    zeichneStein(i, quadratGroesse, 0, 0, 0);
                    break;
                case 1:
                    zeichneStein(i, quadratGroesse, 255, 255, 255);
                    break;
                case 2:
                    zeichneKoenigen(i, quadratGroesse, 255, 255, 255);
                    break;
                case -2:
                    zeichneKoenigen(i, quadratGroesse, 0, 0, 0);
                    break;
                default:
                    break;
            }

        }
    }

    void zeichneInfo(Spielfeld spielfeld) {
        
        int besterpunktestand = spielfeld.getBestenPunktestandVerwalten().getBesterPunktestand();
        int spPunkte = spielfeld.getSpieler1().getPunkte();
        int spSteine = spielfeld.getSpieler1().getAnzahlStein();
        int kiPunkte = spielfeld.getSpieler2().getPunkte();
        int kiSteine = spielfeld.getSpieler2().getAnzahlStein();

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
