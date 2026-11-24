package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PanelEntrada extends JPanel {

    private JTextField txtMensaje;
    private JButton btnEnviar;
    private JButton btnLimpiar;

    public PanelEntrada() {
        setLayout(new BorderLayout());

        txtMensaje = new JTextField();
        btnEnviar = new JButton("Enviar");
        btnLimpiar = new JButton("Limpiar");

        JPanel pnlBotones = new JPanel(new FlowLayout());
        pnlBotones.add(btnEnviar);
        pnlBotones.add(btnLimpiar);

        add(txtMensaje, BorderLayout.CENTER);
        add(pnlBotones, BorderLayout.EAST);
    }
}
