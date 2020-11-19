package org.zerock.task;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zerock.domain.BoardAttachVO;
import org.zerock.mapper.BoardAttachMapper;

import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class FileCheckTask {

	@Autowired
	private BoardAttachMapper boardAttachMapper;
	
	@Scheduled(cron="0 * * * * *")
	public void checkFiles() {
		log.warn("File Check Task run..........");
		log.warn(new Date());
		
		// 데이터베이스에서 boardAttachVO 객체로 값을 전달해준다.
		List<BoardAttachVO> fileList = boardAttachMapper.getOldFiles();
		
		// 객체의 속성들을 이용하여 파일이 존재하는 path를 각각의 데이터마다 만들어준다.
		List<Path> fileListPath = fileList.stream()
				.map(vo -> Paths.get("C:\\upload", vo.getUploadPath(), vo.getUuid() + "_" + vo.getFileName()))
				.collect(Collectors.toList());
		
		// 이미지 파일이면 섬네일 파일까지 fileListPath 리스트에 넣어준다.
		fileList.stream().filter(vo -> vo.isFileType() == true)
			.map(vo -> Paths.get("C:\\upload", vo.getUploadPath(), "s_" + vo.getUuid() + "_" + vo.getFileName()))
			.forEach(p -> fileListPath.add(p));
		
		log.warn("=================================");
		
		fileListPath.forEach(filePath -> log.warn(filePath));
		
		// 
		File targetDirectory = Paths.get("C:\\upload", getFolderYesterDay()).toFile();
		log.info("target directory에는 fileName이 들어오니?: " + targetDirectory);
		
		File[] removeFiles = targetDirectory
				.listFiles(file -> fileListPath.contains(file.toPath()) == false);
		log.info("removeFiles에는 fileName이 들어오니?: " + removeFiles[0].toString());
		
		
		log.warn("--------------------------");
		for (File file : removeFiles) {
			log.warn(file.getAbsolutePath());
			file.delete();
		}
	}
	
	private String getFolderYesterDay() {
		SimpleDateFormat simpleDataFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		
		String dateOfYesterday = simpleDataFormat.format(calendar.getTime());
		
		return dateOfYesterday.replace("-", File.separator);
	}
}
