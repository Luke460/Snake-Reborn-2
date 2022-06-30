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
import static java.awt.event.KeyEvent.VK_SPACE;
import static java.awt.event.KeyEvent.VK_F;
import static java.awt.event.KeyEvent.VK_CONTROL;
import static java.awt.event.KeyEvent.VK_SHIFT;


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
					sequenzaComandi.add("W");
					break;
				case VK_S:
					sequenzaComandi.add("S");
					break;
				case VK_A:
					sequenzaComandi.add("A");
					break;
				case VK_D:
					sequenzaComandi.add("D");
					break;
				case VK_UP:
					sequenzaComandi.add("W");
					break;
				case VK_DOWN:
					sequenzaComandi.add("S");
					break;
				case VK_LEFT:
					sequenzaComandi.add("A");
					break;
				case VK_RIGHT:
					sequenzaComandi.add("D");
					break;
				case VK_Q:
					sequenzaComandi.add("LEFT");
					break;
				case VK_E:
					sequenzaComandi.add("RIGHT");
					break;
				case VK_ESCAPE:
					sequenzaComandi.add("ESCAPE");
					break;
				case VK_SPACE:
					sequenzaComandi.add("SPACE");
					break;
				case VK_F:
					sequenzaComandi.add("F");
					break;
				case VK_CONTROL:
					sequenzaComandi.add("CONTROL");
					break;
				case VK_SHIFT:
					sequenzaComandi.add("SHIFT");
					break;
				case VK_ENTER:
					sequenzaComandi.add("ENTER");
					break;
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {}
		});
	}
}