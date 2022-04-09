package com.example.new_firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SubjectList extends AppCompatActivity {

    ListView list_view_subjects;
    ArrayList<String> subjects_list = new ArrayList<>();
    DatabaseReference dbref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);
        list_view_subjects = findViewById(R.id.subjects);
        dbref = FirebaseDatabase.getInstance().getReference("Subject");
        //list_view_subjects.setAdapter(myAdapater);
        MyListAdapter myListAdapter= new MyListAdapter(this,R.layout.subjectlist_row,subjects_list);
        list_view_subjects.setAdapter(myListAdapter);
        dbref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String get_Subject = snapshot.child("subjectName").getValue(String.class);

                subjects_list.add(get_Subject);
                myListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //myAdapater.notifyDataSetChanged();
                myListAdapter.notifyDataSetChanged();
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
    System.out.println("name"+subjects_list.size());
    }


    class MyListAdapter extends ArrayAdapter<String>{
        private int layout;
        private ArrayList<String> subjects_list;

        private MyListAdapter(Context context, int resource, ArrayList<String> subjects_list) {
            super(context, resource, subjects_list);
            this.subjects_list = subjects_list;
            layout = resource;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder mainViewholder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.subject_name = (TextView) convertView.findViewById(R.id.list_subject_name);

                viewHolder.subject_imageView = (ImageView) convertView.findViewById(R.id.subject_list_button);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (ViewHolder) convertView.getTag();
            mainViewholder.subject_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(SubjectList.this,YearList.class);
                    intent.putExtra("subjectName",subjects_list.get(position));
                    startActivity(intent);
                }
            });
            mainViewholder.subject_name.setText(subjects_list.get(position));
            return convertView;
        }
    }

        public class ViewHolder{
            TextView subject_name;
            ImageView subject_imageView;
        }

}
