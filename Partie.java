import java.util.Scanner;

public class Partie {
    private Echiquier echiquier;
    private Joueur[] joueurActuel;
    private int tour;
    private Scanner scanner;
    private boolean fin;

    public Partie() {
        this.echiquier = new Echiquier();
        this.scanner = new Scanner(System.in);
        System.out.print("Entrez le nom du joueur 1 (Blanc): ");
        String nomJoueur1 = scanner.nextLine();
        System.out.print("Entrez le nom du joueur 2 (Noir): ");
        String nomJoueur2 = scanner.nextLine();
        this.joueurActuel = new Joueur[2];
        this.joueurActuel[0] = new Joueur(nomJoueur1, "Blanc");
        this.joueurActuel[1] = new Joueur(nomJoueur2, "Noir");
        this.tour = 0;
        this.fin = false;
    }

    public void lancer() {
        while (!fin) {
            this.echiquier.afficher();
            Joueur joueur = this.joueurActuel[tour];
            String couleur = joueur.getCouleur();
            System.out.println("Tour de: " + joueur);

            if(this.estEchecEtMat(couleur)){
                Joueur gagnant = (tour == 0) ? this.joueurActuel[1] : this.joueurActuel[0];
                System.out.println("Partie terminée, Echec et Mat pour " + gagnant.getNom() + " !");
                break;
            }
            if(this.estPat(couleur)){
                System.out.println("Partie terminée, Pat !");
                break;
            }
            if(this.estEnEchec(this.trouverRoi(couleur))){
                System.out.println("Votre roi est en échec.");
            }

            boolean coupValide = false;
            while(!coupValide){
                System.out.print("Entrez la position de la piece a deplacer (ex: 2 B): ");
                int ligneDepart = this.scanner.nextInt();
                String colonneDepart = this.scanner.next().toUpperCase();

                Case caseDepart = this.echiquier.getCase(ligneDepart, colonneDepart);
                if(caseDepart == null){
                    System.out.println("Case de départ invalide.");
                    continue;
                }

                Piece piece = caseDepart.getOccupee();
                if (piece == null || !piece.getCouleur().equals(couleur)) {
                    System.out.println("Aucune piece valide a cet endroit.");
                    continue;
                }

                System.out.print("Entrez la position d'arrivee (ex: 3 B): ");
                int ligneArrivee = this.scanner.nextInt();
                String colonneArrivee = this.scanner.next().toUpperCase();

                Case caseArrivee = this.echiquier.getCase(ligneArrivee, colonneArrivee);
                if(caseArrivee == null){
                    System.out.println("Case d'arrivée invalide.");
                    continue;
                }

                if(!this.echiquier.deplacementValide(piece, caseArrivee)){
                    System.out.println("Déplacement invalide.");
                    continue;
                }

                if(!this.coupNeMetPasRoiEnEchec(piece, caseArrivee)){
                    System.out.println("Déplacement invalide, votre roi resterait en échec.");
                    continue;
                }

                this.echiquier.deplacerPiece(piece, caseArrivee);
                coupValide = true;
            }

            this.changerJoueur();
        }

        this.scanner.close();
    }

    private void changerJoueur() {
        if (tour == 0) {
            tour = 1;
        } else {
            tour = 0;
        }
    }

    private boolean estEchecEtMat(String couleur) {
        Roi roi = this.trouverRoi(couleur);
        if(roi == null){
            return false;
        }
        if(!this.estEnEchec(roi)){
            return false;
        }
        return !this.existeCoupLegal(couleur);
    }

    private boolean estPat(String couleur) {
        Roi roi = this.trouverRoi(couleur);
        if(roi == null){
            return false;
        }
        if(this.estEnEchec(roi)){
            return false;
        }
        return !this.existeCoupLegal(couleur);

    }

    private boolean existeCoupLegal(String couleur){
        for(int i = 0; i < this.echiquier.getPlateau().length; i++){
            for(int j = 0; j < this.echiquier.getPlateau()[i].length; j++){
                Piece piece = this.echiquier.getPlateau()[i][j].getOccupee();
                if(piece != null && piece.getCouleur().equals(couleur)){
                    for(int x = 0; x < this.echiquier.getPlateau().length; x++){
                        for(int y = 0; y < this.echiquier.getPlateau()[x].length; y++){
                            Case dest = this.echiquier.getPlateau()[x][y];
                            if(this.echiquier.deplacementValide(piece, dest) && this.coupNeMetPasRoiEnEchec(piece, dest)){
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean coupNeMetPasRoiEnEchec(Piece piece, Case destination){
        Case source = this.echiquier.getCase(piece.getLigne(), piece.getColonne());
        Piece capturee = destination.getOccupee();
        int oldLigne = piece.getLigne();
        String oldCol = piece.getColonne();

        source.setOccupee(null);
        destination.setOccupee(piece);
        piece.deplacer(destination);

        Roi roi = (piece instanceof Roi) ? (Roi) piece : this.trouverRoi(piece.getCouleur());
        boolean enEchec = this.estEnEchec(roi);

        piece.setLigne(oldLigne);
        piece.setColonne(oldCol);
        source.setOccupee(piece);
        destination.setOccupee(capturee);

        return !enEchec;
    }

    private boolean estEnEchec(Piece roi) {
        Case caseRoi = this.echiquier.getCase(roi.getLigne(), roi.getColonne());
        String enemyCouleur = roi.getCouleur().equals("Blanc") ? "Noir" : "Blanc";

        return this.echiquier.estEnPrise(caseRoi, enemyCouleur, false);
    }


    private Roi trouverRoi(String couleur) {
        for(int i = 0; i < this.echiquier.getPlateau().length; i++){
            for(int j = 0; j < this.echiquier.getPlateau()[i].length; j++){
                if(this.echiquier.getPlateau()[i][j].getOccupee() instanceof Roi && this.echiquier.getPlateau()[i][j].getOccupee().getCouleur().equals(couleur)){
                    Roi roi = (Roi) this.echiquier.getPlateau()[i][j].getOccupee();
                    return roi;
                }
            }
        }
        return null;
    }
}
