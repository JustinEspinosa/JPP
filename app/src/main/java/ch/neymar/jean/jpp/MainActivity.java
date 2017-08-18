package ch.neymar.jean.jpp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import java.util.Random;

import ch.neymar.jean.jpp.items.ItemLibrary;
import ch.neymar.jean.jpp.items.ItemPositioner;
import ch.neymar.jean.jpp.items.JppItem;
import ch.neymar.jean.jpp.text.JppText;

public class MainActivity extends AppCompatActivity {

    private static final int MIN_ITEMS = 10;
    private static final int MAX_ITEMS = 50;

    private final ItemLibrary itemLibrary = new ItemLibrary(this);
    private RelativeLayout mainView;

    public MainActivity(){
        itemLibrary.addItem(R.drawable.ic_keyboard);
        itemLibrary.addItem(R.drawable.ic_cup);
        itemLibrary.addItem(R.drawable.ic_news);
        itemLibrary.addItem(R.drawable.ic_card);
        itemLibrary.addItem(R.drawable.ic_terminal);
    }

    public RelativeLayout getMainView(){
        return mainView;
    }

    private void createItems(RelativeLayout frame, ItemPositioner positioner){
        Random rnd = new Random();
        int max = rnd.nextInt(MAX_ITEMS-MIN_ITEMS)+MIN_ITEMS;

        for(int i=0;i<max;++i){
            JppItem item = itemLibrary.getRandomItem();
            frame.addView(item.getImageView());
            positioner.addItem(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainView = (RelativeLayout)findViewById(R.id.mainframe);

        final ItemPositioner positioner = new ItemPositioner(mainView);
        createItems(mainView, positioner);

        //in onCreate the size of the elements is not yet known
        //so we position the items later using an event listener
        final View content = findViewById(android.R.id.content);
        content.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                content.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                positioner.positionItems();
                mainView.requestLayout();
            }
        });

        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    new JppText(MainActivity.this).start((int)event.getX(), (int)event.getY());
                }
                return true;
            }
        });

    }
}
