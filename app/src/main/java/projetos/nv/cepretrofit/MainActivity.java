package projetos.nv.cepretrofit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import projetos.nv.cepretrofit.adapter.RecyclerViewAdapter;
import projetos.nv.cepretrofit.dao.CadastroDAO;
import projetos.nv.cepretrofit.models.Cliente;
import projetos.nv.cepretrofit.models.Endereco;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvClientes;
    private RecyclerViewAdapter adapter;
    private CadastroDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvClientes = findViewById(R.id.rvClientes);
        dao = new CadastroDAO(this);
        resumeList(dao.buscarTodosClientes());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.idAddBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CadastroActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void resumeList(List<Cliente> clientes) {
        adapter = new RecyclerViewAdapter(this, clientes);
        rvClientes.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,1);
        rvClientes.setLayoutManager(layoutManager);
        rvClientes.setNestedScrollingEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dao = new CadastroDAO(this);
        resumeList(dao.buscarTodosClientes());
    }
}
