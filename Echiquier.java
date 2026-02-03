import java.util.ArrayList;

public class Echiquier {
    private Piece[] pieces_blanches;
    private Piece[] pieces_noires;
    private Case[][] plateau;
    private ArrayList<String> historique;
    public static String[] colonnes = {"A", "B", "C", "D", "E", "F", "G", "H"};

    public Echiquier(){
        this.pieces_blanches = new Piece[16];
        this.pieces_noires = new Piece[16];
        this.plateau = new Case[8][8];
        this.historique = new ArrayList<String>();

        this.pieces_blanches[0] = new Tour(1,Echiquier.colonnes[0], "Blanc");
        this.pieces_blanches[7] = new Tour(1,Echiquier.colonnes[7], "Blanc");
        this.pieces_blanches[1] = new Cavalier(1, Echiquier.colonnes[1], "Blanc");
        this.pieces_blanches[6] = new Cavalier(1, Echiquier.colonnes[6], "Blanc");
        this.pieces_blanches[2] = new Fou(1,Echiquier.colonnes[2],"Blanc");
        this.pieces_blanches[5] = new Fou(1, Echiquier.colonnes[5], "Blanc");
        this.pieces_blanches[3] = new Dame(1, Echiquier.colonnes[3], "Blanc");
        this.pieces_blanches[4] = new Roi(1, Echiquier.colonnes[4], "Blanc");

        this.pieces_noires[0] = new Tour(8,Echiquier.colonnes[0], "Noir");
        this.pieces_noires[7] = new Tour(8,Echiquier.colonnes[7], "Noir");
        this.pieces_noires[1] = new Cavalier(8, Echiquier.colonnes[1], "Noir");
        this.pieces_noires[6] = new Cavalier(8, Echiquier.colonnes[6], "Noir");
        this.pieces_noires[2] = new Fou(8,Echiquier.colonnes[2],"Noir");
        this.pieces_noires[5] = new Fou(8, Echiquier.colonnes[5], "Noir");
        this.pieces_noires[3] = new Dame(8, Echiquier.colonnes[3],"Noir");
        this.pieces_noires[4] = new Roi(8, Echiquier.colonnes[4], "Noir");

        for(int i = 0; i<8; i++){
            this.pieces_blanches[i + 8] = new Pion(2, Echiquier.colonnes[i], "Blanc");
            this.pieces_noires[i + 8] = new Pion(7, Echiquier.colonnes[i], "Noir");
        }
        
        for(int j = 0; j < 8; j++){
            Case[] ligne = new Case[8];
            for(int i = 0; i < 8; i++){
                ligne[i] = new Case(j+1, Echiquier.colonnes[i], null);
            }
            this.plateau[j] = ligne;
        }
        for(int i = 0; i<8; i++){
            this.plateau[0][i].setOccupee(this.pieces_blanches[i]);
            this.plateau[1][i].setOccupee(this.pieces_blanches[i+8]);

            this.plateau[7][i].setOccupee(this.pieces_noires[i]);
            this.plateau[6][i].setOccupee(this.pieces_noires[i+8]);
        }
    }

    public void afficher() {
        System.out.println("  +---+---+---+---+---+---+---+---+");
        for (int i = 7; i >= 0; i--) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < plateau[i].length; j++) {
                String contenu = plateau[i][j].afficherContenu();
                // On s'assure que le contenu est sur 1 caractère
                System.out.print("| " + (contenu.length() == 1 ? contenu + " " : contenu));
            }
            System.out.println("|");
            System.out.println("  +---+---+---+---+---+---+---+---+");
        }
        System.out.println("    A   B   C   D   E   F   G   H");
    }

    public boolean deplacerPiece(Piece piece, Case nouvelleCase){
        if(piece == null || nouvelleCase == null){
            System.out.println("La case cible n'existe pas.");
            return false;
        }
        if(!this.deplacementValide(piece, nouvelleCase)){
            System.out.println("Déplacement invalide.");
            return false;
        }

        Case cAncienne = this.getCase(piece.getLigne(), piece.getColonne());
        Piece capturee = nouvelleCase.getOccupee();

        if(capturee != null){
            capturee.setIsAlive(false);
        }
        cAncienne.setOccupee(null);
        nouvelleCase.setOccupee(piece);
        piece.deplacer(nouvelleCase);

        if(piece instanceof Pion){
            ((Pion) piece).setFirstMove(false);
        }

        String coup = cAncienne.afficherNom() + piece.afficherNom() + "->";
        if(capturee != null){
            coup += "X" + capturee.afficherNom();
        }
        coup += nouvelleCase.afficherNom();
        this.addToHistorique(coup);
        return true;
    }

    public boolean deplacementValide(Piece piece, Case nouvelleCase){
        if(piece == null || nouvelleCase == null){
            return false;
        }
        if(nouvelleCase.getOccupee() != null && nouvelleCase.getOccupee().getCouleur().equals(piece.getCouleur())){
            return false;
        }

        if(piece instanceof Pion){
            return this.deplacementValidePion((Pion) piece, nouvelleCase);
        }

        if(piece instanceof Cavalier || piece instanceof Roi){
            return piece.deplacement(nouvelleCase);
        }

        Case[] chemin = this.trouverChemin(piece, nouvelleCase);
        return chemin != null && this.cheminLibre(chemin);
    }

    private boolean deplacementValidePion(Pion pion, Case nouvelleCase){
        int ncol = indexColonne(pion.getColonne());
        int ndest = indexColonne(nouvelleCase.getColonne());
        if(ncol == -1 || ndest == -1){
            return false;
        }
        int dCol = ndest - ncol;
        int dLigne = nouvelleCase.getLigne() - pion.getLigne();
        int dir = pion.getCouleur().equals("Blanc") ? 1 : -1;

        if(dCol == 0){
            if(nouvelleCase.getOccupee() != null){
                return false;
            }
            if(dLigne == dir){
                return true;
            }
            if(dLigne == 2 * dir && pion.getFirstMove()){
                Case intermediaire = this.getCase(pion.getLigne() + dir, Echiquier.colonnes[ncol]);
                return intermediaire != null && intermediaire.getOccupee() == null;
            }
            return false;
        }

        if(Math.abs(dCol) == 1 && dLigne == dir){
            return nouvelleCase.getOccupee() != null && !nouvelleCase.getOccupee().getCouleur().equals(pion.getCouleur());
        }
        return false;
    }

    private boolean cheminLibre(Case[] chemin){
        for(int i = 0; i < chemin.length; i++){
            if(chemin[i] != null && chemin[i].getOccupee() != null){
                return false;
            }
        }
        return true;
    }

    public void addToHistorique(String lastMove){
        historique.add(lastMove);
    }

    public boolean estEnPrise(Case c, String couleur){
        return this.estEnPrise(c, couleur,  false);
    }

    public boolean estEnPrise(Case c, String couleur, boolean ignoreKing){
        for(int i = 0; i<this.plateau.length; i++){
            for(int j = 0; j<this.plateau[i].length; j++){
                if(this.plateau[i][j].getOccupee() != null && this.plateau[i][j].getOccupee().getCouleur().equals(couleur)){
                    Piece p = this.plateau[i][j].getOccupee();
                    
                    if(ignoreKing && p instanceof Roi){
                        continue;
                    }

                    if(c.getOccupee() != null && c.getOccupee().getCouleur().equals(p.getCouleur())){
                        continue;
                    }

                    if(p instanceof Pion){
                        if(this.pionAttaqueCase((Pion) p, c)){
                            return true;
                        }
                        continue;
                    }

                    if(p instanceof Cavalier || p instanceof Roi){
                        if(p.deplacement(c)){
                            return true;
                        }
                        continue;
                    }

                    Case[] chemin = this.trouverChemin(p, c);
                    if (chemin != null && this.cheminLibre(chemin)) {
                        return true;
                    }
                }
                
            }
        }
        return false;
    }

    private boolean pionAttaqueCase(Pion pion, Case c){
        int nPionCol = indexColonne(pion.getColonne());
        int nCaseCol = indexColonne(c.getColonne());
        if(nPionCol == -1 || nCaseCol == -1){
            return false;
        }
        int dCol = nCaseCol - nPionCol;
        int dLigne = c.getLigne() - pion.getLigne();
        if(pion.getCouleur().equals("Blanc")){
            return dLigne == 1 && Math.abs(dCol) == 1;
        }
        return dLigne == -1 && Math.abs(dCol) == 1;
    }

    private int indexColonne(String col){
        for(int i = 0; i < Echiquier.colonnes.length; i++){
            if(Echiquier.colonnes[i].equals(col)){
                return i;
            }
        }
        return -1;
    }

    public Case[] trouverChemin(Piece p, Case c){
        if(p == null || c == null){
            return null;
        }
        int nColDepart = indexColonne(p.getColonne());
        int nColArrivee = indexColonne(c.getColonne());
        if(nColDepart == -1 || nColArrivee == -1){
            return null;
        }

        int dLigne = c.getLigne() - p.getLigne();
        int dCol = nColArrivee - nColDepart;
        int absLigne = Math.abs(dLigne);
        int absCol = Math.abs(dCol);

        if(dLigne == 0 && dCol == 0){
            return null;
        }

        if(p instanceof Tour){
            if(!((dLigne == 0) ^ (dCol == 0))){
                return null;
            }
        }else if(p instanceof Fou){
            if(absLigne != absCol){
                return null;
            }
        }else if(p instanceof Dame){
            if(!((dLigne == 0) ^ (dCol == 0)) && absLigne != absCol){
                return null;
            }
        }else{
            return null;
        }

        int stepLigne = Integer.signum(dLigne);
        int stepCol = Integer.signum(dCol);
        int longueur = Math.max(absLigne, absCol) - 1;
        Case[] chemin = new Case[longueur];
        int curLigne = p.getLigne() + stepLigne;
        int curCol = nColDepart + stepCol;
        for(int i = 0; i < longueur; i++){
            chemin[i] = this.getCase(curLigne, Echiquier.colonnes[curCol]);
            curLigne += stepLigne;
            curCol += stepCol;
        }
        return chemin;
    }

    public Case getCase(int x, String c){
        if((x <= this.plateau.length && x > 0) && (c.charAt(0) >= 'A' && c.charAt(0) <= 'H')){
            int y = 0;
            for(int i = 0; i < Echiquier.colonnes.length; i++){
                if(Echiquier.colonnes[i].equals(c)){
                    y = i;
                }
            }
            return this.plateau[x - 1][y];
        }
        return null;
    }

    public Piece getPieceNoire(int index){
        return this.pieces_noires[index];
    }

    public Piece getPieceBlanche(int index){
        return this.pieces_blanches[index];
    }

    public ArrayList<String> getHistorique(){
        return historique;
    }

    public void setCase(Case c){
        int y = 0;
        for(int i = 0; i < Echiquier.colonnes.length; i++){
            if(Echiquier.colonnes[i].equals(c.getColonne())){
                y = i;
            }
        }
        this.plateau[c.getLigne() - 1][y] = c;
    }

    public void setPieceNoire(int index, int x, String c){
        this.pieces_noires[index].setLigne(x);
        this.pieces_noires[index].setColonne(c);
    }

    public Case[][] getPlateau() {
        return plateau;
    }

    public void setPlateau(Case[][] p){
        this.plateau = p;
    }

    public void setPieceBlanche(int index, int x, String c){
        this.pieces_blanches[index].setLigne(x);
        this.pieces_blanches[index].setColonne(c);
    }

    public void setHistorique(ArrayList<String> Historique){
        this.historique = Historique;
    }
}
