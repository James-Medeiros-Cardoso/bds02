package com.devsuperior.bds02.dto.services.validations;

import com.devsuperior.bds02.controller.exceptions.FieldMessage;
import com.devsuperior.bds02.dto.UserInsertDTO;
import com.devsuperior.bds02.entities.User;
import com.devsuperior.bds02.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {
    // <(anotation criada), (classe que receberá a anotation)>
    // anottation UserInsertValid será aplicada na classe UserInsertDTO

    @Override
    public void initialize(UserInsertValid ann) {
    }

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {

        List<FieldMessage> list = new ArrayList<>();

        // Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à lista

        User user = userRepository.findByEmail(dto.getEmail()); // returna null de não encontrar
        if (user != null) {
            list.add(new FieldMessage("email", "O email " + dto.getEmail() + " email já existe."));
        }

        for (FieldMessage e : list) { // inserir na lista de erros do bean validation
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}