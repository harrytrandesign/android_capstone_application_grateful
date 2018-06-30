package com.htdwps.grateful.Adapter;

import android.content.Context;
import android.support.annotation.IntRange;
import android.view.Gravity;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;
import com.beloo.widget.chipslayoutmanager.layouter.breaker.IRowBreaker;

/**
 * Created by HTDWPS on 6/29/18.
 */
public class ChipLayoutManager {

    private static ChipsLayoutManager chipsLayoutManager;

    public static ChipsLayoutManager createLayoutManager(Context context) {

        chipsLayoutManager = ChipsLayoutManager.newBuilder(context)
                //set vertical gravity for all items in a row. Default = Gravity.CENTER_VERTICAL
                .setChildGravity(Gravity.START)
                //whether RecyclerView can scroll. TRUE by default
                .setScrollingEnabled(true)
                //set maximum views count in a particular row
                .setMaxViewsInRow(10)
                //set gravity resolver where you can determine gravity for item in position.
                //This method have priority over previous one
                .setGravityResolver(new IChildGravityResolver() {
                    @Override
                    public int getItemGravity(int position) {
                        return Gravity.START;
                    }
                })
                //you are able to break row due to your conditions. Row breaker should return true for that views
                .setRowBreaker(new IRowBreaker() {
                    @Override
                    public boolean isItemBreakRow(@IntRange(from = 0) int position) {
                        return position == -1;
                    }
                })
                //a layoutOarientation of layout manager, could be VERTICAL OR HORIZONTAL. HORIZONTAL by default
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                // row strategy for views in completed row, could be STRATEGY_DEFAULT, STRATEGY_FILL_VIEW,
                //STRATEGY_FILL_SPACE or STRATEGY_CENTER
                .setRowStrategy(ChipsLayoutManager.STRATEGY_FILL_VIEW)
                // whether strategy is applied to last row. FALSE by default
                .withLastRow(true)
                .build();

        return chipsLayoutManager;

    }

}
