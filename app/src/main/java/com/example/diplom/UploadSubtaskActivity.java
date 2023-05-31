package com.example.diplom;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diplom.model.EstimateLine;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UploadSubtaskActivity extends AppCompatActivity {

    Button saveButton;
    EditText subtaskName, subtaskFrom, subtaskTo;
    String userId, estimatePath;
    String tabName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_subtask);

        subtaskFrom = findViewById(R.id.inputSubtaskFrom);
        subtaskName = findViewById(R.id.inputSubtaskName);
        subtaskTo = findViewById(R.id.inputSubtaskTo);
        saveButton = findViewById(R.id.saveButtonLine);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getUid();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            estimatePath = bundle.getString("FormattedDate");
            userId = bundle.getString("UserId");
            tabName = bundle.getString("TabName");
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    public void saveData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadSubtaskActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
        uploadData();
        dialog.dismiss();
    }

    public void uploadData() {
        String subtaskName = this.subtaskName.getText().toString();
        String subtaskFrom = this.subtaskFrom.getText().toString();
        String subtaskTo = this.subtaskTo.getText().toString();
        EstimateLine estimateLine = new EstimateLine(subtaskName, subtaskTo, subtaskFrom, userId, tabName);
        Date currentDate = new Date();
        String formattedDate = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.ENGLISH).format(currentDate);
        FirebaseDatabase.getInstance().getReference("Estimates").child(userId).child(estimatePath)
                .child("tabs").child(tabName).child(formattedDate).setValue(estimateLine)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UploadSubtaskActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(UploadSubtaskActivity.this, "NOT Saved", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadSubtaskActivity.this, "AAAAAAAAAAAA", Toast.LENGTH_SHORT).show();
            }
        });
    }
}