public class Tour extends Piece {
    public Tour(){
        super();
    }

    public Tour(int lig, String col, String color){
        super(lig, col, color);
    }

    public Tour(Tour tour){
        super(tour);
    }

    public String afficherNom(){
            if(this.couleur.equals("Blanc")){
                return "TB";
            }
            return "TN";
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
        int dy = Math.abs(ndest - ncol);
        return (dx == 0 && dy > 0) || (dy == 0 && dx > 0);
    }
    
    
    public String toString(){
        return "Tour " + this.couleur + ": [Position : (" + this.ligne + " ; " + this.col + ") " + "; Est en Vie : " + this.isAlive + ";];";
    }
}
