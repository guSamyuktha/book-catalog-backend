package com.hcl.book.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.PlatformTransactionManager;

import com.hcl.book.entities.Book;

@Configuration
public class JobConfig {

    @Bean
    @StepScope
    public FlatFileItemReader<Book> reader(@Value("#{jobExecutionContext['fileName']}") String fileName) {
        return new FlatFileItemReaderBuilder<Book>()
                .name("Reader")
                .resource(new FileSystemResource(fileName))
                .strict(true) 
                .lineMapper(new DefaultLineMapper<Book>() {
                    {
                        setLineTokenizer(new DelimitedLineTokenizer() {
                            {
                                setNames("title", "description", "published");
                            }
                        });
                        setFieldSetMapper(fieldSet -> {
                            Book book = new Book();
                            book.setTitle(fieldSet.readString("title"));
                            book.setDescription(fieldSet.readString("description"));
                            book.setPublished(fieldSet.readBoolean("published"));
                            return book;
                        });
                    }
                })
                .build();
    }


    @Bean
    public JdbcBatchItemWriter<Book> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Book>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO book (description, published, title) VALUES (:description, :published, :title)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager manager,
                     FlatFileItemReader<Book> reader, JdbcBatchItemWriter<Book> writer) {
        return new StepBuilder("step1", jobRepository)
                .<Book, Book>chunk(2, manager)
                .reader(reader)
                .writer(writer)
                .faultTolerant()
                .skip(DuplicateKeyException.class)
                .build();
    }

    @Bean
    public Job job(JobRepository jobRepository, Step step, JobCompletionNotificationListener listener) {
        return new JobBuilder("newJob", jobRepository)
                .listener(listener)
                .start(step)
                .build();
    }
}
