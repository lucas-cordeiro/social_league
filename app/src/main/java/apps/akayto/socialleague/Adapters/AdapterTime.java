package apps.akayto.socialleague.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import apps.akayto.socialleague.Models.Membros;
import apps.akayto.socialleague.Models.Time;
import apps.akayto.socialleague.R;

/**
 * Created by LUCASGABRIELALVESCOR on 19/03/2018.
 */

public class AdapterTime extends RecyclerView.Adapter<AdapterTime.MyViewHolder> {

    private ArrayList<Time> timeArrayList = new ArrayList<>();

    public AdapterTime(ArrayList<Time> timeArrayList) {
        this.timeArrayList = timeArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_recyclerview, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        int image=0;
            if(timeArrayList.get(position).getDivisao().equals("3ª Divisão")){
                image = R.drawable.div3;
            }

        holder.img.setBackgroundResource(image);
        holder.nome.setText(timeArrayList.get(position).getNome());
        holder.lider.setText(holder.lider.getText()+" "+timeArrayList.get(position).getLider());
        holder.quantMembros.setText(holder.quantMembros.getText()+" "+timeArrayList.get(position).getQuantMembros()+"/7");
        holder.campeonatos.setText(holder.campeonatos.getText()+" "+timeArrayList.get(position).getCampeonatos());
        holder.vitorias.setText(holder.vitorias.getText()+" "+timeArrayList.get(position).getVitorias());

    }

    @Override
    public int getItemCount() {
        return timeArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView nome;
        TextView lider;
        TextView quantMembros;
        TextView campeonatos;
        TextView vitorias;

        public MyViewHolder(View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.time_ImgIcoce);
            nome = itemView.findViewById(R.id.time_TxtNome);
            lider = itemView.findViewById(R.id.time_TxtLider);
            quantMembros = itemView.findViewById(R.id.time_TxtQuantMembros);
            campeonatos = itemView.findViewById(R.id.time_TxtCampeonatos);
            vitorias = itemView.findViewById(R.id.time_TxtVitorias);
        }
    }
}
