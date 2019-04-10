package com.wongnai.interview.movie.external;

import java.util.ArrayList;
import java.util.List;

public class MoviesResponse extends ArrayList<MovieData> {
    public MoviesResponse(List<MovieData> movies) {
        this.addAll(movies);
    }
}
