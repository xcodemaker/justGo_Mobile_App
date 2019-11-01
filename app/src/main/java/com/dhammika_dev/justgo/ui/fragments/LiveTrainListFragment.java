package com.dhammika_dev.justgo.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhammika_dev.justgo.BaseApplication;
import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.dto.ScheduleList;
import com.dhammika_dev.justgo.ui.activity.AuthActivity;
import com.dhammika_dev.justgo.ui.activity.PassengerHomeActivity;
import com.dhammika_dev.justgo.ui.adapters.LiveTrainListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.constraint.Constraints.TAG;

public class LiveTrainListFragment extends BaseFragment implements LiveTrainListAdapter.OnNoteListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    FirebaseFirestore firestore;
    private RecyclerView.Adapter adapter;
    @BindView(R.id.live_list_recycler_view)
    RecyclerView recyclerView;
    private List<ScheduleList> listItems;
    Context ct;

    public LiveTrainListFragment() {
    }

    public static LiveTrainListFragment newInstance(String param1, String param2) {
        LiveTrainListFragment fragment = new LiveTrainListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    protected void initializePresenter() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = null;
        rootView = inflater.inflate(R.layout.fragment_live_train_list, container, false);
        ButterKnife.bind(this, rootView);
        firestore = FirebaseFirestore.getInstance();
        removedOldTrainData();
        listItems = new ArrayList<>();
        ct = getContext();
        return rootView;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void removedOldTrainData() {
        setLoading(true);
        firestore.collection("trains").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Date date = (Date) document.get("time");
                        if (date != null) {
                            Date now = new Date();
                            long diff = now.getTime() - date.getTime();
                            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
                            if (minutes > 30) {
                                list.add(document.getId());
                            }
                            System.out.println("=========    Time in minutes: " + minutes + " minutes.");
                        }
                    }
                    System.out.println("=============>>>>>>> list " + list.toString());
                    WriteBatch batch = firestore.batch();
                    for (String id : list) {
                        DocumentReference ref = firestore.collection("trains").document(id);
                        batch.delete(ref);
                    }
                    batch.commit();
                    getLiveTrain();
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void getLiveTrain() {
        firestore.collection("trains").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<ScheduleList> list = new ArrayList<>();
                    if(task.getResult().size()>0){
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ScheduleList item = new ScheduleList();
                            item.setOrigin(document.get("FromStation").toString());
                            item.setDestination(document.get("ToStation").toString());
                            item.setTrain(document.get("TrainName").toString());
                            item.setFrequency(document.getId());
                            list.add(item);
                        }
                        listItems.addAll(list);
                        setAdapter();
                    } else {
                        setLoading(false);
                        showAlertDialog(true, "Oopps!", "There are no live trains on the track", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ((PassengerHomeActivity) getActivity()).selectSpecificTab(0);
                            }
                        });
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void setAdapter() {
        setLoading(false);
        adapter = new LiveTrainListAdapter(getContext(), listItems, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(ct));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onNoteClick(int position) {

    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
