package terrenoDiGioco;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;

public class Mappa {

	private String nomeUnivoco;
	private HashSet<Stanza> stanze;

	public Mappa(String nomeUnivoco) throws IOException {
		this.nomeUnivoco = nomeUnivoco;
		this.stanze = new HashSet<Stanza>();
	}

	public String getNomeUnivoco() {
		return nomeUnivoco;
	}

	public void setNomeUnivoco(String nomeUnivoco) {
		this.nomeUnivoco = nomeUnivoco;
	}

	public HashSet<Stanza> getStanze() {
		return stanze;
	}

	public void setStanze(HashSet<Stanza> stanze) {
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
