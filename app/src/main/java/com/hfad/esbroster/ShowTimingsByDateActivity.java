package com.hfad.esbroster;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Vivek on 10-Apr-16.
 */
public class ShowTimingsByDateActivity extends ActionBarActivity {
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String CHECKTIMINGSBYDATE_URL = "http://www.android2.in/shift_timings_allotter/checktimingsbydate.php";

    ListView showtimingsbydate_listview;
    private ArrayList<HashMap<String, String>> list;
    RowItem item=null;
    private String[] titles; // for drawer
    private ListView drawerList; // for drawer
    private DrawerLayout mdrawerLayout; // for drawer
    private ArrayAdapter<String> mArrayAdapter; // for drawer
    private ActionBarDrawerToggle mDrawerToggle; // for drawer
    private String mActivityTitle; // for drawer
Boolean loginflag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showtimingsbydate);
        showtimingsbydate_listview = (ListView) findViewById(R.id.listView_bydate_id);
        new showtimingsbydateAsyncTask().execute();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for drawer
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
                i = new Intent(ShowTimingsByDateActivity.this, CheckTimingsActivity.class);
                startActivity(i);
                break;
            case 1:
                i = new Intent(ShowTimingsByDateActivity.this, CheckTimingsByDateActivity.class);
                startActivity(i);
                break;
            case 2:
                enterPasswordDialog();
                break;
            case 3:
                i = new Intent(ShowTimingsByDateActivity.this, AddUserActivity.class);
                startActivity(i);
                break;
            case 4:
                i = new Intent(ShowTimingsByDateActivity.this, DeleteUserActivity.class);
                startActivity(i);
                break;
            default:
                i = new Intent(ShowTimingsByDateActivity.this, DrawerActivity.class);
                startActivity(i);


        }
        setActionBarTitle(position); // for drawer
        mdrawerLayout.closeDrawer(drawerList); // for drawer

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
                    Toast.makeText(ShowTimingsByDateActivity.this,
                            "Login Successful", Toast.LENGTH_SHORT).show();
                    loginflag=true;
                    Intent i = new Intent(ShowTimingsByDateActivity.this, SaveTimingsActivity.class);
                    startActivity(i);
                    // Redirect to dashboard / home screen.
                    login.dismiss();

                }
                else   if(!username_string.equals("123") && !password_string.equals("123")){
                    Toast.makeText(ShowTimingsByDateActivity.this,
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




    class showtimingsbydateAsyncTask extends AsyncTask<String, String, List<RowItem>> {

        JSONArray jsonArray=null;
        List<String> listofdates;
        List<String> listofnames;
        List<String> listofshifts;
        List<RowItem> rowItems;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ShowTimingsByDateActivity.this);
            pDialog.setMessage("Checking the records...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected List<RowItem> doInBackground(String... params) {
            String val = null;
            String val1 = null;
            String val2 = null;
            int i = 0;
            rowItems = new ArrayList<RowItem>();
           listofdates = new ArrayList<String>();
            listofnames = new ArrayList<String>();
            listofshifts = new ArrayList<String>();


            try {
                JSONObject jsonObject = jsonParser.getJSONFromUrl(CHECKTIMINGSBYDATE_URL);
                jsonArray = jsonObject.getJSONArray("allshifts");
              //  Log.d("JSON PARSER", jsonObject);

                if (jsonArray != null) {

                    for ( i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        val = object.getString("Date");
                        val1 = object.getString("Name");
                        val2 = object.getString("Shift");

                        listofdates.add(val);
                        listofnames.add(val1);
                        listofshifts.add(val2);

                         item = new RowItem(listofdates.get(i), listofnames.get(i), listofshifts.get(i));

                        rowItems.add(item);

                    }

                    return rowItems;


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
return null;

        }


        protected void onPostExecute(List<RowItem> result) {
            super.onPostExecute(result);
            pDialog.dismiss();

            if (result != null) {

                CustomListViewAdapter customListViewAdapter = new CustomListViewAdapter(ShowTimingsByDateActivity.this, R.layout.showtimings_unused, rowItems);
                showtimingsbydate_listview.setAdapter(customListViewAdapter);



            } else {
                    finishAfterTransition();
                Toast.makeText(ShowTimingsByDateActivity.this, "No Shift Allotted for the specified date!!", Toast.LENGTH_SHORT).show();


            }
        }
    }
}
