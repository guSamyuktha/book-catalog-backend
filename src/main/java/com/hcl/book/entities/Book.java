package com.hcl.book.entities;

import org.hibernate.validator.constraints.NotBlank;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Book {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	 private long id;       
	@Column(unique=true)
     private String title;  
     private String description;    
     private boolean published;
}
