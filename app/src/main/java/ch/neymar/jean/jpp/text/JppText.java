package ch.neymar.jean.jpp.text;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import ch.neymar.jean.jpp.MainActivity;
import ch.neymar.jean.jpp.R;
import ch.neymar.jean.jpp.utils.Utils;
import ch.neymar.jean.jpp.utils.ViewRemover;

/**
 * Represents the text JPP displayed when the user taps anywhere on the screen
 * Created by justi on 18.08.2017.
 */

public class JppText {
    private static Typeface font;

    private final TextView textView;
    private final Animation jppAnimation;
    private final MainActivity activity;

    public JppText(MainActivity activity){
        this.activity = activity;

        textView = (TextView) LayoutInflater.from(activity).inflate(R.layout.jpp_text, null);
        textView.setTypeface(getFont(activity));
        jppAnimation = AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.jpp);
        jppAnimation.setAnimationListener(new ViewRemover(activity,textView));

    }

    private static Typeface getFont(Activity activity){
        if(font == null) {
            font = Typeface.createFromAsset(activity.getAssets(), "256_bytes.ttf");
        }
        return font;
    }

    public void start(int x, int y){
        activity.getMainView().addView(textView);
        Utils.positionView(textView, x, y);
        textView.startAnimation(jppAnimation);
    }
}
