package ch.neymar.jean.jpp.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.neymar.jean.jpp.utils.Utils;

/**
 * Used to randomly position the items on the screen.
 * The items cannot be positionned in the activity's onCreate method because the bounds of the
 * component are not yet known
 *
 * Created by justi on 18.08.2017.
 */

public class ItemPositioner {
    private final RelativeLayout frame;
    private final List<JppItem> items = new ArrayList<>();

    public ItemPositioner(RelativeLayout frame){
        this.frame = frame;
    }

    public void addItem(JppItem item){
        synchronized (items) {
            items.add(item);
        }
    }

    public void positionItems(){
        Random rnd = new Random();

        synchronized (items) {
            for (JppItem item : items) {
                ImageView view = item.getImageView();
                int x = rnd.nextInt(frame.getWidth() - view.getWidth());
                int y = rnd.nextInt(frame.getHeight() - view.getHeight());
                Utils.positionView(view,x,y);
            }
            items.clear();
        }
    }

}
