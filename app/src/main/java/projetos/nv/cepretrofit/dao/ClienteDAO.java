package projetos.nv.cepretrofit.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import projetos.nv.cepretrofit.models.Cliente;

public class ClienteDAO extends SQLiteOpenHelper {

    public static final String nome_tabela = "tb_cliente";
    public static final String coluna_id = "id";
    public static final String coluna_nomeCompleto = "nomeCompleto";
    public static final String coluna_cpf = "cpf";
    public static final String coluna_endereco = "endereco";
    public static final String coluna_dataNascimento = "dataNascimento";

    public static final String create_tb_cliente = "CREATE TABLE " + nome_tabela + "("
            + coluna_id + " INTEGER PRIMARY KEY AUTOINCREMENT," + coluna_nomeCompleto + " TEXT,"
            + coluna_cpf + " TEXT," + coluna_endereco + " TEXT," + coluna_dataNascimento + "TEXT" + ")";

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

//    @NonNull
//    private ContentValues contentValues(Cliente cliente) {
//        ContentValues dados = new ContentValues();
//
//        dados.put("nomeCompleto", cliente.getNomeCompleto());
//        dados.put("cpf", cliente.getCpf());
//        dados.put("endereco", cliente.getEndereco());
//        dados.put("dataNascimento", cliente.getDataNascimento().toString());
//
//        return dados;
//    }


}
