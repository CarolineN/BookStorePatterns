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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateReview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_review);
        Bundle extras=getIntent().getExtras();
        String name = extras.getString("StockName");
        System.out.println("hhhh" + name);

        ((EditText) findViewById(R.id.titleTextField)).setText(name);

//        String review = ((TextView) findViewById(R.id.reviewTextView)).getText().toString();
//        String rating =((TextView))findViewById(R.id.RatingTextField)).getText().toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_review, menu);
        return true;
    }
public void saveItem(View v){
    String title = ((TextView) findViewById(R.id.titleTextField)).getText().toString();
    String message = ((TextView) findViewById(R.id.reviewTextView)).getText().toString();
    String rating = ((TextView) findViewById(R.id.RatingTextField)).getText().toString();
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    String reportDate=null;
    try {
        Date today = dateFormat.parse(dateFormat.format(new Date()));
        reportDate = dateFormat.format(today);
    } catch (ParseException e) {
        e.printStackTrace();
    }
    new MyDownloadTask().execute(reportDate,message,rating,title);
    Intent intent = new Intent(this, ViewReviews.class);

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
            String url = "http://147.252.141.139:8080/create_review";
            String result = "fail";

            String date = params[0];
            String message = params[1];
            String rating = params[2];
            String stockName= params[3];



            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost request = new HttpPost(url);
                List<NameValuePair> postParameters =
                        new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("date", date));
                postParameters.add(new BasicNameValuePair("message",message));
                postParameters.add(new BasicNameValuePair("rating", rating));
                postParameters.add(new BasicNameValuePair("stockName", stockName));


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
