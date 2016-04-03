package com.example.caroline.designpatterns;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AddStock extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);
      FileWriterCaretaker caretaker = new FileWriterCaretaker();

       Stock fileWriter = new Stock("Hell","Welll",1,"ddd");

       caretaker.save(fileWriter);
        fileWriter.setAuthor("Second Set of Data\n");

      System.out.println(fileWriter+"\n\n");

      caretaker.undo(fileWriter);

       System.out.println(fileWriter+"\n\n");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_stock, menu);
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
}

