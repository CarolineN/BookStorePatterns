package com.example.caroline.designpatterns;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Caroline on 4/9/2016.
 */
public class CartSingleton {

    private List<Stock> stocks = new ArrayList<Stock>();
    private static CartSingleton singletonInstance = null;
    private ShoppingCart cart = new ShoppingCart();

    public static CartSingleton getCartSingletonInstance() {
        if (singletonInstance == null) {
            singletonInstance = new CartSingleton();
        }
        return singletonInstance;
    }

    public void addStock(Stock stock) {
        synchronized (stocks) {
            stocks.add(stock);
        }
    }

    public List<Stock> getStock() {
        return stocks;
    }
    public void addCart(ShoppingCart cart) {
        synchronized (cart) {
            this.cart = cart;
        }
    }
    public String getCartObject() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("userName", cart.getUserName());
        obj.put("totalPrice", cart.getTotalPrice());

        JSONArray employeeJson = new JSONArray();
        for (Stock emp : stocks) {

            Map<String, String> stockData = new HashMap<String, String>();
            stockData.put("title", emp.getTitle());
            stockData.put("auhor", emp.getAuthor());
            stockData.put("price", emp.getPrice() + "");
            stockData.put("category",emp.getCategory());

            employeeJson.put(stockData);
        }
        obj.put("Stocks", employeeJson);
        return obj.toString();
    }

}

