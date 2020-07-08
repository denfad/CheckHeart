package ru.denfad.cheackheart.services;

import android.os.CountDownTimer;

import com.chinodev.androidneomorphframelayout.NeomorphFrameLayout;

public class Animator {

    public static void buttonPress(final NeomorphFrameLayout frame){
        frame.switchShadowType();
        CountDownTimer timer = new CountDownTimer(80,80) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                frame.switchShadowType();
            }
        };
        timer.start();
    }
}
