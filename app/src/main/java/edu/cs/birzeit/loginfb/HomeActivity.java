package edu.cs.birzeit.loginfb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private TextView txtUser;
    private EditText edtMovie, edtQuote;
    private Button btnSave;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        txtUser = findViewById(R.id.txtUser);
        btnSave = findViewById(R.id.btnSave);
        edtMovie = findViewById(R.id.edtMovie);
        edtQuote = findViewById(R.id.edtQuote);


        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            String email = user.getEmail();
            txtUser.setText("welcome " +email);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> map = new HashMap<>();
                String movie = edtMovie.getText().toString();
                String quote = edtQuote.getText().toString();
                map.put("MOVIE1", movie);
                map.put("QUOTE1", quote);
                database.collection("movie_quotes")
                        .document("quotes")
                        .set(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(HomeActivity.this,
                                        "Data saved successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(HomeActivity.this,
                                        "Data save failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });



    }
}