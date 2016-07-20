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
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CheckTimingsActivity extends ActionBarActivity {

    Button checkbutton;
    EditText edittext_date;
    Spinner spinner1;
    // EditText eT1, eT2;
    Boolean loginflag=false;

    int year_x, month_x, day_x;
    int year_y, month_y, day_y;
    static final int DIALOG_ID_STARTDATE = 0;
    ArrayList<String> listItems=new ArrayList<>();
    ArrayAdapter<String> adapter;


    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser(); // object of JSON PARSER CLASS. Initialization
  
    private String[] titles; // for drawer
    private ListView drawerList; // for drawer
    private DrawerLayout mdrawerLayout; // for drawer
    private ArrayAdapter<String> mArrayAdapter; // for drawer
    private ActionBarDrawerToggle mDrawerToggle; // for drawer
    private String mActivityTitle; // for drawer


    private static final String LOGIN_URL = "http://www.android2.in/timesheet/checktimings.php";
    private static final String SHOWNAMES_URL = "http://www.android2.in/shift_timings_allotter/shownames.php";




   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  getWindow().setEnterTransition(animFadein);
       // overridePendingTransition(R.anim.slide_right, R.anim.slide_right);
        setContentView(R.layout.checktimings);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //------------------------------------------------------
       // animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                //R.anim.slide_right);
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
        // for datepicker ----------------------------------------
        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        year_y = cal.get(Calendar.YEAR);
        month_y = cal.get(Calendar.MONTH);
        day_y = cal.get(Calendar.DAY_OF_MONTH);


        spinner1 =(Spinner) findViewById(R.id.name_spinner);
        adapter=new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.txt,listItems);
        spinner1.setAdapter(adapter);
        new SpinnerNameAsyncTask().execute();
        edittext_date = (EditText) findViewById(R.id.edittext_id_date);
        edittext_date.setTypeface(Typeface.SERIF);

        showDialogOnFromDateButtonClick();


        onCheckButtonClick();


    }

    public class SpinnerNameAsyncTask extends AsyncTask<Void,Void,Void> {
        ArrayList<String> list;
        private JSONArray mNames = null;
        protected void onPreExecute(){
            super.onPreExecute();
            list=new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... params) {

            JSONParser jsonParser1 = new JSONParser();
            try {
                JSONObject jsonObject = jsonParser1.getJSONFromUrl(SHOWNAMES_URL);
                mNames = jsonObject.getJSONArray("names");
                for(int i =0; i<mNames.length();i++){
                    JSONObject c = mNames.getJSONObject(i);
                    String title = c.getString("Name");
                    list.add(title);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(Void result){
            listItems.addAll(list);
            adapter.notifyDataSetChanged();
        }
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
                    Toast.makeText(CheckTimingsActivity.this,
                            "Login Successful", Toast.LENGTH_SHORT).show();
                    loginflag=true;
                    Intent i = new Intent(CheckTimingsActivity.this, SaveTimingsActivity.class);
                    startActivity(i);
                    // Redirect to dashboard / home screen.
                    login.dismiss();

                }
                else   if(!username_string.equals("123") && !password_string.equals("123")){
                    Toast.makeText(CheckTimingsActivity.this,
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
                i = new Intent(CheckTimingsActivity.this, CheckTimingsActivity.class);
                startActivity(i);
                break;
            case 1:
                i = new Intent(CheckTimingsActivity.this, CheckTimingsByDateActivity.class);
                startActivity(i);
                break;
            case 2:
               enterPasswordDialog();
                /*i = new Intent(CheckTimingsActivity.this, SaveTimingsActivity.class);
                startActivity(i);*/
                break;
            case 3:
                i = new Intent(CheckTimingsActivity.this, AddUserActivity.class);
                startActivity(i);
                break;
            case 4:
                i = new Intent(CheckTimingsActivity.this, DeleteUserActivity.class);
                startActivity(i);
                break;
            default:
                i = new Intent(CheckTimingsActivity.this, DrawerActivity.class);
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

    public void showDialogOnFromDateButtonClick() {

        edittext_date = (EditText) findViewById(R.id.edittext_id_date);
        edittext_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID_STARTDATE);
            }
        });
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_ID_STARTDATE:
                return new DatePickerDialog(this, dpickerListener1, year_x, month_x, day_x);

        }

        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener1 = new DatePickerDialog.OnDateSetListener() {

        @Override

        public void onDateSet(DatePicker view, int year1, int monthOfYear1, int dayOfMonth1) {
            year_y = year1;
            month_y = monthOfYear1 + 1;
            day_y = dayOfMonth1;

            String date = year1 + "-" + (monthOfYear1 + 1) + "-" + dayOfMonth1;

            edittext_date.setText(date);
        }
    };

    public void onCheckButtonClick() {

        checkbutton = (Button) findViewById(R.id.checkbutton_id);
        checkbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner1.getSelectedItem().toString().trim().length() > 0 && edittext_date.getText().toString().trim().length() > 0) {
                    new check().execute();

                } else {
                    Toast.makeText(CheckTimingsActivity.this, "Please Enter Name/Date", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    class check extends AsyncTask<String, String, String> {
        // String c = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CheckTimingsActivity.this);
            pDialog.setMessage("Showing the details...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            //Toast.makeText(CheckTimingsActivity.this, "onPreExecute completed", Toast.LENGTH_LONG).show();


        }

        @Override
        protected String doInBackground(String... params) {


            String name_value1 = spinner1.getSelectedItem().toString();
            String startdate_value1 = edittext_date.getText().toString();
            /// String enddate_value1 = eT2.getText().toString();

            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("name", name_value1));
            parameters.add(new BasicNameValuePair("startdate", startdate_value1));

            // parameters.add(new BasicNameValuePair("enddate", enddate_value1));
            try {
                JSONObject jsonObject = jsonParser.makeHttpRequest(LOGIN_URL, "POST", parameters);

                // c = jsonObject.getString(KEY_NAME);

                // Log.v("VALUE OF C: ", c);
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
            Intent showtimings_intent = new Intent(CheckTimingsActivity.this, ShowTimingsActivity.class);
            startActivity(showtimings_intent);

        }
    }








      /*  private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                year_x = year;
                month_x = monthOfYear + 1;
                day_x = dayOfMonth;


                String fromdate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
               // editText1.setText(fromdate);


            }
        };*/


}

