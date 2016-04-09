package com.example.caroline.designpatterns;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ViewReviews extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    List<Review> reviews;
    List<Stock>stocks;
    List<Review>reviews2;
    List<String> stockItems;
    List<Stock> products;
    ListView listView;
    Menu1 menu;
    int total;
    ItemElement[] items;
    public static String item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reviews);
        new GetStockDetails().execute("http://192.168.0.7:8080/reststocks");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new GetReviewDetails().execute("http://192.168.0.7:8080/restreviews");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        addToList();
        products = new ArrayList<>();

    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        item = parent.getItemAtPosition(position).toString();


    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    public void goBack(View v) {
        ((EditText) findViewById(R.id.search)).setText("");
        addToList();
    }

    public void addStock(View v) {
        Intent intent = new Intent(this, AddStock.class);
        startActivity(intent);
    }

    public void addToList() {
        stockItems = new ArrayList<>();
        menu = new Menu1();// iterator pattern
        for (Stock stock : stocks) {

            menu.addStock(stock);
        }
        for (Stock stock1 : menu.stocks) {
            String author = stock1.getAuthor();
            String title = stock1.getTitle();
            String category = stock1.getCategory();
            int price = stock1.getPrice();
            stockItems.add("Title: " + title + "\n" + "Author: " + author + "\n" + "Caregory: " + category + "\n" + "Price: " + price);
        }
        listView = (ListView) findViewById(R.id.list);
        ArrayAdapter<Stock> adapter = new ArrayAdapter<Stock>(this, R.layout.row_layout, menu.stocks);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
                Stock stock;

                stock = (Stock)listView.getItemAtPosition(index);

                Intent intent = new Intent(ViewReviews.this, CreateReview.class);
                intent.putExtra("StockName",stock.getTitle());
                startActivity(intent);

                return false;

            }
        });

    }

    private static int calculatePrice(List<Stock> products) {
        ShoppingCartVisitor visitor = new ShoppingCartImpl();
        int sum = 0;
        for (Stock stock : products) {
            sum = sum + stock.accept(visitor);
        }
        return sum;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_store, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void strategy(View view) {

        reviews2= new ArrayList<>();
        String searchedItem = ((EditText) findViewById(R.id.search)).getText().toString();
        for(Review r:reviews){
            if(r.getStockName().equalsIgnoreCase(searchedItem)){
                reviews2.add(r);
            }
        }
        System.out.println("hhhhh" + reviews2.toString());
        listView = (ListView) findViewById(R.id.list);
        ArrayAdapter<Review> adapter = new ArrayAdapter<Review>(this, R.layout.row_layout, reviews2);
        listView.setAdapter(adapter);


    }

    public class GetStockDetails extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            stocks = new ArrayList<>();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONArray jsonArray = new JSONArray(finalJson);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id = jsonObject.getInt("id");
                    String title = jsonObject.getString("title");
                    String author = jsonObject.getString("author");
                    int price = jsonObject.getInt("price");
                    String category = jsonObject.getString("category");


                    Stock stock = new Stock(title, author, price, category);//original
                    stocks.add(stock);//original;
                }
                System.out.println("List Size1... " + stocks.size());
                for (Stock stock : stocks) {
                    System.out.println(stock.toString());

                }
                return buffer.toString();

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;

        }
    }

    public class GetReviewDetails extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            reviews = new ArrayList<>();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONArray jsonArray = new JSONArray(finalJson);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id = jsonObject.getInt("id");
                    String date = jsonObject.getString("date");
                    String message = jsonObject.getString("message");
                    String rating = jsonObject.getString("rating");
                    String stockName = jsonObject.getString("stockName");


                    Review review = new Review(stockName,date,message,rating);//original
                    reviews.add(review);//original;
                }
                System.out.println("List Size1... " + stocks.size());
                for (Review review : reviews) {
                    System.out.println(review.toString());

                }
                return buffer.toString();

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;

        }
    }

}

