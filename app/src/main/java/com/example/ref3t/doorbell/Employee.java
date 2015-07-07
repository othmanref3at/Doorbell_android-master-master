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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class Employee extends ActionBarActivity {
    String[] items;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ListView listView;
    EditText editText;
    private Firebase ref;
    String str_name = "";
    String str_id = "";
    String[] name_array;
    String[] string_tokanizer;
    String[] tokenid;
    private Firebase vistor;

    Map<String, String> post2 = new HashMap<String, String>();

    StringBuffer buffer;

    StringBuffer bufferid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);
        listView = (ListView) findViewById(R.id.listview);
        editText = (EditText) findViewById(R.id.txtsearch);

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
                            Toast.makeText(getBaseContext(), listItems.get(position), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void searchItem(String textToSearch) {
        for (String item : items) {
            if (!item.contains(textToSearch)) {
                listItems.remove(item);


            }

        }
        adapter.notifyDataSetChanged();


    }
    String [] Id;
    public void initList() {

        Firebase.setAndroidContext(getApplication().getApplicationContext());
//        Toast.makeText(getBaseContext(), "NNNNNNNN", Toast.LENGTH_SHORT).show();
        ref = new Firebase("https://doorbellyamsafer.firebaseio.com/EMPLOYEE");

        vistor = new Firebase("https://doorbellyamsafer.firebaseio.com/DataVistor");

        final Calendar calender=Calendar.getInstance();
        final SimpleDateFormat form=new SimpleDateFormat("yyyy-MM-dd      HH:mm:ss");

        final Firebase addhistory = vistor.child("History");
        buffer = new StringBuffer();
        bufferid = new StringBuffer();
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                buffer.append(snapshot.getValue());
                bufferid.append(snapshot.getValue());
                str_name = buffer.toString();
                str_id = bufferid.toString();
                buffer.setLength(0);
                bufferid.setLength(0);
                string_tokanizer = str_name.split(",");
                tokenid = str_id.split(",");
                for (int index = 0; index < string_tokanizer.length; index++) {
                    if (string_tokanizer[index].contains("name=")) {
                        int indeex1 = string_tokanizer[index].indexOf("=");
                        string_tokanizer[index] = string_tokanizer[index].substring(indeex1 + 1, string_tokanizer[index].indexOf("}"));

                        buffer.append(string_tokanizer[index] + "#");
                    }
                }
                for (int index = 0; index < tokenid.length; index++) {
                    if (tokenid[index].contains("token=")) {


                        int indeex2 = tokenid[index].lastIndexOf("=");
                        tokenid[index] = tokenid[index].substring(indeex2 + 1, tokenid[index].length());

                        bufferid.append(tokenid[index] + "#");

                    }
                }

                name_array = buffer.toString().split("#");

                items = buffer.toString().split("#");
                Id=bufferid.toString().split("#");

                listItems = new ArrayList<>(Arrays.asList(items));
                adapter = new ArrayAdapter<String>(Employee.this, R.layout.list_item, R.id.txtitem, listItems);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Calendar calender=Calendar.getInstance();
                        SimpleDateFormat form=new SimpleDateFormat("yyyy-MM-dd      HH:mm:ss");

                        Firebase addhistory = vistor.child("History");
                        String date_time=form.format(calender.getTime());

                        date_time=date_time.replaceAll(" ","-");
//                        Toast.makeText(getBaseContext(),date_time, Toast.LENGTH_LONG).show();

                        post2.put("name", listItems.get(position));
                        post2.put("type", "Vistor");
                        post2.put("Time", date_time);
                        addhistory.push().setValue(post2);

                        Toast.makeText(getBaseContext(), listItems.get(position)+"**"+Id[position], Toast.LENGTH_SHORT).show();

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

    public void ClickButtonDefualt(View view) {
        Toast.makeText(this, "lllll", Toast.LENGTH_LONG).show();
        initListDefult();
    }


    public void initListDefult() {

        Firebase.setAndroidContext(getApplication().getApplicationContext());

        ref = new Firebase("https://doorbellyamsafer.firebaseio.com/Defult");

        vistor = new Firebase("https://doorbellyamsafer.firebaseio.com/DataVistor");

        final Firebase addhistory = vistor.child("History");
        buffer = new StringBuffer();
        bufferid=new StringBuffer();
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                buffer.append(snapshot.getValue());
                bufferid.append(snapshot.getValue());
                str_name = buffer.toString();
                str_id = bufferid.toString();

                buffer.setLength(0);
                bufferid.setLength(0);
                string_tokanizer = str_name.split(",");
                tokenid = str_id.split(",");

                Calendar calender=Calendar.getInstance();
                SimpleDateFormat form=new SimpleDateFormat("yyyy-MM-dd      HH:mm:ss");
                string_tokanizer = str_name.split(",");
                for (int index = 0; index < string_tokanizer.length; index++) {
                    if (string_tokanizer[index].contains("name=")) {
                        int indeex1 = string_tokanizer[index].indexOf("=");
                        string_tokanizer[index] = string_tokanizer[index].substring(indeex1 + 1, string_tokanizer[index].indexOf("}"));

                        Toast.makeText(getBaseContext(),string_tokanizer[index], Toast.LENGTH_SHORT).show();
                        String date_time=form.format(calender.getTime());
                        date_time=date_time.replaceAll(" ","-");
//                        Toast.makeText(getBaseContext(),date_time, Toast.LENGTH_LONG).show();

                        post2.put("name", string_tokanizer[index]);
                        post2.put("type", "Vistor");
                        post2.put("Time", date_time);
                        addhistory.push().setValue(post2);
                    }
                }
                for (int index = 0; index < tokenid.length; index++) {
                    if (tokenid[index].contains("token=")) {


                        int indeex2 = tokenid[index].lastIndexOf("token=");
                        tokenid[index] = tokenid[index].substring(indeex2 +6, tokenid[index].length());

                        Toast.makeText(getBaseContext(),tokenid[index], Toast.LENGTH_SHORT).show();


                    }
                }




            }


            @Override
            public void onCancelled(FirebaseError error) {

            }

        });



    }
}
