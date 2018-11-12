package com.pearlcoaching.pearlcoaching.ServicesModule;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabWidget;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.pearlcoaching.pearlcoaching.R;
import com.pearlcoaching.pearlcoaching.baseclass.BaseFragment;
import com.pearlcoaching.pearlcoaching.customEditText;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class ServiceBooking extends BaseFragment implements View.OnClickListener {


    static final String ARG_PARAM1 = "param1";
    private static final String TAG = "ServiceBooking";

    private int mID;
    private String mParam2;

    private OnServiceInteraction mListener;
    ImageView service_img;
    AppCompatEditText name,phone,sent_otp_code;
    customEditText expectation_response,timeline;
    AppCompatTextView parent_teacher_question,expectation_question,timeline_question,service_header;
    AppCompatButton bookNow;
    Intent emailIntent;
    LinearLayout otp_module;

    RadioGroup parentTeacherGroup, timelineGroup;

    private FirebaseAuth mAuth;
    private String mVerificationId;

    private Context mContext;
    private String client_phone,timeline_response;
    private String client_name, parent_teacher_response;
    private String client_email;

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
        mContext = getContext();
        //initializing objects
        mAuth = FirebaseAuth.getInstance();
        mAuth.useAppLanguage();
        if (getArguments() != null) {
            mID = getArguments().getInt(ARG_PARAM1);
            client_name = getArguments().getString("displayName");
            client_phone= getArguments().getString("phoneNumber");
            client_email = getArguments().getString("email");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service_booking, container, false);
        service_img = view.findViewById(R.id.service_img);
        bookNow = view.findViewById(R.id.shoot);
        parentTeacherGroup = view.findViewById(R.id.parent_teacher_group);
        timelineGroup = view.findViewById(R.id.timeline_response);
        parentTeacherGroup.clearCheck();
        timeline = view.findViewById(R.id.coaching_timeline);

        parentTeacherGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton checked = group.findViewById(checkedId);

                // Check which radio button was clicked
                switch(checked.getId()) {
                    case R.id.parent:
                           parent_teacher_response = checked.getText().toString();
                            // Pirates are the best
                            break;
                    case R.id.teacher:
                        parent_teacher_response = checked.getText().toString();
                            // Ninjas rule
                            break;
                }
            }
        });

        timelineGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checked = group.findViewById(checkedId);

                // Check which radio button was clicked
                switch(checked.getId()) {
                    case R.id.immediate:
                        timeline_response = checked.getText().toString();
                        timeline.setText(timeline_response);
                        // Pirates are the best
                        break;
                    case R.id.two_weeks:
                        timeline_response = checked.getText().toString();
                        timeline.setText(timeline_response);
                        // Ninjas rule
                        break;

                    case R.id.one_month:
                        timeline_response = checked.getText().toString();
                        timeline.setText(timeline_response);
                        // Ninjas rule
                        break;
                    case R.id.after_a_month:
                        timeline_response = checked.getText().toString();
                        timeline.setText(timeline_response);
                        // Ninjas rule
                        break;
                }
            }
        });

        bookNow.setOnClickListener(this);
        switch (mID){
            case 0:
                service_img.setImageResource(R.drawable.personal_coaching);
                break;

            case 1:
                service_img.setImageResource(R.drawable.career_coaching);
                break;

            case 2:
                view.findViewById(R.id.parent_teacher_group).setVisibility(View.VISIBLE);
                view.findViewById(R.id.parent_teacher_question).setVisibility(View.VISIBLE);
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
        otp_module = view.findViewById(R.id.otp_module);
        sent_otp_code = view.findViewById(R.id.sent_otp_code);


        if (client_name != null && !client_name.isEmpty())
            name.setText(client_name);
        if (client_phone != null && !client_phone.isEmpty())
            phone.setText(client_phone);


        String parent_teacher_ques = parent_teacher_question.getText().toString();



        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e(")))", "start : " + start + " b4 : " + before + " count :" + count);
                if (start ==9){
                    client_phone= phone.getText().toString();
                    Pattern pattern = Pattern.compile("(0/91)?[7-9][0-9]{9}");
                    if (pattern.matcher(client_phone).matches()) {
                        otp_module.setVisibility(View.VISIBLE);
                        sendVerificationCode(client_phone);
                    }
                    else
                        new AlertDialog.Builder(getActivity()).setTitle("Alert").setMessage("Doesn't seem like a valid phone number. Please check again").create().show();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){

                    if (client_name == null || client_name.isEmpty() || client_name.length() > 3){
                        new AlertDialog.Builder(getActivity()).setTitle("Alert").setMessage("Your name should be more than 3 letters. Please check again").create().show();
                    }

                }
            }
        });

        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == false){

                    if (client_phone == null || client_phone.isEmpty() || client_phone.length() !=10){
                        client_phone = "entered invalid number";
                        new AlertDialog.Builder(getActivity()).setTitle("Alert").setMessage("Doesn't seem like a valid phone number. Please check again").create().show();
                    }

                }
            }
        });
//     phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//         @Override
//         public void onFocusChange(View v, boolean hasFocus) {
//
//             if (hasFocus == false){
//
//             }
//         }
//     });


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


        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
//        emailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
        emailIntent.setType("text/html");
        emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"pearlcoaching.in@gmail.com"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Client Details");
        String text="";
        if(mID == 2) {
            text = "<tr>  <td> " + parent_teacher_question.getText() + " : </td> <td>" + parent_teacher_response + "</td> </tr> <br><br>";
        }
        String body = "<tr>  <td>Name : </td> <td>" + name.getText() +"</td> </tr> <br><br>"+
                "<tr>  <td>Phone number : </td> <td>" + client_phone + "</td> </tr> <br><br>"+
                text+
                "<tr>  <td>" + expectation_question.getText()+ " :  </td> <td>" + expectation_response.getText() + "</td> </tr> <br><br>"+
                "<tr>  <td>" + timeline_question.getText() +": </td> <td> " + timeline_response + "</td> </tr> <br>";
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(body));
        startActivity(Intent.createChooser(emailIntent, "Email:"));
        mListener.onThankYou("");
    }

    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }


    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                sent_otp_code.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("$$$$", e.getMessage());
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }
    public void updateUI(boolean isvalid){
        if (isvalid)
            Toast.makeText(mContext,"verifies sucessfully",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(mContext,"mobile number verification failed",Toast.LENGTH_LONG).show();
        }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            client_phone = client_phone + "  (verified)";
                            Toast.makeText(getActivity(),"verifies sucessfully",Toast.LENGTH_SHORT).show();
                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";
                            Log.e("======D", message);
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                                Log.e("======D", message);
                            }
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        sendMail();
    }
}
