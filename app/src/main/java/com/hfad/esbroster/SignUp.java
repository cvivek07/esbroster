package com.hfad.esbroster;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SignUp extends Activity {

    EditText email_et, phonenum_et, fullname_et, orgname_et, pass_et, confirmpass_et;
    Button signup_button;
JSONParser jsonParser = new JSONParser();
    private static final String SIGNUP_URL = "http://www.android2.in/timesheet/signup.php";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signuppage);

        findIDs();
        signUpButtonClick();

    }

    protected void findIDs() {
        email_et = (EditText) findViewById(R.id.email_id);
        phonenum_et = (EditText) findViewById(R.id.phonenumber_id);
        fullname_et = (EditText) findViewById(R.id.fullname_id);
        orgname_et = (EditText) findViewById(R.id.orgname_id);
        pass_et = (EditText) findViewById(R.id.pass_id);
        confirmpass_et = (EditText) findViewById(R.id.confirmpass_id);
        signup_button = (Button) findViewById(R.id.signup_id);


    }

    protected void signUpButtonClick() {
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
new SignUpAsyncTask().execute();
            }
        });
    }

    class SignUpAsyncTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            String email_string = email_et.getText().toString().trim();
            String phonenumber_string = email_et.getText().toString().trim();
            String fullname_string = email_et.getText().toString().trim();
            String orgname_string = email_et.getText().toString().trim();
            String pass_string = email_et.getText().toString().trim();

            List<NameValuePair> params1 = new ArrayList<NameValuePair>();


            params1.add(new BasicNameValuePair("email", email_string));
            params1.add(new BasicNameValuePair("phonenumber", phonenumber_string));
            params1.add(new BasicNameValuePair("fullname", fullname_string));
            params1.add(new BasicNameValuePair("orgname", orgname_string));
            params1.add(new BasicNameValuePair("password", pass_string));


            try {
                JSONObject json = jsonParser.makeHttpRequest(

                        SIGNUP_URL, "POST", params1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}
