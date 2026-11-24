package vista;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class VentanaAgente extends JFrame {

    private PanelEncabezado pnlEncabezado;
    private PanelAgentes pnlAgentes;
    private PanelChat pnlChat;
    private PanelEntrada pnlEntrada;

    public VentanaAgente() {
        setTitle("Agente IA - Programación 2");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        pnlEncabezado = new PanelEncabezado();
        pnlAgentes = new PanelAgentes();
        pnlChat = new PanelChat();
        pnlEntrada = new PanelEntrada();

        add(pnlEncabezado, BorderLayout.NORTH);
        add(pnlAgentes, BorderLayout.WEST);
        add(pnlChat, BorderLayout.CENTER);
        add(pnlEntrada, BorderLayout.SOUTH);
    }
}
