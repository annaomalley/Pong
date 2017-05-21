package mobile.pong;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import mobile.pong.model.GameModel;
import mobile.pong.view.GameView;

import static mobile.pong.R.id.etPassword;

/**
 * Created by student on 21/05/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private FirebaseAuth firebaseAuth;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        username = (EditText) findViewById(R.id.etUsername);
        password = (EditText) findViewById(etPassword);

        pd = new ProgressDialog(LoginActivity.this);

        Button btnCreate = (Button) findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerClick();
            }
        });

    }

    private String addExtension(String username){
        String email = username + "@gmail.com";
        return email;
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

    private String userNameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

}
