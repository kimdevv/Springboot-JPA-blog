package com.kimdev.Blog.controller;

import com.kimdev.Blog.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping({"", "/"}) // 아무것도 안 적었을 때랑 /을 적으면 이게 호출됨.
    public String index(Model model) {
        model.addAttribute("boards", boardService.글목록()); // Model 사용하면, 넘어가는 index.jsp에 여기 담은 boards가 같이 날아감.
        return "index"; // viewResolver 작동 -> index.jsp가 나타남.
    }

    @GetMapping("board/{id}")
    public String findById(@PathVariable int id, Model model) {
        model.addAttribute(boardService.글상세보기(id));
        return "board/detail";
    }

    @GetMapping("/board/updateForm/{id}")
    public String updateForm(@PathVariable int id, Model model) {
        model.addAttribute("board", boardService.글상세보기(id));
        return "board/updateForm";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        return "board/saveForm";
    }
}
