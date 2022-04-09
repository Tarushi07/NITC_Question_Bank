package com.example.new_firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FacultyDashboard extends AppCompatActivity {

    private Button uploadNewPaper;
    FirebaseAuth mAuth;
    String get_request;
    String paper_year, paper_subject , paper_type;

    ListView list_view_faculty_requests;
    ArrayList<String> faculty_requests_list = new ArrayList<>();
    DatabaseReference dbref_faculty_requests;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_dashboard);
        uploadNewPaper = (Button)findViewById(R.id.uploadNewPaper);

        mAuth = FirebaseAuth.getInstance();

        uploadNewPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FacultyDashboard.this,UploadPaper.class));
            }
        });

        list_view_faculty_requests = findViewById(R.id.requests_faculty);
        dbref_faculty_requests = FirebaseDatabase.getInstance().getReference("RequestPapers");
        FacultyDashboard.FacultyAdapter myFacultyAdapter= new FacultyDashboard.FacultyAdapter(this,R.layout.faculty_request_row,faculty_requests_list);
        list_view_faculty_requests.setAdapter(myFacultyAdapter);
        String faculty_email = mAuth.getCurrentUser().getEmail();

        dbref_faculty_requests.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                int status = snapshot.child("status").getValue(Integer.class);
                if(faculty_email.compareTo(snapshot.child("faculty_name").getValue().toString()) == 0 && status == 1)
                {
                    faculty_requests_list.add(snapshot.getKey().toString());
                }
                myFacultyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                myFacultyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    class FacultyAdapter extends ArrayAdapter<String> {
        private int layout;
        private ArrayList<String> faculty_requests_list;

        private FacultyAdapter(Context context, int resource, ArrayList<String> faculty_requests_list) {
            super(context, resource, faculty_requests_list);
            this.faculty_requests_list = faculty_requests_list;
            layout = resource;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            FacultyDashboard.ViewHolder mainViewholder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                FacultyDashboard.ViewHolder viewHolder = new FacultyDashboard.ViewHolder();
                viewHolder.request_id = (TextView) convertView.findViewById(R.id.heading_faculty_request);
                viewHolder.add_paper_imageView= (ImageView) convertView.findViewById(R.id.add_paper_button);

                convertView.setTag(viewHolder);
            }
            mainViewholder = (FacultyDashboard.ViewHolder) convertView.getTag();
            mainViewholder.add_paper_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = faculty_requests_list.get(position);
                    dbref_faculty_requests.child(id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            paper_subject = snapshot.child("request_subject_name").getValue(String.class);
                            paper_year = snapshot.child("request_paper_year").getValue(String.class);
                            paper_type = snapshot.child("request_paper_name").getValue(String.class);
                            if(paper_type.compareTo("M1")==0)
                            {
                                paper_type = "Mid Term 1";
                            }
                            else if(paper_type.compareTo("M2")==0)
                            {
                                paper_type = "Mid Term 2";
                            }
                            else
                            {
                                paper_type = "End Semester";
                            }
                            Intent i = new Intent(new Intent(FacultyDashboard.this,FacultyHandlesRequest.class));
                            i.putExtra("paper_subject",paper_subject);
                            i.putExtra("paper_year",paper_year);
                            i.putExtra("paper_type",paper_type);
                            startActivity(i);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });


            mainViewholder.request_id.setText(faculty_requests_list.get(position));
            return convertView;
        }
    }

    public class ViewHolder{
        TextView request_id;
        ImageView add_paper_imageView;
   }
}