#include <jni.h>

#include <stdio.h>
#include "C:\Users\diego\Documents\NetBeansProjects\mentiroso\src\main\java\comdiegocano\mentiroso\comdiegocano_mentiroso_Jugador.h"


JNIEXPORT void JNICALL Java_comdiegocano_mentiroso_Jugador_agregarPuntos(JNIEnv* env, jobject obj, jint puntosAgregar) {
    jclass claseJugador = (*env)->GetObjectClass(env, obj);
    jfieldID campoPuntos = (*env)->GetFieldID(env, claseJugador, "puntuacion", "I");
    jint puntosActuales = (*env)->GetIntField(env, obj, campoPuntos);

    __asm {
        mov eax, puntosActuales
        add eax, puntosAgregar
        mov puntosActuales, eax
    }

    (*env)->SetIntField(env, obj, campoPuntos, puntosActuales);
}

JNIEXPORT void JNICALL Java_comdiegocano_mentiroso_Jugador_quitarPuntos(JNIEnv* env, jobject obj, jint puntosQuitar) {
    jclass claseJugador = (*env)->GetObjectClass(env, obj);
    jfieldID campoPuntos = (*env)->GetFieldID(env, claseJugador, "puntuacion", "I");
    jint puntosActuales = (*env)->GetIntField(env, obj, campoPuntos);

    __asm {
        mov eax, puntosActuales
        sub eax, puntosQuitar
        mov puntosActuales, eax
    }

    (*env)->SetIntField(env, obj, campoPuntos, puntosActuales);
}



