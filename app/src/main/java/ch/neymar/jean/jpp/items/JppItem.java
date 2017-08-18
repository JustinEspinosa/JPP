package ch.neymar.jean.jpp.items;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ch.neymar.jean.jpp.MainActivity;
import ch.neymar.jean.jpp.R;
import ch.neymar.jean.jpp.text.JppText;
import ch.neymar.jean.jpp.utils.ViewRemover;

/**
 *
 * Represents an item to be jpp-ized
 *
 * Created by justi on 18.08.2017.
 */

public class JppItem {
    private ImageView imageView;
    private Drawable drawable;
    private MainActivity activity;

    /**
     * @param drawable : the image of the item
     * @param activity : the main activity
     */
    public JppItem(Drawable drawable, MainActivity activity){
        this.drawable = drawable;
        this.activity = activity;
    }

    /**
     * Returns the ImageView UI component and generates it if needed
     *
     * @return the imageview component
     */
    public ImageView getImageView(){
        if(imageView == null){
            imageView = createImageView();
        }
        return imageView;
    }

    private ViewGroup getParent(){
        return (ViewGroup)imageView.getParent();
    }

    private int getBestAnimation(){
        int x = getX();
        int y = getY();
        int w = getParent().getWidth();
        int h = getParent().getHeight();

        if(y > h / 2){
            if(x > w / 2) {
                return R.anim.fly_up_left;
            }else{
                return R.anim.fly_up_right;
            }
        }else{
            if(x > w / 2) {
                return R.anim.fly_down_left;
            }else{
                return R.anim.fly_down_right;
            }
        }
    }

    public int getX(){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        return params.leftMargin;
    }

    public int getY(){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        return params.topMargin;
    }

    protected void animate(){
        getParent().bringChildToFront(imageView);
        Animation jpp = AnimationUtils.loadAnimation(activity.getApplicationContext(), getBestAnimation());
        jpp.setAnimationListener(new ViewRemover(activity,imageView));
        imageView.startAnimation(jpp);
    }

    private void setupListener(){
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    imageView.setOnTouchListener(null);
                    animate();
                    new JppText(activity).start((int)event.getX()+getX(), (int)event.getY()+getY());
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
