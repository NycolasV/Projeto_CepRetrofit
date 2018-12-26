package projetos.nv.cepretrofit.holder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import projetos.nv.cepretrofit.CadastroActivity;
import projetos.nv.cepretrofit.MainActivity;
import projetos.nv.cepretrofit.R;
import projetos.nv.cepretrofit.adapter.RecyclerViewAdapter;
import projetos.nv.cepretrofit.dao.CadastroDAO;
import projetos.nv.cepretrofit.models.Cliente;
import projetos.nv.cepretrofit.models.Endereco;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private final RecyclerViewAdapter adapter;
    private Context context;
    private Long clienteId;
    private TextView nomeCompleto, cpf;
    private TextView dataNascimento, cep;
    private CadastroDAO dao;

    public RecyclerViewHolder(final View view, RecyclerViewAdapter adapter, final Context context) {
        super(view);
        this.adapter = adapter;
        this.context = context;

        nomeCompleto = view.findViewById(R.id.cardNomeCompleto);
        cpf = view.findViewById(R.id.cardCpf);
        dataNascimento = view.findViewById(R.id.cardDataNascimento);
        cep = view.findViewById(R.id.cardCep);

        // Envia o ID do cliente para a tela de cadastro
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Activity context = (Activity) v.getContext();
                final Intent intent = new Intent(context, CadastroActivity.class);
                intent.putExtra("clienteId", clienteId);
                context.startActivityForResult(intent, 1);
            }
        });

        // Ao segurar o card, ele abre uma opção para deletar o elemento
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                PopupMenu popup = new PopupMenu(context, view);
                popup.inflate(R.menu.menu_main);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.itemDeletar:
                                dao = new CadastroDAO(context);
                                Cliente clienteDeletado = dao.buscarCliente(clienteId);
                                dao.removerEndereco(clienteDeletado.getEndereco());
                                dao.removerCliente(clienteDeletado);
                                break;
                        }
                        final Activity context = (Activity) v.getContext();
                        final Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("delete", true);
                        context.startActivityForResult(intent, 1);
                        return false;
                    }
                });
                popup.show();
                return true;
            }
        });
    }

    /**
     * Método para inserir os dados resgatados do cliente no CardView
     */
    public void preencher(Cliente cliente) {
        clienteId = cliente.getId();
        nomeCompleto.setText(cliente.getNomeCompleto());
        cpf.setText(cliente.getCpf());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dataNascimento.setText(dateFormat.format(cliente.getDataNascimento()));
        cep.setText(cliente.getEndereco().getCep());
    }
}
