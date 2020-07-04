package com.ratiakapp.ratiak;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

/**
 * Activity for adding new comment to selected item in db
 */

public class AddActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {
    //rating bar
    private RatingBar ratingBar;

    //firestore definitions
    private FirebaseFirestore firebaseFirestore;

    //definition for recaptcha
    private GoogleApiClient googleApiClient;
    private String SITE_KEY="###";

    //form definitions
    private TextView Name;
    private EditText mCode,mDepartment,mSemester,mComment,mRate;
    private Button mSaveButton;

    private String dName,upCode,upDepartment,upSemester,upComment;
    private int upRate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        //firestore connection
        firebaseFirestore = FirebaseFirestore.getInstance();

        //getting intent from detail activity
        Intent intent = getIntent();
        final String sName = intent.getStringExtra("Name");

        //bindings
        Name = findViewById(R.id.dLectName);
        Name.setText(sName);
        mCode = findViewById(R.id.code_add);
        mDepartment = findViewById(R.id.department_add);
        mSemester = findViewById(R.id.semester_add);
        ratingBar = findViewById(R.id.addStar);
        mComment = findViewById(R.id.comment_add);
        mSaveButton = findViewById(R.id.save_button);


        //recaptcha binding
        googleApiClient = new GoogleApiClient.Builder(this).addApi(SafetyNet.API)
                .addConnectionCallbacks(AddActivity.this).build();
        googleApiClient.connect();



        //save button function
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //prepare data to upload
                dName = Name.getText().toString();
                upCode= mCode.getText().toString();
                upDepartment=mDepartment.getText().toString();
                upSemester=mSemester.getText().toString();
                upComment=mComment.getText().toString();
                upRate= (int) ratingBar.getRating();
                if (dName.length() != 0 && upCode.length() != 0 &&upComment.length() != 0 &&upSemester.length() != 0 && upRate != 0 && upDepartment.length() != 0) {
                    SafetyNet.SafetyNetApi.verifyWithRecaptcha(googleApiClient, SITE_KEY)
                            .setResultCallback(new ResultCallback<SafetyNetApi.RecaptchaTokenResult>() {
                                @Override
                                public void onResult(@NonNull SafetyNetApi.RecaptchaTokenResult recaptchaTokenResult) {
                                    Status status = recaptchaTokenResult.getStatus();
                                    if ((status != null) && (status.isSuccess())) {
                                        uploadData(dName, upCode, upDepartment, upSemester, upComment, upRate);
                                        Intent goBack = new Intent(AddActivity.this, DetailActivity.class);
                                        goBack.putExtra("backName", dName);
                                        startActivity(goBack);
                                        finish();
                                    }
                                }
                            });
                } else{
                    Toast.makeText(getApplicationContext(),"Fields can't be empty",Toast.LENGTH_LONG).show();
                }

                }
            });
    }

    //uploading new comment to appropriate document
    private void uploadData(String dName, String upCode, String upDepartment, String upSemester, String upComment,int upRate) {

        //object map to put new comment
        Map<String,Object> putData = new HashMap<>();
        putData.put("code",upCode);
        putData.put("department",upDepartment);
        putData.put("semester",upSemester);
        putData.put("comment",upComment);
        putData.put("rate",upRate);

        firebaseFirestore.collection("Lecturers").document(dName)
                .collection("comments").document().set(putData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
