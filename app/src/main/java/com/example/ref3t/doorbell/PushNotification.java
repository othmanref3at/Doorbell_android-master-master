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
          //establish conniction
            HttpClient httpclient = new DefaultHttpClient();
            //to initialize post request url
            HttpPost httppost = new HttpPost("https://android.googleapis.com/gcm/send");
            HttpResponse Response = null;
            try {
                //convert curl command to be executable in android ,run notify to pc api using key and api pc 
                httppost.setHeader("Content-Type", "application/json");
                httppost.setHeader("Authorization", "key=AIzaSyAQxsSLcZtAV_vplw302NKRuUubxegqKa0");
                httppost.setHeader("Host","android.googleapis.com");
                JSONObject jsonBodyObject = new JSONObject();
                jsonBodyObject.put("registration_ids",new JSONArray("[\""+Api_pc+"\"]"));
                ByteArrayEntity entity = new ByteArrayEntity(jsonBodyObject.toString().getBytes("UTF8"));
                httppost.setEntity(entity);
                Response = httpclient.execute(httppost);
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return Response;
        }
}
