package com.pearlcoaching.pearlcoaching;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;

public class ServiceDescription extends AppCompatActivity {

    ImageView service_img;
    AppCompatTextView service_header,service_description;
    AppCompatButton bookBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_description);

        Bundle details = getIntent().getExtras().getBundle("service_details");
        service_img = findViewById(R.id.service_img);
        service_header = findViewById(R.id.description_header);
        service_description = findViewById(R.id.description_text);
        bookBtn = findViewById(R.id.book_btn);

        int img = (int) details.get("service_image");


        switch (img){
            case R.drawable.personal_coaching:
                service_img.setImageResource(R.drawable.personal_coaching);
                service_header.setText(R.string.personal_coaching_header);
                service_description.setText(R.string.personal_couching_drscription);
                break;

            case R.drawable.career_coaching:
                service_img.setImageResource(R.drawable.career_coaching);
                service_header.setText(R.string.career_coaching_header);
                service_description.setText(R.string.career_coaching_description);
                break;

            case R.drawable.corporate_coaching:
                service_img.setImageResource(R.drawable.corporate_coaching);
                service_header.setText(R.string.corporate_coaching_header);
                service_description.setText(R.string.corporate_coaching_description);
                break;

            case R.drawable.student_coaching:
                service_img.setImageResource(R.drawable.student_coaching);
                service_header.setText(R.string.student_coaching_header);
                service_description.setText(R.string.student_coaching_description);
                break;
        }

        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ServiceDescription.this,FeedbackScreen.class));
            }
        });

    }
}
