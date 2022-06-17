package terrenoDiGioco;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Mappa {

	private String nomeUnivoco;
	private ArrayList<Stanza> stanze;

	public Mappa(String nomeUnivoco) throws IOException {
		this.nomeUnivoco = nomeUnivoco;
		this.stanze = new ArrayList<Stanza>();
	}

	public String getNomeUnivoco() {
		return nomeUnivoco;
	}

	public void setNomeUnivoco(String nomeUnivoco) {
		this.nomeUnivoco = nomeUnivoco;
	}

	public ArrayList<Stanza> getStanze() {
		return stanze;
	}

	public void setStanze(ArrayList<Stanza> stanze) {
		this.stanze = stanze;
	}

	@Override
	public int hashCode() {
		return Objects.hash(nomeUnivoco);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mappa other = (Mappa) obj;
		return Objects.equals(nomeUnivoco, other.nomeUnivoco);
	}

}
