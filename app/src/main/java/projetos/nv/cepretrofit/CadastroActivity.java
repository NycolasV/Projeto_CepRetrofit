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

    private EditText cep;
    private TextView resposta;
    private Button btnBuscarCep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        cep = findViewById(R.id.etMain_cep);
        resposta = findViewById(R.id.etMain_resposta);
        btnBuscarCep = findViewById(R.id.btnMain_buscarCep);
        btnBuscarCep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Endereco> call = new RetrofitConfig().getEnderecoService().buscarEndereco(cep.getText().toString());
                call.enqueue(new Callback<Endereco>() {
                    @Override
                    public void onResponse(Call<Endereco> call, Response<Endereco> response) {
                        Endereco endereco = response.body();
                        resposta.setText(endereco.toString());
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
