package com.example.proyectoandroid.dialogs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.proyectoandroid.R;
import com.example.proyectoandroid.activities.ActivityPersonaje;
import com.example.proyectoandroid.adapters.TransformacionesAdapter;
import com.example.proyectoandroid.databases.DragonBallSQL;
import com.example.proyectoandroid.interfaces.InterfazDialogFragment;
import com.example.proyectoandroid.model.Personaje;
import com.example.proyectoandroid.model.Transformacion;

import static android.app.Activity.RESULT_OK;


public class DialogCrearTransformacionFragment extends DialogFragment implements InterfazDialogFragment {

    private TransformacionesAdapter adapter;
    private ImageView imageView;
    private EditText nombre;
    private Button btnAceptar;
    private Button btnCancelar;
    private Personaje personaje;
    private Transformacion transformacion;
    private DragonBallSQL dragonBallSQL;
    private ActivityPersonaje activityPersonaje;
    private Uri pathImagen;


    public DialogCrearTransformacionFragment(TransformacionesAdapter adapter, Personaje personaje, DragonBallSQL dragonBallSQL, ActivityPersonaje activityPersonaje) {
        this.adapter = adapter;
        this.personaje = personaje;
        this.dragonBallSQL = dragonBallSQL;
        this.activityPersonaje = activityPersonaje;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialog_crear_transformacion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pulsarAceptar(view);
        pulsarCancelar(view);
        pulsarImagen(view);
    }

    /**
     * Creo una transformacion nueva al pulsar aceptar con las caracteristicas insertadas
     */

    @Override
    public void pulsarAceptar(View view) {
        nombre = (EditText) view.findViewById(R.id.nuevoNombreTransformacion);
        btnAceptar = (Button) view.findViewById(R.id.btnCrearTransformacion);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creo una nueva transformacion
                transformacion = new Transformacion(nombre.getText().toString(), pathImagen);

                //Inserto la transformación en base de datos
                dragonBallSQL.insertarTransformacion(personaje, transformacion);

                //Actualizo la activity
                activityPersonaje.actualizarTransformaciones();

                //Creo un Toast para avisar de que se ha creado
                Toast.makeText(activityPersonaje, "Transformación " + nombre.getText().toString() + " creada con éxito", Toast.LENGTH_SHORT).show();

                //Actualizamos el adapter
                adapter.notifyDataSetChanged();

                //Cerramos el dialogo
                dismiss();
            }
        });
    }

    /**
     * Cierro el dialogo sin guardar al pulsar cancelar
     */

    @Override
    public void pulsarCancelar(View view) {
        btnCancelar = (Button) view.findViewById(R.id.btnCancelarTransformacion);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * Insertar imagen desde la galeria
     */

    @Override
    public void pulsarImagen(View view) {
        imageView = (ImageView) view.findViewById(R.id.nuevaFotoTransformacion);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creo un intent para darme la opción para acceder a la galería
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //Le asigno el tipo
                intent.setType("image/");

                //Lanzo la orden
                startActivityForResult(intent.createChooser(intent, "Selecciona aplicación"), 11);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { //Si está bien
            //Creamos una URI con los datos recogidos de la galería
            pathImagen = data.getData();

            //Asignamos la foto al imageView con esa URI
            imageView.setImageURI(pathImagen);
        }
    }

}