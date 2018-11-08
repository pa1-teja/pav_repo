package com.pearlcoaching.pearlcoaching;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.viewHolder> {

    int[] serviceImages = {
            R.drawable.personal_coaching,
            R.drawable.career_coaching,
            R.drawable.student_coaching,
            R.drawable.corporate_coaching,
    };

    private Context mContext;
     Bundle data;
    public ServiceAdapter(Context context, Bundle data){
        mContext = context;
        this.data = data;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.srvice_list_item,viewGroup,false);

        viewHolder viewHolder = new viewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder viewHolder, int i) {

        viewHolder.serviceImage.setImageResource(serviceImages[i]);

        viewHolder.serviceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sdIntent = new Intent(mContext, ServiceDescription.class);
                data.putInt("service_image",serviceImages[viewHolder.getAdapterPosition()]);
                sdIntent.putExtra("service_details",data);
                mContext.startActivity(sdIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceImages.length;
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        ImageView serviceImage;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            serviceImage = itemView.findViewById(R.id.services_list_item_img);
        }
    }

}
