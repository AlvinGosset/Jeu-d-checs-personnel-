public abstract class Piece {
    protected String col;
    protected int ligne;
    protected String couleur;
    protected boolean isAlive;

    public Piece(int lig, String c, String color) {
        this.ligne = lig;
        this.col = c;
        this.isAlive = true;
        this.couleur = color;
    }

    public Piece() {
        this.ligne = 0;
        this.col = "";
        this.isAlive = true;
        this.couleur = "Blanc";
    }

    public Piece(Piece piece) {
        this.ligne = piece.getLigne();
        this.col = piece.getColonne();
        this.couleur = piece.getCouleur();
        this.isAlive = piece.getIsAlive();
    }

    public void deplacer(Case c){
        if(this.deplacement(c)){
            this.setLigne(c.getLigne());
            this.setColonne(c.getColonne());
        }else{
            System.out.println("Veuillez choisir un déplacement valide");
        }
    }

    // === Méthodes abstraites ===

    public abstract String afficherNom();

    public abstract boolean deplacement(Case destination);

    // === Getters / Setters ===

    public String getColonne() {
        return this.col;
    }

    public int getLigne() {
        return this.ligne;
    }

    public String getCouleur() {
        return this.couleur;
    }

    public boolean getIsAlive() {
        return this.isAlive;
    }

    public void setColonne(String colonne) {
        this.col = colonne;
    }

    public void setLigne(int lig) {
        this.ligne = lig;
    }

    public void setCouleur(String color) {
        this.couleur = color;
    }

    public void setIsAlive(boolean state) {
        this.isAlive = state;
    }

    public String toString() {
        return "Position : (" + this.ligne + " ; " + this.col + "); Couleur : " + this.couleur + "; Est en Vie : " + this.isAlive + ";";
    }
}