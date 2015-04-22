package com.pipplware.teixeiras.virtualkeypad.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.pipplware.teixeiras.network.NetworkRequest;
import com.pipplware.teixeiras.network.models.PS;
import com.pipplware.teixeiras.virtualkeypad.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by teixeiras on 26/03/15.
 */
public class ProcessesArrayAdapter extends BaseExpandableListAdapter {

    private LayoutInflater inflater;
    private float density = 2f;
    private ListView listView;


    private ArrayList<PS.Process> processes;

    public ProcessesArrayAdapter(Context con, ArrayList<PS.Process> processes) {
        super();
        inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.processes = processes;
    }


    @Override
    public int getGroupCount() {
        return processes.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return processes.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return processes.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View workingView = null;

        if (convertView == null) {
            workingView = inflater.inflate(R.layout.process_row, null);
        } else {
            workingView = convertView;
        }

        final ProcessHolder holder = getProcessHolder(workingView);
        final PS.Process entry = (PS.Process) getGroup(groupPosition);


        holder.pid.setText(entry.getPid());
        holder.memory.setText(entry.getMemory() + "%");
        holder.name.setText(entry.getName());


        return workingView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View workingView = null;

        if (convertView == null) {
            workingView = inflater.inflate(R.layout.process_row_details, null);
        } else {
            workingView = convertView;
        }

        final ProcessDetailsHolder holder = getProcessDetailsHolder(workingView);
        final PS.Process entry = (PS.Process) getGroup(groupPosition);

        holder.kill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("pid", entry.getPid()));
                NetworkRequest.makeRequest("kill_process", list);

            }
        });

        return workingView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    private ProcessHolder getProcessHolder(View workingView) {
        Object tag = workingView.getTag();
        ProcessHolder holder = null;

        if (tag == null || !(tag instanceof ProcessHolder)) {
            holder = new ProcessHolder();
            holder.pid = (TextView) workingView.findViewById(R.id.pid);
            holder.memory = (TextView) workingView.findViewById(R.id.memory);
            holder.name = (TextView) workingView.findViewById(R.id.name);

            holder.mainView = (LinearLayout) workingView.findViewById(R.id.row_mainview);

            workingView.setTag(holder);
        } else {
            holder = (ProcessHolder) tag;
        }

        return holder;
    }

    private ProcessDetailsHolder getProcessDetailsHolder(View workingView) {
        Object tag = workingView.getTag();
        ProcessDetailsHolder holder = null;

        if (tag == null || !(tag instanceof ProcessHolder)) {
            holder = new ProcessDetailsHolder();
            holder.kill = (Button) workingView.findViewById(R.id.kill_process);

            workingView.setTag(holder);
        } else {
            holder = (ProcessDetailsHolder) tag;
        }

        return holder;
    }

    public ArrayList<PS.Process> getProcesses() {
        return processes;
    }

    public void setProcesses(ArrayList<PS.Process> processes) {
        this.processes = processes;
    }

    public void setListView(ListView view) {
        listView = view;
    }

    public static class ProcessHolder {
        public TextView pid;
        public TextView memory;
        public TextView name;

        public LinearLayout mainView;

    }

    public static class ProcessDetailsHolder {
        public Button kill;


    }


}
