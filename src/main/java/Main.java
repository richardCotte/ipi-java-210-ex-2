import java.util.Scanner;

public class Main
{
    static final short MAX_PTS_VIE = 100;
    static final short PTS_BOUCLIER = 25;
    static final short MAX_ATTAQUE_ENNEMI = 5;
    static final short MAX_VIE_ENNEMI = 30;
    static final short MAX_ATTAQUE_JOUEUR = 5;
    static final short REGENERATION_BOUCLIER_PAR_TOUR = 10;

    static String nomPersonnage;
    static short ptsDeVie;
    static short ptsBouclier;
    static short nbEnnemisTues = 0;
    static boolean bouclierActif = true;

    /**
     * Méthode qui demande au joueur son nom et ensuite initialise ses points de vie et de bouclier a leur valeures
     * maximales
     */
    static void initPersonnage()
    {
        System.out.println("Saisir le nom de votre personnage");
        Scanner scan = new Scanner(System.in);
        nomPersonnage = scan.nextLine();
        ptsDeVie = MAX_PTS_VIE;
        ptsBouclier = PTS_BOUCLIER;
        System.out.println("OK " + Util.color(nomPersonnage , Color.GREEN) + " ! C'est parti !");
    }

    /**
     * Méthode qui simule un essaie avec un pourcentage
     * @param X chance de succès
     * @return si il y a succès
     */
    static boolean hasard(double X)
    {
        return Math.random() < X;
    }

    /**
     * Méthode qui retourne un short entre 0 et un nombre (born) donné
     * @param born la borne
     * @return un short entre 0 et la borne
     */
    static short nombreAuHasard(short born)
    {
        return (short) Math.round(born * Math.random());
    }

    /**
     * Methode qui calcule les dégats que le joueur a infligé a un énnemis
     * @param pvEnnemi pv ennemi restant
     * @return pv ennemi restant
     */
    static short attaqueJoueur(short pvEnnemi)
    {
        short pvRetire = nombreAuHasard(MAX_ATTAQUE_JOUEUR);
        pvEnnemi = (short) (pvEnnemi - pvRetire);
        System.out.println(Util.color(nomPersonnage , Color.GREEN) + " attaque l'" +
                Util.color("ennemi" , Color.YELLOW) + " ! Il lui fait perdre " +
                Util.color(pvRetire , Color.PURPLE) + " points de dommages");
        return pvEnnemi;
    }

    /**
     * Méthode qui montre les pv et le bouclier restant du joueur
     */
    static void afficherPersonnage()
    {
        if (bouclierActif) {
            System.out.print(Util.color(nomPersonnage, Color.GREEN) + " (" +
                    Util.color((short) Math.max(ptsDeVie, 0), Color.RED) + " " +
                    Util.color(ptsBouclier, Color.BLUE) + ")");
        }
        else {
            System.out.print(Util.color(nomPersonnage, Color.GREEN) + " (" +
                    Util.color((short) Math.max(ptsDeVie, 0), Color.RED) + ")");
        }
    }

    /**
     * Méthode qui calcule les dégats qu'un ennemi inflige au joueur
     */
    static void attaqueEnnemi()
    {
        short dgtEnnemi = nombreAuHasard(MAX_ATTAQUE_ENNEMI);
        // si le joueur n'a plus de bouclier, en enlève des points de vie
        if (ptsBouclier == 0) {
            ptsDeVie = (short) (ptsBouclier - dgtEnnemi + ptsDeVie);
            System.out.println("L'" + Util.color("ennemi" , Color.YELLOW) + " attaque " +
                    Util.color(nomPersonnage , Color.GREEN) + " ! Il lui fait " + dgtEnnemi + " points de dommages ! " +
                    Util.color(nomPersonnage , Color.GREEN) + " perd " + Util.color(dgtEnnemi , Color.RED) +
                    " points " + "de vie !");
        }
        // si l'ennemi inflige plus de dégats que le joueur n'a de bouclier, on enlève d'abord des points de bouclier
        // et le reste de l'attaque vas enlevé des pv
        else if (ptsBouclier < dgtEnnemi) {
            ptsDeVie = (short) (ptsDeVie + (ptsBouclier - dgtEnnemi));
            System.out.println("L'" + Util.color("ennemi" , Color.YELLOW) + " attaque " +
                    Util.color(nomPersonnage , Color.GREEN) + " ! Il lui fait " + dgtEnnemi + " points de dommages !" +
                    " Le bouclier perd " + Util.color(ptsBouclier , Color.BLUE) + " points. " +
                    Util.color(nomPersonnage , Color.GREEN) + " perd " +
                    Util.color((short) ((ptsBouclier - dgtEnnemi)*-1) , Color.RED) + " points de vie !");
            ptsBouclier = 0;
        }
        // si le joueur a un bouclier, le bouclier prend les dégats avant la vie
        else {
            ptsBouclier = (short) (ptsBouclier - dgtEnnemi);
            System.out.println("L'" + Util.color("ennemi" , Color.YELLOW) + " attaque " +
                    Util.color(nomPersonnage , Color.GREEN) + " ! Il lui fait " + dgtEnnemi + " points de dommages !" +
                    " Le bouclier perd " + Util.color(dgtEnnemi , Color.BLUE) + " points.");
        }
    }

    /**
     * Methode qui combine l'attaque de l'ennemie et l'attaque du joueur
     * @param ptsEnnemi points de vite ennemi
     * @param joueurJoue booléen disant si c'est au tour du joueur ou pas
     * @return les pv de l'ennemi
     */
    static short attaque(short ptsEnnemi,boolean joueurJoue)
    {
        if (ptsEnnemi > 0 && ptsDeVie > 0) {
            if (joueurJoue) {
                // on propose au joueur de pouvoir réparer sont bouclier si il est désactivé, ce qui a 1/3 de chance
                // d'aboutir.
                if (!bouclierActif) {
                    System.out.println("Votre bouclier est cassé... Tapez 'Y' si vous tentez de le réparer, tapez" +
                            "autre chose si vous ne voulez pas essayer");
                    Scanner scan = new Scanner(System.in);
                    String response = scan.nextLine();
                    if (response.equals("Y")) {
                        if (hasard(0.33)) {
                            System.out.println("Vous avez réussis ! Votre bouclier est maintenant fonctionnel " +
                                    "et au max");
                            bouclierActif = true;
                            ptsBouclier = PTS_BOUCLIER;
                        }
                        else {
                            System.out.println("La tantative de réparation de bouclier a échouée, vous passez votre " +
                                    "tour");
                        }
                    }
                    else {
                        System.out.println("D'accord, le jeu continu...");
                        ptsEnnemi = attaqueJoueur(ptsEnnemi);
                    }
                }
                else {
                    ptsEnnemi = attaqueJoueur(ptsEnnemi);
                }
            }
            else {
                attaqueEnnemi();
                // L'ennemi a une chance sur dix de désactiver le bouclier du joueur lors de son attaque
                if (hasard(0.1)) {
                    bouclierActif = false;
                    ptsBouclier = 0;
                }
            }
            return ptsEnnemi;
        }
        return ptsEnnemi;
    }

    /**
     * Méthode qui retourne une liste contenant les pv de chaque ennemis dont leur nombre est donné par le joueur
     * @return list contenant les pv des ennemis
     */
    static short[] initEnnemis() {
        System.out.println("Combien souhaitez-vous combattre d'ennemis ?");
        Scanner scan = new Scanner(System.in);
        short nbrEnnemis = scan.nextShort();
        System.out.println("Génération des ennemis...");
        short[] pvEnnemisList;
        pvEnnemisList = new short[nbrEnnemis];
        for(short i = 0; i < pvEnnemisList.length; i++) {
            pvEnnemisList[i] = nombreAuHasard(MAX_VIE_ENNEMI);
            System.out.println("Ennemi numéro " + (i + 1) + " : " + Util.color(pvEnnemisList[i] , Color.PURPLE));
        }
        return pvEnnemisList;
    }

    public static void main(String[] args){
        initPersonnage();
        short[] list = initEnnemis();
        short i = 0;
        // Le jeu continu tant qu'il y a des ennemis et que le joueur a des points de vie
        for(short element : list){
            boolean joueurJoue;
            joueurJoue = hasard(0.5);
            System.out.println("Combat avec un ennemi possédant " + element + " points de vie");
            afficherPersonnage();
            System.out.println(" vs ennemi (" + Util.color((short) Math.max(element, 0), Color.PURPLE) + ")");
            while (ptsDeVie > 0 && element > 0) {
                element = attaque(element, joueurJoue);
                joueurJoue = !joueurJoue;
                afficherPersonnage();
                System.out.println(" vs " + Util.color("ennemi" , Color.YELLOW) + " (" +
                        Util.color((short) Math.max(element, 0), Color.PURPLE) + ")");
            }
            if (ptsDeVie <= 0) {
                System.out.println(Util.color(nomPersonnage, Color.GREEN) + " est mort mais a tué " + nbEnnemisTues +
                        " ennemis");
                break;
            }
            else {
                nbEnnemisTues++;
                i++;
                if (i == (short) list.length) {
                    System.out.println(Util.color(nomPersonnage, Color.GREEN) + " a tué tous les ennemis !");
                    break;
                }
                System.out.println("L'ennemi est mort ! Au suivant !");
                // Le bouclier se régénère après chaque ennemis tué
                if (bouclierActif) {
                    System.out.println("Régénération du bouclier : " +
                            Util.color("+" + REGENERATION_BOUCLIER_PAR_TOUR, Color.BLUE));
                    ptsBouclier = (short) Math.min(PTS_BOUCLIER, ptsBouclier + REGENERATION_BOUCLIER_PAR_TOUR);
                }
                System.out.println("Saisisser S pour passer au combat suivant ou n'importe quoi d'autre pour fuir...");
                Scanner scan = new Scanner(System.in);
                String reponse = scan.nextLine();
                if (!reponse.equals("S")) {
                    System.out.println("Vous avez tué " + nbEnnemisTues + " ennemis mais êtes partis lâchement avant" +
                            " la fin");
                    break;
                }

            }
        }
    }
}