package com.example.notesapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> implements Filterable {

    Context context;
    Activity activity;
    List<Model> notesList;
    List<Model>newList;

    public Adapter(Context context, Activity activity, List<Model> notesList) {
        this.context = context;
        this.activity = activity;
        this.notesList = notesList;
        newList = new ArrayList<>(notesList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_recycler_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(notesList.get(position).getTitle());
        holder.description.setText(notesList.get(position).getDesciption());

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,UpdateActivity.class);
                intent.putExtra("title",notesList.get(position).getTitle());
                intent.putExtra("desc",notesList.get(position).getDesciption());
                intent.putExtra("id",notesList.get(position).getId());

                activity.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private  Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Model> filterList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filterList.addAll(newList);
            } else {
                String filterpattern = constraint.toString().toLowerCase().trim();
                for (Model item:newList){
                    if (item.getTitle().toLowerCase().contains(filterpattern)){
                        filterList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notesList.clear();
            notesList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView title, description;
        RelativeLayout mLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mLayout  = itemView.findViewById(R.id.note_layout);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
        }
    }

    public List<Model> getList(){
        return  notesList;
    }
    public void  removeItem(int poition){
        notesList.remove(poition);
        notifyItemRemoved(poition);
    }

    public void  restoreItem(Model item, int position){
        newList.add(position,item);
        notifyItemInserted(position);
    }
}
