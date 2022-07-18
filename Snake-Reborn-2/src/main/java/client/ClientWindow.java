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

import audio.SoundManager;
import constants.ConfigFileConstants;
import game.Partita;
import game.UserLocal;
import loaders.ConfigurationManager;
import server.client.Client;

public class ClientWindow extends JFrame{

	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private boolean actionRequired;

	private JPanel loginContainerPanel;
	private JPanel settingsContainerPanel;
	private JPanel buttonsContainerPanel;
	
	private JPanel loginMessagePanel;
	private JPanel settingsMessagePanel;
	private JPanel usernamePanel;
	private JPanel passwordPanel;
	
	private JLabel loginTitleLabel;
	private JLabel settingsTitleLabel;
	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private JLabel gameSpeedLabel;
	private JLabel mapLabel;
	private JLabel validScoreLabel;
	
	private JTextField usernameTextField;
	private JTextField passwordTextField;
	
	private JCheckBox musicCheckBox;
	private JSlider musicVolumeSlider;
	private JCheckBox soundEffectsCheckBox;
	private JSlider soundEffectsVolumeSlider;
	private JComboBox<String> gameSpeedComboBox;
	private JComboBox<String> mapComboBox;
	private JCheckBox showInterfaceCheckBox;
	private JCheckBox lowGraphicModeCheckBox;
	private JCheckBox endlessModeCheckBox;

	private JButton loginAndPlayButton;
	private JButton playAsGuestButton;
	
	private Partita game;
	private Client client;
	private ArrayList<String> mapList;

	public ClientWindow() throws IOException {
		super("Snake Reborn");
		client = new Client();
		mapList = support.FileHandler.getFolderList(constants.GeneralConstants.MAPS_PATH);
		createPanels();
		linkPanels();
		initPanelsFromConfigFile();

		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		addListnerToButtons();

		setupWindowSize();
	}
	
	public void reload(Partita game) {
		this.game = game;
		game.setClient(client);
		this.actionRequired = false;
	}

	private void setupWindowSize() {
		this.pack();
		Dimension d = new Dimension(460,400);
		this.setSize(d);
		this.setMinimumSize(d);
		this.setResizable(true);
		this.setLocationRelativeTo(null); 
	}

	private void addListnerToButtons() {
		Listener listener = new Listener();
		playAsGuestButton.addActionListener(listener);
		loginAndPlayButton.addActionListener(listener);
	}

	public void readClientSettings() throws IOException {
		updateConfigurationFile();
		SoundManager.soundInitialization(soundEffectsVolumeSlider.getValue(), musicVolumeSlider.getValue());
		SoundManager.enableSoundEffects(soundEffectsCheckBox.isSelected());
		SoundManager.enableMusic(musicCheckBox.isSelected());
		game.setGameSpeed(gameSpeedComboBox.getSelectedIndex()+1);
		game.setMapFileName(mapList.get(mapComboBox.getSelectedIndex()));
		game.setShowInterface(showInterfaceCheckBox.isSelected());
		game.setLowGraphicMode(lowGraphicModeCheckBox.isSelected());
		game.setEndlessMode(endlessModeCheckBox.isSelected());
	}

	private void updateConfigurationFile() throws IOException {
		ConfigurationManager cm = new ConfigurationManager();
		cm.updateSetting(ConfigFileConstants.EFFETTI, Boolean.toString(soundEffectsCheckBox.isSelected()));
		cm.updateSetting(ConfigFileConstants.VOLUME_EFFETTI, Integer.toString(soundEffectsVolumeSlider.getValue()));
		cm.updateSetting(ConfigFileConstants.MUSICA, Boolean.toString(musicCheckBox.isSelected()));
		cm.updateSetting(ConfigFileConstants.VOLUME_MUSICA, Integer.toString(musicVolumeSlider.getValue()));
		cm.updateSetting(ConfigFileConstants.USERNAME, usernameTextField.getText());
		cm.updateSetting(ConfigFileConstants.NOME_MAPPA, (String)mapComboBox.getSelectedItem());
		cm.updateSetting(ConfigFileConstants.MOSTRA_INTERFACCIA, Boolean.toString(showInterfaceCheckBox.isSelected()));
		cm.updateSetting(ConfigFileConstants.GRAFICA_SEMPLIFICATA, Boolean.toString(lowGraphicModeCheckBox.isSelected()));
		cm.updateSetting(ConfigFileConstants.GIOCO_SENZA_FINE, Boolean.toString(endlessModeCheckBox.isSelected()));
		cm.updateFile();
	}

	private void initPanelsFromConfigFile() throws IOException {
		ConfigurationManager cm = new ConfigurationManager();
		cm.readFile();
		soundEffectsCheckBox.setSelected(Boolean.parseBoolean(cm.getSetting(ConfigFileConstants.EFFETTI)));
		soundEffectsVolumeSlider.setValue(Integer.parseInt(cm.getSetting(ConfigFileConstants.VOLUME_EFFETTI)));
		musicCheckBox.setSelected(Boolean.parseBoolean(cm.getSetting(ConfigFileConstants.MUSICA)));
		musicVolumeSlider.setValue(Integer.parseInt(cm.getSetting(ConfigFileConstants.VOLUME_MUSICA)));
		usernameTextField.setText(cm.getSetting(ConfigFileConstants.USERNAME));
		mapComboBox.setSelectedItem(cm.getSetting(ConfigFileConstants.NOME_MAPPA));
		showInterfaceCheckBox.setSelected(Boolean.parseBoolean(cm.getSetting(ConfigFileConstants.MOSTRA_INTERFACCIA)));
		lowGraphicModeCheckBox.setSelected(Boolean.parseBoolean(cm.getSetting(ConfigFileConstants.GRAFICA_SEMPLIFICATA)));
		endlessModeCheckBox.setSelected(Boolean.parseBoolean(cm.getSetting(ConfigFileConstants.GIOCO_SENZA_FINE)));
		gameSpeedComboBox.setSelectedIndex(2);
	}

	private void createPanels() {
		Dimension textFieldSize = new Dimension(300, 26);
		Dimension labelSize = new Dimension(70, 26);
		usernamePanel = new JPanel();
		passwordPanel = new JPanel();
		loginContainerPanel = new JPanel();
		settingsContainerPanel = new JPanel();
		buttonsContainerPanel = new JPanel();
		loginMessagePanel =  new JPanel();
		settingsMessagePanel =  new JPanel();
		loginTitleLabel = new JLabel("Login:");
		loginTitleLabel.setFont(new Font(loginTitleLabel.getFont().getFontName(), 2, 18));
		settingsTitleLabel = new JLabel("Impostazioni:");
		settingsTitleLabel.setFont(new Font(loginTitleLabel.getFont().getFontName(), 2, 18));
		usernameLabel = new JLabel("Username");
		usernameLabel.setPreferredSize(labelSize);
		usernameTextField = new JTextField(20);
		usernameTextField.setPreferredSize(textFieldSize);
		passwordLabel = new JLabel("Password");
		passwordLabel.setPreferredSize(labelSize);
		passwordTextField = new JPasswordField(20);
		passwordTextField.setPreferredSize(textFieldSize);
		
		gameSpeedLabel=new JLabel(" Velocità gioco:");
		mapLabel=new JLabel(" Mappa:");
		soundEffectsCheckBox = new JCheckBox("Effetti sonori");
		soundEffectsVolumeSlider = new JSlider(0, 100, 50);
		musicCheckBox = new JCheckBox("Musica di sottofondo");
		musicVolumeSlider = new JSlider(0, 100, 50);
		String[] speedArray = {"bassa", "media", "alta*"}; 
		gameSpeedComboBox = new JComboBox<String>(speedArray);
		String[] mapArray = mapList.toArray(new String[mapList.size()]);
		mapComboBox = new JComboBox<String>(mapArray);
		showInterfaceCheckBox = new JCheckBox("Mostra interfaccia");
		lowGraphicModeCheckBox = new JCheckBox("Grafica semplificata");
		endlessModeCheckBox = new JCheckBox("Modalità senza fine");
		validScoreLabel = new JLabel("      *Punteggio valido");

		loginAndPlayButton=new JButton("Accedi e gioca");
		playAsGuestButton=new JButton("Gioca come playAsGuestButton");
	}

	private void linkPanels() {
		loginMessagePanel.add(loginTitleLabel);
		settingsMessagePanel.add(settingsTitleLabel);

		usernamePanel.add(usernameLabel);
		usernamePanel.add(usernameTextField);
		passwordPanel.add(passwordLabel);
		passwordPanel.add(passwordTextField);

		loginContainerPanel.setLayout(new GridLayout(4,1));

		loginContainerPanel.add(loginMessagePanel);
		loginContainerPanel.add(usernamePanel);
		loginContainerPanel.add(passwordPanel);
		loginContainerPanel.add(settingsMessagePanel);

		settingsContainerPanel.setLayout(new GridLayout(6,2));
		
		settingsContainerPanel.add(gameSpeedLabel);
		settingsContainerPanel.add(mapLabel);
		
		settingsContainerPanel.add(gameSpeedComboBox);
		settingsContainerPanel.add(mapComboBox);
		
		settingsContainerPanel.add(validScoreLabel);
		settingsContainerPanel.add(endlessModeCheckBox);
		
		settingsContainerPanel.add(soundEffectsCheckBox);
		settingsContainerPanel.add(soundEffectsVolumeSlider);
		
		settingsContainerPanel.add(musicCheckBox);
		settingsContainerPanel.add(musicVolumeSlider);
		
		settingsContainerPanel.add(showInterfaceCheckBox);
		settingsContainerPanel.add(lowGraphicModeCheckBox);
		
		buttonsContainerPanel.setLayout(new GridLayout(1, 2));
		buttonsContainerPanel.setPreferredSize(new Dimension(1,40));
		buttonsContainerPanel.add(loginAndPlayButton);
		buttonsContainerPanel.add(playAsGuestButton);
		
		getContentPane().add(loginContainerPanel,BorderLayout.NORTH);
		getContentPane().add(settingsContainerPanel,BorderLayout.CENTER);
		getContentPane().add(buttonsContainerPanel,BorderLayout.SOUTH);
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client c) {
		client = c;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}
	
	public boolean isActionRequired() {
		return actionRequired;
	}

	public void setActionRequired(boolean premuto) {
		this.actionRequired = premuto;
	}

	class Listener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object src = e.getSource();
			if (src == loginAndPlayButton){
				if(usernameTextField.getText().equals("")||passwordTextField.getText().equals("")){
					JOptionPane.showMessageDialog(null, 
							"                   Inserisci Username e Password."
									+ "\nNon sei registrato? Registrati gratuitamente sul sito ufficiale!");
				} else {
					UserLocal userLocal = new UserLocal();
					userLocal.setUsername(usernameTextField.getText());
					userLocal.setPassword(passwordTextField.getText());
					game.setUtente(userLocal);
					boolean autenticato = false;
					try{
						autenticato = getClient().logUser(userLocal.getUsername() , userLocal.getPassword(),game);

						if(!autenticato){
							JOptionPane.showMessageDialog(null, 
									"           Combinazione Username/Password errata. "
											+ "\nNon sei registrato? Registrati gratuitamente sul sito ufficiale!");
						} else { // successo, sono autenticato
							game.setOspite(false);
							setActionRequired(true);
						}
					} catch (Exception e3){
						autenticato = false;
						JOptionPane.showMessageDialog(null, 
								"Non e' possibile contattare il server, controlla la tua connessione.");
						e3.printStackTrace();
					}

				}
			} else if(src == playAsGuestButton){
				game.setOspite(true);
				setActionRequired(true);
			}
		}
	}
}


