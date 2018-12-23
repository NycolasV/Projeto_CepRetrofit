package projetos.nv.cepretrofit.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import projetos.nv.cepretrofit.models.Cliente;
import projetos.nv.cepretrofit.models.Endereco;

public class CadastroDAO extends SQLiteOpenHelper {

    /**
     * Nome do banco de dados
     */
    public static final String NOME_BANCO = "db_cep_retrofit";

    /**
     * Tabela de enderecos
     */
    public static final String NOME_TABELA_ENDERECO = "tb_endereco";
    public static final String COLUNA_ID_ENDERECO = "id";
    public static final String COLUNA_CEP = "cep";
    public static final String COLUNA_NUMERO = "numero";
    public static final String COLUNA_COMPLEMENTO = "complemento";
    public static final String COLUNA_LOGRADOURO = "logradouro";
    public static final String COLUNA_BAIRRO = "bairro";
    public static final String COLUNA_LOCALIDADE = "localidade";
    public static final String COLUNA_UF = "uf";

    public static final String CREATE_TB_ENDERECO = "CREATE TABLE " + NOME_TABELA_ENDERECO + "("
            + COLUNA_ID_ENDERECO + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUNA_CEP + " TEXT,"
            + COLUNA_NUMERO + " INTEGER," + COLUNA_COMPLEMENTO + " TEXT," + COLUNA_LOGRADOURO + " TEXT,"
            + COLUNA_BAIRRO + " TEXT," + COLUNA_LOCALIDADE + " TEXT," + COLUNA_UF + " TEXT" + ")";

    public static final String UPDATE_TB_ENDERECO =  "DROP TABLE IF EXISTS " + NOME_TABELA_ENDERECO;

    /**
     * Tabela de clientes
     */
    public static final String NOME_TABELA_CLIENTE = "tb_cliente";
    public static final String COLUNA_ID_CLIENTE = "id";
    public static final String COLUNA_NOME_COMPLETO = "nomeCompleto";
    public static final String COLUNA_CPF = "cpf";
    public static final String COLUNA_DATA_NASCIMENTO = "dataNascimento";
    public static final String COLUNA_ENDERECO_ID = "endereco_id";

    public static final String CREATE_TB_CLIENTE = "CREATE TABLE " + NOME_TABELA_CLIENTE + "("
            + COLUNA_ID_CLIENTE + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUNA_NOME_COMPLETO + " TEXT,"
            + COLUNA_CPF + " TEXT," + COLUNA_DATA_NASCIMENTO + " TEXT," + COLUNA_ENDERECO_ID + " INTEGER,"
            + "CONSTRAINT FK_endereco FOREIGN KEY(" + COLUNA_ENDERECO_ID + ") REFERENCES " + NOME_TABELA_ENDERECO + " (id))";

    public static final String UPDATE_TB_CLIENTE =  "DROP TABLE IF EXISTS " + NOME_TABELA_CLIENTE;

    /**
     * Construtor da classe, cria o banco de dados
     */
    public CadastroDAO(Context context) {
        super(context, NOME_BANCO, null, 1);
    }

    /**
     * Método onCreate para criar e inserir as tabelas no banco de dados 
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TB_ENDERECO);
        db.execSQL(CREATE_TB_CLIENTE);
    }

    /**
     * Método onUpgrade para atualizar as tabelas do banco de dados
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(UPDATE_TB_ENDERECO);
        db.execSQL(UPDATE_TB_CLIENTE);
    }

    @NonNull
    private ContentValues contentValuesEndereco(Endereco endereco) {
        ContentValues dados = new ContentValues();
        dados.put(COLUNA_CEP, endereco.getCep());
        dados.put(COLUNA_NUMERO, endereco.getNumero());
        dados.put(COLUNA_COMPLEMENTO, endereco.getComplemento());
        dados.put(COLUNA_LOGRADOURO, endereco.getLogradouro());
        dados.put(COLUNA_BAIRRO, endereco.getBairro());
        dados.put(COLUNA_LOCALIDADE, endereco.getLocalidade());
        dados.put(COLUNA_UF, endereco.getUf());

        return dados;
    }

    public void persistirEndereco(Endereco endereco) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = contentValuesEndereco(endereco);
        db.insert(NOME_TABELA_ENDERECO,null, dados);
    }

    public void removerEndereco(Endereco endereco) {
        SQLiteDatabase db = getWritableDatabase();
        String[] parametros = {endereco.getId().toString()};
        db.delete(NOME_TABELA_ENDERECO,"id = ?",parametros);
    }

    public void alterarEndereco(Endereco endereco) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = contentValuesEndereco(endereco);
        String[] parametros = {endereco.getId().toString()};
        db.update(NOME_TABELA_ENDERECO, dados,"id = ?", parametros);
    }

    public Endereco buscarEndereco(Long enderecoId) {
        String sql = "SELECT * FROM " + NOME_TABELA_ENDERECO + " WHERE id = ?";
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(sql,new String[]{String.valueOf(enderecoId)});
        c.moveToFirst();

        Endereco endereco = new Endereco();
        endereco.setId(c.getLong(c.getColumnIndex(COLUNA_ID_ENDERECO)));
        endereco.setCep(c.getString(c.getColumnIndex(COLUNA_CEP)));
        endereco.setNumero(c.getInt(c.getColumnIndex(COLUNA_NUMERO)));
        endereco.setComplemento(c.getString(c.getColumnIndex(COLUNA_COMPLEMENTO)));
        endereco.setLogradouro(c.getString(c.getColumnIndex(COLUNA_LOGRADOURO)));
        endereco.setBairro(c.getString(c.getColumnIndex(COLUNA_BAIRRO)));
        endereco.setLocalidade(c.getString(c.getColumnIndex(COLUNA_LOCALIDADE)));
        endereco.setUf(c.getString(c.getColumnIndex(COLUNA_UF)));

        db.close();
        return endereco;
    }

    @NonNull
    private ContentValues contentValuesCliente(Cliente cliente) {
        ContentValues dados = new ContentValues();
        dados.put(COLUNA_NOME_COMPLETO, cliente.getNomeCompleto());
        dados.put(COLUNA_CPF, cliente.getCpf());
        dados.put(COLUNA_ENDERECO_ID, cliente.getEndereco().getId());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dados.put(COLUNA_DATA_NASCIMENTO, dateFormat.format(cliente.getDataNascimento()));

        return dados;
    }

    public void persistirCliente(Cliente cliente) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = contentValuesCliente(cliente);
        db.insert(NOME_TABELA_CLIENTE,null, dados);
    }

    public void removerCliente(Cliente cliente) {
        SQLiteDatabase db = getWritableDatabase();
        String[] parametros = {cliente.getId().toString()};
        db.delete(NOME_TABELA_CLIENTE,"id = ?",parametros);
    }

    public void alterarCLiente(Cliente cliente) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = contentValuesCliente(cliente);
        String[] parametros = {cliente.getId().toString()};
        db.update(NOME_TABELA_CLIENTE, dados,"id = ?", parametros);
    }

    public List<Cliente> buscarTodosClientes() {
        String sql = "SELECT * FROM " + NOME_TABELA_CLIENTE;
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(sql, null);
        List<Cliente> clientes = new ArrayList<Cliente>();

        while (c.moveToNext()){
            Cliente cliente = new Cliente();

            cliente.setId(c.getLong(c.getColumnIndex(COLUNA_ID_CLIENTE)));
            cliente.setNomeCompleto(c.getString(c.getColumnIndex(COLUNA_NOME_COMPLETO)));
            cliente.setCpf(c.getString(c.getColumnIndex(COLUNA_CPF)));

            // Convertendo String para date
            String dateString = c.getString(c.getColumnIndex(COLUNA_DATA_NASCIMENTO));
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date date = format.parse(dateString);
                cliente.setDataNascimento(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Pegando o endereco da tabela tb_enderecos
            cliente.setEndereco(buscarEndereco(c.getLong(c.getColumnIndex(COLUNA_ENDERECO_ID))));

            clientes.add(cliente);
        }

        return clientes;
    }

    public Cliente buscarCliente(Long clienteId) {
        String sql = "SELECT * FROM " + NOME_TABELA_CLIENTE + " WHERE id = ?";
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(sql,new String[]{String.valueOf(clienteId)});
        c.moveToFirst();

        Cliente cliente = new Cliente();

        cliente.setId(c.getLong(c.getColumnIndex(COLUNA_ID_CLIENTE)));
        cliente.setNomeCompleto(c.getString(c.getColumnIndex(COLUNA_NOME_COMPLETO)));
        cliente.setCpf(c.getString(c.getColumnIndex(COLUNA_CPF)));

        // Convertendo String para date
        String dateString = c.getString(c.getColumnIndex(COLUNA_DATA_NASCIMENTO));
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = format.parse(dateString);
            cliente.setDataNascimento(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Pegando o endereco da tabela tb_enderecos
        cliente.setEndereco(buscarEndereco(c.getLong(c.getColumnIndex(COLUNA_ENDERECO_ID))));

        db.close();

        return cliente;
    }

    public List<Cliente> buscarPorNome(String textoPesquisado) {
        SQLiteDatabase db = getWritableDatabase();

        try {
            String sql = "SELECT * FROM " + NOME_TABELA_CLIENTE + " WHERE " + COLUNA_NOME_COMPLETO + " Like'"+ textoPesquisado +"%'";

            Cursor c = db.rawQuery(sql, null);
            List<Cliente> clienteProcurado = new ArrayList<>();

            while (c.moveToNext()){
                Cliente cliente = new Cliente();

                cliente.setId(c.getLong(c.getColumnIndex(COLUNA_ID_CLIENTE)));
                cliente.setNomeCompleto(c.getString(c.getColumnIndex(COLUNA_NOME_COMPLETO)));
                cliente.setCpf(c.getString(c.getColumnIndex(COLUNA_CPF)));

                // Convertendo String para date
                String dateString = c.getString(c.getColumnIndex(COLUNA_DATA_NASCIMENTO));
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date = format.parse(dateString);
                    cliente.setDataNascimento(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Pegando o endereco da tabela tb_enderecos
                cliente.setEndereco(buscarEndereco(c.getLong(c.getColumnIndex(COLUNA_ENDERECO_ID))));

                clienteProcurado.add(cliente);
            }

            c.close();
            return  clienteProcurado;
        }finally {
            db.close();
        }
    }
}
