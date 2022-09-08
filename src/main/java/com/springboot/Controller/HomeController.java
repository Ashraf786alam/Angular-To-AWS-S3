package com.springboot.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.Service.FileStorageService;

@RestController
@RequestMapping("File")
@CrossOrigin("*")
public class HomeController {
	
	
	@Autowired
	private  FileStorageService  storageService;
	
	@PostMapping("/Upload-File")
	public ResponseEntity<String> UploadFile(@RequestParam("file") MultipartFile file) {
		
		//return ResponseEntity.status(HttpStatus.OK).body(this.storageService.UploadFile(file));
		return new ResponseEntity<>(this.storageService.UploadFile(file),HttpStatus.OK);
	}
	
	@GetMapping("/download/{fileName}")
	public ResponseEntity<ByteArrayResource> DownloadFile(@PathVariable("fileName") String fileName){
		System.out.println("Download Ka Request Aya haiii"+fileName);
	   byte[] data=this.storageService.downloadFile(fileName);
	   ByteArrayResource resource=new ByteArrayResource(data);
	   return ResponseEntity.ok()
			   .contentLength(data.length)
			   .header("Content-type","application/octet-stream")
			   .header("Content-disposition","attachment; filename=\""+ fileName+"\"")
			   .body(resource);
	}
    
	@DeleteMapping("/delete/{fileName}")
	public ResponseEntity<String> deleteFile(@PathVariable("fileName") String fileName){
		System.out.println(fileName);
		
		return new ResponseEntity<>(this.storageService.deleteFile(fileName),HttpStatus.OK);
	}
}
