/*package com.example.smartindiahackathon;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class TasksAdapter extends FirebaseRecyclerAdapter<Tasks_Data, TasksAdapter.TaskViewHolder>{

    public TasksAdapter(@NonNull FirebaseRecyclerOptions<Tasks_Data> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TaskViewHolder holder, int position, @NonNull Tasks_Data model) {

        holder.taskTitle.setText(model.getTitle());
        holder.taskDesc.setText(model.getDesc());

        holder.tView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent particularTask = new Intent(Tasks_All_Activity.this, Tasks_Particular_Activity.class);
            }
        });

    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tasks_recycler_layout, parent, false);

        return new TaskViewHolder(view);
    }

    class TaskViewHolder extends RecyclerView.ViewHolder{
        View tView;
        TextView taskTitle, taskDesc;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tView = itemView;
            taskTitle = itemView.findViewById(R.id.task_title);
            taskDesc = itemView.findViewById(R.id.task_desc);

        }

    }

}
*/