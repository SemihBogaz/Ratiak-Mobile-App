package com.ratiakapp.ratiak;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Adapter class bridge between data and ui
 */

// implements Filterable
public class LectAdapter extends RecyclerView.Adapter<LectHolder>  {

    private ArrayList<String> lectNameList;
    //private ArrayList<String> lectNameListFull;

    //calling custom filter and ic interface
    private ItemClickListener mItemClickListener;



//önceki hali => ArrayList<String> lectNameList,ArrayList<List<Map<String, String>>> lectRatingList,ItemClickListener itemClickListener
    LectAdapter(ArrayList<String> lectNameList,ItemClickListener itemClickListener) {
        this.lectNameList = lectNameList;
        this.mItemClickListener = itemClickListener;
        //for search
        //lectNameListFull = new ArrayList<>(lectNameList);
    }

    @NonNull
    @Override
    public LectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row,parent,false);
        return new LectHolder(view,mItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull LectHolder holder, int position) {
        holder.lectName.setText(lectNameList.get(position));
    }

    @Override
    public int getItemCount() {
        return lectNameList.size();
    }

    public void updateList(List<String> newList){
        lectNameList = new ArrayList<>();
        lectNameList.addAll(newList);
        notifyDataSetChanged();
    }

//    @Override
//    public Filter getFilter() {
//        return lectFilter;
//    }
//
//    private  Filter lectFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            List<String> filteredList = new ArrayList<>();
//
//            if(constraint == null || constraint.length()==0){
//                filteredList.addAll(lectNameListFull);
//            }else{
//                String filteredPattern = constraint.toString().toLowerCase().trim();
//
//                //String s : lectNameListFull
//                for (int i=0;i<lectNameListFull.size();i++){
//                    if (lectNameListFull.get(i).toLowerCase().contains(filteredPattern.toLowerCase())){
//                        filteredList.add(lectNameListFull.get(i));
//                        System.out.println("filtered List => "+filteredList);
//                    }
//                }
//            }
//
//            FilterResults results = new FilterResults();
//            results.values = filteredList;
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            lectNameList.clear();
//            lectNameList.addAll((Collection<? extends String>) results.values);
//            notifyDataSetChanged();
//        }
//    };
}
