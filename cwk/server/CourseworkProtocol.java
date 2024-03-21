import java.net.*;
import java.io.*;
public class CourseworkProtocol 
{   
    private static final String SERVER_FILES = "serverFiles";
    
    public String processRequest(String theInput, String FileName, String fileContents)
    {   
        // Wrong User Input
        String theOutput = "NO COMMAND LINE INPUT";

        if (theInput.equals("list"))
        {
            theOutput = listServerFiles(true);

        }else if(theInput.equals("put"))
        {   
            String FileExists = isFileOnServer(FileName);
            //System.out.println("FileExists");
            //System.out.println(FileExists);
            if (FileExists == "yes")
            {   
               theOutput = "Error 2";
               return theOutput;

            }else
            {
               theOutput = uploadFile(FileName, fileContents); 
            }
            
        }else
        {   
            theOutput = "Error 1";
            return theOutput;
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
    private String listServerFiles(boolean Head) {
        File folder = new File(SERVER_FILES);
        File[] files = folder.listFiles();
    
        StringBuilder fileList = new StringBuilder();
        if (files != null) 
        {
            int fileCount = 0;

            for (File file : files) 
            {
                if (file.isFile()) 
                {
                    fileList.append(file.getName()).append("\n");
                    fileCount++;
                }
            }
            // If we want the head that lists the number of files in the string
            if (Head == true)
            {
                fileList.insert(0, "Listing " + fileCount + " file(s):\n");
            }
            
        }
    
        return fileList.toString();
    }

   // Function that takes all the file names from server and compares if the 
   // file name is trying to be uploaded aswell
    private String isFileOnServer(String FileName) 
    {   
        String Outcome = "no"; 
        String filelist = listServerFiles(false);
        
        // Split the file list string into individual file names
        // Check if the FileName exists in the filelist
        if (filelist.contains(FileName)) 
        {
            Outcome = "yes"; // Update outcome if file is found
        }
        
        return Outcome;
        
    }
}
