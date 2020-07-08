package ru.denfad.cheackheart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Timer;
import java.util.TimerTask;

import ru.denfad.cheackheart.services.Animator;

public class PulseFragment extends Fragment {

    private AnimTimer animTimer;
    private ImageView heart;
    private Animation animation;
    public PulseFragment(){}

    public static PulseFragment newInstance(){
        return new PulseFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.pulse_fragment, container, false);
        heart = rootView.findViewById(R.id.image_heart);

        //анимация сердца
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.heart_anim);
        heart.startAnimation(animation);
        animTimer = new AnimTimer(1800,1800);
        animTimer.start();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        heart.startAnimation(animation);
        animTimer.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        animTimer.cancel();
    }


    private class AnimTimer  extends CountDownTimer{

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public AnimTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            heart.startAnimation(animation);
        }

        @Override
        public void onFinish() {
            animTimer.start();
        }
    }
}
