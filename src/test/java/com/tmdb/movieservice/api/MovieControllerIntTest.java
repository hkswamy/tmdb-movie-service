package com.tmdb.movieservice.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.tools.javac.util.List;
import com.tmdb.movieservice.model.Movie;
import com.tmdb.movieservice.repo.MovieRepository;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerIntTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MovieRepository movieRepository;

    @BeforeEach
    void cleanUp() {
        movieRepository.deleteAllInBatch();
    }

    @Test
    void givenMovie_whenCreateMovie_thenReturnSavedMovie() throws Exception {
        //given
        Movie movie = new Movie();
        movie.setName("rrr");
        movie.setDirector("SS Rajamouli");
        movie.setActors(List.of("ntr", "ramcharan", "aliabhutt"));

        //when create movie
        var response = mockMvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movie))
        );
        // then verify saved service
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.name",is(movie.getName())))
                .andExpect(jsonPath("$.director",is(movie.getDirector())))
                .andExpect(jsonPath("$.actors",is(movie.getActors())));

    }
    @Test
    void givenMovieId_whenFetchMovie_thenReturnMovie() throws Exception {
        Movie movie = new Movie();
        movie.setName("rrr");
        movie.setDirector("SS Rajamouli");
        movie.setActors(List.of("ntr", "ramcharan", "aliabhutt"));
        Movie savedMovie = movieRepository.save(movie);

        //Fetch movie
        var response = mockMvc.perform(get("/movies/" + savedMovie.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedMovie.getId().intValue())))
                .andExpect(jsonPath("$.name",is(savedMovie.getName())))
                .andExpect(jsonPath("$.director",is(savedMovie.getDirector())))
                .andExpect(jsonPath("$.actors",is(savedMovie.getActors())));
    }

    @Test
    void givenSavedMovie_whenUpdatedMovie_thenMovieUpdatedInDb() throws Exception {
        Movie movie = new Movie();
        movie.setName("rrr");
        movie.setDirector("SS Rajamouli");
        movie.setActors(List.of("ntr", "ramcharan", "aliabhutt"));
        Movie savedMovie = movieRepository.save(movie);
        Long id = savedMovie.getId();

        //update movie
        movie.setActors(List.of("ntr", "ramcharan", "aliabhutt","kumaraswami"));
        var response = mockMvc.perform(put("/movies/" + id).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movie))
        );
        response.andDo(print())
                .andExpect(status().isOk());
        var fetchResponse = mockMvc.perform(get("/movies/" + id));
        fetchResponse.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is(movie.getName())))
                .andExpect(jsonPath("$.actors",is(movie.getActors())))
                .andExpect(jsonPath("$.director",is(movie.getDirector())));
    }

    @Test
    void givenMovie_whenDeleteMovieRequest_thenMovieDeletedFromDB() throws Exception {
        Movie movie = new Movie();
        movie.setName("rrr");
        movie.setDirector("SS Rajamouli");
        movie.setActors(List.of("ntr", "ramcharan", "aliabhutt"));
        Movie savedMovie = movieRepository.save(movie);
        Long id = savedMovie.getId();

        mockMvc.perform(delete("/movies/" + id))
                .andDo(print())
                .andExpect(status().isOk());
        assertFalse(movieRepository.findById(id).isPresent());
    }
}