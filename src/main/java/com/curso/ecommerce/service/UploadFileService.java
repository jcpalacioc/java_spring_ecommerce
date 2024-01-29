package com.curso.ecommerce.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class UploadFileService {
	private String folder="images/";
	private final Path root=Paths.get("images");
	
	private final S3Client s3Client;
		
	@Autowired
	public UploadFileService(S3Client s3Client) {
		this.s3Client=s3Client;
	}
	
	@Value("${s3.localPath}")
	private String localPath;
	
	public String save_image(MultipartFile file) throws IOException {
		if(!file.isEmpty()) {
			byte[] bytes=file.getBytes();
			PutObjectRequest object_request=PutObjectRequest.builder()
					.bucket("elasticbeanstalk-us-east-1-891377088755")
					.key("images/"+file.getOriginalFilename())
					.build();
			
			s3Client.putObject(object_request, RequestBody.fromBytes(bytes));
			
			return file.getOriginalFilename();
			
			
		}else {
			File filesource=new File("/opt/default.jpg");
			File fileDest=new File("/default.jpg");
			InputStream in=new FileInputStream(fileDest);
			Path path=Paths.get(folder+fileDest);
			Files.write(path, in.readAllBytes());
		}
		return "default.jpg";
	}
	
	public String downloadFile(String fileName) throws IOException {
		
		if(!doesObjectExists(fileName)) {
			return "El archivo no existe";
		}
		GetObjectRequest request=GetObjectRequest.builder()
				.bucket("elasticbeanstalk-us-east-1-891377088755")
				.key("images/"+fileName)
				.build();
		ResponseInputStream<GetObjectResponse> result=s3Client.getObject(request);
		try (FileOutputStream fos=new FileOutputStream(localPath+fileName)){
			byte[] read_buf=new byte[1024];
			int read_len=0;
			while((read_len=result.read(read_buf))>0) {
				fos.write(read_buf,0,read_len);
			}
		}catch(IOException ex) {
			throw new IOException(ex.getMessage());
		}
		return "Archivo descargado correctamente";
	}
	
	private boolean doesObjectExists(String objectKey) {
		try {
			HeadObjectRequest headObjectRequest=HeadObjectRequest.builder()
					.bucket("elasticbeanstalk-us-east-1-891377088755")
					.key("images/"+objectKey)
					.build();
			return true;
		}catch(S3Exception ex) {
			if(ex.statusCode()==404) {
				return false;
			}
		}
		return false;
	}
	
	public void deleteImage(String nombre) {
		String ruta="images//";
		File file=new File(ruta+nombre);
		file.delete();
	}
}
