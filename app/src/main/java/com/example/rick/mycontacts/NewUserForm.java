package com.example.rick.mycontacts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

public class NewUserForm extends AppCompatActivity {

    NewUserFormHelper helper;
    Contato contato;

    private String localArquivoFoto;
    private static final int TIRA_FOTO = 123;
    private boolean fotoResource = false;

    private Bitmap bitmap;

    ImageView imagemContato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imagemContato = (ImageView) findViewById(R.id.imageView);
        this.helper = new NewUserFormHelper(this);
        final Button botaoFoto = helper.getBotaoFoto();

        if (toolbar != null) {
            toolbar.setTitle("Editar Contato");
            toolbar.setNavigationIcon(R.drawable.voltar);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NewUserForm.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        }

        Intent intent = this.getIntent();
        this.contato = (Contato) intent.getSerializableExtra("contatoSelecionado");

        if (this.contato != null) {
            this.helper.SetForm(contato);
        }

        botaoFoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                alertaSourceImagem();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                Contato contato = helper.getContato();
                Log.d("VITOR","ID CONTATO = "+contato.getId());
                ContatoDAO dao = new ContatoDAO(NewUserForm.this);
                if (contato.getId() == null || contato.getId() == 0) {
                    dao.inserirContato(contato);
                    Log.d("VITOR","INSERINDO");
                }
                else {
                    dao.alterarContato(contato);
                    Log.d("VITOR","ALTERANDO");
                }
                dao.close();
                finish();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void alertaSourceImagem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Selecione a fonte da foto:");
        builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                carregaFotoCamera();
            }
        });

        builder.setNegativeButton("Biblioteca", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                carregaFotoBiblioteca();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void carregaFotoBiblioteca() {
        fotoResource = false;
/*
        //Intent intent = new Intent();
        Intent intent = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("imagem/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent,"Selecione uma imagem"),1);
        */

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent,"Selecione uma imagem"),1);

    }

    public void carregaFotoCamera(){

        fotoResource = true;

        localArquivoFoto = getExternalFilesDir(null)+"/"+ System.currentTimeMillis()+".jpg";
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(localArquivoFoto)));
        startActivityForResult(intentCamera,123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (fotoResource) {
            Log.d("VITOR", "RESULT 2= "+String.valueOf(resultCode)+ " - "+ String.valueOf(requestCode));
            if (requestCode == TIRA_FOTO) {
                if (resultCode == Activity.RESULT_OK) {
                    helper.LoadImage(this.localArquivoFoto);
                } else {
                    this.localArquivoFoto = null;
                }
            }
        } else {
            Log.d("VITOR", "RESULT = "+String.valueOf(resultCode)+ " - "+ String.valueOf(requestCode));
            if (resultCode == -1) {
                Log.d("VITOR", "ENTROU");
                InputStream stream = null;
                try{
                    if (bitmap != null) {
                        bitmap.recycle();
                    }
                    stream = getContentResolver().openInputStream(data.getData());
                    bitmap = BitmapFactory.decodeStream(stream);
                    //bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    imagemContato.setImageBitmap(bitmap);
                    //helper.LoadImage(this.localArquivoFoto);

                } catch (Exception e){
                    e.printStackTrace();
                } finally {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
