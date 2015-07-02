package com.example.ref3t.doorbell;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {
    public final static String Key = "100";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void visitorClickedButton(View view) {
        Intent intent = new Intent(this, Employee.class);
        int diffrece = 1;
        intent.putExtra("key","200");
//        intent.putExtra(diffrece);
        startActivity(intent);
    }

    public void DelivaryClickedButton(View view) {
        Intent intent = new Intent(this, viewList.class);
        int diffrece = 1;
        intent.putExtra("key","100");
//        intent.putExtra(diffrece);
        startActivity(intent);
    }
}
