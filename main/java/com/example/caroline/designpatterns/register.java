package com.example.caroline.designpatterns;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
    public void ShowDetails(View v){
        String address = ((TextView) findViewById(R.id.HomeTextField)).getText().toString();
        String payment = ((TextView) findViewById(R.id.PasswordTextField)).getText().toString();
        String name = ((TextView) findViewById(R.id.nameTextField)).getText().toString();
        String password = ((TextView) findViewById(R.id.emailTextView)).getText().toString();
        new MyDownloadTask().execute(address, payment, name, password);
        Intent intent = new Intent(this,BookStore.class);
        startActivity(intent);
    }
    private class MyDownloadTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            BufferedReader inBuffer = null;
            String url = "http://192.168.192.15:8080/create_user";
            String result = "fail";

            String address = params[0];
            String payment = params[1];
            String name = params[2];
            String password= params[3];



            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost request = new HttpPost(url);
                List<NameValuePair> postParameters =
                        new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("address", address));
                postParameters.add(new BasicNameValuePair("payment", payment));
                postParameters.add(new BasicNameValuePair("name", name));
                postParameters.add(new BasicNameValuePair("password", password));


                UrlEncodedFormEntity formEntity =
                        new UrlEncodedFormEntity(postParameters);

                request.setEntity(formEntity);
                HttpResponse httpResponse = httpClient.execute(request);
                inBuffer = new BufferedReader(new InputStreamReader(
                        httpResponse.getEntity().getContent()));

                StringBuffer stringBuffer = new StringBuffer("");
                String line = "";
                String newLine = System.getProperty("line.separator");
                while ((line = inBuffer.readLine()) != null) {
                    stringBuffer.append(line + newLine);
                }
                inBuffer.close();
                result = stringBuffer.toString();
            } catch (Exception e) {
                result = e.getMessage();
            } finally {
                if (inBuffer != null) {
                    try {
                        inBuffer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return result;
        }
    }
}
