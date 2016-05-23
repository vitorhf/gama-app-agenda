package com.example.rick.mycontacts;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by rick on 21/05/16.
 */
public class NewUserFormHelper {
    private Contato contato;
    private EditText nome;
    private EditText email;
    private EditText telefone;
    private ImageView foto;
    private Button botaoFoto;

    public NewUserFormHelper(NewUserForm activity) {
        this.contato = new Contato();

        this.nome = (EditText) activity.findViewById(R.id.PName);
        this.email = (EditText) activity.findViewById(R.id.PEmail);;
        this.telefone = (EditText) activity.findViewById(R.id.PPhone);;
        this.foto = (ImageView) activity.findViewById(R.id.imageView);;
        this.botaoFoto = (Button) activity.findViewById(R.id.botaoFoto);;
    }

    public Button getBotaoFoto() {
        return botaoFoto;
    }

    public Contato getContato() {
        contato.setNome(nome.getText().toString());
        contato.setEmail(email.getText().toString());
        contato.setTelefone(telefone.getText().toString());
        contato.setFoto((String) foto.getTag());
        return contato;
    }

    public void SetForm(Contato contato) {
        nome.setText(contato.getNome());
        email.setText(contato.getEmail());
        telefone.setText(contato.getTelefone());

        foto.setTag(contato.getFoto());
        LoadImage(contato.getFoto());

        this.contato = contato;
    }

    public void LoadImage(String photoPath) {
        if (photoPath != null) {
            Bitmap photoImage = BitmapFactory.decodeFile(photoPath);
            foto.setImageBitmap(photoImage);
            foto.setTag(photoPath);
        }
    }




}
