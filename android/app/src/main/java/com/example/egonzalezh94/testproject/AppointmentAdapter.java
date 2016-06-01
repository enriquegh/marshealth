/*
package com.example.egonzalezh94.testproject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

*/
/**
 * Created by junn on 5/11/16.
 *//*

public class AppointmentAdapter extends BaseAdapter {

    View renderer;
    private LayoutInflater layoutInflater;
    List<AppointmentFragment.AppointmentButton> buttons;

    public AppointmentAdapter(View rend) {
        renderer = rend;
//        layoutInflater = activity.getLayoutInflater();
        buttons = new ArrayList<>();
    }

    public void addButton(AppointmentFragment.AppointmentButton button) {
        buttons.add(button);
    }

    public void setModel(List<AppointmentFragment.AppointmentButton> list) {
        buttons = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return buttons.size();
    }

    @Override
    public Object getItem(int position) {
        return buttons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(buttons.get(position).appointmentID);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = renderer;
//            convertView = layoutInflater.inflate(R.layout.content_appointments, parent, false);


        AppointmentFragment.AppointmentButton button = buttons.get(position);

        TextView label = (TextView) convertView.findViewById(R.id.buttonLabel);
        label.setText(button.getText());
        Button butt = (Button) convertView.findViewById(R.id.appointmentButton);

        butt.setOnClickListener(button.listener);



        return convertView;
    }

}
*/
