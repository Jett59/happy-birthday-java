package com.mycodefu.birthday.java;

import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundLoader {
public static Clip loadSoundFile(String path) {
	try {
		var in = AudioSystem.getAudioInputStream(new File(path));
		var clip = AudioSystem.getClip();
		clip.open(in);
		return clip;
	}catch(Exception e) {
		throw new IllegalArgumentException(e);
	}
}
}
