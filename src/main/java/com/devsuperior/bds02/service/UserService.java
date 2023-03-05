package com.devsuperior.bds02.service;

import com.devsuperior.bds02.dto.RoleDTO;
import com.devsuperior.bds02.dto.UserDTO;
import com.devsuperior.bds02.dto.UserInsertDTO;
import com.devsuperior.bds02.dto.UserUpdateDTO;
import com.devsuperior.bds02.entities.Role;
import com.devsuperior.bds02.entities.User;
import com.devsuperior.bds02.repositories.RoleRepository;
import com.devsuperior.bds02.repositories.UserRepository;
import com.devsuperior.bds02.service.exceptions.DataBaseException;
import com.devsuperior.bds02.service.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable) {
        Page<User> list = userRepository.findAll(pageable);
        return list.map(x -> new UserDTO(x));
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        Optional<User> obj = userRepository.findById(id);
        User user = obj.orElseThrow(() -> new ResourceNotFoundException("UserService/Entity not Found. Id = " + id));
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO userDto) {
        User user = new User();
        copyDtoToEntity(userDto, user);
        user = userRepository.save(user);
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO userDto) {
        try {
            User user = userRepository.getOne(id);
            copyDtoToEntity(userDto, user);
            user = userRepository.save(user);
            return new UserDTO(user);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("UserService/Entity not Found. Id = " + id);
        }
    }

    //método DELETE = não precisa do Transactional
    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("UserService/Entity not Found. Id = " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("UserService/Integrity violation.");
        }
    }

    private void copyDtoToEntity(UserDTO userDto, User user) {
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());

        user.getRoles().clear();
        for (RoleDTO roleDto : userDto.getRoles()) {
            Role role = roleRepository.findById(roleDto.getId()).orElseThrow(
                    () -> new ResourceNotFoundException("UserService/Entity Role not Found. Id = " + roleDto.getId()));
            user.getRoles().add(role);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            logger.error("User not found: " + username);
            throw new UsernameNotFoundException("UserService/Email " + username + " not found.");
        }
        logger.info("UserService/User was found: " + username);
        return user;
    }
}
