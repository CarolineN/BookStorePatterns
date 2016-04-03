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
import android.widget.EditText;
import android.widget.TextView;
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

public class login extends AppCompatActivity {
    List<User> users;
    List<Admin>admins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        new GetUserDetails().execute("http://192.168.0.7:8080/restusers");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new GetAdmin().execute("http://192.168.0.7:8080/restreports");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void onAdmin(View v) {
        String name = ((EditText) findViewById(R.id.nameText)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordText)).getText().toString();
        if ((admins.size() == 0) || (admins == null)) {
            Toast.makeText(getApplicationContext(),
                    "There are no admins to view",
                    Toast.LENGTH_LONG).show();
        } else {
            for (Admin user1 : admins) {
                if (user1.getUsername().equalsIgnoreCase(name) && user1.getPassword().equals(password)) {
                    Intent intent = new Intent(this, AdminBookStore.class);
                    startActivity(intent);


                }


            }
        }
    }
    public void onLogin(View v) {
        String name = ((EditText) findViewById(R.id.nameText)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordText)).getText().toString();
        if ((users.size() == 0) || (users == null)) {
            Toast.makeText(getApplicationContext(),
                    "There are no users to view",
                    Toast.LENGTH_LONG).show();
        } else {


            for (User user1 : users) {
                if (user1.getName().equalsIgnoreCase(name) && user1.getPassword().equals(password)) {
                    Intent intent = new Intent(this, BookStore.class);
                    startActivity(intent);
                    SharedPreferences preferences = getSharedPreferences("User_Info", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putLong("ID", user1.getId());
                    editor.putString("ADDRESS", user1.getAddress());
                    editor.putString("NAME", user1.getName());
                    editor.putString("PAYMENT", user1.getPayment());
                    editor.putString("PASSWORD", user1.getPassword());
                    editor.commit();
                    break;

                }
            }
        }
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

    public class GetAdmin extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            admins = new ArrayList<>();
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
                    String name = jsonObject.getString("username");

                    String password = jsonObject.getString("password");



                    Admin admin = new Admin(name,password);//original
                    admins.add(admin);//original;
                }
                System.out.println("List Size1... " + users.size());
                for (Admin user1 : admins) {
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
}
