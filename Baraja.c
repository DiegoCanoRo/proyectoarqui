#include <jni.h>

#include <stdio.h>
#include "C:\Users\diego\Documents\NetBeansProjects\mentiroso\src\main\java\comdiegocano\mentiroso\comdiegocano_mentiroso_Baraja.h"


//cambiar "[Lcomdiegocano/mentiroso/Carta;" 

JNIEXPORT jint JNICALL Java_comdiegocano_mentiroso_Baraja_tamanoBaraja(JNIEnv* env, jobject obj) {
    jint contador = 0;

    jclass claseBaraja = (*env)->GetObjectClass(env, obj);
    jfieldID fidBaraja = (*env)->GetFieldID(env, claseBaraja, "baraja", "[Lcomdiegocano/mentiroso/Carta;");
    jobjectArray arregloBaraja = (jobjectArray)(*env)->GetObjectField(env, obj, fidBaraja);

    jobject elementos[52];
    for (int i = 0; i < 52; i++) {
        elementos[i] = (*env)->GetObjectArrayElement(env, arregloBaraja, i);
    }

    __asm {
        lea esi, elementos
        mov eax, 0
        mov ecx, 52; numero de cartas

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

    jobject elementos[52];
    for (int i = 0; i < 52; i++) {
        elementos[i] = (*env)->GetObjectArrayElement(env, arregloBaraja, i);
    }

    int pos = (int)posicion;

    jobject* pElementos = elementos;  // puntero al arreglo local

    __asm {
        mov esi, pElementos; elementos[0]
        mov ebx, pos; indice de la carta que se va a eliminar

        mov eax, ebx
        mov eax, pos
        add eax, eax; eax = pos * 2
        add eax, eax; eax = pos * 4

        add esi, eax; esi = elementos[pos]

            mov ecx, 52
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

    for (int i = 0; i < 52; i++) {
        (*env)->SetObjectArrayElement(env, arregloBaraja, i, elementos[i]);
    }
}




//agrega una carta en el primer lugar sin carta, osea null que encuentra
JNIEXPORT void JNICALL Java_comdiegocano_mentiroso_Baraja_agregarCarta(JNIEnv* env, jobject obj, jobject carta) {
    jclass claseBaraja = (*env)->GetObjectClass(env, obj);
    jfieldID fidBaraja = (*env)->GetFieldID(env, claseBaraja, "baraja", "[Lcomdiegocano/mentiroso/Carta;");
    jobjectArray arregloBaraja = (jobjectArray)(*env)->GetObjectField(env, obj, fidBaraja);

    jobject elementos[52];
    for (int i = 0; i < 52; i++) {
        elementos[i] = (*env)->GetObjectArrayElement(env, arregloBaraja, i);
    }

    jint indice = -1;

    __asm {
        lea esi, elementos
        mov eax, 0; indice
        mov ecx, 52; contador

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

    jobject elementos[52];
    for (int i = 0; i < 52; i++) {
        elementos[i] = (*env)->GetObjectArrayElement(env, arregloBaraja, i);
    }

    __asm {
        lea esi, elementos
        mov ecx, 52
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

    for (int i = 0; i < 52; i++) {
        (*env)->SetObjectArrayElement(env, arregloBaraja, i, elementos[i]);
    }
}





JNIEXPORT void JNICALL Java_comdiegocano_mentiroso_Baraja_barajar(JNIEnv* env, jobject obj) {
    jclass claseBaraja = (*env)->GetObjectClass(env, obj);
    jfieldID fidBaraja = (*env)->GetFieldID(env, claseBaraja, "baraja", "[Lcomdiegocano/mentiroso/Carta;");
    jobjectArray arregloBaraja = (jobjectArray)(*env)->GetObjectField(env, obj, fidBaraja);

    jobject elementos[52];
    for (int i = 0; i < 52; i++) {
        elementos[i] = (*env)->GetObjectArrayElement(env, arregloBaraja, i);
    }

    jobject* pElementos = elementos;

    __asm {
        mov esi, pElementos
        mov ecx, 52

        ciclo_barajar:
        cmp ecx, 1
            jle fin_barajar; si quedan 1 o menos cartas, termina de barajar


            ; se usa rdtsc para obtener un valor random del contador del cpu
            rdtsc
            mov edx, 0
            mov ebx, ecx
            div ebx; aqui hacemos estos calculos para mas aleatoridad
            mov edi, edx

            mov eax, ecx
            dec eax; indice del ultima carta no barajeada


            mov edx, eax
            mov ebx, 4
            mul ebx; EAX = offset(ecx - 1) * 4
            mov edx, [esi + eax]; temp = elementos[ecx - 1]

            ; calcular offset = edi * 4
            mov eax, edi
            mov ebx, 4
            mul ebx
            mov ebx, [esi + eax]; elementos[edi]

            ; guardar elementos[edi] = temp
                mov eax, edi
                mov ebx, 4
                mul ebx
                mov[esi + eax], edx

                ; guardar elementos[ecx - 1] = elementos[edi]
                mov eax, ecx
                    dec eax
                    mov ebx, 4
                    mul ebx
                    mov[esi + eax], ebx

                    dec ecx; reducir contador, y carta que sigue
                    jmp ciclo_barajar

                    fin_barajar :

                ; se mueven las cartas nulas al final
                    mov edi, 0; posicion donde se movera la carta
                    mov ecx, 52; total cartas
                    mov ebx, 0; posicion donde se obtendra la carta

                    ciclo_compactar :
                cmp ebx, ecx
                    jge fin_compactar

                    ; calcular direccion donde se colocara
                    mov eax, ebx
                    mov edx, 4
                    mul edx
                    mov edx, [esi + eax]
                    test edx, edx
                    jz es_nulo_compactar

                    cmp edi, ebx
                    je siguiente_compactar

                    ; mover elemento hacia la izquierda
                    ; [esi + edi * 4] = edx
                    mov eax, edi
                    mov edx, 4
                    mul edx
                    mov[esi + eax], edx

                    ; poner nulo en la posiciob anterior
                    mov eax, ebx
                    mov edx, 4
                    mul edx
                    mov dword ptr[esi + eax], 0

                    siguiente_compactar:
                inc edi

                    es_nulo_compactar :
                inc ebx
                    jmp ciclo_compactar

                    fin_compactar :
    }

    for (int i = 0; i < 52; i++) {
        (*env)->SetObjectArrayElement(env, arregloBaraja, i, elementos[i]);
    }
}

