package com.nixcvf18.myunitdemo.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nixcvf18.myunitdemo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AliceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AliceFragment extends Fragment {



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @paramparam1 Parameter 1.
     * @paramparam2 Parameter 2.
     * @return A new instance of fragment AliceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AliceFragment newInstance() {
        AliceFragment fragment = new AliceFragment();

        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alice, container, false);
    }
}