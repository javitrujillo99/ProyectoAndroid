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

import com.example.proyectoandroid.R;
import com.example.proyectoandroid.activities.MainActivity;
import com.example.proyectoandroid.adapters.MainAdapter;
import com.example.proyectoandroid.databases.DragonBallSQL;
import com.example.proyectoandroid.interfaces.InterfazDialogFragment;
import com.example.proyectoandroid.model.Personaje;

import static android.app.Activity.RESULT_OK;


public class DialogCrearPersonajeFragment extends DialogFragment implements InterfazDialogFragment {

    private Button btnCrear;
    private Button btnCancelar;
    private ImageView imagen;
    private EditText nombre;
    private EditText descripcion;
    private EditText raza;
    private EditText ataqueEspecial;
    private MainAdapter adapter;
    private Personaje personaje;
    private DragonBallSQL personajes;
    private MainActivity mainActivity;
    private Uri path;

    /**
     * Constructor
     *
     * @param adapter
     * @param personajes
     * @param mainActivity
     */
    public DialogCrearPersonajeFragment(MainAdapter adapter, DragonBallSQL personajes, MainActivity mainActivity) {
        // En este caso, le pasamos el adapter y la base de datos, para añadir el personaje creado a ella
        this.adapter = adapter;
        this.personajes = personajes;
        this.mainActivity = mainActivity;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialog_crear_personaje, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imagen = (ImageView) view.findViewById(R.id.nuevaFoto);
        nombre = (EditText) view.findViewById(R.id.nuevoNombre);
        descripcion = (EditText) view.findViewById(R.id.nuevaDescripcion);
        raza = (EditText) view.findViewById(R.id.nuevaRaza);
        ataqueEspecial = (EditText) view.findViewById(R.id.nuevoAtaqueEspecial);

        pulsarAceptar(view);

        pulsarCancelar(view);

        pulsarImagen(view);

    }

    /**
     * Creo un personaje nuevo al pulsar aceptar con las caracteristicas insertadas
     */
    public void pulsarAceptar(View view) {
        //Cuando pulse crear:
        btnCrear = (Button) view.findViewById(R.id.btnCrear);
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creo un nuevo personaje
                //TODO: INSERTAR LA IMAGEN CON SU INT CORRESPONDIENTE
                personaje = new Personaje(nombre.getText().toString(), descripcion.getText().toString(),
                        raza.getText().toString(), ataqueEspecial.getText().toString(), R.drawable.predeterminado, R.drawable.predeterminado);


                //Inserto el personaje en base de datos
                personajes.insertarPersonaje(personaje);

                //Actualizo la activity
                mainActivity.rellenarActivity();

                //Actualizamos con el adapter
                adapter.notifyDataSetChanged();

                //Cerramos el dialogo
                dismiss();
            }
        });
    }

    /**
     * Cierro el dialogo sin guardar al pulsar cancelar
     */

    public void pulsarCancelar(View view) {
        //Cuando pulse cancelar:
        btnCancelar = (Button) view.findViewById(R.id.btnCancelar2);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * TODO: Insertar imagen desde la galeria
     */

    @Override
    public void pulsarImagen(View view) {
        //Al pulsar en la imagen:
        imagen = (ImageView) view.findViewById(R.id.nuevaFoto);


        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creo un intent para darme la opción para acceder a la galería
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //Le asigno el tipo
                intent.setType("image/");

                //Lanzo la orden
                startActivityForResult(intent.createChooser(intent, "Selecciona aplicación"), 10);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { //Si está bien
            //Creamos una URI con los datos recogidos de la galería
            this.path = data.getData();

            //Asignamos la foto al imageView con esa URI
            imagen.setImageURI(path);
        }
    }
}