package com.sumeyra.javainstagramclone.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sumeyra.javainstagramclone.R;
import com.sumeyra.javainstagramclone.adapter.PostAdapter;
import com.sumeyra.javainstagramclone.databinding.ActivityFeedBinding;
import com.sumeyra.javainstagramclone.model.Post;

import java.util.ArrayList;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    ArrayList<Post> postArrayList;
    private FirebaseAuth mAuth;

    private FirebaseFirestore firebaseFirestore;

    private ActivityFeedBinding binding;
    PostAdapter postAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedBinding.inflate(getLayoutInflater());
        View view =binding.getRoot();
        setContentView(view);
        postArrayList= new ArrayList<>();
        mAuth= FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getData();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(postArrayList);
        binding.recyclerView.setAdapter(postAdapter);

    }

    private void getData(){
    firebaseFirestore.collection("Posts").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
        @Override
        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
            if (error != null){
                Toast.makeText(FeedActivity.this,error.getLocalizedMessage(),Toast.LENGTH_LONG);
            }
            if (value != null){
                for (DocumentSnapshot doc : value.getDocuments()){
                    Map<String,Object> data = doc.getData();
                    //casting
                    String useremail = (String) data.get("useremail");
                    String comment = (String) data.get("comment");
                    String downloadurl = (String) data.get("downloadurl");

                    Post post = new Post(useremail,comment,downloadurl);
                    postArrayList.add(post);
                }
                postAdapter.notifyDataSetChanged();
            }
        }
    });
    }




    //1- onOptionsItemSelected: seçilince ne olacak ? Intent
    //2- onCreateOptionsMenu : bağlama işlemi MenuInflater

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //menumüzü bağamamızı sağladı
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //menülerden biri bağlanırsa ne olacak?
        if (item.getItemId()==R.id.add_post){
            Intent intentToUpload = new Intent(FeedActivity.this,UploadActivity.class);
            startActivity(intentToUpload);
        }else if(item.getItemId()== R.id.signout){
            //signout işlemi + intent
            mAuth.signOut();
            Intent intentToMain =new Intent(FeedActivity.this,MainActivity.class);
            startActivity(intentToMain);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}