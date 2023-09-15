package com.example;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest;
import jakarta.inject.Inject
import spock.lang.Specification;

@MicronautTest()
class BookControllerSpec extends Specification {
    @Inject
    @Client("/")
    HttpClient httpClient;

    @Inject
    BookRepository bookRepository;

    def bookCount() {
        setup:
        String title = "Building Microservices";
        String isbn = "1491950358";
        Book book = bookRepository.save(new Book(isbn, title));

        when:
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = HttpRequest.GET("/books/count")
                .accept(MediaType.TEXT_PLAIN);
        Integer count = client.retrieve(request, Integer.class);

        then:
        1 == count
        bookRepository.delete(book);
    }
}