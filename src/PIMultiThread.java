public class PIMultiThread
{
    public static void main(String[] args)
    {
        Thread piThread = new Thread(){

            @Override
            public void run(){
                super.run();
            }
        };
    }


    private static double berechnerPI(long iterationen)
    {
        double summe = 0.0;

        // Term =


        for (long i = 0;
             i < iterationen;
             i++) {
            double term = Math.pow(-1, i) / (2 * i + 1);
            summe += term;
        }


        return summe * 4;

    }
}