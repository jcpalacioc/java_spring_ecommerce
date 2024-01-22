package com.curso.ecommerce.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileService {
	private String folder="images/";
	private final Path root=Paths.get("images");
	
	public UploadFileService() {
		if(!Files.exists(root)) {
			try {
				Files.createDirectory(root);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("No se pudo crear el directorio");
				e.printStackTrace();
			}
		}
		
	}
	
	
	public String save_image(MultipartFile file) throws IOException {
		if(!file.isEmpty()) {
			byte[] bytes=file.getBytes();
			Path path=Paths.get(folder+file.getOriginalFilename());
			Files.write(path, bytes);
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
	
	public void deleteImage(String nombre) {
		String ruta="images//";
		File file=new File(ruta+nombre);
		file.delete();
	}
}
