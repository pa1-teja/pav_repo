package com.pearlcoaching.pearlcoaching.ServicesModule;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
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
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabWidget;
import android.widget.TextView;
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
import com.pearlcoaching.pearlcoaching.LoginScreen;
import com.pearlcoaching.pearlcoaching.R;
import com.pearlcoaching.pearlcoaching.SMTP.sendEmailTask;
import com.pearlcoaching.pearlcoaching.baseclass.BaseFragment;
import com.pearlcoaching.pearlcoaching.customEditText;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ServiceBooking extends BaseFragment implements View.OnClickListener {


    static final String ARG_PARAM1 = "param1";
    private static final String TAG = "ServiceBooking";

    private int mID;
    private String mParam2;

    private OnServiceInteraction mListener;
    ImageView service_img;
    AppCompatEditText name,phone,sent_otp_code;
    customEditText expectation_response;
    TextView timeline;
    AppCompatTextView parent_teacher_question,expectation_question,timeline_question,service_header;
    AppCompatButton bookNow;
    Intent emailIntent;
    LinearLayout otp_module;

    RadioGroup parentTeacherGroup, timelineGroup;

    private FirebaseAuth mAuth;
    private String mVerificationId;

    private Context mContext;
    private String client_phone,timeline_response="";
    private String client_name, parent_teacher_response;
    private String client_email;
    private Pattern pattern;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    public static final String PREFS_PEARL_COACHING ="com.pearlcoaching.pearlcoaching";
    public static final String MOBILE_NUMBER_PREF= "MobileNumber";

    private SharedPreferences sp;
    private String OTPcode;
    private SharedPreferences.Editor spEditor;
    public static boolean isVerified;

    public AlertDialog.Builder builder;

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

        sp = getContext().getSharedPreferences(PREFS_PEARL_COACHING ,Context.MODE_PRIVATE);
        spEditor = sp.edit();



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
        service_header = view.findViewById(R.id.service_header);
        parentTeacherGroup = view.findViewById(R.id.parent_teacher_group);
        timelineGroup = view.findViewById(R.id.timeline_response);
        parentTeacherGroup.clearCheck();
        timeline = view.findViewById(R.id.coaching_timeline);
        timeline.setTypeface(null, Typeface.BOLD_ITALIC);

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
                service_header.setText(R.string.personal_coaching_header);
                service_img.setImageResource(R.drawable.personal_coaching);
                break;

            case 1:
                service_header.setText(R.string.student_coaching_header);
                view.findViewById(R.id.parent_teacher_group).setVisibility(View.VISIBLE);
                view.findViewById(R.id.parent_teacher_question).setVisibility(View.VISIBLE);
                service_img.setImageResource(R.drawable.student_coaching);
//                service_img.setImageResource(R.drawable.career_coaching);
                break;

            case 2:
                service_header.setText(R.string.career_coaching_header);
                service_img.setImageResource(R.drawable.career_coaching);
//                view.findViewById(R.id.parent_teacher_group).setVisibility(View.VISIBLE);
//                view.findViewById(R.id.parent_teacher_question).setVisibility(View.VISIBLE);
//                service_img.setImageResource(R.drawable.student_coaching);
                break;

            case 3:
                service_header.setText(R.string.corporate_coaching_header);
                service_img.setImageResource(R.drawable.corporate_coaching);
                break;
        }

        name = view.findViewById(R.id.client_name);
        name.setFocusable(false);
        name.setFocusableInTouchMode(true);
        phone = view.findViewById(R.id.client_phone);
        phone.setFocusable(false);
        phone.setFocusableInTouchMode(true);
        expectation_response = view.findViewById(R.id.expectation_response);
        expectation_response.setFocusable(false);
        expectation_response.setFocusableInTouchMode(true);

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


        expectation_response.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    sent_otp_code.clearFocus();
            }
        });

//        String parent_teacher_ques = parent_teacher_question.getText().toString();

        pattern = Pattern.compile("(0/91)?[6-9][0-9]{9}");

        if (!sp.getString(MOBILE_NUMBER_PREF, "").equals("")
                && sp.getString(MOBILE_NUMBER_PREF, "").matches(String.valueOf(pattern)))
        {
            client_phone = sp.getString(MOBILE_NUMBER_PREF, "");
            phone.setText(client_phone);
        }

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e(")))", "start : " + start + " b4 : " + before + " count :" + count);
                if (start ==9){
                    client_phone= phone.getText().toString();
//                    pattern = Pattern.compile("(0/91)?[7-9][0-9]{9}");
                    if (pattern.matcher(client_phone).matches() && !client_phone.equals(sp.getString(MOBILE_NUMBER_PREF, ""))) {
                        if (LoginScreen.isConnected(mContext)) {
                            otp_module.setVisibility(View.VISIBLE);
//                        sp.edit().putString(MOBILE_NUMBER_PREF,client_phone).apply();
                            sendVerificationCode(client_phone);
                        }else {
                            alert();
//                            Toast.makeText(mContext,"Please check your internet connection",Toast.LENGTH_LONG).show();
                        }
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

                    client_name = name.getText().toString();
                    if (client_name.isEmpty() || client_name.length() <= 2){
                        new AlertDialog.Builder(getActivity()).setTitle("Alert").setMessage("Your name should be more than 3 letters. Please check again").create().show();
                    }

                }
            }
        });


        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!Patterns.PHONE.matcher(client_phone).matches()) {
                        client_phone = "entered invalid number";
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Alert")
                                .setMessage("Doesn't seem like a valid phone number. Please check again")
                                .create()
                                .show();
                    }
                }
            }
        });




        return view;
    }

    public void alert(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(mContext, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(mContext);
        }
        builder.setTitle("Oops!")
                .setMessage("Sorry, We failed to validate the mobile number." +
                        " Please check your internet connection and click on \"try again\" below or \"close\" to try after some time")
                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        if (Patterns.PHONE.matcher(client_phone).matches()) {
                            otp_module.setVisibility(View.VISIBLE);
//                        sp.edit().putString(MOBILE_NUMBER_PREF,client_phone).apply();
                            sendVerificationCode(client_phone);
                        } else {
                            Toast.makeText(mContext,"Please check and re-enter the mobile number again.",Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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

        if(LoginScreen.isConnected(mContext)) {
            if (checkValidation()) {
                final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                //        emailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
                emailIntent.setType("text/html");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"pearlcoaching.in@gmail.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Client Details");
                String text = "";
                String coachingType = "";
                if (mID == 0)
                    coachingType = getResources().getString(R.string.personal_coaching_header);
                else if (mID == 1) {
                    coachingType = getResources().getString(R.string.student_coaching_header);
                    text = "<tr> <td> " + parent_teacher_question.getText() + " : </td> <td>" + parent_teacher_response + "</td> </tr> <br><br>";
                } else if (mID == 2)
                    coachingType = getResources().getString(R.string.career_coaching_header);
                else if (mID == 3)
                    coachingType = getResources().getString(R.string.corporate_coaching_header);


                String body = "<tr> <td> " + coachingType + "</td></tr> " + "<tr>  <td>Name : </td> <td>" + name.getText() + "</td> </tr> <br><br>" +
                        "<tr>  <td>Phone number : </td> <td>" + client_phone + "</td> </tr> <br><br>" +
                        text +
                        "<tr>  <td>" + expectation_question.getText() + " :  </td> <td>" +
                        expectation_response.getText() + "</td> </tr> <br><br>" +
                        "<tr>  <td>" + timeline_question.getText() + ": </td> <td> " + timeline_response + "</td> </tr> <br>";


            new sendEmailTask(getActivity(),mListener,body).execute("info.pearlcoaching@gmail.com",
                    "admin@2017" , Arrays.asList("pearlcoaching.in@gmail.com"), "Client Details", body);

//                new sendEmailTask(getActivity(), mListener).execute("info.pearlcoaching@gmail.com",
//                        "admin@2017", Arrays.asList("pavanteja.93@gmail.com"), "Client Details", body);


//            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(body));
//            startActivity(Intent.createChooser(emailIntent, "Email:"));
//            goToThankYouPage();
            }
        }
        else {
            Toast.makeText(getContext(), "Please check your internet connection and try again to send your information.", Toast.LENGTH_LONG).show();
        }
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
                mCallbacks,
                mResendToken);
    }


    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            OTPcode = phoneAuthCredential.getSmsCode();


            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
//            if (code != null) {
//                sent_otp_code.setText(code);
//                //verifying the code
//                verifyVerificationCode(code);
//            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            if (LoginScreen.isConnected(getContext())) {
                Toast.makeText(getContext(), "Please check the entered mobile number again.", Toast.LENGTH_LONG).show();
                Log.e("$$$$", e.getMessage());
            }
            else {
                Toast.makeText(getContext(), "Please check Your Internet connection and try entering the mobile number again", Toast.LENGTH_LONG).show();
            }

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;

            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:" + s);

            // Save verification ID and resending token so we can use them later
//            mVerificationId = verificationId;
            mResendToken = forceResendingToken;
        }
    };


    private boolean verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
        return isVerified;
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
                            isVerified = true;
                            client_phone = client_phone + "  (verified)";
                            sp.edit().putString(MOBILE_NUMBER_PREF,client_phone).apply();
                            Toast.makeText(getActivity(),"verifies sucessfully",Toast.LENGTH_SHORT).show();
                        } else {

                            //verification unsuccessful.. display an error message
                            isVerified = false;
                            Toast.makeText(getContext(),"OTP verification unsuccessful.", Toast.LENGTH_SHORT).show();
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
        switch (v.getId()) {
            case R.id.shoot:
                sendMailMsg();
                break;
            case R.id.client_name:
                phone.clearFocus();
                sent_otp_code.clearFocus();
                break;
        }
    }

    private void sendMailMsg(){
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//
//        // Setting Dialog Title
//        alertDialog.setTitle("Confirm");
//
//        // Setting Dialog Message
//        alertDialog.setMessage("Please E-mail us your information to pearlcoaching.in@gmail.com in order to book a session with us.");
//        // Setting Positive "Yes" Button
//        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog,int which) {
//                mListener.onThankYou("");
                sendMail();
//                goToThankYouPage();
//            }
//        });

        // Setting Negative "NO" Button
//        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                // Write your code here to invoke NO event
//                //Toast.makeText(getApplicationContext(), "NO", Toast.LENGTH_SHORT).show();
//                dialog.cancel();
//            }
//        });

        // Showing Alert Message
//        alertDialog.show();
    }

    public void goToThankYouPage() {

        mListener.onThankYou("");

    }

    boolean checkValidation() {
        //boolean isValid = true;
        String mob = sent_otp_code.getText().toString();
        String savedMob = sp.getString(MOBILE_NUMBER_PREF,"");
        String onScreenMob = phone.getText().toString();
        if (!savedMob.equals(onScreenMob)) {
            if ((!mob.isEmpty() && mob.equals(OTPcode)))
                spEditor.putString(MOBILE_NUMBER_PREF, client_phone).apply();
            else {
                Toast.makeText(getContext(), "Please check and enter your OTP code correctly.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
//        } else {
//            Toast.makeText(getContext(),"Please check your OTP code once again.", Toast.LENGTH_SHORT).show();
//            return false;
//        }

        if (mID == 1){
            if (parent_teacher_response== null || parent_teacher_response.isEmpty() || parent_teacher_response.equals("null")){
                Toast.makeText(getContext(),"Please confirm if you are a parent or a teacher.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        String str = expectation_response.getText().toString();
        if(null == client_phone || !pattern.matcher(client_phone).matches()) {
            Toast.makeText(getActivity(), "Please enter valid phone number. Make sure that the mobile number is validated with OTP.",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(str.isEmpty() || str.length() <=5) {
            Toast.makeText(getActivity(), "Please enter your expectations from the service in a sentence.",Toast.LENGTH_LONG).show();
            return false;
        } else if(timeline_response.isEmpty()) {
            Toast.makeText(getActivity(), "Please select the timeline.",Toast.LENGTH_LONG).show();
            return false;
        }


        return true;
    }
}
