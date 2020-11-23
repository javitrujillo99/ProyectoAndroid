package com.example.proyectoandroid.interfaces;

import android.view.View;

public interface InterfazDialogFragment {

    //Creo esta interfaz para que me implemente directamente los métodos al crear un DialogFragment, ya que todos
    //son iguales.

    void pulsarAceptar(View view);

    void pulsarCancelar(View view);

    void pulsarImagen(View view);
}
