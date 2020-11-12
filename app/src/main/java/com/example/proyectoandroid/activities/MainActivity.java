package com.example.proyectoandroid.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.example.proyectoandroid.R;
import com.example.proyectoandroid.adapters.MainAdapter;
import com.example.proyectoandroid.dialogs.DialogCrearPersonajeFragment;
import com.example.proyectoandroid.dialogs.DialogEditarPersonajeFragment;
import com.example.proyectoandroid.model.Personaje;
import com.example.proyectoandroid.model.Transformacion;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Variables hola
    private ListView listView;
    private List<Personaje> personajes;
    private MainAdapter adapter;
    private Toolbar toolbar;
    private FloatingActionButton btnCrearPersonaje;
    private static final int REQUEST_CODE_FUNCTONE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Quitamos el titulo de la toolbar
        setTitle("");

        //Rellenamos el activity con el listview
        rellenarActivity();

        //Cuando pulse el botón flotante, que muestre el dialogo de crear personaje
        pulsarBotonFlotante();

        //Hacemos referencia al context menu para que lo muestre en pantalla
        registerForContextMenu(listView);

        //Agregamos la toolbar personalizada
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Agregamos el metodo para cambiar de activity
        irACaracteristicas(listView);

        //TODO: SOLUCIONAR PROBLEMA DE QUE NO SE ACTUALIZAN LAS TRANSFORMACIONES AL CAMBIAR DE PANTALLA.
        //TODO: ESTO PASA PORQUE NO LE PASAMOS LOS DATOS NUEVOS A LA ACTIVITY PRINCIPAL AL VOLVER DE LA OTRA ACTIVITY

        //TODO: INSERTAR IMAGEN DESDE LA GALERIA

        //TODO: INSERTAR AUDIO DE CADA PERSONAJE

        //TODO: DIALOGFRAGMENT DE EDITAR TRANSFORMACION

        //TODO: ALERTDIALOG DE BORRAR TRANSFORMACION
    }

    /**
     * Método para rellenar el activity con el listview
     */

    private void rellenarActivity() {
        //Insertamos el ListView en la activity
        listView = (ListView) findViewById(R.id.listView);

        //Creamos la lista de personajes que aparecerá en la vista
        personajes = new ArrayList<Personaje>();
        llenarLista((ArrayList<Personaje>) personajes);

        //Inyectamos el adapter
        adapter = new MainAdapter(this, R.layout.lista, personajes);
        listView.setAdapter(adapter);
    }

    /**
     * Método para llenar la lista
     *
     * @param personajes
     */
    private void llenarLista(ArrayList<Personaje> personajes) {
        personajes.add(new Personaje(1, "Goku", "Son Gokū es el protagonista del manga y anime Dragon Ball creado por Akira Toriyama.", "Saiyan", "Kamehameha", R.drawable.goku, R.drawable.goku_completa));
        personajes.add(new Personaje(2, "Gohan", "Son Gohan es un personaje del manga y anime Dragon Ball creado por Akira Toriyama. Es el primer hijo de Son Gokū y Chi-Chi", "Saiyan", "Masenko", R.drawable.gohan, R.drawable.gohan_completa));
        personajes.add(new Personaje(3, "Goten", "Goten es un personaje ficticio de la serie de manga y anime Dragon Ball. Segundo hijo del protagonista, Goku, y Chichi/Milk.", "Saiyan", "Kamehameha", R.drawable.goten, R.drawable.goten_completa));
        personajes.add(new Personaje(4, "Krilin", "Krilin es un personaje de la serie de manga y anime Dragon Ball. Es el primer rival en artes marciales de Son Gokū aunque luego se convierte en su mejor amigo.", "Humano", "Kienzan", R.drawable.krilin, R.drawable.krilin_completa));
        personajes.add(new Personaje(5, "Piccolo", "Piccolo es un personaje de ficción de la serie de manga y anime Dragon Ball. Su padre, Piccolo Daimaō, surgió tras separarse de Kamisama. ", "Namekiano", "Makankosappo", R.drawable.piccolo, R.drawable.piccolo_completa));
        personajes.add(new Personaje(6, "Trunks", "Trunks es un personaje de ficción de la serie de manga y anime Dragon Ball de Akira Toriyama. Hijo de Vegeta y Bulma", "Saiyan", "Kamehameha", R.drawable.trunks, R.drawable.trunks_completa));
        personajes.add(new Personaje(7, "Vegeta", "Vegeta es un personaje ficticio perteneciente a la raza llamada saiyajin, del manga y anime Dragon Ball.", "Saiyan", "Final flash", R.drawable.vegeta, R.drawable.vegeta_completa));

        //TODO: En vez de insertar a mano las fotos de las transformaciones, hacerlo mediante galeria
        List<Transformacion> transformacionesGoku;
        transformacionesGoku = personajes.get(0).getTransformaciones();
        transformacionesGoku.add(new Transformacion("Goku Super Saiyan 1", R.drawable.goku_ssj1));
        transformacionesGoku.add(new Transformacion("Goku Super Saiyan 2", R.drawable.goku_ssj2));
        transformacionesGoku.add(new Transformacion("Goku Super Saiyan 3", R.drawable.goku_ssj3));
        transformacionesGoku.add(new Transformacion("Goku Super Saiyan Dios", R.drawable.goku_ssjydios));
        transformacionesGoku.add(new Transformacion("Goku Super Saiyan Blue", R.drawable.goku_ssjyblue));
        personajes.get(0).setTransformaciones(transformacionesGoku);
    }

    /**
     * Método para cuando pulse en las caracteristicas
     */

    private void irACaracteristicas(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Recibimos el personaje editado de la otra activity, en caso de que se haya editado
                Personaje personajeEditado = (Personaje) getIntent().getSerializableExtra("personajeEditado");

                if (personajeEditado != null && personajeEditado.getId() == personajes.get(position).getId()) {
                    //Actualizo el personaje
                    personajes.set(position, personajeEditado);
                }

                //Creamos el Intent explicito, y le decimos que vaya desde aqui hasta la activity 2
                Intent intent = new Intent(getApplicationContext(), ActivityPersonaje.class);

                //Insertamos el personaje que de la lista (importante que implemente el Serializable)
                intent.putExtra("personaje", personajes.get(position));

                //Lanzamos la activity (IMPORTANTE)
                startActivityForResult(intent, REQUEST_CODE_FUNCTONE);
            }
        });
    }


    /**
     * Método para que salte error si falla algo en el cambio de Activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Esto es por si hay algun error, nos dira donde esta el error
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FUNCTONE) {
            if (resultCode != RESULT_OK) {
                Exception e = new Exception();
                e.printStackTrace();
            }
        }
    }


    /**
     * MENU

     Originalmente mi idea era hacer un botón de añadir personaje en el menú. Finalmente he hecho un botón flotante,
     me ha parecido una mejor opción.

     @Override public boolean onCreateOptionsMenu(Menu menu) {
     //Inflamos menu
     MenuInflater inflater = getMenuInflater();
     inflater.inflate(R.menu.menu, menu);
     return true;
     }

     /**
      * Cuando pulsemos items del menu

     @Override public boolean onOptionsItemSelected(@NonNull MenuItem item) {
     //Esto es para saber qué item está seleccionado en el menú. Lo mejor es hacer un switch
     switch (item.getItemId()) {
     default:
     return super.onOptionsItemSelected(item);
     }
     }

     */

    /**
     * Cuando pulse el boton flotante, que me cree un dialogo para crear personaje
     */

    private void pulsarBotonFlotante() {
        //Si pulsamos el boton de crear personaje, que muestre el dialogo
        btnCrearPersonaje = (FloatingActionButton) findViewById(R.id.floatingActionButtonCrearPersonaje);
        btnCrearPersonaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearPersonaje();
            }
        });
    }

    /**
     * Creamos el personaje con el Dialog
     */
    private void crearPersonaje() {
        DialogCrearPersonajeFragment dialog = new DialogCrearPersonajeFragment(this.adapter, this.personajes);
        dialog.show(getSupportFragmentManager(), "DialogoCrearPersonaje");
    }

    /**
     * CONTEXT MENU
     */

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        //Recogemos la información con el adapter. Sin esto no sale bien el menú.
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

        //Inflamos el context menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    /**
     * Cuando pulsemos items del context menu
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        //Recogemos la información con el adapter. Sin esto no sale bien el menú.
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        //Este es el personaje actual que estamos pulsando
        Personaje currentPersonaje = personajes.get(info.position);

        //Creamos el switch con todas las opciones del context menu
        switch (item.getItemId()) {
            case R.id.editar:
                editarPersonaje(currentPersonaje);
                return true;
            case R.id.borrar:
                confirmarBorrarPersonaje(currentPersonaje);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Editamos el personaje
     *
     * @param personaje
     */
    private void editarPersonaje(Personaje personaje) {
        //Le paso el personaje que vamos a editar y el adapter para hacer los cambios desde la clase del dialog
        DialogEditarPersonajeFragment dialog = new DialogEditarPersonajeFragment(personaje, this.adapter);
        dialog.show(getSupportFragmentManager(), "DialogoEditarPersonaje");
    }


    /**
     * Método para confirmar borrar un personaje
     */
    private void confirmarBorrarPersonaje(final Personaje p) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar personaje");
        builder.setMessage("¿Estás seguro de que desea eliminar el personaje?");
        builder.setNegativeButton("Cancelar", null);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                personajes.remove(p);
                adapter.notifyDataSetChanged();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}