package com.example.atack08.localizacion_mapas;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        ActivityCompat.OnRequestPermissionsResultCallback ,
        OnMapReadyCallback{

    //CONSTANTES PARA LOS PERMISOS
    private static int PETICION_PERMISO_LOCALIZACION;

    private GoogleApiClient clienteApi;
    private EditText textoLatitud, textLongitud;
    private GoogleMap mapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //CREAMOS EL OBJETO GoogleApiClient
        clienteApi = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        textoLatitud = (EditText)findViewById(R.id.textLatitud);
        textLongitud = (EditText)findViewById(R.id.textLongitud);


        //MapFragment
        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    public void updateUI(Location loc){

        //IMPRIMIMOS LAS COORDENADAS SI LA LOCALIZACIÓN NO ES NULL
        if(loc != null) {
            textLongitud.setText(String.valueOf(loc.getLongitude()));
            textoLatitud.setText(String.valueOf(loc.getLatitude()));
        }
        else{
            textLongitud.setText("Desconocida");
            textoLatitud.setText("Desconocida");
        }

    }



    //MÉTODO PARA MOSTRAR ERROR EN LA CONEXIÓN DEL API
    @Override
    public void onConnectionFailed(ConnectionResult result){

        Log.e("ERROR  GOOGLE PLAY","ERROR GRAVE AL CONECTAR CON GOOGLE PLAY SERVICES");
    }


    //IMPLEMENTAMOS MÉTODOS DE LA INTERFACE ConnectionCallBacks
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        consultaPermisos();
        Toast.makeText(this, "SERVICIO CONECTADO", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionSuspended(int i) {

        Toast.makeText(this, "SERVICIO DESCONECTADO", Toast.LENGTH_SHORT).show();
    }

    //GESTIÓN DE PERMISOS
    public void consultaPermisos(){

        //SI NO TIENE PERMISOS
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED){

            //CONCEDE PERMISO
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PETICION_PERMISO_LOCALIZACION);

        }
        else{
            //SI YA TIENE PERMISOS ASIGNADOS RECUPERAMOS LA ÚLTIMA POSICIÓN
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(clienteApi);

            //GENERAMOS LAS COORDENADAS
            updateUI(lastLocation);
            activarBotones();
        }



    }

    //EVENTOS PARA LOS PERMISOS
    @Override
    public void  onRequestPermissionsResult(int requestCode, String[] permissions, int [] grantResults){

        if(requestCode == PETICION_PERMISO_LOCALIZACION){

            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //PERMISO CONCEDIDO
                //PEDIMOS LA ULTIMA LOCALIZACION
                @SuppressWarnings("MissingPermission")
                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(clienteApi);

                //GENERAMOS LAS COORDENADAS
                updateUI(lastLocation);
                activarBotones();
            }

        }
        else{
            //PERMISO DENEGADO
            Log.e("PERMISO DENEGADO","PERMISO DENEGADO");
        }


    }


    //MÉTODO QUE ACTIVA LOS BOTONES
    public void activarBotones(){
        ((Button)findViewById(R.id.botonLocalizar)).setEnabled(true);
        ((Button)findViewById(R.id.botonPosicionar)).setEnabled(true);
    }

    //MÉTODO IMPLEMENTADO DE LA INTERFACE OnMapReadyCallback

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
    }
}
