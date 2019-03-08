import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class PeerToPeer 
{
  public static ArrayList<String> ipList = new ArrayList<>(); //output

  public static void main(String[] args) throws Exception 
  {
	//Client List - IP information included 
	ipList.add("150.243.219.96"); 
	//ipList.add("localhost");
	ipList.add("150.243.223.255");
	
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
        	
        	byte data[] = "150.243.196.220".getBytes();
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
						
						/*
						if (ipAddress.isReachable(5000)) //5 second timeout 
						{
							String upString = "Client " + (i) + " is up.";
				            byte dataUp[] = upString.getBytes();
							DatagramPacket sendPacket = new DatagramPacket(dataUp,data.length,ipAddress,9877);
			                clientSocket.send(sendPacket);
			                System.out.println("good");
						}
						else
						{
							System.out.println("bad");
							//timeout will occur
							String downString = "Client " + (i) + " is down.";
				            byte dataBad[] = downString.getBytes();
							DatagramPacket sendPacket = new DatagramPacket(dataBad,data.length,ipAddress,9877);
			                clientSocket.send(sendPacket);
						}
		                */
						
						
						String hostIP = "150.243.196.220";
						byte dataUp[] = hostIP.getBytes();
						DatagramPacket sendPacket = new DatagramPacket(dataUp,data.length,ipAddress,9877); 
						
						
		                Thread.sleep(5000);//give time for receiver to boot 
					} 
					catch (UnknownHostException e) //unable to find IP
					{
						System.out.println("Unknown Host");
					}
					catch (IOException ex) 
	                {
						System.out.println("IOException.");
	                }
					catch (InterruptedException e)
					{
						System.out.println("InterruptedException");
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
                //InetAddress ipAddress = null;
        
                
                try 
                {
                	serverSocket = new DatagramSocket(9877);
                
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