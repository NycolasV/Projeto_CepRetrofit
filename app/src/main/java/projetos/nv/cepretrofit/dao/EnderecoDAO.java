package projetos.nv.cepretrofit.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

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

    @NonNull
    private ContentValues contentValues(Endereco endereco) {
        ContentValues dados = new ContentValues();
        dados.put("cep", endereco.getCep());
        dados.put("numero", endereco.getNumero());
        dados.put("complemento", endereco.getComplemento());
        dados.put("logradouro", endereco.getLogradouro());
        dados.put("bairro", endereco.getBairro());
        dados.put("localidade", endereco.getLocalidade());
        dados.put("uf", endereco.getUf());

        return dados;
    }

    public void persistir(Endereco endereco) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = contentValues(endereco);
        db.insert("tb_endereco",null, dados);
    }

    public void remover(Endereco endereco) {
        SQLiteDatabase db = getWritableDatabase();
        String[] parametros = {endereco.getId().toString()};
        db.delete("tb_endereco","id = ?",parametros);
    }

    public void alterar(Endereco endereco) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = contentValues(endereco);
        String[] parametros = {endereco.getId().toString()};
        db.update("tb_endereco", dados,"id = ?", parametros);
    }

    public Endereco buscar(Long enderecoId) {
        String sql = "SELECT * FROM tb_endereco WHERE id = ?";
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(sql,new String[]{String.valueOf(enderecoId)});
        c.moveToFirst();

        Endereco endereco = new Endereco();
        endereco.setId(c.getLong(c.getColumnIndex("id")));
        endereco.setCep(c.getString(c.getColumnIndex("cep")));
        endereco.setNumero(c.getInt(c.getColumnIndex("numero")));
        endereco.setComplemento(c.getString(c.getColumnIndex("complemento")));
        endereco.setLogradouro(c.getString(c.getColumnIndex("logradouro")));
        endereco.setBairro(c.getString(c.getColumnIndex("bairro")));
        endereco.setLocalidade(c.getString(c.getColumnIndex("localidade")));
        endereco.setUf(c.getString(c.getColumnIndex("uf")));

        db.close();
        return endereco;
    }
}
