package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelEncabezado extends JPanel {

    private JLabel lblTitulo;
    private JLabel lblCarpeta;
    private JButton btnCarpeta;

    public PanelEncabezado() {
        setLayout(new BorderLayout());

        lblTitulo = new JLabel("  Agente IA");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));

        lblCarpeta = new JLabel("Carpeta: (ninguna)");
        btnCarpeta = new JButton("Elegir carpeta...");

        JPanel pnlCarpeta = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlCarpeta.add(lblCarpeta);
        pnlCarpeta.add(btnCarpeta);

        add(lblTitulo, BorderLayout.WEST);
        add(pnlCarpeta, BorderLayout.EAST);
    }
}
