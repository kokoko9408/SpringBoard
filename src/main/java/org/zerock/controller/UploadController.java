package org.zerock.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
import org.zerock.domain.AttachFileDTO;

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

		// 첨부파일을 여러 개 선택할 수 있으므로 배열 타입으로 설정
		for (MultipartFile multipartFile : uploadFile) {

			log.info("-------------------------------------");
			log.info("Upload File Name: " + multipartFile.getOriginalFilename());
			log.info("Upload File Size: " + multipartFile.getSize());

			File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());

			try {
				multipartFile.transferTo(saveFile);
			} catch (Exception e) {
				log.error(e.getMessage());
			} // end catch
		} // end for

	}

	@GetMapping("/uploadAjax")
	public void uploadAjax() {

		log.info("upload ajax");
	}

	private String getFolder() { // 오늘 날짜의 경로를 문자열로 생성

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Date date = new Date();

		String str = sdf.format(date);

		return str.replace("-", File.separator);
	}

	/* 파일 자체가 이미지인지 정확히 체크하기 위한 메서드 */
	private boolean checkImageType(File file) {

		try {
			String contentType = Files.probeContentType(file.toPath());

			return contentType.startsWith("image");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	@PostMapping(value = "/uploadAjaxAction", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost(MultipartFile[] uploadFile) {

		List<AttachFileDTO> list = new ArrayList<>();

		// log.info("update ajax post..");

		String uploadFolder = "C:\\upload";

		String uploadFolderPath = getFolder();

		/* 년/월/일 폴더 생성 */
		//File uploadPath = new File(uploadFolder, getFolder());
		File uploadPath = new File(uploadFolder, uploadFolderPath);
		log.info("uploadPath : " + uploadPath);

		if (uploadPath.exists() == false) { // 해당경로가 있는지 검사 후, 폴더를 생성
			uploadPath.mkdirs(); // mkdirs()를 이용해 필요한 상위폴더까지 한 번에 생성 가능
		}

		for (MultipartFile multipartFile : uploadFile) {

			AttachFileDTO attachDTO = new AttachFileDTO();

//			log.info("-------------------------------------");
//			log.info("Upload File Name: " + multipartFile.getOriginalFilename());
//			log.info("Upload File Size: " + multipartFile.getSize());

			String uploadFileName = multipartFile.getOriginalFilename();

			// IE has file path
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
			
			log.info("only file name: " + uploadFileName);
			
			//File saveFile = new File(uploadPath, uploadFileName);
			
			attachDTO.setFileName(uploadFileName);

			// 동일한 이름 중복방지 위해 UUID값을 이용해 처리(임의의 값 생성)
			UUID uuid = UUID.randomUUID();

			uploadFileName = uuid.toString() + "_" + uploadFileName;

			try {
				File saveFile = new File(uploadPath, uploadFileName);
				multipartFile.transferTo(saveFile);

				attachDTO.setUuid(uuid.toString());
				attachDTO.setUploadPath(uploadFolderPath);
				// 이미지 파일인지 체크
				if (checkImageType(saveFile)) {

					attachDTO.setImage(true); // 이걸 안 넣어주면 image인지 아닌지 구분 못해

					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));
					// Thumbnailator는 InputStream과 File객체를 이용해 파일 생성 후, 사이즈에 대한
					// 부분을 파라미터로 width와 height를 지정가능
					Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 100, 100);

					thumbnail.close();
				}

				list.add(attachDTO);

			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
			} // end catch

		} // end for
		return new ResponseEntity<>(list, HttpStatus.OK);
		// 기존과 달리 ResponseEntity<List<AttachFileDTO>>를 반환하는 형태로 수정하고, JSON 데이터를
		// 반환하도록 변경

	}

	@GetMapping("/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName) {

		log.info("fileName: " + fileName);

		File file = new File("c:\\upload\\" + fileName);

		log.info("파일: " + file);

		ResponseEntity<byte[]> result = null;

		try {
			HttpHeaders header = new HttpHeaders();

			header.add("Content-Type", Files.probeContentType(file.toPath()));
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/* 이미지 다운로드 */
	@GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody									
	public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent")String userAgent, 
			String fileName) {			//@RequestHeader을 이용해 필요한 HTTP 헤더 메시지의 내용 수집

		log.info("download file: " + fileName);

		FileSystemResource resource = new FileSystemResource("c:\\upload\\" + fileName);
		
		log.info("resource: " + resource);
		
		if (resource.exists() == false) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		String resourceName = resource.getFilename();
		
		// resourceOriginalName을 생성해 UUID부분을 잘라낸 상태의 파일 이름으로 저장
		String resourceOriginalName = resourceName.substring(resourceName.indexOf("_") + 1);

		HttpHeaders headers = new HttpHeaders();
		
		try {

			String downloadName = null;

			if ( userAgent.contains("Trident")) {
				log.info("IE browser");
				downloadName = URLEncoder.encode(resourceName, "UTF-8").replaceAll("\\+", " ");
			}else if(userAgent.contains("Edge")) {
				log.info("Edge browser");
				downloadName =  URLEncoder.encode(resourceName,"UTF-8");
			}else {
				log.info("Chrome browser");
				downloadName = new String(resourceName.getBytes("UTF-8"), "ISO-8859-1");
			}
			
			log.info("downloadName: " + downloadName);

			headers.add("Content-Disposition", "attachment; filename=" + downloadName);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	}
	
	@PostMapping("/deleteFile")
	@ResponseBody
	public ResponseEntity<String> deleteFile(String fileName, String type) {

		log.info("deleteFile: " + fileName);

		File file;

		try {
			file = new File("c:\\upload\\" + URLDecoder.decode(fileName, "UTF-8"));

			file.delete();

			if (type.equals("image")) {

				String largeFileName = file.getAbsolutePath().replace("s_", "");

				log.info("largeFileName: " + largeFileName);

				file = new File(largeFileName);

				file.delete();
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<String>("deleted", HttpStatus.OK);

	}
	

}



