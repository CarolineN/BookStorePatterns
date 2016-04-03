package com.example.caroline.designpatterns;

import java.util.List;


public class ShoppingCart {

    private long id;
    private String userName;
    private int totalPrice;


    private List<Stock> stocks;

    public ShoppingCart(String userName, int totalPrice, List<Stock> stocks) {
        this.userName = userName;
        this.totalPrice = totalPrice;
        this.stocks = stocks;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
                ", userName=" + userName +
                ", totalPrice=" + totalPrice +
                ", stocks=" + stocks +
                '}';
    }
}