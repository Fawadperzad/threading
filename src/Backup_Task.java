public class Backup_Task implements Runnable{

    private Thread worker;
    private volatile boolean isRunning = true;
    private int backupCounter = 0;

    public void Start()
    {
        worker = new Thread(this);
        worker.start();
    }

    public void stopp()
    {
        isRunning = false;
        worker.interrupt();
    }

    @Override
    public void run(){
        while(isRunning){
            backupCounter++;
            System.out.println("Backup" + backupCounter + "erstellt.");
            try {
                Thread.sleep(500);

            }catch (InterruptedException e){
                System.out.println("Backup.Service beendet");
                return;
            }
        }
    }
}
