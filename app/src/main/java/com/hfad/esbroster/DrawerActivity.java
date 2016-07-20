package com.hfad.esbroster;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



/**
 * Created by Vivek on 31-Mar-16.
 */
public class DrawerActivity extends ActionBarActivity {

    Boolean loginflag=false;
    final Context context = this;
    private Button button;
    private EditText result;
    private TextView tv_username;
    private String[] titles; // for drawer
    private ListView drawerList; // for drawer
    private DrawerLayout mdrawerLayout; // for drawer
    private ArrayAdapter<String> mArrayAdapter; // for drawer
    private ActionBarDrawerToggle mDrawerToggle; // for drawer
    private String mActivityTitle; // for drawer
    Button checktimings_button, checktimingsbydate_button, managetimings_button, addnewuser_button, deleteuser_button;

    Animation animFadein;// for animation

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draweractivity_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for drawer
        getSupportActionBar().setHomeButtonEnabled(true); // for drawer
        titles = getResources().getStringArray(R.array.titles); // for drawer
        mdrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout); // for drawer
        drawerList = (ListView) findViewById(R.id.drawer); // for drawer
        mActivityTitle = getTitle().toString(); // for drawer

        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_right);
        tv_username = (TextView) findViewById(R.id.tv_Username);
Intent intent = getIntent();
        String username_string= intent.getStringExtra("username");
        tv_username.setText(getString(R.string.signed_in_fmt, username_string));


        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, titles); // for drawer
        drawerList.setAdapter(mArrayAdapter);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        setupDrawer();// for drawer
        onAllButtonClick(); // for button clicks on the main screen

    }


    private void onAllButtonClick() {
        checktimings_button = (Button) findViewById(R.id.checktimingsbutton_id);
        checktimingsbydate_button = (Button) findViewById(R.id.checktimingsbydatebutton_id);
        managetimings_button = (Button) findViewById(R.id.managetimings__id);
        addnewuser_button = (Button) findViewById(R.id.addnewuser_id);
        deleteuser_button = (Button) findViewById(R.id.deleteuserbutton_id);

        checktimings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(DrawerActivity.this, CheckTimingsActivity.class);
                startActivity(i);
                //checktimings_button.startAnimation(animFadein);
            }
        });
        checktimingsbydate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DrawerActivity.this, CheckTimingsByDateActivity.class);
                startActivity(i);
            }
        });
        managetimings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

enterPasswordDialog();
           /*Intent i = new Intent(DrawerActivity.this, SaveTimingsActivity.class);
                startActivity(i);*/

            }
        });
        addnewuser_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DrawerActivity.this, AddUserActivity.class);
                startActivity(i);
            }
        });
       deleteuser_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DrawerActivity.this, DeleteUserActivity.class);
                startActivity(i);
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
                Toast.makeText(DrawerActivity.this,
                        "Login Successful", Toast.LENGTH_SHORT).show();
                loginflag=true;
                Intent i = new Intent(DrawerActivity.this, SaveTimingsActivity.class);
                startActivity(i);
                // Redirect to dashboard / home screen.
                login.dismiss();

            }
            else   if(!username_string.equals("123") && !password_string.equals("123")){
                Toast.makeText(DrawerActivity.this,
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
                i = new Intent(DrawerActivity.this, CheckTimingsActivity.class);
                startActivity(i);
                break;
            case 1:
                i = new Intent(DrawerActivity.this, CheckTimingsByDateActivity.class);
                startActivity(i);
                break;
            case 2:
                enterPasswordDialog();
               /* i = new Intent(DrawerActivity.this, SaveTimingsActivity.class);
                startActivity(i);*/
                break;
            case 3:
                i = new Intent(DrawerActivity.this, AddUserActivity.class);
                startActivity(i);
                break;
            case 4:
                i = new Intent(DrawerActivity.this, DeleteUserActivity.class);
                startActivity(i);
                break;
            default:
                //Toast.makeText(DrawerActivity.this, "Default page", Toast.LENGTH_SHORT).show();
                i=new Intent(DrawerActivity.this, DrawerActivity.class);
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
}