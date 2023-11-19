package com.trancha;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestUtils{
	
	public static void main(String [] args) throws IOException {
		System.out.println("HELLO TO TEST UTILS");
		
		Path root = Paths.get("uploads/" + 15);// + _person.getId());
  	  	Files.createDirectories(root);
  	  
		 //String uploadsDir = "/uploads/";
         //String realPathtoUploads =  request.getServletContext().getRealPath(uploadsDir);
         //if(! new File(realPathtoUploads).exists())
         //{
        //     new File(realPathtoUploads).mkdir();
         //}

         System.out.println("root :" +  root.toAbsolutePath().toString());
	}
}