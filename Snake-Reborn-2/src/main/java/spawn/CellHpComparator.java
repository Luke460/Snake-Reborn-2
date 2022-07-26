package spawn;

import java.util.Comparator;

import gamefield.Cell;

public class CellHpComparator implements Comparator<Cell> {

    @Override
    public int compare(Cell first, Cell second) {
       return Integer.compare(second.getHp(), first.getHp());
    }
	
}
