package com.example.ref3t.doorbell;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class Employee extends ActionBarActivity {
    private Firebase ref;
    EditText texxt;

ListView listname;
    String[] name={"refat","toto","wlaa2","mohammad","saja"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee2);
        listname=(ListView)findViewById(R.id.list);
        texxt=(EditText)findViewById(R.id.textView2);
        listname.setAdapter(new ArrayAdapter<String>(Employee.this, android.R.layout.simple_list_item_1, name));
        listname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(),name[position],Toast.LENGTH_SHORT).show();
            }
        });

        Firebase.setAndroidContext(getApplication().getApplicationContext());

        ref = new Firebase("https://doorbellyamsafer.firebaseio.com/emplyee");

        ref.addValueEventListener(new ValueEventListener() {

              @Override
           public void onDataChange(DataSnapshot snapshot) {

                Toast.makeText(getBaseContext(), snapshot.getValue() + "NNNNNNNN", Toast.LENGTH_SHORT).show();
                  texxt.append(snapshot.getValue()+"");

              }

            @Override
            public void onCancelled(FirebaseError error) {

            }

        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_employee, menu);
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
