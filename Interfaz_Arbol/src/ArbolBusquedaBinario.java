import java.util.List;

public class ArbolBusquedaBinario {
    NodoArbol raiz;

    ArbolBusquedaBinario() {
        raiz = null;
    }

    void insertar(int clave) {
        raiz = insertarRec(raiz, clave);
    }

    NodoArbol insertarRec(NodoArbol nodo, int clave) {
        if (nodo == null) {
            nodo = new NodoArbol(clave);
            return nodo;
        }
        if (clave < nodo.valor)
            nodo.izquierdo = insertarRec(nodo.izquierdo, clave);
        else if (clave > nodo.valor)
            nodo.derecho = insertarRec(nodo.derecho, clave);

        return nodo;
    }

    void preorden(NodoArbol nodo, List<Integer> resultado) {
        if (nodo != null) {
            resultado.add(nodo.valor);
            preorden(nodo.izquierdo, resultado);
            preorden(nodo.derecho, resultado);
        }
    }

    void inorden(NodoArbol nodo, List<Integer> resultado) {
        if (nodo != null) {
            inorden(nodo.izquierdo, resultado);
            resultado.add(nodo.valor);
            inorden(nodo.derecho, resultado);
        }
    }

    void postorden(NodoArbol nodo, List<Integer> resultado) {
        if (nodo != null) {
            postorden(nodo.izquierdo, resultado);
            postorden(nodo.derecho, resultado);
            resultado.add(nodo.valor);
        }
    }


}
