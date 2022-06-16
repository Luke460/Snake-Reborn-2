package testGenerici;

import org.junit.Test;

public class testMathRandom {

	@Test
	public void test() {
		boolean ok0 = false;
		boolean ok1 = false;
		boolean ok2 = false;
		int numero;
		for(int i = 0; i<100; i++){
			numero = (int)(Math.random() * 3) ;     // da 0 a 2 compresi
			//       (int)(Math.random() * 3) + 1 ; // da 1 a 3 compresi
			System.out.println(numero);
			if(numero==0) ok0 = true;
			if(numero==1) ok1 = true;
			if(numero==2) ok2 = true;
		}
		System.out.println("0"+ok0);
		System.out.println("1"+ok1);
		System.out.println("2"+ok2);
	}

}
