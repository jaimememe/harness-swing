package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PanelEntrada extends JPanel implements ActionListener {

    private JTextField txtMensaje;
    private JButton btnEnviar;
    private JButton btnLimpiar;
    private PanelChat pnlChat;

    public PanelEntrada(PanelChat pnlChat) {
        this.pnlChat = pnlChat;

        setLayout(new BorderLayout());

        txtMensaje = new JTextField();
        btnEnviar = new JButton("Enviar");
        btnLimpiar = new JButton("Limpiar");

        btnEnviar.addActionListener(this);
        btnLimpiar.addActionListener(this);

        JPanel pnlBotones = new JPanel(new FlowLayout());
        pnlBotones.add(btnEnviar);
        pnlBotones.add(btnLimpiar);

        add(txtMensaje, BorderLayout.CENTER);
        add(pnlBotones, BorderLayout.EAST);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnEnviar) {
            String texto = txtMensaje.getText();
            if (!texto.isEmpty()) {
                pnlChat.agregarMensaje("jaimeme: " + texto);
                txtMensaje.setText("");
            }
        } else if (e.getSource() == btnLimpiar) {
            pnlChat.limpiar();
        }
    }
}
