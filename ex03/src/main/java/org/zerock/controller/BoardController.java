package org.zerock.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.zerock.domain.Criteria;
import org.zerock.domain.PageDTO;
import org.zerock.service.BoardService;

import lombok.extern.log4j.Log4j;

@Log4j
@Controller
@RequestMapping("/board/*")
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	@GetMapping("/list")
	public void list(Criteria cri, Model model) {
		log.info("list : " + cri);
		model.addAttribute("list", boardService.getList(cri));
		// model.addAttribute("pageMaker", new PageDTO(cri, 123));
		
		int total = boardService.getTotal(cri);
		
		log.info("total : " + total);
		
		model.addAttribute("pageMaker", new PageDTO(cri, total));
	}
	
	@GetMapping({"/get", "/modify"})
	public void get(@RequestParam("bno") Long bno, 
				@ModelAttribute("cri") Criteria cri, 
				Model model) {
		
		log.info("/get or modify");
		
		model.addAttribute("board", boardService.get(bno));
	}
	
	@GetMapping("/register")
	public void register() {
		
	}
	
	@ResponseBody
	@GetMapping(value="/getAttachList", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<BoardAttachVO>> getAttachList(Long bno) {
		return new ResponseEntity<>(boardService.getAttachList(bno), HttpStatus.OK);
	}
	
	@PostMapping("/register")
	public String register(BoardVO boardVO, RedirectAttributes rttr) {
		
		log.info("==========================");
		
		log.info("register : " + boardVO);
		
		if (boardVO.getAttachList() != null) {
			boardVO.getAttachList().forEach(attach -> log.info(attach));
		}
		
		log.info("==========================");
		
		boardService.register(boardVO);
		
		//redirect시, 추가적으로 데이터를 전달하기 위해 사용한다.
		rttr.addFlashAttribute("result", boardVO.getBno());
		
		return "redirect:/board/list";
	}
	
	@PostMapping("/modify")
	public String modify(BoardVO boardVO, 
						@ModelAttribute("cri") Criteria cri, 
						RedirectAttributes rttr) {
		
		log.info("modify: " + boardVO);
		
		if (boardService.modify(boardVO)) {
			rttr.addFlashAttribute("result", "success");
		}
		
		/*
		 * rttr.addAttribute("pageNum", cri.getPageNum()); 
		 * rttr.addAttribute("amount", cri.getAmount()); 
		 * rttr.addAttribute("type", cri.getType());
		 * rttr.addAttribute("keyword", cri.getKeyword());
		 */
		
		return "redirect:/board/list" + cri.getListLink();
	}
	
	@PostMapping("/remove")
	public String remove(@RequestParam("bno") Long bno, 
						@ModelAttribute("cri") Criteria cri, 
						RedirectAttributes rttr) {
		
		log.info("remove...." + bno);
		
		List<BoardAttachVO> attachList = boardService.getAttachList(bno);
		
		if (boardService.remove(bno)) {
			deleteFiles(attachList);
			rttr.addFlashAttribute("result", "success");
		}
		
		/*
		 * rttr.addAttribute("pageNum", cri.getPageNum()); 
		 * rttr.addAttribute("amount", cri.getAmount());
		 * rttr.addAttribute("type", cri.getType()); 
		 * rttr.addAttribute("keyword", cri.getKeyword());
		 */
		
		return "redirect:/board/list" + cri.getListLink();
	}
	
	// 게시판이 submit으로 올라간후, 파일을 지울때 사용하는 메서드
	private void deleteFiles(List<BoardAttachVO> attachList) {
		if (attachList == null || attachList.size() == 0) {
			return;
		}
		
		log.info("delete attach files......");
		log.info(attachList);
		
		attachList.forEach(attach -> {
			try {
				Path filePath = 
						Paths.get("C:\\upload\\" + attach.getUploadPath() + "\\" + attach.getUuid() + "_" + attach.getFileName());
				Files.deleteIfExists(filePath);
				
				if (Files.probeContentType(filePath).startsWith("image")) {
					Path thumbnail = 
							Paths.get("C:\\upload\\" + attach.getUploadPath() + "\\s_" + attach.getUuid() + "_" + attach.getFileName());
					Files.delete(thumbnail);
				}
			} catch (Exception e) {
				log.error("delete file error : " + e.getMessage());
			}
		}); // end foreach
	} // end deleteFiles
}
