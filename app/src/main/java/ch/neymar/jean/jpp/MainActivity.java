package ch.neymar.jean.jpp;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static int[] IMAGES = {
            R.drawable.ic_keyboard,
            R.drawable.ic_cup,
            R.drawable.ic_news
    };

    private List<View> jppImages = new ArrayList<>();


    private class JppRemover implements Animation.AnimationListener{
        private RelativeLayout frame;
        private View item;

        private JppRemover(RelativeLayout frame, View item){
            this.frame = frame;
            this.item = item;
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            frame.post(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
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

    private void addJppText(int x, int y,final RelativeLayout frame){
        final TextView jppText = (TextView) LayoutInflater.from(this).inflate(R.layout.jpp_text, null);
        frame.addView(jppText);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) jppText.getLayoutParams();
        params.leftMargin = x;
        params.topMargin = y;

        Animation jpp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.jpp);
        jpp.setAnimationListener(new JppRemover(frame,jppText));
        jppText.startAnimation(jpp);
    }

    private Drawable createItemImage(){
        Random rnd = new Random();
        int index = rnd.nextInt(IMAGES.length);
        return getResources().getDrawable(IMAGES[index], null);
    }

    private void setupItem(final RelativeLayout frame, final View jppImage){
        jppImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    jppImage.setOnTouchListener(null);
                    jppImages.remove(jppImage);
                    makeItemFly(frame, jppImage);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) jppImage.getLayoutParams();
                    addJppText((int)event.getX()+params.leftMargin , (int)event.getY() + params.topMargin, frame);
                }
                return true;
            }
        });
    }

    private View createItem(RelativeLayout frame, Drawable item){
        final ImageView jppImage = (ImageView) LayoutInflater.from(this).inflate(R.layout.jpp_item, null);
        setupItem(frame, jppImage);
        jppImage.setImageDrawable(item);
        frame.addView(jppImage);

        return jppImage;
    }

    private void placeItem(RelativeLayout frame, View jppImage){
        Random rnd = new Random();
        int x = rnd.nextInt(frame.getWidth()- jppImage.getWidth());
        int y = rnd.nextInt(frame.getHeight() - jppImage.getHeight());
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) jppImage.getLayoutParams();
        params.leftMargin = x;
        params.topMargin = y;
    }

    private void makeItemFly(RelativeLayout frame, View item){
        Animation jpp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fly);
        jpp.setAnimationListener(new JppRemover(frame,item));
        item.startAnimation(jpp);
    }

    private void initItems(RelativeLayout frame){
        Random rnd = new Random();
        int max = rnd.nextInt(40)+10;
        for(int i=0;i<max;++i){
            jppImages.add(createItem(frame, createItemImage()));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RelativeLayout mainView = (RelativeLayout)findViewById(R.id.mainframe);
        initItems(mainView);

        //in onCreate the size of the elements is not yet known
        final View content = findViewById(android.R.id.content);
        content.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                content.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                for(View jppImage : jppImages){
                    placeItem(mainView, jppImage);
                }
                mainView.requestLayout();
            }
        });


        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    addJppText((int)event.getX(), (int)event.getY(), mainView);
                }
                return true;
            }
        });

    }
}
