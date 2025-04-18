package com.hcl.book.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hcl.book.entities.Book;

import jakarta.validation.Valid;

public interface BookService{

	Book addBook(Book book1);
	List<Book> getAllBook();
	Book getByID(Long id);
	void deleteBook();
	Book deleteByID(Long id);
	List<Book> getPublishedBooks();
	List<Book> getBooksByTitle(String title);
	Book updateBook(Long bookId, @Valid Book book);

}
