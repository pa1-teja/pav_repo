package com.pearlcoaching.pearlcoaching.ServicesModule;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.pearlcoaching.pearlcoaching.R;

public class ServiceItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView serviceImage;
    OnServiceInteraction mListener;
    public ServiceItemHolder(@NonNull View itemView, OnServiceInteraction listener) {
        super(itemView);
        mListener = listener;
        serviceImage = itemView.findViewById(R.id.services_list_item_img);
        serviceImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mListener.onServiceInfo((Integer) v.getTag());
        /*ServiceItemHolder.serviceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sdIntent = new Intent(mContext, ServiceDescription.class);
                data.putInt("service_image",serviceImages[ServiceItemHolder.getAdapterPosition()]);
                sdIntent.putExtra("service_details",data);
                mContext.startActivity(sdIntent);
            }
        });*/
    }
}
