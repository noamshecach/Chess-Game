package RMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ServerMain {

	private static int serverPort = 8080; 
	private static String serviceName = "chess" ;
	
	public static void main(String[] args) {
		
		try {
			// RMI workflow
			// 1) Defining interface of the server API - IServerService.
			
			// 2) Implementing the server API functions - ServerService
			
			// 3) Create a remote object registry that accepts calls on a specific port
			Registry registry = getRegistry(serverPort);
			
			// 4) Bind the class (ServerService) to an unique name in the registry instance.
			new ServerService(registry, serviceName);
			
			System.out.println("Server is running, type 'exit' to stop it.");
			
			// 5) Client implementation - ContactServer
			
			// this server will run indefinitely until "exit" is specified 
			try(Scanner scanner = new Scanner(System.in)){
				while(true) {
					String command = scanner.nextLine();
					if(command == null || "exit".equalsIgnoreCase(command))
						break;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	// Create registry object that listens on the specified port.
	public static  Registry getRegistry(int port) throws RemoteException {
		try {
			return LocateRegistry.createRegistry(port);
		} catch (RemoteException e) {
			return LocateRegistry.getRegistry(port);
		}
	}

}