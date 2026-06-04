import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Varianten
{
    public static void main(String[] args)
    {
        Backup_Task backupTask = new Backup_Task();
        backupTask.Start();
        
        try{
            Thread.sleep(2000);

        }catch (InterruptedException e){
            System.out.println(e.getMessage());

        }
        backupTask.stopp();
        //4. ThreadsPools: Pool Kummert sich um Zutilung der Aufgaben zu Threads
        // Verschidene strategien: FixedThreadPool (3. Task muss warten bis einer fertig ist)
        // Verschiednene Strategien: FixedThreadPool

        // Try withe resources: AutoCloseble = schlieesst automatisch
        try(ExecutorService executor = Executors.newFixedThreadPool(2)){
            executor.execute(()-> simulateDownload("BigFile.Zip"));
            executor.execute(()-> simulateDownload("AnothreBigFile.Zip"));
            //executor.shutdown(); Weglassen wegen AutoClose

        }
        //5. Wenn Threads erschöft sind , wird automattisch neuer Thread erstellt
        // läuft nicht parallel zu FixedThreadPool, da dieser blockiert,
        // ehe CachedThreadPool angelegt wird
        try(ExecutorService executor = Executors.newCachedThreadPool()){
            executor.execute(()-> simulateDownload("CachedBigFile.Zip"));
        }

        //ExecutorService Blockiert solange bis alle Threads derin Fertig sind kein join nötig)
        // JVM (Programm) beendet erst, wenn alle Vordergrund-Threads beendet sind
        System.out.println("Ende Main");
    }

    private static void simulateDownload(String filename){
        System.out.println(" Start Donwload in Thread" + Thread.currentThread().threadId());
        for (int progress = 0; progress <= 100; progress++){
            System.out.println(filename +""+ progress+ "% completed");
        }

        try {
            Thread.sleep(100);
        }catch (InterruptedException e){
            return;
        }
    }
}
