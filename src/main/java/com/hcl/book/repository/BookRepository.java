package com.hcl.book.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcl.book.entities.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{

	List<Book> findAllByPublished(boolean b);
	List<Book> findByTitleContainingIgnoreCase(String title);


}
