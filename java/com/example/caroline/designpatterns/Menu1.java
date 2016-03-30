package com.example.caroline.designpatterns;

/**
 * Created by Caroline on 3/27/2016.
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
// for the iterator
public class Menu1 {

    List<Stock> stocks;

    public Menu1() {
        stocks = new ArrayList<Stock>();
    }

    public void addStock(Stock stock) {
        stocks.add(stock);
    }

    public Iterator<Stock> iterator() {
        return new MenuIterator();
    }

    class MenuIterator implements Iterator<Stock> {
        int currentIndex = 0;

        @Override
        public boolean hasNext() {
            if (currentIndex >= stocks.size()) {
                return false;
            } else {
                return true;
            }
        }

        @Override
        public Stock next() {
            return stocks.get(currentIndex++);
        }

        @Override
        public void remove() {
            stocks.remove(--currentIndex);
        }

    }

}