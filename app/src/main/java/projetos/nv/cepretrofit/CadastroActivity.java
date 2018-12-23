package projetos.nv.cepretrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import projetos.nv.cepretrofit.config.RetrofitConfig;
import projetos.nv.cepretrofit.models.Endereco;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroActivity extends AppCompatActivity {

    private EditText nomeCompleto, cpf, dataNascimento;
    private EditText cep, numero, complemento;
    private TextView logradouro, bairro, localidade, uf;
    private Button btnBuscarCep;
    private Button btnCadastrarCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        nomeCompleto = findViewById(R.id.nomeCompletoId);
        cpf = findViewById(R.id.cpfId);
        dataNascimento = findViewById(R.id.dataNascimentoId);

        cep = findViewById(R.id.cepId);
        numero = findViewById(R.id.numeroId);
        complemento = findViewById(R.id.complementoId);
        logradouro = findViewById(R.id.logradouroId);
        bairro = findViewById(R.id.bairroId);
        localidade = findViewById(R.id.localidadeId);
        uf = findViewById(R.id.ufId);

        btnBuscarCep = findViewById(R.id.btnBuscarCep);
        btnCadastrarCliente = findViewById(R.id.btnCadastrarCliente);

        btnBuscarCep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Endereco> call = new RetrofitConfig().getEnderecoService().buscarEndereco(cep.getText().toString());
                call.enqueue(new Callback<Endereco>() {
                    @Override
                    public void onResponse(Call<Endereco> call, Response<Endereco> response) {
                        Endereco endereco = response.body();
                        logradouro.setText(endereco.getLogradouro());
                        bairro.setText(endereco.getBairro());
                        localidade.setText(endereco.getLocalidade());
                        uf.setText(endereco.getUf());
                    }

                    @Override
                    public void onFailure(Call<Endereco> call, Throwable t) {
                        Log.e("CEPService", "Erro ao buscar o cep:" + t.getMessage());
                    }
                });
            }
        });
    }
}
