package projetos.nv.cepretrofit;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import projetos.nv.cepretrofit.config.RetrofitConfig;
import projetos.nv.cepretrofit.dao.CadastroDAO;
import projetos.nv.cepretrofit.helper.CadastroHelper;
import projetos.nv.cepretrofit.models.Cliente;
import projetos.nv.cepretrofit.models.Endereco;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroActivity extends AppCompatActivity {

    private EditText cep;
    private TextView dataNascimento;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private Button btnBuscarCep;
    private Button btnCadastrarCliente;
    private CadastroHelper helper;
    private CadastroDAO cadastroDAO;
    private Long enderecoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        cadastroDAO = new CadastroDAO(getApplicationContext());
        helper = new CadastroHelper(this);
        cep = helper.getCep();
        dataNascimento = helper.getDataNascimento();
        btnBuscarCep = helper.getBtnBuscarCep();
        btnCadastrarCliente = helper.getBtnCadastrarCliente();

        dataNascimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendario = Calendar.getInstance();
                int year = calendario.get(Calendar.YEAR);
                int month = calendario.get(Calendar.MONTH);
                int day = calendario.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(CadastroActivity.this,
                        android.R.style.Theme_Black, dateSetListener, year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String dataString = dayOfMonth + "/" + month + "/" + year;
                dataNascimento.setText(dataString);
            }
        };

        btnBuscarCep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Endereco> call = new RetrofitConfig().getEnderecoService().buscarEndereco(cep.getText().toString());
                call.enqueue(new Callback<Endereco>() {
                    @Override
                    public void onResponse(Call<Endereco> call, Response<Endereco> response) {
                        Endereco endereco = response.body();
                        if (enderecoId != null){
                            endereco.setId(enderecoId);
                            helper.consultarCep(endereco);
                        } else {
                            helper.consultarCep(endereco);
                        }
                    }

                    @Override
                    public void onFailure(Call<Endereco> call, Throwable t) {
                        Log.e("CEPService", "Erro ao buscar o cep:" + t.getMessage());
                    }
                });
            }
        });

        final Bundle extras = getIntent().getExtras();
        Long clienteId = (extras != null) ? extras.getLong("clienteId") : null;
        if (clienteId != null) {
            Cliente clienteClicked = cadastroDAO.buscarCliente(clienteId);
            enderecoId = clienteClicked.getEndereco().getId();
            helper.preencherCliente(clienteClicked);
        }

        btnCadastrarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastroDAO = new CadastroDAO(CadastroActivity.this);
                Endereco endereco = helper.inserirEndereco();
                Cliente cliente = helper.inserirCliente(endereco);

                if (cliente.getId() != null) {
                    cadastroDAO.alterarEndereco(endereco);
                    cadastroDAO.alterarCLiente(cliente);
                } else {
                    cadastroDAO.persistirEndereco(endereco);
                    cadastroDAO.persistirCliente(cliente);
                }

                cadastroDAO.close();
                Toast.makeText(getApplicationContext(), "Cliente " + cliente.getNomeCompleto() + " cadastrado", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
