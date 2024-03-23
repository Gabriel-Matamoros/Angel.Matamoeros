/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.nibbles;

import java.awt.EventQueue;
import javax.swing.JFrame;

/**
 *
 * @author Carlos
 */
public class Nibbles extends JFrame {

    public Nibbles() {
        InicializarIU();
    }

    private void InicializarIU() {
        add(new Tablero());
        setResizable(false);
        pack();

        setTitle("Nibbles");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame prog = new Nibbles();
            prog.setVisible(true);
        });
    }

}
