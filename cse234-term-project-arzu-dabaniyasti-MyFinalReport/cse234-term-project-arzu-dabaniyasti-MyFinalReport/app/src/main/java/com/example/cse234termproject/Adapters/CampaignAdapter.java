package com.example.cse234termproject.Adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.example.cse234termproject.Model.CampaignModel;
import com.example.cse234termproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;
public class CampaignAdapter extends PagerAdapter {
    private Context context;
    List<CampaignModel> imageList;
    LayoutInflater layoutInflater;
    public CampaignAdapter(Context context, List<CampaignModel> imageList){
        this.imageList=imageList;
        this.context=context;
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=layoutInflater.inflate(R.layout.item_campaigns,container,false);
        ImageView imageView=(ImageView) view.findViewById(R.id.imageView2);
        Picasso.get().load(imageList.get(position).getImage()).into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position,Object object) {
        ( (ViewPager)container).removeView((View)object);
    }

}