package com.hfad.esbroster;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class SaveTimingsActivity extends ActionBarActivity {
    EditText editText1, editText2, edittext_name;
    Spinner spinner1,  spinner2;
    ArrayList<String> listItems=new ArrayList<>();
    ArrayAdapter<String> adapter;

    Button button1, button2, button3, button4, button5;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private String[] titles; // for drawer
    private ListView drawerList; // for drawer
    private DrawerLayout mdrawerLayout; // for drawer
    private ArrayAdapter<String> mArrayAdapter; // for drawer
    private ActionBarDrawerToggle mDrawerToggle; // for drawer
    private String mActivityTitle; // for drawer


    private static final String SAVING_URL = "http://www.android2.in/shift_timings_allotter/savetimings.php";
    private static final String DELETING_URL = "http://www.android2.in/shift_timings_allotter/deletetimings.php";
    private static final String UPDATING_URL = "http://www.android2.in/shift_timings_allotter/updatetimings.php";
    private static final String SHOWNAMES_URL = "http://www.android2.in/shift_timings_allotter/shownames.php";
    private static final String TAG_SUCCESS = "success";

    private static final String TAG_MESSAGE = "message";

    int year_x, month_x, day_x;
    int year_y, month_y, day_y;
    static final int DIALOG_ID_STARTDATE = 0;
    static final int DIALOG_ID_ENDDATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savetimings);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for drawer I m here
        getSupportActionBar().setHomeButtonEnabled(true); // for drawer
        titles = getResources().getStringArray(R.array.titles); // for drawer
        mdrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout); // for drawer
        drawerList = (ListView) findViewById(R.id.drawer); // for drawer
        mActivityTitle = getTitle().toString(); // for drawer
        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        year_y = cal.get(Calendar.YEAR);
        month_y = cal.get(Calendar.MONTH);
        day_y = cal.get(Calendar.DAY_OF_MONTH);
        spinner1 = (Spinner) findViewById(R.id.name_spinner);

        new SpinnerNameAsyncTask().execute();
        adapter=new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.txt,listItems);
        spinner1.setAdapter(adapter);
       // edittext_name = (EditText) findViewById(R.id.name_edittext_id);
       // edittext_name.setTypeface(Typeface.SERIF);
        spinner2 = (Spinner) findViewById(R.id.shift_spinner);


        showAdminDialogOnFromDateButtonClick();
        //showAdminDialogOnToDateButtonClick();
        onSaveButtonClick();
        onDeleteButtonClick();
        onUpdateButtonClick();
        onAddUserButtonClick();
        onDeleteUserButtonClick();
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




    private void selectItem(int position) { // for drawer
        Intent i;
        switch (position) {
            case 0:
                i = new Intent(SaveTimingsActivity.this, CheckTimingsActivity.class);
                startActivity(i);
                break;
            case 1:
                i = new Intent(SaveTimingsActivity.this, CheckTimingsByDateActivity.class);
                startActivity(i);
                break;
            case 2:
                i = new Intent(SaveTimingsActivity.this, SaveTimingsActivity.class);
                startActivity(i);
                break;
            case 3:
                i = new Intent(SaveTimingsActivity.this, AddUserActivity.class);
                startActivity(i);
                break;
            case 4:
                i = new Intent(SaveTimingsActivity.this, DeleteUserActivity.class);
                startActivity(i);
                break;
            default:
                i = new Intent(SaveTimingsActivity.this, DrawerActivity.class);
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

    public void showAdminDialogOnFromDateButtonClick() {

        editText1 = (EditText) findViewById(R.id.edittext_ID1);
        editText1.setTypeface(Typeface.SERIF);
        editText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID_STARTDATE);
            }
        });
    }


    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear + 1;
            day_x = dayOfMonth;


            String fromdate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            editText1.setText(fromdate);


        }
    };


    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_ID_STARTDATE:
                return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x);
            case DIALOG_ID_ENDDATE:
                return new DatePickerDialog(this, dpickerListener1, year_y, month_y, day_y);
        }

        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener1 = new DatePickerDialog.OnDateSetListener() {

        @Override

        public void onDateSet(DatePicker view, int year1, int monthOfYear1, int dayOfMonth1) {
            year_y = year1;
            month_y = monthOfYear1 + 1;
            day_y = dayOfMonth1;

            String todate = year1 + "-" + (monthOfYear1 + 1) + "-" + dayOfMonth1;

            editText2.setText(todate);
        }
    };

    public void onSaveButtonClick() {

        button1 = (Button) findViewById(R.id.savebutton);
       // Typeface typeface = Typeface.createFromAsset(getAssets(), "OstrichSans-Bold.otf");
        //button1.setText("show");
        //button1.setTypeface(typeface);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CreateUser().execute();
            }
        });
    }

    public void onDeleteUserButtonClick(){
        button5 = (Button) findViewById(R.id.deleteuserbutton);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SaveTimingsActivity.this, DeleteUserActivity.class);
                startActivity(i);
            }
        });

    }

    class CreateUser extends AsyncTask<String, String, String> {

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SaveTimingsActivity.this);
            pDialog.setMessage("Saving the details...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... args) {
            int success;
            String name_value = spinner1.getSelectedItem().toString();

            String startdate_value = editText1.getText().toString();

            String shift_value = spinner2.getSelectedItem().toString();


            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();


                params.add(new BasicNameValuePair("name", name_value));
                params.add(new BasicNameValuePair("startdate", startdate_value));

                //  params.add(new BasicNameValuePair("enddate", enddate_value));
                params.add(new BasicNameValuePair("shift", shift_value));


                Log.d("request!", "starting");


                //Posting user data to script

                JSONObject json = jsonParser.makeHttpRequest(

                        SAVING_URL, "POST", params);


                // full json response

                Log.d("Login attempt", json.toString());


                // json success element

                success = json.getInt(TAG_SUCCESS);

                Log.v("SUCCESS RESPONSE: ", String.valueOf(success));

                if (success == 1) {

                    Log.d("User Created!", json.toString());


                    return json.getString(TAG_MESSAGE);

                } else if (success == 0) {

                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));

                    return json.getString(TAG_MESSAGE);


                } else {

                    return json.getString(TAG_MESSAGE);
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

                Toast.makeText(SaveTimingsActivity.this, file_url, Toast.LENGTH_LONG).show();

            }


        }
    }

    private void onDeleteButtonClick() {
        button2 = (Button) findViewById(R.id.deletebutton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteUser().execute();
            }
        });

    }

    class DeleteUser extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SaveTimingsActivity.this);
            pDialog.setMessage("Deleting the records...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            int success1;
            String name_value1 = edittext_name.getText().toString();
            String startdate_value1 = editText1.getText().toString();

            String shift_value1 = spinner2.getSelectedItem().toString();


            List<NameValuePair> params1 = new ArrayList<NameValuePair>();


            params1.add(new BasicNameValuePair("name", name_value1));
            params1.add(new BasicNameValuePair("startdate", startdate_value1));
            params1.add(new BasicNameValuePair("shift", shift_value1));

            try {
                JSONObject json1 = jsonParser.makeHttpRequest(DELETING_URL, "POST", params1);
                success1 = json1.getInt(TAG_SUCCESS);
                if (success1 == 0) {


                    return json1.getString(TAG_MESSAGE);

                } else {


                    return json1.getString(TAG_MESSAGE);


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

                Toast.makeText(SaveTimingsActivity.this, file_url, Toast.LENGTH_LONG).show();

            }


        }
    }

    private void onUpdateButtonClick() {

        button3 = (Button) findViewById(R.id.updatebutton);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateUser().execute();
            }
        });
    }

    class UpdateUser extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SaveTimingsActivity.this);
            pDialog.setMessage("Updating the records...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            int success2;
            String name_value2 = edittext_name.getText().toString();
            String startdate_value2 = editText1.getText().toString();

            String shift_value2 = spinner2.getSelectedItem().toString();


            List<NameValuePair> params2 = new ArrayList<NameValuePair>();


            params2.add(new BasicNameValuePair("name", name_value2));
            params2.add(new BasicNameValuePair("startdate", startdate_value2));
            params2.add(new BasicNameValuePair("shift", shift_value2));
            JSONObject json2 = null;
            try {
                json2 = jsonParser.makeHttpRequest(UPDATING_URL, "POST", params2);
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

                Toast.makeText(SaveTimingsActivity.this, file_url, Toast.LENGTH_LONG).show();

            }


        }
    }


    private void onAddUserButtonClick(){
        button4 = (Button) findViewById(R.id.adduserbutton);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(SaveTimingsActivity.this, AddUserActivity.class);
                startActivity(i);
            }
        });
    }


}
