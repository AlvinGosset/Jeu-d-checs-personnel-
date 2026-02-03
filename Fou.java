public class Fou extends Piece {
    public Fou(){
        super();
    }

    public Fou(int lig, String col, String color){
        super(lig, col, color);
    }

    public Fou(Fou fou){
        super(fou);
    }

    public String afficherNom(){
        if(this.couleur.equals("Blanc")){
            return "FB";
        }
        return "FN";
    }

    public boolean deplacement(Case destination) {
        int dx = Math.abs(this.ligne - destination.getLigne());
        String[] colonnes = {"A", "B", "C", "D", "E", "F", "G", "H"};
        int ncol = 0;
        int ndest = 0;
        for (int i = 0; i < colonnes.length; i++) {
            if (colonnes[i].equals(this.col)) {
                ncol = i + 1;
            }
            if (colonnes[i].equals(destination.getColonne())) {
                ndest = i + 1;
            }
        }
        int dy = Math.abs(ncol - ndest);
        return dx == dy && dx > 0;
    }

    public String toString(){
        return "Fou " + this.couleur + ": [Position : (" + this.ligne + " ; " + this.col + ") " + "; Est en Vie : " + this.isAlive + ";];";
    }
}
