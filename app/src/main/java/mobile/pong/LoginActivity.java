package mobile.pong;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import mobile.pong.data.Player;


public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private TextView tvPlayerTeam;
    private FirebaseAuth firebaseAuth;

    private int usersLoggedIn;
    private String[] usernames = new String[4];

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        username = (EditText) findViewById(R.id.etUsername);
        password = (EditText) findViewById(R.id.etPassword);
        tvPlayerTeam = (TextView) findViewById(R.id.tvPlayerTeam);


        pd = new ProgressDialog(LoginActivity.this);
        usersLoggedIn = 0;

        Button btnCreate = (Button) findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerClick();
            }
        });

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginClick();
            }
        });
    }


    protected void onDestroy() {
        firebaseAuth.signOut();
        hideProgressDialog();
        super.onDestroy();
    }

    private String addExtension(String name) {
        if (!name.contains("@")) {
            String email = name + getString(R.string.email_ext);
            return email;
        }
        return name;
    }

    private void showProgressDialog() {
        pd.setMessage(getString(R.string.pd_loading));
        pd.show();
    }

    public void hideProgressDialog() {
        if (pd != null && pd.isShowing()) {
            pd.hide();
        }
    }


    public void registerClick() {
        if (!isFormValid()) {
            return;
        }

        showProgressDialog();

        firebaseAuth.createUserWithEmailAndPassword(
                addExtension(username.getText().toString()), password.getText().toString()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgressDialog();

                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    firebaseUser.updateProfile(
                            new UserProfileChangeRequest.Builder().
                                    setDisplayName(
                                            userNameFromEmail(
                                                    firebaseUser.getEmail())).build()
                    );

                    Toast.makeText(LoginActivity.this, R.string.toast_user_created,
                            Toast.LENGTH_SHORT).show();

                    createPlayer(userNameFromEmail(firebaseUser.getEmail()));
                    moveToNext(userNameFromEmail(firebaseUser.getEmail()));
                } else {
                    toastException(task.getException());
                }
            }
        });
    }

    private void toastException(Exception e) {
        if (e.getLocalizedMessage().equals(getString(R.string.internal_err_msg))) {
            Toast.makeText(LoginActivity.this, R.string.err_pw_length,
                    Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(LoginActivity.this,
                    e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void loginClick() {

        if (!isFormValid()) {
            return;
        }

        if (repeatUser(username.getText().toString())) {
            return;
        }

        showProgressDialog();

        firebaseAuth.signInWithEmailAndPassword(
                addExtension(username.getText().toString()),
                password.getText().toString()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgressDialog();

                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, R.string.toast_login_success, Toast.LENGTH_SHORT).show();
                    moveToNext(username.getText().toString());
                } else {
                    Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private boolean isFormValid() {
        if (TextUtils.isEmpty(username.getText().toString())) {
            username.setError(getString(R.string.err_empty_field));
            return false;
        }

        if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError(getString(R.string.err_empty_field));
            return false;
        }

        return true;
    }

    private boolean repeatUser(String name) {
        for (int i = 0; i < usersLoggedIn; i++) {
            if (usernames[i].equals(name)) {
                username.setError(getString(R.string.err_mult_login));
                return true;
            }
        }
        return false;
    }

    private void moveToNext(String name) {
        usernames[usersLoggedIn] = name;
        usersLoggedIn++;

        if (usersLoggedIn == 4) {
            returnValues();
        }

        username.setText("");
        password.setText("");

        if (usersLoggedIn == 1) {
            tvPlayerTeam.setText(R.string.enter_t1p2);
        } else if (usersLoggedIn == 2) {
            tvPlayerTeam.setTextColor(Color.BLUE);
            tvPlayerTeam.setText(R.string.enter_t2p1);
        } else {
            tvPlayerTeam.setText(R.string.enter_t2p2);
        }
    }


    private void returnValues() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(getString(R.string.team1player1), usernames[0]);
        resultIntent.putExtra(getString(R.string.team1player2), usernames[1]);
        resultIntent.putExtra(getString(R.string.team2player1), usernames[2]);
        resultIntent.putExtra(getString(R.string.team2player2), usernames[3]);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private String userNameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private void createPlayer(String name) {
        String key = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.fb_players)).push().getKey();
        Player newPlayer = new Player(name, key);

        FirebaseDatabase.getInstance().getReference().
                child(getString(R.string.fb_players)).child(key).setValue(newPlayer);
    }

}
