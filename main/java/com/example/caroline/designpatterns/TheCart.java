package com.example.caroline.designpatterns;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

public class TheCart extends AppCompatActivity {
    List<ShoppingCart> carts;
    List<Stock> stocks;
    List<Stock> stocks1;
    List<String>names;
    ListView listView;
    TextView myAwesomeTextView;
    TextView my;
    static CartSingleton instance = CartSingleton.getCartSingletonInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_cart);
        Intent intent = getIntent();

        names=new ArrayList<>();
        new GetCartDetails().execute("http://192.168.0.7:8080/restcarts");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SharedPreferences preferences1 = getSharedPreferences("User_Info", Context.MODE_PRIVATE);
        String name = preferences1.getString("NAME", "");
        stocks1= new ArrayList<>();

        int total=0;

        System.out.println("The name:" + name);
        for(ShoppingCart c:carts){
            total = c.getTotalPrice();
            if(c.getUserName().equalsIgnoreCase(name)){
                for(Stock f1: c.getStocks()){
                    stocks1.add(f1);
                }
            }
        }
        for(Stock stock:stocks1){
            String title= stock.getTitle();
            String author = stock.getAuthor();
            System.out.println(title);
            names.add(title + "\n" + author);
            instance.addStock(stock);

        }
        ShoppingCart cart = new ShoppingCart();
        cart.setUserName(name);
        cart.setTotalPrice(total);

        instance.addCart(cart);
        listView = (ListView) findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_layout,names );
        listView.setAdapter(adapter);


        myAwesomeTextView = (TextView)findViewById(R.id.total);
        my=(TextView)findViewById(R.id.name);
//in your OnCreate() method
        myAwesomeTextView.setText("Total Price:" + cart.getTotalPrice());
        my.setText("Customer:" + cart.getUserName());
        String result = null;
        try {
            result = instance.getCartObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(result);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_the_cart, menu);
        return true;
    }
    public void bought(View v){
        Intent intent = new Intent(this, BookStore.class);
        startActivity(intent);
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
    public class GetCartDetails extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            carts = new ArrayList<>();
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
                System.out.println("Checking right here" +jsonArray);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id = jsonObject.getInt("id");
                    int total = jsonObject.getInt("totalPrice");
                    String userName = jsonObject.getString("userName");
                    JSONArray f = jsonObject.getJSONArray("others");
                    if(f!=null) {
                        for (int j = 0; j < f.length(); j++) {
                            String title = f.getJSONObject(j).getString("title");
                            String author = f.getJSONObject(j).getString("author");
                            int price = f.getJSONObject(j).getInt("price");
                            String category = f.getJSONObject(j).getString("category");
                            Stock f2 = new Stock(title,author,price,category);

                            stocks.add(f2);

                        }


                    ShoppingCart stock1 = new ShoppingCart(userName,total,stocks);//original
                    carts.add(stock1);//original;
                    }
                }
                System.out.println("List Size1... " + carts.size());
                for (ShoppingCart cart : carts) {
                    System.out.println(cart.toString());

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
