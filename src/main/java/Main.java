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
     * Method that ask the player name and then initiate the hp and
     * the shield rate of the player to their max value
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
     * Mothod that simulate a try with a percentage
     * @param X percentage to success
     * @return if success
     */
    static boolean hasard(double X)
    {
        return Math.random() < X;
    }

    /**
     * method that return a short between 0 and a given number
     * @param born last born
     * @return a short between 0 and "born"
     */
    static short nombreAuHasard(short born)
    {
        return (short) Math.round(born * Math.random());
    }

    /**
     * Method that calculate the damage that the player deal to the enemy
     * @param pvEnnemi remaining enemy health point
     * @return remaining enemy health point
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
     * Method that show the remaining health point and shield rate of the player
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

    static void attaqueEnnemi()
    {
        short dgtEnnemi = nombreAuHasard(MAX_ATTAQUE_ENNEMI);
        if (ptsBouclier == 0) {
            ptsDeVie = (short) (ptsBouclier - dgtEnnemi + ptsDeVie);
            System.out.println("L'" + Util.color("ennemi" , Color.YELLOW) + " attaque " +
                    Util.color(nomPersonnage , Color.GREEN) + " ! Il lui fait " + dgtEnnemi + " points de dommages ! " +
                    Util.color(nomPersonnage , Color.GREEN) + " perd " + Util.color(dgtEnnemi , Color.RED) + " points " +
                    "de vie !");
        }
        else if (ptsBouclier < dgtEnnemi) {
            ptsDeVie = (short) (ptsDeVie + (ptsBouclier - dgtEnnemi));
            System.out.println("L'" + Util.color("ennemi" , Color.YELLOW) + " attaque " +
                    Util.color(nomPersonnage , Color.GREEN) + " ! Il lui fait " + dgtEnnemi + " points de dommages !" +
                    " Le bouclier perd " + Util.color(ptsBouclier , Color.BLUE) + " points. " +
                    Util.color(nomPersonnage , Color.GREEN) + " perd " +
                    Util.color((short) ((ptsBouclier - dgtEnnemi)*-1) , Color.RED) + " points de vie !");
            ptsBouclier = 0;
        }
        else {
            ptsBouclier = (short) (ptsBouclier - dgtEnnemi);
            System.out.println("L'" + Util.color("ennemi" , Color.YELLOW) + " attaque " +
                    Util.color(nomPersonnage , Color.GREEN) + " ! Il lui fait " + dgtEnnemi + " points de dommages !" +
                    " Le bouclier perd " + Util.color(dgtEnnemi , Color.BLUE) + " points.");
        }
    }

    static short attaque(short ptsEnnemi,boolean joueurJoue)
    {
        if (ptsEnnemi > 0 && ptsDeVie > 0) {
            if (joueurJoue) {
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
                if (hasard(0.1)) {
                    bouclierActif = false;
                    ptsBouclier = 0;
                }
            }
            return ptsEnnemi;
        }
        return ptsEnnemi;
    }

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