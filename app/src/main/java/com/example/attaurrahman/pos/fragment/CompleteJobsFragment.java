package com.example.attaurrahman.pos.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.attaurrahman.pos.R;
import com.example.attaurrahman.pos.genrilUtils.API;
import com.example.attaurrahman.pos.genrilUtils.PosHelper;
import com.example.attaurrahman.pos.genrilUtils.PosJobsAdapter;
import com.example.attaurrahman.pos.genrilUtils.Utilities;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompleteJobsFragment extends Fragment {

    View parentView;
    PosJobsAdapter posJobsAdapter;
    List<PosHelper> list;
    String strUrl, strArray;
    @BindView(R.id.iv_jobs_mission)
    ImageView ivJobsMission;
    @BindView(R.id.tv_complete)
    TextView tvComplete;
    @BindView(R.id.tv_mission)
    TextView tvMission;
    @BindView(R.id.rv_pos_jobs_complete)
    RecyclerView rvPosJobsComplete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_complete_jobs, container, false);
        ButterKnife.bind(this, parentView);
        ivJobsMission.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Utilities.connectFragmentWithOutBackStack(getActivity(), new PosJobsFragment());

            }
        });

        rvPosJobsComplete.setHasFixedSize(true);
        rvPosJobsComplete.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();
        posJobsAdapter = new PosJobsAdapter(list, getActivity());
        rvPosJobsComplete.setAdapter(posJobsAdapter);
        strUrl = "user_job_accepted";
        API.PosCompleteJobs(getActivity(), strUrl, list, posJobsAdapter);

        return parentView;
    }

}
