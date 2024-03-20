
import java.net.*;
import java.io.*;
import java.util.*;
public class Executor extends Thread{

	private static final String SERVER_FILES = "serverFiles";

    private Socket socket = null;

    public Executor(Socket socket) {
		super("Executor");
		this.socket = socket;
    }
    
    public void run()
    {
        try {

			// Input and output streams to/from the client.
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(
						new InputStreamReader(
						socket.getInputStream()));

			// Logging.
			InetAddress inet = socket.getInetAddress();
			Date date = new Date();
			System.out.println("\nDate " + date.toString() );
			System.out.println("Connection made from " + inet.getHostName() );
			
			// Read userInput from client
            String userInput = in.readLine();
            System.out.println("User Input: " + userInput);

            // Read fileString from client
            String fileString = in.readLine();
            System.out.println("File String: " + fileString);


			// Read file contents only if command is "put"

            StringBuilder fileContentsBuilder = new StringBuilder();
			String fileContents = null;


            if (userInput.equals("put")) {
                String line;
                while ((line = in.readLine()) != null && !line.equals("END_OF_FILE")) {
                    fileContentsBuilder.append(line).append("\n");
                }

				fileContents = fileContentsBuilder.toString().trim();
			}	
            System.out.println("File Contents: \n" + fileContents);


			// Initialise a protocol object for this client.
			String inputLine, outputLine;
			CourseworkProtocol cProtocol = new CourseworkProtocol();
			outputLine = cProtocol.processRequest(userInput,fileString,fileContents);
			out.println(outputLine);

			//Here we construct the Log File after we know that the commands have been run corectly.
			logRequest(date, inet.getHostAddress(), userInput);




			// Sequential protocol. WE NOT USING THIS?
			/* 
			while( (inputLine = in.readLine())!=null ) {
				outputLine = cProtocol.processRequest(inputLine);
				out.println(outputLine);
				if (outputLine.equals("Bye"))
					break;
			}*/


			// Free up resources for this connection.
			out.close();
			in.close();
			socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	private void logRequest(Date date, String ipAddress, String request) 
	{
        String logFilePath = SERVER_FILES + File.separator + "log.txt";

        try (

			FileWriter fileWriter = new FileWriter(logFilePath, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter)) {

            File logFile = new File(logFilePath);

            if (!logFile.exists()) 
			{
                logFile.createNewFile();
            }

            String logEntry = String.format("%s | %s | %s | %s%n",
                    date,
                    date,
                    ipAddress,
                    request);

            printWriter.print(logEntry);
			
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

