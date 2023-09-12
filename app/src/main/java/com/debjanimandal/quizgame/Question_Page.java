package com.debjanimandal.quizgame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Question_Page extends AppCompatActivity {

    TextView time,correct,wrong;
    TextView ques,a,b,c,d;
    Button next,finish;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference reference=database.getReference("Questions");
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    DatabaseReference databaseReference=database.getReference();
    String quiz_ques;
    String quiz_a;
    String quiz_b;
    String quiz_c;
    String quiz_d;
    String quiz_ans;
    int ques_count;
    int ques_number=1;
    String user_ans;
    int user_correct_Count=0, user_wrong_count=0;
    CountDownTimer countDownTimer;
    private static final long Total_time=25000;
    Boolean timerCont;
    long leftTime=Total_time;
    int disablecounter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_page);
        time=findViewById(R.id.textViewtime);
        correct=findViewById(R.id.textViewcorrect);
        wrong=findViewById(R.id.textViewwrong);
        ques=findViewById(R.id.textViewQues);
        a=findViewById(R.id.textViewOption1);
        b=findViewById(R.id.textViewOption2);
        c=findViewById(R.id.textViewOption3);
        d=findViewById(R.id.textViewOption4);
        next=findViewById(R.id.buttonNext);
        finish=findViewById(R.id.buttonfinish);
        game();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disablecounter=0;
                resttimer();
                game();
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ques_number<=ques_count)
                {
                    AlertDialog.Builder alertDailog=new AlertDialog.Builder(Question_Page.this);
                    alertDailog.setTitle("Exit").setMessage("Do you want to exit the quiz").setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            sendScore();
                            Intent intent=new Intent(Question_Page.this,Score_Page.class);
                            startActivity(intent);
                            finish();
                        }
                    }).show();
                }
                else {
                    sendScore();
                    Intent intent=new Intent(Question_Page.this,Score_Page.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(disablecounter==0) {
                    pauseTimer();
                    user_ans = "a";
                    if (quiz_ans.equals(user_ans)) {
                        a.setBackgroundColor(Color.GREEN);
                        user_correct_Count++;
                        correct.setText("" + user_correct_Count);
                    } else {
                        a.setBackgroundColor(Color.RED);
                        user_wrong_count++;
                        wrong.setText("" + user_wrong_count);
                        find_answer();
                    }
                }
                disablecounter=1;
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(disablecounter==0) {
                    pauseTimer();
                    user_ans = "b";
                    if (quiz_ans.equals(user_ans)) {
                        b.setBackgroundColor(Color.GREEN);
                        user_correct_Count++;
                        correct.setText("" + user_correct_Count);
                    } else {
                        b.setBackgroundColor(Color.RED);
                        user_wrong_count++;
                        wrong.setText("" + user_wrong_count);
                        find_answer();
                    }
                }
                disablecounter=1;
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(disablecounter==0) {
                    pauseTimer();
                    user_ans = "c";
                    if (quiz_ans.equals(user_ans)) {
                        c.setBackgroundColor(Color.GREEN);
                        user_correct_Count++;
                        correct.setText("" + user_correct_Count);
                    } else {
                        c.setBackgroundColor(Color.RED);
                        user_wrong_count++;
                        wrong.setText("" + user_wrong_count);
                        find_answer();
                    }
                }
                disablecounter=1;
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(disablecounter==0) {
                    pauseTimer();
                    user_ans = "d";
                    if (quiz_ans.equals(user_ans)) {
                        d.setBackgroundColor(Color.GREEN);
                        user_correct_Count++;
                        correct.setText("" + user_correct_Count);
                    } else {
                        d.setBackgroundColor(Color.RED);
                        user_wrong_count++;
                        wrong.setText("" + user_wrong_count);
                        find_answer();
                    }
                }
                disablecounter=1;
            }
        });
    }
    public void game(){
        startTimer();
        a.setBackgroundColor(Color.WHITE);
        b.setBackgroundColor(Color.WHITE);
        c.setBackgroundColor(Color.WHITE);
        d.setBackgroundColor(Color.WHITE);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ques_count=(int)dataSnapshot.getChildrenCount();
                quiz_ques=dataSnapshot.child(String.valueOf(ques_number)).child("q").getValue().toString();
                quiz_a=dataSnapshot.child(String.valueOf(ques_number)).child("a").getValue().toString();
                quiz_b=dataSnapshot.child(String.valueOf(ques_number)).child("b").getValue().toString();
                quiz_c=dataSnapshot.child(String.valueOf(ques_number)).child("c").getValue().toString();
                quiz_d=dataSnapshot.child(String.valueOf(ques_number)).child("d").getValue().toString();
                quiz_ans=dataSnapshot.child(String.valueOf(ques_number)).child("answer").getValue().toString();

                ques.setText(quiz_ques);
                a.setText(quiz_a);
                b.setText(quiz_b);
                c.setText(quiz_c);
                d.setText(quiz_d);

                if(ques_number<ques_count)
                {
                    ques_number++;
                } else if (ques_number==ques_count) {
                    ques_number++;
                    next.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(Question_Page.this, "You answered all questions.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(Question_Page.this, "There is an error.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void find_answer()
    {
        if(quiz_ans.equals("a"))
        {
            a.setBackgroundColor(Color.GREEN);
        } else if(quiz_ans.equals("b")) {
            b.setBackgroundColor(Color.GREEN);
        } else if(quiz_ans.equals("c")) {
            c.setBackgroundColor(Color.GREEN);
        } else if(quiz_ans.equals("d")) {
            d.setBackgroundColor(Color.GREEN);
        }
    }
    public void startTimer()
    {
        countDownTimer=new CountDownTimer(leftTime,1000) {
            @Override
            public void onTick(long l) {
                leftTime=l;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timerCont=false;
                pauseTimer();
                ques.setText("Sorry, Time is up!");
            }
        }.start();
        timerCont=true;
    }
    public  void resttimer()
    {
        leftTime=Total_time;
        updateCountDownText();
    }
    public void updateCountDownText()
    {
        int second =(int) (leftTime/1000)%60;
        time.setText(""+second);
    }
    public void pauseTimer()
    {
        countDownTimer.cancel();
        timerCont=false;
    }
    public void sendScore()
    {
        String usrID=user.getUid();
        databaseReference.child("scores").child(usrID).child("correct").setValue(""+user_correct_Count)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Question_Page.this,"Scores sent successfully",Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference.child("scores").child(usrID).child("wrong").setValue(""+user_wrong_count);
    }
}