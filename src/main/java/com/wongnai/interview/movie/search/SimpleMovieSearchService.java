package com.wongnai.interview.movie.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.wongnai.interview.movie.external.MovieData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieSearchService;
import com.wongnai.interview.movie.external.MovieDataService;

@Component("simpleMovieSearchService")
public class SimpleMovieSearchService implements MovieSearchService {
	@Autowired
	private MovieDataService movieDataService;

	@Override
	public List<Movie> search(String queryText) {
		//TODO: Step 2 => Implement this method by using data from MovieDataService
		// All test in SimpleMovieSearchServiceIntegrationTest must pass.
		// Please do not change @Component annotation on this class

		List<Movie> searchedMovies = new ArrayList<>();
		Stream<MovieData> moviesData = movieDataService.fetchAll().stream();
		moviesData.forEach(movie -> {
			Stream<String> words = Arrays.stream(movie.getTitle().split(" "));
			if (words.anyMatch(word -> word.equalsIgnoreCase(queryText))) {
				Movie temp = new Movie(movie.getTitle(), movie.getCast());
				searchedMovies.add(temp);
			}
		});
		return searchedMovies;
	}
}
