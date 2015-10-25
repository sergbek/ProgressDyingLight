package com.example.sergbek.cpb_3;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;


public final class AnimatorAdapter extends AnimatorListenerAdapter {

    private AnimatorSet mAnimatorSet;

    public AnimatorAdapter(AnimatorSet animatorSet) {
        mAnimatorSet = animatorSet;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        mAnimatorSet.start();
    }
}
