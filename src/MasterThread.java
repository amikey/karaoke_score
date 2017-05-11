import java.util.concurrent.CountDownLatch;

/**
 * Created by ks on 14/04/16.
 */
public class MasterThread extends Thread {
    private CountDownLatch latch;
    public volatile boolean stop = false;

    public MasterThread(CountDownLatch latch)
    {
        this.latch = latch;
    }

    @Override
    public void run(){
        try
        {
            latch.await();          //The thread keeps waiting till it is informed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while(!stop){
            //Sleep 20ms
            try{
                sleep(200);
                if (Main.mediaPlayer != null){
                    char micNote = Main.getShortNoteFromMic();
                    double currentTime = Main.mediaPlayer.getCurrentTime().toSeconds();
                    char songNote = Main.getShortNoteFromSong(currentTime);
                    String fullNote = Main.getNoteFromSong(currentTime);

                    String message = "Mic note: " + micNote + ". Expected note: " + songNote + "/" + fullNote+ ".";
                    message = message + String.format(" - Pitch detected at %.2fs\n", currentTime);
                    System.out.println(message);
                    if (songNote != 'N'){
                        if (micNote == songNote){
                            Main.hit++;
                        }
                        else{
                            Main.miss++;
                        }
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
                break;
            }
        }
    }
}
