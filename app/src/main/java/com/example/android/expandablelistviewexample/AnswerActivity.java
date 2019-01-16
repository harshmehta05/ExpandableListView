package com.example.android.expandablelistviewexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AnswerActivity extends AppCompatActivity {

    String disp;
    TextView ansTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        ansTextView = findViewById(R.id.ansTextView);

        Intent intent = getIntent();
        String [] ansArray = intent.getStringArrayExtra("ansArray");
        String [] questionArray = intent.getStringArrayExtra("questionArray");
        for (int i=0;i<ansArray.length;i++)
        {
            if (ansArray[i] != null)
            disp = disp + "\n\n" + questionArray[i] + "\n"+ansArray[i];
       }



        ansTextView.setText(disp);
    }
}
