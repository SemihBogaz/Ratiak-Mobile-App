package com.ratiakapp.ratiak;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

/***
 * Holder Class for binding data and where to show them
 */

public class LectHolder extends RecyclerView .ViewHolder implements View.OnClickListener{

    //binding recycler_row elements
    TextView  lectName,clickText;
    ItemClickListener itemClickListener;

     LectHolder(View itemView, ItemClickListener itemClickListener) {
        super(itemView);

        lectName = itemView.findViewById(R.id.lectName_recycler_row);
        clickText = itemView.findViewById(R.id.clicking);
        this.itemClickListener = itemClickListener;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
         itemClickListener.onItemClick(getAdapterPosition());

    }
}
