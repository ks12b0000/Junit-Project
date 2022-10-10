package site.metacoding.junitproject.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import site.metacoding.junitproject.service.BookService;
import site.metacoding.junitproject.web.dto.response.BookListRespDto;
import site.metacoding.junitproject.web.dto.response.BookRespDto;
import site.metacoding.junitproject.web.dto.request.BookSaveReqDto;
import site.metacoding.junitproject.web.dto.response.CMRespDto;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class BookApiController {

    private final BookService bookService;

    // 1. 책등록
    // key=value&key=value
    // {"key" : value, "key" : value}
    @PostMapping("/api/v1/book")
    public ResponseEntity<?> saveBook(@RequestBody @Valid BookSaveReqDto bookSaveReqDto, BindingResult bindingResult) {

        // AOP 처리하는게 더 좋음!
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                errorMap.put(fe.getField(), fe.getDefaultMessage());
            }
            System.out.println("=====================");
            System.out.println(errorMap.toString());
            System.out.println("=====================");

            throw new RuntimeException(errorMap.toString());
        }
        BookRespDto bookRespDto = bookService.책등록하기(bookSaveReqDto);
        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("책 저장 성공").body(bookRespDto).build(),
                HttpStatus.CREATED); // 201 = insert
    }

    // 2. 책목록보기
    @GetMapping("/api/v1/bookList")
    public ResponseEntity<?> getBookList() {
        BookListRespDto bookListRespDto = bookService.책목록보기();
        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("책 목록보기 성공").body(bookListRespDto).build(),
                HttpStatus.OK); // 200 = ok
    }

    // 3. 책한건보기
    @GetMapping("/api/v1/book/{id}")
    public ResponseEntity<?> getBookOne(@PathVariable Long id) {
        BookRespDto bookRespDto = bookService.책한건보기(id);
        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("책 한건보기 성공").body(bookRespDto).build(),
                HttpStatus.OK);
    }

    // 4. 책삭제
    @DeleteMapping("/api/v1/book/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        bookService.책삭제하기(id);
        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("책 삭제하기 성공").body(null).build(),
                HttpStatus.OK);
    }

    // 5. 책수정
    @PutMapping("/api/v1/book/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody @Valid BookSaveReqDto bookSaveReqDto, BindingResult bindingResult) {

        // AOP 처리하는게 더 좋음!
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                errorMap.put(fe.getField(), fe.getDefaultMessage());
            }
            throw new RuntimeException(errorMap.toString());
        }
        BookRespDto bookRespDto = bookService.책수정하기(id, bookSaveReqDto);
        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("책 수정하기 성공").body(bookRespDto).build(),
                HttpStatus.OK);
    }
}
