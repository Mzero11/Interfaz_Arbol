import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class AplicacionArbol {
    private final ArbolBusquedaBinario arbol;
    private final JFrame ventana;
    private final JTextField campoTexto;
    private final JTextArea areaResultado;
    private final PanelArbol panelArbol;
    private final List<Integer> recorridoActual;
    private Timer temporizador;
    private int indiceRecorrido;

    public AplicacionArbol() {
        arbol = new ArbolBusquedaBinario();
        recorridoActual = new ArrayList<>();
        indiceRecorrido = 0;
        ventana = new JFrame("Árbol Binario de Búsqueda");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(800, 600);

        JPanel panel = new JPanel();
        ventana.getContentPane().add(panel);
        panel.setLayout(null);

        JLabel etiquetaInsertar = new JLabel("Inserte un nodo:");
        etiquetaInsertar.setBounds(10, 20, 100, 25);
        panel.add(etiquetaInsertar);

        campoTexto = new JTextField(10);
        campoTexto.setBounds(120, 20, 150, 25);
        panel.add(campoTexto);

        JButton botonInsertar = new JButton("Insertar");
        botonInsertar.setBounds(280, 20, 80, 25);
        panel.add(botonInsertar);
        botonInsertar.addActionListener(e -> insertarNodo());

        JButton botonPreorden = new JButton("Preorden");
        botonPreorden.setBounds(10, 60, 100, 25);
        panel.add(botonPreorden);
        botonPreorden.addActionListener(e -> mostrarPreorden());

        JButton botonInorden = new JButton("Inorden");
        botonInorden.setBounds(120, 60, 100, 25);
        panel.add(botonInorden);
        botonInorden.addActionListener(e -> mostrarInorden());

        JButton botonPostorden = new JButton("Postorden");
        botonPostorden.setBounds(230, 60, 100, 25);
        panel.add(botonPostorden);
        botonPostorden.addActionListener(e -> mostrarPostorden());

        areaResultado = new JTextArea();
        areaResultado.setBounds(10, 100, 350, 150);
        panel.add(areaResultado);

        panelArbol = new PanelArbol();
        panelArbol.setBounds(370, 100, 400, 400);
        panel.add(panelArbol);

        ventana.setVisible(true);
    }

    private void insertarNodo() {
        try {
            int clave = Integer.parseInt(campoTexto.getText());
            arbol.insertar(clave);
            JOptionPane.showMessageDialog(ventana, "Nodo " + clave + " insertado.");
            panelArbol.repaint(); // Actualiza el panel del árbol
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(ventana, "Por favor ingrese un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        campoTexto.setText("");
    }

    private void mostrarPreorden() {
        actualizarRecorrido(arbol::preorden, "Preorden");
    }

    private void mostrarInorden() {
        actualizarRecorrido(arbol::inorden, "Inorden");
    }

    private void mostrarPostorden() {
        actualizarRecorrido(arbol::postorden, "Postorden");
    }

    private void actualizarRecorrido(Recorrido recorrido, String tipo) {
        recorridoActual.clear();
        recorrido.realizar(arbol.raiz, recorridoActual);
        areaResultado.setText(tipo + ": " + recorridoActual);
        iniciarAnimacionRecorrido();
    }

    private void iniciarAnimacionRecorrido() {
        if (temporizador != null && temporizador.isRunning()) {
            temporizador.stop();
        }

        indiceRecorrido = 0;
        temporizador = new Timer(500, e -> {
            panelArbol.repaint();
            indiceRecorrido++;
            if (indiceRecorrido >= recorridoActual.size()) {
                temporizador.stop();
            }
        });
        temporizador.start();
    }

    public static void main(String[] args) {
        new AplicacionArbol();
    }

    @FunctionalInterface
    interface Recorrido {
        void realizar(NodoArbol nodo, List<Integer> recorrido);
    }

    class PanelArbol extends JPanel {
        private final int ESPACIO_NODOS = 50;
        private final Color COLOR_NODO_NORMAL = Color.WHITE;
        private final Color COLOR_NODO_RECORRIDO = Color.PINK;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (arbol.raiz != null) {
                dibujarArbol(g, arbol.raiz, getWidth() / 2, 30, getWidth() / 4);
                pintarRecorrido(g, arbol.raiz, getWidth() / 2, 30, getWidth() / 4);
            }
        }

        private void dibujarArbol(Graphics g, NodoArbol nodo, int x, int y, int espaciado) {
            if (nodo == null) return;

            dibujarNodo(g, nodo, x, y, COLOR_NODO_NORMAL);

            if (nodo.izquierdo != null) {
                g.setColor(Color.BLACK);
                g.drawLine(x, y, x - espaciado, y + ESPACIO_NODOS);
                dibujarArbol(g, nodo.izquierdo, x - espaciado, y + ESPACIO_NODOS, espaciado / 2);
            }

            if (nodo.derecho != null) {
                g.setColor(Color.BLACK);
                g.drawLine(x, y, x + espaciado, y + ESPACIO_NODOS);
                dibujarArbol(g, nodo.derecho, x + espaciado, y + ESPACIO_NODOS, espaciado / 2);
            }
        }

        private void pintarRecorrido(Graphics g, NodoArbol nodo, int x, int y, int espaciado) {
            if (nodo == null) return;

            Color colorNodo = recorridoActual.contains(nodo.valor) && recorridoActual.indexOf(nodo.valor) <= indiceRecorrido
                    ? COLOR_NODO_RECORRIDO
                    : COLOR_NODO_NORMAL;

            dibujarNodo(g, nodo, x, y, colorNodo);

            if (nodo.izquierdo != null) {
                g.setColor(Color.BLACK);
                g.drawLine(x, y, x - espaciado, y + ESPACIO_NODOS);
                pintarRecorrido(g, nodo.izquierdo, x - espaciado, y + ESPACIO_NODOS, espaciado / 2);
            }

            if (nodo.derecho != null) {
                g.setColor(Color.BLACK);
                g.drawLine(x, y, x + espaciado, y + ESPACIO_NODOS);
                pintarRecorrido(g, nodo.derecho, x + espaciado, y + ESPACIO_NODOS, espaciado / 2);
            }
        }

        private void dibujarNodo(Graphics g, NodoArbol nodo, int x, int y, Color color) {
            g.setColor(color);
            int RADIO_NODO = 20;
            g.fillOval(x - RADIO_NODO, y - RADIO_NODO, 2 * RADIO_NODO, 2 * RADIO_NODO);
            g.setColor(Color.BLACK);
            g.drawString(String.valueOf(nodo.valor), x - 5, y + 5);
        }
    }
}
