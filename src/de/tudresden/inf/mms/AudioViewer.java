package de.tudresden.inf.mms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * JFrame-Anwendung (Fenster mit Playeransicht und Funktionsauswahl) <br>
 * <strong>Nicht bearbeiten!</strong>
 */
public class AudioViewer extends JFrame {

	private static final long serialVersionUID = 1L;
	File file = new File("stereo.wav");
	AudioEditor myEditor;

	Color BACKGROUND_COLOR = Color.white;
	Color REFERENCE_LINE_COLOR = Color.black;
	Color WAVEFORM_COLOR = Color.blue;

	JPanel audioPanel = new JPanel();
	ArrayList<SingleWaveformPanel> singleChannelWaveformPanels = new ArrayList<SingleWaveformPanel>();

	public AudioViewer() {

		this.setResizable(false);

		add(new JLabel(file.getAbsolutePath()), BorderLayout.SOUTH);

		audioPanel.setLayout(new GridLayout(0, 1));
		audioPanel.setBackground(Color.WHITE);

		add(audioPanel, BorderLayout.CENTER);
		add(new JPanel(), BorderLayout.WEST);
		add(new JPanel(), BorderLayout.EAST);

		singleChannelWaveformPanels = new ArrayList<SingleWaveformPanel>();

		myEditor = new AudioEditor(file);

		createWaveformPanels(myEditor.getAudioInfo());

		final JComboBox<String> operator = new JComboBox<String>();
		operator.addItem("Original");
		operator.addItem("Leiser");
		operator.addItem("Schneller");
		operator.addItem("ILD");
		operator.addItem("ITD");
		operator.addItem("Fade-in/Fade-out");
		operator.addItem("Echo");

		operator.setMaximumRowCount(operator.getModel().getSize());

		JPanel menu = new JPanel();
		menu.setBackground(Color.WHITE);
		add(menu, BorderLayout.NORTH);

		menu.add(operator);

		operator.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {

				switch (operator.getSelectedItem().toString()) {
				case "Original":
					myEditor.reset();
					break;
				case "Leiser":
					myEditor.reduceVolume();
					break;
				case "Schneller":
					myEditor.speed();
					break;
				case "ILD":
					myEditor.ild();
					break;
				case "ITD":
					myEditor.itd();
					break;
				case "Fade-in/Fade-out":
					myEditor.fadeInOut();
					break;
				case "Echo":
					myEditor.echo();
					break;

				}

				createWaveformPanels(myEditor.getAudioInfo());

				audioPanel.validate();
				audioPanel.repaint();

			}
		});

	}

	private void createWaveformPanels(AudioInfo audioInfo) {

		audioPanel.removeAll();

		for (int t = 0; t < audioInfo.getNumberOfChannels(); t++) {
			SingleWaveformPanel waveformPanel = new SingleWaveformPanel(audioInfo, t);
			singleChannelWaveformPanels.add(waveformPanel);
			JPanel panel = new JPanel(new BorderLayout());
			panel.add(waveformPanel, BorderLayout.CENTER);

			JLabel label = new JLabel("Channel " + (t + 1));
			panel.add(label, BorderLayout.NORTH);

			audioPanel.add(panel);
		}

	}

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		AudioViewer frame = new AudioViewer();

		frame.setSize(800, 450);
		frame.setTitle("Aufgabe 2");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		frame.validate();
		frame.repaint();

	}

	public class SingleWaveformPanel extends JPanel {

		private static final long serialVersionUID = 1L;
		private AudioInfo helper;
		private int channelIndex;

		public SingleWaveformPanel(AudioInfo helper, int channelIndex) {
			this.helper = helper;
			this.channelIndex = channelIndex;
			setBackground(BACKGROUND_COLOR);
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			int lineHeight = getHeight() / 2;
			g.setColor(REFERENCE_LINE_COLOR);
			g.drawLine(0, lineHeight, (int) getWidth(), lineHeight);

			drawWaveform(g, helper.getAudio(channelIndex));

		}

		protected void drawWaveform(Graphics g, int[] samples) {
			if (samples == null) {
				return;
			}

			int oldX = 0;
			int oldY = (int) (getHeight() / 2);
			int xIndex = 0;

			int increment = helper.getIncrement(helper.getXScaleFactor(getWidth()));
			g.setColor(WAVEFORM_COLOR);

			int t = 0;

			for (t = 0; t < increment; t += increment) {
				g.drawLine(oldX, oldY, xIndex, oldY);
				xIndex++;
				oldX = xIndex;
			}

			for (; t < samples.length; t += increment) {
				double scaleFactor = helper.getYScaleFactor(getHeight());
				double scaledSample = samples[t] * scaleFactor;
				int y = (int) ((getHeight() / 2) - (scaledSample));
				g.drawLine(oldX, oldY, xIndex, y);

				xIndex++;
				oldX = xIndex;
				oldY = y;
			}
		}
	}

}
