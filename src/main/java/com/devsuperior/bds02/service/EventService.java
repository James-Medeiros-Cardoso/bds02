package com.devsuperior.bds02.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds02.dto.EventDTO;
import com.devsuperior.bds02.entities.City;
import com.devsuperior.bds02.entities.Event;
import com.devsuperior.bds02.repositories.EventRepository;
import com.devsuperior.bds02.services.exceptions.ResourceNotFoundException;

@Service
public class EventService {

	@Autowired
	private EventRepository eventRepository;
	
	/*@Transactional(readOnly=true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj=repository.findById(id);
		Product entity=obj.orElseThrow(() -> new ResourceNotFoundException("Entity not Found."));
		return new ProductDTO(entity, entity.getCategories());
	}*/

	/*@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity=new Product();
		copyDtoToEntity(dto, entity); //entity.setName(dto.getName());
		entity=repository.save(entity);
		return new ProductDTO(entity);
	}*/

	@Transactional
	public EventDTO update(Long id, EventDTO dto) {
		try {
			Event entity=eventRepository.getOne(id); //getOne para atualizar dados
			
			entity.setName(dto.getName());
			entity.setDate(dto.getDate());
			entity.setUrl(dto.getUrl());
			entity.setCity(new City(dto.getCityId(), null));
		
			entity=eventRepository.save(entity);
			return new EventDTO(entity);
		}
		catch(EntityNotFoundException e) { //EntityNotFoundException = exceção da JPA
			throw new ResourceNotFoundException("Id not found "+id);
		}
	}

	/*public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found "+id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}*/
}
