package com.example.caroline.designpatterns;

/**
 * Created by Caroline on 3/31/2016.
 */
public class ShoppingCartImpl implements ShoppingCartVisitor {

    @Override
    public int visit(Stock stock) {
        int cost = 0;
        //apply 5$ discount if book price is greater than 50
        if (stock.getPrice() > 50) {
            cost = stock.getPrice() - 5;
        } else cost = stock.getPrice();
        System.out.println("Title:" + stock.getTitle() + " cost =" + cost);
        return cost;
    }
}