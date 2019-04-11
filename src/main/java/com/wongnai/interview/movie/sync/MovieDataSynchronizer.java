package com.wongnai.interview.movie.sync;

import javax.transaction.Transactional;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.external.MovieData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wongnai.interview.movie.MovieRepository;
import com.wongnai.interview.movie.external.MovieDataService;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

@Component
public class MovieDataSynchronizer {
	@Autowired
	private MovieDataService movieDataService;

	@Autowired
	private MovieRepository movieRepository;

	@Transactional
	public void forceSync() {
		//TODO: implement this to sync movie into repository

		Connection con = getConnection();
		if (con != null) {
			initData();
		}
	}

	// Get connection from in-memory database
	private Connection getConnection() {
		Connection con = null;
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
			con = DriverManager.getConnection("jdbc:hsqldb:mem:movies;sql.ignore_case=true", "SA", "");
			System.out.println("HSQLDB Connected");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return con;
	}

	// Remove outdated data and retrieve new movie data from movieDataService
	private void initData() {
		Stream<MovieData> moviesData = movieDataService.fetchAll().stream();
		movieRepository.deleteAll();
		moviesData.forEach(movie -> movieRepository.save(new Movie(movie.getTitle(), movie.getCast())));
	}
}
