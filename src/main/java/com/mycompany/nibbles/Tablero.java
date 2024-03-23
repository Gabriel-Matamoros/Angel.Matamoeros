/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.nibbles;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JOptionPane;

/**
 *
 * @author Carlos
 */
public class Tablero extends JPanel implements ActionListener {

    private final int ANCHO_TABLERO = 600;
    private final int ALTURA_TABLERO = 600;
    private final int TAM_PUNTO = 10;
    private final int RAND_POS = 59;
    private final int RETRASO = 140;
    private final int TODOS_PUNTOS = 3600;

    private final int x[] = new int[TODOS_PUNTOS];
    private final int y[] = new int[TODOS_PUNTOS];

    private Image punto;
    private Image cabeza;
    private Image manzana;

    private Timer timer;
    private int puntos;
    private int manzana_x;
    private int manzana_y;
    private int puntajeJuego; //4. PARA EL PUNTAJE

    private boolean enJuego = true;
    private boolean dirDerecha = true;
    private boolean dirArriba = false;
    private boolean dirIzquierda = false;
    private boolean dirAbajo = false;

    public Tablero() {
        inicializarTablero();
        puntajeJuego = 0; //4. INICIA EL PUNTAJE EN 0
    }

    public void inicializarTablero() {
        addKeyListener(new AdaptadorTeclado());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(ANCHO_TABLERO, ALTURA_TABLERO));
        cargarImagenes();
        inicializarJuego();
    }

    private void cargarImagenes() {
        ImageIcon iiPunto = new ImageIcon("src/main/java/com/mycompany/nibbles/recursos/dot.png");
        punto = iiPunto.getImage();

        ImageIcon iiCabeza = new ImageIcon("src/main/java/com/mycompany/nibbles/recursos/head.png");
        cabeza = iiCabeza.getImage();

        ImageIcon iiManzana = new ImageIcon("src/main/java/com/mycompany/nibbles/recursos/apple.png");
        manzana = iiManzana.getImage();
    }

    private void inicializarJuego() {
        puntos = 3;
        for (int i = 0; i < puntos; i++) {
            x[i] = 50 - i * 10;
            y[i] = 50;
        }

        posicionarManzana();

        timer = new Timer(RETRASO, this);
        timer.start();
    }

    private void posicionarManzana() {
        int r = (int) (Math.random() * RAND_POS);
        manzana_x = ((r * TAM_PUNTO));

        r = (int) (Math.random() * RAND_POS);
        manzana_y = ((r * TAM_PUNTO));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        pintar(g);
        //4. MOSTRAR PUNTAJE EN LA ESQUINA SUPERIOR DERECHA
        String puntajeString = "PUNTUACIÓN: " + puntajeJuego;
        g.setColor(Color.RED);
        g.drawString(puntajeString, ANCHO_TABLERO - 100, 50);
    }

    private void pintar(Graphics g) {
        if (enJuego) {
            g.drawImage(manzana, manzana_x, manzana_y, this);

            for (int i = 0; i < puntos; i++) {
                if (i == 0) {
                    g.drawImage(cabeza, x[i], y[i], this);
                } else {
                    g.drawImage(punto, x[i], y[i], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        } else {
            finJuego(g);
        }
    }

    private void finJuego(Graphics g) {
        String msj = "Fin de Juego";
        Font peq = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metrics = getFontMetrics(peq);

        g.setColor(Color.white);
        g.setFont(peq);
        g.drawString(msj, (ANCHO_TABLERO - metrics.stringWidth(msj)) / 2, ALTURA_TABLERO / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (enJuego) {
            verificarManzana();
            verificarColision();
            Mover();
        }

        repaint();
    }

    private void verificarManzana() {
        if ((x[0] == manzana_x) && (y[0] == manzana_y)) {
            puntos++;
            puntajeJuego++; //4. INCREMENTA CADA QUE SE COMA UNA MANZANA
            posicionarManzana();
        }
    }

    private void verificarColision() {
        //2. PERMITE A LA SERPIENTE SALIR DEL TABLERO
        if (x[0] >= ANCHO_TABLERO) {
            x[0] = 0; // Lado derech a lado izquierdo
        } else if (x[0] < 0) {
            x[0] = ANCHO_TABLERO - TAM_PUNTO; // Lado izquierdo a lado derecho
        }
        if (y[0] >= ALTURA_TABLERO) {
            y[0] = 0; // Parte inferior a parte superior
        } else if (y[0] < 0) {
            y[0] = ALTURA_TABLERO - TAM_PUNTO; // Parte superior a parte inferior
        }
        for (int i = puntos; i > 0; i--) {

            //Cabeza colisiono con cola
            if ((i > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
                enJuego = false;
            }

            if (y[0] >= ALTURA_TABLERO) {
                enJuego = false;
            }

            if (y[0] < 0) {
                enJuego = false;
            }

            if (x[0] >= ANCHO_TABLERO) {
                enJuego = false;
            }

            if (x[0] < 0) {
                enJuego = false;
            }

            if (!enJuego) {
                timer.stop();
            }

            //1. PERMITE REINICIAR EL JUEGO
            if (!enJuego) {
                timer.stop();
                int respuesta = JOptionPane.showConfirmDialog(this, "¿Desea reiniciar el juego?", "Fin de Juego", JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    reiniciarJuego(); // Llama al método para reiniciar el juego.

                    //3. VERIFICA LA COLISION CON ALGUNA PARTE DEL CUERPO Y FINALIZA EL JUEGO
                } else {
                    JOptionPane.showMessageDialog(this, "Fin del Juego", "Fin de Juego", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0); // Salir del juego
                }
            }
        }
    }

    private void Mover() {

        //mover los puntos verdes siguiendo la ultima ubicacion del punto rojo
        for (int i = puntos; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (dirIzquierda) {
            x[0] -= TAM_PUNTO;
        }

        if (dirDerecha) {
            x[0] += TAM_PUNTO;
        }

        if (dirArriba) {
            y[0] -= TAM_PUNTO;
        }

        if (dirAbajo) {
            y[0] += TAM_PUNTO;
        }
    }

    private class AdaptadorTeclado extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            int tecla = e.getKeyCode();

            if (tecla == KeyEvent.VK_SPACE) { // Tecla espacio para reiniciar el juego
                reiniciarJuego();
            }

            if ((tecla == KeyEvent.VK_LEFT) && (!dirDerecha)) {
                dirIzquierda = true;
                dirArriba = false;
                dirAbajo = false;
            }

            if ((tecla == KeyEvent.VK_RIGHT) && (!dirIzquierda)) {
                dirDerecha = true;
                dirArriba = false;
                dirAbajo = false;
            }

            if ((tecla == KeyEvent.VK_UP) && (!dirAbajo)) {
                dirArriba = true;
                dirDerecha = false;
                dirIzquierda = false;
            }

            if ((tecla == KeyEvent.VK_DOWN) && (!dirArriba)) {
                dirAbajo = true;
                dirDerecha = false;
                dirIzquierda = false;
            }
        }
    }

    //1. METODO DE REINICIO DEL JUEGO
    private void reiniciarJuego() {
        puntos = 3;
        enJuego = true;
        dirDerecha = true;
        dirArriba = false;
        dirIzquierda = false;
        dirAbajo = false;

        for (int i = 0; i < puntos; i++) {
            x[i] = 50 - i * 10;
            y[i] = 50;
        }

        posicionarManzana();

        if (timer != null) {
            timer.stop();
            timer = new Timer(RETRASO, this);
            timer.start();
        }
    }

}
