package com.ratiakapp.ratiak;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ItemClickListener {
    private FirebaseFirestore firebaseFirestore;

    //definitions
    ArrayList<String> lectNameList;
    //ArrayList<List<Map<String, String>>> lectRatingList; gerek yok
    RecyclerView mRecyclerView;
    LectAdapter mLectAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //binding custom toolbar
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //firestore connection
        firebaseFirestore = FirebaseFirestore.getInstance();

        //defining vars
        lectNameList = new ArrayList<>();

        //recycler view definition and binding
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //adapter definition and binding
        mLectAdapter = new LectAdapter(lectNameList,this);
        mRecyclerView.setAdapter(mLectAdapter);

        getDataFromFirebase();


    }

    // method for rendering all data
    public void getDataFromFirebase() {
        CollectionReference collectionReference = firebaseFirestore.collection("Lecturers");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
                if (queryDocumentSnapshots != null) {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {

                        //getting names
                        lectNameList.add(snapshot.getId());

                        //looking for change if occurs
                        mLectAdapter.notifyDataSetChanged();
                    }
                }

            }
        });
    }

    //method to bind search icon and its functions
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                lectNameList.clear();
                getDataFromFirebase();
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String input =newText.toString();
                List<String> liste = new ArrayList<>();
                for (String s: lectNameList){
                    if (s.toLowerCase().contains(input)){
                        liste.add(s);
                    }
                }
                mLectAdapter.updateList(liste);
                //mLectAdapter.getFilter().filter(newText);
                return true;
            }

        });
        return true;
    }

    //method for selecting item
    @Override
    public void onItemClick(int pos) {
        Intent intent =new Intent(this,DetailActivity.class);
        intent.putExtra("sName",lectNameList.get(pos));
        startActivity(intent);

    }
}