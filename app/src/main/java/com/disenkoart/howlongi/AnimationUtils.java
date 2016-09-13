package com.disenkoart.howlongi;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Артём on 11.09.2016.
 */
public class AnimationUtils {

    /**
     * Анимация заполнения прогресс-бара.
     * @param view View прогресс-бара.
     * @param progressValue Текщее значение прогресс-бара.
     * @param previousProgressValue Предыдущее значение прогресс-бара.
     * @param progressSetter Имя метода, устанавливающего значение прогресса.
     * @return
     */
    public static AnimatorSet createProgressBarAnimation(View view, float progressValue,
                                                         float previousProgressValue, String progressSetter){
        AnimatorSet animatorSet = new AnimatorSet();
        List<Animator> animatorList = new ArrayList<>(1);
        animatorList.add(createProgressAnimator(view, progressValue, previousProgressValue, progressSetter));
        animatorSet.playTogether(animatorList);
        return animatorSet;
    }

    /**
     * Создает аниматор заполнения прогресс-бара
     * @param view View прогресс-бара.
     * @param progressValue Текщее значение прогресс-бара.
     * @param previousProgressValue Предыдущее значение прогресс-бара.
     * @param progressSetter Имя метода, устанавливающего значение прогресса.
     * @return
     */
    private static ObjectAnimator createProgressAnimator(View view, float progressValue, float previousProgressValue, String progressSetter){
        ObjectAnimator shapeProgressAnimator = ObjectAnimator.ofFloat(view, progressSetter, previousProgressValue, progressValue);
        shapeProgressAnimator.setDuration(1000);
        return shapeProgressAnimator;
    }

    /**
     * Создаёт анимацию движения View по оси X.
     * @param prevX Предыдущее значение позиции по оси X.
     * @param newX Новое значение позиции по оси X.
     * @param duration Время выполнения анимации.
     * @param view View для анимирования.
     * @return
     */
    public static AnimatorSet createXAnimation(float prevX, float newX, long duration, View view) {
        Log.d(String.valueOf(prevX), String.valueOf(newX));
        AnimatorSet animatorSet = new AnimatorSet();
        Animator animatorList = createXAnimator(prevX, newX, duration, view);
        animatorSet.playTogether(animatorList);
        return animatorSet;
    }

    /**
     * Создает аниматор движения View по оси X.
     * @param prevX Предыдущее значение позиции по оси X.
     * @param newX Новое значение позиции по оси X.
     * @param duration Время выполнения анимации.
     * @param view View для анимирования.
     * @return
     */
    private static ObjectAnimator createXAnimator(float prevX, float newX, long duration, View view) {
        ObjectAnimator xTranslate = ObjectAnimator.ofFloat(view, "translationX", prevX, newX);
        xTranslate.setDuration(duration);
        return xTranslate;
    }

    /**
     * Создает анимацию изменения прозрачности View.
     * @param view View, у которого будет меняться прозрачность.
     * @param startAlpha Начальное значение прозрачности View.
     * @param finishAlpha Конечное значение прозрачности View.
     * @param duration Время выполнения анимации.
     * @return
     */
    public static AnimatorSet createAlphaAnimation(View view, float startAlpha, float finishAlpha, long duration) {
        AnimatorSet animatorSet = new AnimatorSet();
        Animator animator = ObjectAnimator.ofFloat(view, "alpha", startAlpha, finishAlpha);
        animator.setDuration(duration);
        animatorSet.playTogether(animator);
        return animatorSet;
    }

}
