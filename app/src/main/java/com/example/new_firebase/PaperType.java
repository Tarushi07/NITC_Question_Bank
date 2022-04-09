package com.example.new_firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
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

public class PaperType extends AppCompatActivity {
    TextView paper_type;
    ArrayList<String> list_paper_types = new ArrayList<>();
    DatabaseReference dbref_paper_list,dbref_download;
    ListView list_view_papers;
    String name,year,get_paper_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_type);

        Intent intent = getIntent();
        year = intent.getStringExtra("yearName");
        name = intent.getStringExtra("subjectName");

        paper_type = findViewById(R.id.paper_types);
        paper_type.setText(year);
        System.out.println(year);

        list_view_papers = findViewById(R.id.papers);
        dbref_paper_list = FirebaseDatabase.getInstance().getReference("Paper");
        dbref_download = FirebaseDatabase.getInstance().getReference("Paper").child(name).child(year);

        PaperType.PaperAdapter myPaperAdapter= new PaperType.PaperAdapter(this,R.layout.paper_type_row,list_paper_types);
        list_view_papers.setAdapter(myPaperAdapter);

        dbref_paper_list.child(name).child(year).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                get_paper_type = snapshot.getKey().toString();
                list_paper_types.add(get_paper_type);
                myPaperAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                myPaperAdapter.notifyDataSetChanged();
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

    class PaperAdapter extends ArrayAdapter<String> {
        private int layout;
        private ArrayList<String> paper_list;

        private PaperAdapter(Context context, int resource, ArrayList<String> paper_list) {
            super(context, resource,paper_list);
            this.paper_list = paper_list;
            layout = resource;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            PaperType.ViewHolder mainViewholder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                PaperType.ViewHolder viewHolder = new PaperType.ViewHolder();
                viewHolder.paper_type = (TextView) convertView.findViewById(R.id.list_paper_type);

                viewHolder.download_imageView= (ImageView) convertView.findViewById(R.id.download_paper);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (PaperType.ViewHolder) convertView.getTag();
            mainViewholder.download_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                      String pos = paper_list.get(position);
                      dbref_download.child(pos).child("pdf").child("url").addListenerForSingleValueEvent(new ValueEventListener() {
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
            mainViewholder.paper_type.setText(paper_list.get(position));
            return convertView;
        }
    }

    public class ViewHolder{
        TextView paper_type;
        ImageView download_imageView;
    }
}