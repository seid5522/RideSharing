package com.ridesharing.ui.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;

import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.ridesharing.Entity.Result;
import com.ridesharing.Entity.ResultType;
import com.ridesharing.Entity.User;
import com.ridesharing.R;
import com.ridesharing.Service.AuthenticationService;
import com.ridesharing.Service.DriverService;
import com.ridesharing.Service.UserService;
import com.ridesharing.ui.main.MainActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;




/**
 * A login screen that offers login via email/password and via Google+ sign in.
 * <p/>
 * ************ IMPORTANT SETUP NOTES: ************
 * In order for Google+ sign in to work with your app, you must first go to:
 * https://developers.google.com/+/mobile/android/getting-started#step_1_enable_the_google_api
 * and follow the steps in "Step 1" to create an OAuth 2.0 client for your package.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends PlusBaseActivity implements LoaderCallbacks<Cursor>,OnClickListener{
    private static final int AUTH_CODE_REQUEST_CODE = 2000;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    @ViewById(R.id.email)
    AutoCompleteTextView mEmailView;
    @ViewById(R.id.password)
    EditText mPasswordView;
    @ViewById(R.id.login_progress)
    View mProgressView;
    @ViewById(R.id.email_login_form)
    View mEmailLoginFormView;
    @ViewById(R.id.plus_sign_in_button)
    SignInButton mPlusSignInButton;
    @ViewById(R.id.plus_sign_out_buttons)
    View mSignOutButtons;
    @ViewById(R.id.login_form)
    View mLoginFormView;
    @ViewById(R.id.link_to_register)
    TextView registerLink;
    @ViewById(R.id.link_need_password)
    LinearLayout linkPassword;
    @ViewById(R.id.link_message)
    TextView linkMessage;
    @ViewById(R.id.link_password)
    TextView linkPasswordTextView;

    @Inject
    AuthenticationService authService;
    @Inject
    UserService userService;
    @Inject
    DriverService driverService;

    private boolean logout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setting default screen to login.xml

        setContentView(R.layout.activity_login);


        // Listening to register new account link
        Intent intent = getIntent();
        logout = intent.getBooleanExtra("logout", false);
    }

    @Override
    public void onClick(View view) {
        //Log.i("clicks","You Clicked B1");
        // Switching to Register screen
        Intent i = new Intent(this, com.ridesharing.ui.user.registerActivity_.class);
        startActivity(i);

    }

    @AfterViews
    void initView(){
        if (supportsGooglePlayServices()) {
            // Set a listener to connect the user when the G+ button is clicked.
            mPlusSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    signIn();
                }
            });
        } else {
            // Don't offer G+ sign in if the app's version is too low to support Google Play
            // Services.
            mPlusSignInButton.setVisibility(View.GONE);
            return;
        }
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        registerLink.setOnClickListener(this);
    }

    private void populateAutoComplete() {
        if (VERSION.SDK_INT >= 14) {
            // Use ContactsContract.Profile (API 14+)
            getLoaderManager().initLoader(0, null, this);
        } else if (VERSION.SDK_INT >= 8) {
            // Use AccountManager (API 8+)
            new SetupEmailAutoCompleteTask().execute(null, null);
        }
    }

    @UiThread
    protected void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
       //hide soft keyboard
        hideKeyboard();

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }
    private boolean isEmailValid(String email) {
        return (email != "");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    protected void onPlusClientSignIn() {
        //Set up sign out and disconnect buttons.
        Button signOutButton = (Button) findViewById(R.id.plus_sign_out_button);
        signOutButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
        Button disconnectButton = (Button) findViewById(R.id.plus_disconnect_button);
        disconnectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                revokeAccess();
            }
        });
    }

    @Background
    protected void getPlusAuthorizationCode(){
        String scopes = "oauth2:server:client_id:828246609517-ak6l3jd35ikp77pk7tltm5f9noks6cgd.apps.googleusercontent.com:api_scope:"+ Scopes.PLUS_LOGIN;
        String code = null;
        try {
            code = GoogleAuthUtil.getToken(
                    this,                                              // Context context
                    Plus.AccountApi.getAccountName(getGoogleApiClient()),  // String accountName
                    scopes                                            // String scope
            );
            Log.v("Offline token",code);

        } catch (IOException transientEx) {
            // network or server error, the call is expected to succeed if you try again later.
            // Don't attempt to call again immediately - the request is likely to
            // fail, you'll hit quotas or back-off.
            return;
        } catch (UserRecoverableAuthException e) {
            // Requesting an authorization code will always throw
            // UserRecoverableAuthException on the first call to GoogleAuthUtil.getToken
            // because the user must consent to offline access to their data.  After
            // consent is granted control is returned to your activity in onActivityResult
            // and the second call to GoogleAuthUtil.getToken will succeed.
            startActivityForResult(e.getIntent(), AUTH_CODE_REQUEST_CODE);
            return;
        } catch (GoogleAuthException authEx) {
            // Failure. The call is not expected to ever succeed so it should not be
            // retried.
            return;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    protected void onPlusClientBlockingUI(boolean show) {
        showProgress(show);
    }

    @Override
    protected void updateConnectButtonState() {
        boolean connected = getGoogleApiClient().isConnected();

        mSignOutButtons.setVisibility(connected ? View.VISIBLE : View.GONE);
        mPlusSignInButton.setVisibility(connected ? View.GONE : View.VISIBLE);
        mEmailLoginFormView.setVisibility(connected ? View.GONE : View.VISIBLE);

        if(getGoogleApiClient().isConnected()){
            if(logout){
                signOut();
                logout = false;
                return;
            }
            String email = Plus.AccountApi.getAccountName(getGoogleApiClient());
            checkRegistration(email);
            //getPlusAuthorizationCode();
        }
    }

    @Background
    public void checkRegistration(String email){
        if(userService.isRegister(email)){
            Person person = Plus.PeopleApi.getCurrentPerson(getGoogleApiClient());
            User user = new User();
            user.setUsername(email);
            user.setSessionKey(person.getId());
            Result result = authService.SocialLogin(user);
            if(result.getType() == ResultType.Success){
                boolean isDriver = driverService.isDirver();
                userService.setDriver(isDriver);
                if(isDriver){
                    userService.setUser(driverService.fetchSelfInfo());
                }else{
                    userService.setUser(userService.fetchSelfInfo());
                }
                goToMainActivity();
            }else{
                showLinkPassword();
            }
        }else{
            Person person = Plus.PeopleApi.getCurrentPerson(getGoogleApiClient());
            String uname = email;
            Date birthday = null;
            String strBirthday = "1981-01-01";
            if(person.hasBirthday() && !person.getBirthday().equals("")){
                strBirthday = person.getBirthday();
            }
            try {
                birthday = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(strBirthday);
            } catch (ParseException e) {
                Log.e("com.ridesharing error: ", e.getMessage());
            }
            String firstname = person.getName().getGivenName();
            String lastname = person.getName().getFamilyName();
            String address = "";
            String address2 = "";
            String city = "";
            String state = "";
            String zipcode = "";
            String phone ="";
            String ImageURL = "";
            String SessionKey = person.getId();
            if(person.hasPlacesLived()){
                Person.PlacesLived lived = person.getPlacesLived().get(0);
                city = lived.getValue();
            }
            if(person.hasCurrentLocation()){
                address = person.getCurrentLocation();
            }

            User user = new User(uname, email, firstname, lastname, birthday, address, address2, city, state, zipcode, phone, ImageURL, SessionKey, "");
            Result result = userService.Register(user);
            if(result.getType() == ResultType.Success){
                Result resultSocial = authService.SocialLogin(user);
                if(resultSocial.getType() == ResultType.Success) {
                    boolean isDriver = false;
                    userService.setDriver(isDriver);
                    userService.setUser(userService.fetchSelfInfo());
                    goToMainActivity();
                }else{
                    showError(this, resultSocial.getMessage());
                }
            }else{
                showError(this, result.getMessage());
            }
        }
    }

    @UiThread
    public void showError(Context context, String errorMessage){
        new AlertDialog.Builder(context)
                .setTitle("Error")
                .setMessage(errorMessage)
                .setNegativeButton("OK", null)
                .show();
    }

    @Click(R.id.buttonLink)
    void linkPasswordBtnClicked() {
        String email = Plus.AccountApi.getAccountName(getGoogleApiClient());
        Person person = Plus.PeopleApi.getCurrentPerson(getGoogleApiClient());
        String sessionId = person.getId();
        String password =  linkPasswordTextView.getText().toString();

        User user = new User();
        user.setUsername(email);
        user.setPassword(password);
        user.setSessionKey(sessionId);
        linkAccount(user);

    }

    @Background
    public void linkAccount(User user){
        Result result = authService.Link(user);
        if(result.getType() == ResultType.Success){
            userService.setUser(user);
            goToMainActivity();
        }else{
            showLinkPasswordError(result);
        }
    }

    @UiThread
    public void showLinkPasswordError(Result result){
        linkPasswordTextView.setError(result.getMessage());
    }

    @UiThread
    public void showLinkPassword(){
        linkMessage.setVisibility(View.VISIBLE);
        linkPassword.setVisibility(View.VISIBLE);
    }

    @UiThread
    public void goToMainActivity(){
        Intent main = new Intent(getApplicationContext(), MainActivity_.class);
        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(main);
        finish();
    }

    @Override
    protected void onPlusClientRevokeAccess() {
        // any stored user data here.
        String email = Plus.AccountApi.getAccountName(getGoogleApiClient());
        Person currentPerson = Plus.PeopleApi.getCurrentPerson(getGoogleApiClient());
        String personName = currentPerson.getDisplayName();
        Person.Image personPhoto = currentPerson.getImage();
        String birthday = currentPerson.getBirthday();
    }

    @Override
    protected void onPlusClientSignOut() {

    }

    /**
     * Check if the device supports Google Play Services.  It's best
     * practice to check first rather than handling this as an error case.
     *
     * @return whether the device supports Google Play Services
     */
    private boolean supportsGooglePlayServices() {
        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) ==
                ConnectionResult.SUCCESS;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                                                                     .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }



    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Use an AsyncTask to fetch the user's email addresses on a background thread, and update
     * the email text field with results on the main UI thread.
     */
    class SetupEmailAutoCompleteTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... voids) {
            ArrayList<String> emailAddressCollection = new ArrayList<String>();

            // Get all emails from the user's contacts and copy them to a list.
            ContentResolver cr = getContentResolver();
            Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                    null, null, null);
            while (emailCur.moveToNext()) {
                String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract
                        .CommonDataKinds.Email.DATA));
                emailAddressCollection.add(email);
            }
            emailCur.close();

            return emailAddressCollection;
        }

	    @Override
	    protected void onPostExecute(List<String> emailAddressCollection) {
	       addEmailsToAutoComplete(emailAddressCollection);
	    }
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            User user = new User(mEmail, mPassword);
            Result result = authService.Login(user);
            if(result.getType() == ResultType.Success) {
                boolean isDriver = driverService.isDirver();
                userService.setDriver(isDriver);
                if(isDriver){
                    userService.setUser(driverService.fetchSelfInfo());
                }else{
                    userService.setUser(userService.fetchSelfInfo());
                }

            }
            return (result.getType() == ResultType.Success);

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                goToMainActivity();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }




}



