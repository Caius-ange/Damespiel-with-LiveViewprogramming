
   /*  // Déclaration des variables
    Turtle turtle = new Turtle();
    int tailleCase = 50; // Taille d'une case
    int lignes = 8; // Nombre de lignes
    int colonnes = 8; // Nombre de colonnes

    public Print() {
        // Position initiale pour le damier : la tortue est au centre et regarde à droite
        turtle.penUp();
        turtle.backward(colonnes * tailleCase / 2);  // Déplacement vers la gauche pour se positionner correctement
        turtle.right(90);  // Tourner pour aller vers le bas
        turtle.forward(lignes * tailleCase / 2);  // Déplacement vers le bas pour commencer à dessiner à partir du centre
        turtle.left(90);   // Tourner à nouveau pour revenir à la position de départ
        turtle.penDown();

        // Dessiner le damier avec des pions
        dessinerDamier();
    }

    // Méthode pour dessiner le damier
    public void dessinerDamier() {
        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {
                // Couleur des cases
                if ((i + j) % 2 == 0) {
                    turtle.color(139, 69, 19); // Marron foncé
                } else {
                    turtle.color(244, 164, 96); // Marron clair
                }
                drawSquare(turtle, tailleCase);

                // Placer les pions sur les cases foncées
                if ((i + j) % 2 == 0 && (i < 3 || i > 4)) {
                    if (i < 3) {
                        drawPawn(turtle, tailleCase, 0, 0, 0); // Pion noir
                    } else {
                        drawPawn(turtle, tailleCase, 255, 255, 255); // Pion blanc
                    }
                }

                // Passer à la case suivante
                turtle.penUp();
                turtle.forward(tailleCase);  // Avancer à la case suivante
                turtle.penDown();
            }
            // Revenir au début de la ligne et passer à la suivante
            turtle.penUp();
            turtle.backward(colonnes * tailleCase);  // Revenir à la position de départ de la ligne
            turtle.right(90);  // Tourner à droite pour aller à la ligne suivante
            turtle.forward(tailleCase);  // Avancer d'une case
            turtle.left(90);   // Tourner à gauche pour dessiner dans la bonne direction
            turtle.penDown();
        }
    }

    // Méthode pour dessiner une case carrée
    public static void drawSquare(Turtle t, int taille) {
        t.penDown();
        for (int i = 0; i < 4; i++) {
            t.forward(taille);  // Avancer pour dessiner un côté
            t.right(90);  // Tourner de 90° pour le prochain côté
        }
        t.penUp();
    }

    // Méthode pour dessiner un pion (cercle approximé avec des segments)
    public static void drawPawn(Turtle t, int tailleCase, int r, int g, int b) {
        t.penUp();
        t.forward(tailleCase / 2);  // Aller au centre de la case
        t.right(90);  // Tourner pour se positionner au centre
        t.forward(tailleCase / 2 - 5);  // S'ajuster légèrement pour éviter de dessiner sur les bords
        t.left(90);   // Tourner à nouveau pour dessiner dans la bonne direction
        t.color(r, g, b);  // Couleur du pion
        t.penDown();

        // Dessiner un cercle approximé (36 segments de 10 degrés chacun)
        for (int i = 0; i < 36; i++) {
            t.forward(2 * Math.PI * (tailleCase / 4) / 36);  // Avancer légèrement pour chaque segment
            t.right(10);  // Tourner de 10° à chaque fois
        }

        // Revenir à la position initiale
        t.penUp();
        t.backward(tailleCase / 2 - 5);  // Revenir au centre de la case
        t.left(90);  // Tourner pour retourner à la position initiale
        t.backward(tailleCase / 2);  // Revenir au départ
        t.right(90);  // Tourner pour rétablir l'orientation initiale
    }
    



// Déclaration des variables
Turtle turtle = new Turtle();
int tailleCase = 50; // Taille d'une case
int lignes = 8; // Nombre de lignes
int colonnes = 8; // Nombre de colonnes

public void leeresQuadrat (Turtle t, int tailleCase) {
    t.penDown();
    for (int i = 0; i < 4; i++) {
        t.forward(tailleCase);
        t.right(90);  
    }
    t.penUp();
    t.forward(tailleCase + 15);
}

public void gefülltesQuadrat(Turtle t, int tailleCase) {
    t.penDown();

    for (int i = 0; i < 4; i++) {
      t.forward(tailleCase);
      t.right(90);
    }

    for(int i=0; i<tailleCase ; i++){
        t.color(0,0,0);
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
    t.forward(tailleCase + 15);
}

void zeichnen(Turtle t, int lignes, int colonnes,int tailleCase, boolean[][] values){
    int totalWidth = colonnes * tailleCase + (colonnes - 1) * 15; 
    int totalHeight = lignes * tailleCase + (lignes - 1) * 15; 

    t.penUp();
    t.backward(totalWidth / 2); 
    t.right(90);
    t.backward(totalHeight / 2); 
    t.left(90);
    t.penDown();

    for(int i=0; i<lignes;i++){
        for(int j=0;j<colonnes;j++){
            if(values[i][j]){
                gefülltesQuadrat (t, tailleCase);
            }else{
                leeresQuadrat(t, tailleCase);
            }
        }

        t.penUp(); 
        t.backward((tailleCase + 15) * colonnes);
        t.right(90);
        t.forward(tailleCase + 15);  
        t.left(90);
        t.penDown();
    }
}*/
/* 
public class Print {
    // Déclaration des variables
    Turtle turtle = new Turtle(480,480);
    int tailleCase = 60; // Taille d'une case
    int lignes = 8; // Nombre de lignes
    int colonnes = 8; // Nombre de colonnes

    public Print() {
        // Position initiale pour commencer au coin supérieur gauche
        turtle.penUp();
        turtle.backward(240); // Déplacement à gauche
        turtle.right(90);
        turtle.forward(240); // Déplacement vers le haut
        turtle.right(90);
        turtle.penDown();

        // Dessiner le damier
        dessinerDamier();
    }

    public void dessinerDamier() {
        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {
                // Couleur des cases
                if ((i + j) % 2 == 0) {
                    turtle.color(244, 164, 96); // Marron clair
                } else {
                    turtle.color(139, 69, 19); // Marron foncé
                    remplirCase(tailleCase); // Remplir les cases marron foncé
                }
                drawSquare(tailleCase);

                // Placer les pions sur les cases marron foncé
                if ((i + j) % 2 != 0 && (i < 3 || i > 4)) {
                    if (i < 3) {
                        drawPawn(turtle,tailleCase, 255, 255, 255); // Pion blanc
                    } else {
                        drawPawn(turtle,tailleCase, 0,0, 0); // Pion noir
                    }
                }

                // Passer à la case suivante
                turtle.penUp();
                turtle.forward(tailleCase);
                turtle.penDown();
            }
            // Revenir au début de la ligne et passer à la suivante
            turtle.penUp();
            turtle.backward(colonnes * tailleCase);
            turtle.right(90);
            turtle.forward(tailleCase);
            turtle.left(90);
            turtle.penDown();
        }
    }

    // Méthode pour dessiner une case carrée
    public void drawSquare(int taille) {
        turtle.penDown();
        for (int i = 0; i < 4; i++) {
            turtle.forward(taille);
            turtle.right(90);
        }
        System.out.println("hi");
        turtle.penUp();
        turtle.forward(tailleCase);
     
    }

    // Méthode pour remplir une case (marron foncé)
    public void remplirCase(int taille) {
        turtle.penUp();
        turtle.right(90);
        for (int y = 0; y < taille; y++) {
            turtle.forward(1);
            turtle.left(90);
            turtle.color(139, 69, 19); // Couleur marron foncé
            turtle.penDown();
            turtle.forward(taille);
            turtle.penUp();
            turtle.backward(taille);
            turtle.right(90);
        }
        turtle.backward(taille);
        turtle.left(90);
    }

    // Méthode pour dessiner un pion (cercle approximé avec des segments)
     void drawPawn(Turtle t, int tailleCase, int r, int g, int b) {
        t.penUp();
        t.forward(tailleCase / 2);  // Aller au centre de la case
        t.right(90);  // Tourner pour se positionner au centre
        t.forward(tailleCase / 2 - 5);  // S'ajuster légèrement pour éviter de dessiner sur les bords
        t.left(90);   // Tourner à nouveau pour dessiner dans la bonne direction
        t.color(r, g, b);  // Couleur du pion
        t.penDown();

        // Dessiner un cercle approximé (36 segments de 10 degrés chacun)
        for (int i = 0; i < 36; i++) {
            t.forward(2 * Math.PI * (tailleCase / 4) / 36);  // Avancer légèrement pour chaque segment
            t.right(10);  // Tourner de 10° à chaque fois
        }

        // Revenir à la position initiale
        t.penUp();
        t.backward(tailleCase / 2 - 5);  // Revenir au centre de la case
        t.left(90);  // Tourner pour retourner à la position initiale
        t.backward(tailleCase / 2);  // Revenir au départ
        t.right(90);  // Tourner pour rétablir l'orientation initiale
    }
}*/

public class Print {
    // Déclaration des variables
    Turtle turtle = new Turtle(480,480);
    int tailleCase = 60; // Taille d'une case
    int lignes = 8; // Nombre de lignes
    int colonnes = 8; // Nombre de colonnes
    int[] board = new int[64];

    public Print() {
        // Position initiale pour commencer au coin supérieur gauche
        turtle.penUp();
        turtle.backward(240);  // Déplacement à gauche
        turtle.left(90);
        turtle.forward(240);  // Déplacement en haut
        turtle.right(90);
        turtle.penDown();

        // Dessiner le damier
       // initializeBrett();
        initializeBoardWithPawns(); 
    }

    
    // Méthode pour dessiner le damier avec les pions en utilisant gefuelltesQuadrat et drawPawn
    void initializeBoardWithPawns() {
        for (int i = 0; i < 64; i++) {
            if (i / 8 < 3 && (i + i / 8) % 2 == 1) {
                gefuelltesQuadrat(turtle, tailleCase, 139, 69, 19);
                drawPawn(turtle, tailleCase, 0, 0, 0);
            } else if (i / 8 > 4 && (i + i / 8) % 2 == 1) {
                gefuelltesQuadrat(turtle, tailleCase, 139, 69, 19);
                drawPawn(turtle, tailleCase, 255, 255, 255);
            } else {
                gefuelltesQuadrat(turtle, tailleCase, 244, 164, 96);
            }

            
        }
    }
      void initializeBrett(){
        for(int i=0; i<lignes; i++){
            for(int j=0; j<colonnes; j++){
                if((i+j)%2==0){
                    gefuelltesQuadrat(turtle, tailleCase, 244, 164, 96);
                    turtle.forward(tailleCase);
                }else{
                    gefuelltesQuadrat(turtle, tailleCase, 139, 69, 19);
                    turtle.forward(tailleCase);
                }
            }
            turtle.penUp();
            turtle.backward(colonnes*tailleCase);
            turtle.right(90);
            turtle.forward(tailleCase);
            turtle.left(90);
            turtle.penDown();
        }
      }

      
    /*// Méthode pour dessiner une case carrée
    public void drawSquare(int taille) {
        turtle.penDown();
        for (int i = 0; i < 4; i++) {
            turtle.forward(taille);
            turtle.right(90);
        }
        turtle.penUp();
        turtle.forward(10);
        
    }

    // Méthode pour remplir une case (marron foncé)
    public void remplirCase(int taille) {
        turtle.penUp();
        turtle.right(90);
        for (int y = 0; y < taille; y++) {
            turtle.forward(1);
            turtle.left(90);
            turtle.color(139, 69, 19); // Couleur marron foncé
            turtle.penDown();
            turtle.forward(taille);
            turtle.penUp();
            turtle.backward(taille);
            turtle.right(90);
        }
        turtle.backward(taille);
        turtle.left(90);
    }

    // Méthode pour dessiner un pion (cercle approximé avec des segments)
   public void drawPawn(int tailleCase, int r, int g, int b) {
        turtle.penUp();
        turtle.forward(tailleCase / 2); 
        turtle.right(90);
        turtle.forward(tailleCase / 4); 
        turtle.left(90);
        turtle.color(r, g, b);
        turtle.penDown();

        // Remplir le pion (cercle plein)
        for (int radius = tailleCase / 8; radius > 0; radius--) {
            for (int i = 0; i < 36; i++) {
                turtle.forward(2 * Math.PI * radius / 36);
                turtle.right(10);
            }
            turtle.penUp();
            turtle.backward(1);
            turtle.penDown();
        }

        // Revenir au centre de la case
        turtle.penUp();
        turtle.left(90);
        turtle.forward(tailleCase / 4); 
        turtle.right(90);
        turtle.backward(tailleCase / 2);
    }
*/
    public void gefuelltesQuadrat(Turtle t, int tailleCase,int r, int g, int b) {
        t.penDown();
    
        for (int i = 0; i < 4; i++) {
          t.forward(tailleCase);
          t.right(90);
        }
    
        for(int i=0; i<tailleCase ; i++){
            t.color(r,g,b);
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

    //method to draw a Pawn with a given color
    public void drawPawn(Turtle t, int tailleCase, int r, int g, int b) {
        t.penUp();
        t.forward(tailleCase / 2);  // Aller au centre de la case
        t.right(90);  // Tourner pour se positionner au centre
        t.forward(tailleCase / 2 - 5);  // S'ajuster légèrement pour éviter de dessiner sur les bords
        t.left(90);   // Tourner à nouveau pour dessiner dans la bonne direction
        t.color(r, g, b);  // Couleur du pion
        t.penDown();

        // Dessiner un cercle approximé (36 segments de 10 degrés chacun)
        for (int i = 0; i < 36; i++) {
            t.forward(2 * Math.PI * (tailleCase / 4) / 36);  // Avancer légèrement pour chaque segment
            t.right(10);  // Tourner de 10° à chaque fois
        }

        // Revenir à la position initiale
        t.penUp();
        t.backward(tailleCase / 2 - 5);  // Revenir au centre de la case
        t.left(90);  // Tourner pour retourner à la position initiale
        t.backward(tailleCase / 2);  // Revenir au départ
        t.right(90);  // Tourner pour rétablir l'orientation initiale
    }
    
}
