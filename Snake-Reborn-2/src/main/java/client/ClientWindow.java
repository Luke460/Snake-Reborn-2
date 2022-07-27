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

import org.json.JSONObject;

import audio.SoundManager;
import constants.ConfigFileConstants;
import game.Game;
import game.UserLocal;
import server.client.Client;
import support.FileHandler;

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
	
	private Game game;
	private Client client;
	private String serverSettingsFile;
	private String clientSettingsFile;
	private ArrayList<String> mapList;

	public ClientWindow(String clientSettingsFile, String serverSettingsFile) throws IOException {
		super("Snake Reborn 2");
		this.serverSettingsFile = serverSettingsFile;
		initClient();
		mapList = support.FileHandler.getFolderList(constants.GeneralConstants.MAPS_PATH);
		createPanels();
		linkPanels();
		this.clientSettingsFile = clientSettingsFile;
		initPanelsFromConfigFile();

		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		addListnerToButtons();

		setupWindowSize();
	}
	
	private void initClient() throws IOException {
		String fileContent = support.FileHandler.readFile(this.serverSettingsFile);
		JSONObject settings = new JSONObject(fileContent);
		String host = settings.getString("host");
		int port = settings.getInt("port");
		this.client = new Client(host, port);
	}

	public void reload(Game game) {
		this.game = game;
		game.setClient(client);
		this.actionRequired = false;
	}

	private void setupWindowSize() {
		this.pack();
		Dimension d = new Dimension(480,420);
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
		JSONObject settings = new JSONObject();
		settings.put(ConfigFileConstants.ENABLE_SOUND_EFFECTS, Boolean.toString(soundEffectsCheckBox.isSelected()));
		settings.put(ConfigFileConstants.SOUND_EFFECTS_VOLUME, Integer.toString(soundEffectsVolumeSlider.getValue()));
		settings.put(ConfigFileConstants.ENABLE_MUSIC, Boolean.toString(musicCheckBox.isSelected()));
		settings.put(ConfigFileConstants.MUSIC_VOLUME, Integer.toString(musicVolumeSlider.getValue()));
		settings.put(ConfigFileConstants.USERNAME, usernameTextField.getText());
		settings.put(ConfigFileConstants.MAP_NAME, (String)mapComboBox.getSelectedItem());
		settings.put(ConfigFileConstants.SHOW_INTERFACE, Boolean.toString(showInterfaceCheckBox.isSelected()));
		settings.put(ConfigFileConstants.LOW_GRAPHIC_MODE, Boolean.toString(lowGraphicModeCheckBox.isSelected()));
		settings.put(ConfigFileConstants.ENDLESS_MODE, Boolean.toString(endlessModeCheckBox.isSelected()));
		FileHandler.writeFile(this.clientSettingsFile, settings.toString(4));

	}

	private void initPanelsFromConfigFile() throws IOException {
		try {
			String fileContent = FileHandler.readFile(this.clientSettingsFile);
			JSONObject settings = new JSONObject(fileContent);
			soundEffectsCheckBox.setSelected(settings.getBoolean(ConfigFileConstants.ENABLE_SOUND_EFFECTS));
			soundEffectsVolumeSlider.setValue(settings.getInt(ConfigFileConstants.SOUND_EFFECTS_VOLUME));
			musicCheckBox.setSelected(settings.getBoolean(ConfigFileConstants.ENABLE_MUSIC));
			musicVolumeSlider.setValue(settings.getInt(ConfigFileConstants.MUSIC_VOLUME));
			usernameTextField.setText(settings.getString(ConfigFileConstants.USERNAME));
			mapComboBox.setSelectedItem(settings.getString(ConfigFileConstants.MAP_NAME));
			showInterfaceCheckBox.setSelected(settings.getBoolean(ConfigFileConstants.SHOW_INTERFACE));
			lowGraphicModeCheckBox.setSelected(settings.getBoolean(ConfigFileConstants.LOW_GRAPHIC_MODE));
			endlessModeCheckBox.setSelected(settings.getBoolean(ConfigFileConstants.ENDLESS_MODE));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Cannot read stored settings, loading default values.");
			soundEffectsCheckBox.setSelected(true);
			musicCheckBox.setSelected(true);
			showInterfaceCheckBox.setSelected(true);
		}
	}

	private void createPanels() {
		
		Dimension textFieldSize = new Dimension(220, 28);
		usernamePanel = new JPanel();
		passwordPanel = new JPanel();
		loginContainerPanel = new JPanel();
		settingsContainerPanel = new JPanel();
		buttonsContainerPanel = new JPanel();
		loginMessagePanel =  new JPanel();
		settingsMessagePanel =  new JPanel();
		loginTitleLabel = new JLabel("Login:");
		Font bigFont = (new Font(loginTitleLabel.getFont().getFontName(), Font.ITALIC, 18));
		Font mediumFont = (new Font(loginTitleLabel.getFont().getFontName(), Font.PLAIN, 15));
		Font smallFont = (new Font(loginTitleLabel.getFont().getFontName(), Font.PLAIN, 13));
		loginTitleLabel.setFont(bigFont);
		settingsTitleLabel = new JLabel("Settings:");
		settingsTitleLabel.setFont(bigFont);
		usernameLabel = new JLabel("Username ");
		usernameLabel.setFont(smallFont);
		usernameTextField = new JTextField();
		usernameTextField.setFont(smallFont);
		usernameTextField.setPreferredSize(textFieldSize);
		passwordLabel = new JLabel("Password ");
		passwordLabel.setFont(smallFont);
		passwordTextField = new JPasswordField();
		passwordTextField.setFont(smallFont);
		passwordTextField.setPreferredSize(textFieldSize);
		
		gameSpeedLabel = new JLabel(" Game Speed:");
		gameSpeedLabel.setFont(smallFont);
		mapLabel=new JLabel(" Map Selection:");
		mapLabel.setFont(smallFont);
		soundEffectsCheckBox = new JCheckBox("Enable Sound Effects");
		soundEffectsCheckBox.setFont(smallFont);
		soundEffectsVolumeSlider = new JSlider(0, 100, 50);
		soundEffectsVolumeSlider.setMinorTickSpacing(5);
		soundEffectsVolumeSlider.setSnapToTicks(true);
		musicCheckBox = new JCheckBox("Enable Music");
		musicCheckBox.setFont(smallFont);
		musicVolumeSlider = new JSlider(0, 100, 50);
		musicVolumeSlider.setMinorTickSpacing(5);
		musicVolumeSlider.setSnapToTicks(true);
		String[] speedArray = {"slow", "medium", "competitive*"}; 
		gameSpeedComboBox = new JComboBox<String>(speedArray);
		gameSpeedComboBox.setFont(smallFont);
		gameSpeedComboBox.setSelectedIndex(2);
		String[] mapArray = mapList.toArray(new String[mapList.size()]);
		mapComboBox = new JComboBox<String>(mapArray);
		mapComboBox.setFont(smallFont);
		showInterfaceCheckBox = new JCheckBox("Show Interface");
		showInterfaceCheckBox.setFont(smallFont);
		lowGraphicModeCheckBox = new JCheckBox("Low Graphic Mode");
		lowGraphicModeCheckBox.setFont(smallFont);
		endlessModeCheckBox = new JCheckBox("Endless Mode");
		endlessModeCheckBox.setFont(smallFont);
		validScoreLabel = new JLabel("      *Valid Online Score");
		validScoreLabel.setFont(smallFont);

		loginAndPlayButton=new JButton("Login and Play");
		loginAndPlayButton.setFont(mediumFont);
		playAsGuestButton=new JButton("Play as a Guest");
		playAsGuestButton.setFont(mediumFont);
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
							"                   Enter username and password."
									+ "\nNot registered? Register for free on the official website!");
				} else {
					UserLocal userLocal = new UserLocal();
					userLocal.setUsername(usernameTextField.getText());
					userLocal.setPassword(passwordTextField.getText());
					game.setUser(userLocal);
					boolean autenticato = false;
					try{
						autenticato = getClient().logUser(userLocal.getUsername() , userLocal.getPassword(),game);

						if(!autenticato){
							JOptionPane.showMessageDialog(null, 
									"           Wrong username or password. "
											+ "\nNot registered? Register for free on the official website!");
						} else { // successo, sono autenticato
							game.setGuest(false);
							setActionRequired(true);
						}
					} catch (Exception e3){
						autenticato = false;
						JOptionPane.showMessageDialog(null, 
								"Unable to connect to the server, please check your internet connection.");
						e3.printStackTrace();
					}

				}
			} else if(src == playAsGuestButton){
				game.setGuest(true);
				setActionRequired(true);
			}
		}
	}
}


