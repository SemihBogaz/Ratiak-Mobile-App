package com.ratiakapp.ratiak;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;

    //add floating action button
    private FloatingActionButton fab;

    private String docName;
    private TextView dLectName;
    private List<Map<String,Object>> mCommentList;

    //adapter and recycler view definition
    private RecyclerView mSelectedRecycler;
    private SelectedAdapter selectedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //firebase object
        firebaseFirestore= FirebaseFirestore.getInstance();

        //binding custom toolbar
        Toolbar toolbar =findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //changing name value
        dLectName = findViewById(R.id.dLectName);

        //list of comments
        mCommentList = new ArrayList();


        // getting intent values from main activity
        Intent name = getIntent();
        final String sName = name.getStringExtra("sName");

        // getting intent values from add activity
        Intent gotBack = getIntent();
        final String backName = gotBack.getStringExtra("backName");

        //setting doc name
        if (sName!=null && backName == null){
            dLectName.setText(sName);
            docName = sName;
        }else if (sName==null && backName != null){
            dLectName.setText(backName);
            docName=backName;
        }

        //recycler view definition and binding
        mSelectedRecycler=findViewById(R.id.detail_recycler);
        mSelectedRecycler.setLayoutManager(new LinearLayoutManager(this));

        //adapter definition and binding
        selectedAdapter = new SelectedAdapter(mCommentList);
        mSelectedRecycler.setAdapter(selectedAdapter);


        //method to get comments
        getComments(docName);

        fab=findViewById(R.id.fab_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //redirect to add comment page
                Intent intent = new Intent(DetailActivity.this,AddActivity.class);
                intent.putExtra("Name",sName);
                startActivity(intent);

            }
        });



    }

    public void getComments(String name){
        CollectionReference collectionReference = firebaseFirestore.collection("Lecturers")
                .document(name).collection("comments");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    Toast.makeText(DetailActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                if (queryDocumentSnapshots != null){
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        Map<String,Object> comments = snapshot.getData();
                        mCommentList.add(comments);

                        selectedAdapter.notifyDataSetChanged();

                        /*System.out.println(mCommentList);
                        System.out.println(mCommentList.get(0).get("code"));*/
                    }
                }

            }
        });
    }

    @Override
    public void onBackPressed(){
        backButtonHandler();
    }

    public void backButtonHandler() {
        Intent goMain = new Intent(DetailActivity.this, MainActivity.class);
        startActivity(goMain);
    }


}
