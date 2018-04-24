package com.example.attaurrahman.pos.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.attaurrahman.pos.R;
import com.example.attaurrahman.pos.genrilUtils.API;
import com.example.attaurrahman.pos.genrilUtils.PosHelper;
import com.example.attaurrahman.pos.genrilUtils.PosJobsAdapter;
import com.example.attaurrahman.pos.genrilUtils.Utilities;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PosJobsFragment extends Fragment {
    View parentView;

    PosJobsAdapter posJobsAdapter;
    List<PosHelper> list;
    @BindView(R.id.rv_pos_jobs)
    RecyclerView rvPosJobs;
    @BindView(R.id.iv_jobs_mission)
    ImageView ivJobsMission;
    @BindView(R.id.iv_jobs_complete)
    ImageView ivJobsComplete;
    String strUrl,strArray;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_pos_jobs, container, false);
        ButterKnife.bind(this, parentView);

        rvPosJobs.setHasFixedSize(true);
        rvPosJobs.setLayoutManager(new LinearLayoutManager(getActivity()));

        ivJobsComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.connectFragmentWithOutBackStack(getActivity(), new CompleteJobsFragment());
            }
        });

        list = new ArrayList<>();
        posJobsAdapter = new PosJobsAdapter(list, getActivity());
        rvPosJobs.setAdapter(posJobsAdapter);
        strUrl = "jobs";
        strArray="jobs";
        API.PosJobs(getActivity(), strUrl,strArray, list, posJobsAdapter);

        return parentView;

    }


}
