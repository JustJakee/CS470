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
		startReciever(); //Begin server thread
		startSender(); //Begin client thread
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
				if(!ipList.contains(i))
				{
					//System.out.println("added " + i + " to the array list");
					ipList.add(i);
				}
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
	public static void writeConfigFile() 
	{
		try 
		{
			FileWriter fileWriter = new FileWriter("config.txt", false);

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
				DatagramSocket clientSocket = null;
				readConfigFile();
				
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
							byte data[] = Inet4Address.getLocalHost().getHostAddress().getBytes();
							String hostIP =  Inet4Address.getLocalHost().getHostAddress();
							
							System.out.println("sent to " + ipList.get(i).toString());
							
							byte dataUp[] = hostIP.getBytes();
							DatagramPacket sendPacket = new DatagramPacket( dataUp, data.length, ipAddress, 9882); 
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
					serverSocket = new DatagramSocket(9882);

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
						System.out.println("Before receive");
						serverSocket.receive(receivePacket);
						System.out.println("After receive");
						String msg = new String(receivePacket.getData(), receivePacket.getOffset() , receivePacket.getLength());
						
						System.out.println("received from " + msg);
						
						if(!ipList.contains(msg))
						{
							ipList.add(msg);
						}

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