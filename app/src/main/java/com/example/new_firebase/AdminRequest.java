package com.example.new_firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import android.app.ProgressDialog;
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
import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;

        import com.google.android.gms.tasks.Continuation;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdminRequest extends AppCompatActivity {
    ListView list_view_requests;
    ArrayList<String> requests_list = new ArrayList<>();
    DatabaseReference dbref_student_requests;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_request);

        list_view_requests = findViewById(R.id.requests);
        dbref_student_requests = FirebaseDatabase.getInstance().getReference("RequestPapers");
        AdminRequest.AdminAdapter myAdminAdapter= new AdminRequest.AdminAdapter(this,R.layout.student_request_row,requests_list);
        list_view_requests.setAdapter(myAdminAdapter);
        dbref_student_requests.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String get_request = snapshot.getKey().toString();

                int status = snapshot.child("status").getValue(Integer.class);
                if(status == 0)
                {
                    requests_list.add(get_request);
                }
                myAdminAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                myAdminAdapter.notifyDataSetChanged();
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

    class AdminAdapter extends ArrayAdapter<String> {
        private int layout;
        private ArrayList<String> requests_list;

        private AdminAdapter(Context context, int resource, ArrayList<String> requests_list) {
            super(context, resource, requests_list);
            this.requests_list = requests_list;
            layout = resource;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            AdminRequest.ViewHolder mainViewholder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                AdminRequest.ViewHolder viewHolder = new AdminRequest.ViewHolder();
                viewHolder.request_id = (TextView) convertView.findViewById(R.id.heading_student_request);

                viewHolder.approve_imageView= (ImageView) convertView.findViewById(R.id.approve_button);
                viewHolder.close_imageView= (ImageView) convertView.findViewById(R.id.decline_button);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (AdminRequest.ViewHolder) convertView.getTag();
            mainViewholder.approve_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   String id = requests_list.get(position);
                   dbref_student_requests.child(id).child("status").setValue(1);
                   startActivity(new Intent(AdminRequest.this,AdminRequest.class));
                }
            });

            mainViewholder.close_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = requests_list.get(position);
                    dbref_student_requests.child(id).removeValue();
                    startActivity(new Intent(AdminRequest.this,AdminRequest.class));
                }
            });
            mainViewholder.request_id.setText(requests_list.get(position));
            return convertView;
        }
    }

    public class ViewHolder{
        TextView request_id;
        ImageView approve_imageView, close_imageView;
    }

}
