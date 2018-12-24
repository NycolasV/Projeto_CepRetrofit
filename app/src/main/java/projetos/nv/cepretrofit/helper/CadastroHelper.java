package projetos.nv.cepretrofit.helper;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import projetos.nv.cepretrofit.CadastroActivity;
import projetos.nv.cepretrofit.R;
import projetos.nv.cepretrofit.models.Cliente;
import projetos.nv.cepretrofit.models.Endereco;

public class CadastroHelper {

    private EditText nomeCompleto, cpf;
    private TextView dataNascimento;
    private EditText cep, numero, complemento;
    private TextView logradouro, bairro, localidade, uf;
    private Button btnBuscarCep;
    private Button btnCadastrarCliente;
    private Endereco endereco;
    private Cliente cliente;

    public Button getBtnBuscarCep() {
        return btnBuscarCep;
    }

    public void setBtnBuscarCep(Button btnBuscarCep) {
        this.btnBuscarCep = btnBuscarCep;
    }

    public Button getBtnCadastrarCliente() {
        return btnCadastrarCliente;
    }

    public void setBtnCadastrarCliente(Button btnCadastrarCliente) {
        this.btnCadastrarCliente = btnCadastrarCliente;
    }

    public TextView getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(TextView dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public EditText getCep() {
        return cep;
    }

    public void setCep(EditText cep) {
        this.cep = cep;
    }

    public CadastroHelper(CadastroActivity activity) {
        nomeCompleto = activity.findViewById(R.id.nomeCompletoId);
        cpf= activity.findViewById(R.id.cpfId);
        dataNascimento = activity.findViewById(R.id.dataNascimentoId);

        cep = activity.findViewById(R.id.cepId);
        numero = activity.findViewById(R.id.numeroId);
        complemento = activity.findViewById(R.id.complementoId);
        logradouro = activity.findViewById(R.id.logradouroId);
        bairro = activity.findViewById(R.id.bairroId);
        localidade = activity.findViewById(R.id.localidadeId);
        uf = activity.findViewById(R.id.ufId);

        btnBuscarCep = activity.findViewById(R.id.btnBuscarCep);
        btnCadastrarCliente = activity.findViewById(R.id.btnCadastrarCliente);

        endereco = new Endereco();
        cliente = new Cliente();
    }

    public Endereco inserirEndereco(){
        endereco.setCep(cep.getText().toString());
        int numeroInt = Integer.parseInt(numero.getText().toString());
        endereco.setNumero(numeroInt);
        endereco.setComplemento(complemento.getText().toString());
        endereco.setLogradouro(logradouro.getText().toString());
        endereco.setBairro(bairro.getText().toString());
        endereco.setLocalidade(localidade.getText().toString());
        endereco.setUf(uf.getText().toString());

        return endereco;
    }
    
    public void preencherEndereco(Endereco enderecoRecover){
        cep.setText(enderecoRecover.getCep());
        numero.setText(enderecoRecover.getNumero());
        complemento.setText(enderecoRecover.getComplemento());
        logradouro.setText(enderecoRecover.getLogradouro());
        bairro.setText(enderecoRecover.getBairro());
        localidade.setText(enderecoRecover.getLocalidade());
        uf.setText(enderecoRecover.getUf());

        this.endereco = enderecoRecover;
    }

    public void consultarCep(Endereco enderecoResponse){
        logradouro.setText(enderecoResponse.getLogradouro());
        bairro.setText(enderecoResponse.getBairro());
        localidade.setText(enderecoResponse.getLocalidade());
        uf.setText(enderecoResponse.getUf());

        this.endereco = enderecoResponse;
    }
    
    public Cliente inserirCliente(Endereco endereco){
        cliente.setNomeCompleto(nomeCompleto.getText().toString());
        cliente.setCpf(cpf.getText().toString());
        cliente.setEndereco(endereco);
        String dateString = dataNascimento.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = format.parse(dateString);
            cliente.setDataNascimento(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return cliente;
    }

    public void preencherCliente(Cliente clienteRecover){
        nomeCompleto.setText(clienteRecover.getNomeCompleto());
        cpf.setText(clienteRecover.getCpf());
        preencherEndereco(clienteRecover.getEndereco());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dataNascimento.setText(dateFormat.format(clienteRecover.getDataNascimento()));

        this.cliente = clienteRecover;
    }
}
