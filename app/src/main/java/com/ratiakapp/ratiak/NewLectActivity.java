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
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NewLectActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {

    //definitions
    private RatingBar newRatingBar;
    private EditText nName,nCode,nSemester,nDepartment,nComment;
    private Button nSaveButton;
    private FirebaseFirestore firebaseFirestore;
    private String upName,upNCode,upNDepartment,upNSemester,upNComment;
    private int upNRate;


    //definition for recaptcha
    private GoogleApiClient googleApiClient;
    private String SITE_KEY="6LdETgAVAAAAAMBoQE9JSRVXSKEsfJyHCTUO5g4u";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_lect);

        Intent getFind = getIntent();

        //bindings
        newRatingBar = findViewById(R.id.new_addStar);
        nName = findViewById(R.id.name_add);
        nCode = findViewById(R.id.new_code_add);
        nComment = findViewById(R.id.new_comment_add);
        nSemester = findViewById(R.id.new_semester_add);
        nDepartment = findViewById(R.id.new_department_add);
        nSaveButton=findViewById(R.id.new_save_button);
        firebaseFirestore = FirebaseFirestore.getInstance();


        //recaptcha binding
        googleApiClient = new GoogleApiClient.Builder(this).addApi(SafetyNet.API)
                .addConnectionCallbacks(NewLectActivity.this).build();
        googleApiClient.connect();


        nSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upName = nName.getText().toString();
                upNCode = nCode.getText().toString();
                upNDepartment = nDepartment.getText().toString();
                upNSemester = nSemester.getText().toString();
                upNComment = nComment.getText().toString();
                upNRate = (int) newRatingBar.getRating();

                if (upName.length() != 0 && upNCode.length() != 0 &&upNComment.length() != 0 &&upNSemester.length() != 0 && upNRate != 0 && upNDepartment.length() != 0) {
                    SafetyNet.SafetyNetApi.verifyWithRecaptcha(googleApiClient, SITE_KEY)
                            .setResultCallback(new ResultCallback<SafetyNetApi.RecaptchaTokenResult>() {
                                @Override
                                public void onResult(@NonNull SafetyNetApi.RecaptchaTokenResult recaptchaTokenResult) {
                                    Status status = recaptchaTokenResult.getStatus();
                                    if ((status != null) && (status.isSuccess())) {
                                        setLecturer(upName);
                                        uploadData(upName, upNCode, upNDepartment, upNSemester, upNComment, upNRate);
                                        Intent goDetail = new Intent(NewLectActivity.this, DetailActivity.class);
                                        goDetail.putExtra("backName", upName);
                                        startActivity(goDetail);
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

    public void setLecturer(final String name){
        Map<String,Object> map= new HashMap<>();
        map.put("code","123");
        firebaseFirestore.collection("Lecturers").document(name).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                DocumentReference docRef = firebaseFirestore.collection("Lecturers").document(name);
                Map<String,Object> temp = new HashMap<>();
                temp.put("code", FieldValue.delete());
                docRef.update(temp).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("Sildim");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Silemedim");
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Başaramadık");
            }
        });
    }



    //uploading new comment to appropriate document
    private void uploadData(String upName, String upCode, String upDepartment, String upSemester, String upComment,int upRate) {

        //object map to put new comment
        Map<String,Object> putData = new HashMap<>();
        putData.put("code",upCode);
        putData.put("department",upDepartment);
        putData.put("semester",upSemester);
        putData.put("comment",upComment);
        putData.put("rate",upRate);

        firebaseFirestore.collection("Lecturers").document(upName)
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
