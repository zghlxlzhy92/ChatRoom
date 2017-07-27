package hz44_rx4.Game.controller;

import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


import common.IChatServer;
import common.IChatroom;
import common.ICmd2ModelAdapter;
import common.IUser;
import hz44_rx4.Game.model.ChatAppModel;
import hz44_rx4.Game.model.IModel2ViewAdapter;
import hz44_rx4.Game.model.MiniModel;
import hz44_rx4.Game.model.MiniModel2ViewAdapter;
import hz44_rx4.Game.view.ChatAppGUI;
import hz44_rx4.Game.view.IView2ModelAdapter;
import hz44_rx4.Game.view.MiniView;
import hz44_rx4.Game.view.MiniView2ModelAdapter;
import hz44_rx4.Game.view.ScrollView;

/**
 * @author crespo5609
 * Chat App controller
 */
public class ChatAppController {
      
      private ChatAppGUI<IUser,MiniModel> mainView;
      private ChatAppModel mainModel;
      public ChatAppController(){
    	  mainView = new ChatAppGUI<>(new IView2ModelAdapter<IUser, MiniModel>() {

			@Override
			public void setUsername(String username) {
				mainModel.setName(username);
			}

			@Override
			public void connetToRemoteHost(String ipAddr) {
                  	mainModel.connect(ipAddr);			
			}

			@Override
			public void quitApp() {
                  mainModel.quitAllChatrooms(); 				
			}

			@Override
			public void joinChatRoom(MiniModel chatroom) {
				mainModel.joinChatroom(chatroom, chatroom.getName());
			}

			@Override
			public void inviteUserToChatroom(IUser user, MiniModel chatroom) {
                    mainModel.invite(user, chatroom);				
			}

			@Override
			public void getRoomList(IUser user) {
                    mainModel.getChatRoomList(user);				
			}

			@Override
			public void creatRoom() {
                String chatroomName = mainView.getNewChatroomName();
                if (chatroomName != null) {
                    MiniModel chatroom = mainModel.creatNewChatroom(UUID.randomUUID(), chatroomName);
                    chatroom.addChatServer(chatroom.getChatServer());
                }

			}
		});
    	 mainModel = new ChatAppModel(new IModel2ViewAdapter() {
			
			@Override
			public boolean willJoinChatroom(String user, String charName) {
				return mainView.willJoinChatroom(user, charName);
			}
			
			@Override
			public void setTitle(String sentence) {
                  mainView.setTitle(sentence);				
			}
			
			@Override
			public MiniModel2ViewAdapter makeChatroomViewAdapter(MiniModel miniModel) {
				MiniView2ModelAdapter miniView2ModelAdapter = new MiniView2ModelAdapter() {
					
					@Override
					public void sendMessage(String msg) {
                           	miniModel.sendMessage(msg);					
					}
					
					@Override
					public void sendFile(ImageIcon image) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void exitRoom() {
                          mainModel.quitChatroom(miniModel);						
					}
					@Override
					public void startGame(){
						miniModel.startGame();
					}
				};
				MiniView<IUser> miniView = mainView.makeChatView(miniView2ModelAdapter);
				return new MiniModel2ViewAdapter() {
					
					@Override
					public void start() {
                            miniView.start();						
					}
					
					@Override
					public void setTitle(String name) {
                           	miniView.setTitle(name);				
					}
					
					@Override
					public void sendMessage(String msg) {
                             miniView.append(msg);						
					}
					
					@Override
					public void removeUser(String user) {
                            miniView.removeUser(user);						
					}
					

					
					@Override
					public void addUser(String user) {
                            miniView.addUser(user);						
					}
					
					@Override
					public void addComponent(Component component) {
                        ScrollView view = new ScrollView();
                        view.start();
                        view.getScrollPane().setViewportView(component);
    						
					}
				};
			}
			
			@Override
			public void append(String format) {
                  mainView.append(format);				
			}
			
			@Override
			public void addUser(String name,IUser user) {
                   mainView.addUser(name, user); 				
			}
			
			@Override
			public void addChatroom(MiniModel miniModel) {
                    mainView.addChatroom(miniModel);			
			}
		});
      }

	/**
	 * Start function to start model and view
	 */
	public void start() {
           mainView.start();
           mainModel.start();
	}

	/**
	 * @param args
	 *            The system parameter main method
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ChatAppController controller = new ChatAppController();
					controller.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		});
	}

}
