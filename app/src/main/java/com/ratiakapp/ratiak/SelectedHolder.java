package com.ratiakapp.ratiak;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Holder class to show comment for selected item
 */

public class SelectedHolder extends RecyclerView.ViewHolder {

     TextView mCourseCode,mDepartment,mSemester,mComment;
     RatingBar ratingBar;


    public SelectedHolder(View itemView) {
        super(itemView);

        this.mCourseCode = itemView.findViewById(R.id.course_code);
        this.mDepartment=itemView.findViewById(R.id.department);
        this.mSemester=itemView.findViewById(R.id.semester);
        this.mComment=itemView.findViewById(R.id.comment);
        ratingBar =(RatingBar) itemView.findViewById(R.id.detailStar);
    }
}
