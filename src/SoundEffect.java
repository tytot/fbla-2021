
import java.io.*;
import javax.sound.sampled.*;

public class SoundEffect {

	public static final String MUSIC = "src/res/audio/bg.wav";
	public static final String CLICK = "src/res/audio/click.wav";
	public static final String PICKUP = "src/res/audio/pickup.wav";
	public static final String GROW = "src/res/audio/grow.wav";
	public static final String SPLIT = "src/res/audio/split.wav";
	public static final String SAD = "src/res/audio/sad.wav";
	public static final String MERGE = "src/res/audio/merge.wav";
	public static final String BUTTON_DOWN = "src/res/audio/button_down.wav";
	public static final String BUTTON_UP = "src/res/audio/button_up.wav";
	public static final String DEATH = "src/res/audio/death.wav";
	public static final String SUCCESS = "src/res/audio/success.wav";
	public static final String START = "src/res/audio/start.wav";

	private AudioInputStream stream;
	private Clip clip;

	SoundEffect(String soundFileName) {
		try {
			stream = AudioSystem
			.getAudioInputStream(new File(soundFileName));
			clip = AudioSystem.getClip();
			clip.open(stream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	SoundEffect(String soundFileName, float gain) {
		this(soundFileName);
		FloatControl gainControl = (FloatControl) clip
		.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(gain);
	}

	public void play(boolean loop) {
		if (clip.isRunning())
			clip.stop();
		clip.setFramePosition(0);
		clip.start();
		if (loop)
			clip.loop(Clip.LOOP_CONTINUOUSLY);

	}

	public void stop() {
		clip.stop();
		clip.setFramePosition(0);
	}
}