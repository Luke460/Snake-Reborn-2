package commands;

import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_D;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_ESCAPE;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_W;
import static java.awt.event.KeyEvent.VK_SPACE;
import static java.awt.event.KeyEvent.VK_F;
import static java.awt.event.KeyEvent.VK_CONTROL;
import static java.awt.event.KeyEvent.VK_SHIFT;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Queue;

import javax.swing.JFrame;

import game.Partita;
import video.Visualizzatore;

public class LettoreComandi {
	public Partita partita;
	public int dimensioneCasella;
	public JFrame finestra;


	public static void initControlliDaTastiera(Visualizzatore visualizzatore, Queue<String> sequenzaComandi) {

		// Gestione eventi associati alla tastiera
		visualizzatore.getFinestra().addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case VK_ENTER:
					//GestoreComandi.resuscitaPlayer1();
					sequenzaComandi.add("ENTER");
					break;
				case VK_LEFT:
					//GestoreComandi.turnLeftP1();
					sequenzaComandi.add("LEFT");
					break;
				case VK_RIGHT:
					//GestoreComandi.turnRightP1();
					sequenzaComandi.add("RIGHT");
					break;
				case VK_W:
					//GestoreComandi.goUpP1();
					sequenzaComandi.add("W");
					break;
				case VK_S:
					//GestoreComandi.goDownP1();
					sequenzaComandi.add("S");
					break;
				case VK_A:
					//GestoreComandi.goLeftP1();
					sequenzaComandi.add("A");
					break;
				case VK_D:
					//GestoreComandi.goRightP1();
					sequenzaComandi.add("D");
					break;
				case VK_ESCAPE:
					//GestoreComandi.gameOver();
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
				}

			}
			@Override
			public void keyReleased(KeyEvent e) {}
		});
	}
}