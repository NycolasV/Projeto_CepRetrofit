package projetos.nv.cepretrofit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import projetos.nv.cepretrofit.R;
import projetos.nv.cepretrofit.holder.RecyclerViewHolder;
import projetos.nv.cepretrofit.models.Cliente;

public class RecyclerViewAdapter extends RecyclerView.Adapter{

    private final Context context;
    private final List<Cliente> clientes;

    public RecyclerViewAdapter(Context context, List<Cliente> clientes) {
        this.context = context;
        this.clientes = clientes;
    }

    /**
     * Métodos para enviar os dados recebidos da MainActivity para o RecyclerViewHolder
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_cliente, viewGroup, false);
        RecyclerViewHolder holder = new RecyclerViewHolder(view, this, context);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        RecyclerViewHolder rvHolder = (RecyclerViewHolder) viewHolder;
        Cliente cliente = clientes.get(i);
        rvHolder.preencher(cliente);
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }
}
