package com.example.rick.mycontacts;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView minhaLista;
    EditText campoPesquisa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewUserForm.class);
                startActivity(intent);
            }
        });

        minhaLista = (ListView) findViewById(R.id.minhaLista);
        campoPesquisa = (EditText) findViewById(R.id.txtPesquisar);
        registerForContextMenu(minhaLista);

        minhaLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contato contato;
                contato = (Contato) parent.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, NewUserForm.class);
                intent.putExtra("contatoSelecionado", contato);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Contato contatoSelecionado = (Contato) minhaLista.getAdapter().getItem(info.position);

        final MenuItem itemLigar = menu.add("Ligar para Contato");
        final MenuItem itemApagar = menu.add("Apagar Contato");

        itemApagar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Apagar Contato?")
                        .setMessage("Deseja realmente apagar este contato?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ContatoDAO dao = new ContatoDAO(MainActivity.this);
                                dao.apagarContato(contatoSelecionado);
                                dao.close();
                                carregaLista();
                            }
                        })
                        .setNegativeButton("Não", null).show();
                return false;
            }
        });

        itemLigar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CALL_PHONE)) {
                        // ja foi negado a permissao
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 0);
                    }
                } else {
                    ligarParaContato(contatoSelecionado);
                }

                return false;
            }
        });
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    protected void onResume() {
        carregaLista();
        campoPesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    // carrega a lista toda
                    carregaLista();
                } else {
                    pesquisaLista(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        super.onResume();
    }

    private void carregaLista() {
        ContatoDAO dao = new ContatoDAO(this);
        List<Contato> contatos = dao.getList();
        dao.close();

        ContatoAdaptador adaptador = new ContatoAdaptador(this, contatos);
        this.minhaLista.setAdapter(adaptador);
    }

    private void pesquisaLista(String filtro) {
        ContatoDAO dao = new ContatoDAO(this);
        List<Contato> contatos = dao.getListFiltro(filtro);
        dao.close();

        ContatoAdaptador adaptador = new ContatoAdaptador(this, contatos);
        this.minhaLista.setAdapter(adaptador);
    }

    public void ligarParaContato(Contato contato) {
        if (contato != null) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + contato.getTelefone()));

            startActivity(intent);
        }
    }
}