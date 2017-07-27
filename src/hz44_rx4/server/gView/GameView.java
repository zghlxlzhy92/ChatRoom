package hz44_rx4.server.gView;

import java.awt.BorderLayout;
import java.awt.dnd.DragGestureEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.omg.CORBA.PUBLIC_MEMBER;

import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.util.BasicDragger;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class GameView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4018869827036659748L;
	private JPanel contentPane;
    private GameViewModelAdpater gameViewModelAdpater;
    private Map worldWindGanvas;
    private final JPanel panel_2 = new JPanel();
    private final JLabel lblNewLabel = new JLabel("Answer:");
    private final JTextField answerTxT = new JTextField();
    private final JButton goButton = new JButton("Go");
    private final JButton startGameButton = new JButton("StartGame");
    private final JButton relocateButton = new JButton("Relocate");
    private Position curPos;
	private final JTextArea instruction = new JTextArea();
	private final JScrollPane infoPane = new JScrollPane();
    private int leftTime  = 600;
    private final JLabel timeLabel = new JLabel("Time : 600");
    public GameView(GameViewModelAdpater gameViewModelAdpater){
		relocateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				   goToPlace(curPos);
			}

		});
    	 answerTxT.setColumns(10);
    	 this.gameViewModelAdpater = gameViewModelAdpater;
    	 worldWindGanvas.getCanvas().addSelectListener(new SelectListener() {
			private BasicDragger dragger = new BasicDragger(worldWindGanvas.getCanvas());
			@Override
			public void selected(SelectEvent event) {
                  	this.dragger.selected(event);		
			}
		});
    	 initGUI();
    }
	
   
	public void start(){
		worldWindGanvas.start();
		 setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public void initGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		contentPane.add(panel_2, BorderLayout.SOUTH);
		startGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				  gameViewModelAdpater.StartGame();
			}
		});
		instruction.setLineWrap(true);
		instruction.setTabSize(20);
		infoPane.setViewportView(instruction);
		instruction.append("Instruction" + "\n");
		
		panel_2.add(timeLabel);
		
		panel_2.add(startGameButton);
		
		panel_2.add(lblNewLabel);
		
		panel_2.add(answerTxT);
		goButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String answer = answerTxT.getText();
			    String information = gameViewModelAdpater.goToPlace(answer);
			    postInfo(information);
			    answerTxT.setText("");
			}
		});
		
		panel_2.add(goButton);

		
		panel_2.add(relocateButton);

		
		contentPane.add(infoPane, BorderLayout.WEST);
	}
	public void goToPlace(Position position){
		 curPos = position;
		 worldWindGanvas.setPosition(curPos, true);
	}
	public void update(){
		timeLabel.setText("Time :" + leftTime);
		//gameViewModelAdpater.update();
		if(leftTime == 0){
			 gameViewModelAdpater.timeOut();
		}
		leftTime--;
	}
	public void addLayer(Layer layer){
		worldWindGanvas.addLayer(layer);
	}
	public void disabledBton(){
		startGameButton.setEnabled(false);
		goButton.setEnabled(false);
		relocateButton.setEnabled(false);
	}
	public void postInfo(String info){
		instruction.append("New message :" + info + "\n");
	}
	public void showRank(String rank){
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				JOptionPane.showMessageDialog(GameView.this, rank);
			}
			
		});

	}

}
