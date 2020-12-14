package com.jokers.todolist.adapters;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jokers.todolist.R;
import com.jokers.todolist.models.ToDo;

import java.util.List;
/**
 * Adapter for logbook recyclerview
 *
 * @author Anh Pham
**/
public class LogbookAdapter extends RecyclerView.Adapter<LogbookAdapter.MyViewHolder> {
    private final List<ToDo> toDos;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTitleTextView;
        private final TextView mDoneDateTextView;
        private final TextView mDoDay, mDescriptionTexView, mDueDateExpandedTextView;
        private final android.view.View mDivider;
        private final CheckBox mToDoCheckBox;

        public MyViewHolder(@NonNull android.view.View itemView) {
            super(itemView);

            mTitleTextView = itemView.findViewById(R.id.titleLogbookTv);
            mDescriptionTexView = itemView.findViewById(R.id.descriptionLogbookExpandedTv);
            mDoneDateTextView = itemView.findViewById(R.id.doneDateLogbookTextView);
            mDueDateExpandedTextView = itemView.findViewById(R.id.dueDateExpandedLogbookTv);
            mDoDay = itemView.findViewById(R.id.doDateLogbookExpandedTv);
            mDivider = itemView.findViewById(R.id.toDoSubItemLogbookGroup);
            mToDoCheckBox = itemView.findViewById(R.id.toDoLogbookCheckBox);
        }

        @SuppressLint("SetTextI18n")
        private void bind(ToDo toDo) {
            mTitleTextView.setText(toDo.getTitle());
            mDescriptionTexView.setText(toDo.getDescription());

            mDoneDateTextView.setText(toDo.getDateInDisplayFormat("EEE, MMM d", toDo.getDoneDate()));

            mDoDay.setText(
                    toDo.getDoDate() == null ?
                            "Today" :
                            toDo.getDateInDisplayFormat("EEE, MMM d", toDo.getDoDate())
            );
            mDueDateExpandedTextView.setText(
                    (toDo.getDueDate() == null) ?
                            "" : "Deadline: " + toDo.getDateInDisplayFormat("EEE, MMM d", toDo.getDoneDate())
            );

            // Strike out the title
            mTitleTextView.setPaintFlags(mTitleTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            mDoneDateTextView.setVisibility(toDo.isExpanded() ? android.view.View.GONE : android.view.View.VISIBLE);
            mDivider.setVisibility(toDo.isExpanded() ? android.view.View.VISIBLE : android.view.View.GONE);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public LogbookAdapter(List<ToDo> toDos) {
        this.toDos = toDos;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public LogbookAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.log_book_retrieved_layout, parent, false);

        return new LogbookAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LogbookAdapter.MyViewHolder holder, int position) {
        holder.bind(toDos.get(position));

        holder.itemView.setOnClickListener(v -> {
            // Get the current state of the item
            boolean expanded = toDos.get(position).isExpanded();
            // Change the state
            toDos.get(position).setExpanded(!expanded);
            // Notify the adapter that item has changed
            notifyItemChanged(position);
        });

        holder.mToDoCheckBox.setOnClickListener(v -> {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference();

            // Remove done date
            toDos.get(position).setDoneDate(toDos.get(position).getCurrentDate());

            // Push back to to-to list
            DatabaseReference logBookRef = db                        // users/$uid/todos
                    .child(toDos.get(position).getUid())
                    .child("todos");

            String toDoKey = logBookRef.push().getKey();            // Push to db with a unique key

            assert toDoKey != null;
            logBookRef.child(toDoKey).setValue(toDos.get(position));

            // Delete current item
            DatabaseReference mTodosRef = FirebaseDatabase.getInstance().getReference()
                    .child(toDos.get(position).getUid())
                    .child("logbook")
                    .child(toDos.get(position).getID());

            mTodosRef.removeValue();

            // Uncheck the box
            holder.mToDoCheckBox.toggle();
        });
    }

    @Override
    public int getItemCount() {
        return toDos.size();
    }
}
