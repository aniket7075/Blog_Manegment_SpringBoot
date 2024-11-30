package com.helper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.commons.fileupload.FileItem;

public class UploadPostPic {
    
    /**
     * Uploads a file, validates its extension and size, and saves it to the destination path.
     * 
     * @param name           The name of the file (typically user or post name)
     * @param item           The FileItem representing the uploaded file
     * @param destinationPath The directory path where the file will be saved
     * @param validExtensions The list of valid file extensions (e.g., .jpg, .png, etc.)
     * @return               The new filename if the file is uploaded successfully, otherwise an empty string
     * @throws Exception     If an error occurs during file upload
     */
    public String uploadFile(String name, FileItem item, String destinationPath, ArrayList<String> validExtensions) throws Exception {
        String newFilename = "";
        
        // Ensure the item is a file (not a form field)
        if (!item.isFormField()) {
            System.out.println("File upload function called");
            
            // Retrieve extension from the file name
            String fileName = item.getName();
            String fileExtension = fileName.substring(fileName.lastIndexOf('.'));
            
            // Validate file extension and size (file size should not exceed 10MB)
            if (validExtensions.contains(fileExtension) && (item.getSize() / (1024 * 1024)) <= 10) {
                // Create a new filename by appending the file extension to the provided name
                newFilename = name + fileExtension;
                
                System.out.println("New file name: " + newFilename);
                
                // Create a new file object in the destination path
                File file = new File(destinationPath + newFilename);
                
                // Ensure the destination directory exists
                File directory = new File(destinationPath);
                if (!directory.exists()) {
                    directory.mkdirs();  // Create the directory if it doesn't exist
                }
                
               
                item.write(file);
                System.out.println("File uploaded successfully: " + newFilename);
            } else {
                throw new Exception("Invalid file type or file size exceeds the limit (10MB).");
            }
        }

        return newFilename; 
    }
}
