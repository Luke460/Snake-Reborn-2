package server.client;

import static constants.GeneralConstants.FILE_NOME_SERVER;
import static constants.GeneralConstants.FILE_PORTA;
import static constants.GeneralConstants.NOME_FILE_INDIRIZZO_SERVER;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.MediaType;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;

import game.Partita;
import server.model.Credentials;
import server.model.Match;
import server.model.User;
import support.FileHandler;

public class Client {

	private JsonService jsonService;
	private HttpClientContext httpClientContext;
	private User userLogged;

	public Client() {
		this.jsonService = new JsonService();
		this.httpClientContext = new HttpClientContext();
	}

	public User addMatch(Match match) throws ClientProtocolException, IOException {

		String matchJson = jsonService.match2Json(match);

		String playerJson = this.sendHttp("/client/addMatch", matchJson);

		return jsonService.json2Player(playerJson);
	}

	public User logUser(Credentials credentials) throws ClientProtocolException, IOException {

		String credentialsJson = jsonService.credentials2Json(credentials);

		String playerJson = this.sendHttp("/client/logUser", credentialsJson);

		return jsonService.json2Player(playerJson);
	}

	public boolean logUser(String username, String password, Partita partita) throws ClientProtocolException, IOException {
		Credentials credentials = new Credentials(username, password);
		//System.out.println(credentials.getUsername() + ":" +  credentials.getPassword());
		this.userLogged = this.logUser(credentials);
		if(this.userLogged!=null && this.userLogged.getUsername().equals(partita.getUtente().getUsername())) {
			return true;
		} else {
			return false;
		}
	}

	public User getUser() {
		return this.userLogged;
	}

	private String sendHttp(String function, String json) throws ClientProtocolException, IOException {

		String host = FileHandler.readFile(NOME_FILE_INDIRIZZO_SERVER);

		int port = Integer.parseInt(FileHandler.readFile(FILE_PORTA));

		UsernamePasswordCredentials credentialsClient = new UsernamePasswordCredentials("SnakeReborn", "Snake123");		
		AuthScope authScope = new AuthScope(host, port);
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(authScope, credentialsClient);

		HttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();

		String responseGSon = null;

		String nomeServer = FileHandler.readFile(FILE_NOME_SERVER);

		String stringRequest = ("http://" + host + ":" + port + nomeServer + function);
		HttpPost request = new HttpPost(stringRequest);
		request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

		StringEntity entity = new StringEntity(json);
		entity.setContentType(MediaType.APPLICATION_JSON);

		request.setEntity(entity);
		HttpResponse response = httpClient.execute(request, this.httpClientContext);

		InputStream is = response.getEntity().getContent();	    
		responseGSon = IOUtils.toString(is, "UTF-8");



		return responseGSon;
	}
}
