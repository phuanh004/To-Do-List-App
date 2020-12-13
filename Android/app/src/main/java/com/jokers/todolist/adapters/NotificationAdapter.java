package com.jokers.todolist.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jokers.todolist.R;
import com.jokers.todolist.models.ToDo;

import java.util.List;

/**
 * Adapter for to do list recyclerview
 *
 * @author Anh Pham
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private final List<ToDo> toDos;

    // Provide a suitable constructor (depends on the kind of dataset)
    public NotificationAdapter(List<ToDo> todos) {
        this.toDos = todos;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_retrieved_layout, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(toDos.get(position));
    }

    @Override
    public int getItemCount() {
        return toDos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView mNotificationTextView;

        private String message;

        public MyViewHolder(@NonNull android.view.View itemView) {
            super(itemView);
            mNotificationTextView = itemView.findViewById(R.id.notificationTextView);
        }

        @SuppressLint("SetTextI18n")
        private void bind(ToDo toDo) {

            // Message that display on notification
            if (toDo.getRemainingDays() == 0) {
                message = "It's time to finish " + toDo.getTitle()
                        + ", which overdue " + toDo.getFormattedRemainingDays();
            } else {
                message = "Your task, " + toDo.getTitle()
                        + " is due today !";
            }

            mNotificationTextView.setText(message);
        }
    }
}