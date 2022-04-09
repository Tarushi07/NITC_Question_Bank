package com.example.new_firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FacultyHandlesRequest extends AppCompatActivity {

    EditText paper_name , paper_type , paper_year;
    EditText pdf;
    Button submit_requested;
    StorageReference store_pdf;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_handles_request);

        paper_name = (EditText) findViewById(R.id.subject_requested);
        paper_year = (EditText) findViewById(R.id.year_requested);
        paper_type = (EditText) findViewById(R.id.paper_type_requested);
        submit_requested = (Button)findViewById(R.id.request_paper_submitted);
        pdf = (EditText) findViewById(R.id.pdf_requested);
        store_pdf = FirebaseStorage.getInstance().getReference();
        submit_requested.setEnabled(false);

        String temp;
        Intent intent = getIntent();
        temp = intent.getStringExtra("paper_subject");
        paper_name.setText(temp);
        paper_name.setEnabled(false);

        temp = intent.getStringExtra("paper_year");
        paper_year.setText(temp);
        paper_year.setEnabled(false);

        temp = intent.getStringExtra("paper_type");
        paper_type.setText(temp);
        paper_type.setEnabled(false);

        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPdf();
            }
        });
    }

    private void selectPdf() {
        Intent intent =  new Intent();
        intent.setType("application/pdf");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select PDF File"),12);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 12 && resultCode == RESULT_OK && data!=null && data.getData()!=null)
        {
            submit_requested.setEnabled(true);
            pdf.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/") + 1 ));
            submit_requested.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadPDF(data.getData());
                }
            });
        }
    }

    private void uploadPDF(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File is loading..");
        progressDialog.show();

        StorageReference reference = store_pdf.child("upload"+System.currentTimeMillis()+".pdf");
        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri uri = uriTask.getResult();

                UploadPdf Pdf = new UploadPdf(pdf.getText().toString(), uri.toString());
                String subject_name = paper_name.getText().toString();
                String year = paper_year.getText().toString();
                String paper_name = paper_type.getText().toString();

                Toast.makeText(getApplicationContext(), "Uploaded Paper Successfully!", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                DatabaseReference dbref_approval = FirebaseDatabase.getInstance().getReference("ApprovePaper");
                String id = subject_name + year + paper_name;
                id = id.replaceAll(" ", "");
                PaperApproval paperApproval = new PaperApproval(id, subject_name, year, paper_name, 0, Pdf);
                dbref_approval.child(id).setValue(paperApproval);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                progressDialog.setMessage("File Uploaded.."+ (int)progress +"%");
            }
        });
    }
}