package edu.cs.birzeit.loginfb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private TextView txtUser, txtQuote;
    private EditText edtMovie, edtQuote;
    private Button btnSave, btnShow;
    public static final String QUOTE = "QUOTE1";
    public static final String MOVIE = "MOVIE1";
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private DocumentReference documentRef = database.collection("movie_quotes")
            .document("quotes");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        txtUser = findViewById(R.id.txtUser);
        btnSave = findViewById(R.id.btnSave);
        edtMovie = findViewById(R.id.edtMovie);
        edtQuote = findViewById(R.id.edtQuote);

        txtQuote = findViewById(R.id.txtQuote);
        btnShow  = findViewById(R.id.btnShow);


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
                map.put(MOVIE, movie);
                map.put(QUOTE, quote);
                documentRef
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

                btnShow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        documentRef.get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(documentSnapshot.exists()){
                                            String movie = documentSnapshot.getString(MOVIE);
                                            String quote = documentSnapshot.getString(QUOTE);

                                            txtQuote.setText(movie + "\n" + quote);
                                        }

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(HomeActivity.this, "could not get data", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        documentRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
               if(error != null){
                   Toast.makeText(HomeActivity.this, "Something went wrong",
                           Toast.LENGTH_SHORT).show();
                   Log.e("ERROR", error.toString());
                   return;
               }
               if(value!=null && value.exists() ){
                   String movie = value.getString(MOVIE);
                   String quote = value.getString(QUOTE);

                   txtQuote.setText(movie + "\n" + quote);

               }

            }
        });
    }
}