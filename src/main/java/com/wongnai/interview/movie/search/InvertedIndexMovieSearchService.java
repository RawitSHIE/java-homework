package com.wongnai.interview.movie.search;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieRepository;
import com.wongnai.interview.movie.MovieSearchService;

@Component("invertedIndexMovieSearchService")
@DependsOn("movieDatabaseInitializer")
public class InvertedIndexMovieSearchService implements MovieSearchService {
	@Autowired
	private MovieRepository movieRepository;

	@Override
	public List<Movie> search(String queryText) {
		//TODO: Step 4 => Please implement in-memory inverted index to search movie by keyword.
		// You must find a way to build inverted index before you do an actual search.
		// Inverted index would looks like this:
		// -------------------------------
		// |  Term      | Movie Ids      |
		// -------------------------------
		// |  Star      |  5, 8, 1       |
		// |  War       |  5, 2          |
		// |  Trek      |  1, 8          |
		// -------------------------------
		// When you search with keyword "Star", you will know immediately, by looking at Term column, and see that
		// there are 3 movie ids contains this word -- 1,5,8. Then, you can use these ids to find full movie object from repository.
		// Another case is when you find with keyword "Star War", there are 2 terms, Star and War, then you lookup
		// from inverted index for Star and for War so that you get movie ids 1,5,8 for Star and 2,5 for War. The result that
		// you have to return can be union or intersection of those 2 sets of ids.
		// By the way, in this assignment, you must use intersection so that it left for just movie id 5.

		HashMap<String, HashSet<Long>> index = new HashMap<>();
		List<Movie> movies = movieRepository.getAll();
		for (Movie m: movies) {
			Supplier<Stream<String>> words = () -> Stream.of(m.getName().toLowerCase().split(" "));
			words.get().filter(w -> !index.containsKey(w)).forEach(w -> index.put(w, new HashSet<Long>()));
			words.get().forEach(w -> index.get(w).add(m.getId()));
		}
		Stream<String> queryWords = Arrays.stream(queryText.toLowerCase().split(" "));
		List<HashSet<Long>> setList = new ArrayList<>(Collections.emptyList());
		queryWords.filter(word -> index.containsKey(word)).forEach(word -> setList.add(index.get(word)));
		List<Movie> findingResult = new ArrayList<>(Collections.emptyList());
		if (!setList.isEmpty()) {
			findingResult = movieRepository.findByIdIn(intersect(setList));
		}
		return findingResult;
	}

	// Find intersection of n HashSet
	private List<Long> intersect(List<HashSet<Long>> setList) {
		if (setList.isEmpty()) {
			System.out.println("Set of list is empty");
			return null;
		}
		Iterator<HashSet<Long>> it = setList.iterator();
		HashSet<Long> result = new HashSet<>(it.next());
		while (it.hasNext()) {
			result.retainAll(it.next());
		}
		return new ArrayList<>(result);
	}
}
