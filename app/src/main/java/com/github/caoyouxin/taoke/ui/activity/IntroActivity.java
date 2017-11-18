package com.github.caoyouxin.taoke.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.caoyouxin.taoke.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

/**
 * Created by jasontsang on 10/23/17.
 */

public class IntroActivity extends AppIntro {
    public final static String INTRO_READ = "intro_read";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SliderPage sliderPage1 = new SliderPage();
        sliderPage1.setTitle("海量爆款疯狂秒");
        sliderPage1.setDescription("每日上新券，万件任你选。");
        sliderPage1.setImageDrawable(R.mipmap.ic_slide);
        sliderPage1.setBgColor(Color.CYAN);
        addSlide(AppIntroFragment.newInstance(sliderPage1));

        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle("Clean App Intros");
        sliderPage2.setDescription("This library \n offers developers the \n ability to add clean app \n intros at the start of their apps.");
        sliderPage2.setImageDrawable(R.mipmap.ic_slide2);
        sliderPage2.setBgColor(Color.DKGRAY);
        addSlide(AppIntroFragment.newInstance(sliderPage2));

        SliderPage sliderPage3 = new SliderPage();
        sliderPage3.setTitle("Simple, yet Customizable");
        sliderPage3.setDescription("The library offers a lot of customization, while keeping it simple for those that like simple.");
        sliderPage3.setImageDrawable(R.mipmap.ic_slide3);
        sliderPage3.setBgColor(Color.YELLOW);
        addSlide(AppIntroFragment.newInstance(sliderPage3));

        SliderPage sliderPage4 = new SliderPage();
        sliderPage4.setTitle("Explore");
        sliderPage4.setDescription("Feel free to explore the rest of the library demo!");
        sliderPage4.setImageDrawable(R.mipmap.ic_slide4);
        sliderPage4.setBgColor(Color.DKGRAY);
        addSlide(AppIntroFragment.newInstance(sliderPage4));

        setSkipText(getResources().getString(R.string.intro_skip));
        setDoneText(getResources().getString(R.string.intro_done));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        start();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        start();
    }

    private void start() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(INTRO_READ, true);
        editor.apply();
        startActivity(new Intent(this, TaoKeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
}
