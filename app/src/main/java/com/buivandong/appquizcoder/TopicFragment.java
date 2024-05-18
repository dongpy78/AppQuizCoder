package com.buivandong.appquizcoder;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TopicFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the correct layout for this fragment
        View view = inflater.inflate(R.layout.fragment_topic, container, false);

        // Find your CardView elements here
        CardView reactCard = view.findViewById(R.id.react_card);
        CardView javascriptCard = view.findViewById(R.id.javascript_card);
        CardView htmlCard = view.findViewById(R.id.html_card);
        CardView pythonCard = view.findViewById(R.id.python_card);
        CardView cplusCard = view.findViewById(R.id.cplus_card);
        CardView javaCard = view.findViewById(R.id.java_card);

        reactCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), QuestionsReactJS.class);
                startActivity(intent);
            }
        });
        return view;
    }
}