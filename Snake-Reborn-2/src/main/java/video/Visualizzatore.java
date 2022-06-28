package video;

import static java.awt.event.KeyEvent.VK_SHIFT;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import static support.Costanti.DIMENSIONE_STANZA_DEFAULT;
import static support.Costanti.RAPPORTO_DIMENSIONE_SCHERMO;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.JPanel;

import game.Partita;
import gamefield.Casella;
import gamefield.CellRenderOption;
import gamefield.Stanza;
import score.SnakeScoreComparator;
import snake.Snake;
import support.Utility;

public class Visualizzatore extends JPanel {

	static final private long serialVersionUID = 0L;

	static final public int VK_HEARTBEAT = VK_SHIFT; // meglio un tasto "innocuo"

	private Partita game;
	int cellSize;
	final private JFrame frame;

	public Visualizzatore() {
		this.frame = new JFrame("Snake Reborn");
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setBackground(Color.BLACK);
		this.cellSize = calcolaDimensioneCasellaMassima();
		Dimension panelDimension = new Dimension((DIMENSIONE_STANZA_DEFAULT*this.cellSize)-1, (DIMENSIONE_STANZA_DEFAULT*this.cellSize)-1);
		frame.getContentPane().setPreferredSize(panelDimension);
		frame.pack();
		frame.add(this);
		frame.setLocationRelativeTo(null);
	}
	
	public void setPartita(Partita partita) {
		this.game = partita;
	}

	private int calcolaDimensioneCasellaMassima() {
		Dimension screenSize = Toolkit.getDefaultToolkit ().getScreenSize ();
		int larghezza = (int) screenSize.getWidth();
		int altezza = (int) screenSize.getHeight();
		return (int) ((Utility.minimoTra(larghezza,altezza)/(DIMENSIONE_STANZA_DEFAULT))*RAPPORTO_DIMENSIONE_SCHERMO);
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, DIMENSIONE_STANZA_DEFAULT*cellSize, DIMENSIONE_STANZA_DEFAULT*cellSize);
		//g.drawString
		Stanza stanzaCorrente;
		Snake snakePlayer1 = this.game.getSerpentePlayer1();
		if(snakePlayer1.isVivo()){
			stanzaCorrente = snakePlayer1.getCasellaDiTesta().getStanza(); 
		} else if(snakePlayer1.getUltimaStanza() != null){
			stanzaCorrente = snakePlayer1.getUltimaStanza();
		} else {
			stanzaCorrente = this.game.getStanzaDiSpawn();
		}
		for (Casella c : stanzaCorrente.getCaselle().values()) {
			disegnaCasella(g, c);
		}
		drawStatisticsOnScreen(g,game.getSerpentePlayer1().getTotalSnakeScore());
	}

	private void drawStatisticsOnScreen(Graphics g, long punteggio) {
		if(this.game.isShowLeaderboard()) {
			addLeaderboard(g);
		}
		this.frame.setTitle( " Avversari: " + (this.game.getNumeroAvversari()) +
				"        Uccisioni: " + this.game.getSerpentePlayer1().getNumeroUccisioni() +
				"        Record: " + this.game.getVecchioRecord() + 
				"        Punteggio: " + punteggio +
				"        Tempo: " + (int)(this.game.getSerpentePlayer1().getTempoSopravvissutoMillis()/1000));
	}

	private void addLeaderboard(Graphics g) {
		ArrayList<Snake> snakes = new ArrayList<>(this.game.getSerpenti().values());
		SnakeScoreComparator comparator = new SnakeScoreComparator();
		Collections.sort(snakes, comparator);
		int x = (int)((DIMENSIONE_STANZA_DEFAULT*cellSize * 0.9)+cellSize*0.25);
		int y = (int)(cellSize*0.75);
		boolean first = true;
		Font currentFont = g.getFont();
		Font newFont = currentFont.deriveFont(currentFont.getSize() * 2F);
		int i = 0;
		for(Snake snake:snakes) {
			g.setColor(snake.getCellRenderOption().getColor());
			if(first) {
				drawDarkerCell(g, (int)(cellSize*0.75), x-(cellSize/8), y-(cellSize/8));
				first = false;
			} else {
				drawDarkerCell(g, (int)(cellSize*0.5), x, y);
			}
			g.setColor(Color.white);
			g.setFont(newFont);
			g.drawString(String.valueOf(snake.getTotalSnakeScore()), x + cellSize, (int)(y + cellSize*0.5));
			y+=cellSize;
			i++;
			if(i>=5) break;
		}
	}

	private void disegnaCasella(Graphics g, Casella casella) {
		final int posX = casella.getPosizione().getX();
		final int posY = casella.getPosizione().getY();
		//final Color colore = Pittore.getColore(casella.getStato());
		CellRenderOption cellRenderOption = GraphicManager.getCellRenderOption(casella);
		g.setColor(cellRenderOption.getColor());
		int gx = posX*cellSize, gy = posY*cellSize;
		if(cellRenderOption.isFlat()) {
			drawStandardCell(g, cellSize, gx, gy);
		} else if (cellRenderOption.isRelief()){
			drawReliefCell(g, cellSize, gx, gy);
		} else if (cellRenderOption.isDarker()) {
			drawDarkerCell(g, cellSize, gx, gy);
		}
	}

	private void drawStandardCell(Graphics g, int dimC, int gx, int gy) {
		paintSquare(g, dimC, gx, gy);
	}

	private void drawReliefCell(Graphics g, int dimC, int gx, int gy) {
		paintSquare(g, dimC, gx, gy);
		g.fill3DRect(   gx+3,gy+3, dimC-7, dimC-7, true );
	}

	private void drawDarkerCell(Graphics g, int dimC, int gx, int gy) {
		paintSquare(g, dimC, gx, gy);
		Color nuovoColore = g.getColor().darker();
		g.setColor(nuovoColore);
		g.fill3DRect(   gx+2,gy+2, dimC-6, dimC-6, true );
	}
	
	private void paintSquare(Graphics g, int dimC, int gx, int gy) {
		g.fill3DRect(   gx,  gy,   dimC-1, dimC-1, true);
		g.fillRoundRect(gx+2,gy+2, dimC-4, dimC-4, 2, 2 );
	}

	public Partita getPartita() {
		return game;
	}

	public JFrame getFinestra() {
		return frame;
	}

}
