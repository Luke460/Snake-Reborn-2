package commands;

import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_D;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_ESCAPE;
import static java.awt.event.KeyEvent.VK_UP;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_W;
import static java.awt.event.KeyEvent.VK_Q;
import static java.awt.event.KeyEvent.VK_E;

import static commands.GestoreComandi.*;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Queue;

import javax.swing.JFrame;

import game.Partita;
import video.GameVisualizer;

public class LettoreComandi {
	public Partita partita;
	public int dimensioneCasella;
	public JFrame finestra;


	public static void initControlliDaTastiera(GameVisualizer visualizzatore, Queue<String> sequenzaComandi) {

		// Gestione eventi associati alla tastiera
		visualizzatore.getFrame().addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case VK_W:
					sequenzaComandi.add(UP);
					break;
				case VK_S:
					sequenzaComandi.add(DOWN);
					break;
				case VK_A:
					sequenzaComandi.add(LEFT);
					break;
				case VK_D:
					sequenzaComandi.add(RIGHT);
					break;
				case VK_UP:
					sequenzaComandi.add(UP);
					break;
				case VK_DOWN:
					sequenzaComandi.add(DOWN);
					break;
				case VK_LEFT:
					sequenzaComandi.add(LEFT);
					break;
				case VK_RIGHT:
					sequenzaComandi.add(RIGHT);
					break;
				case VK_Q:
					sequenzaComandi.add(ROTATE_LEFT);
					break;
				case VK_E:
					sequenzaComandi.add(ROTATE_RIGHT);
					break;
				case VK_ENTER:
					sequenzaComandi.add(RESPAWN);
					break;
				case VK_ESCAPE:
					sequenzaComandi.add(EXIT);
					break;
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {}
		});
	}
}