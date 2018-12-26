package projetos.nv.cepretrofit.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

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
     * Ativando a chave estrangeira
     */
    public static final String FK_ON = "PRAGMA foreign_keys=ON";

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
            + COLUNA_ID_ENDERECO + " INTEGER PRIMARY KEY," + COLUNA_CEP + " TEXT NOT NULL,"
            + COLUNA_NUMERO + " INTEGER NOT NULL," + COLUNA_COMPLEMENTO + " TEXT,"
            + COLUNA_LOGRADOURO + " TEXT NOT NULL," + COLUNA_BAIRRO + " TEXT NOT NULL," + COLUNA_LOCALIDADE
            + " TEXT NOT NULL," + COLUNA_UF + " TEXT NOT NULL" + ")";

    public static final String UPDATE_TB_ENDERECO = "DROP TABLE IF EXISTS " + NOME_TABELA_ENDERECO;

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
            + COLUNA_ID_CLIENTE + " INTEGER PRIMARY KEY," + COLUNA_NOME_COMPLETO + " TEXT NOT NULL,"
            + COLUNA_CPF + " TEXT NOT NULL," + COLUNA_DATA_NASCIMENTO + " TEXT NOT NULL," + COLUNA_ENDERECO_ID + " INTEGER)";

    // Tentativa de criação de uma tabela com FOREIGN KEY - Falho no SQLite, porém útil para registro e futuras novas tentativas
//    public static final String CREATE_TB_CLIENTE = "CREATE TABLE " + NOME_TABELA_CLIENTE + "("
//            + COLUNA_ID_CLIENTE + " INTEGER PRIMARY KEY," + COLUNA_NOME_COMPLETO + " TEXT NOT NULL,"
//            + COLUNA_CPF + " TEXT NOT NULL," + COLUNA_DATA_NASCIMENTO + " TEXT NOT NULL," + COLUNA_ENDERECO_ID + " INTEGER,"
//            + "FOREIGN KEY(" + COLUNA_ENDERECO_ID + ") REFERENCES " + NOME_TABELA_ENDERECO + "(" + COLUNA_ID_ENDERECO + "))";

    public static final String UPDATE_TB_CLIENTE = "DROP TABLE IF EXISTS " + NOME_TABELA_CLIENTE;

    /**
     * Construtor da classe, cria o banco de dados
     */
    public CadastroDAO(Context context) {
        super(context, NOME_BANCO, null, 1);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL(FK_ON);
        }
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

    /**
     * ContentValue da classe Endereco
     */
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

    /**
     * Método de cadastro na tabela de enderecos
     */
    public void persistirEndereco(Endereco endereco) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = contentValuesEndereco(endereco);
        db.insert(NOME_TABELA_ENDERECO, null, dados);
    }

    /**
     * Método de exclusão na tabela de enderecos
     */
    public void removerEndereco(Endereco endereco) {
        SQLiteDatabase db = getWritableDatabase();
        String[] parametros = {endereco.getId().toString()};
        db.delete(NOME_TABELA_ENDERECO, "id = ?", parametros);
    }

    /**
     * Método de edição na tabela de enderecos
     */
    public void alterarEndereco(Endereco endereco) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = contentValuesEndereco(endereco);
        String[] parametros = {endereco.getId().toString()};
        db.update(NOME_TABELA_ENDERECO, dados, "id = ?", parametros);
    }

    /**
     * Setter da classe Endereco para métodos de busca
     */
    public Endereco setEnderecoBuscado(Endereco endereco, Cursor c) {
        endereco.setId(c.getLong(c.getColumnIndex(COLUNA_ID_ENDERECO)));
        endereco.setCep(c.getString(c.getColumnIndex(COLUNA_CEP)));
        endereco.setNumero(c.getInt(c.getColumnIndex(COLUNA_NUMERO)));
        endereco.setComplemento(c.getString(c.getColumnIndex(COLUNA_COMPLEMENTO)));
        endereco.setLogradouro(c.getString(c.getColumnIndex(COLUNA_LOGRADOURO)));
        endereco.setBairro(c.getString(c.getColumnIndex(COLUNA_BAIRRO)));
        endereco.setLocalidade(c.getString(c.getColumnIndex(COLUNA_LOCALIDADE)));
        endereco.setUf(c.getString(c.getColumnIndex(COLUNA_UF)));

        return endereco;
    }

    /**
     * Busca de endereço a partir de um determinado ID
     */
    public Endereco buscarEndereco(Long enderecoId) {
        Endereco endereco;
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + NOME_TABELA_ENDERECO + " WHERE " + COLUNA_ID_ENDERECO + " = ?";
        Cursor c = db.rawQuery(sql, new String[]{String.valueOf(enderecoId)});
        c.moveToFirst();
        if (c.getCount() > 0) {
            endereco = new Endereco();
            setEnderecoBuscado(endereco, c);
        } else {
            endereco = null;
            System.out.println("Erro ao buscar endereco");
        }
        c.close();
        db.close();
        return endereco;
    }

    /**
     * ContentValue da classe Cliente
     */
    @NonNull
    private ContentValues contentValuesCliente(Cliente cliente) {
        ContentValues dados = new ContentValues();
        dados.put(COLUNA_NOME_COMPLETO, cliente.getNomeCompleto());
        dados.put(COLUNA_CPF, cliente.getCpf());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dados.put(COLUNA_DATA_NASCIMENTO, dateFormat.format(cliente.getDataNascimento()));

        return dados;
    }

    /**
     * Método de cadastro na tabela de clientes
     */
    public void persistirCliente(Cliente cliente) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = contentValuesCliente(cliente);
        db.insert(NOME_TABELA_CLIENTE, null, dados);
    }

    /**
     * Método de exclusão na tabela de clientes
     */
    public void removerCliente(Cliente cliente) {
        SQLiteDatabase db = getWritableDatabase();
        String[] parametros = {cliente.getId().toString()};
        db.delete(NOME_TABELA_CLIENTE, "id = ?", parametros);
    }

    /**
     * Método de edição na tabela de clientes
     */
    public void alterarCLiente(Cliente cliente) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = contentValuesCliente(cliente);
        String[] parametros = {cliente.getId().toString()};
        db.update(NOME_TABELA_CLIENTE, dados, "id = ?", parametros);
    }

    /**
     * Setter da classe Cliente para métodos de busca
     */
    public Cliente setClienteBuscado(Cliente cliente, Cursor c) {
        cliente.setId(c.getLong(c.getColumnIndex(COLUNA_ID_CLIENTE)));
        cliente.setNomeCompleto(c.getString(c.getColumnIndex(COLUNA_NOME_COMPLETO)));
        cliente.setCpf(c.getString(c.getColumnIndex(COLUNA_CPF)));

        // Pegando o endereco da tabela tb_endereco a partir do Id
        Long enderecoId = c.getLong(c.getColumnIndex(COLUNA_ID_ENDERECO));
        Endereco endereco = buscarEndereco(enderecoId);
        cliente.setEndereco(endereco);

        // Convertendo String para date
        String dateString = c.getString(c.getColumnIndex(COLUNA_DATA_NASCIMENTO));
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = format.parse(dateString);
            cliente.setDataNascimento(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return cliente;
    }

    /**
     * Método para listar todos os clientes registrados na tabela
     */
    public List<Cliente> buscarTodosClientes() {
        String sql = "SELECT * FROM " + NOME_TABELA_CLIENTE;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        List<Cliente> clientes = new ArrayList<Cliente>();
        Cliente cliente;

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                cliente = new Cliente();
                setClienteBuscado(cliente, c);
                clientes.add(cliente);
            }
        } else {
            System.out.println("Erro ao listar clientes");
        }
        db.close();
        return clientes;
    }

    /**
     * Busca de um cliente a partir de um determinado ID
     */
    public Cliente buscarCliente(Long clienteId) {
        Cliente cliente;
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + NOME_TABELA_CLIENTE + " WHERE id = ?";
        Cursor c = db.rawQuery(sql, new String[]{String.valueOf(clienteId)});
        c.moveToFirst();

        if (c.getCount() > 0) {
            cliente = new Cliente();
            setClienteBuscado(cliente, c);
        } else {
            cliente = null;
            System.out.println("Erro ao buscar cliente");
        }
        c.close();
        db.close();
        return cliente;
    }


    /**
     * Busca de um cliente a partir de determinado nome
     * Método para pesquisar clientes pelo SearchView na MainActivity
     */
    public List<Cliente> buscarClientePorNome(String textoPesquisado) {
        SQLiteDatabase db = getWritableDatabase();

        try {
            String sql = "SELECT * FROM " + NOME_TABELA_CLIENTE + " WHERE " + COLUNA_NOME_COMPLETO + " Like'" + textoPesquisado + "%'";
            Cursor c = db.rawQuery(sql, null);
            List<Cliente> clienteProcurado = new ArrayList<>();

            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    Cliente cliente = new Cliente();
                    setClienteBuscado(cliente, c);
                    clienteProcurado.add(cliente);
                }
            } else {
                System.out.println("Erro ao buscar cliente pelo nome");
            }
            c.close();
            return clienteProcurado;
        } finally {
            db.close();
        }
    }
}
