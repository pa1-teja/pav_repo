package com.pearlcoaching.pearlcoaching.SMTP;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pearlcoaching.pearlcoaching.ServicesModule.OnServiceInteraction;
import com.pearlcoaching.pearlcoaching.ServicesModule.ServiceBooking;

import java.util.Arrays;
import java.util.List;

public class sendEmailTask extends AsyncTask {

    private final String body;
    private Context context;
    private ProgressDialog progressBar;
    private OnServiceInteraction mListener;
    private AlertDialog.Builder builder;


    public sendEmailTask(Context context, OnServiceInteraction mListener,String body) {
        this.context = context;
        this.mListener = mListener;
        this.body = body;
    }


    @Override
    protected void onPreExecute() {
        progressBar = new ProgressDialog(context);
        progressBar.setIndeterminate(false);
        progressBar.setCancelable(false);
        progressBar.setMessage("Sending Mail...");
        progressBar.show();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        boolean isMailSent = false;
        try {
            Log.i("SendMailTask", "About to instantiate GMail...");
            publishProgress("Processing input....");
            GMail androidEmail = new GMail(objects[0].toString(),
                    objects[1].toString(), (List) objects[2], objects[3].toString(),
                    objects[4].toString());
            publishProgress("Preparing mail message....");
            androidEmail.createEmailMessage();
            publishProgress("Sending email....");
             isMailSent = androidEmail.sendEmail();
            publishProgress("Email Sent.");
            Log.i("SendMailTask", "Mail Sent.");
        } catch (Exception e) {
            publishProgress(e.getMessage());
            Log.e("SendMailTask", e.getMessage(), e);
        }
        String str;
        if (isMailSent)
            str = "success";
        else str = "failed";

        return str;
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Object o) {

        progressBar.dismiss();
        switch (o.toString()){
            case "success":
                Toast.makeText(context,"Mail Sent",Toast.LENGTH_SHORT).show();
               mListener.onThankYou((String) o);
                break;
            case "failed":
                alert();
                break;
        }
    }

    public void alert(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle("Oops!")
                .setMessage("Sorry, We couldn't receive your details." +
                        " Please check your internet connection and click on \"try again\" below or \"close\" to try after some time")
                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        new sendEmailTask(context,mListener,body).execute("info.pearlcoaching@gmail.com",
                                "admin@2017" , Arrays.asList("pearlcoaching.in@gmail.com"), "Client Details", body);

                new sendEmailTask(context, mListener,body).execute("info.pearlcoaching@gmail.com",
                        "admin@2017", Arrays.asList("pavanteja.93@gmail.com"), "Client Details", body);

                    }
                })
                .setNegativeButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
