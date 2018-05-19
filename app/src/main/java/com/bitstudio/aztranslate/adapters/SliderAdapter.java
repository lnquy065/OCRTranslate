package com.bitstudio.aztranslate.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bitstudio.aztranslate.R;

/**
 * Created by khoanguyen on 3/18/18.
 */

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    public String[] slider_descs = new String[3];

    public SliderAdapter(Context context){
        this.context = context;
        slider_descs[0] = "OCR Translation is very convenient and easy to use";
        slider_descs[1] = "Easy to review history and store favorite images";
        slider_descs[2] = "Store your favorite words and reminders to learn vocabulary";
    }

    public int[] slider_images = {
            R.drawable.m1,
            R.drawable.m2,
            R.drawable.m3
    };

//    public String[] slider_headings = {
//            "",
//            "",
//            ""
//    };



    @Override
    public int getCount() {
        return slider_descs.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (RelativeLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout,container,false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_image);
        //TextView slideHeading = (TextView) view.findViewById(R.id.slide_heading);
        TextView slideDescription = (TextView) view.findViewById(R.id.slide_desc);

        slideImageView.setImageResource(slider_images[position]);
        //slideHeading.setText(slider_headings[position]);
        slideDescription.setText(slider_descs[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }
}
