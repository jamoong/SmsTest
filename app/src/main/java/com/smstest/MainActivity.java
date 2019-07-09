package com.smstest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.data.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.layout.simple_list_item_1;

public class MainActivity extends AppCompatActivity {

    private static  MainActivity inst;
    ArrayList<String> smsMessageList = new ArrayList<String>();
    ListView smsListView;
    ArrayAdapter arrayAdapter;

//*****
 //   Map<String, List<Message>> mSMS = new HashMap<>();

    public static MainActivity instance() {
        return inst;
    }

    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smsListView = findViewById(R.id.SMSList);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, smsMessageList);

        smsListView.setAdapter(arrayAdapter);

        //Add SMS Read Permission
        //Permission not GRANTED
        if(ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
            refreshSmsInBox();
        } else {
            //set Permission
            final int REQUEST_CODE_ASK_PERMISSION = 123;
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSION);
            refreshSmsInBox();
        }

        //tv_sms.setText("Phone Number: " + phoneNumber1 + " " + "SMS" + SMSBody1);
    }


    public void refreshSmsInBox() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null,null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        arrayAdapter.clear();
        do {
            String str = "SMS From: " + smsInboxCursor.getString(indexAddress) +
                    "\n" + smsInboxCursor.getString(indexBody) + "\n";
            arrayAdapter.add(str);

            //create
//            Message msg = Message.create(str);
//            if( msg == null ) {
//                continue;
//            }
//
//
//            List<Message> messages = mSMS.get(msg.day);
//            if( messages == null ) {
//                messages = new ArrayList<>();
//                mSMS.put(msg.day, messages);
//            }
//            messages.add(msg);

        } while ( smsInboxCursor.moveToNext() );
    }

    public void updateList(final String smsMessage) {
        arrayAdapter.insert(smsMessage, 0);
        arrayAdapter.notifyDataSetChanged();
    }

    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        try {
            String[] smsMessages = smsMessageList.get(pos).split("\n");
            String address = smsMessages[0];
            String smsMessage = "";


            for( String i : smsMessages ) {
                smsMessage += smsMessages[1];
            }
            String smsMessageStr = address + "\n";
            smsMessageStr += smsMessage;
            Toast.makeText(this, smsMessageStr, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
