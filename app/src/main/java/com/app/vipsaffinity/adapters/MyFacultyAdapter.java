package com.app.vipsaffinity.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.app.vipsaffinity.utils.Constants;
import com.app.vipsaffinity.utils.Helper;
import com.app.vipsaffinity.R;
import com.app.vipsaffinity.interfaces.MyFacultyListener;
import com.app.vipsaffinity.models.FacultyMember;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class MyFacultyAdapter extends RecyclerView.Adapter<MyFacultyAdapter.ViewHolder> {
    //class variables
    Context mContext;
    LayoutInflater inflater;
    List<FacultyMember> mList;
    MyFacultyListener mListener;

    public MyFacultyAdapter(Context context, MyFacultyListener listener, List<FacultyMember> mList) {
        mContext = context;
        this.mList = mList;
        mListener = listener;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_my_faculty_item, parent, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Helper.setText(mList.get(position).getName(), holder.name_faculty, true);
        Helper.setText(mList.get(position).getBio(), holder.bio_faculty, true);
        Helper.setText(mList.get(position).getDepartment_short(), holder.department_short_faculty, true);
        if (mList.get(position).getImage_url().length() != 0) {
            Glide.with(mContext).setDefaultRequestOptions(new RequestOptions()
                    .placeholder(R.drawable.icon_my_faculty).error(R.drawable.icon_my_faculty))
                    .load(Constants.FIREBASE_BASE_URL + mList.get(position).getImage_url())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            // setting alpha
                            holder.img_faculty.startAnimation(Helper.getAlphaAnimation1000());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            // setting alpha
                            holder.img_faculty.startAnimation(Helper.getAlphaAnimation1000());
                            return false;
                        }
                    }).into(holder.img_faculty);
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        //class variables
        ImageView img_faculty;
        TextView name_faculty;
        TextView bio_faculty;
        TextView department_short_faculty;

        public ViewHolder(@NonNull View itemView, final MyFacultyListener mListener) {
            super(itemView);
            img_faculty = itemView.findViewById(R.id.img_faculty);
            name_faculty = itemView.findViewById(R.id.name_faculty);
            bio_faculty = itemView.findViewById(R.id.bio_faculty);
            department_short_faculty = itemView.findViewById(R.id.department_short_faculty);
            //setting onClick listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.showFacultyDetailsActivity(getAdapterPosition());
                }
            });
        }
    }
}
