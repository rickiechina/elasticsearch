package com.rickie.elasticsearch.transportclientdemo.controller;

import com.rickie.elasticsearch.transportclientdemo.domain.vo.BookVO;
import com.rickie.elasticsearch.transportclientdemo.domain.vo.BoolQueryVO;
import com.rickie.elasticsearch.transportclientdemo.service.BookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("book")
public class BookController {
    @Resource
    private BookService bookService;

    @PostMapping("add")
    public String add(@RequestBody BookVO vo) {
        return bookService.addBook(vo);
    }

    @GetMapping("test")
    public String test() {
        return "hello rickie";
    }

    @GetMapping("get/{id}")
    public String get(@PathVariable String id) {
        System.out.println("id:" + id);
        return bookService.findBookById(id);
    }

    @GetMapping("gets")
    public String gets(@RequestParam("ids") List<String> ids) {
        return bookService.findBooks(ids);
    }

    @PostMapping("update")
    public String update(@RequestBody BookVO vo) {
        System.out.println("id:" + vo.getId());
        return bookService.update(vo);
    }

    @PostMapping("delete/{id}")
    public String delete(@PathVariable String id) {
        System.out.println("id:" + id);
        return bookService.delete(id);
    }

    @PostMapping("search")
    public String search(@RequestBody BoolQueryVO vo) {
        return bookService.boolQuery(vo);
    }
}
