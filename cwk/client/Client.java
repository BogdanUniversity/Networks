import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private Socket cSocket = null;
    private PrintWriter socketOutput = null;
    private BufferedReader socketInput = null;

    public void serverConnect(String userInput, String fileString) {

        try {

            cSocket = new Socket("localhost", 9100);

            socketOutput = new PrintWriter(cSocket.getOutputStream(), true);

            socketInput = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));

            socketOutput.println(userInput);
            socketOutput.println(fileString);
            
            //Perform Actions Based on Command Requirements

            if (userInput.equals("put")) {
                sendFileContents(fileString);
            }


            // Read from server.
            String fromServer;
            while ((fromServer = socketInput.readLine()) != null) {
                // Echo server string.
                if (fromServer.equals("Error 1"))
                {
                    System.out.println("Incorrect command line argument - Has to be either 'put' or 'list' ");
                }else if(fromServer.equals("Error 2"))
                {
                    System.out.println("Cannot Upload File - File already exists on Server");

                }else
                {
                    System.out.println(fromServer);
                }
                
            }

            socketOutput.close();
            socketInput.close();
            cSocket.close();

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host.\n");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to host.\n");
            System.exit(1);
        } catch (RuntimeException e)
        {
            System.err.println(e);
            System.exit(1);
        }
    }

    //Function that reads a new text line from the file and sends it over
    private void sendFileContents(String fileName) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                socketOutput.println(line); // Send each line of the file to the server
            }
            // Signal end of file
            socketOutput.println("END_OF_FILE");

        } catch (IOException e) {
            System.err.println("Error: Cannot open file " + e.getMessage());
            System.exit(1);
        }
    }

    public static void main(String[] args) 
    {   
        //Checks if number of arguments is correct
        if (args.length < 1) 
        {
            System.err.println("Not Enough Arguments - Usage: java Client <userInput>");
            System.exit(1);
        }

        //Sets variable contents from Command Line Arguments
        String userInput = args[0];
        String fileString = null;

        // If we have 2 args we know we using put therefore we assign the filename to fileString
        if (args.length == 2){
         
            fileString = args[1];
            if (fileString == null)
            {
                System.err.println(" Error: No file name given to move" );
                System.exit(1);
            }
        }

        //We create a new client
        Client courseworkClient = new Client();
        courseworkClient.serverConnect(userInput,fileString);
    }
}
