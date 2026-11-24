package vista;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class PanelAgentes extends JPanel {

    private JList<String> lstAgentes;
    private JButton btnNuevoAgente;

    public PanelAgentes() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Agentes"));
        setPreferredSize(new Dimension(200, 0));

        String[] nombres = { "Asistente", "Explorador", "Programador", "Creador de agentes" };
        lstAgentes = new JList<String>(nombres);

        btnNuevoAgente = new JButton("Nuevo agente");

        add(new JScrollPane(lstAgentes), BorderLayout.CENTER);
        add(btnNuevoAgente, BorderLayout.SOUTH);
    }
}
