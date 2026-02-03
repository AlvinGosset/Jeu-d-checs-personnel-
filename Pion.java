public class Pion extends Piece {
    private boolean firstMove;

    public Pion(){
        super();
        this.firstMove = true;
    }

    public Pion(int lig, String col, String color){
        super(lig, col, color);
        this.firstMove = true;
    }

    public Pion(Pion pion){
        super(pion);
        this.firstMove = pion.firstMove;
    }
    
    public boolean deplacement(Case destination) {
        int dy = destination.getLigne() - this.ligne;
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
        int dx = ndest - ncol;
        if (this.couleur.equals("Blanc")) {
            return (dy == 1 && dx == 0) || (dy == 1 && dx == 1) || (dy == 2 && dx == 0) || (dy == 1 && dx == -1);
        }
        return (dy == -1 && dx == 0) || (dy == -1 && dx == 1) || (dy == -2 && dx == 0) || (dy == -1 && dx == -1);
    }

    public String afficherNom(){
        if(this.couleur.equals("Blanc")){
            return "PB";
        }
        return "PN";
    }

    public boolean getFirstMove() {
        return this.firstMove;
    }

    public void setFirstMove(boolean firstMove) {
        this.firstMove = firstMove;
    }
    
    public String toString(){
        return "Pion " + this.couleur + ": [Position : (" + this.ligne + " ; " + this.col + ") " + "; Est en Vie : " + this.isAlive + ";];";
    }
}
