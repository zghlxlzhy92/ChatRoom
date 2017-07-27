package hz44_rx4.Game.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;



public class ChatAppGUI<TIUser, TChatroom> extends JFrame {
	
     /**
	 * 
	 */
	private static final long serialVersionUID = 8890303214554247257L;

	/**
     * The generated serial version UID
     */
    
    /**
     * The view to model adapter
     */
    private IView2ModelAdapter<TIUser, TChatroom> modelAdapter;
    
    /**
     * The main panel
     */
    private JPanel contentPane;
    
    /**
     * The text area for showing infomation
     */
    private final JTextArea txtrInfo = new JTextArea();
    
    /**
     * The panel containing panelUP and panelDown
     */
    private final JPanel panelCtrl = new JPanel();
    
    /**
     * The upper panel
     */
    private final JPanel panelUp = new JPanel();
    
    /**
     * The new teammate label
     */
    private final JLabel lblNewUser = new JLabel("New User:");
    
    /**
     * The text field for inputing IP address
     */
    private final JTextField txtIp = new JTextField();
    
    /**
     * The connect button
     */
    private final JButton btnConnect = new JButton("Connect");
    
    /**
     * The scroll panel
     */
    private final JScrollPane scrollPaneInfo = new JScrollPane();
    
    /**
     * The lower panel
     */
    private final JPanel panelDown = new JPanel();
    
    /**
     * The invite button
     */
    private final JButton btnInvite = new JButton("Invite");
    
    /**
     * The teammate label
     */
    private final JLabel lblUser = new JLabel("User:");
    
    /**
     * The combo box for selecing user
     */
    private final JComboBox<String> cbxUsers = new JComboBox<>();
    
    /**
     * The chatroom label
     */
    private final JLabel lblChatroom = new JLabel("Chatroom:");
    
    /**
     * The combo box for selecting a chatroom
     */
    private final JComboBox<TChatroom> cbxChatrooms = new JComboBox<>();
    
    /**
     * The button for creating a new chat room
     */
    private final JButton btnNewChatroom = new JButton("New Chatroom");
    
    /**
     * The button for getting chat room list
     */
    private final JButton btnGetChatroomList = new JButton("Get Chatroom List");
    private final JButton joinButton = new JButton("Join");

    /**
     * Create the frame.
     * @param modelAdapter The model adapter
     */
     private HashMap<String, TIUser> users = new HashMap<>();
     public ChatAppGUI(IView2ModelAdapter<TIUser, TChatroom> iView2ModelAdapter){
    	  this.modelAdapter = iView2ModelAdapter;
    	  initGUI();
     }
    
    /**
     * Initialize GUI
     */
    private void initGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        
        contentPane.add(panelCtrl, BorderLayout.NORTH);
        panelCtrl.setLayout(new GridLayout(2, 1, 0, 0));
        panelCtrl.add(panelUp);
        
        panelUp.add(lblNewUser);
        
        txtIp.setColumns(10);
        panelUp.add(txtIp);
        btnConnect.addActionListener(e -> {
            String ip = txtIp.getText().trim();
            if (!ip.isEmpty()) {
            	modelAdapter.connetToRemoteHost(ip);
            }
        });
        
        panelUp.add(btnConnect);
        btnNewChatroom.addActionListener(e -> {
            modelAdapter.creatRoom();;
//			String chatRoomName = JOptionPane.showInputDialog(null, "Please input the chat room name:",
//					"Chat Room Name", JOptionPane.INFORMATION_MESSAGE);
//			if (chatRoomName.isEmpty() || chatRoomName == null) {
//				chatRoomName = "Anonymous";
//			}
//			modelAdapter.creatRoom(chatRoomName);

        });
        
        panelUp.add(btnNewChatroom);
        
        panelCtrl.add(panelDown);
        btnInvite.addActionListener(e -> {
            if (cbxUsers.getSelectedIndex() == -1 || cbxChatrooms.getSelectedIndex() == -1) {
                return;
            }
            
            TIUser user = users.get(cbxUsers.getItemAt(cbxUsers.getSelectedIndex()));
            TChatroom chatroom = cbxChatrooms.getItemAt(cbxChatrooms.getSelectedIndex());
            modelAdapter.inviteUserToChatroom(user, chatroom);
        });
        
        panelDown.add(lblUser);
        
        panelDown.add(cbxUsers);
        btnGetChatroomList.addActionListener(e -> {
            if (cbxUsers.getSelectedIndex() == -1) {
                return;
            }
            
            modelAdapter.getRoomList(users.get(cbxUsers.getItemAt(cbxUsers.getSelectedIndex())));
        });
        
        panelDown.add(btnGetChatroomList);
        
        panelDown.add(lblChatroom);
        
        panelDown.add(cbxChatrooms);
        
        panelDown.add(btnInvite);
        joinButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                if (cbxUsers.getSelectedIndex() == -1 || cbxChatrooms.getSelectedIndex() == -1) {
                    return;
                }
                
                TIUser user = users.get(cbxUsers.getItemAt(cbxUsers.getSelectedIndex()));
                TChatroom chatroom = cbxChatrooms.getItemAt(cbxChatrooms.getSelectedIndex());
        		modelAdapter.joinChatRoom(chatroom);
        	}
        });
        
        panelDown.add(joinButton);
        
        contentPane.add(scrollPaneInfo, BorderLayout.CENTER);
        scrollPaneInfo.setViewportView(txtrInfo);
        
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                modelAdapter.quitApp();
            }
        });
    }
    
    /**
     * Start GUI
     */
    public void start() {
        String username = JOptionPane.showInputDialog(null, "Please input your username:", "Login", JOptionPane.INFORMATION_MESSAGE);
        if (username == null) {
            System.exit(0);
        } else if (username.trim().isEmpty()) {
            username = "COMP 504 user";
        }
        
        setVisible(true);
        modelAdapter.setUsername(username);
    }
    
    /**
     * Append a message
     * @param str The message
     */
    public void append(String str) {
        txtrInfo.append(str);
        txtrInfo.setCaretPosition(txtrInfo.getText().length());
    }
    
    /**
     * Get a new chat room name
     * @return The new chat room name
     */
    public String getNewChatroomName() {
        String chatroomName = null;
        do {
            chatroomName = JOptionPane.showInputDialog(null, "Please input new chatroom's name:", "Chatroom Name", JOptionPane.INFORMATION_MESSAGE);
            if (chatroomName == null) {
                return null;
            }
            
            chatroomName = chatroomName.trim();                
        } while (chatroomName.isEmpty());
        
        return chatroomName;
    }
    
    public MiniView<TIUser> makeChatView(MiniView2ModelAdapter miniView2ModelAdapter) {
        return new MiniView<TIUser>(miniView2ModelAdapter);
    }
    
    /**
     * Ask a user if he will join this chat room
     * @param username The user name of the user who sends the invitation
     * @param chatroom The chat room
     * @return True if accepting invitation, false otherwise
     */
    public boolean willJoinChatroom(String username, String chatroom) {
        String message = String.format("Will you join the chatroom %s invited by %s", chatroom, username);
        int result = JOptionPane.showConfirmDialog(null, message, "Invitation", JOptionPane.OK_CANCEL_OPTION);
        
        return result == JOptionPane.OK_OPTION;
    }
    
    /**
     * Add a user
     * @param user The user to add
     */
    public void addUser(String name,TIUser user) {
    	users.put(name, user);
        cbxUsers.insertItemAt(name, 0);
        cbxUsers.setSelectedIndex(0);
    }
    
    /**
     * Add a chat room
     * @param chatroom The chat room to add
     */
    public void addChatroom(TChatroom chatroom) {
        cbxChatrooms.insertItemAt(chatroom, 0);
        cbxChatrooms.setSelectedIndex(0);
    }
    
    /**
     * Choose a chat room
     * @param username The user who owns the chat room list
     * @param chatroomNames The chat room list
     * @return The chat room's name
     */
    public String chooseChatroom(String username, String[] chatroomNames) {
        if (chatroomNames.length == 0) {
            append("Chatroom List is empty\n");
            return null;
        }
        
        return (String)JOptionPane.showInputDialog(null, 
                                                   "Which chatroom to join?",
                                                   String.format("%s Chatroom List", username),
                                                   JOptionPane.QUESTION_MESSAGE,
                                                   null,
                                                   chatroomNames,
                                                   chatroomNames[0]);
    }
    
    /**
     * Remove a chat room
     * @param chatroom The chat room to remove
     */
    public void removeChatroom(TChatroom chatroom) {
        cbxChatrooms.removeItem(chatroom);
    }
    
    /**
     * Send a warning message
     * @param message The warning message
     */
    public void warn(String message) {
        JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }

}
