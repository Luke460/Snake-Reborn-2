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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Queue;

import game.Partita;
import video.GameVisualizer;
import static commands.CommandHandler.*;

public class CommandReader {
	public Partita game;

	public static void initControlliDaTastiera(GameVisualizer gameVisualizer, Queue<String> commandsSequence) {

		gameVisualizer.getFrame().addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case VK_W:
					commandsSequence.add(UP);
					break;
				case VK_S:
					commandsSequence.add(DOWN);
					break;
				case VK_A:
					commandsSequence.add(LEFT);
					break;
				case VK_D:
					commandsSequence.add(RIGHT);
					break;
				case VK_UP:
					commandsSequence.add(UP);
					break;
				case VK_DOWN:
					commandsSequence.add(DOWN);
					break;
				case VK_LEFT:
					commandsSequence.add(LEFT);
					break;
				case VK_RIGHT:
					commandsSequence.add(RIGHT);
					break;
				case VK_Q:
					commandsSequence.add(ROTATE_LEFT);
					break;
				case VK_E:
					commandsSequence.add(ROTATE_RIGHT);
					break;
				case VK_ENTER:
					commandsSequence.add(RESPAWN);
					break;
				case VK_ESCAPE:
					commandsSequence.add(EXIT);
					break;
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {}
		});
	}
}