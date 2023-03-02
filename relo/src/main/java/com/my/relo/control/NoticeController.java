package com.my.relo.control;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.my.relo.dto.NoticeDTO;
import com.my.relo.entity.Member;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.exception.RemoveException;
import com.my.relo.repository.MemberRepository;
import com.my.relo.service.NoticeService;

@RestController
@RequestMapping("notice/*")
public class NoticeController {
	@Autowired
	private NoticeService ns;

	@Autowired
	private MemberRepository mr;

	private final String saveDirectory = "C:\\storage\\notice";

	// 공지사항 작성
	@PostMapping(value = "write/{mNum}")
	public ResponseEntity<?> write(@PathVariable Long mNum, NoticeDTO dto,
			@RequestPart(value = "f") List<MultipartFile> f)
			throws AddException, IllegalStateException, IOException, FindException {

		Optional<Member> optM = mr.findById(mNum);
		if (optM.isPresent()) {
			Member m = optM.get();
			
			dto = NoticeDTO.builder().member(m).title(dto.getNTitle()).date(LocalDate.now())
					.category(dto.getNCategory()).build();

			Long nNum = ns.addNotice(dto);

			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < f.size(); i++) {
				MultipartFile oneFile = f.get(i);
				String orignFileName = oneFile.getOriginalFilename();
				String folderName = "n_" + nNum;
				String fileName = "";
				if (f.size() > 1) {
					fileName = folderName + "_" + i + "." + orignFileName.substring(orignFileName.lastIndexOf(".") + 1);
				} else {
					fileName = folderName + "." + orignFileName.substring(orignFileName.lastIndexOf(".") + 1);
				}

				sb.append(fileName).append(",");

				File exitFolder = new File(saveDirectory, folderName);
				if (!exitFolder.exists()) {
					exitFolder.mkdirs();
				}

				File exitStorage = new File(saveDirectory + "/" + folderName + "/" + fileName);

				oneFile.transferTo(exitStorage);

				dto = NoticeDTO.builder().nNum(nNum).content(sb.toString()).build();
				
				ns.updateNotice(dto);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			throw new FindException();
		}
	}

	// 제목으로 검색
	@GetMapping(value = "title/{title}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> searchByTitle(@PathVariable String title) {
		try {
			List<NoticeDTO> results = ns.searchByTitle(title);
			return new ResponseEntity<>(results, HttpStatus.OK);
		} catch (FindException e) {
			e.printStackTrace();
			Map<String, Object> results = new HashMap<>();
			results.put("msg", e.getStackTrace());
			return new ResponseEntity<>(results, HttpStatus.BAD_REQUEST);
		}
	}

	// 카테고리로 검색
	@GetMapping(value = "category/{category}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> searchByCategory(@PathVariable Integer category) {
		try {
			List<NoticeDTO> results = ns.searchByCategory(category);
			return new ResponseEntity<>(results, HttpStatus.OK);
		} catch (FindException e) {
			e.printStackTrace();
			Map<String, Object> results = new HashMap<>();
			results.put("msg", e.getStackTrace());
			return new ResponseEntity<>(results, HttpStatus.BAD_REQUEST);
		}
	}

	// 전체 목록
	@GetMapping(value = "{currentPage}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> list(@PathVariable Integer currentPage) {
		try {
			Map<String, Object> results = ns.pagingNotice(currentPage);
			return new ResponseEntity<>(results, HttpStatus.OK);
		} catch (FindException e) {
			e.printStackTrace();
			Map<String, Object> results = new HashMap<>();
			results.put("msg", e.getStackTrace());
			return new ResponseEntity<>(results, HttpStatus.BAD_REQUEST);
		}
	}

	// 공지사항 상세
	@GetMapping(value = "detail/{nNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> detail(@PathVariable Long nNum) {
		try {
			NoticeDTO dto = ns.detailNotice(nNum);
			NoticeDTO pre = ns.searchPre(nNum);
			NoticeDTO next = ns.searchNext(nNum);

			Map<String, Object> results = new HashMap<>();

			if (pre == null) {
				results.put("msg", "이전글이 없습니다.");
			} else {
				results.put("pre", pre);
			}

			if (next == null) {
				results.put("msg", "다음글이 없습니다.");
			} else {
				results.put("next", next);
			}

			results.put("dto", dto);

			return new ResponseEntity<>(results, HttpStatus.OK);
		} catch (FindException e) {
			e.printStackTrace();
			Map<String, Object> results = new HashMap<>();
			results.put("msg", e.getStackTrace());
			return new ResponseEntity<>(results, HttpStatus.BAD_REQUEST);
		}
	}

	// 공지사항 수정
	@PutMapping("{nNum}")
	public ResponseEntity<?> modify(NoticeDTO dto) throws JsonMappingException, JsonProcessingException, FindException {
		ns.updateNotice(dto);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// 공지사항 파일 수정
	@GetMapping(value = "editfile/{nNum}")
	public ResponseEntity<?> editFiles(@PathVariable Long nNum,
			@RequestPart(value = "fileList") List<MultipartFile> fileList) throws IOException, FindException {
		String saveDirectory = "C:\\storage\\notice";
		File saveDirFile = new File(saveDirectory);
		StringBuilder sb = new StringBuilder();

		File[] files = saveDirFile.listFiles();
		for (File f : files) {
			String folderName = f.getName();
			int idx = folderName.lastIndexOf(".");

			if (idx == -1 && folderName.equals("n_" + nNum)) {

				File[] folder = f.listFiles();

				for (int j = 0; j < folder.length; j++) {
					folder[j].delete();
				}

				if (folder.length == 0 && f.isDirectory()) {
					f.delete();
					break;
				}

				for (int i = 0; i < fileList.size(); i++) {
					MultipartFile oneFile = fileList.get(i);
					String orignFileName = oneFile.getOriginalFilename();
					String exitFolderName = "n_" + nNum;
					String fileName = "";
					if (fileList.size() > 1) {
						fileName = exitFolderName + "_" + i + "."
								+ orignFileName.substring(orignFileName.lastIndexOf(".") + 1);
					} else {
						fileName = exitFolderName + "." + orignFileName.substring(orignFileName.lastIndexOf(".") + 1);
					}

					sb.append(fileName).append(",");

					File exitStorage = new File(saveDirectory + "/" + exitFolderName + "/" + fileName);

					oneFile.transferTo(exitStorage);
				}
			}
		}

		NoticeDTO dto = NoticeDTO.builder().nNum(nNum).content(sb.toString()).build();

		ns.updateNotice(dto);

		return new ResponseEntity<>(HttpStatus.OK);
	}

//	// 공지사항 파일 수정 및 출력
//	@GetMapping(value = "img/{nNum}")
//	public HttpEntity<?> showImage(@PathVariable Long nNum,
//			@RequestPart(value = "fileList", required = false) List<MultipartFile> fileList) throws IOException {
//		String saveDirectory = "C:\\storage\\notice";
//		File saveDirFile = new File(saveDirectory);
////		List<Resource> list = new ArrayList<>();
//
//		File[] files = saveDirFile.listFiles();
//		if (fileList == null) {
//
//			MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
//			HttpHeaders httpHeaders = new HttpHeaders();
//
//			for (File f : files) {
////				System.out.println(f.getName());
//
//				String fileName = f.getName();
//				int idx = fileName.lastIndexOf(".");
//				String ext = "";
//
//				if (idx != -1) {
//					ext = fileName.substring(idx + 1);
////				System.out.println("ext: "+ext);
//
//					if (f.getName().equals("n_" + nNum)) {
//						StringTokenizer stk = new StringTokenizer(f.getAbsolutePath(), "/");
//						String fileFullPath = stk.nextToken();
//
////						httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
//						form.add("files", f);
//
//						// 파일 크기 설정
//						httpHeaders.set(HttpHeaders.CONTENT_LENGTH, f.length() + "");
//						// 파일 타입 설정
//						httpHeaders.set(HttpHeaders.CONTENT_TYPE, Files.probeContentType(f.toPath()));
//						// 파일 인코딩 설정
//						httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION,
//								"inline; filename=" + URLEncoder.encode("a", "UTF-8"));
////					UrlResource urlResource = new UrlResource("file:" + fileFullPath + ext);
////					System.out.println(fileFullPath + ext);
////
////					String encodedDownloadFileName = UriUtils.encode(f.getName(), StandardCharsets.UTF_8);
////					String contentDisposition = "inline; filename=\"" + encodedDownloadFileName + "\"";
////
////					list.add(urlResource);
////					
////					return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
////							.body(list);
////						HttpEntity<?> downloadEntity = new HttpEntity<>(form, httpHeaders);
////						RestTemplate restTemplate = new RestTemplate();
////
////						restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
////						restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
////
////						String requestURL = request.getRequestURL().substring(0,
////								request.getRequestURL().indexOf(request.getRequestURI()));
////						String uri = requestURL + "/relo";
////
////						return restTemplate.getForEntity(uri, downloadEntity);
//					}
//				}
//			}
//			return new HttpEntity<>(form, httpHeaders);
//		}
////		return new ResponseEntity<>(HttpStatus.OK);
//		return null;
//	}

	// 공지사항 삭제
	@DeleteMapping("{nNum}")
	public ResponseEntity<?> remove(@PathVariable Long nNum) throws RemoveException, FindException {
		ns.deleteNotice(nNum);

		File saveDirFile = new File(saveDirectory);

		exit: while (saveDirFile.exists()) {

			File[] files = saveDirFile.listFiles();

			for (File f : files) {
				StringTokenizer stk = new StringTokenizer(f.getName(), ".");
				String fName = stk.nextToken();
				String folderName = "n_" + nNum;

				if (fName.equals(folderName)) {

					File[] folder = f.listFiles();

					for (int j = 0; j < folder.length; j++) {
						folder[j].delete();
					}

					if (folder.length == 0 && f.isDirectory()) {
						f.delete();
						break exit;
					}
				}
			}
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
