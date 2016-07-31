package com.hfad.esbroster;

import android.app.DatePickerDialog;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Vivek on 07-Apr-16.
 */
public class CheckTimingsByDateActivity extends ActionBarActivity {

    private String[] titles; // for drawer
    private ListView drawerList; // for drawer
    private DrawerLayout mdrawerLayout; // for drawer
    private ArrayAdapter<String> mArrayAdapter; // for drawer
    private ActionBarDrawerToggle mDrawerToggle; // for drawer
    private String mActivityTitle; // for drawer
    EditText selectdate_edittext;
    Button submit_button;
    static final int DIALOG_ID_STARTDATE = 0;
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    boolean loginflag= false;

    final Calendar mCalendar = Calendar.getInstance();

    private static final String CHECKTIMINGSBYDATE_URL = "http://www.android2.in/shift_timings_allotter/checktimingsbydate.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checktimingsbydate);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for drawer I m here
        getSupportActionBar().setHomeButtonEnabled(true); // for drawer
        titles = getResources().getStringArray(R.array.titles); // for drawer
        mdrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout); // for drawer
        drawerList = (ListView) findViewById(R.id.drawer); // for drawer
        mActivityTitle = getTitle().toString(); // for drawer

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3571727848659159~2627701427");
        AdView adView1 = (AdView) findViewById(R.id.adView1);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        adView1.loadAd(adRequest1);


        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, titles); // for drawer
        drawerList.setAdapter(mArrayAdapter);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        setupDrawer();// for drawer
        onSelectDateEdittextClick();
        onSubmitButtonClick();

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
                    Toast.makeText(CheckTimingsByDateActivity.this,
                            "Login Successful", Toast.LENGTH_SHORT).show();
                    loginflag=true;
                    Intent i = new Intent(CheckTimingsByDateActivity.this, SaveTimingsActivity.class);
                    startActivity(i);
                    // Redirect to dashboard / home screen.
                    login.dismiss();

                }
                else   if(!username_string.equals("123") && !password_string.equals("123")){
                    Toast.makeText(CheckTimingsByDateActivity.this,
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
    private void selectItem(int position) { // for drawer
        Intent i;
        switch (position) {
            case 0:
                i = new Intent(CheckTimingsByDateActivity.this, CheckTimingsActivity.class);
                startActivity(i);
                break;
            case 1:
                i = new Intent(CheckTimingsByDateActivity.this, CheckTimingsByDateActivity.class);
                startActivity(i);
                break;
            case 2:
                enterPasswordDialog();

                break;
            case 3:
                i = new Intent(CheckTimingsByDateActivity.this, AddUserActivity.class);
                startActivity(i);
                break;
            case 4:
                i = new Intent(CheckTimingsByDateActivity.this, DeleteUserActivity.class);
                startActivity(i);
                break;
            default:
                i = new Intent(CheckTimingsByDateActivity.this, DrawerActivity.class);
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

    public void onSelectDateEdittextClick() {

        selectdate_edittext = (EditText) findViewById(R.id.edittext_selectdate);
        selectdate_edittext.setTypeface(Typeface.SERIF);
        selectdate_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID_STARTDATE);
            }
        });
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_ID_STARTDATE:
                return showDatePicker();

        }

        return super.onCreateDialog(id);
    }

    private DatePickerDialog showDatePicker() {

        DatePickerDialog datePicker = new DatePickerDialog(CheckTimingsByDateActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateEdittext();
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        return datePicker;
    }

    private void updateDateEdittext() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String dateForEdittext = dateFormat.format(mCalendar.getTime());
        selectdate_edittext.setText(dateForEdittext);

    }

    private void onSubmitButtonClick() {
        submit_button = (Button) findViewById(R.id.submitbutton_id);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectdate_edittext.getText().toString().trim().length()>0) {
                    new checktimingsbydateAsyncTask().execute();
                }
                else{
                    Toast.makeText(CheckTimingsByDateActivity.this, "Please select a date", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class checktimingsbydateAsyncTask extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CheckTimingsByDateActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            //Toast.makeText(CheckTimingsActivity.this, "onPreExecute completed", Toast.LENGTH_LONG).show();


        }

        @Override
        protected String doInBackground(String... params) {

            int success;

            String date_value = selectdate_edittext.getText().toString();
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("date", date_value));
            System.out.print(date_value);


            try {
                JSONObject jsonObject = jsonParser.makeHttpRequest(CHECKTIMINGSBYDATE_URL, "POST", parameters);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            //  Toast.makeText(CheckTimingsActivity.this, c, Toast.LENGTH_LONG).show();

            // Toast.makeText(CheckTimingsActivity.this, "POST EXECUTE DONE!!", Toast.LENGTH_LONG).show();
            Intent showtimingsbydate_intent = new Intent(CheckTimingsByDateActivity.this, ShowTimingsByDateActivity.class);
            startActivity(showtimingsbydate_intent);

        }
    }
}


