

//Vordergrundthreads: Solange diese laufen, beendet JVM nicht
//Hintergrundthreads: setDaemon(true), werden nach Ende des Hauptthreads entfernt (JVM beendet)
public class CheckDaemonSimple {
    public static void main(String[] args) throws InterruptedException {
        vorderGrundThread();
        //Erst nach join in vorderGrundThread() wird hinterGrundThread aufgerufen
        hinterGrundThread();

        //Hier ist Hauptthread, wartet 3 Sekunden
        countDown(3);
    }

    private static void vorderGrundThread() throws InterruptedException {
        Thread downloader = new Thread(() -> simulateDownload("BigOne.zip", 100, false));
        //downloader.setDaemon(false); default: Vordergrundthread
        downloader.start();
        //Blockiert aufrufenden Thread und wartet bis der Vordergrundthread vollständig fertig ist
        downloader.join();
    }

    private static void hinterGrundThread() throws InterruptedException {
        Thread downloader = new Thread(() -> simulateDownload("BigOneHintergrund.zip", 500, true));
        downloader.setDaemon(true);
        downloader.start();
        //join funktioniert genauso wie bei Vordergrund-Threads (blockiert aufrufenden Thread)
        //Aber nicht sinnvoll: sind für unwichtige Aufgaben im Hintergrund
        //downloader.join();
    }

    private static void simulateDownload(String filename, long wait, boolean isDaemon) {
        System.out.println("Starte Download ... in Thread " + Thread.currentThread().threadId());
        for(int progress = 0; progress <= 100; progress++) {
            if(progress % 10 == 0) {
                System.out.println(filename + " - " + progress + "% completed");
            }
            try {
                //Aktueller Thread wartet (Andere Threads könnten Arbeit machen)
                //Wenn während dieser Wartezeit von ausserhalb interrupt gerufen wird -> InterruptedException
                Thread.sleep(wait);
            } catch (InterruptedException ex) {
                return;
            }
        }
        System.out.println(isDaemon?"Hintergrund":"Vordergrund" + "-Thread Download angeschlossen: " + filename);
    }

    private static void countDown(int sec) {
        for(int i= sec; i>0; i--) {
            try {
                Thread.sleep(1000);
                System.out.println("Hauptthread endet in " + i + " Sekunden");
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("JVM beendet jetzt ...");
    }
}
