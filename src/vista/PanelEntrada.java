package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import modelo.Agent;

public class PanelEntrada extends JPanel implements ActionListener {

    private JTextField txtMensaje;
    private JButton btnEnviar;
    private JButton btnLimpiar;
    private PanelChat pnlChat;
    private PanelAgentes ventanaAgente;
    private PanelEncabezado pnlEncabezado;

    public PanelEntrada(PanelChat pnlChat, PanelAgentes ventanaAgente, PanelEncabezado pnlEncabezado) {
        this.pnlChat = pnlChat;
        this.ventanaAgente = ventanaAgente;
        this.pnlEncabezado = pnlEncabezado;

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
        SwingUtilities.invokeLater(() ->{
            getRootPane().setDefaultButton(btnEnviar);
            txtMensaje.requestFocusInWindow();
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (ventanaAgente.getAgenteSeleccionado() == null) {
            pnlChat.agregarMensaje("Error: primero selecciona un agente.");
            return;
        }

        if (e.getSource() == btnEnviar) {
            String texto = txtMensaje.getText();
            if (!texto.isEmpty()) {
                pnlChat.agregarMensaje("jaimeme: " + texto);

                // Si hay un archivo elegido, su contenido se agrega al mensaje para la IA
                String mensajeParaIA = texto;
                if (pnlEncabezado.getArchivoSeleccionado() != null) {
                    String nombreArchivo = pnlEncabezado.getArchivoSeleccionado().getName();
                    mensajeParaIA = texto + "\n\nContenido del archivo " + nombreArchivo + ":\n"
                            + pnlEncabezado.getContenidoArchivo();
                    pnlChat.agregarMensaje("(archivo adjunto: " + nombreArchivo + ")");
                    pnlEncabezado.limpiarArchivo(); // se envía una sola vez
                }
                String mensajeFinal = mensajeParaIA; // el Thread necesita una variable que no cambie

                pnlChat.agregarMensaje("...");
                txtMensaje.setText("");
                txtMensaje.requestFocus();
                new Thread(() -> {
                    try {
                        Agent agent = new Agent(ventanaAgente.getAgenteSeleccionado());
                        String respuesta = agent.chatear(mensajeFinal);
                        pnlChat.quitarCargando();
                        pnlChat.agregarMensaje("ia: " + respuesta);
                    } catch (Exception error) {
                        pnlChat.agregarMensaje("Error: no se pudo obtener la respuesta (" + error + ")");
                    }
                }).start();
                
            }
        } else if (e.getSource() == btnLimpiar) {
            pnlChat.limpiar();
        }
    }
     
}
