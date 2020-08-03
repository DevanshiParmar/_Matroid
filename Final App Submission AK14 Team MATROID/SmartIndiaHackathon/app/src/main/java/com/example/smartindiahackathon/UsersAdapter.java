package com.example.smartindiahackathon;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class UsersAdapter extends BaseAdapter {

    Activity activity;
    List<UserModel> users;
    LayoutInflater inflater;

    public UsersAdapter(Activity activity) {
        this.activity = activity;
    }

    public UsersAdapter(Activity activity, List<UserModel> users) {
        this.activity = activity;
        this.users = users;

        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        usersViewHolder holder = null;

        if(view == null){
            view = inflater.inflate(R.layout.users_list_view_item, parent, false );
            holder = new usersViewHolder();
            holder.checkBox = view.findViewById(R.id.usersCheckBox);
            view.setTag(holder);
        }
        else
            holder = (usersViewHolder)view.getTag();
        UserModel model = users.get(position);
        holder.checkBox.setText(model.getName());

        if(model.isSelectedTo()){
            holder.checkBox.setChecked(true);
        }
        else {
            holder.checkBox.setChecked(false);
        }

        return view;
    }

    public void updateRecordsList(List<UserModel> users){
        this.users = users;
        notifyDataSetChanged();
    }

    class usersViewHolder{
        CheckBox checkBox;
    }
}
