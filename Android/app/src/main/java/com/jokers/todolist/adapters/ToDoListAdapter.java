package com.jokers.todolist.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jokers.todolist.EditToDoActivity;
import com.jokers.todolist.R;
import com.jokers.todolist.models.ToDo;

import java.util.List;

/**
 * Adapter for to do list recyclerview
 *
 * @author Anh Pham
 */
public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.MyViewHolder>{

    private final List<ToDo> toDos;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTaskTextView;
        private final TextView mDescriptionTexView;
        private final TextView mDueDateTextView, mDueDateExpandedTextView;
        private final TextView mDoDay;
        private final TextView mHasDocumentIcon;
        private final android.view.View mDivider;
        private final CheckBox mToDoCheckBox;
        private final Button mEditToDoButton;

        public MyViewHolder(@NonNull android.view.View itemView) {
            super(itemView);

            mTaskTextView = itemView.findViewById(R.id.titleTv);
            mDescriptionTexView = itemView.findViewById(R.id.descriptionExpandedTv);
            mDueDateTextView = itemView.findViewById(R.id.dueDateTextView);
            mDueDateExpandedTextView = itemView.findViewById(R.id.dueDateExpandedTv);
            mDoDay = itemView.findViewById(R.id.doDateExpanedTv);
            mHasDocumentIcon = itemView.findViewById(R.id.hasDocumentIcon);
            mDivider = itemView.findViewById(R.id.toDoSubItemGroup);
            mToDoCheckBox = itemView.findViewById(R.id.toDoCheckBox);
            mEditToDoButton = itemView.findViewById(R.id.editToDoButton);
        }

        @SuppressLint("SetTextI18n")
        private void bind(ToDo toDo) {
            mTaskTextView.setText(toDo.getTitle());
            mDescriptionTexView.setText(toDo.getDescription());
            mDueDateTextView.setText(toDo.getFormattedRemainingDays());

            mDoDay.setText(
                    toDo.getDoDate() == null ?
                            "Today" :
                            toDo.getDateInDisplayFormat("EEE, MMM d", toDo.getDoDate())
            );
            mDueDateExpandedTextView.setText(
                    (toDo.getDueDate() == null) ?
                            "" : "Deadline: " + toDo.getDateInDisplayFormat("EEE, MMM d", toDo.getDueDate())
            );

            if (!toDo.getDescription().equals("")) {
                mHasDocumentIcon.setVisibility(toDo.isExpanded() ? android.view.View.GONE : android.view.View.VISIBLE);
            }

            mDueDateTextView.setVisibility(toDo.isExpanded() ? android.view.View.GONE : android.view.View.VISIBLE);
            mEditToDoButton.setVisibility(toDo.isExpanded() ? View.VISIBLE : View.GONE);
            mDivider.setVisibility(toDo.isExpanded() ? android.view.View.VISIBLE : android.view.View.GONE);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ToDoListAdapter(List<ToDo> toDos) {
        this.toDos = toDos;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ToDoListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.retrieved_layout, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
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

            // Set done date
            toDos.get(position).setDoneDate(toDos.get(position).getCurrentDate());

            // Add to logbook
            DatabaseReference logBookRef = db                        // users/$uid/todos
                    .child(toDos.get(position).getUid())
                    .child("logbook");

            String toDoKey = logBookRef.push().getKey();            // Push to db with a unique key

            assert toDoKey != null;
            logBookRef.child(toDoKey).setValue(toDos.get(position));

            // Delete current item
            DatabaseReference mTodosRef = FirebaseDatabase.getInstance().getReference()
                    .child(toDos.get(position).getUid())
                    .child("todos")
                    .child(toDos.get(position).getID());

            mTodosRef.removeValue();

            // Uncheck the box
            holder.mToDoCheckBox.toggle();
        });

        holder.mEditToDoButton.setOnClickListener(v -> {
             Intent intent = new Intent(v.getContext(), EditToDoActivity.class);
            // sent bundle
            intent.putExtra("toDoId", toDos.get(position).getID());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return toDos.size();
    }
}