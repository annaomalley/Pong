package mobile.pong;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import mobile.pong.model.GameModel;
import mobile.pong.view.GameView;

/**
 * Created by student on 21/05/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private TextView tvPlayerTeam;
    private FirebaseAuth firebaseAuth;

    private int usersLoggedIn;
    private String[] users = new String[4];

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
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                loginClick();
            }
        });
    }


    protected void onDestroy(){
        firebaseAuth.signOut();
        super.onDestroy();
    }

    private String addExtension(String name) {
        if (!name.contains("@")) {
            String email = name + "@gmail.com";
            return email;
        }
        return name;
    }

    private void showProgressDialog(){
        pd.setMessage("loading");
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

                    Toast.makeText(LoginActivity.this, "REG OK",
                            Toast.LENGTH_SHORT).show();

                    createPlayer(userNameFromEmail(firebaseUser.getEmail()));
                    moveToNext(userNameFromEmail(firebaseUser.getEmail()));
                } else {
                    Toast.makeText(LoginActivity.this, "Failed: "+
                                    task.getException().getLocalizedMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgressDialog();
                Toast.makeText(LoginActivity.this,
                        "error: "+e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loginClick() {

        if (!isFormValid()) {
            return;
        }

        if (repeatUser(username.getText().toString())){
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
                    Toast.makeText(LoginActivity.this, "Login ok", Toast.LENGTH_SHORT).show();
                    moveToNext(username.getText().toString());
                } else {
                    Toast.makeText(LoginActivity.this, "Failed: "+task.getException().getLocalizedMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private boolean isFormValid() {
        if (TextUtils.isEmpty(username.getText().toString())) {
            username.setError("This should not be empty");
            return false;
        }

        if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError("This should not be empty");
            return false;
        }

        return true;
    }

    private boolean repeatUser(String name){
        for (int i = 0; i < usersLoggedIn; i++){
            if (users[i].equals(name)){
                username.setError("User can only be logged in once per game!!!");
                return true;
            }
        }
        return false;
    }

    private void moveToNext(String name){
//        createPlayer(name);

        users[usersLoggedIn] = name;
        usersLoggedIn ++;

        if (usersLoggedIn == 4){
            returnValues();
        }

        username.setText("");
        password.setText("");

        if (usersLoggedIn == 1){
            tvPlayerTeam.setText("Team One Player Two");
        }
        else if (usersLoggedIn == 2){
            tvPlayerTeam.setTextColor(Color.BLUE);
            tvPlayerTeam.setText("Team Two Player One");
        }
        else{
            tvPlayerTeam.setText("Team Two Player Two");
        }
    }


    private void returnValues() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("team1player1", users[0]);
        resultIntent.putExtra("team1player2", users[1]);
        resultIntent.putExtra("team2player1", users[2]);
        resultIntent.putExtra("team2player2", users[3]);
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

    private void createPlayer(String name){
        String key = FirebaseDatabase.getInstance().getReference()
                .child("players").push().getKey();
        Player newPlayer = new Player(name);

        FirebaseDatabase.getInstance().getReference().
                child("players").child(key).setValue(newPlayer);
    }

}
