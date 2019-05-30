package de.tudresden.inf.mms;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Benötigte Hilfsfunktionen und -konstrukte. <br>
 * <strong>Nicht bearbeiten!</strong>
 */
public class AudioInfo {
	
	private AudioInputStream audioInputStream;
	private int[][] samplesContainer;

	public double biggestSample = 32767; 

	public AudioInfo(File file) {

		try {
			audioInputStream = AudioSystem.getAudioInputStream(file);
			createSampleArrayCollection();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void saveWave(File out) {
		byte[] output = this.getByteArrayFromSamplesContainer();

		AudioFormat format = new AudioFormat(this.audioInputStream.getFormat().getSampleRate(),
				(int) this.audioInputStream.getFormat().getSampleSizeInBits(),
				this.audioInputStream.getFormat().getChannels(), true, false);

		ByteArrayInputStream bais = new ByteArrayInputStream(output);

		AudioInputStream audioInputStream = new AudioInputStream(bais, format, (long) this.getFrameLength());

		try {
			AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, out);
			audioInputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public int getNumberOfChannels() {
		int numBytesPerSample = audioInputStream.getFormat().getSampleSizeInBits() / 8;
		return audioInputStream.getFormat().getFrameSize() / numBytesPerSample;
	}

	public long getFrameLength() {
		return audioInputStream.getFrameLength();
	}

	public long getFrameSize() {
		return audioInputStream.getFormat().getFrameSize();
	}

	public float getSampleRate() {
		return audioInputStream.getFormat().getSampleRate();
	}

	private void createSampleArrayCollection() {
		try {
			audioInputStream.mark(Integer.MAX_VALUE);
			byte[] bytes = new byte[(int) (audioInputStream.getFrameLength())
					* ((int) audioInputStream.getFormat().getFrameSize())];
			try {
				audioInputStream.read(bytes);
			} catch (Exception e) {
				e.printStackTrace();
			}

			samplesContainer = getSampleArray(bytes);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected int[][] getSampleArray(byte[] eightBitByteArray) {
		int[][] toReturn = new int[getNumberOfChannels()][eightBitByteArray.length / (2 * getNumberOfChannels())];
		int index = 0;

		for (int t = 0; t < eightBitByteArray.length;) {
			for (int a = 0; a < getNumberOfChannels(); a++) {
				int low = (int) eightBitByteArray[t];
				t++;
				int high = (int) eightBitByteArray[t];
				t++;
				int sample = (high << 8) + (low & 0x00ff);

				toReturn[a][index] = sample;
			}
			index++;
		}

		return toReturn;
	}

	protected byte[] getByteArrayFromSamplesContainer() {
		byte[] bytearray = new byte[getNumberOfChannels() * (int) (this.getFrameLength())
				* ((int) this.getFrameSize())];

		for (int c = 0; c < this.getNumberOfChannels(); c++) {
			for (int i = 0; i < samplesContainer[c].length; i++) {
				bytearray[c * 2 + 4 * i + 0] = (byte) samplesContainer[c][i];
				bytearray[c * 2 + 4 * i + 1] = (byte) (samplesContainer[c][i] >> 8);
			}
		}

		return bytearray;
	}

	public double getXScaleFactor(int panelWidth) {
		return (panelWidth / ((double) samplesContainer[0].length));
	}

	public double getYScaleFactor(int panelHeight) {
		return (panelHeight / (biggestSample * 2 * 1.2));
	}

	public int[] getAudio(int channel) {
		return samplesContainer[channel];
	}

	public int[][] getAudioSamples() {
		return this.samplesContainer;
	}

	protected int getIncrement(double xScale) {
		try {
			int increment = (int) (samplesContainer[0].length / (samplesContainer[0].length * xScale));
			return increment;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

}
