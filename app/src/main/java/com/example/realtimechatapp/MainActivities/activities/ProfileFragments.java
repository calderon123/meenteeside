package com.example.realtimechatapp.MainActivities.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.realtimechatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragments extends Fragment {

    private TextView fullname,email;
    CircleImageView profile_image;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile_fragments, container, false);

        profile_image = view.findViewById(R.id.profile_image);
          fullname = view.findViewById(R.id.fullname);
          email = view.findViewById(R.id.email);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("UserMentee").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserMentee userMentee = dataSnapshot.getValue(UserMentee.class);

                assert userMentee != null;
                fullname.setText(userMentee.getFullname());
                email.setText(userMentee.getEmail());
//                if (userMentee.getImage().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
//                }else{
//                    Glide.with(getContext()).load(userMentee.getImage()).into(profile_image);
//                }
//
              }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }


}
