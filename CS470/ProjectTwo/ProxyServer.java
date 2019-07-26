import java.io.*;
import java.net.*;

//Driver class to run threads

public class ProxyServer 
{

	public static void main(String[] args) 
	{
		
		try {
			
			ServerSocket server = new ServerSocket(8086);
			
			while(true) 
			{

				//Clients connect to the proxy server(this machine) through port 8086	
				ProxyThread a = new ProxyThread(server.accept());
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		
		}
	}
}