package com.debjanimandal.quizgame;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login_Page extends AppCompatActivity {

    EditText email,password;
    Button sigin;
    SignInButton google_signin;
    TextView forgot,signup;
    ProgressBar process;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    GoogleSignInClient googleSignInClient;
    ActivityResultLauncher<Intent> activityResultLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        //register
        registerGoogleSignIn();

        email=findViewById(R.id.editTextTextEmailAddress);
        password=findViewById(R.id.editTextTextPassword);
        sigin=findViewById(R.id.buttonSignin);
        google_signin=findViewById(R.id.signInButton);
        signup=findViewById(R.id.textViewSignUp);
        forgot=findViewById(R.id.textViewForgot);
        process=findViewById(R.id.progressBar2);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login_Page.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });
        sigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_u=email.getText().toString();
                String password_u=password.getText().toString();
                SignInWithFirebase(email_u,password_u);
            }
        });
        google_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signinGoogle();
                }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login_Page.this, ForgotPassword.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void SignInWithFirebase(String e, String p)
    {
        process.setVisibility(View.VISIBLE);
        sigin.setClickable(false);
        firebaseAuth.signInWithEmailAndPassword(e,p)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Login_Page.this, "SignIn is successful", Toast.LENGTH_SHORT).show();
                            process.setVisibility(View.INVISIBLE);
                            Intent intent=new Intent(Login_Page.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(Login_Page.this, "There is a problem, please try again later. ", Toast.LENGTH_SHORT).show();
                            process.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    public void signinGoogle()
    {
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("210957321796-nlngpnf6q1fpborcma1dqfm1rjfrnl42.apps.googleusercontent.com")
                .requestEmail().build();
        googleSignInClient= GoogleSignIn.getClient(this,gso);
        sigining_in();

    }
    public void sigining_in()
    {
        Intent signinintent=googleSignInClient.getSignInIntent();
        activityResultLauncher.launch(signinintent);
    }
    public void registerGoogleSignIn()
    {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                int resultcode=result.getResultCode();
                Intent data=result.getData();
                if(resultcode==RESULT_OK && data!=null)
                {
                    Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
                    firebaseSignInWithGoogle(task);
                }
            }
        });
    }
    private void firebaseSignInWithGoogle(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount acc= task.getResult(ApiException.class);
            Toast.makeText(this,"Successfully",Toast.LENGTH_SHORT).show();
            Intent i=new Intent(Login_Page.this, MainActivity.class);
            startActivity(i);
            finish();
            firebaseGoogleAccount(acc);

        }catch (ApiException e){
            e.printStackTrace();
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    private void firebaseGoogleAccount(GoogleSignInAccount acc)
    {
        AuthCredential authCredential= GoogleAuthProvider.getCredential(acc.getIdToken(),null);
        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {

                        }
                        else {

                        }
                    }
                });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null)
        {
            Intent intent=new Intent(Login_Page.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}