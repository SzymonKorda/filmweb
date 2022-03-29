package com.example.services;

import com.example.mapper.ActorMapper;
import com.example.mapper.FilmMapper;
import com.example.specification.ActorSpecification;
import com.example.exceptions.ResourceNotFoundException;
import com.example.model.Actor;
import com.example.model.Film;
import com.example.payload.*;
import com.example.repositories.ActorRepository;
import com.example.repositories.FilmRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class ActorServiceImpl implements ActorService {

    private ActorRepository actorRepository;
    private FilmRepository filmRepository;

    public ActorServiceImpl(ActorRepository actorRepository, FilmRepository filmRepository) {
        this.actorRepository = actorRepository;
        this.filmRepository = filmRepository;
    }

    @Override
    public void newActor(NewActorRequest newActorRequest) {
        actorRepository.save(ActorMapper.mapNewActorRequestToActor(newActorRequest));
    }

    @Override
    public void deleteActor(Long actorId) {
        Actor actor = actorRepository.findById(actorId).orElseThrow(() -> new ResourceNotFoundException("Actor", "id", actorId));
        actorRepository.delete(actor);
    }

    @Override
    public void updateActor(Long actorId, ActorUpdateRequest actorUpdateRequest) {
        Actor actor = actorRepository.findById(actorId).orElseThrow(() -> new ResourceNotFoundException("Actor", "id", actorId));
        actorRepository.save(ActorMapper.mapActorUpdateRequestToActor(actorUpdateRequest, actor));
    }


    @Override
    public Page<SimpleActorResponse> getAllActors(ActorSpecification actorSpecification, Pageable pageable) {
        Page<Actor> actorsListPage = actorRepository.findAll(actorSpecification, pageable);
        return new PageImpl<>(actorsListPage
                .stream()
                .map(ActorMapper::mapActorToSimpleActorResponse)
                .collect(Collectors.toList()), pageable, actorsListPage.getTotalElements());
    }

    @Override
    public Page<SimpleFilmResponse> getActorFilms(Long actorId, Pageable pageable) {
        Actor actor = actorRepository.findById(actorId).orElseThrow(() -> new ResourceNotFoundException("Actor", "Id", actorId));
        Page<Film> actorsPageList = filmRepository.findAllByActors(actor, pageable);
        return new PageImpl<>(actorsPageList
                .stream()
                .map(FilmMapper::mapFilmToSimpleFilmResponse)
                .collect(Collectors.toList()), pageable, actorsPageList.getTotalElements());
    }

    @Override
    public FullActorResponse findActorById(Long actorId) {
        Actor actor = actorRepository.findById(actorId).orElseThrow(() -> new ResourceNotFoundException("Actor", "Id", actorId));
        return ActorMapper.mapActorToFullActorResponse(actor);
    }

    @Override
    public void addFilmToActor(Long actorId, Long filmId) {
        Actor actor = actorRepository.findById(actorId).orElseThrow(() -> new ResourceNotFoundException("Actor", "id", actorId));
        Film film = filmRepository.findById(filmId).orElseThrow(() -> new ResourceNotFoundException("Film", "id", filmId));
        actor.getFilms().add(film);
        film.getActors().add(actor);
        actorRepository.save(actor);
    }

    @Override
    public void deleteActorFilm(Long actorId, Long filmId) {
        Actor actor = actorRepository.findById(actorId).orElseThrow(() -> new ResourceNotFoundException("Actor", "Id", actorId));
        Film film = filmRepository.findById(filmId).orElseThrow(() -> new ResourceNotFoundException("Film", "Id", filmId));
        actor.getFilms().remove(film);
        film.getActors().remove(actor);
        actorRepository.save(actor);
    }

}
