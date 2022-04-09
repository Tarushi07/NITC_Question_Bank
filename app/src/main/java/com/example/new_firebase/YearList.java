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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class YearList extends AppCompatActivity {

    TextView subject_year_list;
    ArrayList<String> years_list = new ArrayList<>();
    DatabaseReference dbref_years_list;
    ListView list_view_years;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year_list);

        Intent intent = getIntent();
        name= intent.getStringExtra("subjectName");
        subject_year_list = findViewById(R.id.Subject_year_list);
        subject_year_list.setText(name);

        list_view_years = findViewById(R.id.years);
        dbref_years_list = FirebaseDatabase.getInstance().getReference("Paper");

        YearList.MyYearListAdapter myYearAdapter= new YearList.MyYearListAdapter(this,R.layout.year_list_row,years_list);
        list_view_years.setAdapter(myYearAdapter);

        dbref_years_list.child(name).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String get_year = snapshot.getKey().toString();
                System.out.println(get_year);
                years_list.add(get_year);
                myYearAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                myYearAdapter.notifyDataSetChanged();
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
        System.out.println("name"+years_list.size());
    }

    class MyYearListAdapter extends ArrayAdapter<String> {
        private int layout;
        private ArrayList<String> years_list;

        private MyYearListAdapter(Context context, int resource, ArrayList<String> years_list) {
            super(context, resource,years_list);
            this.years_list = years_list;
            layout = resource;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            YearList.ViewHolder mainViewholder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                YearList.ViewHolder viewHolder = new YearList.ViewHolder();
                viewHolder.year_name = (TextView) convertView.findViewById(R.id.list_year_name);

                viewHolder.year_imageView= (ImageView) convertView.findViewById(R.id.year_list_button);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (YearList.ViewHolder) convertView.getTag();
            mainViewholder.year_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(YearList.this,PaperType.class);
                    intent.putExtra("yearName",years_list.get(position));
                    intent.putExtra("subjectName", name);
                    startActivity(intent);
                }
            });
            mainViewholder.year_name.setText(years_list.get(position));
            return convertView;
        }
    }

    public class ViewHolder{
        TextView year_name;
        ImageView year_imageView;
    }
}