package com.soprahr.moodapp.fragments;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.soprahr.moodapp.MainActivity;
import com.soprahr.moodapp.R;
import com.soprahr.moodapp.SecondActivity;
import com.soprahr.moodapp.adapters.NotificationAdapter;
import com.soprahr.moodapp.entities.NotificationEntity;

import java.util.ArrayList;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationFragment extends Fragment {

    ArrayList<NotificationEntity> notifications;
    ListView listView;
    private static NotificationAdapter adapter;
    Button btnNotif;
    NotificationEntity notif;

    /** Notification staff **/
    private NotificationManager mNotificationManager;// Notification Manager
    // Notification id for both Notifications
    private final int notificationID_SingleLine = 111;
    // No. of messages count for both type of notifications
    private int numMessages_SingleLine = 0;
    private static Uri alarmSound;// Alarm sound uri
    private final long[] pattern = { 100, 300, 300, 300 };// Vibrate pattern in long array
    /** Notification staff **/

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        btnNotif = view.findViewById(R.id.btn_notif);

        listView = view.findViewById(R.id.notification_list);
        notifications = new ArrayList<>();

        notif = new NotificationEntity(0,"Sondage","Vous avez un sondage à faire...","4","vide");

        notifications.add(new NotificationEntity(0,"Sondage","Vous avez un sondage à faire...","4","vide"));
        notifications.add(new NotificationEntity(0,"Questions quotidennes","Vous devez répondre aux questions quotidiennes","4","vide"));

        adapter= new NotificationAdapter(getActivity(),R.layout.notification_row_item, notifications);

        listView.setAdapter(adapter);

        /** Notification staff **/
        // Set by default alarm sound
        alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // setting notification manager
        mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        /** Notification staff **/

        btnNotif.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {

                getSondageNotification();
                notifications.add(notif);
            }
        });

        return view;
    }

    /** Single Line Notifications Methods **/
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void getSondageNotification() {
        Log.i("Start", "notification");
        /* Invoking the default notification service */
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                getActivity());// Setting builder
        mBuilder.setContentTitle("Sondage");// title
        mBuilder.setContentText("Vous avez des sondages à répondre.");// Message
        mBuilder.setTicker("New Message Alert!");// Ticker
        mBuilder.setSmallIcon(R.drawable.ic_survey_black);// Small icon
        /* Increase notification number every time a new notification arrives */
        mBuilder.setNumber(++numMessages_SingleLine);

        mBuilder.setSound(alarmSound);// set alarm sound
        mBuilder.setVibrate(pattern);// set vibration
        /* Creates an explicit intent for an Activity in your app */
        Intent resultIntent = new Intent(getActivity(),SecondActivity.class);

        resultIntent.putExtra("notificationId", notificationID_SingleLine);// put  notification id into intent
        resultIntent.putExtra("message", "sondage");//Your message to show in next activity

        // Task builder to maintain task for pending intent
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
        stackBuilder.addParentStack(SecondActivity.class);
        /* Adds the Intent that starts the Activity to the top of the stack */
        stackBuilder.addNextIntent(resultIntent);


        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);// Set flag to update current
        mBuilder.setContentIntent(resultPendingIntent);// set content intent

        /* notificationID allows you to update the notification later on. */
        mNotificationManager
                .notify(notificationID_SingleLine, mBuilder.build());
    }


}
