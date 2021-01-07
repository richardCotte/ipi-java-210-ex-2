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
        scan.close();
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
            System.out.println(Util.color(nomPersonnage, Color.GREEN) + " (" + Util.color(ptsDeVie, Color.RED) + " " +
                    Util.color(ptsBouclier, Color.BLUE) + ")");
        }
        else {
            System.out.println(Util.color(nomPersonnage, Color.GREEN) + " (" + Util.color(ptsDeVie, Color.RED) + ")");
        }
    }

    static void attaqueEnnemi()
    {
        short dgtEnnemi = nombreAuHasard(MAX_ATTAQUE_ENNEMI);
        if (ptsBouclier == 0) {
            ptsDeVie = (short) (ptsBouclier - dgtEnnemi);
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
}