package de.innocow.innocow_v001.utilities.views.recycler_views;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import de.innocow.innocow_v001.R;
import de.innocow.innocow_v001.activities.CowDetails;
import de.innocow.innocow_v001.activities.OnRecyclerViewCowFilter;
import de.innocow.innocow_v001.pojo.barn_details.RelativeCowPosition;
import de.innocow.innocow_v001.pojo.cowsearch.CowValues;
import de.innocow.innocow_v001.utilities.string_constants.StringConstants;

public class RecyclerViewCowPosNav
        extends RecyclerView.Adapter<RecyclerViewCowPosNav.RecyclerViewHolder> {

    private Context context;
    private List<RelativeCowPosition> relativeCowPositionList;
    private OnRecyclerViewCowFilter onRecyclerViewCowFilter;

    public RecyclerViewCowPosNav(Context context, List<RelativeCowPosition> relativeCowPositionList) {
        this.context = context;
        this.relativeCowPositionList = relativeCowPositionList;
        this.onRecyclerViewCowFilter = (OnRecyclerViewCowFilter) context;
    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_cow_pos_nav, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {

        holder.textView.setText("");

        if (relativeCowPositionList != null) {

            RelativeCowPosition cowPosition = relativeCowPositionList.get(position);

            if (StringUtils.isNotBlank(cowPosition.getShedNumber()))
                holder.textView.setText(cowPosition.getShedNumber());
            else if (StringUtils.isNotBlank(cowPosition.getName()))
                holder.textView.setText(cowPosition.getName());
            else if (StringUtils.isNotBlank(cowPosition.getTagId()))
                holder.textView.setText(cowPosition.getTagId());

            if (StringUtils.isNotBlank(cowPosition.getStatus()))
                if (StringUtils.equalsIgnoreCase(cowPosition.getStatus(), StringConstants.NOT_IN_HEAT))
                    holder.imageView.setImageResource(R.drawable.ic_healthy);

            if (StringUtils.equalsIgnoreCase(cowPosition.getStatus(), StringConstants.IN_HEAT))
                holder.imageView.setImageResource(R.drawable.ic_cow_heat);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecyclerViewCowFilter.
                        filterOnCowSelectedFromRecyclerView(relativeCowPositionList.get(position));
            }
        });

        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Intent intent = new Intent(context, CowDetails.class);
                CowValues cowValues = new CowValues();
                cowValues.setTagId(relativeCowPositionList.get(position).getTagId());
                cowValues.setCowId(relativeCowPositionList.get(position).getCowId());
                cowValues.setName(relativeCowPositionList.get(position).getName());
                cowValues.setShedNumber(Integer.valueOf(relativeCowPositionList.get(position).getShedNumber()));
                cowValues.setHeatStatus(relativeCowPositionList.get(position).getStatus());
                intent.putExtra(StringConstants.COW_VALUE, cowValues);
                context.startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (relativeCowPositionList != null)
            return relativeCowPositionList.size();
        return 1;
    }

//    public void DialogBox() {
//        Dialog dialog = new Dialog(context, R.style.CustomMaterialTheme);
//        dialog.setContentView(R.layout.dialog_cow_pos_nav);
//        TextView viewCowDetails = dialog.findViewById(R.id.view_cow_details);
//        viewCowDetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //TODO: Go to CowDetails page
//            }
//
//        });
//    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.rec_cow_name_pos_nav);
            imageView = itemView.findViewById(R.id.rec_img_cow_pos_nav);
        }
    }
}
