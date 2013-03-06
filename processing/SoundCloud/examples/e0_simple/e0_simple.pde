import de.voidplus.soundcloud.*;
import ddf.minim.*;

SoundCloud soundcloud;
Minim minim;
AudioPlayer player;

void setup(){
  
  // http://soundcloud.com/you/apps for APP_CLIENT_ID and APP_CLIENT_SECRET
  soundcloud = new SoundCloud("APP_CLIENT_ID", "APP_CLIENT_SECRET", "LOGIN_NAME", "LOGIN_PASS");
  
  // show user details
  User me = soundcloud.get("me");
  println(me);
  
  // play the first track of search
  ArrayList<Track> result = soundcloud.findTrack("Chromatics");
  Track first_track;
  if(result!=null){
    println("Tracks: "+result.size());

    minim = new Minim(this);  
    player = minim.loadFile(result.get(0).getStreamUrl());
    player.play();
  }
  
  minim = new Minim(this);
}

void draw(){}

void stop(){
  player.close();
  minim.stop();
}
