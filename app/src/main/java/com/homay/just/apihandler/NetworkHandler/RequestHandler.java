package com.homay.just.apihandler.NetworkHandler;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.homay.just.apihandler.Model.DataModel;


import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestHandler {
    private String TAG = "Inside RequestHandler";
    RequestBody requestBody;
    Request request;
ResponseHandler responseHandler;

    public RequestHandler(RequestBody requestBody, String URL, String HTTP_METHOD) {
        this.requestBody = requestBody;
        defineRequest(URL, HTTP_METHOD);
    }



    public void defineRequest(String MY_URL, String HTTP_METHOD) {

        switch (HTTP_METHOD) {
            case "POST":
                request = new Request.Builder()
                        .url(MY_URL)
                        .post(requestBody)
                        .build();
                break;

            case "GET":
                request = new Request.Builder()
                        .url(MY_URL)
                        .get()
                        .build();
                break;

            default:
                Log.i(TAG, "Error: Invalid Request");
        }


    }


    public void executeRequest() {

        NetworkCommunicator networkCommunicator = new NetworkCommunicator(request, requestBody);
        networkCommunicator.execute();
    }





    private static class NetworkCommunicator extends AsyncTask<Void, Void, JsonArray> {
        String TAG = "Network Communicator";
        Request request;
        RequestBody requestBody;

        NetworkCommunicator(Request request, RequestBody requestBody) {
            this.request = request;
            this.requestBody = requestBody;

        }

        @Override
        protected JsonArray doInBackground(Void... voids) {
            OkHttpClient okHttpClient = new OkHttpClient();


            try {
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    Log.i(TAG, "doInBackground: Successful");
                    String stringResponse = response.body().string();

                    JsonParser jsonParser = new JsonParser();

                    JsonArray jsonArray = (JsonArray) jsonParser.parse(stringResponse);


                    return jsonArray;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(JsonArray jsonArray) {
            super.onPostExecute(jsonArray);
            Log.i(TAG, "onPostExecute: jsonarray" + jsonArray);


        }


        //end of inner class
    }

//end of outer class
}
