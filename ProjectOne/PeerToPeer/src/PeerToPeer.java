import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class PeerToPeer 
{
  //public static ArrayList<String> ipList = new ArrayList<>(); //output
  public static ArrayList<String> ipList = new ArrayList<String>();

  public static void main(String[] args) throws Exception 
  {
	//Client List - IP information included 
    //startReciever(); //Begin server thread
    //artSender(); //Begin client thread
    //ipList.add("150.243.xxx.xxx");
    readConfigFile();
    writeConfigFile(ipList);
    
  }


    public static void readConfigFile() 
    {
    try
    {
        FileReader fileReader = new FileReader("config.txt");
        String i = null;    

        BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((i = bufferedReader.readLine()) != null)

            {
                ipList.add(i);
            }   

            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) 
        {
            System.out.println("Unable to open file");                
        }
        catch(IOException ex) 
        {
            System.out.println("Error reading file");                  
        }
    }
    public static void writeConfigFile(ArrayList ipList) 
    {
        try 
        {
            FileWriter fileWriter = new FileWriter("config.txt");

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for( int i = 0; i < ipList.size(); i++)
            {
                bufferedWriter.write(ipList.get(i).toString());
                bufferedWriter.newLine();
            }

            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println("Error writing to file ");
        }

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
	        	for (int i = 0; i < ipList.size() -1; i++) 
	        	{
	        		InetAddress ipAddress;
					try 
					{
						ipAddress = InetAddress.getByName(ipList.get(i));
						
						
						String hostIP =  "150.243.223.255";
						byte dataUp[] = hostIP.getBytes();
						DatagramPacket sendPacket = new DatagramPacket(dataUp,data.length,ipAddress,9880); 
						clientSocket.send(sendPacket);
						
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
                
                try 
                {
                	serverSocket = new DatagramSocket(9880);
                
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
                        ipList.add(msg);

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