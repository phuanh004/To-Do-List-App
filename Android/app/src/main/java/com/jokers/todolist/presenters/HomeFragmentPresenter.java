package com.jokers.todolist.presenters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jokers.todolist.R;
import com.jokers.todolist.models.ToDo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragmentPresenter implements
        FirebaseAuth.AuthStateListener,
        ChildEventListener,
        OnSuccessListener<Void> {

    private static List<ToDo> toDos;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private static View mView;
    private static DatabaseReference mTodosRef = null;
    private boolean loaded;

    public HomeFragmentPresenter(View mView) {
        mDatabase = FirebaseDatabase.getInstance().getReference();                      // initialize Firebase Realtime Database
        mAuth = FirebaseAuth.getInstance();                                             // initialize FirebaseAuth instance
        mAuth.addAuthStateListener(this);

        this.mView = mView;
        this.toDos = new ArrayList<>();
    }

    // TODO: Sort functions


    /* FIREBASE RTDB LISTENERS */
    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        ToDo toDo = snapshot.getValue(ToDo.class);
        assert toDo != null;
        toDo.setID(snapshot.getKey());

        toDos.add(toDo);
        mView.addTodo(toDo);
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//        Log.d("TAG", "onChildChanged: " + snapshot.getKey());
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
        String removedItemId = snapshot.getKey();

        toDos.removeIf(toDo -> toDo.getID().equals(removedItemId));
        mView.removeTodo(removedItemId);
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//        Log.d("TAG", "onChildMoved: ");
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.d("TAG", "onCancelled: ");
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        // Start startToDoListener
        startToDoListener();
    }

    public void startToDoListener(){
        // If user signed in but does not have notes
        if (mAuth.getUid() != null && mTodosRef == null) {
            String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();    // $uid
            mTodosRef = mDatabase                                      // users/$uid/todos
                    .child(userId)
                    .child("todos");

            // Listen for the data change
            mTodosRef.addChildEventListener(this);
        }
    }

    public void stopToDoListener(){
        if (mTodosRef != null) {
            mTodosRef.removeEventListener(this);
            mTodosRef = null;
        }
    }

    /* GETTERS AND SETTERS */

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    /**
     * Remove to-do by id
     * @param id to-do id
     */
    private static void removeToDo(int position, String id) {
        mTodosRef.child(id).removeValue();
    }

    /**
     * Format the remaining days
     * @param remainDays remaining days
     * @return result in display format
     */
    private static String getFormattedRemainingDays(Integer remainDays) {
        String result = "";

        if (remainDays == null) { return result; }
        else if (remainDays == 0) {
            result = "Due today";
        } else if (remainDays > 0) {
            result = remainDays + " days left";
        } else if (remainDays < 0){
            result = Math.abs(remainDays) + " days ago";
        }

        return result;
    }

    @Override
    public void onSuccess(Void aVoid) {

    }

    /**
     * Adapter
     */
    public static class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.MyViewHolder>{

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class MyViewHolder extends RecyclerView.ViewHolder {

            private final TextView mTaskTextView;
            private final TextView mDescriptionTexView;
            private final TextView mDueDateTextView, mDueDateExpandedTextView;
            private final TextView mDoDay;
            private final android.view.View mDivider;
            private final CheckBox mToDoCheckBox;

            public MyViewHolder(@NonNull android.view.View itemView) {
                super(itemView);

                // TODO: Add remains folder
                mTaskTextView = itemView.findViewById(R.id.titleTv);
                mDescriptionTexView = itemView.findViewById(R.id.descriptionTv);
                mDueDateTextView = itemView.findViewById(R.id.dueDateTextView);
                mDueDateExpandedTextView = itemView.findViewById(R.id.dueDateExpandedTv);
                mDoDay = itemView.findViewById(R.id.doDateTv);
                mDivider = itemView.findViewById(R.id.toDoSubItemGroup);
                mToDoCheckBox = itemView.findViewById(R.id.toDoCheckBox);
            }

            @SuppressLint("SetTextI18n")
            private void bind(ToDo toDo) {
                mTaskTextView.setText(toDo.getTitle());
                mDescriptionTexView.setText(toDo.getDescription());
                mDoDay.setText(toDo.getDateInDisplayFormat("EEE, MMM d", toDo.getDoDate()));
                mDueDateTextView.setText(getFormattedRemainingDays(toDo.getRemainingDays()));
                mDueDateExpandedTextView.setText(
                    (toDo.getDueDate() == null) ?
                    "" : "Deadline: " + toDo.getDateInDisplayFormat("EEE, MMM d", toDo.getDueDate())
                );

                mDueDateTextView.setVisibility(toDo.isExpanded() ? android.view.View.GONE : android.view.View.VISIBLE);
                mDivider.setVisibility(toDo.isExpanded() ? android.view.View.VISIBLE : android.view.View.GONE);
            }

        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public ToDoListAdapter() {}

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
                // TODO: Replace this with real function
                // | move the selected to do to logbook

                // Delete current item
                removeToDo(position, toDos.get(position).getID());
            });
        }

        @Override
        public int getItemCount() {
            return toDos.size();
        }
    }

    public interface View {
        void addTodo(ToDo todo);
        void changeTodo(ToDo todo);
        void removeTodo(String todoId);
        void resetTodoList();
        void showProgressBar();
        void hideProgressBar();
    }
}
