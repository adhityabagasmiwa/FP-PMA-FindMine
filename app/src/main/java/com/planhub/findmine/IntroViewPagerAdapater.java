package com.planhub.findmine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class IntroViewPagerAdapater extends PagerAdapter {

    Context mContext;
    List<ScreenItem> mListItemScreen;

    public IntroViewPagerAdapater(Context mContext, List<ScreenItem> mListItemScreen) {
        this.mContext = mContext;
        this.mListItemScreen = mListItemScreen;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.layout_screen, null);

        ImageView imgSlide = layoutScreen.findViewById(R.id.imgIntro);
        TextView tvTitle = layoutScreen.findViewById(R.id.tvIntroTitle);
        TextView tvDesc = layoutScreen.findViewById(R.id.tvIntroDesc);

        tvTitle.setText(mListItemScreen.get(position).getTitle());
        tvDesc.setText(mListItemScreen.get(position).getDesc());
        imgSlide.setImageResource(mListItemScreen.get(position).getScreenImage());

        container.addView(layoutScreen);

        return layoutScreen;

    }

    @Override
    public int getCount() {
        return mListItemScreen.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
