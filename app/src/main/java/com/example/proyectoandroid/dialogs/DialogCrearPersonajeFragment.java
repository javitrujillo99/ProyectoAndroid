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
import com.example.proyectoandroid.adapters.MainAdapter;
import com.example.proyectoandroid.interfaces.InterfazDialogFragment;
import com.example.proyectoandroid.model.Personaje;

import java.util.List;


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
    private List<Personaje> personajes;

    /**
     * Constructor
     * @param adapter
     * @param personajes
     */
    public DialogCrearPersonajeFragment(MainAdapter adapter, List<Personaje> personajes) {
        // En este caso, le pasamos el adapter y la lista, para añadir el personaje creado a ella
        this.adapter = adapter;
        this.personajes = personajes;
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
                        raza.getText().toString(), ataqueEspecial.getText().toString() ,R.drawable.predeterminado, R.drawable.predeterminado);

                //Añado el personaje a la lista
                personajes.add(personaje);

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

    }

}