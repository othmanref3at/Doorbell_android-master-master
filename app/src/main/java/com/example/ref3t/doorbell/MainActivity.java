package com.example.ref3t.doorbell;

import android.app.AlertDialog;
import android.app.MediaRouteButton;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity {
    public final static String Key = "100";
    private Firebase ref;//refrence of link firebase of Employee
    private Firebase vistor;//refrence of link firebase of History visitor
    String str_name = "";
    String[] string_tokanizer;
    StringBuffer buffer;
    Map<String, String> push_to_firebase = new HashMap<String, String>();
    String str_id = "";
    String[] tokenid;
    ListView listView;
    StringBuffer bufferid;
    String Api_pc;
    ArrayAdapter<String> adapter;
    ArrayList<String> listItems;//list of serch list
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listviewc);
        TextView txt=(TextView)findViewById(R.id.textmain);
        Typeface tepeface=Typeface.createFromAsset(getAssets(), "fonts/Chantelli_Antiqua.ttf");
        txt.setTypeface(tepeface);

        String [] items_name={"Interveiw","Delivery","Visitor"};
        listItems = new ArrayList<>(Arrays.asList(items_name));

//                Toast.makeText(getBaseContext(), listItems2.toString(), Toast.LENGTH_SHORT).show();

        adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.layoutlistmain, R.id.txtitem, listItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(listItems.get(position).equals("Interveiw")){
                    interClickedButton();
                   // Toast.makeText(getBaseContext(), listItems.get(position) + "*fffffff*" , Toast.LENGTH_SHORT).show();

                }else if(listItems.get(position).equals("Visitor")){
                    visitorClickedButton();
                  //  Toast.makeText(getBaseContext(), listItems.get(position) + "*fffffff*" , Toast.LENGTH_SHORT).show();

                }else{

                    DelivaryClickedButton();
                  //  Toast.makeText(getBaseContext(), listItems.get(position) + "*fffffff*" , Toast.LENGTH_SHORT).show();
                }


            }
        });


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

    //Button visitor click
    public void visitorClickedButton() {
        Intent intent = new Intent(this, Employee.class);//intent to open new activity Employee
        intent.putExtra("key", "visitor");//send data toEmployee activity to how click on button
        startActivity(intent);//start of activity
    }//end button

    //Button Delivary click
    public void DelivaryClickedButton() {
        Intent intent = new Intent(this, Employee.class);//intent to open new activity Employee
        intent.putExtra("key", "delivary");//send data toEmployee activity to how click on button
        startActivity(intent);//start of activity
    }//end button

    //Button Interview click
    public void interClickedButton() {
        interVeiw();//call function to push notification to inter view group
        alertMasseg();


    }//end button

    //function to push the interview group to data base history and push notification to interview group
    public void interVeiw() {
        Firebase.setAndroidContext(getApplication().getApplicationContext());
        ref = new Firebase("https://doorbellyamsafer.firebaseio.com/Interview");//refrence  of firbase interview
        vistor = new Firebase("https://doorbellyamsafer.firebaseio.com/DataVistor");
        final Firebase addhistory = vistor.child("History");
        buffer = new StringBuffer();
        bufferid = new StringBuffer();
        //listener to add the name and time and type to database and push notify to list of interview group
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
                Calendar calender = Calendar.getInstance();
                SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd      HH:mm:ss");
                //loop to get name in firbase and add the name and type data or time to database
                for (int index = 0; index < string_tokanizer.length; index++) {
                    if (string_tokanizer[index].contains("name=")) {
                        int indeex1 = string_tokanizer[index].indexOf("=");
                        string_tokanizer[index] = string_tokanizer[index].substring(indeex1 + 1, string_tokanizer[index].indexOf("}"));
                        //Toast.makeText(getBaseContext(), string_tokanizer[index]+"  ", Toast.LENGTH_SHORT).show();                         ;
                        String date_time = form.format(calender.getTime());
                        date_time = date_time.replaceAll(" ", "-");
                        push_to_firebase.put("name", string_tokanizer[index]);
                        push_to_firebase.put("type", "Interview");
                        push_to_firebase.put("Time", date_time);
                        addhistory.push().setValue(push_to_firebase);
                    }//end if
                    //if statment to get token and push notify to list interview   token
                    if (tokenid[index].contains("token=")) {
                        int indeex2 = tokenid[index].lastIndexOf("token=");
                        tokenid[index] = tokenid[index].substring(indeex2 + 6, tokenid[index].length());
//                        Toast.makeText(getBaseContext(), tokenid[index], Toast.LENGTH_SHORT).show();

                        PushNotification task = new PushNotification();
                        task.Api_pc = tokenid[index];
                        task.execute();


                    }//end if
                }//end loop
            }

            @Override
            public void onCancelled(FirebaseError error) {

            }

        });


    }
    private void alertMasseg() {

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Welcome to yamsafer");
        alertDialog.setMessage("You send notification ...please wait!!"+"01:00");
        alertDialog.show();   //

        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                alertDialog.setMessage("You send notification ...please wait!! (00:"+ (millisUntilFinished/1000)+")");
                if(millisUntilFinished/1000==1){

                    alertDialog.hide();
                }
            }

            @Override
            public void onFinish() {
            }
        }.start();
    }

}
