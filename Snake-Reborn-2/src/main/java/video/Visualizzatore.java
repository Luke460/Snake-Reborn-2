package video;

import static java.awt.event.KeyEvent.VK_SHIFT;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import static supporto.Costanti.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import game.Partita;
import supporto.OSdetector;
import supporto.Utility;
import terrenoDiGioco.Casella;
import terrenoDiGioco.CasellaManager;
import terrenoDiGioco.Stanza;

public class Visualizzatore extends JPanel {

	static final private long serialVersionUID = 0L;

	static final public int VK_HEARTBEAT = VK_SHIFT; // meglio un tasto "innocuo"

	private Partita partita;
	private Stanza cacheStanza;
	int dimensioneCasella;
	final private JFrame finestra;

	public Visualizzatore() {
		this.finestra = new JFrame("Snake Reborn");		
		finestra.add(this);
		finestra.setDefaultCloseOperation(EXIT_ON_CLOSE);
		finestra.setBackground(Color.BLACK);
		this.dimensioneCasella = calcolaDimensioneCasellaMassima();
		// AGGIUNTE LE DIMENSIONI DEI BORDI DELLE FINESRE

		if(OSdetector.isWindows()) {
			finestra.setSize((int)(15+(DIMENSIONE_STANZA_DEFAULT)*this.dimensioneCasella), (int) (37+(DIMENSIONE_STANZA_DEFAULT)*dimensioneCasella));
		} else {
			finestra.setSize((int)(6+(DIMENSIONE_STANZA_DEFAULT)*this.dimensioneCasella), (int) (32+(DIMENSIONE_STANZA_DEFAULT)*dimensioneCasella));
		}
		
		finestra.setLocationRelativeTo(null);

	}
	
	public void setPartita(Partita partita) {
		this.partita = partita;
	}

	private int calcolaDimensioneCasellaMassima() {
		Dimension screenSize = Toolkit.getDefaultToolkit ().getScreenSize ();
		int larghezza = (int) screenSize.getWidth();
		int altezza = (int) screenSize.getHeight();
		return (int) ((Utility.minimoTra(larghezza,altezza)/(DIMENSIONE_STANZA_DEFAULT))*RAPPORTO_DIMENSIONE_SCHERMO);
	}

	@Override
	public void paintComponent(Graphics g) {
		//Thread.currentThread().setPriority(6);
		g.setColor(Color.black);
		g.fillRect(0, 0, DIMENSIONE_STANZA_DEFAULT*dimensioneCasella, DIMENSIONE_STANZA_DEFAULT*dimensioneCasella);
		//g.drawString
		Stanza stanzaCorrente = this.partita.getSerpentePlayer1().getUltimaStanza();
		if(stanzaCorrente!=null) {
			this.cacheStanza = stanzaCorrente;
		} else {
			this.cacheStanza = this.partita.getStanzaDiSpawn();
		}
		for (Casella c : this.cacheStanza.getCaselle().values()) {
			if(!CasellaManager.isVuota(c)) {
				disegnaCasella(g, c);
			}
		}
		riportaStatisticheSullaFinestra(partita.getPunteggioPlayer1());
	}


	private void riportaStatisticheSullaFinestra(long punteggio) {
		this.finestra.setTitle( " Avversari: " + (this.partita.getNumeroAvversari())+ 
				"        Uccisioni: " + this.partita.getSerpentePlayer1().getNumeroUccisioni() +
				"        Record: " + this.partita.getVecchioRecord() + 
				"        Punteggio: " + punteggio +
				"        Tempo: " + (int)(this.partita.getSerpentePlayer1().getTempoSopravvissuto()/1000));
	}

	private void disegnaCasella(Graphics g, Casella casella) {
		final int posX = casella.getPosizione().getX();
		final int posY = casella.getPosizione().getY();
		final Color colore = Pittore.getColore(casella.getStato());
		g.setColor(colore);
		int gx = posX*dimensioneCasella, gy = posY*dimensioneCasella;
		if(casella.getStato()==CARATTERE_CASELLA_PORTALE){
			disegnaCasellaInRilievo(g, dimensioneCasella, gx, gy);
		} else if(casella.isTestaDiSerpente()){
			disegnaCasellaTesta(g, dimensioneCasella, gx, gy);
		} else {
			disegnaCasellaNormale(g, dimensioneCasella, gx, gy);
		}
	}

	private void disegnaCasellaNormale(Graphics g, int dimC, int gx, int gy) {

		g.fill3DRect(   gx,  gy,   dimC-1, dimC-1, true);
		g.fillRoundRect(gx+2,gy+2, dimC-4, dimC-4, 2, 2 );

	}

	private void disegnaCasellaInRilievo(Graphics g, int dimC, int gx, int gy) {

		g.fill3DRect(   gx,  gy,   dimC-1, dimC-1, true);
		g.fillRoundRect(gx+2,gy+2, dimC-4, dimC-4, 2, 2 );
		g.fill3DRect(   gx+3,gy+3, dimC-7, dimC-7, true );

	}

	private void disegnaCasellaTesta(Graphics g, int dimC, int gx, int gy) {

		g.fill3DRect(   gx,  gy,   dimC-1, dimC-1, true);
		g.fillRoundRect(gx+2,gy+2, dimC-4, dimC-4, 2, 2 );
		Color nuovoColore = g.getColor().darker();
		g.setColor(nuovoColore);
		g.fill3DRect(   gx+2,gy+2, dimC-6, dimC-6, true );

	}

	public Partita getPartita() {
		return partita;
	}

	public JFrame getFinestra() {
		return finestra;
	}

}
