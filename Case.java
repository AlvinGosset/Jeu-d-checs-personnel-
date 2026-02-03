public class Case{
    private int ligne;
    private String colonne;
    private Piece occupe;

    public Case(){
        this.ligne = 0;
        this.colonne = "";
        this.occupe = null;
    }
    public Case(int lig, String col, Piece piece){
        this.ligne = lig;
        this.colonne = col;
        this.occupe = piece;
    }

    public Case(Case c){
        this.ligne = c.getLigne();
        this.colonne = c.getColonne();
        this.occupe = c.getOccupee();
    }

    public Piece getOccupee(){
        return this.occupe;
    }
    
    public String afficherContenu(){
        if(this.occupe != null){
            return this.occupe.afficherNom();
        }
        return "  ";
        
    }

    public String afficherNom(){
        return this.colonne + this.ligne;
    }
    public void setOccupee(Piece piece){
        this.occupe = piece;
    }

    public String getColonne(){
        return this.colonne;
    }

    public void setColonne(String col){
        this.colonne = col;
    }

    public int getLigne(){
        return this.ligne;
    }

    public void setLigne(int lig){
        this.ligne = lig;
    }

    public String toString(){
        return "Coordonnées : (" + this.ligne + ";" + this.colonne + "); " + "Occupé: " + this.occupe + ";";
    }
}