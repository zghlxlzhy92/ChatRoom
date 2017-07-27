package hz44_rx4.Game.model;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import common.IChatServer;
import common.IUser;
import provided.rmiUtils.IRMIUtils;
import provided.rmiUtils.IRMI_Defs;
import provided.rmiUtils.RMIUtils;
import provided.util.IVoidLambda;

public class Registry {
    /**
     * RMI utility to get Registry. 
     */
    private IRMIUtils rmiUtils;

    /**
     * Constructs a bootstrap.
     * @param outputCmd The outputCmd
     */
    public Registry(IVoidLambda<String> outputCmd) {
        rmiUtils = new RMIUtils(outputCmd);
    }

    /**
     * Returns the IP of this computer. 
     * @return the IP of this computer. 
     */
    public String getIP() {
        return System.getProperty("java.rmi.server.hostname");
    }

    /**
     * Returns the initial stub of remote me. 
     * @param me - the remote to local adapter. 
     * @return the initial stub of remote me. 
     */
    public IUser register(IUser me) {
        try {
            IUser stub = (IUser)UnicastRemoteObject.exportObject(me, IUser.BOUND_PORT_SERVER);
            rmiUtils.startRMI(IRMI_Defs.CLASS_SERVER_PORT_SERVER);
            rmiUtils.getLocalRegistry().rebind(IUser.BOUND_NAME, stub);
            System.out.println("Created remote me. ");
            return stub;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns the chatroom stub of remote me. 
     * @param me - the remote to chatroom adapter. 
     * @return the chatroom stub of remote me. 
     */
    public IChatServer register(IChatServer me) {
        try {
            IChatServer stub = (IChatServer) UnicastRemoteObject.exportObject(me, IChatServer.BOUND_PORT_SERVER);
            System.out.println("Created remote me in chatroom. ");
            return stub;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns the remote friend. 
     * @param userIP - friend's IP address. 
     * @return the remote friend. 
     */
    public IUser connectToUser(String userIP) {
			try {
	            IUser stub = (IUser) rmiUtils.getRemoteRegistry(userIP).lookup(IUser.BOUND_NAME);
	            System.out.println("Found remote friend. ");
	            return stub;

			} catch (AccessException e) {
				e.printStackTrace();
				return null;
			} catch (RemoteException e) {
				e.printStackTrace();
				return null;
			} catch (NotBoundException e) {
				e.printStackTrace();
				return null;
			}

    }

}
