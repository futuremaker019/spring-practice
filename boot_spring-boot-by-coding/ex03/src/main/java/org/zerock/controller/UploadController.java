package org.zerock.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import org.zerock.domain.AttachFileDTO;
import org.zerock.domain.BoardAttachVO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Log4j
@Controller
public class UploadController {
	
	private final static String uploadFolder = "C:\\upload";
	
	@GetMapping("/uploadForm")
	public void uploadForm() {
		log.info("upload Form");
	}
	
	@PostMapping("/uploadFormAction")
	public void uploadFormPost(MultipartFile[] uploadFile, Model model) {
		
		for (MultipartFile multipartFile : uploadFile) {
			log.info("--------------------");
			log.info("Upload file Name : " + multipartFile.getOriginalFilename());
			log.info("upload file Size : " + multipartFile.getSize());
			
			File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());
			
			try {
				multipartFile.transferTo(saveFile);
			} catch (Exception e) {
				log.error(e.getMessage());
			} // end catch
		} //end for
	}
	
	@GetMapping("/uploadAjax")
	public void uploadAjax() {
		log.info("upload Ajax");
	}
	
	
	
	@ResponseBody
	@GetMapping("/display")
	public ResponseEntity<byte[]> getFile(String fileName){
		log.info("fileName in display: " + fileName);
		
		File file = new File(uploadFolder+ "\\" + fileName);
		
		log.info("file in display : " + file);
		
		ResponseEntity<byte[]> result = null;
		
		try {
			HttpHeaders header = new HttpHeaders();
			header.add("Content-Type", Files.probeContentType(file.toPath()));
			result = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@ResponseBody
	@GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<Resource> downloadFile(String fileName) {
		log.info("download file : " + fileName);
		
		Resource resource = new FileSystemResource(uploadFolder + "\\" + fileName);
		
		log.info("resource : " + resource);
		
		String resourceName = resource.getFilename();
		
		//remove UUID
		String resourceOriginalName = resourceName.substring(resourceName.indexOf("_") + 1);
		
		HttpHeaders headers = new HttpHeaders();
		
		try {
			headers.add("Content-Disposition", 
					"attachment; fileName=" + new String(resourceOriginalName.getBytes("UTF-8"), "ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	}
	
	@PostMapping(value = "/uploadAjaxAction", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost(MultipartFile[] uploadFile) {
		
		List<AttachFileDTO> list = new ArrayList<AttachFileDTO>();
		log.info("upload ajax post........");
		
		// 날짜를 디렉토리로 구분해여 path를 만들어준다.
		String uploadFolderPath = getFolderFormat();
		File uploadPath = new File(uploadFolder, uploadFolderPath);
		log.info("upload path in uploadAjaxAction: " + uploadPath);
		
		// 날짜로 들어오는 디렉토리가 없으면 만들어준다.
		if (!uploadPath.exists()) {
			// make yyyy/MM/dd folder
			uploadPath.mkdirs();
		}
		
		for (MultipartFile multipartFile : uploadFile) {
			log.info("---------------------");
			log.info("Upload File Name in uploadAjaxAction : " + multipartFile.getOriginalFilename());
			log.info("Upload File size in uploadAjaxAction : " + multipartFile.getSize());
			
			AttachFileDTO attachDTO = new AttachFileDTO();
			
			String uploadFileName = multipartFile.getOriginalFilename();
			// 파일의 이름을 객체에 set
			attachDTO.setFileName(uploadFileName);
			
			UUID uuid = UUID.randomUUID();
			uploadFileName = uuid.toString() + "_" + uploadFileName;
			
			File saveFile = new File(uploadPath, uploadFileName);
			
			try {
				multipartFile.transferTo(saveFile);
				
				// 파일의 uuid와 uploadPath를 set
				attachDTO.setUuid(uuid.toString());
				attachDTO.setUploadPath(uploadFolderPath);
				
				if (checkImageType(saveFile)) {
					// 파일의 타입이 이미지이면 true를 객체에 set 
					attachDTO.setImage(true);
					
					// outputstream의 생성자의 속성으로 filePath가 들어와야 하는구나
					FileOutputStream thumbnailOutputStream = 
							new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));
					// inputstream, outputstream이 들어오고, 섬네일 크기도 지정해야 한다.
					// 섬네일을 만들어준다.
					Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnailOutputStream, 50, 50);
					thumbnailOutputStream.close();
				} // end if
				
				// 
				list.add(attachDTO);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		} // end for loop
		
		return new ResponseEntity<List<AttachFileDTO>>(list, HttpStatus.OK);
	}
	
	@ResponseBody
	@PostMapping("/deleteFile")
	public ResponseEntity<String> deleteFile(String fileName, String type) {
		log.info("deteteFile : " + fileName);
		
		File file;
		
		try {
			file = new File(uploadFolder + "\\" + URLDecoder.decode(fileName, "UTF-8"));
			file.delete();
			
			if (type.equals("image")) {
				String largeFileName = file.getAbsolutePath().replace("s_", "");
				log.info("orininal file Name: " + largeFileName);
				
				// 원본 이미지를 찾아서 지워준다.
				file = new File(largeFileName);
				file.delete();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<String>("deleted", HttpStatus.OK);
	}
	
	// 날짜로 파일 경로를 만드는 메서드
	private String getFolderFormat() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String str = sdf.format(date);
		return str.replace("-", File.separator);
	}
	
	// 이미지 타입을 검사하는 메서드
	private boolean checkImageType(File file) {
		try {
			String contentType = Files.probeContentType(file.toPath());
			return contentType.startsWith("image");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
