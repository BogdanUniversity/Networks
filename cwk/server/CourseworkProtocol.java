import java.net.*;
import java.io.*;
public class CourseworkProtocol 
{   
    private static final int WAITING = 0;
    private static final int COPYING = 1;
    private static final String SERVER_FILES = "serverFiles";

    //Define Private Variables that will be used by the Server
    // From Lec 10
    private int state = WAITING;
    
    
    public String processRequest(String theInput, String FileName, String fileContents)
    {   
        // Wrong User Input
        String theOutput = "NO COMMAND LINE INPUT";

        /*if (state == WAITING)
        {

            theOutput = "Waiting";

            if (theInput != null)
                    {

                        if( theInput.equals("1"))
                        {
                                
                            theOutput = "User Wants to View Files on Server";

                        }
                    }
        }*/

        
        if (theInput.equals("list"))
        {

            theOutput = listServerFiles();
            // I need to gather the names of files in the serverFiles folder
            // And then print them out to the client


        }else if(theInput.equals("put"))
        {

            
            theOutput = uploadFile(FileName, fileContents);


        }

        return theOutput;
    }

    // Takes in the FileContents and FileName and constructs the new file on server folder
    private String uploadFile(String fileName, String fileContents) 
    {
        String filePath = SERVER_FILES + File.separator + fileName;

        try (PrintWriter writer = new PrintWriter(filePath)) 
        {
            writer.print(fileContents);
            return "Uploaded file " + fileName + " successfully.";

        }catch (IOException e) {
            e.printStackTrace();
            return "Error uploading file " + fileName;
        }
    }

    // Loops through the ServerFiles directory looking for files, and adds their name to a list.
    private String listServerFiles() {
        File folder = new File(SERVER_FILES);
        File[] files = folder.listFiles();
    
        StringBuilder fileList = new StringBuilder();
        if (files != null) 
        {
            int fileCount = 0;

            for (File file : files) {
                if (file.isFile()) {
                    fileList.append(file.getName()).append("\n");
                    fileCount++;
                }
            }
            // Append the file count information
            fileList.insert(0, "Listing " + fileCount + " file(s):\n");
        }
    
        return fileList.toString();
    }

   
}
