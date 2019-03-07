import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class PeerToPeer 
{
  public static ArrayList<String> ipList = new ArrayList<>(); //output

  public static void main(String[] args) throws Exception 
  {
	//Client List - IP information included 
	ipList.add("localhost"); 
	ipList.add("0");
	ipList.add("localhost");
	ipList.add("localhost"); 
	ipList.add("localhost"); 
	ipList.add("0"); 
	ipList.add("localhost");
	ipList.add("localhost");
	
    startReciever(); //Begin server thread
    startSender(); //Begin client thread
  }

  public static void startSender() throws UnknownHostException
  {
    (new Thread() 
    {
        @SuppressWarnings("resource")
		@Override
        public void run() 
        {
        	
        	byte data[] = "Client x is up.".getBytes();
            DatagramSocket clientSocket = null;
            
            try 
            {
                clientSocket = new DatagramSocket();
                clientSocket.setBroadcast(true);
            } 
            catch (SocketException ex) 
            {
                ex.printStackTrace();
            }
            
            while (true)
            {
	        	for (int i = 0; i < ipList.size(); i++) 
	        	{
	        		InetAddress ipAddress;
					try 
					{
						ipAddress = InetAddress.getByName(ipList.get(i));
						
						if (ipAddress.isReachable(5000)) //5 second timeout 
						{
							String upString = "Client " + (i + 1) + " is up.";
				            byte dataUp[] = upString.getBytes();
							DatagramPacket sendPacket = new DatagramPacket(dataUp,data.length,ipAddress,123);
			                clientSocket.send(sendPacket);
						}
						else
						{
							//timeout will occur
						}
		                
		                Thread.sleep(5000);//give time for receiver to boot 
					} 
					catch (UnknownHostException e) //unable to find IP
					{
						System.out.println("Client " + (i + 1) + " is down.");
					}
					catch (IOException ex) 
	                {
						System.out.println("Client " + (i + 1) + " is down.");
	                }
					catch (InterruptedException e)
					{
						System.out.println("Client " + (i + 1) + " is down.");
					}
	        	}
	        	System.out.println("Client list exhausted.\n");
            }
        }
       }).start();
    }

  public static void startReciever() 
  {
    (new Thread() 
    {
        @SuppressWarnings("resource")
		@Override
        public void run() 
        {
                DatagramSocket serverSocket = null;
                
                try 
                {
                	serverSocket = new DatagramSocket(123);
                } 
                catch (SocketException ex) 
                {
                    ex.printStackTrace();
                }
                
                DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
                
                while (true) 
                {
                	try 
                	{
                		serverSocket.receive(receivePacket);
	                    String msg = new String(receivePacket.getData());
	                    
	                    System.out.println(msg);
                	} 
                	catch (IOException ex) 
                	{
	                    ex.printStackTrace();
                	}
                }
            }
    }).start();
 }
}