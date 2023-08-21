package com.tmdb.movieservice.service;

import com.tmdb.movieservice.MovieServiceApplication;
import com.tmdb.movieservice.model.Movie;
import com.tmdb.movieservice.repo.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    //CRUD Operations - Create, Read, Update and Delete operations

    public Movie create(Movie movie) {
        if (movie == null) {
            throw new RuntimeException("Invalid Movie");
        }
        return movieRepository.save(movie);
    }

    public Movie read(Long id) {
        return movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found"));
    }

    public void update(Long id, Movie updateMovie) {
        if (updateMovie ==null || id == null) {
            throw new RuntimeException("Invalid Movie");
        }
        if(movieRepository.existsById(id)) {
            Movie movie = movieRepository.getReferenceById(id);
            movie.setName(updateMovie.getName());
            movie.setDirector(updateMovie.getDirector());
            movie.setActors(updateMovie.getActors());
            movieRepository.save(movie);
        } else {
            throw new RuntimeException("Movie not found");
        }
    }

    public void delete(Long id) {
        if(movieRepository.existsById(id)) {
             movieRepository.deleteById(id);
        } else {
            throw new RuntimeException("Movie not found");
        }
    }
}
