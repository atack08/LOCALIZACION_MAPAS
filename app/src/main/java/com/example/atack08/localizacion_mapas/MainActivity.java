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
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        ActivityCompat.OnRequestPermissionsResultCallback{

    //CONSTANTES PARA LOS PERMISOS
    private static int PETICION_PERMISO_LOCALIZACION;

    private GoogleApiClient clienteApi;

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


    }

    public void updateUI(Location loc){


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
            }

        }
        else{
            //PERMISO DENEGADO
            Log.e("PERMISO DENEGADO","PERMISO DENEGADO");
        }


    }
}