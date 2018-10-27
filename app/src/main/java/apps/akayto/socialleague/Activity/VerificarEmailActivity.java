package apps.akayto.socialleague.Activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import apps.akayto.socialleague.Adapters.AdapterIcones;
import apps.akayto.socialleague.Control.FirebaseConfiguracoes;
import apps.akayto.socialleague.Helper.RecyclerItemClickListener;
import apps.akayto.socialleague.R;

public class VerificarEmailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private ArrayList<Integer> arrayList = new ArrayList<>();

    private int icone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificar_email);

        setIcones();

        final ProgressBar progressBar = findViewById(R.id.verificar_ProgressBar);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources()
                .getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        recyclerView = findViewById(R.id.conta_RecyclerView);

        AdapterIcones adapter = new AdapterIcones(arrayList);
        recyclerView.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),4);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        View parentLayout = findViewById(R.id.containerVerificar);
        Snackbar.make(parentLayout, getResources().getText(R.string.stg_ContinuacaoCadastro), Snackbar.LENGTH_SHORT).show();

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        View parentLayout = findViewById(R.id.containerVerificar);
                        Snackbar.make(parentLayout, getText(R.string.stg_IconeSelecionado), Snackbar.LENGTH_SHORT).show();
                        icone = position;
                        Log.i("INFO","icone: "+icone);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                })
        );

        FirebaseAuth firebaseAuth = FirebaseConfiguracoes.getFirebaseAuth();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        Button bnt_Verificar = findViewById(R.id.verificar_BntEnviar);
        bnt_Verificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user.reload();
                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            View parentLayout = findViewById(R.id.containerVerificar);
                            Snackbar.make(parentLayout, getResources().getText(R.string.stg_EmailEnviado), Snackbar.LENGTH_SHORT).show();
                        }else{
                            View parentLayout = findViewById(R.id.containerVerificar);
                            Snackbar.make(parentLayout, getResources().getText(R.string.stg_EmailError), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        Button bnt_Proximo = findViewById(R.id.verificar_BntProximo);
        bnt_Proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                user.reload();
                if(user.isEmailVerified()){
                    if(icone!=0){
                        DatabaseReference referenceUser = FirebaseConfiguracoes.getFirebaseDatabase().child("User").child(user.getUid());
                        referenceUser.child("icone").setValue(icone).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressBar.setVisibility(View.INVISIBLE);
                                startActivity(new Intent(VerificarEmailActivity.this, LoginActivity.class));
                                finish();
                            }
                        });
                    }else{
                        View parentLayout = findViewById(R.id.containerVerificar);
                        Snackbar.make(parentLayout, getText(R.string.stg_IconenaoSelecionado), Snackbar.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }else{
                    View parentLayout = findViewById(R.id.containerVerificar);
                    Snackbar.make(parentLayout, getText(R.string.stg_EmailnaoVerificado), Snackbar.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void setIcones(){


        for(int i=0;i<146;i++){
            arrayList.add(2131165504+i);
            Log.i("INFO","ID: "+arrayList.get(i));
        }

        Log.i("INFO","ID 181: "+arrayList.get(145));
        Log.i("INFO","ID IMG: "+R.drawable.icone_146);
    }
}
