package com.example.calllogapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import android.provider.CallLog;
import android.database.Cursor;
import java.util.Date;

public class CallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                // Incoming call
                String lastCallDate = getLastCallDate(context);
                Toast.makeText(context, "Last Call: " + lastCallDate, Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getLastCallDate(Context context) {
        String[] projection = new String[]{CallLog.Calls.DATE};
        String sortOrder = CallLog.Calls.DATE + " DESC";
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null, null, sortOrder);

        if (cursor != null && cursor.moveToFirst()) {
            long date = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
            cursor.close();
            return new Date(date).toString();
        }

        if (cursor != null) {
            cursor.close();
        }

        return "No call logs found";
    }
}
