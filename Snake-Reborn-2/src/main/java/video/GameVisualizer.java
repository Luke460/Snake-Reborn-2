package video;

import static constants.GeneralConstants.DIMENSIONE_STANZA_DEFAULT;
import static constants.GeneralConstants.RAPPORTO_DIMENSIONE_SCHERMO;
import static constants.MapConstants.DARKER_CELL;
import static constants.MapConstants.FLAT_CELL;
import static constants.MapConstants.LIGHT_CELL;
import static constants.MapConstants.RELIEF_CELL;
import static java.awt.event.KeyEvent.VK_SHIFT;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import server.model.Match;
import support.Utility;

public class GameVisualizer extends JPanel {

	static final private long serialVersionUID = 0L;
	static final public int DEFAULT_FONT_SIZE = 12;
	static final public int VK_HEARTBEAT = VK_SHIFT;
	static final private String END_GAME_MESSAGE = "STATISTICHE PARTITA";

	int cellSize;
	final private JFrame frame;
	private List<CellRenderOptionWithPosition> cellRenderOptionWithPosition;
	private List<LeaderBoardCellRenderOption> leaderboard;
	private int leaderboardPositionX;
	private int leaderboardPositionY;
	private float leaderboardFontMultiplier;
	private float messageFontMultiplayer;
	private Match match;
	private Color frameBackground;
	private String message;
	private boolean showEndGameStatistics;
	private Integer secondsLeft;

	public GameVisualizer() {
		this.frame = new JFrame("Snake Reborn");
		this.frameBackground = Color.black;
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setBackground(Color.BLACK);
		this.cellSize = getCellSize();
		Dimension panelDimension = new Dimension((DIMENSIONE_STANZA_DEFAULT*this.cellSize)-1, (DIMENSIONE_STANZA_DEFAULT*this.cellSize)-1);
		this.leaderboardPositionX = (int)((DIMENSIONE_STANZA_DEFAULT*cellSize * 0.9)-cellSize*0.25);
		this.leaderboardPositionY = (int)(cellSize*0.75);
		this.leaderboardFontMultiplier = cellSize/16f;
		this.messageFontMultiplayer = cellSize/8f;
		this.showEndGameStatistics = false;
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
		if(!showEndGameStatistics) {
			for (CellRenderOptionWithPosition c : cellRenderOptionWithPosition) {
				drawCell(g, c);
			}
			drawStatisticsOnScreen(g);
			addTimeLeft(g);
			addScreenMessage(g);
		} else {
			drawEndGameStatisticsOnScreen(g);
		}
	}

	private void addTimeLeft(Graphics g) {
		if(this.secondsLeft==null || this.secondsLeft<0) return;
		Font newFont = g.getFont().deriveFont(DEFAULT_FONT_SIZE * this.leaderboardFontMultiplier);
		g.setFont(newFont);
		if(this.secondsLeft>60) {
			g.setColor(Color.white);
		} else if(this.secondsLeft>10) {
			g.setColor(Color.yellow);
		} else {
			g.setColor(Color.red);
		}
		int minutes = this.secondsLeft/60;
		int seconds = this.secondsLeft%60;
		String sec;
		if(seconds==0) {
			sec = "00";
		} else if(seconds<10) {
			sec = "0"+seconds;
		} else {
			sec = ""+seconds;
		}
		g.drawString(minutes + ":" + sec, (int)(this.cellSize*1.25), (int)(leaderboardPositionY+this.cellSize*1));
	}

	private void addScreenMessage(Graphics g) {
		if(this.message==null) return;
		Font newFont = g.getFont().deriveFont(DEFAULT_FONT_SIZE * this.messageFontMultiplayer);
		g.setFont(newFont);
		g.setColor(new Color(255,255,255,125));
		int screenCenter = (int)(DIMENSIONE_STANZA_DEFAULT*cellSize/2);
		g.drawString(this.message, (int)(screenCenter-(this.cellSize*message.length()*0.33)), screenCenter);
	}

	private void drawStatisticsOnScreen(Graphics g) {
		if(this.leaderboard!=null) {
			addLeaderboard(g);
		}
		String kd =       "  K/D: " + this.match.getKillsNumber() + "/" + this.match.getDeathsNumber();
		String streak =   "        Serie: " + this.match.getBestKillingStreak();
		String food =     "        Cibo: " + this.match.getTotalFoodTaken();
		String score =    "        Punteggio: " + this.match.getFinalScore();
		String position = "        Posizione: " + this.match.getFinalLeaderboardPosition();
		this.frame.setTitle(kd + streak + food + score + position);
	}
	
	private void drawEndGameStatisticsOnScreen(Graphics g) {
		int screenCenter = (int)(DIMENSIONE_STANZA_DEFAULT*cellSize/2);
		int relativeY = screenCenter;
		Font newFont = g.getFont().deriveFont(DEFAULT_FONT_SIZE * this.messageFontMultiplayer);
		int leaderboardLength = leaderboard.size();
		relativeY = (int)(screenCenter - (leaderboardLength*cellSize*1.5) + cellSize*2);
		g.setFont(newFont);
		g.setColor(Color.white);
		g.drawString(END_GAME_MESSAGE, (int)(screenCenter-(this.cellSize*END_GAME_MESSAGE.length()*0.40)), relativeY - cellSize*4);
		for(LeaderBoardCellRenderOption leaderboardCellRenOpt:leaderboard) {
			g.setColor(leaderboardCellRenOpt.getColor());
			int score = leaderboardCellRenOpt.getScore();
			drawCustomCell(g, (int)(cellSize*1.5), screenCenter-cellSize*6, relativeY, leaderboardCellRenOpt);
			g.setColor(Color.white);
			g.setFont(newFont);
			g.drawString(String.valueOf(score), screenCenter-cellSize*3, (int)(relativeY + cellSize*1.25));
			String additionalInfo = "(" + leaderboardCellRenOpt.getKillsNuber() + "/" + leaderboardCellRenOpt.getDeathsNumber() + ")";
			g.drawString(additionalInfo, screenCenter+cellSize*4, (int)(relativeY + cellSize*1.25));
			relativeY+=cellSize*3;
		}
	}

	private void addLeaderboard(Graphics g) {
		int relativeY = leaderboardPositionY;
		boolean first = true;
		Font newFont = g.getFont().deriveFont(DEFAULT_FONT_SIZE * this.leaderboardFontMultiplier);
		int maxValue = -1;
		for(LeaderBoardCellRenderOption leaderboardCellRenOpt:leaderboard) {
			g.setColor(leaderboardCellRenOpt.getColor());
			int score = leaderboardCellRenOpt.getScore();
			if(score>0 && (first && leaderboardCellRenOpt.isPlayerAlive() || score>=maxValue && leaderboardCellRenOpt.isPlayerAlive())) {
				maxValue = leaderboardCellRenOpt.getScore();
				drawCustomCell(g, (int)(cellSize*0.75), leaderboardPositionX-(cellSize/8), relativeY-(cellSize/8), leaderboardCellRenOpt);
				first = false;
			} else {
				drawCustomCell(g, (int)(cellSize*0.5), leaderboardPositionX, relativeY, leaderboardCellRenOpt);
			}
			if(leaderboardCellRenOpt.isPlayerAlive()) {
				g.setColor(Color.white);
			} else {
				g.setColor(Color.gray);
			}
			g.setFont(newFont);
			g.drawString(String.valueOf(score), leaderboardPositionX + cellSize, (int)(relativeY + cellSize*0.5));
			String additionalInfo = String.valueOf(leaderboardCellRenOpt.getKillsNuber());
			g.drawString(additionalInfo, leaderboardPositionX + cellSize*3, (int)(relativeY + cellSize*0.5));
			relativeY+=cellSize;
		}
	}

	private void drawCell(Graphics g, CellRenderOptionWithPosition cellRenderOption) {
		if(cellRenderOption!=null) {
			final int posX = cellRenderOption.getPosition().getX();
			final int posY = cellRenderOption.getPosition().getY();
			g.setColor(cellRenderOption.getColor());
			int gx = posX*cellSize, gy = posY*cellSize;
			drawCustomCell(g, cellSize, gx, gy, cellRenderOption);
		}
	}
	
	private void drawCustomCell(Graphics g, int dimC, int gx, int gy, CellRenderOption cellRenderOption) {
		g.setColor(cellRenderOption.getColor());
		if(!cellRenderOption.getColor().equals(this.frameBackground)) {
			if(cellRenderOption.getRenderType() == FLAT_CELL) {
				drawStandardCell(g, dimC, gx, gy);
			} else if (cellRenderOption.getRenderType() == RELIEF_CELL){
				drawReliefCell(g, dimC, gx, gy);
			} else if (cellRenderOption.getRenderType() == DARKER_CELL) {
				drawDarkerCell(g, dimC, gx, gy);
			} else if (cellRenderOption.getRenderType() == LIGHT_CELL) {
				drawLightCell(g, dimC, gx, gy);
			}
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

	public boolean isShowEndGameStatistics() {
		return showEndGameStatistics;
	}

	public void setShowEndGameStatistics(boolean showEndGameStatistics) {
		this.showEndGameStatistics = showEndGameStatistics;
	}

	public int getSecondsLeft() {
		return secondsLeft;
	}

	public void setSecondsLeft(int secondsLeft) {
		this.secondsLeft = secondsLeft;
	}

	public void setUpVisualization(
			Color backgroundColor, 
			List<CellRenderOptionWithPosition> cellRenderOptionWithPosition,
			List<LeaderBoardCellRenderOption> leaderboard, 
			Match match, 
			String message,
			Integer secondsLeft
			) {
		this.frameBackground = backgroundColor;
		this.cellRenderOptionWithPosition = cellRenderOptionWithPosition;
		this.leaderboard = leaderboard;
		this.match = match;
		this.message = message;
		this.secondsLeft = secondsLeft;
	}
	
	public void setUpGameWindowForEndGameStatistics(
			Color backgroundColor, 
			List<LeaderBoardCellRenderOption> leaderboard
			) {
		this.frameBackground = backgroundColor;
		this.cellRenderOptionWithPosition = null;
		this.leaderboard = leaderboard;
		this.match = null;
		this.message = null;
		this.showEndGameStatistics = true;
		this.secondsLeft = null;
	}

}
