package com.github.caoyouxin.taoke.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.ui.fragment.SlideFragment;
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

        addSlide(SlideFragment.newInstance(R.layout.intro1));
        addSlide(SlideFragment.newInstance(R.layout.intro2));
        addSlide(SlideFragment.newInstance(R.layout.intro3));
        addSlide(SlideFragment.newInstance(R.layout.intro4));

        setSkipText(getResources().getString(R.string.intro_skip));
        setDoneText(getResources().getString(R.string.intro_done));
        setBarColor(getResources().getColor(R.color.trans));
        setSeparatorColor(getResources().getColor(R.color.trans));
        setColorDoneText(getResources().getColor(R.color.black_alpha_176));
        setColorSkipButton(getResources().getColor(R.color.black_alpha_176));
        setNextArrowColor(getResources().getColor(R.color.black_alpha_176));
        setIndicatorColor(getResources().getColor(R.color.black_alpha_176), getResources().getColor(R.color.black_alpha_64));
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
