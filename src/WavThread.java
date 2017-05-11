import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.filters.LowPassFS;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.io.jvm.WaveformWriter;
import be.tarsos.dsp.pitch.PitchProcessor;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

/**
 * Created by kevin on 4/8/16.
 */
public class WavThread extends Thread {
    private CountDownLatch latch;
    public AudioDispatcher dispatcher;
    public WavThread(CountDownLatch latch)
    {
        this.latch = latch;
    }

    @Override
    public void run(){
        try
        {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Setup song
        AudioInputStream wavStream;
        try{
            wavStream = AudioSystem.getAudioInputStream(new File("/home/ks/Music/TabacoYChanel.wav"));
            if (wavStream.getFormat().getChannels()==2){
                wavStream = Main.convertToMono(wavStream);
            }

            JVMAudioInputStream wavJVMStream = new JVMAudioInputStream(wavStream);
            dispatcher = new AudioDispatcher(wavJVMStream, Main.audioBufferSize, Main.bufferOverlap);
            WavPitchDetector wavPitchDetector = new WavPitchDetector();
            dispatcher.addAudioProcessor(new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.YIN, wavJVMStream.getFormat().getSampleRate(), Main.audioBufferSize, wavPitchDetector));
            //dispatcher.addAudioProcessor(new BandPass(4000,8000,44100));
            dispatcher.addAudioProcessor(new LowPassFS(8000,44100));
            //dispatcher.addAudioProcessor(new HighPass(15, 44100));
            dispatcher.addAudioProcessor(new WaveformWriter(wavStream.getFormat(), "/home/ks/Music/filtered.wav"));
            dispatcher.run();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
