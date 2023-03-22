package principal;

/**
 *
 * @author Diego
 */
import java.util.ArrayList;

public class Pila<T> {

    private ArrayList<T> elementos;

    public Pila() {
        this.elementos = new ArrayList<T>();
    }

    public void push(T elemento) {
        this.elementos.add(elemento);
    }

    public T pop() {
        if (this.estaVacia()) {
            throw new RuntimeException("La pila esta vacia");
        }
        return this.elementos.remove(this.elementos.size() - 1);
    }

    public T peek() {
        if (this.estaVacia()) {
            throw new RuntimeException("La pila esta vacia");
        }
        return this.elementos.get(this.elementos.size() - 1);
    }

    public boolean estaVacia() {
        return this.elementos.isEmpty();
    }

    public int tamano() {
        return this.elementos.size();
    }
    
    public String prefija(){
        String prefija = "";
        
        for (int i = 0; i < elementos.size(); i++) {
            prefija+= " " + elementos.get(i);
        }
        
        return prefija;
    }
}
