package com.hfad.esbroster;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vivek on 02-Apr-16.
 */
public class AddUserActivity extends ActionBarActivity {

    EditText empId_edittext, name_edittext;
    Button button1;

    ProgressDialog pDialog;
Boolean loginflag = false;
    JSONParser jsonParser = new JSONParser();
    private String[] titles; // for drawer
    private ListView drawerList; // for drawer
    private DrawerLayout mdrawerLayout; // for drawer
    private ArrayAdapter<String> mArrayAdapter; // for drawer
    private ActionBarDrawerToggle mDrawerToggle; // for drawer
    private String mActivityTitle; // for drawer


    private static final String ADDINGUSER_URL = "http://www.android2.in/shift_timings_allotter/adduser.php";

    private static final String TAG_SUCCESS = "success";

    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.adduser);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        empId_edittext = (EditText) findViewById(R.id.empidditext_id);
        empId_edittext.setTypeface(Typeface.SERIF);
        name_edittext = (EditText) findViewById(R.id.nameeditext_id);
        name_edittext.setTypeface(Typeface.SERIF);
        button1 = (Button) findViewById(R.id.adduserbutton_id);
        onAddUserButtonClick();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for drawer I m here
        getSupportActionBar().setHomeButtonEnabled(true); // for drawer
        titles = getResources().getStringArray(R.array.titles); // for drawer
        mdrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout); // for drawer
        drawerList = (ListView) findViewById(R.id.drawer); // for drawer
        mActivityTitle = getTitle().toString(); // for drawer




        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, titles); // for drawer
        drawerList.setAdapter(mArrayAdapter);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        setupDrawer();// for drawer
    }
    private void selectItem(int position) { // for drawer
        Intent i;
        switch (position) {
            case 0:
                i = new Intent(AddUserActivity.this, CheckTimingsActivity.class);
                startActivity(i);
                break;
            case 1:
                i = new Intent(AddUserActivity.this, CheckTimingsByDateActivity.class);
                startActivity(i);
                break;
            case 2:
                enterPasswordDialog();
                break;
            case 3:
                i = new Intent(AddUserActivity.this, AddUserActivity.class);
                startActivity(i);
                break;
            case 4:
                i = new Intent(AddUserActivity.this, DeleteUserActivity.class);
                startActivity(i);
                break;
            default:
                i = new Intent(AddUserActivity.this, DrawerActivity.class);
                startActivity(i);


        }
        setActionBarTitle(position); // for drawer
        mdrawerLayout.closeDrawer(drawerList); // for drawer

    }

    private void setActionBarTitle(int position) { // for drawer
        String title;
        if (position == 0) {
            title = titles[position];

        } else {
            title = titles[position];
        }
        getSupportActionBar().setTitle(title);
    }

    private void setupDrawer() { // for drawer
        mDrawerToggle = new ActionBarDrawerToggle(this, mdrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Timesheet");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true); // for drawer
        mdrawerLayout.setDrawerListener(mDrawerToggle); // for drawer


    }

    public boolean onOptionsItemSelected(MenuItem item) { // for drawer


        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) { // for drawer
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) { // for drawer
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void onAddUserButtonClick() {
        button1 = (Button) findViewById(R.id.adduserbutton_id);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(empId_edittext.getText().toString().trim().length()>0 && name_edittext.getText().toString().trim().length()>0) {
                    new AddUser().execute();
                }
                else{
                    Toast.makeText(AddUserActivity.this, "Please enter EmpID/Name", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public boolean enterPasswordDialog(){
        // Create Object of Dialog class
        final Dialog login = new Dialog(this);
        // Set GUI of login screen
        login.setContentView(R.layout.alertbox);
        login.setTitle("Please Login");
        Button btnLogin = (Button) login.findViewById(R.id.btnLogin);
        Button btnCancel = (Button) login.findViewById(R.id.btnCancel);
        final EditText txtUsername = (EditText)login.findViewById(R.id.txtUsername);
        final EditText txtPassword = (EditText)login.findViewById(R.id.txtPassword);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String username_string = txtUsername.getText().toString().trim();
                String password_string = txtPassword.getText().toString().trim();

                if(username_string.equals("123") && password_string.equals("123"))
                {
                    // Validate Your login credential here than display message
                    Toast.makeText(AddUserActivity.this,
                            "Login Successful", Toast.LENGTH_SHORT).show();
                    loginflag=true;
                    Intent i = new Intent(AddUserActivity.this, SaveTimingsActivity.class);
                    startActivity(i);
                    // Redirect to dashboard / home screen.
                    login.dismiss();

                }
                else   if(!username_string.equals("123") && !password_string.equals("123")){
                    Toast.makeText(AddUserActivity.this,
                            "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    loginflag=false;
                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.dismiss();
            }
        });
        login.show();
        return loginflag;
    }


    class AddUser extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddUserActivity.this);
            pDialog.setMessage("Adding new user...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... args) {
            int success2;
            String empid_value1 = empId_edittext.getText().toString();
            String name_value1 = name_edittext.getText().toString();


            try {

                List<NameValuePair> params1 = new ArrayList<NameValuePair>();

                params1.add(new BasicNameValuePair("empid", empid_value1));
                params1.add(new BasicNameValuePair("name", name_value1));
                System.out.println(empid_value1);
                Log.d("EMPID_VALUE:", empid_value1);
                Log.d("NAME_VALUE:", name_value1);
                System.out.println(params1);


                JSONObject json2 = jsonParser.makeHttpRequest(ADDINGUSER_URL, "POST", params1);
                success2 = json2.getInt(TAG_SUCCESS);
                if (success2 == 1) {
                    return json2.getString(TAG_MESSAGE);

                } else {
                    return json2.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(String file_url) {

            // dismiss the dialog once product deleted

            pDialog.dismiss();


            if (file_url != null) {

                Toast.makeText(AddUserActivity.this, file_url, Toast.LENGTH_LONG).show();

            }


        }
    }
}
