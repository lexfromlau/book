package com.lex.books.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lex.books.domain.Book;
import com.lex.books.services.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.lex.books.TestData.ISBN;
import static com.lex.books.TestData.testBook;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testThatBookIsCreated() throws Exception {
        Book book = testBook();
        ObjectMapper objectMapper = new ObjectMapper();
        String bookJson = objectMapper.writeValueAsString(book);
        mockMvc.perform(MockMvcRequestBuilders.put("/books/" + book.getIsbn()).content(bookJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()));
    }

    @Test
    public void testThatBookIsUpdatedReturnsHttp201() throws Exception {
        Book book = testBook();
        boolean isBookExists = bookService.isBookExists(book);
        book.setAuthor("Another name");
        ObjectMapper objectMapper = new ObjectMapper();
        String bookJson = objectMapper.writeValueAsString(book);
        mockMvc.perform(MockMvcRequestBuilders.put("/books/" + book.getIsbn()).content(bookJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(isBookExists ? MockMvcResultMatchers.status().isOk()
                        : MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()));
    }

    @Test
    public void testThatRetrieveBookReturns404WhenBookNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books/12345688568465956"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatRetrieveBookReturns200AndBookWhenExists() throws Exception {
        Book book = testBook();
        bookService.save(book);
        mockMvc.perform(MockMvcRequestBuilders.get("/books/" + book.getIsbn()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()));
    }

    @Test
    public void testThatListBooksReturnsHttp200EmptyListWhenNoBooksExist() throws Exception {
        bookService.getAll();
        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    public void testThatListBooksReturnsHttp200AndBooksWhenBooksExists() throws Exception {
        Book book = testBook();
        bookService.save(book);
        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].title").value(book.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].author").value(book.getAuthor()));
    }

    @Test
    public void testThatHttp204IsReturnedWhenBookDoesntExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/" + ISBN))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatHttp204IsReturnedWhenExistingBookIsDeleted() throws Exception {
        Book book = testBook();
        bookService.save(book);
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/" + book.getIsbn()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
