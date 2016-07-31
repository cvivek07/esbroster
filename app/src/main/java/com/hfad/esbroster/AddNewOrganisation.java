package com.hfad.esbroster;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.*;
import org.apache.http.message.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vivek on 04-May-16.
 */
public class AddNewOrganisation extends ActionBarActivity {

    ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    EditText editText;
    Button button;

    private static final String ADDNEWTEAM_URL = "http://www.android2.in/shift_timings_allotter/addnewteam.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  getWindow().setEnterTransition(animFadein);
        // overridePendingTransition(R.anim.slide_right, R.anim.slide_right);
        setContentView(R.layout.addneworganisation);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        editText = (EditText) findViewById(R.id.edittext_id_addneworg);
        button = (Button) findViewById(R.id.addneworgbutton_id);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddNewOrgAsyncTask().execute();
            }
        });
    }


    class AddNewOrgAsyncTask extends AsyncTask<String, String, String> {
        // String c = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddNewOrganisation.this);
            pDialog.setMessage("Adding your organisation...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            //Toast.makeText(CheckTimingsActivity.this, "onPreExecute completed", Toast.LENGTH_LONG).show();


        }

        @Override
        protected String doInBackground(String... params) {


            String orgname_value = editText.getText().toString();


            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("name", orgname_value));


            // parameters.add(new BasicNameValuePair("enddate", enddate_value1));
            try {
                JSONObject jsonObject = jsonParser.makeHttpRequest(ADDNEWTEAM_URL, "POST", parameters);

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

            //Toast.makeText(CheckTimingsActivity.this, "POST EXECUTE DONE!!", Toast.LENGTH_LONG).show();
            Intent showtimings_intent = new Intent(AddNewOrganisation.this, CheckTimingsActivity.class);
            startActivity(showtimings_intent);

        }
    }
}
