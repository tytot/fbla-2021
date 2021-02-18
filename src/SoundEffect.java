
import java.io.*;
import javax.sound.sampled.*;

public enum SoundEffect {
	
	MUSIC("audio/bg.wav", -10f),
	CLICK("audio/click.wav"),
	PICKUP("audio/pickup.wav"),
	GROW("audio/grow.wav"),
	SPLIT("audio/split.wav"),
	SAD("audio/sad.wav"),
	MERGE("audio/merge.wav"),
	BUTTON_DOWN("audio/button_down.wav"),
	BUTTON_UP("audio/button_up.wav"),
	DEATH("audio/death.wav"),
	GAME_OVER("audio/game_over.wav"),
	SUCCESS("audio/success.wav"),
	START("audio/start.wav"),
	
	RAIN("audio/rain.wav", -20f),
	SNOW("audio/snow.wav"),
	WIND("audio/wind.wav");
	
	private AudioInputStream stream;
	private Clip clip;

	SoundEffect(String soundFileName) {
		try {
			stream = AudioSystem.getAudioInputStream(new File(soundFileName));
			clip = AudioSystem.getClip();
			clip.open(stream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	SoundEffect(String soundFileName, float gain) {
		this(soundFileName);
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(gain);
	}

	public void play(boolean loop) {
		if (clip.isRunning()) {
			clip.stop();
		}
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