package video;

import static java.awt.event.KeyEvent.VK_SHIFT;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import static support.Costanti.DIMENSIONE_STANZA_DEFAULT;
import static support.Costanti.RAPPORTO_DIMENSIONE_SCHERMO;
import static support.CostantiConfig.FLAT_CELL;
import static support.CostantiConfig.RELIEF_CELL;
import static support.CostantiConfig.DARKER_CELL;
import static support.CostantiConfig.LIGHT_CELL;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import support.Utility;

public class GameVisualizer extends JPanel {

	static final private long serialVersionUID = 0L;

	static final public int VK_HEARTBEAT = VK_SHIFT;

	int cellSize;
	final private JFrame frame;
	private List<CellRenderOptionWithPosition> cellRenderOptionWithPosition;
	private List<LeaderBoardCellRenderOption> leaderboard;
	private int leaderboardPositionX;
	private int leaderboardPositionY;
	private float leaderboardFontMultiplier;
	private ScoreInfo scoreInfo;
	private Color frameBackground;

	public GameVisualizer() {
		this.frame = new JFrame("Snake Reborn");
		this.frameBackground = Color.black;
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setBackground(Color.BLACK);
		this.cellSize = getCellSize();
		Dimension panelDimension = new Dimension((DIMENSIONE_STANZA_DEFAULT*this.cellSize)-1, (DIMENSIONE_STANZA_DEFAULT*this.cellSize)-1);
		this.leaderboardPositionX = (int)((DIMENSIONE_STANZA_DEFAULT*cellSize * 0.9)+cellSize*0.25);
		this.leaderboardPositionY = (int)(cellSize*0.75);
		this.leaderboardFontMultiplier = cellSize/16f;
		frame.getContentPane().setPreferredSize(panelDimension);
		frame.pack();
		frame.add(this);
		frame.setLocationRelativeTo(null);
	}

	private int getCellSize() {
		Dimension screenSize = Toolkit.getDefaultToolkit ().getScreenSize ();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();
		return (int) ((Utility.minimoTra(width,height)/(DIMENSIONE_STANZA_DEFAULT))*RAPPORTO_DIMENSIONE_SCHERMO);
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(this.frameBackground);
		g.fillRect(0, 0, DIMENSIONE_STANZA_DEFAULT*cellSize, DIMENSIONE_STANZA_DEFAULT*cellSize);
		for (CellRenderOptionWithPosition c : cellRenderOptionWithPosition) {
			drawCell(g, c);
		}
		drawStatisticsOnScreen(g);
	}

	private void drawStatisticsOnScreen(Graphics g) {
		if(this.leaderboard!=null) {
			addLeaderboard(g);
		}
		this.frame.setTitle( " Avversari: " + (this.scoreInfo.getEnemiesNumber()) +
				"        Uccisioni: " + this.scoreInfo.getPlayerKills() +
				"        Record: " + this.scoreInfo.getPlayerOldRecord() + 
				"        Punteggio: " + this.scoreInfo.getCurrentScore() +
				"        Tempo: " + this.scoreInfo.getSurvivalTime());
	}

	private void addLeaderboard(Graphics g) {
		int relativeY = leaderboardPositionY;
		boolean first = true;
		Font currentFont = g.getFont();
		Font newFont = currentFont.deriveFont(currentFont.getSize() * this.leaderboardFontMultiplier);
		int i = 0;
		int maxValue = -1;
		for(LeaderBoardCellRenderOption crowp:leaderboard) {
			g.setColor(crowp.getColor());
			int score = crowp.getScore();
			if(score>0 && (first && crowp.isActiveScore() || score>=maxValue && crowp.isActiveScore())) {
				maxValue = crowp.getScore();
				drawDarkerCell(g, (int)(cellSize*0.75), leaderboardPositionX-(cellSize/8), relativeY-(cellSize/8));
				first = false;
			} else {
				drawDarkerCell(g, (int)(cellSize*0.5), leaderboardPositionX, relativeY);
			}
			if(crowp.isActiveScore()) {
				g.setColor(Color.white);
			} else {
				g.setColor(Color.gray);
			}
			g.setFont(newFont);
			g.drawString(String.valueOf(score), leaderboardPositionX + cellSize, (int)(relativeY + cellSize*0.5));
			relativeY+=cellSize;
			i++;
			if(i>=5) break;
		}
	}

	private void drawCell(Graphics g, CellRenderOptionWithPosition cellRenderOption) {
		final int posX = cellRenderOption.getPosition().getX();
		final int posY = cellRenderOption.getPosition().getY();
		g.setColor(cellRenderOption.getColor());
		int gx = posX*cellSize, gy = posY*cellSize;
		if(cellRenderOption.getRenderType().equals(FLAT_CELL)) {
			drawStandardCell(g, cellSize, gx, gy);
		} else if (cellRenderOption.getRenderType().equals(RELIEF_CELL)){
			drawReliefCell(g, cellSize, gx, gy);
		} else if (cellRenderOption.getRenderType().equals(DARKER_CELL)) {
			drawDarkerCell(g, cellSize, gx, gy);
		} else if (cellRenderOption.getRenderType().equals(LIGHT_CELL)) {
			drawLightCell(g, cellSize, gx, gy);
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
	
	private void drawLightCell(Graphics g, int dimC, int gx, int gy) {
		paintSquare(g, dimC, gx, gy);
		g.setColor(Color.white);
		g.fill3DRect(   gx+3,gy+3, dimC-7, dimC-7, true );
	}
	
	private void paintSquare(Graphics g, int dimC, int gx, int gy) {
		g.fill3DRect(   gx,  gy,   dimC-1, dimC-1, true);
		g.fillRoundRect(gx+2,gy+2, dimC-4, dimC-4, 2, 2 );
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setUpVisualization(List<CellRenderOptionWithPosition> cellRenderOptionWithPosition,
			List<LeaderBoardCellRenderOption> leaderboard, ScoreInfo scoreInfo) {
		this.cellRenderOptionWithPosition = cellRenderOptionWithPosition;
		this.leaderboard = leaderboard;
		this.scoreInfo = scoreInfo;
	}

}
