#include  <jni.h>

#include <stdio.h>
#include "C:/Users/diego/Documents/NetBeansProjects/prueba/src/main/java/comdiegocano/prueba/comdiegocano_prueba_Prueba.h"



JNIEXPORT jint JNICALL Java_comdiegocano_prueba_Prueba_sumar(JNIEnv* env, jobject obj, jint a, jint b) {
    jint resultado;
    __asm {
        mov eax, a
        add eax, b
        mov resultado, eax
    }
    return resultado;
}

// Implementación de restar en ensamblador x86
JNIEXPORT jint JNICALL Java_comdiegocano_prueba_Prueba_restar(JNIEnv* env, jobject obj, jint a, jint b) {
    jint resultado;
    __asm {
        mov eax, a
        sub eax, b
        mov resultado, eax
    }
    return resultado;
}

// Implementación de multiplicar en ensamblador x86
JNIEXPORT jint JNICALL Java_comdiegocano_prueba_Prueba_multiplicar(JNIEnv* env, jobject obj, jint a, jint b) {
    jint resultado;
    __asm {
        mov eax, a
        imul eax, b
        mov resultado, eax
    }
    return resultado;
}

