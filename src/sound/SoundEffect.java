package sound;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class SoundEffect {
	
	private Map<String, SoundPool> sounds=new HashMap<>();
	public int volumeScale=3;
	
	
	//Loads a sound
	public void load(String name, String path) {

	    URL url = getClass().getResource(path);

	    if(url == null) {

	        System.out.println("Sound not found: " + path);

	        return;
	    }

	    sounds.put(name, new SoundPool(url));
	}
	
	
	//Plays a sound
	public void play(String name) {

	    SoundPool pool=sounds.get(name);

	    if(pool==null) {

	        return;
	    }

	    Clip clip=pool.getAvailableClip();

	    if(clip==null) {

	        return;
	    }

	    clip.stop();
	    clip.setFramePosition(0);

	    applyVolume(clip);

	    clip.start();
	}
	
	
	//Stops a sound
	public void stop(String name) {

	    SoundPool pool=sounds.get(name);

	    if(pool==null) {

	        return;
	    }

	    for(Clip clip:pool.getClips()) {

	        clip.stop();
	    }
	}
	
	
	//Adjust the SE volume
	public void setVolume(int volumeScale) {this.volumeScale = volumeScale;}
	
	
	//
	private void applyVolume(Clip clip) {

	    FloatControl fc =(FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

	    float volume;
	    switch(volumeScale) {

	        case 0: volume=-80f; break;
	        case 1: volume=-30f; break;
	        case 2: volume=-12f; break;
	        case 3: volume=-5f; break;
	        case 4: volume=1f; break;
	        case 5: volume=6f; break;

	        default: volume=-5f; break;
	    };

	    fc.setValue(volume);
	}
	
	
}

































