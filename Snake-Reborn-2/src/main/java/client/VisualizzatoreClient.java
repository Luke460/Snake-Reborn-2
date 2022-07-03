package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSlider;
import javax.swing.JTextField;

import audio.GestoreSuoni;
import constants.ConfigFileConstants;
import game.Partita;
import game.UserLocal;
import server.client.Client;
import support.ConfigurationManager;

public class VisualizzatoreClient extends JFrame{

	private static final long serialVersionUID = 1L;
	private String nomeUtente;
	private String password;
	private boolean premuto;

	JPanel PannelloMessaggioLogin;
	JPanel PannelloMessaggioImpostazioni;
	JPanel PannelloInserimentoNome;
	JPanel PannelloInserimentoPassword;
	JPanel PannelloInserimenti;
	JPanel PannelloOpzioni;
	JPanel PannelloTastiConferma;
	JLabel messaggioLogin;
	JLabel messaggioImpostazioni;
	JLabel messaggioNome;
	JTextField nomeInserito;
	JLabel messaggioPassword;
	JLabel messaggioLivello;
	JLabel messaggioMappa;
	JTextField passwordInserita;	
	JCheckBox opzMusica;
	JSlider volumeMusica;
	JCheckBox opzEffetti;
	JSlider volumeEffetti;
	JComboBox<String> selettoreLivello;
	JComboBox<String> selettoreMappa;
	JCheckBox hideLeaderboard;
	JCheckBox lowGraphicMode;
	JCheckBox endlessMode;
	JLabel messaggioInformativo;
	JButton accedi;
	JButton ospite;
	Partita partita;
	Client client;
	ArrayList<String> listaFileMappe;

	public VisualizzatoreClient() throws IOException {
		super("Snake Reborn");
		client = new Client();
		listaFileMappe = support.FileHandler.getFolderList(constants.GeneralConstants.MAPS_PATH);
		creaPannelli();
		sistemaPannelli();
		preimpostaPannelli();
		aggiungiPannelliAlContainer();

		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		preparaLetturaPulsanti();

		regolaFinestra();
	}
	
	public void rileggi(Partita partita) {
		this.partita = partita;
		partita.setClient(client);
		this.premuto = false;
	}

	private void regolaFinestra() {
		// autoregola dimensione finestra e posizionala al centro
		this.pack();
		Dimension d = new Dimension(460,400);
		this.setSize(d);
		this.setMinimumSize(d);
		this.setResizable(true);
		// centra la finestra (non funziona su KDE)
		this.setLocationRelativeTo(null); 
	}

	private void preparaLetturaPulsanti() {
		Listener listener = new Listener();
		ospite.addActionListener(listener);
		accedi.addActionListener(listener);
	}

	public void leggiImpostazioniDaUI() throws IOException {
		aggiornaFileImpostazioni();
		GestoreSuoni.inizzializzaSuoni(volumeEffetti.getValue(), volumeMusica.getValue());
		GestoreSuoni.setEffettiAbilitati(opzEffetti.isSelected());
		GestoreSuoni.setMusicaAbilitata(opzMusica.isSelected());
		partita.setLivello(selettoreLivello.getSelectedIndex()+1);
		partita.setMapFileName(listaFileMappe.get(selettoreMappa.getSelectedIndex()));
		partita.setShowLeaderboard(hideLeaderboard.isSelected());
		partita.setLowGraphicMode(lowGraphicMode.isSelected());
		partita.setEndlessMode(endlessMode.isSelected());
	}

	private void aggiornaFileImpostazioni() throws IOException {
		ConfigurationManager cr = new ConfigurationManager();
		cr.salvaImpostazione(ConfigFileConstants.EFFETTI, Boolean.toString(opzEffetti.isSelected()));
		cr.salvaImpostazione(ConfigFileConstants.VOLUME_EFFETTI, Integer.toString(volumeEffetti.getValue()));
		cr.salvaImpostazione(ConfigFileConstants.MUSICA, Boolean.toString(opzMusica.isSelected()));
		cr.salvaImpostazione(ConfigFileConstants.VOLUME_MUSICA, Integer.toString(volumeMusica.getValue()));
		cr.salvaImpostazione(ConfigFileConstants.USERNAME, nomeInserito.getText());
		cr.salvaImpostazione(ConfigFileConstants.NOME_MAPPA, (String)selettoreMappa.getSelectedItem());
		cr.salvaImpostazione(ConfigFileConstants.MOSTRA_LEADERBOARD, Boolean.toString(hideLeaderboard.isSelected()));
		cr.salvaImpostazione(ConfigFileConstants.GRAFICA_SEMPLIFICATA, Boolean.toString(lowGraphicMode.isSelected()));
		cr.salvaImpostazione(ConfigFileConstants.GIOCO_SENZA_FINE, Boolean.toString(endlessMode.isSelected()));
	}

	private void aggiungiPannelliAlContainer() {
		getContentPane().add(PannelloInserimenti,BorderLayout.NORTH);
		getContentPane().add(PannelloOpzioni,BorderLayout.CENTER);
		getContentPane().add(PannelloTastiConferma,BorderLayout.SOUTH);
	}

	private void preimpostaPannelli() throws IOException {
		ConfigurationManager cr = new ConfigurationManager();
		opzEffetti.setSelected(Boolean.parseBoolean(cr.leggiImpostazione(ConfigFileConstants.EFFETTI)));
		volumeEffetti.setValue(Integer.parseInt(cr.leggiImpostazione(ConfigFileConstants.VOLUME_EFFETTI)));
		opzMusica.setSelected(Boolean.parseBoolean(cr.leggiImpostazione(ConfigFileConstants.MUSICA)));
		volumeMusica.setValue(Integer.parseInt(cr.leggiImpostazione(ConfigFileConstants.VOLUME_MUSICA)));
		nomeInserito.setText(cr.leggiImpostazione(ConfigFileConstants.USERNAME));
		selettoreMappa.setSelectedItem(cr.leggiImpostazione(ConfigFileConstants.NOME_MAPPA));
		hideLeaderboard.setSelected(Boolean.parseBoolean(cr.leggiImpostazione(ConfigFileConstants.MOSTRA_LEADERBOARD)));
		lowGraphicMode.setSelected(Boolean.parseBoolean(cr.leggiImpostazione(ConfigFileConstants.GRAFICA_SEMPLIFICATA)));
		endlessMode.setSelected(Boolean.parseBoolean(cr.leggiImpostazione(ConfigFileConstants.GIOCO_SENZA_FINE)));
		selettoreLivello.setSelectedIndex(2);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void creaPannelli() {
		Dimension textFieldSize = new Dimension(300, 26);
		Dimension labelSize = new Dimension(70, 26);
		PannelloInserimentoNome = new JPanel();
		PannelloInserimentoPassword = new JPanel();
		PannelloInserimenti = new JPanel();
		PannelloOpzioni = new JPanel();
		PannelloTastiConferma = new JPanel();
		PannelloMessaggioLogin =  new JPanel();
		PannelloMessaggioImpostazioni =  new JPanel();
		messaggioLogin = new JLabel("Login:");
		messaggioLogin.setFont(new Font(messaggioLogin.getFont().getFontName(), 2, 18));
		messaggioImpostazioni = new JLabel("Impostazioni:");
		messaggioImpostazioni.setFont(new Font(messaggioLogin.getFont().getFontName(), 2, 18));
		messaggioNome = new JLabel("Username");
		messaggioNome.setPreferredSize(labelSize);
		nomeInserito = new JTextField(20);
		nomeInserito.setPreferredSize(textFieldSize);
		messaggioPassword = new JLabel("Password");
		messaggioPassword.setPreferredSize(labelSize);
		passwordInserita = new JPasswordField(20);
		passwordInserita.setPreferredSize(textFieldSize);
		
		messaggioLivello=new JLabel(" Velocità gioco:");
		messaggioMappa=new JLabel(" Mappa:");
		opzEffetti = new JCheckBox("Effetti sonori");
		volumeEffetti = new JSlider(0, 100, 50);
		opzMusica = new JCheckBox("Musica di sottofondo");
		volumeMusica = new JSlider(0, 100, 50);
		String[] data1 = {"bassa", "media", "alta*"}; 
		selettoreLivello = new JComboBox(data1);
		selettoreMappa = new JComboBox(listaFileMappe.toArray());
		hideLeaderboard = new JCheckBox("Mostra classifica");
		lowGraphicMode = new JCheckBox("Grafica semplificata");
		endlessMode = new JCheckBox("Modalità senza fine");
		messaggioInformativo = new JLabel("      *Punteggio valido");

		accedi=new JButton("Accedi e gioca");
		ospite=new JButton("Gioca come ospite");

	}

	private void sistemaPannelli() {

		PannelloMessaggioLogin.add(messaggioLogin);
		PannelloMessaggioImpostazioni.add(messaggioImpostazioni);

		PannelloInserimentoNome.add(messaggioNome);
		PannelloInserimentoNome.add(nomeInserito);
		PannelloInserimentoPassword.add(messaggioPassword);
		PannelloInserimentoPassword.add(passwordInserita);

		PannelloInserimenti.setLayout(new GridLayout(4,1));

		PannelloInserimenti.add(PannelloMessaggioLogin);
		PannelloInserimenti.add(PannelloInserimentoNome);
		PannelloInserimenti.add(PannelloInserimentoPassword);
		PannelloInserimenti.add(PannelloMessaggioImpostazioni);

		PannelloOpzioni.setLayout(new GridLayout(6,2));
		
		PannelloOpzioni.add(messaggioLivello);
		PannelloOpzioni.add(messaggioMappa);
		
		PannelloOpzioni.add(selettoreLivello);
		PannelloOpzioni.add(selettoreMappa);
		
		PannelloOpzioni.add(messaggioInformativo);
		PannelloOpzioni.add(endlessMode);
		
		PannelloOpzioni.add(opzEffetti);
		PannelloOpzioni.add(volumeEffetti);
		
		PannelloOpzioni.add(opzMusica);
		PannelloOpzioni.add(volumeMusica);
		
		PannelloOpzioni.add(hideLeaderboard);
		PannelloOpzioni.add(lowGraphicMode);
		
		PannelloTastiConferma.setLayout(new GridLayout(1, 2));
		PannelloTastiConferma.setPreferredSize(new Dimension(1,40));
		PannelloTastiConferma.add(accedi);
		PannelloTastiConferma.add(ospite);
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client c) {
		client = c;
	}

	public String getNomeUtente() {
		return this.nomeUtente;
	}

	public String getPassword() {
		return this.password;
	}
	
	public boolean isPremuto() {
		return premuto;
	}

	public void setPremuto(boolean premuto) {
		this.premuto = premuto;
	}

	class Listener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object src = e.getSource();
			if (src == accedi){
				if(nomeInserito.getText().equals("")||passwordInserita.getText().equals("")){
					JOptionPane.showMessageDialog(null, 
							"                   Inserisci Username e Password."
									+ "\nNon sei registrato? Registrati gratuitamente sul sito ufficiale!");
				} else {
					UserLocal userLocal = new UserLocal();
					userLocal.setUsername(nomeInserito.getText());
					userLocal.setPassword(passwordInserita.getText());
					partita.setUtente(userLocal);
					boolean autenticato = false;
					try{
						autenticato = getClient().logUser(userLocal.getUsername() , userLocal.getPassword(),partita);

						if(!autenticato){
							JOptionPane.showMessageDialog(null, 
									"           Combinazione Username/Password errata. "
											+ "\nNon sei registrato? Registrati gratuitamente sul sito ufficiale!");
						} else { // successo, sono autenticato
							partita.setOspite(false);
							setPremuto(true);
						}
					} catch (Exception e3){
						autenticato = false;
						JOptionPane.showMessageDialog(null, 
								"Non e' possibile contattare il server, controlla la tua connessione.");
						e3.printStackTrace();
					}

				}
			} else if(src == ospite){
				partita.setOspite(true);
				setPremuto(true);
			}
		}
	}
}


