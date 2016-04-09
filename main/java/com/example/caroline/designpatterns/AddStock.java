package com.example.caroline.designpatterns;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

public class AddStock extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);
//      FileWriterCaretaker caretaker = new FileWriterCaretaker();
//
//       Stock fileWriter = new Stock("Hell","Welll",1,"ddd");
//
//       caretaker.save(fileWriter);
//        fileWriter.setAuthor("Second Set of Data\n");
//
//      System.out.println(fileWriter+"\n\n");
//
//      caretaker.undo(fileWriter);
//
//       System.out.println(fileWriter+"\n\n");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_stock, menu);
        return true;
    }
    public void saveItem(View v){
        FileWriterCaretaker caretaker = new FileWriterCaretaker();
        String category = ((TextView) findViewById(R.id.HomeTextField)).getText().toString();
        String price = ((TextView) findViewById(R.id.PasswordTextField)).getText().toString();
        String title = ((TextView) findViewById(R.id.nameTextField)).getText().toString();
        String author = ((TextView) findViewById(R.id.emailTextView)).getText().toString();
        int foo = Integer.parseInt(price);
        Stock fileWriter = new Stock(title,author,foo,category);
        System.out.println(fileWriter);
        caretaker.save(fileWriter);
        new MyDownloadTask().execute(fileWriter.getTitle(), fileWriter.getAuthor(), price, fileWriter.getCategory());
        ((EditText) findViewById(R.id.HomeTextField)).setText("");
        ((EditText) findViewById(R.id.PasswordTextField)).setText("");
        ((EditText) findViewById(R.id.nameTextField)).setText("");
        ((EditText) findViewById(R.id.emailTextView)).setText("");
        caretaker.undo(fileWriter);
    }
    public void viewAll(View v){
        Intent intent = new Intent(this, AdminBookStore.class);
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
    private class MyDownloadTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            BufferedReader inBuffer = null;
            String url = "http://192.168.0.7:8080/create_stock";
            String result = "fail";

            String title = params[0];
            String author = params[1];
            String price = params[2];
            String category= params[3];



            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost request = new HttpPost(url);
                List<NameValuePair> postParameters =
                        new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("title", title));
                postParameters.add(new BasicNameValuePair("author", author));
                postParameters.add(new BasicNameValuePair("price", price));
                postParameters.add(new BasicNameValuePair("category", category));


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

