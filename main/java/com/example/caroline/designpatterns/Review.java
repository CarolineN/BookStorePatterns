package com.example.caroline.designpatterns;

/**
 * Created by Caroline on 4/3/2016.
 */

public class Review {

    private long id;
    private String stockName;
    private String date;
    private String message;
    private String rating;


    public Review(String stockName, String date, String message,String rating) {
        this.stockName=stockName;
        this.date=date;
        this.message=message;
        this.rating=rating;
    }
    public Review(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return
                "Stock Title='" + stockName + "\n" +
                "Date='" + date + "\n" +
                "Message='" + message + "\n" +
                "Rating='" + rating + "\n" ;
    }
}
