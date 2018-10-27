package apps.akayto.socialleague.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import apps.akayto.socialleague.Models.Membros;
import apps.akayto.socialleague.R;

/**
 * Created by LUCASGABRIELALVESCOR on 19/03/2018.
 */

public class AdapterMembros extends RecyclerView.Adapter<AdapterMembros.MyViewHolder> {

    private ArrayList<Membros> arrayListMembros = new ArrayList<>();

    private ArrayList<Integer> arrayList = new ArrayList<>();

    public AdapterMembros(ArrayList<Membros> arrayListMembros) {
        this.arrayListMembros = arrayListMembros;
        setIcones();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.membro_recyclerview, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.img.setBackgroundResource(arrayList.get(arrayListMembros.get(position).getIcone()));
        holder.nome.setText(arrayListMembros.get(position).getNome());
        holder.campeonatos.setText(holder.campeonatos.getText()+" "+arrayListMembros.get(position).getCampeonatos());
        holder.vitorias.setText(holder.vitorias.getText()+" "+arrayListMembros.get(position).getVitorias());

    }

    @Override
    public int getItemCount() {
        return arrayListMembros.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView nome;
        TextView campeonatos;
        TextView vitorias;

        public MyViewHolder(View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.membro_ImgIcoce);
            nome = itemView.findViewById(R.id.membro_TxtNome);
            campeonatos = itemView.findViewById(R.id.membro_TxtCampeonatos);
            vitorias = itemView.findViewById(R.id.membro_TxtVitorias);
        }
    }

    public void setIcones(){


        for(int i=0;i<146;i++){
            arrayList.add(2131165504+i);
        }

        //Log.i("INFO","ID 181: "+arrayList.get(171));
        Log.i("INFO","ID IMG: "+R.drawable.icone_001);
        Log.i("INFO","ID IMG: "+R.drawable.icone_146);
    }
}
