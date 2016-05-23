package com.example.rick.mycontacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 21/05/16.
 */
public class ContatoDAO extends SQLiteOpenHelper {
    private static final int VERSAO = 1;
    private static final String TABELA = "contatos";
    private static final String DATABASE = "agenda";

    public ContatoDAO(Context context) {
        super(context, DATABASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String dll = "CREATE TABLE " + TABELA + " (id INTEGER PRIMARY KEY, nome TEXT NOT NULL, email TEXT, telefone TEXT, caminhoFoto TEXT);";
        db.execSQL(dll);
        Log.d("VITOR","TABELA CRIADA");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            String sql = "ALTER TABLE " + TABELA + " ADD COLUMN caminhoFoto TEXT;";
            db.execSQL(sql);
        }
    }

    public void inserirContato(Contato contato) {
        ContentValues values = new ContentValues();
        values.put("nome", contato.getNome());
        values.put("telefone", contato.getTelefone());
        values.put("email", contato.getEmail());
        values.put("caminhoFoto", contato.getFoto());
        Log.d("VITOR","INSERINDO REGISTRO "+contato.getNome()+" - "+contato.getTelefone()+" - "+contato.getEmail()+" - "+contato.getFoto());
        getWritableDatabase().insert(TABELA, null, values);
    }

    public void apagarContato(Contato contato) {
        SQLiteDatabase db = getWritableDatabase();
        String [] args = {contato.getId().toString()};
        db.delete(TABELA, "id = ?", args);
        Log.d("VITOR","APAGANDO REGISTRO");
    }

    public void alterarContato(Contato contato) {
        ContentValues values = new ContentValues();
        values.put("nome", contato.getNome());
        values.put("telefone", contato.getTelefone());
        values.put("email", contato.getEmail());
        values.put("caminhoFoto", contato.getFoto());

        String [] args = {contato.getId().toString()};
        getWritableDatabase().update(TABELA, values, "id = ?", args);
        Log.d("VITOR","UPDATE REGISTRO");
    }

    public List<Contato> getList() {
        List<Contato> contatos = new ArrayList<Contato>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABELA + " ORDER BY nome;", null);
        while (cursor.moveToNext()) {
            Contato contato = new Contato();
            contato.setId(cursor.getLong(cursor.getColumnIndex("id")));
            contato.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            contato.setTelefone(cursor.getString(cursor.getColumnIndex("telefone")));
            contato.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            contato.setFoto(cursor.getString(cursor.getColumnIndex("caminhoFoto")));
            contatos.add(contato);
        }
        cursor.close();
        Log.d("VITOR","BUSCANDO DADOS");
        return contatos;
    }

    public boolean isContato(String telefone) {
        String[] parametros = {telefone};
        Cursor cursor = getReadableDatabase().rawQuery("SELECT telefone FROM " + TABELA + " WHERE telefone = ?", parametros);
        int total = cursor.getCount();
        return total > 0;
    }
}
