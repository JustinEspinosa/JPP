package ch.neymar.jean.jpp.utils;

import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by justi on 18.08.2017.
 */

public class Utils {
    public static void positionView(View view, int x , int y){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.leftMargin = x;
        params.topMargin = y;
    }
}
