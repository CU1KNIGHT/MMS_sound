package de.tudresden.inf.mms;

import java.io.File;

/**
 * @author <Vorname> <Name>, <Matrikelnummer>
 * 
 */
public class AudioEditor {

	/**
	 * Original (Ausgangspunkt für Berechnungen)
	 */
	private AudioInfo audioInfo;
	private File file;

	public AudioEditor(File file) {
		super();
		this.file = file;
		this.audioInfo = new AudioInfo(file);
	}

	public AudioInfo getAudioInfo() {
		return audioInfo;
	}

	public void reset() {
		this.audioInfo = new AudioInfo(file);
	}

	/**
	 * Reduziert die Lautstärke des vorgegebenen Samples und speichert das neue
	 * Sample als quieter.wav lokal ab.
	 * 
	 * @return
	 */
	public void reduceVolume() {
		
		this.audioInfo = new AudioInfo(file);
		int[][] audio = audioInfo.getAudioSamples();

		for (int c = 0; c < audio.length; c++) {
			for (int i = 0; i < audio[c].length; i++) {
				audio[c][i] = audio[c][i] / 2;
			}
		}
		audioInfo.saveWave(new File("quieter.wav"));
	}

	/**
	 * Erhöht die Abspielgeschwindigkeit  des vorgegebenen Samples um den Faktor 2 
	 * und speichert das neue Sample als speed.wav lokal ab.
	 * 
	 * @return
	 */
	public void speed() {

        this.audioInfo = new AudioInfo(file);
        int[][] audio = audioInfo.getAudioSamples();


		audioInfo.saveWave(new File("speed.wav"));
	}

	/**
	 * Nimmt eine Soundpositionierung entsprechend dem Prinzip der Interaural Level
	 * Difference (ILD) vor und speichert das Ergebnis als ild.wav lokal ab.
	 * 
	 * @return
	 */
	public void ild() {
		
		/*
		 * ToDo
		 */

		audioInfo.saveWave(new File("ild.wav"));
	}

	/**
	 * Nimmt eine Soundpositionierung entsprechend dem Prinzip der Interaural Time
	 * Difference (ITD) vor und speichert das Ergebnis als itd.wav lokal ab.
	 * 
	 * @return
	 */
	public void itd() {
		
		/*
		 * ToDo
		 */

		audioInfo.saveWave(new File("itd.wav"));
	}

	/**
	 * Fügt ein Fade-in am Anfang und Fade-out am Ende hinzu und speichert das neue
	 * Sample als fade.wav lokal ab.
	 * 
	 * @return
	 */
	public void fadeInOut() {
		
		/*
		 * ToDo
		 */
		
		audioInfo.saveWave(new File("fade.wav"));
	}

	/**
	 * Fügt ein Echo in das vorgegebene Sample ein und speichert das Ergebnis als
	 * echo.wav lokal ab.
	 * 
	 * @return
	 */
	public void echo() {
		
		/*
		 * ToDo
		 */

		audioInfo.saveWave(new File("echo.wav"));

	}

}
