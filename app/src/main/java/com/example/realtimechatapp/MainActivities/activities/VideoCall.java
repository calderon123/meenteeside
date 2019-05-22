package com.example.realtimechatapp.MainActivities.activities;

import android.Manifest;

import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.realtimechatapp.R;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class VideoCall extends AppCompatActivity implements Session.SessionListener, PublisherKit.PublisherListener {

    private static String API_KEY = "46326572";
    private static String SESSION_ID = "1_MX40NjMyNjU3Mn5-MTU1ODU2Nzg1Mzk2OH5HU09JbDVGNWlpdGpJeUlyc1NaaFhwL3N-fgg";
    private static String TOKEN = "T1==cGFydG5lcl9pZD00NjMyNjU3MiZzaWc9NTgzYzc5MjBkMzVmOTYyMTI2NTVjY2UxZjkxMTIwN2NmYTdkZTQwMjpzZXNzaW9uX2lkPTFfTVg0ME5qTXlOalUzTW41LU1UVTFPRFUyTnpnMU16azJPSDVIVTA5SmJEVkdOV2xwZEdwSmVVbHljMU5hYUZod0wzTi1mZyZjcmVhdGVfdGltZT0xNTU4NTY3ODk1Jm5vbmNlPTAuODgxNDczMjg4Mjc5MTAzMiZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNTYxMTU5ODk0JmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9";
     private static String LOG_TAG = VideoCall.class.getSimpleName();
    private static final int RC_SETTINGS= 123;

    private Session session;

    private FrameLayout publisher,subscriber;

    private Publisher publisher1;

    private Subscriber subscriber1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);

        requestPermissions();

        publisher = findViewById(R.id.publisher_container);
        subscriber = findViewById(R.id.subscriber_container);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    @AfterPermissionGranted(RC_SETTINGS)
    private void requestPermissions(){
        String [] perm = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};

        if (EasyPermissions.hasPermissions(this,perm))
        {
            session = new Session.Builder(this,API_KEY,SESSION_ID).build();
            session.setSessionListener(this);
            session.connect(TOKEN);
        }
        else
        {
            EasyPermissions.requestPermissions(this,"Needs acces to your camera and mic",RC_SETTINGS,perm);
        }
    }

    @Override
    public void onConnected(Session session) {
        publisher1 = new Publisher.Builder(this).build();
        publisher1.setPublisherListener(this);

        publisher.addView(publisher1.getView());
        session.publish(publisher1);

    }

    @Override
    public void onDisconnected(Session session) {

    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        if (subscriber1 == null) {
            subscriber1 = new Subscriber.Builder(this,stream).build();
            session.subscribe(subscriber1);
            subscriber.addView(subscriber1.getView());
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {

        if (subscriber1!= null){
            subscriber1 = null;
            subscriber.removeAllViews();
        }

    }

    @Override
    public void onError(Session session, OpentokError opentokError) {

    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }
}
