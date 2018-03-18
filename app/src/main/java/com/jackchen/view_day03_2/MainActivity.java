package com.jackchen.view_day03_2;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final QQStepView step_view = (QQStepView) findViewById(R.id.step_view);
        step_view.setStepMax(4000);

        // 属性动画，让圆弧动起来
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, 3000);
        valueAnimator.setDuration(1000) ;
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 不断获取当前角度，不断设置当前角度，不断重新去绘制
                float currentStep = (float) animation.getAnimatedValue();
                step_view.setCurrentStep((int) currentStep);
            }
        });
        valueAnimator.start();
    }
}
