package com.app.vipsaffinity.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.app.vipsaffinity.R;
import com.app.vipsaffinity.databinding.FragmentPollBinding;

public class PollFragment extends Fragment {
    //field declaration
    FragmentPollBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_poll, container, false);
        return binding.getRoot();
    }
}
