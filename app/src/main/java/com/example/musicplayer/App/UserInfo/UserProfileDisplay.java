package com.example.musicplayer.App.UserInfo;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.musicplayer.PreLogin.Login;
import com.example.musicplayer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileDisplay extends Fragment {

    FirebaseAuth auth;
    Button logout;
    TextView emailDisplay;
    FirebaseUser user;

    public UserProfileDisplay()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_user_profile_display, container, false);

        auth = FirebaseAuth.getInstance();
        logout = view.findViewById(R.id.logout);
        emailDisplay = view.findViewById(R.id.user_details);
        user = auth.getCurrentUser();

        if (user == null)
        {
            Intent loginIntent = new Intent(getActivity() /*this of the parent*/ , Login.class);
            startActivity(loginIntent);
            getActivity().finish();
        }
        else
        {
            emailDisplay.setText(user.getEmail());
        }

        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FirebaseAuth.getInstance().signOut();
                Intent loginIntent = new Intent(getActivity() /*this of the parent*/, Login.class);
                startActivity(loginIntent);
                getActivity().finish();
            }
        });

        return view;
    }
}

