package com.pearlcoaching.pearlcoaching.ServicesModule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pearlcoaching.pearlcoaching.FeedbackScreen;
import com.pearlcoaching.pearlcoaching.R;
import com.pearlcoaching.pearlcoaching.baseclass.BaseFragment;

public class ServiceInfo extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int mID;
    private String mParam2;

    private OnServiceInteraction mListener;

    ImageView service_img;
    AppCompatTextView mTVHeader;
    AppCompatTextView mTVDescription;
    AppCompatTextView mBTBook;

    public ServiceInfo() {
        // Required empty public constructor
    }

    public static ServiceInfo newInstance(int id) {
        ServiceInfo fragment = new ServiceInfo();
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
        View view = inflater.inflate(R.layout.fragment_service_info, container, false);
        service_img = view.findViewById(R.id.service_img);
        mTVHeader = view.findViewById(R.id.description_header);
        mTVDescription = view.findViewById(R.id.description_text);
        mBTBook = view.findViewById(R.id.book_btn);

        switch (mID){
            case 0:
                service_img.setImageResource(R.drawable.personal_coaching);
                mTVHeader.setText(R.string.personal_coaching_header);
                mTVDescription.setText(R.string.personal_couching_drscription);
                break;

            case 1:
                service_img.setImageResource(R.drawable.career_coaching);
                mTVHeader.setText(R.string.career_coaching_header);
                mTVDescription.setText(R.string.career_coaching_description);
                break;

            case 2:
                service_img.setImageResource(R.drawable.student_coaching);
                mTVHeader.setText(R.string.student_coaching_header);
                mTVDescription.setText(R.string.student_coaching_description);
                break;

            case 3:
                service_img.setImageResource(R.drawable.corporate_coaching);
                mTVHeader.setText(R.string.corporate_coaching_header);
                mTVDescription.setText(R.string.corporate_coaching_description);
                break;
        }

        mBTBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onServiceBooking(mID);
            }
        });
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
}
