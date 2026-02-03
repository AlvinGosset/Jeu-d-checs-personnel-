public class Cavalier extends Piece {
    public Cavalier(){
        super();
    }

    public Cavalier(int lig, String col, String color){
        super(lig, col, color);
    }

    public Cavalier(Cavalier cavalier){
        super(cavalier);
    }

    public String afficherNom(){
        if(this.couleur.equals("Blanc")){
            return "CB";
        }
        return "CN";
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
        return (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
    }

    public String toString(){
        return "Cavalier " + this.couleur + ": [Position : (" + this.ligne + " ; " + this.col + ") " + "; Est en Vie : " + this.isAlive + ";];";
    }
}
