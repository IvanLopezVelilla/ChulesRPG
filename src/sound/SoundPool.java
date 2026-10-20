package sound;

import java.net.URL;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundPool {

	private ArrayList<Clip> clips=new ArrayList<>();
	private URL soundURL;
	
	
	public SoundPool(URL soundURL) {
		
		this.soundURL=soundURL;
		
		for(int i=0;i<2;i++) {
			
			Clip clip=createClip();
			
			if(clip!=null) {
				
				clips.add(clip);
				
			}
			
		}
		
	}
	
	
	//Creates a clip
	public Clip createClip() {
		
	    try {

	        AudioInputStream ais=AudioSystem.getAudioInputStream(soundURL);

	        Clip clip=AudioSystem.getClip();

	        clip.open(ais);

	        ais.close();

	        return clip;

	    } catch(Exception e) {
	        e.printStackTrace();
	    }

	    return null;
		
	}
	
	
	//Looks for an available clip and creates a new one if can't find any
	public Clip getAvailableClip() {

	    for(Clip clip:clips) {

	        if(!clip.isRunning()) {

	            return clip;
	            
	        }
	        
	    }

	    Clip newClip=createClip();

	    if(newClip!=null) {

	        clips.add(newClip);
	    }

	    return newClip;
	}
	
	
	//Returns the clip list
	public ArrayList<Clip> getClips() {

	    return clips;
	}
	
	
}













