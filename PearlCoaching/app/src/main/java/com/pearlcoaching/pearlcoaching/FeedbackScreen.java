package com.pearlcoaching.pearlcoaching;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.widget.ImageView;

public class FeedbackScreen extends AppCompatActivity {

     ImageView service_img;
     AppCompatEditText name,phone;
     customEditText expectation_response,timeline;
     AppCompatTextView parent_teacher_question,expectation_question,timeline_question,service_header;
     AppCompatButton book_now;
     Intent emailIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_screen);

        service_img = findViewById(R.id.service_img);
        name = findViewById(R.id.client_name);
        phone = findViewById(R.id.client_phone);
        expectation_response = findViewById(R.id.expectation_response);

        timeline = findViewById(R.id.coaching_timeline);
        parent_teacher_question = findViewById(R.id.parent_teacher_question);
        expectation_question = findViewById(R.id.expectation_question);
        timeline_question = findViewById(R.id.timeline_question);


        String client_name = name.getText().toString();
        String client_phone= phone.getText().toString();
        String parent_teacher_ques = parent_teacher_question.getText().toString();

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"Recipient"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject");

        if (client_name != null && client_name != "")
            if (client_phone != null && client_phone != "")
        emailIntent.putExtra(Intent.EXTRA_TEXT   ,
                getResources().getString(R.string.mail_body) +
        "\n \n" + "Name : " + client_name);


    }
}
