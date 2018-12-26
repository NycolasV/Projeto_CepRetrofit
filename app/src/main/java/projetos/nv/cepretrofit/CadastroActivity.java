package projetos.nv.cepretrofit;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import projetos.nv.cepretrofit.validations.Validator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroActivity extends AppCompatActivity {

    private EditText nomeCompleto, cpf;
    private EditText cep, numero;
    private TextView logradouro;
    private TextView dataNascimento;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private Button btnBuscarCep, btnPesquisarMapa;
    private Button btnCancelar, btnCadastrarCliente;
    private CadastroHelper helper;
    private CadastroDAO cadastroDAO;
    private Long enderecoId;
    private String cepComparacao;
    private Boolean cepErro;
    private Boolean btnBuscarCepUsado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        cadastroDAO = new CadastroDAO(getApplicationContext());
        helper = new CadastroHelper(this);

        nomeCompleto = helper.getNomeCompleto();
        cpf = helper.getCpf();
        dataNascimento = helper.getDataNascimento();

        cep = helper.getCep();
        numero = helper.getNumero();
        logradouro = helper.getLogradouro();

        btnBuscarCep = helper.getBtnBuscarCep();
        btnPesquisarMapa = helper.getBtnPesquisarMapa();
        btnCancelar = helper.getBtnCancelar();
        btnCadastrarCliente = helper.getBtnCadastrarCliente();

        // Elemento que seleciona calendário para a data de nascimento
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

        // Botão para buscar endereço a partir do CEP inserido, através do Retrofit
        btnBuscarCep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Endereco> call = new RetrofitConfig().getEnderecoService().buscarEndereco(cep.getText().toString());
                call.enqueue(new Callback<Endereco>() {
                    @Override
                    public void onResponse(Call<Endereco> call, Response<Endereco> response) {
                        btnBuscarCepUsado = true;
                        try {
                            // Validação de elemento nulo
                            boolean cepNotNull = Validator.validateNotNull(cep);
                            if (!cepNotNull) {
                                cep.setError("Digite o CEP!");
                                cep.setFocusable(true);
                                cep.requestFocus();
                                throw new Exception();
                            }
                            // Validação de CEP sem o tamanho correto de 8 dígitos
                            boolean cepValido = Validator.validateCEP(cep.getText().toString());
                            if (!cepValido) {
                                cep.setError("CEP incorreto!");
                                cep.setFocusable(true);
                                cep.requestFocus();
                                throw new Exception();
                            }

                            Endereco endereco = response.body();
                            // Validação de CEP não existente
                            cepErro = endereco.getErro();
                            if (cepErro != null) {
                                cep.setError("CEP inválido!");
                                throw new Exception();
                            }

                            if (enderecoId != null) {
                                endereco.setId(enderecoId);
                                helper.consultarCep(endereco);
                            } else {
                                helper.consultarCep(endereco);
                            }
                        } catch (Exception e) {
                            System.out.println("------------------------");
                            System.out.println("Erro value: " + e);
                            System.out.println("------------------------");
                        }
                    }

                    @Override
                    public void onFailure(Call<Endereco> call, Throwable t) {
                        Log.e("CEPService", "Erro ao buscar o cep:" + t.getMessage());
                    }
                });
            }
        });

        // Botão para pesquisar o endereço inserido no aplicativo de mapa do celular
        btnPesquisarMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Validação de elemento nulo
                    boolean cepNotNull = Validator.validateNotNull(cep);
                    if (!cepNotNull) {
                        cep.setError("Digite o CEP!");
                        cep.setFocusable(true);
                        cep.requestFocus();
                        throw new Exception();
                    }
                    // Validação de CEP sem o tamanho correto de 8 dígitos
                    boolean cepValido = Validator.validateCEP(cep.getText().toString());
                    if (!cepValido) {
                        cep.setError("CEP incompleto!");
                        cep.setFocusable(true);
                        cep.requestFocus();
                        throw new Exception();
                    }
                    // Validação de CEP não buscado
                    // caso o elemento já exista, é verificado a última busca para a validação
                    // caso não, é verificado se o botão de buscarCep foi usado e depois a última busca
                    if (enderecoId != null) {
                        if (!cepComparacao.equals(cep.getText().toString())) {
                            cepComparacao = cep.getText().toString();
                            cep.setError("CEP não buscado!");
                            cep.setFocusable(true);
                            cep.requestFocus();
                            throw new Exception();
                        }
                    } else {
                        if (cepComparacao == null) {
                            if (!btnBuscarCepUsado) {
                                cepComparacao = cep.getText().toString();
                                cep.setError("CEP não buscado!");
                                cep.setFocusable(true);
                                cep.requestFocus();
                                throw new Exception();
                            }
                        } else if (!cepComparacao.equals(cep.getText().toString())) {
                            cepComparacao = cep.getText().toString();
                            cep.setError("CEP não buscado!");
                            cep.setFocusable(true);
                            cep.requestFocus();
                            throw new Exception();
                        }
                    }
                    // Validação de CEP não existente
                    if (cepErro != null) {
                        cep.setError("CEP inválido!");
                        throw new Exception();
                    }
                    // Validação de elemento nulo
                    boolean numeroNotNull = Validator.validateNotNull(numero);
                    if (!numeroNotNull) {
                        numero.setError("Digite o número!");
                        numero.setFocusable(true);
                        numero.requestFocus();
                        throw new Exception();
                    }

                    String numeroStr = numero.getText().toString();
                    String logradouroStr = logradouro.getText().toString();
                    String cepStr = cep.getText().toString();

                    // Uri location com sintaxe: geo:0,0?q=número+rua+cep
                    String enderecoPesquisado = numeroStr + ", " + logradouroStr + ", " + cepStr;
                    Uri location = Uri.parse("geo:0,0?q=" + Uri.encode(enderecoPesquisado));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                    startActivity(mapIntent);
                } catch (Exception e) {
                    System.out.println("------------------------");
                    System.out.println("Erro value: " + e);
                    System.out.println("------------------------");
                }
            }
        });

        // Caso o elemento já exista, é inserido seus dados na página de cadastro
        final Bundle extras = getIntent().getExtras();
        Long clienteId = (extras != null) ? extras.getLong("clienteId") : null;
        if (clienteId != null) {
            Cliente clienteClicked = cadastroDAO.buscarCliente(clienteId);
            helper.preencherCliente(clienteClicked);
            enderecoId = clienteClicked.getEndereco().getId();
            cepComparacao = clienteClicked.getEndereco().getCep();
        }

        // Botão para cadastro do cliente
        btnCadastrarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Validação de elemento nulo
                    boolean nomeCompletoNotNull = Validator.validateNotNull(nomeCompleto);
                    if (!nomeCompletoNotNull) {
                        nomeCompleto.setError("Digite o nome!");
                        nomeCompleto.setFocusable(true);
                        nomeCompleto.requestFocus();
                        throw new Exception();
                    }
                    // Validação de elemento nulo
                    boolean cpfNotNull = Validator.validateNotNull(cpf);
                    if (!cpfNotNull) {
                        cpf.setError("Digite o CPF!");
                        cpf.setFocusable(true);
                        cpf.requestFocus();
                        throw new Exception();
                    }
                    // Validação de CPF sem o tamanho correto de 11 dígitos
                    boolean cpfValido = Validator.validateCPF(cpf.getText().toString());
                    if (!cpfValido) {
                        cpf.setError("CPF inválido");
                        cpf.setFocusable(true);
                        cpf.requestFocus();
                        throw new Exception();
                    }
                    // Validação de elemento nulo e se a data de nascimento é maior que a data atual
                    boolean dataNascimentoValido = Validator.validateDataNascimento(dataNascimento.getText().toString());
                    if (!dataNascimentoValido) {
                        dataNascimento.setText("Data não selecionada ou inválida!");
                        dataNascimento.setTextColor(getResources().getColor(R.color.colorRed));
                        throw new Exception();
                    } else {
                        dataNascimento.setTextColor(getResources().getColor(R.color.colorBlack));
                    }
                    // Validação de elemento nulo
                    boolean cepNotNull = Validator.validateNotNull(cep);
                    if (!cepNotNull) {
                        cep.setError("Digite o CEP!");
                        cep.setFocusable(true);
                        cep.requestFocus();
                        throw new Exception();
                    }
                    // Validação de CEP sem o tamanho correto de 8 dígitos
                    boolean cepValido = Validator.validateCEP(cep.getText().toString());
                    if (!cepValido) {
                        cep.setError("CEP incompleto!");
                        cep.setFocusable(true);
                        cep.requestFocus();
                        throw new Exception();
                    }
                    // Validação de CEP não buscado
                    // caso o elemento já exista, é verificado a última busca para a validação
                    // caso não, é verificado se o botão de buscarCep foi usado e depois a última busca
                    if (enderecoId != null) {
                        if (!cepComparacao.equals(cep.getText().toString())) {
                            cepComparacao = cep.getText().toString();
                            cep.setError("CEP não buscado!");
                            cep.setFocusable(true);
                            cep.requestFocus();
                            throw new Exception();
                        }
                    } else {
                        if (cepComparacao == null) {
                            if (!btnBuscarCepUsado) {
                                cepComparacao = cep.getText().toString();
                                cep.setError("CEP não buscado!");
                                cep.setFocusable(true);
                                cep.requestFocus();
                                throw new Exception();
                            }
                        } else if (!cepComparacao.equals(cep.getText().toString())) {
                            cepComparacao = cep.getText().toString();
                            cep.setError("CEP não buscado!");
                            cep.setFocusable(true);
                            cep.requestFocus();
                            throw new Exception();
                        }
                    }
                    // Validação de CEP não existente
                    if (cepErro != null) {
                        cep.setError("CEP inválido!");
                        throw new Exception();
                    }
                    // Validação de elemento nulo
                    boolean numeroNotNull = Validator.validateNotNull(numero);
                    if (!numeroNotNull) {
                        numero.setError("Digite o número!");
                        numero.setFocusable(true);
                        numero.requestFocus();
                        throw new Exception();
                    }
                    cadastroDAO = new CadastroDAO(CadastroActivity.this);
                    Endereco endereco = helper.inserirEndereco();
                    Cliente cliente = helper.inserirCliente(endereco);

                    // Caso o elemento já exista, irá ser alterado
                    // caso não, irá ser cadastrado
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
                } catch (Exception e) {
                    System.out.println("------------------------");
                    System.out.println("Erro value: " + e);
                    System.out.println("------------------------");
                }

            }
        });

        // Botão para cancelar ação na tela de cadastro
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
