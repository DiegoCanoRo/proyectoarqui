#include <jni.h>

#include <stdio.h>
#include "C:\Users\diego\Documents\NetBeansProjects\mentiroso\src\main\java\comdiegocano\mentiroso\comdiegocano_mentiroso_Baraja.h"


//cambiar "[Lcomdiegocano/mentiroso/Carta;" 

JNIEXPORT jint JNICALL Java_comdiegocano_mentiroso_Baraja_tamanoBaraja(JNIEnv* env, jobject obj) {
    jint contador = 0;

    jclass claseBaraja = (*env)->GetObjectClass(env, obj);
    jfieldID fidBaraja = (*env)->GetFieldID(env, claseBaraja, "baraja", "[Lcomdiegocano/mentiroso/Carta;");
    jobjectArray arregloBaraja = (jobjectArray)(*env)->GetObjectField(env, obj, fidBaraja);

    jobject elementos[40];
    for (int i = 0; i < 40; i++) {
        elementos[i] = (*env)->GetObjectArrayElement(env, arregloBaraja, i);
    }

    __asm {
        lea esi, elementos
        mov eax, 0
        mov ecx, 40; numero de cartas

        contar_loop :
        cmp ecx, 0
            je fin_contar

            mov edx, [esi]; Carga el elemento actual
            test edx, edx; Si es null se salra
            je continuar

            inc eax; incrementa el contador solo si no hay null osea si hay carta

            continuar :
        add esi, 4; avanzar al siguiente elemento
            dec ecx
            jmp contar_loop

            fin_contar :
        mov contador, eax
    }

    return contador;
}



JNIEXPORT void JNICALL Java_comdiegocano_mentiroso_Baraja_eliminarCarta(JNIEnv* env, jobject obj, jint posicion) {
    jclass claseBaraja = (*env)->GetObjectClass(env, obj);
    jfieldID fidBaraja = (*env)->GetFieldID(env, claseBaraja, "baraja", "[Lcomdiegocano/mentiroso/Carta;");
    jobjectArray arregloBaraja = (jobjectArray)(*env)->GetObjectField(env, obj, fidBaraja);

    jobject elementos[40];
    for (int i = 0; i < 40; i++) {
        elementos[i] = (*env)->GetObjectArrayElement(env, arregloBaraja, i);
    }

    int pos = (int)posicion;

    jobject* pElementos = elementos;  // puntero al arreglo local

    __asm {
        mov esi, pElementos; elementos[0]
        mov ebx, pos; indice de la carta a eliminar

        mov eax, ebx
        mov eax, pos
        add eax, eax; eax = pos * 2
        add eax, eax; eax = pos * 4

        add esi, eax; esi = elementos[pos]

            mov ecx, 40
                sub ecx, ebx
                dec ecx; elementos posteriores a mover

                cmp ecx, 0
                jle fin

                mov edi, esi; destino = &elementos[pos]
                add esi, 4; origen = &elementos[pos + 1]

                    ; se recorren de lugar las cartas
                    loop_mover :
                mov edx, [esi]; elemento siguiente
                    mov[edi], edx; copiar en posición actual

                    add esi, 4
                    add edi, 4

                    dec ecx
                    jnz loop_mover

                    mov dword ptr[edi], 0; ultimo elemento queda null osea se elimino

                    fin :
    }

    for (int i = 0; i < 40; i++) {
        (*env)->SetObjectArrayElement(env, arregloBaraja, i, elementos[i]);
    }
}




//agrega una carta en el primer lugar sin carta, osea null que encuentra
JNIEXPORT void JNICALL Java_comdiegocano_mentiroso_Baraja_agregarCarta(JNIEnv* env, jobject obj, jobject carta) {
    jclass claseBaraja = (*env)->GetObjectClass(env, obj);
    jfieldID fidBaraja = (*env)->GetFieldID(env, claseBaraja, "baraja", "[Lcomdiegocano/mentiroso/Carta;");
    jobjectArray arregloBaraja = (jobjectArray)(*env)->GetObjectField(env, obj, fidBaraja);

    jobject elementos[40];
    for (int i = 0; i < 40; i++) {
        elementos[i] = (*env)->GetObjectArrayElement(env, arregloBaraja, i);
    }

    jint indice = -1;

    __asm {
        lea esi, elementos
        mov eax, 0; indice
        mov ecx, 40; contador

        buscar_null :
        cmp ecx, 0
            je no_encontrado

            mov edx, [esi + eax * 4]
            test edx, edx
            je encontrado

            inc eax
            loop buscar_null

            no_encontrado :
        mov eax, -1

            encontrado :
            mov indice, eax
    }

    if (indice != -1) {
        (*env)->SetObjectArrayElement(env, arregloBaraja, indice, carta);
    }
}

//elimina todas las cartas
JNIEXPORT void JNICALL Java_comdiegocano_mentiroso_Baraja_vaciarBaraja(JNIEnv* env, jobject obj) {
    jclass claseBaraja = (*env)->GetObjectClass(env, obj);
    jfieldID fidBaraja = (*env)->GetFieldID(env, claseBaraja, "baraja", "[Lcomdiegocano/mentiroso/Carta;");
    jobjectArray arregloBaraja = (jobjectArray)(*env)->GetObjectField(env, obj, fidBaraja);

    jobject elementos[40];
    for (int i = 0; i < 40; i++) {
        elementos[i] = (*env)->GetObjectArrayElement(env, arregloBaraja, i);
    }

    __asm {
        lea esi, elementos
        mov ecx, 40
        mov eax, 0

        vaciar_loop :
        cmp ecx, 0
            je fin_vaciar

            mov dword ptr[esi], eax
            add esi, 4
            dec ecx
            jmp vaciar_loop

            fin_vaciar :
    }

    for (int i = 0; i < 40; i++) {
        (*env)->SetObjectArrayElement(env, arregloBaraja, i, elementos[i]);
    }
}






JNIEXPORT void JNICALL Java_comdiegocano_mentiroso_Baraja_barajar(JNIEnv* env, jobject obj) {
    jclass claseBaraja = (*env)->GetObjectClass(env, obj);
    jfieldID fidBaraja = (*env)->GetFieldID(env, claseBaraja, "baraja", "[Lcomdiegocano/mentiroso/Carta;");
    jobjectArray arregloBaraja = (jobjectArray)(*env)->GetObjectField(env, obj, fidBaraja);

    jobject elementos[40];
    for (int i = 0; i < 40; i++) {
        elementos[i] = (*env)->GetObjectArrayElement(env, arregloBaraja, i);
    }

    //puntero del arreglo de elementos
    jobject* pElementos = elementos;

    __asm {
        ; nescla de baraja
        mov esi, pElementos
        mov ecx, 40

        ciclo_barajar:
        cmp ecx, 1
            jle fin_barajar

            rdtsc
            mov edx, 0
            mov ebx, ecx
            div ebx; eax / ecx, resto en edx
            mov edi, edx; índice aleatorio

            mov eax, ecx
            dec eax; eax = ecx - 1

            mov ebx, edi

            mov edx, eax
            shl edx, 2
            mov eax, [esi + edx]; temp = elementos[ecx - 1]

            mov edx, ebx
            shl edx, 2
            mov ebx, [esi + edx]; elementos[edi]

            mov[esi + edx], eax
                mov edx, ecx
                dec edx
                shl edx, 2
                mov[esi + edx], ebx

                dec ecx
                jmp ciclo_barajar

                fin_barajar :

            ; aqui se mueven las cartas hacia la izquierda y asi solo quedan los espacios vacios del arreglo de ultimo
               mov edi, 0
                mov ecx, 40; total cartas
                mov ebx, 0 readPos = 0

                ciclo_compactar:
            cmp ebx, ecx
                jge fin_compactar

                mov edx, [esi + ebx * 4]
                test edx, edx
                jz es_nulo_compactar

                cmp edi, ebx
                je siguiente_compactar

                mov[esi + edi * 4], edx
                mov dword ptr[esi + ebx * 4], 0

                siguiente_compactar:
            inc edi
                es_nulo_compactar :
            inc ebx
                jmp ciclo_compactar

                fin_compactar :
    }

    for (int i = 0; i < 40; i++) {
        (*env)->SetObjectArrayElement(env, arregloBaraja, i, elementos[i]);
    }
}
