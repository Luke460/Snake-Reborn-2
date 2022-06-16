package visualizzatoreClient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import javax.swing.JTextField;

import audio.GestoreSuoni;
import game.Partita;
import game.UserLocal;
import server.client.Client;
import supporto.ConfigurationManager;
import supporto.CostantiConfig;
import supporto.OSdetector;

public class VisualizzatoreClient extends JFrame{

	private static final long serialVersionUID = 1L;
	private String nomeUtente;
	private String password;
	private boolean premuto;

	JPanel PannelloMessaggioLogin;
	JPanel PannelloInserimentoNome;
	JPanel PannelloInserimentoPassword;
	JPanel PannelloInserimenti;
	JPanel PannelloOpzioni;
	JPanel PannelloTastiConferma;
	JLabel messaggioLogin;
	JLabel messaggioNome;
	JTextField nomeInserito;
	JLabel messaggioPassword;
	JLabel messaggioLivello;
	JLabel messaggioPopolazione;
	JTextField passwordInserita;	
	JCheckBox opzMusica;
	JCheckBox opzEffetti;
	JCheckBox opzPcVecchio;
	JComboBox<String> selettoreLivello;
	JComboBox<String> selettorePopolazione;
	JLabel messaggioInformativo;
	JButton accedi;
	JButton ospite;
	Partita partita;
	Client client;

	public VisualizzatoreClient() throws IOException {
		super("Snake Reborn");
		client = new Client();
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
		Dimension d = new Dimension(380,260);
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

	public void leggiImpostazioni() throws IOException {
		aggiornaFileImpostazioni();
		GestoreSuoni.setEffettiAbilitati(opzEffetti.isSelected());
		GestoreSuoni.setMusicaAbilitata(opzMusica.isSelected());
		partita.setLivello(selettoreLivello.getSelectedIndex()+1);
		partita.setFattorePopolazione(selettorePopolazione.getSelectedIndex()+1);
		partita.setModPcLento(opzPcVecchio.isSelected());
	}

	private void aggiornaFileImpostazioni() throws IOException {
		ConfigurationManager cr = new ConfigurationManager();
		cr.salvaImpostazione(CostantiConfig.EFFETTI, Boolean.toString(opzEffetti.isSelected()));
		cr.salvaImpostazione(CostantiConfig.MUSICA, Boolean.toString(opzMusica.isSelected()));
		cr.salvaImpostazione(CostantiConfig.USERNAME, nomeInserito.getText());
		cr.salvaImpostazione(CostantiConfig.OTTIMIZZAZIONE, Boolean.toString(opzPcVecchio.isSelected()));
	
	}

	private void aggiungiPannelliAlContainer() {
		getContentPane().add(PannelloInserimenti,BorderLayout.NORTH);
		getContentPane().add(PannelloOpzioni,BorderLayout.CENTER);
		getContentPane().add(PannelloTastiConferma,BorderLayout.SOUTH);
	}

	private void preimpostaPannelli() throws IOException {
		ConfigurationManager cr = new ConfigurationManager();
		opzEffetti.setSelected(Boolean.parseBoolean(cr.leggiImpostazione(CostantiConfig.EFFETTI)));
		opzMusica.setSelected(Boolean.parseBoolean(cr.leggiImpostazione(CostantiConfig.MUSICA)));
		nomeInserito.setText(cr.leggiImpostazione(CostantiConfig.USERNAME));
		if(OSdetector.isWindows()) {
			opzPcVecchio.setSelected(true);
			opzPcVecchio.setEnabled(false);
		} else {
			opzPcVecchio.setSelected(Boolean.parseBoolean(cr.leggiImpostazione(CostantiConfig.OTTIMIZZAZIONE)));
		}
		selettoreLivello.setSelectedIndex(2);
		selettorePopolazione.setSelectedIndex(1);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void creaPannelli() {
		PannelloInserimentoNome = new JPanel();
		PannelloInserimentoPassword = new JPanel();
		PannelloInserimenti = new JPanel();
		PannelloOpzioni = new JPanel();
		PannelloTastiConferma = new JPanel();
		PannelloMessaggioLogin =  new JPanel();
		messaggioLogin = new JLabel("Login:");
		messaggioLogin.setFont(new Font(messaggioLogin.getFont().getFontName(), 2, 18));
		messaggioNome = new JLabel("Username");
		nomeInserito = new JTextField(16);
		messaggioPassword = new JLabel("Password");	
		passwordInserita = new JPasswordField(16);

		messaggioLivello=new JLabel(" Livello avversari:");
		messaggioPopolazione=new JLabel(" Popolazione serpenti:");
		opzMusica = new JCheckBox("Musica di sottofondo");
		opzEffetti = new JCheckBox("Effetti sonori");
		opzPcVecchio = new JCheckBox("ottimizza CPU lenta");
		String[] data1 = {"basso", "medio", "alto*"}; 
		selettoreLivello = new JComboBox(data1);
		String[] data2 = {"bassa", "alta*"};
		selettorePopolazione = new JComboBox(data2);
		messaggioInformativo = new JLabel("*punteggio valido");

		accedi=new JButton("Accedi e gioca");
		ospite=new JButton("Gioca come ospite");

	}

	@SuppressWarnings("serial")
	private void sistemaPannelli() {

		PannelloMessaggioLogin.add(messaggioLogin);

		PannelloInserimentoNome.add(messaggioNome);
		PannelloInserimentoNome.add(nomeInserito);
		PannelloInserimentoPassword.add(messaggioPassword);
		PannelloInserimentoPassword.add(passwordInserita);

		PannelloInserimenti.setLayout(new GridLayout(3,1));

		PannelloInserimenti.add(PannelloMessaggioLogin);
		PannelloInserimenti.add(PannelloInserimentoNome);
		PannelloInserimenti.add(PannelloInserimentoPassword);

		PannelloOpzioni.setLayout(new GridLayout(5,2));
		PannelloOpzioni.add(messaggioLivello);
		PannelloOpzioni.add(messaggioPopolazione);
		PannelloOpzioni.add(selettoreLivello);
		PannelloOpzioni.add(selettorePopolazione);
		PannelloOpzioni.add(opzEffetti);
		PannelloOpzioni.add(messaggioInformativo);
		PannelloOpzioni.add(opzMusica);
		PannelloOpzioni.add(new Component() {});
		PannelloOpzioni.add(opzPcVecchio);
		PannelloTastiConferma.setLayout(new GridLayout(1, 2));
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


