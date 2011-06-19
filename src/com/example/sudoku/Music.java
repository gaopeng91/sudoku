package com.example.sudoku;

import android.content.Context;
import android.media.MediaPlayer;

public class Music {

	private static MediaPlayer mp;

	public static void play(Context cxt, int resId) {
		stop(cxt);
		mp = MediaPlayer.create(cxt, resId);
		mp.setLooping(true);
		mp.start();
	}

	public static void stop(Context cxt) {
		if(mp != null) {
			mp.stop();
			mp.release();
			mp = null;
		}
	}

}
