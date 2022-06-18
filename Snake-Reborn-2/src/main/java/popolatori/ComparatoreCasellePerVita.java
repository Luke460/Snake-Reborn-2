package popolatori;

import java.util.Comparator;

import terrenoDiGioco.Casella;

public class ComparatoreCasellePerVita implements Comparator<Casella> {

    @Override
    public int compare(Casella first, Casella second) {
       return Integer.compare(second.getVita(), first.getVita());
    }
	
}
