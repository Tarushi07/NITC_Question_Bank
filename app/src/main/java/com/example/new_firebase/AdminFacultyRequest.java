package com.example.new_firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminFacultyRequest extends AppCompatActivity {

    ListView list_view_faculty_paper_requests;
    ArrayList<String> requests_faculty_paper_list = new ArrayList<>();
    DatabaseReference dbref_faculty_paper_requests;
    DatabaseReference temp1 = FirebaseDatabase.getInstance().getReference("Paper");
    PaperApproval approvePaperName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_faculty_request);
        list_view_faculty_paper_requests = findViewById(R.id.faculty_requests);
        dbref_faculty_paper_requests = FirebaseDatabase.getInstance().getReference("ApprovePaper");
        AdminFacultyRequest.FacultyPaperAdapter myFacultyPaperAdapter= new AdminFacultyRequest.FacultyPaperAdapter(this,R.layout.handle_faculty_request_row,requests_faculty_paper_list);
        list_view_faculty_paper_requests.setAdapter(myFacultyPaperAdapter);
        dbref_faculty_paper_requests.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String get_request = snapshot.getKey().toString();
                int status = snapshot.child("status").getValue(Integer.class);
                requests_faculty_paper_list.add(get_request);
                myFacultyPaperAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                myFacultyPaperAdapter.notifyDataSetChanged();
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
    class FacultyPaperAdapter extends ArrayAdapter<String> {
        private int layout;
        private ArrayList<String> requests_faculty_paper_list;

        private FacultyPaperAdapter(Context context, int resource, ArrayList<String> requests_faculty_paper_list) {
            super(context, resource, requests_faculty_paper_list);
            this.requests_faculty_paper_list = requests_faculty_paper_list;
            layout = resource;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            AdminFacultyRequest.ViewHolder mainViewholder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                AdminFacultyRequest.ViewHolder viewHolder = new AdminFacultyRequest.ViewHolder();
                viewHolder.request_id = (TextView) convertView.findViewById(R.id.heading_handle_faculty_request);
                viewHolder.approve_imageView= (ImageView) convertView.findViewById(R.id.approve_faculty_paper);
                viewHolder.close_imageView= (ImageView) convertView.findViewById(R.id.decline_faculty_paper);
                viewHolder.view_imageView = (ImageView) convertView.findViewById(R.id.view_paper);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (AdminFacultyRequest.ViewHolder) convertView.getTag();
            mainViewholder.approve_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = requests_faculty_paper_list.get(position);
                    dbref_faculty_paper_requests.child(id).child("status").setValue(1);
                    dbref_faculty_paper_requests.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                           approvePaperName = snapshot.getValue(PaperApproval.class);
                            temp1.child(approvePaperName.getSubject_Name())
                                    .child(approvePaperName.getYear_Name())
                                    .child(approvePaperName.getPaper_Name())
                                    .child("pdf")
                                    .setValue(approvePaperName.getPdf());
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    dbref_faculty_paper_requests.child(id).removeValue();
                    startActivity(new Intent(AdminFacultyRequest.this,AdminFacultyRequest.class));
                }
            });

            mainViewholder.close_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = requests_faculty_paper_list.get(position);
                    dbref_faculty_paper_requests.child(id).removeValue();
                    startActivity(new Intent(AdminFacultyRequest.this,AdminFacultyRequest.class));
                }
            });

            mainViewholder.view_imageView.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          String id = requests_faculty_paper_list.get(position);
                          dbref_faculty_paper_requests.child(id).child("pdf").child("url").addListenerForSingleValueEvent(new ValueEventListener() {
                              @Override
                              public void onDataChange(@NonNull DataSnapshot snapshot) {
                                  String url = snapshot.getValue(String.class);
                                  Uri uri = Uri.parse(url);
                                  Intent i = new Intent(Intent.ACTION_VIEW);
                                  i.setDataAndType(uri,"application/pdf");
                                  startActivity(i);
                              }

                              @Override
                              public void onCancelled(@NonNull DatabaseError error) {
                              }
                          });

                      }
            });

            mainViewholder.request_id.setText(requests_faculty_paper_list.get(position));
            return convertView;
        }
    }

    public class ViewHolder{
        TextView request_id;
        ImageView approve_imageView, close_imageView, view_imageView;
    }

}