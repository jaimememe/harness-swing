package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import modelo.GestorArchivos;

public class PanelEncabezado extends JPanel implements ActionListener {

    private JLabel lblTitulo;
    private JLabel lblArchivo;
    private JButton btnArchivo;
    private File archivoSeleccionado;
    private String contenidoArchivo;

    public PanelEncabezado() {
        setLayout(new BorderLayout());

        lblTitulo = new JLabel("  Agente IA");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));

        lblArchivo = new JLabel("Archivo: (ninguno)");
        btnArchivo = new JButton("Elegir archivo...");
        btnArchivo.addActionListener(this);

        JPanel pnlArchivo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlArchivo.add(lblArchivo);
        pnlArchivo.add(btnArchivo);

        add(lblTitulo, BorderLayout.WEST);
        add(pnlArchivo, BorderLayout.EAST);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser selector = new JFileChooser();
        int opcion = selector.showOpenDialog(this);
        if (opcion != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File archivo = selector.getSelectedFile();
        try {
            // GestorArchivos decide si usar leertxt, leerpdf o leerdocx según la extensión
            contenidoArchivo = GestorArchivos.leerArchivo(archivo);
            archivoSeleccionado = archivo;
            lblArchivo.setText("Archivo: " + archivo.getName());
        } catch (Exception error) {
            JOptionPane.showMessageDialog(this, "No se pudo leer " + archivo.getName() + ": " + error.getMessage());
        }
    }

    public File getArchivoSeleccionado() {
        return archivoSeleccionado;
    }

    public String getContenidoArchivo() {
        return contenidoArchivo;
    }

    // Quita el archivo elegido (se usa después de enviarlo a la IA)
    public void limpiarArchivo() {
        archivoSeleccionado = null;
        contenidoArchivo = null;
        lblArchivo.setText("Archivo: (ninguno)");
    }
}
