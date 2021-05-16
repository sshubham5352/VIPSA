package com.app.vipsaffinity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.vipsaffinity.R;
import com.app.vipsaffinity.interfaces.EventListener;
import com.app.vipsaffinity.models.Event;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    //class variables
    Context mContext;
    LayoutInflater inflater;
    List<Event> mList;
    EventListener mListener;

    public EventAdapter(Context context, EventListener listener, List<Event> mList) {
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
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_event_item, parent, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final EventAdapter.ViewHolder holder, int position) {

//        Helper.setText(mList.get(position).getName(), holder.name_event, true);
//        Helper.setText(mList.get(position).getType(), holder.type_event, true);
//        Helper.setText(mList.get(position).getEdition(), holder.edition_event, true);
//
//        if (mList.get(position).getImage_url().length() != 0) {
//            Glide.with(mContext).setDefaultRequestOptions(new RequestOptions()
//                    .placeholder(R.drawable.icon_my_faculty).error(R.drawable.icon_my_faculty))
//                    .load(Constants.FIREBASE_BASE_URL + mList.get(position).getImage_url())
//                    .listener(new RequestListener<Drawable>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                            // setting alpha
//                            holder.img_event.startAnimation(Helper.getAlphaAnimation1000());
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                            // setting alpha
//                            holder.img_event.startAnimation(Helper.getAlphaAnimation1000());
//                            return false;
//                        }
//                    }).into(holder.img_event);
//        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        //class variables
        ImageView img_event;
        TextView name_event;
        TextView type_event;
        TextView edition_event;

        public ViewHolder(@NonNull View itemView, final EventListener mListener) {
            super(itemView);
            img_event = itemView.findViewById(R.id.img_event);
            name_event = itemView.findViewById(R.id.name_event);
            type_event = itemView.findViewById(R.id.type_event);
            edition_event = itemView.findViewById(R.id.edition_event);
            //setting onClick listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.showEventDetailsActivity(getAdapterPosition());
                }
            });
        }
    }
}
