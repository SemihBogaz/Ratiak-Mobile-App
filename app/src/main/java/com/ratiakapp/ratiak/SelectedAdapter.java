package com.ratiakapp.ratiak;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Adapter class to show comment for selected item
 */

public class SelectedAdapter extends RecyclerView.Adapter<SelectedHolder> {

    private List<Map<String,Object>> sCommentList;

    public SelectedAdapter(List<Map<String, Object>> sCommentList) {
        this.sCommentList = sCommentList;
    }

    @NonNull
    @Override
    public SelectedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_row,parent,false);
        return new SelectedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedHolder holder, int position) {
        int iRate =Integer.parseInt(String.valueOf(sCommentList.get(position).get("rate")));
        holder.ratingBar.setRating(iRate);
        holder.mCourseCode.setText("Course code: "+sCommentList.get(position).get("code"));
        holder.mDepartment.setText("Department: "+sCommentList.get(position).get("department"));
        holder.mSemester.setText("Semester: "+sCommentList.get(position).get("semester"));
        holder.mComment.setText("Comment: "+sCommentList.get(position).get("comment"));

    }

    @Override
    public int getItemCount() {
        return sCommentList.size();
    }
}
