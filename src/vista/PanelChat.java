package vista;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class PanelChat extends JPanel {

    private JTextArea txtChat;

    public PanelChat() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Conversación"));

        txtChat = new JTextArea();
        txtChat.setEditable(false);
        txtChat.setLineWrap(true);

        add(new JScrollPane(txtChat), BorderLayout.CENTER);
    }
}
