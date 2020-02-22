package com.digital.farhad_app.SplashScreen;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.digital.farhad_app.R;


public class OpeningFragment extends Fragment {


    public OpeningFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_opening, container, false);


        return root;
    }


}
