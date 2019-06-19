package de.innocow.innocow_v001.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.innocow.innocow_v001.R;
import de.innocow.innocow_v001.pojo.login.LoginResponse;
import de.innocow.innocow_v001.utilities.retrofit.RestClient;
import de.innocow.innocow_v001.utilities.retrofit.RetrofitBuilder;
import de.innocow.innocow_v001.utilities.string_constants.StringConstants;
import retrofit2.Call;
import retrofit2.Response;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class Login extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private String username, password;
    private SharedPreferences preferences;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CheckBox rememberCredentials;

    public Login() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = findViewById(R.id.email);

        populateAutoComplete();

        mPasswordView = findViewById(R.id.password);

        rememberCredentials = findViewById(R.id.checkBox);

        writeCredentialsFromMemory();

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                username = mEmailView.getText().toString();
                password = mPasswordView.getText().toString();
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


    }

    private void writeCredentialsFromMemory() {
        preferences = getApplicationContext()
                .getSharedPreferences(StringConstants.APP_PREFERENCE, MODE_PRIVATE);
        if (StringUtils.isNotBlank(preferences.getString(StringConstants.USERNAME, null))
                && StringUtils.isNotBlank(preferences.getString(StringConstants.PASSWORD, null))) {
            mEmailView.setText(preferences.getString(StringConstants.USERNAME, null));
            mPasswordView.setText(preferences.getString(StringConstants.PASSWORD, null));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (rememberCredentials.isChecked()) {
            writeCredentialsFromMemory();
        }
    }

//    private void hideKeeyboard(){
//        InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(mEmailView.getWindowToken(), 0);
//        imm.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);
//    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     * <p>
     * Form errors e.g. password or username is too short
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(username)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(username)) {
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

            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String username) {
        //TODO: Add other things we might want to check here
        return username.length() > 3;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Add other things we might want to check here
        return password.length() > 3;

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
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
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private boolean isGoogleMapServicesOk() {

        int mapStatus = GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(Login.this);

        if (mapStatus == ConnectionResult.SUCCESS) return Boolean.TRUE;
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(mapStatus)) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(Login.this,
                    mapStatus, 9001);
            dialog.show();
        } else return Boolean.FALSE;

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACTS:
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    populateAutoComplete();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private String mEmail, mPassword;

        UserLoginTask(String username, String password) {
            this.mEmail = username;
            this.mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: Attempt authentication.

            RestClient restClient = RetrofitBuilder.getRestClient();
            String header = "application/json";

            Map<String, String> credentials = new HashMap<>();
            credentials.put("username", mEmail);
            credentials.put("password", mPassword);

            Call<LoginResponse> call = restClient.userLogin(header, credentials);
            Log.d("JSON", "cred " + credentials);

            //Always surround with try/catch block when dealing with network responses
            try {
                Response<LoginResponse> response = call.execute();
                if (response.isSuccessful()) {
                    Log.d("Login response: ", response.message());
                    assert response.body() != null;
                    String token = response.body().getValue().getToken();
                    long tokenExpTime = response.body().getValue().getExpirationTime();
                    long farmId = response.body().getValue().getFarmID();

                    preferences = getApplicationContext()
                            .getSharedPreferences(StringConstants.APP_PREFERENCE, MODE_PRIVATE);
                    SharedPreferences.Editor preferenceEditor = preferences.edit();
                    if (rememberCredentials.isChecked()) {
                        preferenceEditor.putString(StringConstants.USERNAME, mEmail);
                        preferenceEditor.putString(StringConstants.PASSWORD, password);
                    }
                    preferenceEditor.putString(StringConstants.TOKEN, token);
                    preferenceEditor.putLong(StringConstants.FARM_ID, farmId);
                    preferenceEditor.putLong(StringConstants.TOKEN_EXP_TIME, tokenExpTime);
                    preferenceEditor.apply();
                    return true;
                } else {
                    Log.d("Failed login response: ", response.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }


        // TODO: register the new account here.

        /**
         * Once the previous method, doInBackground returns, we test the result here.
         * If it returned true, it means that everything worked well and we got the response 200 OK from the server.
         * If false, it means something went wrong and we got the response code 400.
         */
        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                if (isGoogleMapServicesOk()) {

                    startActivityForResult(new Intent(Login.this, FarmMap.class)
                            , StringConstants.LOGOUT_REFERENCE);
                } else {
                    Toast.makeText(getApplicationContext()
                            , "No Google Map Support"
                            , Toast.LENGTH_LONG).show();
                    Log.e("Google map availability", Boolean.FALSE.toString());
                    startActivity(new Intent(Login.this, Dashboard.class));
                }

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
