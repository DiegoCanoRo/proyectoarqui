#include <jni.h>

#include <stdio.h>
#include "C:\Users\diego\Documents\NetBeansProjects\mentiroso\src\main\java\comdiegocano\mentiroso\comdiegocano_mentiroso_Juego.h"

JNIEXPORT void JNICALL Java_comdiegocano_mentiroso_Juego_cambiarTurno(JNIEnv* env, jobject obj) {
    // Obtener la clase
    jclass claseJuego = (*env)->GetObjectClass(env, obj);

    // Obtener los campos turnoActual y cantidadJugadores
    jfieldID fidTurno = (*env)->GetFieldID(env, claseJuego, "turnoActual", "I");
    jfieldID fidCantidad = (*env)->GetFieldID(env, claseJuego, "cantidadJugadores", "I");

    jint turno = (*env)->GetIntField(env, obj, fidTurno);
    jint cantidad = (*env)->GetIntField(env, obj, fidCantidad);

    __asm {
        mov eax, turno
        add eax, 1
        mov edx, 0
        mov ecx, cantidad
        div ecx; eax = (turno + 1) / cantidad, edx = (turno + 1) % cantidad
        mov turno, edx
    }

    (*env)->SetIntField(env, obj, fidTurno, turno);
}
