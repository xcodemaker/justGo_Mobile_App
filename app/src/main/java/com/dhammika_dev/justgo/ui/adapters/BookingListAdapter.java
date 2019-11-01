package com.dhammika_dev.justgo.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.dto.ScheduleList;
import com.dhammika_dev.justgo.dto.lankagate.TrainsList;
import com.dhammika_dev.justgo.model.entities.response.TrainScheduleResponse;
import com.dhammika_dev.justgo.ui.activity.PassengerHomeActivity;
import com.dhammika_dev.justgo.ui.fragments.TicketBookingConfirmFragment;

import java.util.List;

public class BookingListAdapter extends RecyclerView.Adapter<BookingListAdapter.ViewHolder> {

    TrainScheduleResponse trainScheduleResponse;
    private List<ScheduleList> listItems;
    private OnNoteListener onNoteListener;
    private Context context;

    public BookingListAdapter(Context context, List<ScheduleList> listItems, OnNoteListener onNoteListener, TrainScheduleResponse trainScheduleResponse) {
        this.context = context;
        this.listItems = listItems;
        this.onNoteListener = onNoteListener;
        this.trainScheduleResponse = trainScheduleResponse;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_list_item, parent, false);
        return new ViewHolder(v, onNoteListener);
    }

    @Override
    public void onBindViewHolder(final BookingListAdapter.ViewHolder viewHolder, int i) {
        ScheduleList listItem = listItems.get(i);
        viewHolder.origin.setText(listItem.getOrigin());
        viewHolder.departure.setText(listItem.getDeparture());
        viewHolder.destination.setText(listItem.getDestination());
        viewHolder.book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                TrainsList trainsList = trainScheduleResponse.getRESULTS().getDirectTrains().getTrainsList().get(i);
                args.putString("fromStation", trainScheduleResponse.getQUERY().getStartStaionName());
                args.putString("toStation", trainScheduleResponse.getQUERY().getEndStaionName());
                args.putString("searchDate", trainScheduleResponse.getQUERY().getSearchDate());
                args.putString("startStationID", Integer.toString(trainScheduleResponse.getQUERY().getStartStaion()));
                args.putString("endStationID", Integer.toString(trainScheduleResponse.getQUERY().getEndStaion()));
                args.putSerializable("trainsList", trainsList);
                Fragment fragment = new TicketBookingConfirmFragment();
                ((PassengerHomeActivity) context).loadFragment(fragment, args, "TicketBookingFragment");
            }
        });

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView origin;
        public TextView departure;
        public TextView destination;
        public Button book_btn;
        OnNoteListener onNoteListener;

        public ViewHolder(View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            origin = itemView.findViewById(R.id.start_station);
            departure = itemView.findViewById(R.id.departure_at);
            destination = itemView.findViewById(R.id.end_station);
            book_btn = itemView.findViewById(R.id.book_btn);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

}
