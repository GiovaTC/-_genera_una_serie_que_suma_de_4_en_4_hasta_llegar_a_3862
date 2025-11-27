# -_genera_una_serie_que_suma_de_4_en_4_hasta_llegar_a_3862 :.
# 📌 Programa Java -- Serie de 4 en 4 + Oracle 19c .

<img width="1536" height="1024" alt="image" src="https://github.com/user-attachments/assets/3999818e-c66b-4959-8458-868c95bce0a7" />    

🧮 Genera una serie que suma de 4 en 4 hasta llegar a **3862**\
🖥️ Muestra el resultado en una interfaz gráfica (Swing)\
🗄️ Registra la información en **Oracle 19c mediante procedimiento
almacenado (PL/SQL)**

------------------------------------------------------------------------

## 📌 1. Lógica de la serie

Serie:

    0, 4, 8, 12, 16, ..., 3860, 3864 (se detiene en 3862 por límite)

👉 **Resultado final es la suma de todos los valores generados.**

------------------------------------------------------------------------

## 🛢️ 2. Procedimiento almacenado Oracle 19c

Ejecutar en Oracle:

``` sql
CREATE OR REPLACE PROCEDURE PR_SERIE4_REG (
    p_valor_final    IN NUMBER,
    p_fecha_registro IN DATE
) AS
BEGIN
    INSERT INTO SERIE4_LOG (ID_LOG, RESULTADO, FECHA_REGISTRO)
    VALUES (SERIE4_LOG_SEQ.NEXTVAL, p_valor_final, p_fecha_registro);
    COMMIT;
END;
/
```

Crear tabla y secuencia (si no existen):

``` sql
CREATE TABLE SERIE4_LOG (
    ID_LOG NUMBER PRIMARY KEY,
    RESULTADO NUMBER,
    FECHA_REGISTRO DATE
);

CREATE SEQUENCE SERIE4_LOG_SEQ START WITH 1 INCREMENT BY 1;
```

------------------------------------------------------------------------

## 🧪 3. Código completo Java (IntelliJ -- Swing + Oracle)

📌 Agregar dependencia en `pom.xml` (si se usa Maven):

``` xml
<dependency>
    <groupId>com.oracle.database.jdbc</groupId>
    <artifactId>ojdbc8</artifactId>
    <version>19.3.0.0</version>
</dependency>
```

📂 **FormSerie4.java**

``` java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class FormSerie4 extends JFrame {

    private JTextArea txtResultado;
    private JButton btnCalcular, btnGuardar;
    private JLabel lblEstado;
    private long resultadoFinal = 0;

    private final String connectionString = "jdbc:oracle:thin:@localhost:1521:orcl";
    private final String userDB = "SYSTEM";
    private final String passDB = "Tapiero123";

    public FormSerie4() {
        setTitle("Serie de 4 en 4 – Java + Oracle");
        setSize(450, 350);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        txtResultado = new JTextArea(8, 35);
        txtResultado.setEditable(false);
        btnCalcular = new JButton("Generar Serie");
        btnGuardar = new JButton("Guardar en BD");
        lblEstado = new JLabel("Estado: en espera...");

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
        txtResultado.setText("Serie: " + serie + "\n\nSuma Total = " + resultadoFinal);
        lblEstado.setText("Estado: Serie generada ✔");
    }

    private void guardarEnBD(ActionEvent e) {
        if (resultadoFinal == 0) {
            lblEstado.setText("Debe generar la serie primero ❌");
            return;
        }
        try (Connection conn = DriverManager.getConnection(connectionString, userDB, passDB)) {
            String sql = "{ call PR_SERIE4_REG(?, ?) }";
            CallableStatement cs = conn.prepareCall(sql);
            cs.setLong(1, resultadoFinal);
            cs.setDate(2, new java.sql.Date(System.currentTimeMillis()));
            cs.execute();

            lblEstado.setText("Datos guardados en Oracle ✔");
        } catch (Exception ex) {
            lblEstado.setText("Error BD ❌");
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormSerie4().setVisible(true));
    }
}
```

------------------------------------------------------------------------

## 📦 4. Requisitos para ejecutar

  Componente         Detalle
  ------------------ -----------------------------------------
  💻 IDE             IntelliJ IDEA
  ☕ JDK             17 o superior
  📦 Driver Oracle   ojdbc8.jar o vía Maven
  🛢️ BD              Oracle 19c
  🚪 Puerto          1521 por defecto
  🔑 Usuario BD      SYSTEM (recomendado otro en producción)

------------------------------------------------------------------------

## 🎯 Resultado esperado

✔ Ventana Java muestra la serie y suma\
✔ Botón **Generar Serie** → Calcula\
✔ Botón **Guardar en BD** → Ejecuta procedimiento almacenado\
✔ Registro aparece en `SERIE4_LOG`

📌 Query de verificación:

``` sql
SELECT * FROM SERIE4_LOG ORDER BY ID_LOG DESC;
```

------------------------------------------------------------------------
------------------------------------------------------------------------

✍️ *Generado automáticamente con ❤️*  :. 
