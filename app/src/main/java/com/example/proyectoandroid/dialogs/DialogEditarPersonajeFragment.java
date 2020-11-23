package com.example.proyectoandroid.dialogs;

import android.annotation.SuppressLint;
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
import com.example.proyectoandroid.activities.MainActivity;
import com.example.proyectoandroid.adapters.MainAdapter;
import com.example.proyectoandroid.databases.DragonBallSQL;
import com.example.proyectoandroid.interfaces.InterfazDialogFragment;
import com.example.proyectoandroid.model.Personaje;

import static android.app.Activity.RESULT_OK;


public class DialogEditarPersonajeFragment extends DialogFragment implements InterfazDialogFragment {

    private ImageView imagen;
    private EditText nombre;
    private EditText descripcion;
    private EditText raza;
    private EditText ataqueEspecial;
    private final MainAdapter adapter;
    private final Personaje personaje;
    private final MainActivity mainActivity;
    private final DragonBallSQL personajes;
    private Uri path;


    public DialogEditarPersonajeFragment(Personaje personaje, MainAdapter adapter, MainActivity mainActivity, DragonBallSQL dragonBallSQL) {
        //Le paso al constructor el personaje que queremos editar y el adapter para editarlo
        this.personaje = personaje;
        this.adapter = adapter;
        this.mainActivity = mainActivity;
        this.personajes = dragonBallSQL;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialog_editar_personaje, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imagen = view.findViewById(R.id.editarFoto);
        nombre = view.findViewById(R.id.editarNombre);
        descripcion = view.findViewById(R.id.editarDescripcion);
        raza = view.findViewById(R.id.editarRaza);
        ataqueEspecial = view.findViewById(R.id.editarAtaqueEspecial);

        cargarCaracteristicas();

        pulsarAceptar(view);

        pulsarCancelar(view);

        pulsarImagen(view);
    }

    /**
     * Cargo las características del personaje
     */
    private void cargarCaracteristicas() {
        //Depende del tipo que sea, ya que si insertamos desde galeria será tipo Uri
        if (personaje.getFoto().getClass().getSimpleName().equals("Integer")) {
            imagen.setImageResource((Integer) personaje.getFoto());
        } else {
            imagen.setImageURI((Uri) personaje.getFoto());
        }
        nombre.setText(personaje.getNombre());
        descripcion.setText(personaje.getDescripcion());
        raza.setText(personaje.getRaza());
        ataqueEspecial.setText(personaje.getAtaqueEspecial());
    }

    /**
     *
     */
    public void pulsarAceptar(View view) {
        //Cuando pulse aceptar:
        Button btnAceptar = view.findViewById(R.id.btnAceptar);
        btnAceptar.setOnClickListener(v -> {
            //Cambiamos las caracteristicas del personaje a las escritas en el EditText
            personaje.setNombre(nombre.getText().toString());
            personaje.setDescripcion(descripcion.getText().toString());
            personaje.setRaza(raza.getText().toString());
            personaje.setAtaqueEspecial(ataqueEspecial.getText().toString());
            personaje.setFoto(path);

            //Actualizamos el personaje en la base de datos
            personajes.editarPersonaje(personaje);

            //Actualizamos la activity
            mainActivity.rellenarActivity();

            //Creo un Toast para avisar de que se ha creado
            Toast.makeText(mainActivity, "Personaje " + nombre.getText().toString() + " editado con éxito", Toast.LENGTH_SHORT).show();


            //Actualizamos con el adapter
            adapter.notifyDataSetChanged();
            dismiss(); //Cerramos el dialogo
        });
    }

    /**
     *
     */
    public void pulsarCancelar(View view) {
        //Cuando pulse cancelar:
        Button btnCancelar = view.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(v -> dismiss());
    }

    /**
     * Insertar imagen desde la galeria
     */

    @SuppressLint("IntentReset")
    @Override
    public void pulsarImagen(View view) {
        //Al pulsar en la imagen:
        imagen = view.findViewById(R.id.editarFoto);


        imagen.setOnClickListener(v -> {
            //Creo un intent para darme la opción para acceder a la galería
            @SuppressLint("IntentReset") Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //Le asigno el tipo
            intent.setType("image/");

            //Lanzo la orden
            startActivityForResult(Intent.createChooser(intent, "Selecciona aplicación"), 10);
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