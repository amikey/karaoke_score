import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;

/**
 * Created by kevin on 4/7/16.
 */
public class VoicePitchDetector implements PitchDetectionHandler{
    @Override
    public void handlePitch(PitchDetectionResult pitchDetectionResult,
                            AudioEvent audioEvent) {
        if(pitchDetectionResult.getPitch() != -1){
            if (Main.mediaPlayer != null){
                double timeStamp = Main.mediaPlayer.getCurrentTime().toSeconds();
                //double timeStamp = audioEvent.getTimeStamp();
                float pitch = pitchDetectionResult.getPitch();
                float probability = pitchDetectionResult.getProbability();
                double rms = audioEvent.getRMS() * 100;
                char note = Note.frequencyToShortNote((double) pitch);
                char noteSong = Main.getShortNoteFromSong(timeStamp);
                //String message = String.format("Voice - Pitch detected at %.2fs: %.2fHz ( %.2f probability, RMS: %.5f ) %c\n",timeStamp,pitch,probability,rms,note);
                String message = "Mic note: " + note + ". Expected note: " + noteSong+".";
                System.out.println(message);
                if (noteSong != 'N'){
                    if (noteSong == note){
                        Main.hit++;
                    }
                    else{
                        Main.miss++;
                    }
                }
            }
        }
    }
}
