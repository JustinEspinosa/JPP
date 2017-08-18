package ch.neymar.jean.jpp.utils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

/**
 * Used to delete an item after its animation i finished
 *
 * Created by justi on 18.08.2017.
 */
public class ViewRemover implements Animation.AnimationListener {
    private View item;
    private Activity activity;

    public ViewRemover(Activity activity, View item) {
        this.item = item;
        this.activity = activity;
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        final ViewGroup frame = (ViewGroup)item.getParent();
        frame.post(new Runnable() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        frame.removeView(item);
                    }
                });
            }
        });

    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }
}
