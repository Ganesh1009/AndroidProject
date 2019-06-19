package de.innocow.innocow_v001.utilities.views.recycler_views;


import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.innocow.innocow_v001.R;
import de.innocow.innocow_v001.activities.OnTagEquipClickListener;
import de.innocow.innocow_v001.utilities.string_constants.StringConstants;


public class RecyclerViewTagWaehlen extends RecyclerView.Adapter<RecyclerViewTagWaehlen.RecyclerViewHolder> {

    private Context context;
    private List<String> tagList;
    private OnTagEquipClickListener onTagEquipClickListener;

    public RecyclerViewTagWaehlen(Context context, List<String> unequippedTagList) {
        this.context = context;
        this.tagList = unequippedTagList;
        this.onTagEquipClickListener = (OnTagEquipClickListener) context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_tag_waehlen, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlertBuilder(holder);
            }
        });

        holder.textView.setText(tagList.get(position));
        holder.imageView.setImageResource(R.drawable.ic_tag2);
        holder.imageView.animate();
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlertBuilder(holder);
            }
        });
        holder.textView.animate();
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertBuilder(holder);
            }
        });

    }

    private void createAlertBuilder(final RecyclerViewHolder holder) {

        MaterialAlertDialogBuilder alertBuilder = new MaterialAlertDialogBuilder(context, R.style.CustomMaterialTheme);
        alertBuilder.setTitle(StringConstants.MSG_EQUIP_TAG_TO_COW)
                .setNegativeButton(StringConstants.CANCEL, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(StringConstants.ACCEPT, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onTagEquipClickListener.tagEquipClick(holder.textView.getText().toString());
                    }
                }).create();
        alertBuilder.show();

    }


    @Override
    public int getItemCount() {
        return tagList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        final View view;
        final TextView textView;
        final ImageView imageView;

        public RecyclerViewHolder(@NonNull View view) {
            super(view);
            this.view = view;
            textView = view.findViewById(R.id.tv_tag_list);
            imageView = view.findViewById(R.id.rec_img_cow_pos_nav);
        }
    }
}