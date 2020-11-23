package com.example.proyectoandroid.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.proyectoandroid.R;
import com.example.proyectoandroid.model.Personaje;

import java.util.List;

public class MainAdapter extends BaseAdapter {

    //Creamos un contexto, layout e inyectamos la lista
    private final Context context;
    private final List<Personaje> personajes;


    /**
     * Constructor
     */
    public MainAdapter(Context context, List<Personaje> personajes) {
        this.context = context;
        this.personajes = personajes;
    }

    @Override
    public int getCount() {
        return this.personajes.size();
    }

    @Override
    public Object getItem(int position) {
        return this.personajes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Llamamos a la vista
        View v;

        //Inflamos el layout a nuestro contexto
        LayoutInflater inflater = LayoutInflater.from(this.context);
        v = inflater.inflate(R.layout.lista, null);

        //Obtenemos el personaje de cada recorrido de la lista
        Personaje currentPersonaje = personajes.get(position);

        //Colocamos cada atributo del personaje en su layout correspondiente
        ImageView foto = v.findViewById(R.id.foto);
        TextView nombre = v.findViewById(R.id.nombre);
        TextView descripcion = v.findViewById(R.id.descripcion);

        //Si la foto es de tipo Integer, le asigno la ImageResource
        if (currentPersonaje.getFoto().getClass().getSimpleName().equals("Integer")) {
            foto.setImageResource((Integer) currentPersonaje.getFoto());
        } else { //Si la imagen es de la galería, por tanto tipo Uri, le inserto el Uri
            foto.setImageURI((Uri) currentPersonaje.getFoto());
        }
        nombre.setText(currentPersonaje.getNombre());
        descripcion.setText(currentPersonaje.getDescripcion());

        return v;
    }
}
