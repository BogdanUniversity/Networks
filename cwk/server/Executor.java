
import java.net.*;
import java.text.SimpleDateFormat;
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

			// Server side Logging data to console
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
			String outputLine;
			CourseworkProtocol cProtocol = new CourseworkProtocol();
			outputLine = cProtocol.processRequest(userInput,fileString,fileContents);

			out.println(outputLine);

			//If the Server Output is not an error we correctly log the action ona  file
			if (outputLine.startsWith("Error"))
			{
				//Do Nothing
			}else
			{
				logRequest(date, inet.getHostAddress(), userInput);

			}

			

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
        String logFilePath = SERVER_FILES + "/" + "log.txt";

        try (

			FileWriter fileWriter = new FileWriter(logFilePath, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter)) {

            File logFile = new File(logFilePath);

            if (!logFile.exists()) 
			{
                logFile.createNewFile();
            }

			SimpleDateFormat dayDateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy");
        	String formattedDayDate = dayDateFormat.format(date);

			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        	String formattedTime = timeFormat.format(date);

            String logEntry = String.format("%s | %s | %s | %s%n",
                    formattedTime,
                    formattedDayDate,
                    ipAddress,
                    request);

            printWriter.print(logEntry);
			
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

