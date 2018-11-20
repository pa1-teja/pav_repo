package com.pearlcoaching.pearlcoaching.ServicesModule;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pearlcoaching.pearlcoaching.R;

public class ThankYou extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final int SPLASH_DISPLAY_LENGTH = 3000;

     ImageView thankYouImage;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnServiceInteraction mListener;

    public ThankYou() {
        // Required empty public constructor
    }

    public static ThankYou newInstance(String param1, String param2) {
        ThankYou fragment = new ThankYou();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_thank_you, container, false);
        thankYouImage = view.findViewById(R.id.thank_you);
        /*new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                *//* Create an Intent that will start the Menu-Activity. *//*
               thankYouImage.setVisibility(View.VISIBLE);
            }
        }, SPLASH_DISPLAY_LENGTH);*/
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
