package com.example.rick.mycontacts;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rick on 21/05/16.
 */
public class ContatoAdaptador extends BaseAdapter {

    private final List<Contato> contatos;
    private final Activity activity;

    public ContatoAdaptador(Activity activity, List<Contato> contatos) {
        this.contatos = contatos;
        this.activity = activity;
    }

    @Override
    public int getCount() {

        return this.contatos.size();
    }

    @Override
    public Object getItem(int position) {

        return this.contatos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.contatos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View linha = convertView;
        Contato contato = contatos.get(position);
        Bitmap bitmap;
        if (linha == null) {
            linha = this.activity.getLayoutInflater().inflate(R.layout.contact_cell, parent, false);
        }
        TextView nome = (TextView) linha.findViewById(R.id.nome);
        ImageView foto = (ImageView) linha.findViewById(R.id.foto);

        nome.setText(contato.getNome());

        if (contato.getFoto() != null) {
            bitmap = BitmapFactory.decodeFile(contato.getFoto());
        } else {
            bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.blank_contact);
        }

        bitmap = Bitmap.createScaledBitmap(bitmap, 50, 50, true);
        foto.setImageBitmap(bitmap);


        return linha;
    }


}
