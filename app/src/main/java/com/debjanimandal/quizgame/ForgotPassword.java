package com.debjanimandal.quizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPassword extends AppCompatActivity {

    EditText mail;
    Button cont;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mail=findViewById(R.id.editTextTextEmailAddress3);
        cont=findViewById(R.id.buttonforgot);
        progressBar=findViewById(R.id.progressBar3);
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String m=mail.getText().toString();
                reset(m);
            }
        });
    }
    public void reset(String mail)
    {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.sendPasswordResetEmail(mail)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ForgotPassword.this, "We send you an email to reset your password. ", Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(ForgotPassword.this,Login_Page.class);
                            cont.setClickable(false);
                            progressBar.setVisibility(View.INVISIBLE);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(ForgotPassword.this, "Sorry, There is problem. Please try again later...", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}