import java.net.*;
import java.io.*;

public class Server 
{
	public static void main( String[] args ) throws IOException
	{
		ServerSocket serverSocket = null;
		boolean listening = true;

		try {
			//Do I need to make the port lisening from a range of ports or just pick one?
			serverSocket = new ServerSocket(9100);
			System.out.println("Listening");
        } catch (IOException e) {
            System.err.println("Could not listen on port: 9100.");
            System.exit(-1);
        }

		while (listening)
          //System.out.print('.');
		  // This is the thread manager thingy
	      new Executor(serverSocket.accept()).start();

        serverSocket.close();
		

	}
}



