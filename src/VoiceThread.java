import be.tarsos.dsp.filters.LowPassFS;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;

import java.util.concurrent.CountDownLatch;

/**
 * Created by kevin on 4/8/16.
 */
public class VoiceThread extends Thread {
    private CountDownLatch latch;
    public AudioDispatcher dispatcher;

    public VoiceThread(CountDownLatch latch)
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

        try{
            //Setup voice
            dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(44100, Main.audioBufferSize, Main.bufferOverlap);
            VoicePitchDetector voicePitchDetector = new VoicePitchDetector();
            dispatcher.addAudioProcessor(new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.YIN, 44100, Main.audioBufferSize, voicePitchDetector));
            dispatcher.addAudioProcessor(new LowPassFS(8000,44100));
            dispatcher.run();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
