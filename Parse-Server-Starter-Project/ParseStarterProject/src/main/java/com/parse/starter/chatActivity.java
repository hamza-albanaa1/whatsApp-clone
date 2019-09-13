package com.parse.starter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class chatActivity extends AppCompatActivity {
    String activeuser = " ";
    ArrayList<String> messages = new ArrayList<>();
    ArrayAdapter adapter;






    public void sendChat(View View){
        final EditText chatEditText = (EditText) findViewById(R.id.chatEditText);
        ParseObject message = new ParseObject("Message");
        final String messageContent = chatEditText.getText().toString();
        message.put("sender",ParseUser.getCurrentUser().getUsername());
        message.put("recipient",activeuser);
        message.put("message",messageContent);
        chatEditText.setText("");
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    messages.add(messageContent);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(chatEditText.getWindowToken(),0);
    }










    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        activeuser = intent.getStringExtra("username");
        setTitle("Chat with "+activeuser);

        ListView chatListView = (ListView) findViewById(R.id.chatlistview);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,messages);
        chatListView.setAdapter(adapter);
        ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("Message");
        query1.whereEqualTo("sender",ParseUser.getCurrentUser().getUsername());
        query1.whereEqualTo("recipient",activeuser);


        ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>("Message");
        query2.whereEqualTo("recipient",ParseUser.getCurrentUser().getUsername());
        query2.whereEqualTo("sender",activeuser);


        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(query1);
        queries.add(query2);


        ParseQuery<ParseObject> query = ParseQuery.or(queries);
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    if(objects.size() >0){
                        messages.clear();
                        for(ParseObject message : objects){

                            String messageContent = message.getString("message");
                            if (!message.getString("sender").equals(ParseUser.getCurrentUser().getUsername())){
                                messageContent = activeuser+ ":-    " +messageContent;

                            }
                            messages.add(messageContent);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }
}
