package ch.neymar.jean.jpp.items;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ch.neymar.jean.jpp.MainActivity;
import ch.neymar.jean.jpp.R;
import ch.neymar.jean.jpp.text.JppText;
import ch.neymar.jean.jpp.utils.ViewRemover;

/**
 * Represents an item to be jpp-ized
 * <p>
 * Created by justi on 18.08.2017.
 */

public class JppItem {
    private final static float DISTANCE_MULTIPLYIER = 4;
    private final static long MAX_DURATION = 2000;
    private final static long MIN_DURATION = 300;

    private ImageView imageView;
    private Drawable drawable;
    private MainActivity activity;

    /**
     * @param drawable : the image of the item
     * @param activity : the main activity
     */
    public JppItem(Drawable drawable, MainActivity activity) {
        this.drawable = drawable;
        this.activity = activity;
    }

    /**
     * Returns the ImageView UI component and generates it if needed
     *
     * @return the imageview component
     */
    public ImageView getImageView() {
        if (imageView == null) {
            imageView = createImageView();
        }
        return imageView;
    }

    private ViewGroup getParent() {
        return (ViewGroup) imageView.getParent();
    }


    public int getX() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        return params.leftMargin;
    }

    public int getY() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        return params.topMargin;
    }

    protected void animate(float xDelta, float yDelta, long time) {
        getParent().bringChildToFront(imageView);
        Animation jpp = AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.fly);
        AnimationSet jppSet = (AnimationSet) jpp;

        float xDeltaPct = (xDelta / getParent().getWidth()) * DISTANCE_MULTIPLYIER;
        float yDeltaPct = (yDelta / getParent().getHeight()) * DISTANCE_MULTIPLYIER;

        TranslateAnimation translateAnimation = new TranslateAnimation( TranslateAnimation.RELATIVE_TO_PARENT, 0,
                                                                        TranslateAnimation.RELATIVE_TO_PARENT, xDeltaPct,
                                                                        TranslateAnimation.RELATIVE_TO_PARENT, 0,
                                                                        TranslateAnimation.RELATIVE_TO_PARENT, yDeltaPct);
        jppSet.addAnimation(translateAnimation);

        long duration = time;
        if(duration > MAX_DURATION){
            duration = MAX_DURATION;
        }
        if(duration< MIN_DURATION){
            duration = MIN_DURATION;
        }
        for(Animation anim : jppSet.getAnimations()){
            anim.setDuration(duration);
        }

        jpp.setAnimationListener(new ViewRemover(activity, imageView));
        imageView.startAnimation(jpp);
    }

    private void setupListener() {
        imageView.setOnTouchListener(new View.OnTouchListener() {
            float rawStartX = 0;
            float rawStartY = 0;
            long startTime;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        rawStartX = event.getRawX();
                        rawStartY = event.getRawY();
                        startTime = event.getEventTime();
                        break;
                    case MotionEvent.ACTION_UP:
                        imageView.setOnTouchListener(null);
                        animate( event.getRawX() - rawStartX,  event.getRawY() - rawStartY, event.getEventTime() - startTime);
                        new JppText(activity).start((int) event.getX() + getX(), (int) event.getY() + getY());
                        break;
                }
                return true;
            }
        });
    }

    protected ImageView createImageView() {
        imageView = (ImageView) LayoutInflater.from(activity).inflate(R.layout.jpp_item, null);
        setupListener();
        imageView.setImageDrawable(drawable);
        return imageView;
    }

}
