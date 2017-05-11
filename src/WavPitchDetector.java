import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;

/**
 * Created by kevin on 4/7/16.
 */
public class WavPitchDetector implements PitchDetectionHandler {
    char note;

    @Override
    public void handlePitch(PitchDetectionResult pitchDetectionResult,
                            AudioEvent audioEvent) {
        //if(pitchDetectionResult.getPitch() != -1){
            double timeStamp = audioEvent.getTimeStamp();
            float pitch = pitchDetectionResult.getPitch();
            float probability = pitchDetectionResult.getProbability();
            double rms = audioEvent.getRMS() * 100;
            note = Note.frequencyToShortNote((double) pitch);
            char noteSong = Main.getShortNoteFromSong(timeStamp);
            String message = "Mic note: " + note + ". Expected note: " + noteSong + ".";
            message = message + String.format(" Song - Pitch detected at %.2fs: %.2fHz ( %.2f probability, RMS: %.5f )- %c\n", timeStamp,pitch,probability,rms,note);
            System.out.println(message);
        //}
    }
}
