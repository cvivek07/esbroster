package com.hfad.esbroster;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShowTimingsActivity extends Activity {
    private String[] titles; // for drawer
    private ListView drawerList; // for drawer
    private DrawerLayout mdrawerLayout; // for drawer
    private ArrayAdapter<String> mArrayAdapter; // for drawer
    private ActionBarDrawerToggle mDrawerToggle; // for drawer
    private String mActivityTitle; // for drawer

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();

Boolean loginflag = false;
    private static final String URL = "http://www.android2.in/shift_timings_allotter/checktimings.php";


    private static final String KEY_NAME = "Name";
    private static final String KEY_STARTDATE = "StartDate";

    private static final String KEY_SHIFT = "Shift";
    private static final String TAG_SUCCESS = "success";

    private static final String TAG_MESSAGE = "message";


    ListView listView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item);

        listView = (ListView) findViewById(R.id.listView1);


        new showAsyncTask().execute();
    }


    class showAsyncTask extends AsyncTask<String, String, String> {
        int success;
        String string_Name;

        String string_StartDate;
        //String string_EndDate;
        String string_Shift;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ShowTimingsActivity.this);
            pDialog.setMessage("Checking the records...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject jsonObject = jsonParser.getJSONFromUrl(URL);
                success = jsonObject.getInt(TAG_SUCCESS);

                if (success == 0) {
                    return jsonObject.getString(TAG_MESSAGE);
                } else {
                    string_Name = jsonObject.getString(KEY_NAME);
                    string_StartDate = jsonObject.getString(KEY_STARTDATE);
                    // string_EndDate = jsonObject.getString(KEY_ENDDATE);
                    string_Shift = jsonObject.getString(KEY_SHIFT);
                    // Log.v("VALUE OF D : ", d);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();

            if (result !=null && result.equalsIgnoreCase("No Shift Timings Allotted")) {

                finish();
                Toast.makeText(ShowTimingsActivity.this, result, Toast.LENGTH_LONG).show();


            } else {
                ArrayList<String> listitems = new ArrayList<String>();
                listitems.add(string_Name);
                listitems.add(string_StartDate);
                // listitems.add(string_EndDate);
                listitems.add(string_Shift);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ShowTimingsActivity.this, R.layout.row, R.id.rowTextView, listitems);
                listView.setAdapter(arrayAdapter);
            }
        }
    }


}