package com.tmdb.movieservice.api;

import com.tmdb.movieservice.model.Movie;
import com.tmdb.movieservice.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies")
@Slf4j
public class MovieController {
    @Autowired
    private MovieService movieService;
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovie(@PathVariable Long id) {
        Movie movie = movieService.read(id);
        log.info("Get Movie with id: {}", movie.getId());
        return ResponseEntity.ok(movie);
    }

    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        Movie createMovie = movieService.create(movie);
        log.info("Movie created with id: {}", movie.getId());
        return ResponseEntity.ok(createMovie);
    }

    @PutMapping("/{id}")
    public void updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
        log.info("Movie updated with id: {}", id);
        movieService.update(id, movie);
    }
    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id) {
        log.info("Deleted movie for id: {}", id);
        movieService.delete(id);
    }
}
