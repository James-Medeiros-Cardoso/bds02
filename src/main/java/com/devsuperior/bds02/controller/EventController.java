package com.devsuperior.bds02.controller;

import com.devsuperior.bds02.dto.EventDTO;
import com.devsuperior.bds02.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/events")
public class EventController {

    @Autowired
    private EventService service;

    @GetMapping
    public ResponseEntity<Page<EventDTO>> findAll(Pageable pageable

                                                  //Parâmetros: page, size, sort
                                                  //page=page, size=linesPerPage, sort=Direction.valueOf(direction), orderBy
			
			/*@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction,	
			@RequestParam(value = "orderBy", defaultValue = "name") String orderBy*/
    ) {
        //PageRequest pageRequest=PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);

        Page<EventDTO> list = service.findAllPaged(pageable);

        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EventDTO> findById(@PathVariable Long id) {
        EventDTO eventDto = service.findById(id);
        return ResponseEntity.ok().body(eventDto);
    }

    @PostMapping
    public ResponseEntity<EventDTO> insert(@RequestBody EventDTO eventDto) {
        eventDto = service.insert(eventDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(eventDto.getId()).toUri();
        return ResponseEntity.created(uri).body(eventDto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<EventDTO> update(@PathVariable Long id, @RequestBody EventDTO dto) {
        dto = service.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
