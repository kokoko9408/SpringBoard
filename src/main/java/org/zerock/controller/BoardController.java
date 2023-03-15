package org.zerock.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criterial;
import org.zerock.domain.PageDTO;
import org.zerock.service.BoardService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@Controller
@RequestMapping("/board/*")
@AllArgsConstructor // 클래스에 존재하는 모든 필드에 대한 생성자를 자동으로 생성해주는 역할
public class BoardController {

	private BoardService service;

	@GetMapping("/list")
	public void list(Criterial cri, Model model) {
		log.info("list : " + cri);
		int total = service.getTotal(cri);
		model.addAttribute("list", service.getList(cri)); //
		model.addAttribute("pageMaker", new PageDTO(cri, 123));
		// 현재페이지, 페이지당 화면상 보여지는 레코드 수, 전체 카운트

	}

	@PostMapping("/register")
	public String register(BoardVO board, RedirectAttributes rttr) {

		log.info("==========================");

		log.info("register: " + board);

		if (board.getAttachList() != null) {

			board.getAttachList().forEach(attach -> log.info(attach));

		}

		log.info("==========================");

		service.register(board);

		rttr.addFlashAttribute("result", board.getBno());

		return "redirect:/board/list";
	}

	@GetMapping({ "/get", "/modify" }) // get방식도 받고 modify방식도 받음
	public void get(@RequestParam("bno") Long bno, @ModelAttribute("cri") Criterial cri, Model model) {
		log.info("겟이랑 모디파이 여기라구");
		model.addAttribute("board", service.get(bno)); // get.jsp로 감

	}

//	@GetMapping("/get")
//	public void get(@RequestParam("bno")Long bno, Model model) {
//		log.info("/get");
//		model.addAttribute("board",service.get(bno));
//	} //조회페이지에서는 게시물 번호와 모든 데이터가 읽기 전용

	@PostMapping("/modify")
	public String modify(BoardVO board, @ModelAttribute("cri") Criterial cri, RedirectAttributes rttr) {
		log.info("모디파이");

		if (service.modify(board)) {
			rttr.addFlashAttribute("result", "modify");
		}
		rttr.addAttribute("pageNum", cri.getPageNum()); // addAttribute 일회성으로 데이터 전달
		rttr.addAttribute("amount", cri.getAmount());
		rttr.addAttribute("type", cri.getType());
		rttr.addAttribute("keyword", cri.getKeyword());

		return "redirect:/board/list";
	}

	
	/*
	 * 삭제 전 해당 게시물의 첨부파일 목록을 확보 
	 * 데이터베이스에서 게시물과 첨부파일 데이터 삭제 
	 * 삭제 성공 시 실제 파일의 삭제 시도
	 */
	@PostMapping("/remove")
	public String remove(@RequestParam("bno") Long bno, @ModelAttribute("cri") Criterial cri, RedirectAttributes rttr) {
		
		log.info("remove..." +bno);
		
		List<BoardAttachVO> attachList = service.getAttachList(bno);

		if (service.remove(bno)) {
			rttr.addAttribute("result", "success");
		}
//		rttr.addAttribute("pageNum", cri.getPageNum()); // addAttribute 일회성으로 데이터 전달
//		rttr.addAttribute("amount", cri.getAmount());
//		rttr.addAttribute("type", cri.getType());
//		rttr.addAttribute("keyword", cri.getKeyword());
		return "redirect:/board/list" + cri.getListLink();

	}

	// 게시물의 등록작업은 post방식
	// 화면에서 입력을 받아야하면 get방식
	@GetMapping("/register") // 입력한 페이지를 보여주는 역할만 하기 때문에 별도의 처리 필요x
	public void register() { // WEB-INF/views/board/register.jsp로 이동
								// @RequestMapping("/board/*")
	}

	@GetMapping(value = "/getAttachList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<List<BoardAttachVO>> getAttachList(Long bno) {

		log.info("getAttachList " + bno);

		return new ResponseEntity<>(service.getAttachList(bno), HttpStatus.OK);

	}

	private void deleteFiles(List<BoardAttachVO> attachList) {

		if (attachList == null || attachList.size() == 0) {
			return;
		}

		log.info("delete attach files...................");
		log.info(attachList);

		attachList.forEach(attach -> {
			try {
				Path file = Paths.get(
						"C:\\upload\\" + attach.getUploadPath() + "\\" + attach.getUuid() + "_" + attach.getFileName());

				Files.deleteIfExists(file);

				if (Files.probeContentType(file).startsWith("image")) {

					Path thumbNail = Paths.get("C:\\upload\\" + attach.getUploadPath() + "\\s_" + attach.getUuid() + "_"
							+ attach.getFileName());

					Files.delete(thumbNail);
				}

			} catch (Exception e) {
				log.error("delete file error" + e.getMessage());
			} // end catch
		});// end foreachd
	}

}
