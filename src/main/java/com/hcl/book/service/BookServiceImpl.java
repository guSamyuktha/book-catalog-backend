package com.hcl.book.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hcl.book.entities.Book;
import com.hcl.book.exceptions.BookNotFoundException;
import com.hcl.book.repository.BookRepository;

import io.jsonwebtoken.io.IOException;
import jakarta.validation.Valid;

@Service
public class BookServiceImpl implements BookService{

	@Autowired
	BookRepository bookRepository;
	
	@Autowired
	JobLauncher jobLauncher;
	
	@Autowired
    private Job job;

	
	@Override
	public Book addBook(Book book1) {
		
		return bookRepository.save(book1);
	}
	@Override
	public List<Book> getAllBook() {
		return bookRepository.findAll();
	}
	@Override
	public Book getByID(Long id) {
		return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found with id " + id));
	}
	@Override
	public void deleteBook() {
		bookRepository.deleteAll();
	}
	@Override
	public Book deleteByID(Long id) {
		
		 return bookRepository.findById(id).map(existingBook -> {
			 bookRepository.deleteById(id);
		        return existingBook;
		    }).orElseThrow(() -> new BookNotFoundException("Book not found with id " + id));
		
	}
	@Override
	public List<Book> getPublishedBooks() {
		// TODO Auto-generated method stub
		return bookRepository.findAllByPublished(true);
	}
	@Override
	public List<Book> getBooksByTitle(String title) {
		return bookRepository.findByTitleContainingIgnoreCase(title);
	}
	@Override
	public Book updateBook(Long bookId, @Valid Book book) {
		    return bookRepository.findById(bookId).map(existingBook -> {
		        existingBook.setTitle(book.getTitle());
		        existingBook.setPublished(book.isPublished());
		        existingBook.setDescription(book.getDescription());
		        return bookRepository.save(existingBook);
		    }).orElseThrow(() -> new BookNotFoundException("Book not found with id " + bookId));
		}
	}
		

