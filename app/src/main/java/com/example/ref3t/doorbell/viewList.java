package com.example.ref3t.doorbell;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.ArrayList;
import java.util.Arrays;


public class viewList extends ActionBarActivity {
    String[] items;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ListView listView;
    EditText editText;
    private Firebase ref;
    String str_name="";
    ListView listname;
    String [] name_array;
    String [] string_tokanizer;
    String[] name ;

    StringBuffer buffer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);
        listView=(ListView)findViewById(R.id.listview);
        editText=(EditText)findViewById(R.id.txtsearch);

        initList();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    // reset listview
                    initList();

                } else {
                    // perform search
                    searchItem(s.toString());
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
                               Toast.makeText(getBaseContext(),listItems.get(position), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public void searchItem(String textToSearch){
        for(String item:items){
            if(!item.contains(textToSearch)){
                listItems.remove(item);


            }

        }
        adapter.notifyDataSetChanged();


    }
    public void initList(){

        Firebase.setAndroidContext(getApplication().getApplicationContext());

        ref = new Firebase("https://doorbellyamsafer.firebaseio.com/EMPLOYEE");
        buffer=new StringBuffer();

        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                buffer.append(snapshot.getValue());

                str_name = buffer.toString();
                buffer.setLength(0);
                string_tokanizer = str_name.split(",");
                // Toast.makeText(getBaseContext(), snapshot.getValue() + "NNNNNNNN", Toast.LENGTH_SHORT).show();
                for (int index = 0; index < string_tokanizer.length; index++) {
                    if (string_tokanizer[index].contains("name=")) {
                        int indeex1 = string_tokanizer[index].indexOf("=");
                        string_tokanizer[index] = string_tokanizer[index].substring(indeex1 + 1, string_tokanizer[index].indexOf("}"));

                        buffer.append(string_tokanizer[index] + "#");
                    }
                }

                name_array = buffer.toString().split("#");

                items = buffer.toString().split("#");

//                listView.setAdapter(new ArrayAdapter<String>(viewList.this, android.R.layout.simple_list_item_1, name_array));
//
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Toast.makeText(getBaseContext(), name_array[position], Toast.LENGTH_SHORT).show();
//                    }
//                });
                Toast.makeText(getBaseContext(), name_array[1], Toast.LENGTH_SHORT).show();

                //items = new String[]{"canada", "rafeee", "china", "japan", "uSA", "refat", "rere", "rafe3"};


                listItems = new ArrayList<>(Arrays.asList(items));
                adapter = new ArrayAdapter<String>(viewList.this, R.layout.list_item, R.id.txtitem, listItems);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
                        Toast.makeText(getBaseContext(), listItems.get(position), Toast.LENGTH_SHORT).show();

                    }
                });


            }


            @Override
            public void onCancelled(FirebaseError error) {

            }

        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_list, menu);
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
