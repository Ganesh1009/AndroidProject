package de.innocow.innocow_v001.utilities.views.recycler_views;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import de.innocow.innocow_v001.R;
import de.innocow.innocow_v001.activities.OnDisplayAvailableTagListener;
import de.innocow.innocow_v001.activities.OnDisplayCowDetailsListener;
import de.innocow.innocow_v001.activities.OnTagUnequipClickListener;
import de.innocow.innocow_v001.pojo.cowsearch.CowValues;
import de.innocow.innocow_v001.utilities.string_constants.StringConstants;

public class RecyclerViewAdapter
        extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>
        implements Filterable {

    private Context context;
    private List<CowValues> cowValuesList;
    private OnDisplayAvailableTagListener onDisplayAvailableTagListener;
    private OnTagUnequipClickListener onTagUnequipClickListener;
    private OnDisplayCowDetailsListener onDisplayCowDetailsClickListener;
    private String searchString;

    public RecyclerViewAdapter(Context context, List<CowValues> cowValuesList, String searchString) {
        this.context = context;
        this.cowValuesList = cowValuesList;
        this.onDisplayAvailableTagListener = (OnDisplayAvailableTagListener) context;
        this.onTagUnequipClickListener = (OnTagUnequipClickListener) context;
        this.onDisplayCowDetailsClickListener = (OnDisplayCowDetailsListener) context;
        this.searchString = searchString;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_view_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {
        holder.cowNameView.setText("");
        holder.cowIdView.setText("");
        holder.cowTagIdView.setText("");
        holder.cowShedNumber.setText("");
        final CowValues cowValue = cowValuesList.get(position);
        String cowName = "", cowId = "", cowTagId = "";
        Integer shedNumber = null;

        if (StringUtils.isNotBlank(cowValue.getName())) {
            holder.cowNameView.setText(cowValue.getName());
            cowName = cowValue.getName();
        }
        if (StringUtils.isNotBlank(cowValue.getCowId())) {
            holder.cowIdView.setText(cowValue.getCowId());
            cowId = cowValue.getCowId();
        }
        if (StringUtils.isNotBlank(cowValue.getTagId())) {
            holder.cowTagIdView.setText(cowValue.getTagId());
            cowTagId = cowValue.getTagId();
        }
        if (cowValue.getShedNumber() != null) {
            holder.cowShedNumber.setText(String.valueOf(cowValue.getShedNumber()));
            shedNumber = cowValue.getShedNumber();
        }

        if ((StringUtils.isNotBlank(cowName) && StringUtils.containsIgnoreCase(cowName, searchString))) {
            holder.cowNameView.setText(makeSearchTextSpannable(cowName));
        }

        if (StringUtils.isNotBlank(cowId) && StringUtils.containsIgnoreCase(cowId, searchString)) {
            holder.cowIdView.setText(makeSearchTextSpannable(cowId));
        }

        if (StringUtils.isNotBlank(cowTagId) && StringUtils.containsIgnoreCase(cowTagId, searchString)) {
            holder.cowTagIdView.setText(makeSearchTextSpannable(cowTagId));
        }

        if (shedNumber != null && StringUtils.containsIgnoreCase(String.valueOf(shedNumber),
                searchString)) {
            holder.cowShedNumber.setText(makeSearchTextSpannable(String.valueOf(shedNumber)));
        }

        if (StringUtils.isBlank(cowValue.getTagId())) {

            holder.cowStatusImageView.setImageResource(R.drawable.kopf_rund);
            holder.cowTagImageView.setImageResource(R.drawable.ic_halsband_icon_equip_green);
            holder.cowTagImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDisplayAvailableTagListener.onDisplayAvailableTagClick(cowValuesList.get(position));

                }
            });
        } else {
            holder.cowStatusImageView.setImageResource(R.drawable.ic_healthy);
            holder.cowTagImageView.setImageResource(R.drawable.ic_halsband_icon_unequipped_red);
            holder.cowTagImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MaterialAlertDialogBuilder alertBuilder = new MaterialAlertDialogBuilder(context, R.style.CustomMaterialTheme);
                    alertBuilder.setTitle(StringConstants.MSG_UNEQUIP_TAG_FROM_COW)
                            .setNegativeButton(StringConstants.CANCEL, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton(StringConstants.ACCEPT, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onTagUnequipClickListener.onTagUnequipClick(cowValuesList.get(position));
                                }
                            }).create().show();

                }
            });
            if (StringUtils.equals(cowValue.getHeatStatus(), StringConstants.IN_HEAT)) {
                holder.cowStatusImageView.setImageResource(R.drawable.ic_cow_heat);
            }
        }

        holder.cowStatusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onDisplayCowDetailsClickListener.onDisplayCowDetailsClickListener(cowValuesList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        if (cowValuesList != null)
            return cowValuesList.size();

        return 0;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    private Spannable makeSearchTextSpannable(String viewString) {
        int startPosition, endPosition;
        startPosition = viewString.toLowerCase().indexOf(searchString.toLowerCase());
        endPosition = startPosition + searchString.length();
        Spannable spannableText = Spannable.Factory.getInstance().newSpannable(viewString);
        spannableText.setSpan(new ForegroundColorSpan(Color.parseColor("#6AA526"))
                , startPosition
                , endPosition
                , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableText;
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView cowNameView;
        TextView cowIdView;
        TextView cowTagIdView;
        TextView cowShedNumber;
        ImageView cowStatusImageView;
        ImageView cowTagImageView;


        RecyclerViewHolder(@NonNull View view) {
            super(view);
            cowNameView = view.findViewById(R.id.cowNameTextView);
            cowIdView = view.findViewById(R.id.cowIdTextView);
            cowTagIdView = view.findViewById(R.id.cowTagIdTextView);
            cowShedNumber = view.findViewById(R.id.cowShedNumber);
            cowStatusImageView = view.findViewById(R.id.rec_img_cow_pos_nav);
            cowTagImageView = view.findViewById(R.id.cowTagImageViewGreen);
            cowTagImageView.bringToFront();
            cowTagImageView.setClickable(true);
        }
    }
}
