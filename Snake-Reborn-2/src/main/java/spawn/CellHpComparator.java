package spawn;

import java.util.Comparator;

import gamefield.Casella;

public class CellHpComparator implements Comparator<Casella> {

    @Override
    public int compare(Casella first, Casella second) {
       return Integer.compare(second.getHp(), first.getHp());
    }
	
}
