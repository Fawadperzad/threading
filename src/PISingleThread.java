import org.w3c.dom.ls.LSOutput;

public class PISingleThread
{
    public static void main(String[] args)
    {
    long iterationen = 100000000L;
    // Gibt vergangene Millisekunden von 1.1.1970 zurück
        long startZeit = System.currentTimeMillis();
        double pi = berechnerPI(iterationen);
        long endzeit = System.currentTimeMillis();
        long differenz = endzeit - startZeit;

        System.out.printf("berechnen PI %.10f%n", pi);
        System.out.printf("Math PI %.10f%n", Math.PI);
        System.out.printf("Abweichung: %.10f%n", Math.abs(pi-Math.PI));
        System.out.printf("Zeit %d ms", differenz);

    }


    /*
    leibniz_Formale:
    PI/4 = 1 - 1/3 + 1/5 - 1/7 ........

     */
    private static double berechnerPI(long iterationen){
        double summe = 0.0;

    // Term =


        for (long i= 0; i< iterationen; i++){
            double term = Math.pow(-1, i) / (2 * i + 1);
            summe += term;
        }



        return summe * 4;

    }
}

