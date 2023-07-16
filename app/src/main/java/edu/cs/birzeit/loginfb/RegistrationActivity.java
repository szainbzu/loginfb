package edu.cs.birzeit.loginfb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {
    private EditText edtEmail2, edtPassword2;
    private Button btnSignUp;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        edtEmail2 = findViewById(R.id.edtEmail2);
        edtPassword2 = findViewById(R.id.edtPassword2);
        btnSignUp = findViewById(R.id.btnSginUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail2.getText().toString();
                String pass = edtPassword2.getText().toString();
                if(email.isEmpty() || pass.isEmpty()){
                    Toast.makeText(RegistrationActivity.this,
                            "Enter your creditentials", Toast.LENGTH_SHORT).show();
                    return;
                }
                firebaseAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(RegistrationActivity.this,
                                            "Registration Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegistrationActivity.this,
                                            MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(RegistrationActivity.this,
                                            "Registration failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}