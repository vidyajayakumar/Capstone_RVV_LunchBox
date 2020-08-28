package com.vidya.lunchbox.utils;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vidya.lunchbox.R;
import com.vidya.lunchbox.helper.SessionManager;
import com.vidya.lunchbox.view.CategoryListActivity;
import com.vidya.lunchbox.view.ItemsActivity;
import com.vidya.lunchbox.view.LoginActivity;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;

public class WebsocketStandalone extends AppCompatActivity {


    public static WebSocketClient mWebSocketClient;

    ObjectMapper mapper = new ObjectMapper();

    public void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("ws://ec2-54-144-155-131.compute-1.amazonaws.com:8080/ws/LunchBox");
            //uri = new URI("ws://192.168.0.21:8080/ws/LunchBox");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {


            }

            @Override
            public void onMessage(String s) {
                System.out.println(s);
                try {
//                    SessionManager sessionManager = new SessionManager(getApplicationContext());
//                    sessionManager.getUserData().getEmail();
                    JsonNode actualObj = mapper.readTree(s);
                    if(actualObj.get("action").asText().contains("getAllProducts")){

                    }else if(actualObj.get("action").asText().contains("stockNotification")){
                        Toast.makeText(getApplicationContext(),"Stock notificationt",Toast.LENGTH_SHORT).show();
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                 }

            }

            @Override
            public void onClose(int i, String s, boolean b) {
                System.out.println("close----");
            }

            @Override
            public void onError(Exception e) {
                System.out.println("error-----");
            }
        };
        mWebSocketClient.connect();
    }


    public void sendNotification(){

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.appicon)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");


        // Gets an instance of the NotificationManager service//

               NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String MyText = "Reminder";
        Notification mNotification = new Notification(R.drawable.common_google_signin_btn_icon_dark, MyText, System.currentTimeMillis() );

        int NOTIFICATION_ID = 1;
        notificationManager.notify(NOTIFICATION_ID , mNotification);
    }



}
