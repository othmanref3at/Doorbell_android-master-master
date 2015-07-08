package com.example.ref3t.doorbell;

import android.content.Intent;
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
    String[] items_name;//array of name
    ArrayList<String> listItems;//list of serch list
    ArrayAdapter<String> adapter;
    ListView listView;
    EditText editText;
    private Firebase ref;//refrence of firbase
    String str_name = "";//string of names
    String str_id = "";//string of ides
    String[] name_array;
    String[] string_tokanizer;
    String[] tokenid;
    private Firebase vistor;
    String[] Id;
    Map<String, String> push_to_firebase = new HashMap<String, String>();
    StringBuffer buffer_name;
    StringBuffer bufferid_token;
    String number = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);
        Intent intent = this.getIntent();
        number = intent.getStringExtra("key");
        listView = (ListView) findViewById(R.id.listview);
        editText = (EditText) findViewById(R.id.txtsearch);
        //call funciton initialise of list to search
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
                    //listener of list item to send notify
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            add_to_database(position);
                            Toast.makeText(getBaseContext(), listItems.get(position) + "kkkk", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void add_to_database(int position) {
        Calendar calender = Calendar.getInstance();
        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd      HH:mm:ss");
        Firebase addhistory = vistor.child("History");
        String date_time = form.format(calender.getTime());
        date_time = date_time.replaceAll(" ", "-");
        //check if the button delivry or visitor
        if (number.equals("delivary")) {
            Toast.makeText(getBaseContext(), "this massage to delivery " + number, Toast.LENGTH_LONG).show();
            push_to_firebase.put("name", listItems.get(position));
            push_to_firebase.put("type", "Delivary");
            push_to_firebase.put("Time", date_time);
            addhistory.push().setValue(push_to_firebase);
        } else {
            push_to_firebase.put("name", listItems.get(position));
            push_to_firebase.put("type", "Vistor");
            push_to_firebase.put("Time", date_time);
            addhistory.push().setValue(push_to_firebase);

            Toast.makeText(getBaseContext(), "this massage to visitor " + number, Toast.LENGTH_LONG).show();
        }//end if
    }

    //function to search item in list
    public void searchItem(String textToSearch) {
        for (String item : items_name) {
            if (!item.contains(textToSearch)) {
                listItems.remove(item);//remove items in array list
            }//end if
        }//end for
        adapter.notifyDataSetChanged();
    }//end function searchItem

    //function intialization of list and add to the database
    public void initList() {
        Firebase.setAndroidContext(getApplication().getApplicationContext());
        ref = new Firebase("https://doorbellyamsafer.firebaseio.com/EMPLOYEE");//refrence of all employee in database
        vistor = new Firebase("https://doorbellyamsafer.firebaseio.com/DataVistor");////refrence of history in database
        final Calendar calender = Calendar.getInstance();//Get calender of pc
        final SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd      HH:mm:ss");
        final Firebase addhistory = vistor.child("History");
        buffer_name = new StringBuffer();//buffer to add name
        bufferid_token = new StringBuffer();//buffer to add id token
        //add listener to list and add to history in firbase
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                buffer_name.append(snapshot.getValue());//get data frome firbase to set name
                bufferid_token.append(snapshot.getValue());//get data frome firbase to set token Api
                str_name = buffer_name.toString();
                str_id = bufferid_token.toString();
                buffer_name.setLength(0);//reset buffer
                bufferid_token.setLength(0);//reset buffer
                string_tokanizer = str_name.split(",");
                tokenid = str_id.split(",");
                //loop to add name and token to firebase
                for (int index = 0; index < string_tokanizer.length; index++) {
                    if (string_tokanizer[index].contains("name=")) {
                        int indeex1 = string_tokanizer[index].indexOf("=");
                        string_tokanizer[index] = string_tokanizer[index].substring(indeex1 + 1, string_tokanizer[index].indexOf("}"));

                        buffer_name.append(string_tokanizer[index] + "#");
                    }//end if
                    if (tokenid[index].contains("token=")) {
                        int indeex2 = tokenid[index].lastIndexOf("=");
                        tokenid[index] = tokenid[index].substring(indeex2 + 1, tokenid[index].length());
                        bufferid_token.append(tokenid[index] + "#");
                    }//end if
                }//end for loop
                name_array = buffer_name.toString().split("#");
                items_name = buffer_name.toString().split("#");
                Id = bufferid_token.toString().split("#");
                listItems = new ArrayList<>(Arrays.asList(items_name));
                adapter = new ArrayAdapter<String>(Employee.this, R.layout.list_item, R.id.txtitem, listItems);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        add_to_database(position);
                        Toast.makeText(getBaseContext(), listItems.get(position) + "**" + Id[position], Toast.LENGTH_SHORT).show();

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

    //Button click Defualt
    public void ClickButtonDefualt(View view) {
        //call function to intialize list of defult employee
        initListDefult();
    }


    public void initListDefult() {
        Firebase.setAndroidContext(getApplication().getApplicationContext());
        ref = new Firebase("https://doorbellyamsafer.firebaseio.com/Defult");//refrence link of firebase of default
        vistor = new Firebase("https://doorbellyamsafer.firebaseio.com/DataVistor");//refrence link of firebase of history
        final Firebase addhistory = vistor.child("History");
        buffer_name = new StringBuffer();
        bufferid_token = new StringBuffer();
        //listener to add list of defult to firbase and push notify to list of default
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                buffer_name.append(snapshot.getValue());//get data from firbase
                bufferid_token.append(snapshot.getValue());//get data from firbase
                str_name = buffer_name.toString();
                str_id = bufferid_token.toString();
                buffer_name.setLength(0);//reset the buffer
                bufferid_token.setLength(0);//reset the buffer
                string_tokanizer = str_name.split(",");
                tokenid = str_id.split(",");
                Calendar calender = Calendar.getInstance();
                SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd      HH:mm:ss");
                string_tokanizer = str_name.split(",");
                //loop to add name and type and date to history in database
                for (int index = 0; index < string_tokanizer.length; index++) {
                    if (string_tokanizer[index].contains("name=")) {
                        int indeex1 = string_tokanizer[index].indexOf("=");
                        string_tokanizer[index] = string_tokanizer[index].substring(indeex1 + 1, string_tokanizer[index].indexOf("}"));
                        Toast.makeText(getBaseContext(), string_tokanizer[index], Toast.LENGTH_SHORT).show();
                        String date_time = form.format(calender.getTime());
                        date_time = date_time.replaceAll(" ", "-");

                        if (number.equals("delivary")) {
                            Toast.makeText(getBaseContext(), "this massage to delivery " + number, Toast.LENGTH_LONG).show();
                            push_to_firebase.put("name", string_tokanizer[index]);
                            push_to_firebase.put("type", "Delivary");
                            push_to_firebase.put("Time", date_time);
                            addhistory.push().setValue(push_to_firebase);
                        } else {
                            push_to_firebase.put("name", string_tokanizer[index]);
                            push_to_firebase.put("type", "Vistor");
                            push_to_firebase.put("Time", date_time);
                            addhistory.push().setValue(push_to_firebase);

                            Toast.makeText(getBaseContext(), "this massage to visitor " + number, Toast.LENGTH_LONG).show();
                        }
                    }//end if
                    if (tokenid[index].contains("token=")) {
                        int indeex2 = tokenid[index].lastIndexOf("token=");
                        tokenid[index] = tokenid[index].substring(indeex2 + 6, tokenid[index].length());
                        Toast.makeText(getBaseContext(), tokenid[index], Toast.LENGTH_SHORT).show();
                    }//end if
                }//end for loop
            }


            @Override
            public void onCancelled(FirebaseError error) {

            }

        });


    }
}
