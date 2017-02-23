package com.br.ar.my.samples;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beyondar.android.fragment.BeyondarFragmentSupport;
import com.beyondar.android.view.OnClickBeyondarObjectListener;
import com.beyondar.android.world.BeyondarObject;
import com.br.ar.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentExampleAR#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentExampleAR extends BeyondarFragmentSupport implements OnClickBeyondarObjectListener  {

    public FragmentExampleAR() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentExampleAR.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentExampleAR newInstance(String param1, String param2) {
        FragmentExampleAR fragment = new FragmentExampleAR();
        return fragment;
    }

    public static FragmentExampleAR newInstance() {
        FragmentExampleAR fragment = new FragmentExampleAR();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_example_ar, container, false);
    }

    @Override
    public void onClickBeyondarObject(ArrayList<BeyondarObject> arrayList) {

    }
}
