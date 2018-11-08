package com.pearlcoaching.pearlcoaching.ServicesModule;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pearlcoaching.pearlcoaching.R;

public class ServiceAdapter extends RecyclerView.Adapter<viewHolder> {

    private OnServiceInteraction mListener;
    int[] serviceImages = {
            R.drawable.personal_coaching,
            R.drawable.career_coaching,
            R.drawable.student_coaching,
            R.drawable.corporate_coaching,
    };

    private Context mContext;
     Bundle data;
    public ServiceAdapter(Context context, Bundle data, OnServiceInteraction listener){
        mContext = context;
        this.data = data;
        mListener = listener;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.service_list_item,viewGroup,false);

        viewHolder viewHolder = new viewHolder(itemView, mListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder viewHolder, int i) {
        viewHolder.serviceImage.setImageResource(serviceImages[i]);
        viewHolder.serviceImage.setTag(i);
    }

    @Override
    public int getItemCount() {
        return serviceImages.length;
    }

}
