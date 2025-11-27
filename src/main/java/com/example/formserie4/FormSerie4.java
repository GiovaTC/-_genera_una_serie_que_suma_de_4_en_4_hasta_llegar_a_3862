package com.example.formserie4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class FormSerie4 extends JFrame  {

    private JTextArea txtResultado;
    private JButton btnCalcular, btnGuardar;
    private JLabel lblEstado;
    private long resultadoFinal = 0;

    private final String connectionString = "jdbc:oracle:thin:@//localhost:1521/orcl";
    private final String userDB = "system";
    private final String passDB = "Tapiero123";

    public FormSerie4() {
        setTitle("Serie de 4 en 4 - Java + oracle");
        setSize(450,350);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        txtResultado = new JTextArea(8, 35);
        txtResultado.setEditable(false);
        btnCalcular = new JButton("Generar Serie");
        btnGuardar = new JButton("Guardar en BD");
        lblEstado = new JLabel("Estado: en espera ...");

        add(new JLabel("Resultado Serie:"));
        add(txtResultado);
        add(btnCalcular);
        add(btnGuardar);
        add(lblEstado);

        btnCalcular.addActionListener(this::generarSerie);
        btnGuardar.addActionListener(this::guardarEnBD);
    }

    private void generarSerie(ActionEvent e) {
        resultadoFinal = 0;
        StringBuilder serie = new StringBuilder();

        for (int i = 0; i <= 3862; i += 4) {
            resultadoFinal += i;
            serie.append(i).append(",");
        }
        txtResultado.setText("Serie: " + serie + "\n\nSuma Total = " +  resultadoFinal);
        lblEstado.setText("Estado: Serie generada ✔");
    }

    private void guardarEnBD(ActionEvent e) {
        if (resultadoFinal == 0) {
            lblEstado.setText("Debe generar la serie primero ❌");
            return;
        }
        try (Connection conn = DriverManager.getConnection(connectionString, userDB, passDB)) {
             String sql =  "{ call PR_SERIE4_REG(?, ?) }";
             CallableStatement cs = conn.prepareCall(sql);
             cs.setLong(1, resultadoFinal);
             cs.setDate(2, new java.sql.Date(System.currentTimeMillis()));
             cs.execute();

             lblEstado.setText("Datos guardados en oracle ✔");
        } catch (Exception ex) {
            lblEstado.setText("Error BD ❌");
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormSerie4().setVisible(true));
    }
}   
