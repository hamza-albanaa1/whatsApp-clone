/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnKeyListener {
  Boolean loginModeActive = false;
  EditText usernameEditText;
  EditText passwordEditText;





  public void moveToListUsers(){
    if(ParseUser.getCurrentUser() !=null){
      Intent intent = new Intent(getApplicationContext(),UserListActivity.class);
      startActivity(intent);
    }
  }




  public void signUp (View view) {
    if (usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")) {
      Toast.makeText(this, "A username and password are required", Toast.LENGTH_SHORT).show();
    } else {
      if (loginModeActive) {
//for log in

        ParseUser.logInInBackground(usernameEditText.getText().toString(),
                passwordEditText.getText().toString(), new LogInCallback() {
                  @Override
                  public void done(ParseUser user, ParseException e) {
                    if(user != null){
                      Log.i("Login","Ok!  ");
                      moveToListUsers();

                    }else{
                      Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                  }
                });
      } else {
//for sign in
        ParseUser newuser = new ParseUser();
        newuser.setUsername(usernameEditText.getText().toString());
        newuser.setPassword(passwordEditText.getText().toString());
        newuser.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e) {
            if (e == null) {
              Log.i("sign up ", " Success");
              moveToListUsers();

            } else {
              Toast.makeText(MainActivity.this,
                      e.getMessage().substring(e.getMessage().indexOf(" ")),
                      Toast.LENGTH_SHORT).show();
            }
          }
        });
      }
    }
  }







  public void login (View view){
    Button loginsingButton = (Button) findViewById(R.id.signUpButton);
    TextView loginTextView = (TextView) findViewById(R.id.loginTextView);
    if(loginModeActive){
      loginModeActive =false;
      loginsingButton.setText("Sing up");
      loginTextView.setText("Or, Login");

    }else{
      loginModeActive = true;
      loginsingButton.setText("Login");
      loginTextView.setText("Or, Sign up");

    }
  }









  public void background (View view){
    RelativeLayout background = (RelativeLayout) findViewById(R.id.layout);
    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    mgr.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
  }







  @Override
  public boolean onKey(View view, int i, KeyEvent keyEvent) {
    if(i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
      signUp(view);
    }
    return false;
  }





  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    usernameEditText = (EditText) findViewById(R.id.usernameeditText);
    passwordEditText = (EditText) findViewById(R.id.PasswordeditText);
    passwordEditText.setOnKeyListener(this);
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
    setTitle("whatsapp login");
    //moveToListUsers();
  }








}