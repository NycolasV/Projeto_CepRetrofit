package projetos.nv.cepretrofit.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import projetos.nv.cepretrofit.models.Endereco;

public class EnderecoDAO extends SQLiteOpenHelper {

    public static final String nome_tabela = "tb_endereco";
    public static final String coluna_id = "id";
    public static final String coluna_cep = "cep";
    public static final String coluna_numero = "numero";
    public static final String coluna_complemento = "complemento";
    public static final String coluna_logradouro = "logradouro";
    public static final String coluna_bairro = "bairro";
    public static final String coluna_localidade = "localidade";
    public static final String coluna_uf = "uf";

    public static final String create_tb_endereco = "CREATE TABLE " + nome_tabela + "("
            + coluna_id + " INTEGER PRIMARY KEY AUTOINCREMENT," + coluna_cep + " TEXT,"
            + coluna_numero + " INTEGER," + coluna_complemento + " TEXT," + coluna_logradouro + "TEXT,"
            + coluna_bairro + " TEXT," + coluna_localidade + " TEXT," + coluna_uf + "TEXT" + ")";

    public static final String update_tb_endereco =  "DROP TABLE IF EXISTS " + nome_tabela;

    public EnderecoDAO(Context context) {
        super(context, "db_cep_retrofit", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_tb_endereco);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(update_tb_endereco);
    }
}
