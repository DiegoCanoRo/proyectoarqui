#include <jni.h>

#include <stdio.h>
#include "C:\Users\diego\Documents\NetBeansProjects\mentiroso\src\main\java\comdiegocano\mentiroso\comdiegocano_mentiroso_Juego.h"

JNIEXPORT void JNICALL Java_comdiegocano_mentiroso_Juego_cambiarTurno(JNIEnv* env, jobject obj) {
    //obtener la clase
    jclass claseJuego = (*env)->GetObjectClass(env, obj);

    // obtener los campos turnoActual y cantidadJugadores
    jfieldID fidTurno = (*env)->GetFieldID(env, claseJuego, "turnoActual", "I");
    jfieldID fidCantidad = (*env)->GetFieldID(env, claseJuego, "cantidadJugadores", "I");

    jint turno = (*env)->GetIntField(env, obj, fidTurno);
    jint cantidad = (*env)->GetIntField(env, obj, fidCantidad);

    __asm {
        mov eax, turno
        add eax, 1
        mov edx, 0
        mov ecx, cantidad
        div ecx              ; eax = (turno + 1) / cantidad y edx = (turno + 1) % cantidad
        mov turno, edx
    }

    (*env)->SetIntField(env, obj, fidTurno, turno);
}



JNIEXPORT jint JNICALL Java_comdiegocano_mentiroso_Juego_obtenerTurnoAnterior(JNIEnv* env, jobject obj) {
    jint resultado = 0;

    jclass claseJuego = (*env)->GetObjectClass(env, obj);

    jfieldID fidTurno = (*env)->GetFieldID(env, claseJuego, "turnoActual", "I");
    jint turno = (*env)->GetIntField(env, obj, fidTurno);

    jfieldID fidJugadores = (*env)->GetFieldID(env, claseJuego, "jugadores", "[Lcomdiegocano/mentiroso/Jugador;");
    jobjectArray arregloJugadores = (jobjectArray)(*env)->GetObjectField(env, obj, fidJugadores);

    jsize cantidad = (*env)->GetArrayLength(env, arregloJugadores);

    __asm {
        mov eax, turno          ; eax = turnoActual
        dec eax                 ; eax = turnoActual - 1
        add eax, cantidad       ; eax = (turnoActual - 1 + cantidad)
        mov ecx, cantidad
        mov edx, 0
        div ecx                 
        mov resultado, edx
    }

    return resultado;
}


JNIEXPORT void JNICALL Java_comdiegocano_mentiroso_Juego_incrementarRonda(JNIEnv* env, jobject thisObj) {
    jclass clase = (*env)->GetObjectClass(env, thisObj);
    jfieldID idRonda = (*env)->GetFieldID(env, clase, "ronda", "I");

    jint rondaActual = (*env)->GetIntField(env, thisObj, idRonda);

    __asm {
        mov eax, rondaActual
        add eax, 1
        mov rondaActual, eax
    }

    (*env)->SetIntField(env, thisObj, idRonda, rondaActual);
}

JNIEXPORT jboolean JNICALL Java_comdiegocano_mentiroso_Juego_esGanadorJuego(JNIEnv* env, jobject thisObj, jint puntos) {
    jclass clase = (*env)->GetObjectClass(env, thisObj);
    jfieldID idMax = (*env)->GetFieldID(env, clase, "puntosParaGanar", "I");

    jint puntosMaximos = (*env)->GetIntField(env, thisObj, idMax);
    jboolean resultado = JNI_FALSE;

    __asm {
        mov eax, puntos
        cmp eax, puntosMaximos
        jl noGana
        mov resultado, 1
        noGana:
    }

    return resultado;
}
