package com.example.ref3t.doorbell;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

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

/**
 * Created by ref3t on 13/07/15.
 */
public class PushNotification extends AsyncTask<String, Void, HttpResponse>{
    public String Api_pc;


        @Override
        protected HttpResponse doInBackground(String... Void) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Log.i("msg", "test1");
            HttpClient httpclient = new DefaultHttpClient();
            Log.i("msg", "test2");
            HttpPost httppost = new HttpPost("https://android.googleapis.com/gcm/send");

            HttpResponse Response = null;
            try {
                httppost.setHeader("Content-Type", "application/json");
                httppost.setHeader("Authorization", "key=AIzaSyAQxsSLcZtAV_vplw302NKRuUubxegqKa0");
                //  httppost.setHeader("senderID","736620167368");
                httppost.setHeader("Host","android.googleapis.com");
                //Create JSONObject here
                JSONObject jsonBodyObject = new JSONObject();
                jsonBodyObject.put("registration_ids",new JSONArray("[\""+Api_pc+"\"]"));
                //  jsonBodyObject.put("senderID","736620167368");
                //StringEntity entity = new StringEntity(jsonBodyObject.toString(), "UTF-8");
                ByteArrayEntity entity = new ByteArrayEntity(jsonBodyObject.toString().getBytes("UTF8"));
                httppost.setEntity(entity);

                Log.i("msg", "test3");

                Response = httpclient.execute(httppost);
                Log.i("msg", "test4");
                Log.i("response", Response.getStatusLine().toString());
            } catch (ClientProtocolException e) {
                Log.i("msg", "test6");
                // TODO Auto-generated catch block
            } catch (IOException e) {
                Log.i("msg", "test7");
                // TODO Auto-generated catch block
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return Response;
        }
}
