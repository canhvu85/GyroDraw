package ch.epfl.sweng.SDP.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.AuthUI.IdpConfig;
import com.firebase.ui.auth.AuthUI.IdpConfig.GoogleBuilder;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.List;

import ch.epfl.sweng.SDP.BaseActivity;
import ch.epfl.sweng.SDP.MainActivity;
import ch.epfl.sweng.SDP.R;
import ch.epfl.sweng.SDP.firebase.Database;
import ch.epfl.sweng.SDP.home.HomeActivity;

/**
 * Class containing the methods used for the login.
 * This activity is launched but not actually displayed.
 */
public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";
    private static final String EMAIL = "email";
    private static final int REQUEST_CODE_SIGN_IN = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_login);


        createSignInIntent();

        Glide.with(this).load(R.drawable.waiting_animation_dots)
                .into((ImageView) findViewById(R.id.waitingAnimationDots));
        Glide.with(this).load(R.drawable.background_animation)
                .into((ImageView) findViewById(R.id.backgroundAnimation));
    }

    private void createSignInIntent() {
        final List<IdpConfig> providers = Collections.singletonList(
                new GoogleBuilder().build());
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.LoginTheme)
                        .setLogo(R.drawable.common_google_signin_btn_icon_dark) // custom logo here
                        .build(),
                REQUEST_CODE_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (response == null) {
                launchActivity(MainActivity.class);
                finish();
                return;
            }

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                handleSuccessfulSignIn(response);
            } else {
                // Sign in failed
                handleFailedSignIn(response.getError().getErrorCode());
                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }

    /**
     * Handles a successful sign in.
     *
     * @param response the response to process
     */
    private void handleSuccessfulSignIn(IdpResponse response) {
        assert response != null;

        final String email = response.getEmail();
        if (response.isNewUser()) {
            // New user
            Log.d(TAG, "New user");
            Intent intent = new Intent(this, AccountCreationActivity.class);
            intent.putExtra(EMAIL, email);
            startActivity(intent);
            finish();
        } else {
            Database.getReference("users").orderByChild(EMAIL).equalTo(email)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                // User already has an account on Firebase
                                Log.d(TAG, "User already has an account on Firebase");
                                cloneAccountFromFirebase(snapshot);
                                launchActivity(HomeActivity.class);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                            } else {
                                // User signed in but not did not create an account
                                Log.d(TAG, "User signed in but not did not create an account");
                                Intent intent = new Intent(getApplicationContext(),
                                        AccountCreationActivity.class);
                                intent.putExtra(EMAIL, email);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            throw databaseError.toException();
                        }
                    });
        }
    }

    /**
     * Handles a failed signIn.
     *
     * @param errorCode specifies the error that occurred
     */
    @VisibleForTesting
    public void handleFailedSignIn(int errorCode) {
        TextView errorMessage = findViewById(R.id.error_message);

        // No network
        if (errorCode == ErrorCodes.NO_NETWORK) {
            errorMessage.setText(getString(R.string.no_internet));
            errorMessage.setVisibility(View.VISIBLE);
            return;
        }

        // Unknown error
        errorMessage.setText(getString(R.string.unknown_error));
        errorMessage.setVisibility(View.VISIBLE);
    }
}