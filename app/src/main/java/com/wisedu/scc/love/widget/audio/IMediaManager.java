package com.wisedu.scc.love.widget.audio;

import android.media.MediaPlayer;
import android.media.MediaRecorder;

/**
 * Interface containing media management methods.
 */
public interface IMediaManager {

	/**
	 * Records a file.
	 */
	void recordGreeting(String fileName);

	/**
	 * Plays saved greeting.
	 */
	void playGreeting(String fileName, boolean isRestartRequired);

	/**
	 * Stops recording process.
	 */
	void stopRecording();

	/**
	 * Stops greeting playback process.
	 */
	void stopPlayback();

	/**
	 * Pauses greeting playback.
	 */
	void pausePlayback();

	/**
	 * Gets playback duration.
	 * 
	 * @return ms of the file to be played.
	 */
	int getPlaybackDuration();

	/**
	 * Current position of greeting being played.
	 * 
	 * @return the current position in milliseconds
	 */
	int getCurrentPlaybackPosition();

	/**
	 * Sets current playback position.
	 * 
	 * @param progress
	 */
	void setPlayPosition(int progress);

	/**
	 * Gets current media player object.
	 * 
	 * @return MediaRecorder
	 */
	MediaPlayer getMediaPlayer();

	/**
	 * Gets current MediaRecorder object.
	 * 
	 * @return MediaRecorder
	 */
	MediaRecorder getMediaRecorder();
	
	boolean isPlaying();

	void setOnMediaEventListener(OnMediaEventListener onMediaEventListener);

	public static interface OnMediaEventListener {
		
		public void onMediaRecordCompletion();
		
		public void onMediaPlayError();

		public void onMediaRecordError();
	}

}
