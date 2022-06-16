package testGame;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.java.terrenoDiGioco.Mappa;

import java.io.IOException;

public class TestMappa {
	
	Mappa mappaTest1;
	
	@Before
	public void setUp() throws IOException {
		mappaTest1 = new Mappa("mappa-1");
	}

	@Test
	public void testCaricaMappa() {
		assertNotNull(mappaTest1);
	}
	


}
