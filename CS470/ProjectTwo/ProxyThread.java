import java.io.*;
import java.net.*;

//Thread created by driver class (ProxyServer)
//Relays information from server to client
//Manages caching

public class ProxyThread extends Thread 
{
	private Socket client;
	private BufferedReader inputClientBr;
	private BufferedWriter outputClientBw;

//Constructor to create socket
//Starts ProxyThread

	ProxyThread(Socket client) 
	{
		this.client = client;
		this.start();
	}

//Method that is executed when ProxyThread is started
//Sever to Client implementation

	public void run() 
	{	
		try {

			inputClientBr = new BufferedReader(new InputStreamReader(client.getInputStream()));
			outputClientBw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

			String requestTotal = inputClientBr.readLine();

			String requestMethod = requestTotal.substring(0, requestTotal.indexOf(' '));
			System.out.println(requestMethod);

			String requestURL = requestTotal.substring(requestTotal.indexOf(' ') + 1);
			requestURL = requestURL.substring(0, requestURL.indexOf(' '));

			if (requestMethod.equals("CONNECT") || requestMethod.equals("GET") )
			{	
				String socketSplit[] = requestURL.split(":");
				int port = Integer.valueOf(socketSplit[1]);
				System.out.println(port);
				System.out.println(socketSplit[0]);
				

				//get rid of the header
				for(int i =0; i < 5; i++) 
				{
					inputClientBr.readLine();
				}

				InetAddress address = InetAddress.getByName(socketSplit[0]);
				Socket proxyToServer = new Socket(address, port);

				//Connection established
				String connected = "HTTP/1.0 200 OK\r\n" + "Proxy-Agent: ProxyServer/1.0\r\n" + "\r\n";
				outputClientBw.write(connected);
				outputClientBw.flush();

				clientToServerClass psc = new clientToServerClass( client.getInputStream(), proxyToServer.getOutputStream());
				psc.start();

				byte[] bytes = new byte[8192];

				int bufferSize;

				while ((bufferSize = proxyToServer.getInputStream().read(bytes)) > 0 ) 
				{

					//System.out.println("Buffer Size: " + bufferSize);
					cacheFile(socketSplit[0], bytes);
					client.getOutputStream().write( bytes, 0, bufferSize);
				}

				client.close();
				proxyToServer.close();
			}
			else
			{
				//Send error message to client 
				String message = "HTTP/1.0 501 Not Implemented\r\n" + "Proxy-Agent: ProxyServer/1.0\r\n" + "\r\n";
				outputClientBw.write(message);
				outputClientBw.flush();
			}

		}
		catch (Exception e) 
		{
			//Print error message for client
			System.out.println("HTTP/1.0 400 Bad Request\r\n" + "Proxy-Agent: ProxyServer/1.0\r\n" + "\r\n");
		}
	}

//Thread that relays information from client to server

	public class clientToServerClass extends Thread 
	{

		private InputStream is;
		private OutputStream os;

		clientToServerClass(InputStream is, OutputStream os) 
		{
			this.is = is;
			this.os = os;

		}

	//Method that is executed when ProxyThread is started
	//Client to Server implementation

		public void run() {
			
			try {
				byte[] bytes2 = new byte[8192];
				
				
				int readTotalBytes;

				while((readTotalBytes = is.read(bytes2)) > 0 ) 
				{
					os.write(bytes2, 0, readTotalBytes);
					//System.out.println(new String(bytes2));
				}
			}
			catch (Exception e )
			{
				e.printStackTrace();
			}
		}

	}


//Method to create cache files and folder to contain files 
//Passes URL to create file name and byte content to populate cache files	
	public void cacheFile(String urlString, byte[] content)
	{
		try
		{

			int indexOfFileExtention = urlString.lastIndexOf('.');

			String fileExtention = urlString.substring(indexOfFileExtention, urlString.length());

			String fileName = urlString.substring(0, indexOfFileExtention);

			fileName = fileName.substring(fileName.indexOf('.') + 1);

			fileName = fileName.replace("/", "__");
			fileName = fileName.replace(".", "__");

			if(fileExtention.contains("/"))
			{
				fileExtention = fileExtention.replace("/", "__");
				fileExtention = fileExtention.replace(".", "__");
				fileExtention += ".html";
			}

			fileName = fileName + fileExtention;
				
			File fileToCache = new File("cacheFileFolder/" + fileName);
				
				if(!fileToCache.exists())
				{
					System.out.println(fileName);
					System.out.println(new String(content));
					fileToCache.createNewFile();
					FileOutputStream stream = new FileOutputStream(fileToCache);
					stream.write(content);
					stream.close();
				}

		}
		catch(Exception e)
		{
			System.out.println("Unable to Cache File");
		}
	}
}
