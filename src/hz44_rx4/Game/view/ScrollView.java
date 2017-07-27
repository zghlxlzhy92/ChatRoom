package hz44_rx4.Game.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class ScrollView extends JFrame {

    /**
     * The generated serial version UID
     */
    private static final long serialVersionUID = -6764256922193445228L;
    
    /**
     * The content panel
     */
    private JPanel contentPane;
    
    /**
     * The scroll panel
     */
    private final JScrollPane scrollPane = new JScrollPane();

    /**
     * Create the frame.
     */
    public ScrollView() {
        initGUI();
    }
    
    /**
     * Initialize GUI
     */
    private void initGUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        
        contentPane.add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Start the view
     */
    public void start() {
        setVisible(true);
    }
    
    /**
     * Get the scroll panel
     * @return The scroll panel
     */
    public JScrollPane getScrollPane() {
        return scrollPane;
    }


}
