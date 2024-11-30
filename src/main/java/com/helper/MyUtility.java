package com.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.fileupload.FileItem;

public class MyUtility {
    
	public String uploadFile(String name, FileItem item, String destinationPath, List<String> validExtensions, String oldFilename) throws Exception {
	    String newFilename = "";

	    // Ensure the item is a file (not a form field)
	    if (!item.isFormField()) {
	        System.out.println("MyUtility function called");

	        // Retrieve extension from the file name
	        String fileName = item.getName();
	        String fileExtension = fileName.substring(fileName.lastIndexOf('.'));

	        // Validate the file extension and size (less than or equal to 10 MB)
	        if (validExtensions.contains(fileExtension) && (item.getSize() / (1024 * 1024)) <= 10) {
	            // Create a new file name using the user's name and the file extension
	            newFilename = name + fileExtension;

	            System.out.println("New file name: " + newFilename);

	            // If the old file is not the default "user.png", delete it
	            if (!"user.png".equals(oldFilename)) {
	                File oldFile = new File(destinationPath + oldFilename);
	                if (oldFile.exists()) {
	                    Files.delete(Paths.get(destinationPath + oldFilename));
	                    System.out.println("Old file deleted: " + oldFilename);
	                }
	            }

	            // Create the new file and write it to the destination path
	            File newFile = new File(destinationPath + newFilename);
	            item.write(newFile);
	            System.out.println("File uploaded: " + newFilename);
	        } else {
	            // If the file does not meet the validation criteria
	            throw new Exception("Invalid file type or file size exceeds the limit.");
	        }
	    }

	    return newFilename; // Return the new file name after upload
	}
}
