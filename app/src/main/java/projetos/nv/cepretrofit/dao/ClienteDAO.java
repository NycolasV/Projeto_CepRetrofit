package projetos.nv.cepretrofit.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import projetos.nv.cepretrofit.models.Cliente;
import projetos.nv.cepretrofit.models.Endereco;

public class ClienteDAO extends SQLiteOpenHelper {

    private EnderecoDAO enderecoDAO;
    public static final String nome_tabela = "tb_cliente";
    public static final String coluna_id = "id";
    public static final String coluna_nomeCompleto = "nomeCompleto";
    public static final String coluna_cpf = "cpf";
    public static final String coluna_dataNascimento = "dataNascimento";

    public static final String create_tb_cliente = "CREATE TABLE " + nome_tabela + "("
            + coluna_id + " INTEGER PRIMARY KEY AUTOINCREMENT," + coluna_nomeCompleto + " TEXT,"
            + coluna_cpf + " TEXT," + coluna_dataNascimento + "TEXT, "
            + "CONSTRAINT FK_endereco FOREIGN KEY (endereco_id) REFERENCES tb_endereco" + ")";

    public static final String update_tb_cliente =  "DROP TABLE IF EXISTS " + nome_tabela;

    public ClienteDAO(Context context) {
        super(context, "db_cep_retrofit", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_tb_cliente);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(update_tb_cliente);
    }

    @NonNull
    private ContentValues contentValues(Cliente cliente) {
        ContentValues dados = new ContentValues();

        dados.put("nomeCompleto", cliente.getNomeCompleto());
        dados.put("cpf", cliente.getCpf());
        dados.put("dataNascimento", cliente.getDataNascimento().toString());
        dados.put("FK_endereco", cliente.getEndereco().getId());

        return dados;
    }

    public void persistir(Cliente cliente) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = contentValues(cliente);
        db.insert("tb_cliente",null, dados);
    }

    public void remover(Cliente cliente) {
        SQLiteDatabase db = getWritableDatabase();
        String[] parametros = {cliente.getId().toString()};
        db.delete("tb_cliente","id = ?",parametros);
    }

    public void alterar(Cliente cliente) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = contentValues(cliente);
        String[] parametros = {cliente.getId().toString()};
        db.update("tb_cliente", dados,"id = ?", parametros);
    }

    public List<Cliente> buscarTodos() {
        String sql = "SELECT * FROM tb_cliente";
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(sql, null);
        List<Cliente> clientes= new ArrayList<Cliente>();

        while (c.moveToNext()){
            Cliente cliente = new Cliente();

            cliente.setId(c.getLong(c.getColumnIndex("id")));
            cliente.setNomeCompleto(c.getString(c.getColumnIndex("nomeCompleto")));
            cliente.setCpf(c.getString(c.getColumnIndex("cpf")));

            // Convertendo String para date
            String dateString = c.getString(c.getColumnIndex("dataNascimento"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            try {
                Date date = format.parse(dateString);
                cliente.setDataNascimento(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Pegando o endereco da tabela tb_enderecos
            cliente.setEndereco(enderecoDAO.buscar(c.getLong(c.getColumnIndex("FK_endereco"))));

            clientes.add(cliente);
        }

        return clientes;
    }

    public Cliente buscar(Long clienteId) {
        String sql = "SELECT * FROM tb_cliente WHERE id = ?";
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(sql,new String[]{String.valueOf(clienteId)});
        c.moveToFirst();

        Cliente cliente = new Cliente();

        cliente.setId(c.getLong(c.getColumnIndex("id")));
        cliente.setNomeCompleto(c.getString(c.getColumnIndex("nomeCompleto")));
        cliente.setCpf(c.getString(c.getColumnIndex("cpf")));

        // Convertendo String para date
        String dateString = c.getString(c.getColumnIndex("dataNascimento"));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            Date date = format.parse(dateString);
            cliente.setDataNascimento(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Pegando o endereco da tabela tb_enderecos
        cliente.setEndereco(enderecoDAO.buscar(c.getLong(c.getColumnIndex("FK_endereco"))));

        db.close();

        return cliente;
    }

    public List<Cliente> buscarPorNome(String textoPesquisado) {
        SQLiteDatabase db = getWritableDatabase();

        try {
            String sql = "SELECT * FROM tb_cliente WHERE nomeCompleto Like'"+ textoPesquisado +"%'";

            Cursor c = db.rawQuery(sql, null);
            List<Cliente> clienteProcurado = new ArrayList<>();

            while (c.moveToNext()){
                Cliente cliente = new Cliente();

                cliente.setId(c.getLong(c.getColumnIndex("id")));
                cliente.setNomeCompleto(c.getString(c.getColumnIndex("nomeCompleto")));
                cliente.setCpf(c.getString(c.getColumnIndex("cpf")));

                // Convertendo String para date
                String dateString = c.getString(c.getColumnIndex("dataNascimento"));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                try {
                    Date date = format.parse(dateString);
                    cliente.setDataNascimento(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Pegando o endereco da tabela tb_enderecos
                cliente.setEndereco(enderecoDAO.buscar(c.getLong(c.getColumnIndex("FK_endereco"))));

                clienteProcurado.add(cliente);
            }

            c.close();
            return  clienteProcurado;
        }finally {
            db.close();
        }
    }
}
