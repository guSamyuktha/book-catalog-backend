package com.hcl.book.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hcl.book.entities.ApiResponse;
import com.hcl.book.entities.Book;
import com.hcl.book.entities.ResponseUtil;
import com.hcl.book.service.BookService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class BookController {
	@Autowired
	private BookService bookService;
	
	@Autowired
	private Job job;
	
	@Autowired
	private JobLauncher jobLauncher;
	
//	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@PostMapping("/books")
	public ResponseEntity<ApiResponse<Book>> addBook(@RequestBody Book book, HttpServletRequest request) {
		return ResponseEntity.ok(ResponseUtil.success(bookService.addBook(book), "Book Added", request.getRequestURI()));
	}
	
//	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@PostMapping("/books/upload")
	public ResponseEntity<ApiResponse<Object>> addBooks(@RequestParam("file") MultipartFile file,HttpServletRequest request) {
        File tempFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("fileName", tempFile.getAbsolutePath())
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(job, jobParameters);
            return ResponseEntity.ok(ResponseUtil.success(null, "Books Added sucessfully", request.getRequestURI()));
        } catch (IOException | org.springframework.batch.core.JobExecutionException e) {
            return ResponseEntity.ok(ResponseUtil.error(e.getMessage(),"An error occurred", 203,request.getRequestURI()));
        }
    }


	@DeleteMapping("/books")
//	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<ApiResponse<Object>> deleteBook(HttpServletRequest request) {
		bookService.deleteBook();
		return ResponseEntity.ok(ResponseUtil.success(null, "Book Deleted", request.getRequestURI()));
	}

	@GetMapping("/books/{bookId}")
	public ResponseEntity<ApiResponse<Book>> getByID(Long bookId, HttpServletRequest request) {
		Book book = bookService.getByID(bookId);
		return ResponseEntity.ok(ResponseUtil.success(book, null, request.getRequestURI()));
	}

//	@CrossOrigin("")http://localhost:52456/
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("books")
	public ResponseEntity<ApiResponse<List<Book>>> getBooksByTitle(@RequestParam(required = false) String title,
			HttpServletRequest request) {
		List<Book> books;
		if (title != null) {
			books = bookService.getBooksByTitle(title);
		} else {
			books = bookService.getAllBook();
		}
		return ResponseEntity.ok(ResponseUtil.success(books, null, request.getRequestURI()));
	}

	@GetMapping("books/published")
	public ResponseEntity<ApiResponse<List<Book>>> getPublishedBooks(HttpServletRequest request) {
		List<Book> books = bookService.getPublishedBooks();
		return ResponseEntity.ok(ResponseUtil.success(books, null, request.getRequestURI()));
	}
	@CrossOrigin(origins = "http://localhost:4200")
	@DeleteMapping("/books/{bookId}")
//	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<ApiResponse<Object>> deleteByID(Long bookId, HttpServletRequest request) {
		Book book = bookService.deleteByID(bookId);
		return ResponseEntity.ok(ResponseUtil.success(book, "Book Deleted", request.getRequestURI()));
	}

	@PutMapping("/books/{bookId}")
//	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<ApiResponse<Book>> getByID(@Valid @RequestBody Book book, @PathVariable Long bookId,
			HttpServletRequest request) {
		Book book1 = bookService.updateBook(bookId, book);
		return ResponseEntity.ok(ResponseUtil.success(book1, "Book Updated", request.getRequestURI()));
	}

}
