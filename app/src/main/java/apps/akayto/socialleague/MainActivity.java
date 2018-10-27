package apps.akayto.socialleague;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.ArrayList;

import apps.akayto.socialleague.Activity.LoginActivity;
import apps.akayto.socialleague.Control.FirebaseConfiguracoes;
import apps.akayto.socialleague.Fragments.ContaFragment;
import apps.akayto.socialleague.Fragments.NotificacaoFragment;
import apps.akayto.socialleague.Fragments.PerfilFragment;
import apps.akayto.socialleague.Fragments.PerfilTimeFragment;
import apps.akayto.socialleague.Fragments.TelaInicialFragment;
import apps.akayto.socialleague.Fragments.TimeBuscaFragment;
import apps.akayto.socialleague.Models.Notificacao;
import apps.akayto.socialleague.Models.NotificacaoSolicitarEntrada;
import apps.akayto.socialleague.Models.UsuarioLogado;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private ArrayList<Integer> arrayList = new ArrayList<>();

    private Toolbar toolbar;

    private FrameLayout frameLayout;

    private static final String ARQUIVO = "PREFERENCIAS_USUARIO";
    private static final String TAG = "INFO_MAIN";

    private ArrayList<Fragment> fragments = new ArrayList<>();

    public static boolean isAppRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isAppRunning = true;

        frameLayout = findViewById(R.id.frameContainer);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        firebaseAuth = FirebaseConfiguracoes.getFirebaseAuth();
        user = firebaseAuth.getCurrentUser();

        setIcones();


        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("notificacao")) {
                fragments.add(new TelaInicialFragment());
                NotificacaoSolicitarEntrada notificacao = (NotificacaoSolicitarEntrada) getIntent().getExtras().get("notificacao");
                NotificacaoFragment fragment = new NotificacaoFragment();
                fragment.notificacao = notificacao;
                Log.i(TAG, "Notificacao: " + notificacao.getTitle());
                novoFrame(fragment);
            }

        } else
            telaInicial();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        }else{

            if(fragments.size()>1){

                remove(fragments.get(fragments.size()-1));


            }else {

                super.onBackPressed();
            }
        }
    }
    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {


        final ImageView img = findViewById(R.id.img_Nav_Menu_Principa);
        if (img != null) {

            img.setBackgroundResource(R.drawable.zed1);


            Thread t = new Thread() {

                @Override
                public void run() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            ImageView icon = findViewById(R.id.nav_iconeUsuario);
                            icon.setBackgroundResource(arrayList.get(UsuarioLogado.getIcone()));

                            TextView email = findViewById(R.id.nav_emailUsuario);
                            email.setText(UsuarioLogado.getEmail());

                            TextView nome = findViewById(R.id.nav_nomeUsuario);
                            nome.setText(UsuarioLogado.getNome());


                        }
                    });

                }
            };

            t.start();
        }

        return super.onCreatePanelMenu(featureId, menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

       /* AnimationDrawable anim = (AnimationDrawable) img.getDrawable();
        anim.start();*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_times) {
            if(UsuarioLogado.getTime().equals("nd")) {
                limparFragments();
                TimeBuscaFragment fragment = new TimeBuscaFragment();
                novoFrame(fragment);
                setTitleToolbar(getResources().getString(R.string.txt_Encontrar_Time));
            }else{
                limparFragments();
                PerfilTimeFragment fragment = new PerfilTimeFragment();
                fragment.setStg_NomeTime(UsuarioLogado.getTime());
                novoFrame(fragment);
                setTitleToolbar(getResources().getString(R.string.stg_Times));
            }

        } else if(id == R.id.nav_torneios) {

        } else if(id == R.id.nav_notificacoes) {
            limparFragments();
            NotificacaoFragment fragment = new NotificacaoFragment();
            novoFrame(fragment);

        }else if(id == R.id.nav_perfil) {
            limparFragments();
            PerfilFragment fragment = new PerfilFragment();
            novoFrame(fragment);

        } else if(id == R.id.nav_alterar_dados) {
            limparFragments();
            ContaFragment fragment = new ContaFragment();
            novoFrame(fragment);

        }else if(id == R.id.nav_sair_conta) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);
            dialog.setTitle("Sair da Conta");
            dialog.setMessage("Deseja sair da Conta?");
            dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences preferences = getSharedPreferences(ARQUIVO, 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("ContinuarLogado",false);
                    editor.apply();
                    Snackbar.make(frameLayout,"Saindo...",Snackbar.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            });
            dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            dialog.setCancelable(true);
            dialog.create();
            dialog.show();
        }

        //Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.zed_gif);
/*
        ImageView img = findViewById(R.id.img_Nav_Menu_Principa);
        GifAnimationDrawable gif = null;

        try {
            gif = new
                    GifAnimationDrawable(getResources().openRawResource(R.raw.zed_gif));
            gif.setOneShot(false);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        img.setImageDrawable(gif);
        gif.setVisible(true, true);


        Log.i("MainActivity","onNavigationItemSelected");
*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setTitleToolbar(String titleToolbar)
    {

        toolbar.setTitle(titleToolbar);

        /*
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorNavLetras));
        Drawable drawable = toolbar.getOverflowIcon();
        if(drawable != null) {
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable.mutate(), getResources().getColor(R.color.colorNavLetras));
            toolbar.setOverflowIcon(drawable);
        }*/
    }

    public void remove(final Fragment fragment) {

        fragments.remove(fragment);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameContainer, fragments.get(fragments.size()-1));
        fragmentTransaction.commit();
    }

    public void novoFrame(Fragment fragment) {
        if (fragments.contains(fragment))
            fragments.remove(fragment);
        fragments.add(fragment);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameContainer, fragment);
        fragmentTransaction.commit();
    }

    public void telaInicial(){
        fragments.clear();
        TelaInicialFragment fragment = new TelaInicialFragment();
        novoFrame(fragment);
    }

    public  void limparFragments(){
        fragments.clear();
        TelaInicialFragment fragment = new TelaInicialFragment();
        fragments.add(fragment);
    }

    public void setIcones(){


       for(int i=0;i<146;i++){
            arrayList.add(2131165504+i);
        }

        //Log.i("INFO","ID 181: "+arrayList.get(171));
        Log.i("INFO","ID IMG: "+R.drawable.icone_001);
        Log.i("INFO","ID IMG: "+R.drawable.icone_146);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isAppRunning = false;
    }
}
