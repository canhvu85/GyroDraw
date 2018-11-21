package ch.epfl.sweng.SDP.utils;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import ch.epfl.sweng.SDP.Activity;
import ch.epfl.sweng.SDP.R;
import ch.epfl.sweng.SDP.home.BounceInterpolator;
import ch.epfl.sweng.SDP.home.HomeActivity;

import static ch.epfl.sweng.SDP.utils.Preconditions.checkPrecondition;

public class AnimUtils {

    private static final int MAIN_FREQUENCY = 10;
    private static final double MAIN_AMPLITUDE = 0.1;

    private AnimUtils() {
    }

    public static int getMainFrequency() {
        return MAIN_FREQUENCY;
    }

    public static double getMainAmplitude() {
        return MAIN_AMPLITUDE;
    }

    /**
     * Bounce the given view.
     *
     * @param view      the view
     * @param amplitude the amplitude of the bounce
     * @param frequency the frequency of the bounce
     * @param animMode  the animation mode
     * @param context   the context of the view
     */
    public static void bounceButton(final View view, double amplitude, int frequency,
                                    AnimMode animMode, Context context) {
        checkPrecondition(amplitude != 0, "The amplitude should not be zero");
        final Animation bounce =
                AnimationUtils.loadAnimation(context, AnimMode.getBounceAnimId(animMode));
        BounceInterpolator interpolator = new BounceInterpolator(amplitude, frequency);
        bounce.setInterpolator(interpolator);
        view.startAnimation(bounce);
    }

    /**
     * Bounce the given view with default amplitude and default frequency.
     *
     * @param view    the view
     * @param context the context of the view
     */
    public static void bounceButton(final View view, Context context) {
        final Animation bounce = AnimationUtils.loadAnimation(context, R.anim.bounce);
        BounceInterpolator interpolator = new BounceInterpolator(MAIN_AMPLITUDE, MAIN_FREQUENCY);
        bounce.setInterpolator(interpolator);
        view.startAnimation(bounce);
    }

    /**
     * Press the given view.
     *
     * @param view     the view
     * @param animMode the animation mode
     * @param context  the context of the view
     */
    public static void pressButton(View view, AnimMode animMode, Context context) {
        final Animation press =
                AnimationUtils.loadAnimation(context, AnimMode.getPressAnimId(animMode));
        press.setFillAfter(true);
        view.startAnimation(press);
    }

    /**
     * Sets listener and animation for exit button.
     */
    public static void setExitListener(final View exitButton, final Activity activity) {
        exitButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        pressButton(exitButton, AnimMode.CENTER, activity);
                        break;
                    case MotionEvent.ACTION_UP:
                        bounceButton(view, HomeActivity.MAIN_AMPLITUDE, HomeActivity.MAIN_FREQUENCY,
                                AnimMode.CENTER, activity);
                        activity.launchActivity(HomeActivity.class);
                        break;
                    default:
                }
                return true;
            }
        });
    }

    public enum AnimMode {
        CENTER, LEFT, RIGHT;

        /**
         * Converts an AnimMode to an animation id.
         *
         * @param animMode the requested animation mode
         * @return the matching id
         */
        public static int getPressAnimId(AnimMode animMode) {
            switch (animMode) {
                case CENTER:
                    return R.anim.press;
                case RIGHT:
                    return R.anim.press_right;
                case LEFT:
                    return R.anim.press_left;
            }
            return R.anim.press;
        }

        /**
         * Converts an AnimMode to an animation id.
         *
         * @param animMode the requested animation mode
         * @return the matching id
         */
        public static int getBounceAnimId(AnimMode animMode) {
            switch (animMode) {
                case CENTER:
                    return R.anim.bounce;
                case RIGHT:
                    return R.anim.bounce_right;
                case LEFT:
                    return R.anim.bounce_left;
            }
            return R.anim.bounce;
        }
    }
}
