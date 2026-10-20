package sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class MusicPlayer {

	private Clip clip;
	private FloatControl volumeControl;
	private String currentTrack;
	
	public int volumeScale=3;
	public final int maxVolumeScale=5;
	
	
	//Plays a song in loop
	public void play(String trackName) {

	    if(trackName.equals(currentTrack)
	        && clip!=null
	        && clip.isRunning()) {

	        return;
	    }

	    stop();

	    try {

	        AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResource("/sounds/" + trackName + ".wav"));
	        clip=AudioSystem.getClip();
	        clip.open(ais);

	        volumeControl=(FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
	        checkVolume();

	        currentTrack = trackName;
	        clip.setFramePosition(0);
	        clip.loop(Clip.LOOP_CONTINUOUSLY);
	        clip.start();

	    } catch(Exception e) {

	        e.printStackTrace();

	    }

	}
	
	
	//Stops a song
	public void stop() {

	    if(clip!=null) {

	        clip.stop();
	        clip.close();
	        clip=null;
	        currentTrack=null;

	    }

	}
	
	
	//Pauses a song
	public void pause() {

	    if(clip!=null && clip.isRunning()) {

	        clip.stop();

	    }

	}
	
	
	//Resumes a song
	public void resume() {

	    if(clip!=null && !clip.isRunning()) {

	        clip.start();

	    }

	}
	
	
	//Sets volume externally
	public void setVolume(int volumeScale) {

	    this.volumeScale=Math.max(0, Math.min(volumeScale, maxVolumeScale));

	    if(volumeControl!=null) {

	    	checkVolume();

	    }

	}
	
	
	//Updates the music volume
	public void checkVolume() {

	    float volume;
	    
	    switch(volumeScale) {

	        case 0: volume=-80f; break;
	        case 1: volume=-20f; break;
	        case 2: volume=-10f; break;
	        case 3: volume=-5f; break;
	        case 4: volume=0f; break;
	        case 5: volume=3f; break;
	        default: volume=0f; break;
	        
	    };

	    volumeControl.setValue(volume);

	}	
	
}

























