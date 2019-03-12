import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.io.*;
import java.util.Timer;

public class PeerToPeer
{
	public static ArrayList<String> ipList = new ArrayList<String>();

	public static void main(String[] args) throws Exception
	{
		startSender(); //Begin client thread
		startReciever(); //Begin server thread


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
					readConfigFile();
					for (int i = 0; i < ipList.size(); i++)
					{
						InetAddress ipAddress;

						try
						{
							ipAddress = InetAddress.getByName(ipList.get(i));

							String hostIP =  Inet4Address.getLocalHost().getHostAddress();
							byte hostData[] = hostIP.getBytes();
							DatagramPacket sendHostPacket = new DatagramPacket( hostData, hostData.length, ipAddress, 9882);
							clientSocket.send(sendHostPacket);

							for (int j = 0 ; j < ipList.size(); j++)
							{
								if ( !ipList.get(j).toString().equals(ipList.get(i).toString()))
								{
									System.out.println("This host " + hostIP + " sent to " + ipList.get(i).toString() + " The IP Address of " + ipList.get(j).toString());
									byte dataUp[] = ipList.get(j).getBytes();
									DatagramPacket sendPacket = new DatagramPacket( dataUp, dataUp.length, ipAddress, 9882);
									clientSocket.send(sendPacket);
								}
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
				String recentIP = "error";
				try
				{
					serverSocket = new DatagramSocket(9882);
					serverSocket.setSoTimeout(10000);

					DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);

				while (true)
				{
					readConfigFile();
					try
					{
						serverSocket.receive(receivePacket);
						String msg = new String(receivePacket.getData(), receivePacket.getOffset() , receivePacket.getLength());
						if(!ipList.contains(msg) && !msg.equals("127.0.1.1"))
						{
							System.out.println(msg + " has joined the cluster" );
							ipList.add(msg);
							writeConfigFile();
							recentIP = msg;
						}
						else
						{
							System.out.println( msg + " Network node has updated");
							//writeConfigFile();
							recentIP = msg;
						}

					}
					catch (IOException ex)
					{
						//readConfigFile();
						for (int i = 0; i < ipList.size(); i ++)
						{
							if ( ipList.get(i).toString().equals(recentIP))
							{
								System.out.println(ipList.get(i) + " Has been removed from the cluster");
								ipList.remove(i);
								writeConfigFile();
							}
						}
					}
				}
			}
			catch (SocketException e)
			{
				System.out.print(recentIP + "help");
			}
			}
		}).start();
	}
}
