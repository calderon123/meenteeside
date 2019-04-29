package com.example.realtimechatapp.MainActivities.activities;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.realtimechatapp.MainActivities.fragments.MentorListFragment;
import com.example.realtimechatapp.MainActivities.fragments.MessagesFragment;
import com.example.realtimechatapp.MainActivities.fragments.ProfileFragments;
import com.example.realtimechatapp.MainActivities.models.Chat;
import com.example.realtimechatapp.MainActivities.models.UserMentee;
import com.example.realtimechatapp.MainActivities.models.UserMentor;
import com.example.realtimechatapp.MainActivities.questions.Question1;
import com.example.realtimechatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenteeMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private CircleImageView imageView,img_on,img_off;
    private TextView fullname,email;
    private MenuItem logout_btn;
    Button send_verification;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    NavigationView nav_view;
    RelativeLayout unverified;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentee_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        changeText();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final TabLayout tableLayout = findViewById(R.id.tab_layout);
        final ViewPager viewPager = findViewById(R.id.view_pager);



        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                int unread = 0;
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && !chat.isIsseen()){
                        unread++;
                    }
                }
                if (unread == 0){
                    viewPagerAdapter.addFragment(new MessagesFragment(), "Messages");
                }else {
                    viewPagerAdapter.addFragment(new MessagesFragment(), "("+unread+")Messages");
                }
                viewPagerAdapter.addFragment(new MentorListFragment(),"Counselors" );
                viewPagerAdapter.addFragment(new ProfileFragments(), "Profile");


                viewPager.setAdapter(viewPagerAdapter);

                tableLayout.setupWithViewPager(viewPager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void changeText() {
        changeText2();
    }

    private void changeText2() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("UserMentee").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserMentee userMentee = dataSnapshot.getValue(UserMentee.class);


                img_off = (CircleImageView) findViewById(R.id.img_of);
                img_on =(CircleImageView) findViewById(R.id.img_o);
                fullname = findViewById(R.id.fullnam);
                email = findViewById(R.id.emai);
                imageView = findViewById(R.id.imageView);

                try {

                    if (userMentee.getStatus().equals("online")) {
                        img_on.setVisibility(View.VISIBLE);
                        img_off.setVisibility(View.GONE);
                    } else {
                        img_on.setVisibility(View.GONE);
                        img_off.setVisibility(View.GONE);
                    }
                    if (fullname != null) {
                        fullname.setText(userMentee.getFullname());
                        email.setText(userMentee.getEmail());
                    }
                    if (userMentee.getImageURL().equals("default")) {
                        imageView.setImageResource(R.drawable.ic_account_circle_black_24dp);

                    } else {
                        Glide.with(getApplicationContext()).load(userMentee.getImageURL()).into(imageView);

                    }
                }catch (NullPointerException e){
                    throw  new IllegalStateException("This is not possible",e);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mentor_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item1) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item1.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            auth.signOut();
            startActivity(new Intent(MenteeMainActivity.this, StartActivity.class)
            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            return true;
        }

        return super.onOptionsItemSelected(item1);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.find_mentor) {
            startActivity(new Intent(MenteeMainActivity.this, Question1.class));
        }else if (id == R.id.send_report) {
            startActivity(new Intent(MenteeMainActivity.this, ReportActivity.class));
        }else if (id == R.id.logout_btn){
            auth.signOut();
            finish();
            startActivity(new Intent(MenteeMainActivity.this, StartActivity.class));
            finish();
        }

        if (fragment !=null){
                    FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            ft.replace(R.id.profile_fragment, fragment);

            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment (Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }


        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);

        }
    }
    private void status(String status){
        UserMentor userMentor = null;
        databaseReference  = FirebaseDatabase.getInstance().getReference("UserMentee").
        child(firebaseUser.getUid());

        HashMap<String,Object> hashMap= new HashMap<>();
        hashMap.put("status",status);

        databaseReference.updateChildren(hashMap);

    }
    @Override
    protected void onResume(){
        super.onResume();
        status("online");
    }
    @Override
    protected  void onPause(){
        super.onPause();
        status("offline");
    }
}
