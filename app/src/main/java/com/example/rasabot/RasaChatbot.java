package com.example.rasabot;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RasaChatbot {
//    private static final String TAG = "RasaChatbot";
    private static Context context;

//    private static boolean matchFound=false;

    public interface ChatbotResponseListener {
        void onResponse(String response);
//        void onError(String errorMessage);
    }
    public RasaChatbot(Activity activity) {
        this.context = activity;
    }


    public static void sendMessage(final Context context, final String message, final SharedPreferences sharedPreferences, final ChatbotResponseListener listener) {
        new AsyncTask<Void, Void, String>() {
            private ChatbotResponseListener errorListener;
            boolean serverIsOffline = true;

            @Override
            protected String doInBackground(Void... params) {

                String savedIpAddress = sharedPreferences.getString("ipAddress", "");
                String RASA_SERVER_URL = "http://" + savedIpAddress + ":5005/webhooks/rest/webhook";

//                Log.d(TAG, "Sending message to Rasa server: " + message);
                    if (isServerActive(RASA_SERVER_URL)) {
//                    Log.d(TAG, "Rasa server is active. Sending message: " + message);
                        return sendChatMessage(RASA_SERVER_URL,message);
                    } else {
//                    Log.d(TAG, "Rasa server is not active. Cannot send message.");
                        return "Bot is offlint..., Please try after some time.";
                    }

            }

            @Override
            protected void onPostExecute(String response) {
                if (listener != null) {
//                    Log.d(TAG, "Received response from Rasa server: " + response);
                    listener.onResponse(response);
                }
                else {
//                    errorListener.onError("Error sending message to Rasa server");
//                    listener.onResponse("Error sending message to Rasa server");
                }
            }
        }.execute();
    }
    private static boolean isServerActive(String serverUrl) {
//        Log.d(TAG,"try to enter into isservice active method.");
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(serverUrl).openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
//            Log.d(TAG, "Server is not active. Response Code : " + responseCode);
            return responseCode != 404; // Check if the response code is not 404 (Not Found)
        } catch (IOException e) {
            return false; // Server is not active or cannot connect
        }
    }

    private static String sendChatMessage(String RASA_SERVER_URL, String message) {
        try {
            List<String> mainPhrases = new ArrayList<>();
//            Log.d(TAG, "tring the server....");
            URL url = new URL(RASA_SERVER_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            JSONObject requestBody = new JSONObject();
            requestBody.put("message", message);

            OutputStream os = con.getOutputStream();
            os.write(requestBody.toString().getBytes());
            os.flush();

            int responseCode = con.getResponseCode();
//            Log.d(TAG, "Response Code : " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                JSONArray jsonResponse = new JSONArray(response.toString());
//                Log.d(TAG, "Response from Rasa server: " + jsonResponse.toString());
//                Log.d(TAG, "befor entering into extracting "+response);
                if (jsonResponse.length() > 0) {
                    StringBuilder allTexts = new StringBuilder();

                    for (int i = 0; i < jsonResponse.length(); i++) {
                        JSONObject responseObj = jsonResponse.getJSONObject(i);
                        String botResponse = responseObj.getString("text");
                        allTexts.append(botResponse).append("\n");
                        if(i==0){
                            allTexts.append("\n");
                        }

                    }
//                    Log.e(TAG, "check log 1::"+allTexts.toString());
                    return allTexts.toString();
                } else {
//                    Log.e(TAG, "Empty response from Rasa server.");
                    return "Empty response from Rasa server.";
                }
            }
            else {
//                Log.e(TAG, "Failed to get response from Rasa server. Response Code : " + responseCode);
            }
        } catch (JSONException e) {
//            Log.e(TAG, "Error sending message to Rasa server", e);
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
