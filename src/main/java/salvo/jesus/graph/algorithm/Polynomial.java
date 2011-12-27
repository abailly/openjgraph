/*______________________________________________________________________________
 * 
 * Copyright 2005 Arnaud Bailly - NORSYS/LIFL
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * (1) Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 * (2) Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in
 *     the documentation and/or other materials provided with the
 *     distribution.
 *
 * (3) The name of the author may not be used to endorse or promote
 *     products derived from this software without specific prior
 *     written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Created on 28 avr. 2005
 *
 */
package salvo.jesus.graph.algorithm;

/**
 * @author nono
 * @version $Id$
 */
public class Polynomial {

    int degre;

    double[] coeffs;

    // d�finition d'une constante (pour la pr�cision) globale � la classe
    final static double epsilon = Double.MIN_VALUE * 2;

    /***************************************************************************
     * Les constructeurs
     **************************************************************************/

    public Polynomial(double[] tab) {
        coeffs = tab;
        chercheDegre();
        // le mot-cl� "this" sert � d�signer l'objet en cours de construction si
        // n�cessaire
    }

    Polynomial(int dmax) {
        coeffs = new double[dmax + 1];
        degre = -1; // degr� du polyn�me nul
    }

    public Polynomial(Polynomial P) {
        degre = P.degre;
        coeffs = new double[degre + 1];
        for (int i = 0; i <= degre; i++)
            coeffs[i] = P.coeffs[i];
    }

    /***************************************************************************
     * Des outils pour manipuler simplement les double
     **************************************************************************/

    static boolean egalZero(double x) {
        return (Math.abs(x) < epsilon);
    }

    static boolean sontEgaux(double x, double y) {
        return egalZero(x - y);
    }

    static boolean sontDifferents(double x, double y) {
        return !egalZero(x - y);
    }

    static boolean estEntier(double x) {
        return egalZero(x - (int) x);
    }

    /***************************************************************************
     * Fonctions de base (degr�, �galit�...)
     **************************************************************************/

    // si on fait un peu confiance au champ degre
    void verifieDegre() {
        while ((degre >= 0) && egalZero(coeffs[degre]))
            degre--;
    }

    // si on ne fait confiance � rien
    void chercheDegre() {
        degre = coeffs.length - 1;
        verifieDegre();
    }

    boolean estNul() {
        return (degre == -1);
    }

    public boolean equals(Object o) {
        Polynomial Q = (Polynomial) o;
        if (o == null)
            return false;
        if (degre != Q.degre)
            return false;
        for (int i = 0; i <= degre; i++)
            if (sontDifferents(coeffs[i], Q.coeffs[i]))
                return false;
        return true;
    }

    /***************************************************************************
     * Affichage... un peu compliqu�, pour ressembler � ce qu'on �crit � la main
     **************************************************************************/

    void afficheMonome(double x, int k, StringBuffer sb) {
        // cas particulier des mon�mes nuls
        if (egalZero(x))
            return;
        // le signe
        if (x < 0) {
            sb.append(" - ");
            x = -x;
        } else
            sb.append(" + ");
        // le coefficient et le degr�
        afficheMonomeSansSigne(x, k, sb);
    }

    void afficheMonomeSansSigne(double x, int k, StringBuffer sb) {
        // le coefficient (qui est non nul, sauf erreur)
        if (sontEgaux(x, 1) && k != 0)
            ; // on n'affiche pas les coefficients 1 sauf pour le terme constant
        else if (estEntier(x))
            sb.append((int) x); // c'est plus joli...
        else
            sb.append(x);
        // le degr�
        if (k == 0)
            ;
        else if (k == 1)
            sb.append("X");
        else
            sb.append("X^" + k);
    }

    void afficheMonome(int k, StringBuffer sb) {
        if (k == degre)
            afficheMonomeSansSigne(coeffs[k], k, sb);
        else
            afficheMonome(coeffs[k], k, sb);
    }

    public void affiche(StringBuffer sb) {
        // cas particulier puisqu'on n'affiche pas les mon�mes nuls, en g�n�ral
        if (estNul())
            sb.append(0);
        else {
            for (int i = degre; i >= 0; i--)
                afficheMonome(i, sb);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        affiche(sb);
        return sb.toString();
    }

    /***************************************************************************
     * D�rivation, somme, produit
     **************************************************************************/

   public Polynomial derivee() {
        Polynomial D = new Polynomial(degre - 1);
        for (int i = 0; i < degre; i++)
            D.coeffs[i] = (i + 1) * coeffs[i + 1];
        D.degre = degre - 1;
        return D;
    }

   public Polynomial plus(Polynomial Q) {
       Polynomial R;
       if (degre > Q.degre)
           R = new Polynomial(degre);
       else
           R = new Polynomial(Q.degre);
       for (int i = 0; i <= degre; i++)
           R.coeffs[i] = coeffs[i];
       for (int i = 0; i <= Q.degre; i++)
           R.coeffs[i] += Q.coeffs[i];
       // ou encore, avec la m�thode ci-dessous : ajoute(P,R); ajoute(Q,R);
       R.chercheDegre();
       return R;
   }

   public Polynomial moins(Polynomial Q) {
       Polynomial R;
       if (degre > Q.degre)
           R = new Polynomial(degre);
       else
           R = new Polynomial(Q.degre);
       for (int i = 0; i <= degre; i++)
           R.coeffs[i] = coeffs[i];
       for (int i = 0; i <= Q.degre; i++)
           R.coeffs[i] -= Q.coeffs[i];
       // ou encore, avec la m�thode ci-dessous : ajoute(P,R); ajoute(Q,R);
       R.chercheDegre();
       return R;
   }

    public static void ajoute(Polynomial Q, Polynomial R) { // ajoute Q � R (en
                                                     // modifiant R)
        for (int i = 0; i <= Q.degre; i++)
            R.coeffs[i] += Q.coeffs[i];
    }

    public static Polynomial ajoute(Polynomial[] tabPoly) {
        int degmax = -1;
        for (int i = 0; i < tabPoly.length; i++)
            if (tabPoly[i].degre > degmax)
                degmax = tabPoly[i].degre;
        Polynomial R = new Polynomial(degmax);
        for (int i = 0; i < tabPoly.length; i++)
            ajoute(tabPoly[i], R);
        R.chercheDegre();
        return R;
    }

    // retourne le produit de P par aX^k
    public Polynomial multMonome(double alpha, int k) {
        if (k == 0 && egalZero(alpha))
            return new Polynomial(0);
        Polynomial R = new Polynomial(degre + k);
        for (int i = 0; i <= degre; i++)
            R.coeffs[i + k] = alpha * coeffs[i];
        R.chercheDegre();
        return R;
    }

    // multiplie P par le mon�me de degr� k de Q
    public Polynomial multMonome(Polynomial Q, int k) {
        return multMonome(Q.coeffs[k], k);
    }

    public Polynomial fois(Polynomial Q) {
        Polynomial[] produitsParMonomesDeQ = new Polynomial[Q.degre + 1];
        for (int i = 0; i <= Q.degre; i++)
            produitsParMonomesDeQ[i] = multMonome(Q, i);
        return ajoute(produitsParMonomesDeQ);
    }

    /***************************************************************************
     * �valuation, racines
     **************************************************************************/

    public double evalue(double x) { // m�thode na�ve
        double res = 0;
        for (int i = 0; i <= degre; i++)
            res += coeffs[i] * Math.pow(x, i);
        if (estEntier(res))
            // juste pour avoir �viter les valeurs saugrenues dans certains cas
            // (pour les racines en particulier)
            return (int) res;
        else
            return res;
    }

    // plus efficace, la m�thode de H�rner
    public double evalueParHorner(double x) {
        if (estNul())
            return 0;
        double res = coeffs[degre];
        for (int i = degre - 1; i >= 0; i--)
            res = res * x + coeffs[i];
        if (estEntier(res))
            return (int) res;
        else
            return res;
    }

    public boolean estRacine(double x) {
        return (egalZero(evalueParHorner(x)));
    }

    public int degreRacine(double x) {
        if (estNul()) // cas particulier
            return -1;
        // convention TR�S contestable... mais comment repr�senter +infini?
        // (Infinity est une valeur double)
        if (estRacine(x))
            return 1 + derivee().degreRacine(x); // appel r�cursif
        else
            return 0; // initialisation de la r�currence
    }

}
