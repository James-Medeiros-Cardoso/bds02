package com.devsuperior.bds02.service;

import com.devsuperior.bds02.dto.EventDTO;
import com.devsuperior.bds02.entities.Event;
import com.devsuperior.bds02.repositories.CityRepository;
import com.devsuperior.bds02.repositories.EventRepository;
import com.devsuperior.bds02.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CityRepository cityRepository;

    @Transactional(readOnly = true)
    public Page<EventDTO> findAllPaged(Pageable pageable) {
        Page<Event> list = eventRepository.findAll(pageable); //findAllPaged = retorna uma página (Page)
        return list.map(x -> new EventDTO(x));
    }

    @Transactional(readOnly = true)
    public EventDTO findById(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "EventService/Entity not found. Id = " + id));
        return new EventDTO(event);
    }

    @Transactional
    public EventDTO insert(EventDTO eventDto) {
        Event event = new Event();
        copyDtoToEntity(eventDto, event);
        event = eventRepository.save(event);
        return new EventDTO(event);
    }

    @Transactional
    public EventDTO update(Long id, EventDTO eventDto) {
        try {
            Event event = eventRepository.getOne(id); //getOne para atualizar dados
            copyDtoToEntity(eventDto, event);
            event = eventRepository.save(event);
            return new EventDTO(event);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("EventService/Entity not found. Id = " + id);
        }
    }

    public void delete(Long id) {
        try {
            eventRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("EventService/Entity not found. Id = " + id);
        }
    }

    private void copyDtoToEntity(EventDTO eventDto, Event event) {
        event.setName(eventDto.getName());
        event.setDate(eventDto.getDate());
        event.setUrl(eventDto.getUrl());
        event.setCity(cityRepository.findById(eventDto.getCityId()).orElseThrow(
                () -> new ResourceNotFoundException("EventService/Entity not found. Id = " + eventDto.getCityId())
        ));
    }
}