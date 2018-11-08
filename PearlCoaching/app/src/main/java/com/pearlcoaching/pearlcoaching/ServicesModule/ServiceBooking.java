package com.pearlcoaching.pearlcoaching.ServicesModule;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pearlcoaching.pearlcoaching.R;
import com.pearlcoaching.pearlcoaching.baseclass.BaseFragment;
import com.pearlcoaching.pearlcoaching.customEditText;

public class ServiceBooking extends BaseFragment implements View.OnClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int mID;
    private String mParam2;

    private OnServiceInteraction mListener;
    ImageView service_img;
    AppCompatEditText name,phone;
    customEditText expectation_response,timeline;
    AppCompatTextView parent_teacher_question,expectation_question,timeline_question,service_header;
    AppCompatButton bookNow;
    Intent emailIntent;

    public ServiceBooking() {
        // Required empty public constructor
    }
    public static ServiceBooking newInstance(int id) {
        ServiceBooking fragment = new ServiceBooking();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mID = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service_booking, container, false);
        service_img = view.findViewById(R.id.service_img);
        bookNow = view.findViewById(R.id.shoot);
        bookNow.setOnClickListener(this);
        switch (mID){
            case 0:
                service_img.setImageResource(R.drawable.personal_coaching);
                break;

            case 1:
                service_img.setImageResource(R.drawable.career_coaching);
                break;

            case 2:
                service_img.setImageResource(R.drawable.student_coaching);
                break;

            case 3:
                service_img.setImageResource(R.drawable.corporate_coaching);
                break;
        }

        name = view.findViewById(R.id.client_name);
        phone = view.findViewById(R.id.client_phone);
        expectation_response = view.findViewById(R.id.expectation_response);

        timeline = view.findViewById(R.id.coaching_timeline);
        parent_teacher_question = view.findViewById(R.id.parent_teacher_question);
        expectation_question = view.findViewById(R.id.expectation_question);
        timeline_question = view.findViewById(R.id.timeline_question);


        String client_name = name.getText().toString();
        String client_phone= phone.getText().toString();
        String parent_teacher_ques = parent_teacher_question.getText().toString();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnServiceInteraction) {
            mListener = (OnServiceInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnServiceInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    void sendMail() {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("text/html");
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "subject");
        String body = "<tr>  <td>Name : </td> <td>Doe</td> </tr> <br><br>"+
                "<tr>  <td>Phone number : </td> <td>7416226233</td> </tr> <br><br>"+
                "<tr>  <td>Type : </td> <td>Parent</td> </tr> <br><br>"+
                "<tr>  <td>Coaching service description : </td> <td>flakjfh a;kfjha kfjha lkfjhak jfh alfkjha lfkjha fkhlakfh lafkj halfkhalkfa</td> </tr> <br><br>"+
                "<tr>  <td>Time line : </td> <td>Immediate</td> </tr> <br>";
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(body));
        startActivity(Intent.createChooser(emailIntent, "Email:"));
    }

    @Override
    public void onClick(View v) {
        sendMail();
    }
}
