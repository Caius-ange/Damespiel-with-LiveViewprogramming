import java.util.ArrayList;

public class dameSpiel {
    
}

 record Steine(String color){
   boolean isWhite(){
    return color.equals("w");
   }

   boolean isBlack(){
    return color.equals("b");
   }

   public String toString(){
    return color;
   }

}

class Square{
    int zeile ;
    int spalte;
    Steine stein;

    Square(int zeile,int spalte){
        this.zeile = zeile;
        this.spalte = spalte;
        this.stein = null;
    }

    Steine getSteine(){
        return stein;
    }

    boolean isEmpty(){
        return stein == null;
    }

    void setSteine(Steine stein){
        this.stein = stein;
    }

    void removeSteine(){
        this.stein =null;
       // System.out.println("Le pion a été retiré de la case (" + zeile + ", " + spalte + ")");
    }

    @Override
    public String toString(){
        return isEmpty() ? "*" : stein.toString();
    }
}

class BrettSpiel{
    private Square[][] squares;
    private Spieler spieler1;
    private Spieler spieler2;
    private Spieler aktuellerSpieler;

    BrettSpiel(){
        squares = new Square[8][8];
        spieler1 = new Spieler("spieler1",  "b");
        spieler2 = new Spieler("spieler2",  "w");
        aktuellerSpieler = spieler1;
        initializeBrett();
    }

    void initializeBrett() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                squares[x][y] = new Square(x, y); 
                if ((x + y) % 2 == 1) {
                    if (x < 3) {
                        Steine stein = new Steine("b");
                    squares[x][y].setSteine(stein);
                    spieler1.getInitStein().add(stein);
                    } else if (x > 4) {
                        Steine stein = new Steine("w");
                        squares[x][y].setSteine(stein);
                        spieler2.getInitStein().add(stein);                    }
                }
            }
        }
    }

    Spieler getAktuellerSpieler() {
        return aktuellerSpieler;
    }

    int getAnzahlStein(Spieler spieler) {
        return spieler.getInitStein().size();
    }
    Spieler getSpieler1() {
        return spieler1;
    }
    
    Spieler getSpieler2() {
        return spieler2;
    }        

    boolean isBewegung(int x, int y){
        return x>=0 && x<8 && y>=0 && y<8 ;
    }

    Square getSquare(int x, int y){
        if(!isBewegung(x, y)){
            throw new IllegalArgumentException("Position außerhalb des Spielfelds");
        }

        return squares[x][y];
    }

    void wechselSpieler(){
    //System.out.println("Changement de joueur...");
    aktuellerSpieler = (aktuellerSpieler == spieler1) ? spieler2 : spieler1;
    //System.out.println("Joueur actuel : " + aktuellerSpieler.getName());

    }

    int[][] zeigeZugMoeglichkeit(int x ,int y){
       ArrayList<int[]> moeglicheBewegung = new ArrayList<>();

       if(!isBewegung(x, y)){
        throw new IllegalArgumentException("Der ausgewaelte Stein ist außer das Brettspiel");
       }

       Square square = squares[x][y];

       if(square.isEmpty()){
        throw new IllegalArgumentException("Das feld ist leer");
       }

       int[] richtung = {-1,1};
       for(int dx:richtung){
         for(int dy:richtung){
           int newX = x+dx;
           int newY = y+dy;
          if(isBewegung(newX, newY) &&  squares[newX][newY].isEmpty()){
            moeglicheBewegung.add(new int[]{newX,newY});
          } else if(isBewegung(newX + dx, newY + dy)){
            int midX = newX;
            int midY = newY;
            Square sMid = squares[midX][midY];
              if(!sMid.isEmpty() && !sMid.getSteine().color().equals(aktuellerSpieler.getFarbe()) && squares[newX + dx][newY + dy].isEmpty()) {
                moeglicheBewegung.add(new int[]{newX + dx, newY + dy});
               }
            }
         }
        }

        if (moeglicheBewegung.size() == 0) {
        return new int[0][0];
        }

        int[][] ergebnis = new int[moeglicheBewegung.size()][2];
        for (int i = 0; i < moeglicheBewegung.size(); i++) {
            ergebnis[i] = moeglicheBewegung.get(i);
        }
        return ergebnis;
    }

    boolean moveSteine(int x, int y, int v, int w){
        if(!isBewegung(x,y) || !isBewegung(v,w)){
           throw new IllegalArgumentException("Die Bewegung außerhalb des Spielfelds ist unmöglich");
        }

        Square s1 = squares[x][y];
        Square s2 = squares[v][w];

        if(s1.isEmpty() || !s1.getSteine().color().equals(aktuellerSpieler.getFarbe())){
            throw new IllegalArgumentException("es gibt keinen Stein zu bewegen oder Sie sind nicht dran");
        }

       // System.out.println("Joueur actuel : " + aktuellerSpieler.getName() + " tente de déplacer " + s1.getSteine().toString());


        int[][] moeglicheBewegung = zeigeZugMoeglichkeit(x,y);
        boolean zugErlaubt = false;
        for (int[] zug : moeglicheBewegung) {
           if (zug[0] == v && zug[1] == w) {
            zugErlaubt = true;
            break;
           }
        }

        if (!zugErlaubt) {
          throw new IllegalArgumentException("Die Bewegung ist nicht erlaubt");
        }

        if(Math.abs(v - x) == 2 && Math.abs(w - y) == 2){
            int midX = (x+v) / 2;
            int midY = (y+w) / 2;
            Square sMid = squares[midX][midY];
    
             if(!sMid.isEmpty() && !sMid.getSteine().color().equals(aktuellerSpieler.getFarbe())){
                Steine capturedStein = sMid.getSteine();
                sMid.removeSteine();
                Spieler gegner = (aktuellerSpieler == spieler1) ? spieler2 : spieler1;
                gegner.zurueckZiehenStein((capturedStein));
                aktuellerSpieler.addPunkte();
    
             }
    
            }


       /*  if(!s2.isEmpty()){
            throw new IllegalArgumentException("Das Feld ist besetzt");
        }
*/
        s2.setSteine(s1.getSteine());
        s1.removeSteine();
        wechselSpieler();
        return true;
    }

    boolean isGameOver(){
        while(true){
           if(getAnzahlStein(spieler1) == 0 || getAnzahlStein(spieler2) == 0){
            return true; 
            
           }
        }
    }

 @Override

   public String toString(){
    StringBuilder builder = new StringBuilder();

    builder.append("Aktueller Spieler : ").append(aktuellerSpieler.toString()).append("\n");


    builder.append("\n");

   
    for (int y = 0; y < 8; y++) {
        builder.append(y).append(" ");
    }
    builder.append("\n").append("\n");
    //builder.append("  ");
    for (int x = 0; x < 8; x++) {
        
        for (int y = 0; y < 8; y++) {
            builder.append(squares[x][y].toString()).append(" ");  
        }
        builder.append("\n"); 
    }
    
    return builder.toString();
    

   }
}


class Spieler{
    private String name;
    private String farbe;
    private int punkte;
     ArrayList<Steine> initstein;
     //boolean hatGespielt;

    Spieler(String name , String farbe){
        this.name = name;
        this.farbe = farbe;
        this.punkte = 0;
        initstein = new ArrayList<>();
        //hatGespielt = false;
    }

    String getFarbe(){
        return farbe;
    }

    String getName(){
        return name;
    }

    int getPunkte(){
        return punkte;
    }

    ArrayList<Steine> getInitStein(){
        return initstein;

    }

    void addPunkte(){
        punkte += 2;
        //System.out.println(name + " a maintenant " + punkte + " Punkte.");
    }

    void zurueckZiehenStein(Steine stein){
        initstein.remove(stein);
    }

   /*  void resetSpiel(){
        hatGespielt = false;
    }*/


    boolean spiel(int x, int y, int v, int w, BrettSpiel brett){
        
       return brett.moveSteine(x, y, v, w);

      /* if(bewegung){
        int midX = (x+v) / 2;
        int midY = (y+w) / 2;
        Square sMid =brett.getSquare(midX, midY);

         if(!sMid.isEmpty() && !sMid.getSteine().color().equals(getFarbe())){
            Steine capturedStein = sMid.getSteine();
            sMid.removeSteine();
            gegner.zurueckZiehenStein((capturedStein));
            this.addPunkte();

         }

        }*/
       
      // return bewegung;  
    }

    @Override
    public String toString(){
        return name + " " + "(" + farbe + ") -- Punkte : " + punkte;
    }
}