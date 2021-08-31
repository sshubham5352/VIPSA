package com.app.vipsaffinity.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.app.vipsaffinity.R;
import com.app.vipsaffinity.activities.IDCardModuleActivity;
import com.app.vipsaffinity.databinding.FragmentProfileBinding;
import com.app.vipsaffinity.utils.SessionManager;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    //field declaration
    Activity mActivity;
    Context mContext;
    ProgressDialog progressDialog;
    FragmentProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        mActivity = getActivity();
        //progress dialog
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Logging out...`");
        //setting onClickListeners
        binding.featureVirtualIdCard.setOnClickListener(this);
        binding.logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.feature_virtual_id_card)
            startActivity(new Intent(mActivity, IDCardModuleActivity.class));
        else if (id == R.id.logout) {
            progressDialog.show();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    SessionManager.logoutUser(mActivity);
                    progressDialog.dismiss();
                }
            }, 1000);
        }
    }
}



