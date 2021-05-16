package com.app.vipsaffinity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.vipsaffinity.utils.Constants;
import com.app.vipsaffinity.R;
import com.app.vipsaffinity.models.CampusUpdate;
import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class ImageSliderAdapter extends SliderViewAdapter<ImageSliderAdapter.ViewHolder> {
    //class variables
    Context mContext;
    List<CampusUpdate> campusImages;

    public ImageSliderAdapter(Context context, List<CampusUpdate> campusImages) {
        mContext = context;
        this.campusImages = campusImages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_slider_view_layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Glide.with(mContext).load(Constants.FIREBASE_BASE_URL + campusImages.get(position).getImgCampus()).into(viewHolder.imgCampus);
    }

    @Override
    public int getCount() {
        return campusImages.size();
    }

    public static class ViewHolder extends SliderViewAdapter.ViewHolder {
        //class variables
        ImageView imgCampus;

        ViewHolder(View itemView) {
            super(itemView);
            imgCampus = itemView.findViewById(R.id.img_campus);
        }
    }
}
