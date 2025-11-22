package com.elearning.e_learning_core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.elearning.e_learning_core.Dtos.ApiResponse;
import com.elearning.e_learning_core.Dtos.AuthenticationDTO;
import com.elearning.e_learning_core.Dtos.LoginResponseDTO;
import com.elearning.e_learning_core.Dtos.UserRegisterDTO;
import com.elearning.e_learning_core.Repository.PersonRepository;
import com.elearning.e_learning_core.Repository.UserRepository;
import com.elearning.e_learning_core.exceptions.InvalidRoleException;
import com.elearning.e_learning_core.exceptions.UserAlreadyExistsException;
import com.elearning.e_learning_core.model.Experience;
import com.elearning.e_learning_core.model.Instructor;
import com.elearning.e_learning_core.model.Person;
import com.elearning.e_learning_core.model.Student;
import com.elearning.e_learning_core.model.Usr;

@Service
public class AuthenticationService {
    @Autowired
    private final UserRepository usuarioRepository;
    @Autowired
    private final PersonRepository personRepository;
    @Autowired
    AuthenticationManager authenticatorManager;

    @Autowired
    TokenService tokenService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository usuarioRepository, PersonRepository personRepository) {
        this.usuarioRepository = usuarioRepository;
        this.personRepository = personRepository;
    }

    public ApiResponse<?> authenticateUser(AuthenticationDTO usuarioInsertDTO) {
        var auth = this.authenticatorManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        usuarioInsertDTO.getEmail(),
                        usuarioInsertDTO.getPassword()));

        Usr usuario = (Usr) auth.getPrincipal();

        String token = tokenService.generateToken(usuario);

        return new ApiResponse<>(
                "success",
                "Usuário autenticado com sucesso",
                200,
                new LoginResponseDTO(
                        token,
                        usuario.getEmail(),
                        usuario.getRole().getRoleName(), usuario.getPerson().getId()));
    }

    public ApiResponse<?> registerUser(UserRegisterDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException("Email já existe");
        }

        Person person;

        switch (dto.getRole().getRoleName()) {
            case "ROLE_STUDENT":
                person = new Student();
                break;
            case "ROLE_INSTRUCTOR":
                person = new Instructor();
                break;
            default:
                throw new InvalidRoleException("Tipo de usuário inválido: " + dto.getRole().getRoleName());
        }

        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setUserName(dto.getUserName());
        person.setPhoneNumber(dto.getPhoneNumber());
        person.setEmail(dto.getEmail());
        person.setGender(dto.getGender());
        person.setDob(dto.getDob());

        if (person instanceof Instructor instructor) {
            if (dto.getExperiences() != null) {
                List<Experience> experienceList = dto.getExperiences().stream().map(e -> {
                    Experience exp = new Experience();
                    exp.setJobTitle(e.getJobTitle());
                    exp.setCompany(e.getCompany());
                    exp.setStartDate(e.getStartDate());
                    exp.setEndDate(e.getEndDate());
                    exp.setCurrent(e.getCurrent());
                    exp.setInstructor(instructor);
                    return exp;
                }).toList();
                instructor.setExperiences(experienceList);
            }
            instructor.setBio(dto.getBio() != null ? dto.getBio() : "");
        }

        person = personRepository.save(person);

        Usr user = usuarioRepository.save(new Usr(
                person.getEmail(),
                passwordEncoder.encode(dto.getPassword()), true,
                dto.getRole(),
                person));

        return new ApiResponse<>("success", "Usuário registrado com sucesso", 201, user);
    }

}
