import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class PeerToPeer 
{
	//"150.243.223.255"
	//public static String[] ipList = {"150.243.196.220","150.243.219.96", "150.243.17.27","150.16.197"};
	
	//Jake Koons 150.243.208.129
	//Jake Bertish 150.243.194.71
	//Garry 150.243.xxx.xxx
	public static String[] ipList = {"150.243.208.129"};
	
  public static void main(String[] args) throws Exception 
  {
	
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
        	
        	byte data[] = "xxxxxxxxxxxxxxx".getBytes();
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
	        	for (int i = 0; i < ipList.length; i++) 
	        	{
	        		InetAddress ipAddress;
					try 
					{
						ipAddress = InetAddress.getByName(ipList[i]);
						
						
						
						if (ipAddress.isReachable(5000)) //5 second timeout 
						{
							
							
							
							
							//System.out.println("good");
							//String upString = "Client " + (i) + " is up.";
				            //byte dataUp[] = upString.getBytes();
							//DatagramPacket sendPacket = new DatagramPacket(dataUp,data.length,ipAddress,9878);
			                //clientSocket.send(sendPacket);
			                
						}
						else //timeout occurred
						{
							System.out.println("bad");
							String downString = "Client " + (i) + " is down.";
				            byte dataBad[] = downString.getBytes();
							DatagramPacket sendPacket = new DatagramPacket(dataBad,data.length,ipAddress,9878);
			                clientSocket.send(sendPacket);
						}
						
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
	        	System.out.println("none left.\n");
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
                	serverSocket = new DatagramSocket(9878);
                
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