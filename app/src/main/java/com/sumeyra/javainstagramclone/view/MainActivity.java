package com.sumeyra.javainstagramclone.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sumeyra.javainstagramclone.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view =binding.getRoot();
        setContentView(view);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        // burada daha önceden giriş yapıldıysa eğer onun kontrolü sağlanıyor eğer varsa direkt feed e gönderiyor yoksa diğer işlemlere gidiliyor
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            Intent intent = new Intent(MainActivity.this, FeedActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void signIn(View view){
        //signInWithEmailAndPassword()
    email=binding.emailText.getText().toString();
    password=binding.passwordText.getText().toString();
    if (email.equals("")|| password.equals("")){
        Toast.makeText(MainActivity.this,"Enter email and password", Toast.LENGTH_LONG);
    }else{
        mAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intent = new Intent(MainActivity.this, FeedActivity.class);
                startActivity(intent);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            Toast.makeText(MainActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG);
            }
        });
    }
    }
    public void signUp(View view){
        //createUserWithEmailAndPassword()
        email= binding.emailText.getText().toString();
        password= binding.passwordText.getText().toString();
        if (email.equals("")||password.equals("")){
            Toast.makeText(this,"Enter email and password!",Toast.LENGTH_LONG);
        }else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    //sign up başarılı olursa napcaz feed activiyt e git
                    Intent intent = new Intent(MainActivity.this,FeedActivity.class);
                    startActivity(intent);
                    //giriş yaptım artık çıkış yapana kadar bu aktivite ile işim yok
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //hata alırsak napacağız
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            });

        }
    }


}
