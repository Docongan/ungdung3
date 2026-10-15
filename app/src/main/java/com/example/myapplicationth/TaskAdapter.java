package com.example.myapplicationth;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    public interface OnTaskChangeListener {
        void onTaskStatusChanged(Task task, boolean isCompleted);
        void onTaskDeleted(Task task);
    }

    private List<Task> taskList;
    private OnTaskChangeListener listener;

    public TaskAdapter(List<Task> taskList, OnTaskChangeListener listener) {
        this.taskList = taskList;
        this.listener = listener;
    }

    public void updateTasks(List<Task> newTasks) {
        this.taskList = newTasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.bind(task, listener);
    }

    @Override
    public int getItemCount() {
        return taskList != null ? taskList.size() : 0;
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbCompleted;
        TextView tvTitle;
        TextView tvTime;
        TextView tvNote;
        ImageButton btnDelete;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            cbCompleted = itemView.findViewById(R.id.cbTaskCompleted);
            tvTitle = itemView.findViewById(R.id.tvTaskTitle);
            tvTime = itemView.findViewById(R.id.tvTaskTime);
            tvNote = itemView.findViewById(R.id.tvTaskNote);
            btnDelete = itemView.findViewById(R.id.btnDeleteTask);
        }

        public void bind(Task task, OnTaskChangeListener listener) {
            tvTitle.setText(task.getTitle());

            if (task.getTime() != null && !task.getTime().trim().isEmpty()) {
                tvTime.setText(task.getTime());
                tvTime.setVisibility(View.VISIBLE);
            } else {
                tvTime.setVisibility(View.GONE);
            }

            if (task.getNote() != null && !task.getNote().trim().isEmpty()) {
                tvNote.setText(task.getNote());
                tvNote.setVisibility(View.VISIBLE);
            } else {
                tvNote.setVisibility(View.GONE);
            }

            cbCompleted.setOnCheckedChangeListener(null);
            cbCompleted.setChecked(task.isCompleted());

            updateTextStyle(task.isCompleted());

            cbCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
                task.setCompleted(isChecked);
                updateTextStyle(isChecked);
                if (listener != null) {
                    listener.onTaskStatusChanged(task, isChecked);
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTaskDeleted(task);
                }
            });
        }

        private void updateTextStyle(boolean isCompleted) {
            if (isCompleted) {
                tvTitle.setPaintFlags(tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                tvTitle.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.text_completed));
            } else {
                tvTitle.setPaintFlags(tvTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                tvTitle.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.text_primary));
            }
        }
    }
}
