package vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.json.JSONArray;
import org.json.JSONObject;

import modelo.Agent;

public class PanelAgentes extends JPanel implements ListSelectionListener, ActionListener {

    private JList<String> lstAgentes;
    private DefaultListModel<String> modeloLista;
    private JButton btnNuevoAgente;
    private PanelChat pnlChat;

    public PanelAgentes(PanelChat pnlChat) {
        this.pnlChat = pnlChat;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Agentes"));
        setPreferredSize(new Dimension(200, 0));

        // Un DefaultListModel permite agregar elementos a la lista después de crearla.
        // La llenamos con el nombre de cada carpeta que hay dentro de agentes/
        modeloLista = new DefaultListModel<String>();
        File[] carpetas = new File("agentes").listFiles();
        if (carpetas != null) {
            for (File carpeta : carpetas) {
                if (carpeta.isDirectory()) {
                    modeloLista.addElement(carpeta.getName());
                }
            }
        }
        lstAgentes = new JList<String>(modeloLista);
        lstAgentes.addListSelectionListener(this);

        btnNuevoAgente = new JButton("Nuevo agente");
        btnNuevoAgente.addActionListener(this);

        add(new JScrollPane(lstAgentes), BorderLayout.CENTER);
        add(btnNuevoAgente, BorderLayout.SOUTH);
    }

    public String getAgenteSeleccionado() {
        return lstAgentes.getSelectedValue();
    }

    // se ejecuta cada vez que se selecciona otro agente en la lista
    @Override
    public void valueChanged(ListSelectionEvent e) {
        // mientras el usuario arrastra el mouse llegan varios eventos; esperamos al último
        if (e.getValueIsAdjusting()) {
            return;
        }

        pnlChat.limpiar();

        Agent agent = new Agent(getAgenteSeleccionado());
        JSONArray transcript = agent.transcript;

        for (int i = 0; i < transcript.length(); i++) {
            JSONObject mensaje = transcript.getJSONObject(i);
            String rol = mensaje.getString("role");
            String contenido = mensaje.getString("content");

            if (rol.equals("user")) {
                pnlChat.agregarMensaje("jaimeme: " + contenido);
            } else if (rol.equals("assistant")) {
                pnlChat.agregarMensaje("ia: " + contenido);
            }
        }
    }

    // se ejecuta al presionar "Nuevo agente"
    @Override
    public void actionPerformed(ActionEvent e) {
        // Armamos la pequeña ventana: un campo para el nombre y otro para el SYSTEM
        JTextField txtNombre = new JTextField();
        JTextArea txtSystem = new JTextArea(8, 30);
        txtSystem.setLineWrap(true);

        JPanel formulario = new JPanel(new BorderLayout(5, 5));
        JPanel pnlNombre = new JPanel(new BorderLayout());
        pnlNombre.add(new JLabel("Nombre:"), BorderLayout.NORTH);
        pnlNombre.add(txtNombre, BorderLayout.CENTER);
        JPanel pnlSystem = new JPanel(new BorderLayout());
        pnlSystem.add(new JLabel("¿Qué va a hacer? (SYSTEM):"), BorderLayout.NORTH);
        pnlSystem.add(new JScrollPane(txtSystem), BorderLayout.CENTER);
        formulario.add(pnlNombre, BorderLayout.NORTH);
        formulario.add(pnlSystem, BorderLayout.CENTER);

        int opcion = JOptionPane.showConfirmDialog(this, formulario, "Nuevo agente",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) {
            return; // el usuario canceló
        }

        String nombre = txtNombre.getText().trim();
        String system = txtSystem.getText().trim();
        if (nombre.isEmpty() || system.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre y el SYSTEM no pueden estar vacíos.");
            return;
        }

        try {
            boolean creado = Agent.crearAgente(nombre, system);
            if (!creado) {
                JOptionPane.showMessageDialog(this, "Ya existe un agente llamado " + nombre + ".");
                return;
            }
            modeloLista.addElement(nombre);
            lstAgentes.setSelectedValue(nombre, true); // lo seleccionamos para empezar a chatear
        } catch (Exception error) {
            JOptionPane.showMessageDialog(this, "No se pudo crear el agente (" + error + ")");
        }
    }
}
