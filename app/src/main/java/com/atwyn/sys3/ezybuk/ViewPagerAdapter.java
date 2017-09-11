package com.atwyn.sys3.ezybuk;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

/**
 * Created by SYS3 on 9/7/2017.
 */

public class ViewPagerAdapter extends PagerAdapter {


   // private Integer [] images={ R.drawable.a,R.drawable.b};
    private LayoutInflater layoutInflater;
    private Context context;
    private List<SliderUtils> sliderImg;
    private ImageLoader imageLoader;


    public ViewPagerAdapter(List<SliderUtils> sliderImg, Context context) {
        this.sliderImg=sliderImg;
        this.context = context;




    }


    @Override
    public int getCount() {
        return sliderImg.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slidingimages_layout,null);
        SliderUtils utils=sliderImg.get(position);




        ImageView image=(ImageView)view.findViewById(R.id.brandimage);
       // image.setImageResource(images[position]);
        imageLoader=CustomVolleyRequest.getInstances(context).getImageLoader();
        imageLoader.get(utils.getSliderImageUrl(), ImageLoader.getImageListener(image,R.drawable.camaraposter,android.R.drawable.ic_menu_report_image));

        ViewPager vp=(ViewPager) container;
        vp.addView(view,0);
       return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       ViewPager vp=(ViewPager)container;
        View view =(View)object;
        vp.removeView(view);
    }

}
