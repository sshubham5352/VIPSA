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
import com.app.vipsaffinity.interfaces.ProfessorListener;
import com.app.vipsaffinity.models.Professor;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class ProfessorAdapter extends RecyclerView.Adapter<ProfessorAdapter.ViewHolder> {
    //class variables
    Context mContext;
    LayoutInflater inflater;
    List<Professor> mList;
    ProfessorListener mListener;

    public ProfessorAdapter(Context context, ProfessorListener listener, List<Professor> mList) {
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
        View view = inflater.inflate(R.layout.recyclerview_professor_item, parent, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProfessorAdapter.ViewHolder holder, int position) {

        Helper.setText(mList.get(position).getName(), holder.name_professor, true);
        Helper.setText(mList.get(position).getDepartment_short(), holder.department_short_professor, true);
        Helper.setText(mList.get(position).getBio(), holder.bio_professor, true);
        if (mList.get(position).getImage_url().length() != 0) {
            Glide.with(mContext).setDefaultRequestOptions(new RequestOptions().
                    placeholder(R.drawable.icon_professor).error(R.drawable.icon_professor))
                    .load(Constants.FIREBASE_BASE_URL + mList.get(position).getImage_url())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.img_professor.startAnimation(Helper.getAlphaAnimation(600));
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                            holder.img_professor.startAnimation(Helper.getAlphaAnimation(600));
                            return false;
                        }
                    }).into(holder.img_professor);
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        //class variables
        ImageView img_professor;
        TextView name_professor;
        TextView bio_professor;
        TextView department_short_professor;

        public ViewHolder(@NonNull View itemView, final ProfessorListener mListener) {
            super(itemView);
            img_professor = itemView.findViewById(R.id.img_professor);
            name_professor = itemView.findViewById(R.id.department_professor);
            bio_professor = itemView.findViewById(R.id.bio_professor);
            department_short_professor = itemView.findViewById(R.id.name_professor);
            //setting onClick listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.showProfessorDetailsActivity(getAdapterPosition());
                }
            });
        }
    }
}
