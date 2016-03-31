package com.example.caroline.designpatterns;

import java.util.List;

/**
 * Created by Caroline on 3/30/2016.
 */
public class ShoppingCart {

    private long id;
    private int userId;
    private int totalPrice;
    private List<Stock> stocks;

    public ShoppingCart(int userId, int totalPrice, List<Stock> stocks) {
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.stocks = stocks;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "id=" + id +
                ", userId=" + userId +
                ", totalPrice=" + totalPrice +
                ", stocks=" + stocks +
                '}';
    }
}
