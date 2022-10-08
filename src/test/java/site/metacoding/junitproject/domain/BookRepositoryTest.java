package site.metacoding.junitproject.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest // DB와 관련된 컴포넌트만 메모리에 로딩
public class BookRepositoryTest {

    @Autowired // DI
    private BookRepository bookRepository;

    //@BeforeAll // 테스트 시작전에 한번만 실행됨.
    @BeforeEach // 각 테스트 시작전에 한번씩 실행됨.
    public void 데이터준비() {
        String title = "junit";
        String author = "겟인데어";
        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();
        bookRepository.save(book);
    } // 트랜잭션 종료됐다면 말이안됨.
    // 가정 1 : [데이터준비() + 1 책등록]  (Transaction),   [데이터준비() + 2 책목록보기] (Transaction) 사이즈1  (검증완료)
    // 가정 2 : [데이터준비() + 1 책등록 + 데이터준비() + 2 책목록보기] (Transaction) -> 사이즈2   (검증실패)

    // 1. 책 등록
    @Test
    public void 책등록_Test() {
        // given (데이터 준비)
        String title = "junit5";
        String author = "메타코딩";
        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();

        // when (테스트 실행)
        Book bookPS = bookRepository.save(book);

        // then (검증)
        assertEquals(title, bookPS.getTitle());
        assertEquals(author, bookPS.getAuthor());
    } // 트랜잭션 종료 (저장된 데이터를 초기화함.)

    // 2. 책 목록보기
    @Test
    public void 책목록보기_Test() {
        // given
        String title = "junit";
        String author = "겟인데어";

        // when
        List<Book> booksPS = bookRepository.findAll();

        System.out.println("사이즈 : =========================" + booksPS.size());

        // then
        assertEquals(title, booksPS.get(0).getTitle());
        assertEquals(author, booksPS.get(0).getAuthor());
    }

    // 3. 책 한건보기
    @Sql("classpath:db/tableInit.sql")
    @Test
    public void 책한건보기_Test() {
        // given
        String title = "junit";
        String author = "겟인데어";

        // when
        Book bookPS = bookRepository.findById(1L).get();

        // then
        assertEquals(title, bookPS.getTitle());
        assertEquals(author, bookPS.getAuthor());
    }

    // 4. 책 삭제
    @Sql("classpath:db/tableInit.sql")
    @Test
    public void 책삭제_Test() {
        // given
        Long id = 1L;

        // when
        bookRepository.deleteById(id);

        // then
        assertFalse(bookRepository.findById(id).isPresent());
    }

    // 5. 책 수정
    @Test
    public void 책수정_Test() {
        // given
        Long id = 1L;
        String title = "junit5";
        String author = "메타코딩";
        Book book = new Book(id, title, author);

        // when
        Book bookPS = bookRepository.save(book);

        bookRepository.findAll().stream()
                .forEach((book1) -> {
                    System.out.println(book1.getTitle());
                    System.out.println(book1.getId());
                    System.out.println(book1.getAuthor());
                    System.out.println("==========================");
                });

        // then
    }
}
