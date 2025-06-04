#include <jni.h>

#include <stdio.h>
#include "C:\Users\diego\Documents\NetBeansProjects\mentiroso\src\main\java\comdiegocano\mentiroso\comdiegocano_mentiroso_Jugador.h"


JNIEXPORT void JNICALL Java_comdiegocano_mentiroso_Jugador_agregarPunto(JNIEnv* env, jobject obj) {
    jclass claseJugador = (*env)->GetObjectClass(env, obj);
    jfieldID campoPuntos = (*env)->GetFieldID(env, claseJugador, "puntuacion", "I");
    jint puntos = (*env)->GetIntField(env, obj, campoPuntos);

    __asm {
        mov eax, puntos
        inc eax
        mov puntos, eax
    }

    (*env)->SetIntField(env, obj, campoPuntos, puntos);
}

JNIEXPORT void JNICALL Java_comdiegocano_mentiroso_Jugador_quitarPunto(JNIEnv* env, jobject obj) {
    jclass claseJugador = (*env)->GetObjectClass(env, obj);
    jfieldID campoPuntos = (*env)->GetFieldID(env, claseJugador, "puntuacion", "I");
    jint puntos = (*env)->GetIntField(env, obj, campoPuntos);

    __asm {
        mov eax, puntos
        dec eax
        mov puntos, eax
    }

    (*env)->SetIntField(env, obj, campoPuntos, puntos);
}