package com.example.ref3t.doorbell;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    public final static String Key = "100";
    String[] items;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ListView listView;
    EditText editText;
    private Firebase ref;
    String str_name = "";
    String[] name_array;
    String[] string_tokanizer;
    StringBuffer buffer;

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
        intent.putExtra("key", "200");
//        intent.putExtra(diffrece);
        startActivity(intent);
    }

    public void DelivaryClickedButton(View view) {
        Intent intent = new Intent(this, viewList.class);
        int diffrece = 1;
        intent.putExtra("key", "100");
//        intent.putExtra(diffrece);
        startActivity(intent);
    }

    public void interClickedButton(View view) {
        //interVeiw();

    }


    public void interVeiw() {

        Firebase.setAndroidContext(getApplication().getApplicationContext());

        ref = new Firebase("https://doorbellyamsafer.firebaseio.com/EMPLOYEE");
        buffer = new StringBuffer();

        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                buffer.append(snapshot.getValue());

                str_name = buffer.toString();
                buffer.setLength(0);
                string_tokanizer = str_name.split(",");
                Toast.makeText(getBaseContext(), snapshot.getValue() + "NNNNNNNN", Toast.LENGTH_SHORT).show();
                for (int index = 0; index < string_tokanizer.length; index++) {
                    if (string_tokanizer[index].contains("name=")) {
                        int indeex1 = string_tokanizer[index].indexOf("=");
                        string_tokanizer[index] = string_tokanizer[index].substring(indeex1 + 1, string_tokanizer[index].indexOf("}"));

                        buffer.append(string_tokanizer[index] + "#");
                    }
                }

                name_array = buffer.toString().split("#");

                items = buffer.toString().split("#");

                Toast.makeText(getBaseContext(), name_array[1] + "hhhhhhhhh", Toast.LENGTH_SHORT).show();


            }


            @Override
            public void onCancelled(FirebaseError error) {

            }

        });


    }
}
