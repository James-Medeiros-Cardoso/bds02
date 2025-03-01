package com.devsuperior.bds02.service;

import com.devsuperior.bds02.dto.CityDTO;
import com.devsuperior.bds02.entities.City;
import com.devsuperior.bds02.repositories.CityRepository;
import com.devsuperior.bds02.services.exceptions.DatabaseException;
import com.devsuperior.bds02.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {

    @Autowired
    private CityRepository repository;

    @Transactional(readOnly = true)
    public CityDTO findById(Long id) {
        City city = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "CityService/Entity not found. Id = " + id));
        return new CityDTO(city);
    }

    @Transactional(readOnly = true)
    public List<CityDTO> findAll() {
        List<City> cities = repository.findAll();
        return cities.stream().map(city -> new CityDTO(city)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CityDTO> findAllSortByName() {
        List<City> list = repository.findAll(Sort.by("name")); //Sort.by("name")=ordenado por nome
        return list.stream().map(x -> new CityDTO(x)).collect(Collectors.toList());
    }

    @Transactional
    public CityDTO insert(CityDTO dto) {

        City entity = new City();
        entity.setName(dto.getName());

        //entity.setDepartment(new Department(dto.getDepartmentId(), null));
        //entity.setEvent(new Event(dto.getCityId(), null));

        entity = repository.save(entity);
        return new CityDTO(entity);
    }

    @Transactional
    public CityDTO update(Long id, CityDTO cityDto) {
        try {
            City city = repository.getOne(id);
            city.setName(cityDto.getName());
            city = repository.save(city);
            return new CityDTO(city);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("CityService/Entity not found. Id = " + id);
        }
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }
}