package com.example.caroline.designpatterns;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

public class ViewCustomers extends AppCompatActivity {
    List<User> users;
    List<Stock>stocks;
    List<String>items;
    List<String>items1;
    List<ShoppingCart>carts;
    ListView listView;
    ListView listView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customers);
        new GetUserDetails().execute("http://192.168.0.7:8080/restusers");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new GetCartDetails().execute("http://192.168.0.7:8080/restcarts");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        items=new ArrayList<>();
        items1=new ArrayList<>();
for(ShoppingCart c:carts){
    String name =c.getUserName();
    int total =c.getTotalPrice();
    items.add("Name:" + name +" "+ "Total:" +"" + total + "£");
}
        listView = (ListView) findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_layout,items );
        listView.setAdapter(adapter);

        for(User u:users){
            String nam1=u.getName();
            items1.add("Name:" + nam1);
        }
        listView1 = (ListView) findViewById(R.id.list1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.row_layout,items1);
        listView1.setAdapter(adapter1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_customers, menu);
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
    public class GetUserDetails extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            users = new ArrayList<>();
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
                    String address = jsonObject.getString("address");
                    String name = jsonObject.getString("name");
                    String password = jsonObject.getString("password");
                    String payment = jsonObject.getString("payment");


                    User user = new User(address, payment, name, password);//original
                    users.add(user);//original;
                }
                System.out.println("List Size1... " + users.size());
                for (User user1 : users) {
                    System.out.println(user1.toString());

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
