package com.dhammika_dev.justgo.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.dhammika_dev.justgo.R;
import com.dhammika_dev.justgo.dto.lankagate.TrainsList;
import com.dhammika_dev.justgo.model.entities.response.TrainScheduleResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ExpandableScheduleListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<TrainsList> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<TrainsList, TrainsList> _listDataChild;
    private TrainScheduleResponse trainScheduleResponse;

    public ExpandableScheduleListAdapter(Context context, List<TrainsList> listDataHeader,
                                         HashMap<TrainsList, TrainsList> listChildData, TrainScheduleResponse trainScheduleResponse) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.trainScheduleResponse = trainScheduleResponse;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition));
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final TrainsList trainData = (TrainsList) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_schedule_list_item, null);
        }

        TextView startStation = convertView.findViewById(R.id.startStation);
        TextView arrivalTimeToStartStation = convertView.findViewById(R.id.arrivalTimeToStartStation);
        TextView departureTime = convertView.findViewById(R.id.departureTime);
        TextView endStation = convertView.findViewById(R.id.endStation);
        TextView arrivalTimeToEndStation = convertView.findViewById(R.id.arrivalTimeToEndStation);
        TextView trainType = convertView.findViewById(R.id.trainType);
        TextView trainName = convertView.findViewById(R.id.trainName);
        TextView trainNumber = convertView.findViewById(R.id.trainNumber);
        TextView trainStopAndLastStation = convertView.findViewById(R.id.trainStopAndLastStation);
        TextView runBy = convertView.findViewById(R.id.runBy);
        TextView firstClass = convertView.findViewById(R.id.firstClass);
        TextView secondClass = convertView.findViewById(R.id.secondClass);
        TextView thirdClass = convertView.findViewById(R.id.thirdClass);
        TextView directTrain = convertView.findViewById(R.id.directTrain);
        TextView arrival_time_to = convertView.findViewById(R.id.arrival_time_to);
        TextView arrival_end_to = convertView.findViewById(R.id.arrival_end_to);

        startStation.setText(trainData.getStartStationName());
        arrivalTimeToStartStation.setText(timeFormat(trainData.getArrivalTime()));
        departureTime.setText(timeFormat(trainData.getDepatureTime()));
        endStation.setText(trainData.getFinalStationName());
        arrivalTimeToEndStation.setText(timeFormat(trainData.getArrivalTimeEndStation()));
        trainType.setText(trainData.getTrainType());
        trainName.setText(trainData.getTrainName());
        trainNumber.setText(String.valueOf(trainData.getTrainNo()));
        trainStopAndLastStation.setText(timeFormat(trainData.getArrivalTimeFinalStation()));
        runBy.setText(trainData.getTrainFrequncy());
        arrival_time_to.setText("Arrival to " + trainScheduleResponse.getQUERY().getStartStaionName());
        arrival_end_to.setText("Arrival to " + trainScheduleResponse.getQUERY().getEndStaionName());
        for (int i = 0; i < trainData.getClassList().size(); i++) {
            if (trainData.getClassList().get(i).getClassID() == 1) {
                firstClass.setVisibility(View.VISIBLE);
            }
            if (trainData.getClassList().get(i).getClassID() == 2) {
                secondClass.setVisibility(View.VISIBLE);
            }
            if (trainData.getClassList().get(i).getClassID() == 3) {
                thirdClass.setVisibility(View.VISIBLE);
            }
        }
        directTrain.setText("YES");

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        TrainsList trainsList = (TrainsList) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_schedule_list_group, null);
        }

        TextView train = convertView.findViewById(R.id.text_view_deatails);
        TextView arrival = convertView.findViewById(R.id.text_view_from);
        TextView departure = convertView.findViewById(R.id.text_view_to);
        TextView type = convertView.findViewById(R.id.text_view_type);
        TextView state = convertView.findViewById(R.id.train_state);

        arrival.setText("In " + trainScheduleResponse.getQUERY().getStartStaionName() + " at " + timeFormat(trainsList.getArrivalTime()));
        train.setText(trainsList.getTrainName().toUpperCase());
        departure.setText("Out " + trainScheduleResponse.getQUERY().getEndStaionName() + " at " + timeFormat(trainsList.getArrivalTimeEndStation()));
        type.setText(trainsList.getTrainType());
        state.setText(trainsList.getTrainFrequncy());
        return convertView;
    }

    public String timeFormat(String time) {
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss");
            final Date dateObj = sdf.parse(time);
            return new SimpleDateFormat("K:mm a").format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}