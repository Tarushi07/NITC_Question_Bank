package com.example.new_firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class UploadPaper extends AppCompatActivity {

    Spinner spinner_subject;
    Spinner spinner_year;
    Spinner spinner_paperType;
    ValueEventListener valueEventListener;
    ArrayList<String> subjectList;
    DatabaseReference dbref , dbref_paper_upload;
    StorageReference store_pdf;
    EditText pdf;
    Button submit_paper;
    ArrayAdapter<String> subjectListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_paper);

        spinner_subject = (Spinner) findViewById(R.id.spinner_subject_list);
        pdf = (EditText) findViewById(R.id.details);
        submit_paper = (Button) findViewById(R.id.submit_request_paper);

        subjectList = new ArrayList<String>();
        subjectListAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,subjectList);
        spinner_subject.setAdapter(subjectListAdapter);
        dbref = FirebaseDatabase.getInstance().getReference("Subject");
        dbref_paper_upload = FirebaseDatabase.getInstance().getReference("Paper");
        fetchData();

        spinner_year = (Spinner) findViewById(R.id.request_spinner_year);
        ArrayAdapter<CharSequence> adapter_course = ArrayAdapter.createFromResource(this,
                R.array.Year, android.R.layout.simple_spinner_item);
        adapter_course.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_year.setAdapter(adapter_course);

        spinner_paperType = (Spinner) findViewById(R.id.spinner_paper_type);
        ArrayAdapter<CharSequence> adapter_paper_type = ArrayAdapter.createFromResource(this,
                R.array.Paper_Name, android.R.layout.simple_spinner_item);
        adapter_paper_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_paperType.setAdapter(adapter_paper_type);

        store_pdf = FirebaseStorage.getInstance().getReference();

        submit_paper.setEnabled(false);
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
            submit_paper.setEnabled(true);
            pdf.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/") + 1 ));
            submit_paper.setOnClickListener(new View.OnClickListener() {
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
                while(!uriTask.isComplete());
                Uri uri = uriTask.getResult();

                UploadPdf Pdf = new UploadPdf(pdf.getText().toString() , uri.toString());

                String subject_name = spinner_subject.getSelectedItem().toString();
                String year = spinner_year.getSelectedItem().toString();
                String paper_name = spinner_paperType.getSelectedItem().toString();

                Toast.makeText(getApplicationContext(), "Uploaded Paper Successfully!", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                DatabaseReference dbref_approval = FirebaseDatabase.getInstance().getReference("ApprovePaper");
                String id = subject_name+year+paper_name;
                id = id.replaceAll(" ","");
                PaperApproval paperApproval = new PaperApproval(id,subject_name,year,paper_name,0,Pdf);
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
    public void fetchData()
    {
        valueEventListener = dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot myData : snapshot.getChildren())
                {
                    subjectList.add(myData.child("subjectName").getValue().toString());
                }
                subjectListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}