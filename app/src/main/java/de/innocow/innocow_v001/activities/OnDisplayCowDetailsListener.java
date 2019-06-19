package de.innocow.innocow_v001.activities;

import android.widget.ImageView;
import android.widget.TextView;

import de.innocow.innocow_v001.pojo.cowsearch.CowValues;
import de.innocow.innocow_v001.utilities.views.recycler_views.RecyclerViewAdapter;


public interface OnDisplayCowDetailsListener {

    void onDisplayCowDetailsClickListener(CowValues cowValues);
}
