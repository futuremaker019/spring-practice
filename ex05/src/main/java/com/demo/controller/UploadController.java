package com.demo.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
<<<<<<< HEAD
=======
import java.net.URLEncoder;
>>>>>>> 1f45e21b71279fc85e0df3ac70c238a635a68e5c
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.demo.domain.AttachFileDTO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Log4j
@Controller
public class UploadController {

	@GetMapping("/uploadForm")
	public void uploadForm() {
		log.info("upload form");
	}
	
	@PostMapping("/uploadFormAction")
	public void uploadFormPost(MultipartFile[] uploadFile, Model model) {
		String uploadFolder = "C:\\upload";
		
		for (MultipartFile multipartFile : uploadFile) {
			log.info("---------------------");
			log.info("Upload File Name : " + multipartFile.getOriginalFilename());
			log.info("Upload File size : " + multipartFile.getSize());
			
			File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());
			
			try {
				multipartFile.transferTo(saveFile);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
	}
	
	@GetMapping("/uploadAjax")
	public void uploadAjax() {
		log.info("upload ajax");
	}
	
	@GetMapping("/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName) {
		log.info("fileName : " + fileName);
		
		File file = new File("c:\\upload\\" + fileName);
		
		log.info("file : " + file);
		
		ResponseEntity<byte[]> result = null;
		try {
			HttpHeaders header = new HttpHeaders();
			header.add("Content-Type", Files.probeContentType(file.toPath()));
			log.info("file.toPath() : " + file.toPath());
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}
	
	@ResponseBody
	@GetMapping(value="/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<Resource> downloadFile(String fileName){
		log.info("download file : " + fileName);
		
		Resource resource = new FileSystemResource("c:\\upload\\" + fileName);
		log.info("resource : " + resource);
		
		if (resource.exists() == false) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		String resouceName = resource.getFilename();
		log.info("resourceName : " + resouceName);
		
		// remove UUID
		String resourceOriginalName = resouceName.substring(resouceName.indexOf("_") + 1);
		HttpHeaders headers = new HttpHeaders();
		try {
			// 파일 이름이 한글인 경우 저장할 때 까지 문제를 막기 위해서 문자열 처리를 해준다.
			String downloadName = new String(resourceOriginalName.getBytes("UTF-8"), "ISO-8859-1");
			headers.add("Content-Disposition", "attachment; fileName=" + downloadName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	}
	
	@ResponseBody
	@PostMapping(value = "/uploadAjaxAction", produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
	public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost(MultipartFile[] uploadFile) {
		log.info("upload ajax post........");
		
		List<AttachFileDTO> list = new ArrayList<>();
		String uploadFolder = "C:\\upload";
		
		// make folder
		// file("업로드될 폴더 경로", "폴더의 이름(여기서는 날짜로 구분했다.)"
		String uploadFolderPath = getFolder();
		File uploadPath = new File(uploadFolder, uploadFolderPath);
		log.info("upload path : " + uploadPath);
		
		// make yyyy/MM/dd
		// 업로드 시, 해당 날짜의 폴더가 존재하지 않는다면 생성해라.
		if (uploadPath.exists() == false) {
			uploadPath.mkdirs();
		}
		
		for(MultipartFile multipartFile : uploadFile) {
			log.info("---------------------");
			log.info("Upload File Name : " + multipartFile.getOriginalFilename());
			log.info("Upload File size : " + multipartFile.getSize());
			
			AttachFileDTO attachDTO = new AttachFileDTO();
			
			String uploadFileName = multipartFile.getOriginalFilename();
			
			// file의 이름을 저장
			attachDTO.setFileName(uploadFileName);
			
			// UUID를 추가하여 중복된 파일을 업로드 시, 기존의 파일이 지워지는 것을 방지해준다.
			UUID uuid = UUID.randomUUID();
			uploadFileName = uuid.toString() + "_" + uploadFileName;
			
			// File saveFile = new File(uploadFolder, uploadFileName);
			File saveFile = new File(uploadPath, uploadFileName);
			
			try {
				multipartFile.transferTo(saveFile);
				
				// file의 uuid, uploadPath를 지정해준다.
				attachDTO.setUuid(uuid.toString());
				attachDTO.setUploadPath(uploadFolderPath);
				
				// check image type file
				if (checkImageType(saveFile)) {
					
					// file이 이미지이면 true를 넣게 해준다.
					attachDTO.setImage(true);
					
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));
					Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 100, 100);
					thumbnail.close();
				}
				
				//add to List
				list.add(attachDTO);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}// end for
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value="/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<Resource> downloadFile(String fileName) {
		log.info("download file : " + fileName);
		
		Resource resource = new FileSystemResource("c:\\upload\\" + fileName);
		
		if (resource.exists() == false) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		String resourceName = resource.getFilename();
		
		// remove UUID
		String resourceOriginalName = resourceName.substring(resourceName.indexOf("_") + 1);
		
		HttpHeaders headers = new HttpHeaders();
		try {
			String downloadName = new String(resourceOriginalName.getBytes("UTF-8"), "ISO-8859-1");
			log.info("downloadName : " + downloadName);
			
			headers.add("Content-Disposition", "attachment; filename=" + downloadName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	}
	
	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		//java.util.Date
		Date date = new Date();
		String str = sdf.format(date);
		return str.replace("-", File.separator);
	}
	
	private boolean checkImageType(File file) {
		try {
			String contentType = Files.probeContentType(file.toPath());
			return contentType.startsWith("image");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}
