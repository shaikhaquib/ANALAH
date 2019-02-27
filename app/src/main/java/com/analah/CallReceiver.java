package com.analah;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.Date;

/**
 * Created by haider on 9/22/2018.
 */
public class CallReceiver extends PhonecallReceiver {

    MediaPlayer mediaPlayer;

    @Override
    protected void onIncomingCallStarted(Context ctx, String number, Date start, String name, boolean isIncoming, String path) {


    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start, String name, boolean isIncoming, String path) {

    }


    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
    }

}