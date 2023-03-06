package com.my.relo.control;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringTokenizer;

import javax.servlet.http.HttpSession;

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
	@PostMapping(value = "write")
	public ResponseEntity<?> write(
			@RequestPart(value = "param") Map<String, Object> param 
			, HttpSession session
			,			@RequestPart(required = false) List<MultipartFile> f
			)
			throws AddException, IllegalStateException, IOException, FindException {
		
		String title = (String) param.get("title");
		String rawCategory = (String) param.get("category");
		Integer category = Integer.valueOf(rawCategory);
		String content = (String) param.get("content");
		
		
		System.out.println("값1: " + title);
		System.out.println("값2: " + category);
		System.out.println("값3: " + content);

		Long mNum = (Long) session.getAttribute("logined");
		Optional<Member> optM = mr.findById(4L);
		if (optM.isPresent()) {
			Member m = optM.get();

			NoticeDTO dto = NoticeDTO.builder().member(m).title(title).date(LocalDate.now())
					.category(category).build();

			Long nNum = ns.addNotice(dto);

			String folderName = "n_" + nNum;
			String fileName = folderName + ".html";
			
			File exitFolder = new File(saveDirectory, folderName);
			if (!exitFolder.exists()) {
				exitFolder.mkdirs();
			}
			
			FileWriter writer = new FileWriter(saveDirectory+"/"+folderName+"/"+fileName);
			writer.write(content);
			writer.close();
			if(!f.isEmpty()) {
			uploadFile(dto, nNum, f);
			}

			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			throw new FindException();
		}
	}
	
	public void uploadFile(NoticeDTO dto, Long nNum, List<MultipartFile> f) throws IllegalStateException, IOException, FindException {
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

			File exitStorage = new File(saveDirectory + "/" + folderName + "/" + fileName);

			oneFile.transferTo(exitStorage);

			dto = NoticeDTO.builder().nNum(nNum).content(sb.toString()).build();

			ns.updateNotice(dto);
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

//	@PostMapping("showfile/{nNum}")
//	public ResponseEntity<?> showFile(List<MultipartFile> fileList) {
//		
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
