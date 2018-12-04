package com.pearlcoaching.pearlcoaching;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.pearlcoaching.pearlcoaching.ServicesModule.ServicesScreen;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {


    private SignInButton mSignInButton;

    public static String TAG;

    private FirebaseAuth mAuth;
    private GoogleSignInAccount account;

    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private LoginButton loginButton;

    private CallbackManager callbackManager;

    public static boolean isConnected(Context context) {
        ConnectivityManager
                cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        if (!isConnected(this)){
            new AlertDialog.Builder(this).setMessage("Please check your Internet connection and try logging in again.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create().show();
        }

        TAG = getPackageName();
        mAuth = FirebaseAuth.getInstance();
        FacebookLoginSetup();
        mSignInButton = findViewById(R.id.google_log_in);
        mSignInButton.setSize(SignInButton.SIZE_STANDARD);
        // Configure Google Sign In
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

       // Build a GoogleSignInClient with the options specified by gso.
         mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

         mSignInButton.setOnClickListener(this);

        // Initialize Facebook Login button
//        mCallbackManager = CallbackManager.Factory.create();
//        loginButton = findViewById(R.id.fb_log_in);



    }

    private void FacebookLoginSetup() {
        mAuth = FirebaseAuth.getInstance();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        Toast.makeText(LoginScreen.this,"isLoggedIn: "+isLoggedIn, Toast.LENGTH_LONG).show();

        if(!isLoggedIn) {
            callbackManager = CallbackManager.Factory.create();

            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            // App code
                            Toast.makeText(LoginScreen.this, "FacebookCallback: " + loginResult.getAccessToken().getToken(), Toast.LENGTH_LONG).show();
                            requestData(loginResult.getAccessToken());
                        }

                        @Override
                        public void onCancel() {
                            // App code
                            Toast.makeText(LoginScreen.this, "FacebookCallback: onCancel", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            // App code
                            Toast.makeText(LoginScreen.this, "FacebookException: " + exception.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Log.d(TAG,"uid : " + accessToken.getUserId());
            Toast.makeText(LoginScreen.this, "FacebookCallback: " + accessToken.toString(), Toast.LENGTH_LONG).show();
            requestData(accessToken);
        }
        //mCallbackManager = CallbackManager.Factory.create();
        //createUser(email, password);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.google_log_in:
                signIn();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.e(TAG, "Google sign in failed : ===D :" + e.getMessage());
                // ...
            }
        }


    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginScreen.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


    public void updateUI(FirebaseUser user){
        if (user != null) {

            Intent intent = new Intent(this,ServicesScreen.class);
            if (user.getEmail()!=null && !user.getEmail().isEmpty())
                intent.putExtra("email",user.getEmail());
            if (user.getDisplayName() != null && !user.getDisplayName().isEmpty())
                intent.putExtra("displayName",user.getDisplayName());
            if (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty())
                intent.putExtra("phoneNumber",user.getPhoneNumber());

            /*intent.addCategory(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
            startActivity(intent);
            finish();
        }
    }

    private void requestData(AccessToken accessToken) {

        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                final JSONObject json = response.getJSONObject();

                try {
                    if(json != null){

                        String email = json.getString("name");


                        Intent intent = new Intent(getApplicationContext(),ServicesScreen.class);
                        if (email != null && !email.isEmpty())
                            intent.putExtra("email",email);
                        startActivity(intent);
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
