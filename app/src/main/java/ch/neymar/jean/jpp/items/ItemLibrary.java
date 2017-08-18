package ch.neymar.jean.jpp.items;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import ch.neymar.jean.jpp.MainActivity;

/**
 *
 *  Used to manage the items that can be placed on the screen to be jpp-ized
 *
 * Created by justi on 18.08.2017.
 */
public class ItemLibrary {
    private Map<Integer, Drawable> images = new TreeMap<>();
    private List<Integer> imageIds = new ArrayList<>();

    private MainActivity activity;

    public ItemLibrary(MainActivity activity){
        this.activity = activity;
    }

    protected Drawable createDrawable(int id){
        return activity.getResources().getDrawable(id, null);
    }

    private Drawable getDrawable(int id){
        Drawable drawable = images.get(id);
        if(drawable == null){
            drawable = createDrawable(id);
            images.put(id, drawable);
        }
        return drawable;
    }

    public void addItem(int id){
        imageIds.add(id);
    }

    private Drawable getRandomImage(){
        if(imageIds.isEmpty()){
            return null;
        }

        Random rnd = new Random();
        return getDrawable(imageIds.get(rnd.nextInt(imageIds.size())));
    }

    public JppItem getRandomItem(){
        return new JppItem(getRandomImage(),activity);
    }


}
