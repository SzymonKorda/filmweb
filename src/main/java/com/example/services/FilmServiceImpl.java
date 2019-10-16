package com.example.services;

import com.example.exceptions.NotFoundException;
import com.example.model.Film;
import com.example.repositories.FilmRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FilmServiceImpl implements FilmService {

    private FilmRepository filmRepository;

    public FilmServiceImpl(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Override
    public Page<Film> findAllFilms(Pageable pageable) {
        return filmRepository.findAll(pageable);
    }

    @Override
    public Film newFilm(Film film) {
        return filmRepository.save(film);
    }

    @Override
    public Film updateFilm(Long filmId, Film filmUpdated) {
        return filmRepository.findById(filmId).map(film -> {
            film.setTitle(filmUpdated.getTitle());
            film.setBoxoffice(filmUpdated.getBoxoffice());
            film.setDuration(filmUpdated.getDuration());
            return filmRepository.save(film);
        }).orElseThrow(() -> new NotFoundException("Film not found with id:" + filmId));
    }

    @Override
    public void deleteFilmById(Long filmId) {
        filmRepository.deleteById(filmId);

//        @DeleteMapping("/questions/{questionId}")
//        public ResponseEntity<?> deleteQuestion(@PathVariable Long questionId) {
//            return questionRepository.findById(questionId)
//                    .map(question -> {
//                        questionRepository.delete(question);
//                        return ResponseEntity.ok().build();
//                    }).orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + questionId));
//        }
    }

//    @Override
//    public Film findById(Long filmId) {
//        Optional<Film> filmOptional = filmRepository.findById(filmId);
//
//        if(!filmOptional.isPresent()) {
//            throw new NotFoundException("Recipe Not Found for ID: " + filmId);
//        }
//
//        return filmOptional.get();
//    }

}