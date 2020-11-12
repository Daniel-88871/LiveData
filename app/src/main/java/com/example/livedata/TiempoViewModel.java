package com.example.livedata;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class TiempoViewModel extends AndroidViewModel {
    Tiempo tiempo;

    LiveData<Integer> tiempoLiveData;
    LiveData<String> repeticionLiveData;

    public TiempoViewModel(@NonNull Application application) {
        super(application);

        tiempo = new Tiempo();

        tiempoLiveData = Transformations.switchMap(tiempo.ordenLiveData, new Function<String, LiveData<Integer>>() {

            String tiempoAnterior;

            @Override
            public LiveData<Integer> apply(String orden) {

                String tiempo = orden.split(":")[0];

                if(!tiempo.equals(tiempoAnterior)){
                    tiempoAnterior = tiempo;
                    int imagen;
                    switch (tiempo) {
                        case "SOL":
                        default:
                            imagen = R.drawable.sol;
                            break;
                        case "NUBES":
                            imagen = R.drawable.nubes;
                            break;
                        case "LLUVIA":
                            imagen = R.drawable.lluvia;
                            break;
                        case "VIENTO":
                            imagen = R.drawable.viento;
                            break;
                    }

                    return new MutableLiveData<>(imagen);
                }
                return null;
            }
        });

        repeticionLiveData = Transformations.switchMap(tiempo.ordenLiveData, new Function<String, LiveData<String>>() {
            @Override
            public LiveData<String> apply(String orden) {
                return new MutableLiveData<>(orden.split(":")[1]);
            }
        });
    }

    public LiveData<Integer> obtenerTiempo(){
        return tiempoLiveData;
    }

    public LiveData<String> obtenerRepeticion(){
        return repeticionLiveData;
    }
}