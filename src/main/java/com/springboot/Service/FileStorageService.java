package com.springboot.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileStorageService {
	
	@Value("${application.bucket.name}")
	private String bucketName;
	
	@Autowired
	private AmazonS3  s3client;
	
	
	public String UploadFile(MultipartFile file) {
		File file_object=convertMultiPartFileToFile(file);
		String filename=System.currentTimeMillis()+"_"+file.getOriginalFilename();
		s3client.putObject(new PutObjectRequest(bucketName,filename,file_object));
		file_object.delete();
		return "File Uploaded Successfully: "+ filename;
		
	}
	
	private File convertMultiPartFileToFile(MultipartFile file) {
		File convertedFile=new File(file.getOriginalFilename());
		
		try (FileOutputStream fos=new FileOutputStream(convertedFile)){
				fos.write(file.getBytes());
			}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		return convertedFile;
	}
	
	public byte[] downloadFile(String fileName) {
		
		S3Object s3object=s3client.getObject(bucketName,fileName);
		S3ObjectInputStream inputstream=s3object.getObjectContent();
		try {
			byte[] content=IOUtils.toByteArray(inputstream);
			return content;
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public String deleteFile(String fileName) {
		s3client.deleteObject(bucketName, fileName);
		return fileName + ": Deleted from Amazon s3 Successfully..";
	}

}
