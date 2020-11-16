package com.example.proyectoandroid.dialogs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

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


public class DialogEditarPersonajeFragment extends DialogFragment implements InterfazDialogFragment {

    private Button btnAceptar;
    private Button btnCancelar;
    private ImageView imageView;
    private EditText nombre;
    private EditText descripcion;
    private EditText raza;
    private EditText ataqueEspecial;
    private MainAdapter adapter;
    private Personaje personaje;
    private MainActivity mainActivity;
    private DragonBallSQL personajes;


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

        imageView = (ImageView) view.findViewById(R.id.editarFoto);
        nombre = (EditText) view.findViewById(R.id.editarNombre);
        descripcion = (EditText) view.findViewById(R.id.editarDescripcion);
        raza = (EditText) view.findViewById(R.id.editarRaza);
        ataqueEspecial = (EditText) view.findViewById(R.id.editarAtaqueEspecial);

        cargarCaracteristicas();

        pulsarAceptar(view);

        pulsarCancelar(view);
    }

    /**
     * Cargo las características del personaje
     */
    private void cargarCaracteristicas() {
        imageView.setImageResource(personaje.getFotoCompleta());
        nombre.setText(personaje.getNombre());
        descripcion.setText(personaje.getDescripcion());
        raza.setText(personaje.getRaza());
        ataqueEspecial.setText(personaje.getAtaqueEspecial());
    }

    /**
     *
     * @param view
     */
    public void pulsarAceptar(View view) {
        //Cuando pulse aceptar:
        btnAceptar = (Button) view.findViewById(R.id.btnAceptar);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cambiamos las caracteristicas del personaje a las escritas en el EditText
                personaje.setNombre(nombre.getText().toString());
                personaje.setDescripcion(descripcion.getText().toString());
                personaje.setRaza(raza.getText().toString());
                personaje.setAtaqueEspecial(ataqueEspecial.getText().toString());

                //Actualizamos el personaje en la base de datos
                personajes.editarPersonaje(personaje);

                //Actualizamos la activity
                mainActivity.rellenarActivity();

                //Actualizamos con el adapter
                adapter.notifyDataSetChanged();
                dismiss(); //Cerramos el dialogo
            }
        });
    }

    /**
     *
     * @param view
     */
    public void pulsarCancelar(View view) {
        //Cuando pulse cancelar:
        btnCancelar = (Button) view.findViewById(R.id.btnCancelar);
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

    }
}