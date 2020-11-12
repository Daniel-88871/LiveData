package com.example.livedata;

import androidx.lifecycle.LiveData;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Tiempo {

    interface TiempoListener {
        void cuandoDeLaOrden(String orden);
    }

    Random random = new Random();
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    ScheduledFuture<?> entrenando;

    LiveData<String> ordenLiveData = new LiveData<String>() {
        @Override
        protected void onActive() {
            super.onActive();

            iniciarTiempo(new TiempoListener() {
                @Override
                public void cuandoDeLaOrden(String orden) {
                    postValue(orden);
                }
            });
        }

        @Override
        protected void onInactive() {
            super.onInactive();

            pararTiempo();
        }
    };

    void iniciarTiempo(final TiempoListener tiempoListener){
        if (entrenando == null || entrenando.isCancelled()){
            // Log.e("ABCD" , "INICIANDO ENTRENAMIENTO....");
            entrenando = scheduler.scheduleAtFixedRate(new Runnable() {
                int tiempo;
                int repeticiones = -1;

                @Override
                public void run() {
                    if(repeticiones < 0){
                        repeticiones = random.nextInt(3)+3;
                        tiempo = random.nextInt(5)+1;
                    }
                    tiempoListener.cuandoDeLaOrden("TIEMPO"+tiempo + ":" + (repeticiones == 0 ? "CAMBIO" : repeticiones));
                    repeticiones--;
                }
            }, 0, 1, SECONDS);
        }
    }

    void pararTiempo(){
        if(entrenando != null) {
            // Log.e("ABCD" , "PARANDO ENTRENAMIENTO....");
            entrenando.cancel(true);
        }
    }
}