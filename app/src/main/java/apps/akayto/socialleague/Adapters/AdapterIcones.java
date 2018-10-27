package apps.akayto.socialleague.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import apps.akayto.socialleague.R;

/**
 * Created by LUCASGABRIELALVESCOR on 14/03/2018.
 */

public class AdapterIcones extends RecyclerView.Adapter<AdapterIcones.MyViewHolder>{

    private ArrayList<Integer> arrayList = new ArrayList<>();

    public AdapterIcones(ArrayList<Integer> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.icones_recyclerview, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.icone.setBackgroundResource(arrayList.get(position));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView icone;

        public MyViewHolder(View itemView) {

            super(itemView);

            icone = itemView.findViewById(R.id.icone);
        }
    }
}
