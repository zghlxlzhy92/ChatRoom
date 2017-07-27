package hz44_rx4.server.sView;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ServerView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5485064381787244501L;
	private JPanel contentPane;
	private ServerViewModelAdapter serverViewModelAdapter;
	private final JScrollPane scrollPane = new JScrollPane();
	private final JButton btnNewButton = new JButton("Close");
	private final JTextArea textGameInfo = new JTextArea();

	/**
	 * Launch the application.
	 */
    public ServerView(ServerViewModelAdapter serverViewModelAdapter){
    	  this.serverViewModelAdapter = serverViewModelAdapter;
    	  initGUI();
    }
	public void start(){
		this.setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public void initGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		scrollPane.setViewportBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "ServerInformationTable", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		contentPane.add(scrollPane, BorderLayout.CENTER);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			     serverViewModelAdapter.close();
			}
		});
		
		contentPane.add(btnNewButton, BorderLayout.SOUTH);
		scrollPane.setViewportView(textGameInfo);
	}
   public void ShowScore(String score){
	   textGameInfo.append(score + "\n");
   }
}
