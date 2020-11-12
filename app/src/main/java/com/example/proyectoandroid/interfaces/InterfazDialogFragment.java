package com.example.proyectoandroid.interfaces;

import android.view.View;

public interface InterfazDialogFragment {

    //Creo esta interfaz para que me implemente directamente los métodos al crear un DialogFragment, ya que todos
    //son iguales.

    public void pulsarAceptar(View view);

    public void pulsarCancelar(View view);

    public void pulsarImagen(View view);
}
