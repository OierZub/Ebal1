package com.example.ebal1;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ErosketaZerrenda extends AppCompatActivity {

    FirebaseFirestore db;
    //--
    final String BILDUMA_IZENA = "zerrenda";
    String produktuIzena;
    int kopurua;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erosketa_zerrenda);

        // Datu basea hasieratu.
        db = FirebaseFirestore.getInstance();


        // Botoien entzuleak sortu.
        Button bSartu = findViewById(R.id.bSartu);
        bSartu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llama al método registrat aquí
                sartuProduktua();
            }
        });

        Button bAldatu = findViewById(R.id.bAldatu);
        bAldatu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llama al método registrat aquí
                aldatu();
            }
        });


        Button bEzabatu = findViewById(R.id.bEzabatu);
        bEzabatu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llama al método registrat aquí
                ezabatu();
            }
        });


        Button bIrakurri = findViewById(R.id.bIrakurri);
        bIrakurri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llama al método registrat aquí
                irakurri();
            }
        });
    }


    public void sartuProduktua() {

        // Irakurri sartutako datuak.
        getDatuak();
        // Sortu objektua.
        Produktua oProd = new Produktua(produktuIzena, kopurua);

        // Add a new document with a generated ID
        db.collection(BILDUMA_IZENA)
                .document(produktuIzena).set(oProd)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "sartuProduktua: Ondo sartu du!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "sartuProduktua: Errorea", e);
                    }
                });
    }

    private void aldatu() {
        // Irakurri sartutako datuak.
        getDatuak();
        // Erreferentzia eskuratu.
        DocumentReference docRef = db.collection(BILDUMA_IZENA).document(produktuIzena);
        // Dokumentua existitzen bada.

        // kopurua eguneratu.
        docRef.update("kopurua", kopurua)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.w(TAG, "produktuaAldatu:Arazo barik");
                        Toast.makeText(ErosketaZerrenda.this, "Ondo aldatu da kopurua.",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "produktuaAldatu: ERROREA", e);
                        Toast.makeText(ErosketaZerrenda.this, "Ezin izan da aldatu.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void ezabatu() {
        // Irakurri sartutako datuak.
        getDatuak();

        db.collection(BILDUMA_IZENA).document(produktuIzena)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "ezabatu:Ezabatu da produktua!");
                        Toast.makeText(ErosketaZerrenda.this, "Ezabatu da:" + produktuIzena,
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "ezabatu:Errore bat gertatu da.", e);
                        Toast.makeText(ErosketaZerrenda.this, "Ezin izan da ezabatu.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void irakurri() {
        // Irakurri sartutako datuak.
        //getDatuak();

        db.collection(BILDUMA_IZENA)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Produktua oProd = document.toObject(Produktua.class);
//                                oZerrenda.add(oProd);
                                TextView tZerrenda = findViewById(R.id.tZerrenda);
                                // KONTUZ!!! Hau asinkronoa da.
                                tZerrenda.setText(tZerrenda.getText() + oProd.getIzena() + "  " + oProd.getKopurua() + "\n");

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }


    private void getDatuak() {

        EditText etProduktua = findViewById(R.id.editTextProduktua);
        EditText etKopurua = findViewById(R.id.editTextKopurua);

        // Obtener el texto del EditText
        produktuIzena = etProduktua.getText().toString();
        // Kopurua hutsik badago, 1 ipini.
        String sKopurua = etKopurua.getText().toString();
        if (sKopurua.equals("")){
            sKopurua = "1";
        }
        // int bihurtu.
        kopurua = Integer.parseInt(sKopurua);
    }

}