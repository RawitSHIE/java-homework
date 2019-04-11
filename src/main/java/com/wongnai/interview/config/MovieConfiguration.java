package com.wongnai.interview.config;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Configuration
public class MovieConfiguration {
    @Autowired
    private MovieRepository movieRepository;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean("invertedIndexMovieDatabase")
    public HashMap invertedIndexMovieDatabase() {
        HashMap<String, Set<Long>> index = new HashMap<>();
        Stream<Movie> movies = movieRepository.getAll().stream();
        movies.forEach(movie -> {
            Supplier<Stream<String>> words = () -> Stream.of(movie.getName().toLowerCase().split(" "));
            words.get()
                    .filter(w -> !index.containsKey(w))
                    .forEach(w -> index.put(w, new HashSet<>()));
            words.get()
                    .forEach(w -> index.get(w).add(movie.getId()));
        });
        return index;
    }
}
