package com.example;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(transactional = false)
public class BookControllerTest {
    @Inject
    @Client("/")
    HttpClient httpClient;

    @Inject
    BookRepository bookRepository;

    @Test
    void bookCount() {
        String title = "Building Microservices";
        String isbn = "1491950358";
        Book book = bookRepository.save(new Book(isbn, title));
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = HttpRequest.GET("/books/count")
                .accept(MediaType.TEXT_PLAIN);
        Integer count = client.retrieve(request, Integer.class);
        assertEquals(1, count);
        bookRepository.delete(book);
    }
}