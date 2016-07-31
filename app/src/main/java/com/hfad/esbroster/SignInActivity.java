package com.hfad.esbroster;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;


public class SignInActivity extends ActionBarActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {


   // TextView tv_username, tv_Email;
   // ImageView imageView;


   // Button signOutbutton;
    SignInButton signInbutton;
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signinpage);
        //Google ads Start
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3571727848659159~2627701427");
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        //Google ads End

        //Register both button and add click listener
        signInbutton = (SignInButton) findViewById(R.id.sign_in_button);
        //signOutbutton = (Button) findViewById(R.id.btn_logout);
        signInbutton.setOnClickListener(this);
        //signOutbutton.setOnClickListener(this);

// Google Sign In Start
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)

                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // Google Sign In End

    }
    protected void onStart(){
        super.onStart();
       // signOutbutton.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.sign_in_button:

                signIn();

                break;
           // case R.id.btn_logout:

            //    signOut();

             //   break;
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Toast.makeText(SignInActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
    }

    private void signIn() {
        mGoogleApiClient.connect();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

       // signOutbutton.setVisibility(View.VISIBLE);
        //signInbutton.setVisibility(View.GONE);


    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                       // tv_username.setText("");
                       // tv_Email.setText("");

                    }
                });

        //signOutbutton.setVisibility(View.GONE);
        signInbutton.setVisibility(View.VISIBLE);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            /*Intent i = new Intent(SignInActivity.this, ThirdActivity.class);
            startActivity(i);*/
            String personID= acct.getId();

            Log.e("USER ID", personID);
          //  tv_username.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
           // tv_Email.setText(acct.getEmail());
            Intent i = new Intent(SignInActivity.this, DrawerActivity.class);
            i.putExtra("username", acct.getDisplayName());
            startActivity(i);






        } else {
            // Signed out, show unauthenticated UI.
            // updateUI(false);
        }
    }





}

