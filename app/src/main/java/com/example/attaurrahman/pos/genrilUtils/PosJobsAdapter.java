package com.example.attaurrahman.pos.genrilUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.attaurrahman.pos.R;
import com.example.attaurrahman.pos.activity.MainActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AttaUrRahman on 4/9/2018.
 */

public class PosJobsAdapter extends RecyclerView.Adapter<PosJobsAdapter.ViewHolder> {

    private List<PosHelper> posHelpers;
    private Context context;
    String strLat, strLon, strJobId;


    public PosJobsAdapter(List<PosHelper> posHelpers, Activity context) {
        this.posHelpers = posHelpers;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_mission_screen, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        PosHelper posHelper = posHelpers.get(position);
        holder.tvMissionScreenTitle.setText(posHelper.getStrJobTitle());
        holder.tvMissionScreenDescription.setText(posHelper.getStrDescription());
        holder.tvMissionTime.setText(posHelper.getStrTime());
        Glide.with(context).load(posHelper.getStrPosImage()).into(holder.ivMissionImage);


        holder.ivMissionMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utilities.putValueInEditor(context).putString("job_id", posHelpers.get(position).getStrJobId()).commit();
                Utilities.putValueInEditor(context).putString("lat", posHelpers.get(position).getStrLatitude()).commit();
                Utilities.putValueInEditor(context).putString("lon", posHelpers.get(position).getStrLongitude()).commit();
                Utilities.putValueInEditor(context).putString("title", posHelpers.get(position).getStrJobTitle()).commit();
                Utilities.putValueInEditor(context).putString("description", posHelpers.get(position).getStrDescription()).commit();


                ((MainActivity) context).intent();


            }
        });

    }

    @Override
    public int getItemCount() {
        return posHelpers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView ivMissionImage;
        ImageView ivMissionMap;
        TextView tvMissionScreenTitle, tvMissionScreenDescription, tvMissionTime;

        public ViewHolder(View itemView) {
            super(itemView);

            ivMissionImage = itemView.findViewById(R.id.iv_mission_image);
            tvMissionScreenTitle = itemView.findViewById(R.id.tv_mission_screen_title);
            tvMissionScreenDescription = itemView.findViewById(R.id.tv_mission_screen_text);
            tvMissionTime = itemView.findViewById(R.id.tv_mission_screen_time);
            ivMissionMap = itemView.findViewById(R.id.iv_mission_map);

        }
    }
}
