
package com.example.cse234termproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.cse234termproject.Adapters.CampaignAdapter;
import com.example.cse234termproject.Model.CampaignModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {
    ViewPager viewPager;
    CampaignAdapter campaignAdapter;
    DatabaseReference SliderImages;
    LinearLayout sliderDotsPanel;
    private int dotsCount;
    private ImageView[] dots;
    private Timer timer = new Timer();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        SliderImages = FirebaseDatabase.getInstance().getReference("SliderImages");
        SliderImages.addListenerForSingleValueEvent(new ValueEventListener() {
            List<CampaignModel> imageSlider = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot imageSliderSnapshot : snapshot.getChildren())
                    imageSlider.add(imageSliderSnapshot.getValue(CampaignModel.class));
                campaignAdapter = new CampaignAdapter(getActivity(),imageSlider);
                viewPager.setAdapter(campaignAdapter);
                dots(view);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onIFirebaseLoadFailed(error.getMessage());
            }
        });
        viewPager = view.findViewById(R.id.viewPager);
        //viewPager.setPageTransformer(true,new DepthPageTransformer());
        timer.schedule(new MyTimerTask(), 2000, 2000);
        return view;
    }
    //image slider dots
    private void dots(View view) {
        sliderDotsPanel = (LinearLayout) view.findViewById(R.id.SliderDots);
        dotsCount = campaignAdapter.getCount();
        dots = new ImageView[dotsCount];
        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.nonactive_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            sliderDotsPanel.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.active_dot));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.nonactive_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.active_dot));

            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void onIFirebaseLoadFailed(String message) {
    }
    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }
    //time for Auto Image Slider
    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() != dotsCount - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else if (viewPager.getCurrentItem() == dotsCount - 1) {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }
}