package com.nttdataTest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nttdataTest.dao.UsersDAO;
import com.nttdataTest.dto.PhoneDTO;
import com.nttdataTest.dto.UserDTO;
import com.nttdataTest.dto.UserResponseDTO;
import com.nttdataTest.dto.UserUpdateDTO;
import com.nttdataTest.model.Phone;
import com.nttdataTest.model.User;

import java.util.*;

import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;

@RestController
public class UsersController {
    
    @Autowired
    private UsersDAO usersDAO;

    @GetMapping("/users")
    public List<User> getusers() {
        return (List<User>) usersDAO.findAll();
    }

    @GetMapping("/users/{userId}")
    public User getUser(@PathVariable Long userId) throws ResponseStatusException {
        return usersDAO.findById(userId).orElseThrow(() -> { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "{\"mensaje\": \"Usuario no encontrado\"}"); } );
    }

    @PostMapping("/users")
    public UserResponseDTO addUser(@RequestBody UserDTO userDTO) throws JsonProcessingException {
        if (Objects.isNull(userDTO.getNombre()) || userDTO.getNombre().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Nombre requerido\"}");
        }
        if (Objects.isNull(userDTO.getCorreo()) || userDTO.getCorreo().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Email requerido\"}");
        }
        if (userDTO.getCorreo().matches("\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Email no sigue el formato correcto\"}");
        }
        List<User> users = (List<User>) usersDAO.findAll();
        for (User userDb : users) {
            if (userDTO.getCorreo().equals(userDb.getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"El correo ya está registrado\"}");
            }
        }
        if (Objects.isNull(userDTO.getContrasena()) || userDTO.getContrasena().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Contraseña requerida\"}");
        }
        if (userDTO.getCorreo().matches("[A-Za-z0-9]+[0-9][0-9]")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Contraseña no sigue el formato correcto\"}");
        }
        if (Objects.isNull(userDTO.getTelefonos()) || userDTO.getTelefonos().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Teléfonos requeridos\"}");
        }
        int i = 1;
        for (PhoneDTO phone : userDTO.getTelefonos()) {
            if (Objects.isNull(phone.getNumero()) || phone.getNumero() == 0L) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Número de teléfono " + Integer.toString(i) + " requerido\"}");
            }
            if (Objects.isNull(phone.getCodigoCiudad()) || phone.getCodigoCiudad() == 0L) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Código de ciudad de teléfono " + Integer.toString(i) + " requerido\"}");
            }
            if (Objects.isNull(phone.getCodigoPais()) || phone.getCodigoPais() == 0L) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Código de país de teléfono " + Integer.toString(i) + " requerido\"}");
            }
            i++;
        }
        User user = new User();
        user.setName(userDTO.getNombre());
        user.setEmail(userDTO.getCorreo());
        user.setPassword(userDTO.getContrasena());
        List<Phone> userPhones = new ArrayList<>();
        for (PhoneDTO phone : userDTO.getTelefonos()) {
            Phone userPhone = new Phone();
            userPhone.setNumber(phone.getNumero());
            userPhone.setCityCode(phone.getCodigoCiudad());
            userPhone.setCountryCode(phone.getCodigoPais());
            userPhone.setUser(user);
            userPhones.add(userPhone);
        }
        user.setPhones(userPhones);
        Date createdAt = new Date();
        user.setCreatedAt(createdAt);
        user.setModifiedAt(createdAt);
        user.setLastLogin(createdAt);
        user.setIsActive(true);
        SecretKey key = Jwts.SIG.HS256.key().build();
        String jws = Jwts.builder().subject("nttdataTest").claim("name",userDTO.getNombre()).signWith(key).compact();
        user.setToken(jws);
        usersDAO.save(user);
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setCreado(user.getCreatedAt());
        userResponseDTO.setModificado(user.getModifiedAt());
        userResponseDTO.setUltimoLogin(user.getLastLogin());
        userResponseDTO.setToken(user.getToken());
        userResponseDTO.setActivo(user.getIsActive());
        return userResponseDTO;
    }
    
    @PutMapping("/users/{userId}")
    public UserResponseDTO putUser(@RequestBody UserUpdateDTO userDTO, @PathVariable Long userId) throws JsonProcessingException, ResponseStatusException {
        Optional<User> dbUser = usersDAO.findById(userId);
        if (dbUser.isPresent())
        {
            if (Objects.isNull(userDTO.getNombre()) || userDTO.getNombre().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Nombre requerido\"}");
            }
            if (Objects.isNull(userDTO.getCorreo()) || userDTO.getCorreo().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Email requerido\"}");
            }
            if (userDTO.getCorreo().matches("\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Email no sigue el formato correcto\"}");
            }
            List<User> users = (List<User>) usersDAO.findAll();
            for (User userDb : users) {
                if (userDTO.getCorreo().equals(userDb.getEmail()) && !userDb.getEmail().equals(dbUser.get().getEmail())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"El correo ya está registrado\"}");
                }
            }
            if (Objects.isNull(userDTO.getContrasena()) || userDTO.getContrasena().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Contraseña requerida\"}");
            }
            if (userDTO.getCorreo().matches("[A-Za-z0-9]+[0-9][0-9]")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Contraseña no sigue el formato correcto\"}");
            }
            if (Objects.isNull(userDTO.getTelefonos()) || userDTO.getTelefonos().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Teléfonos requeridos\"}");
            }
            int i = 1;
            for (PhoneDTO phone : userDTO.getTelefonos()) {
                if (Objects.isNull(phone.getNumero()) || phone.getNumero() == 0L) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Número de teléfono " + Integer.toString(i) + " requerido\"}");
                }
                if (Objects.isNull(phone.getCodigoCiudad()) || phone.getCodigoCiudad() == 0L) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Código de ciudad de teléfono " + Integer.toString(i) + " requerido\"}");
                }
                if (Objects.isNull(phone.getCodigoPais()) || phone.getCodigoPais() == 0L) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "mensaje\": \"Código de país de teléfono " + Integer.toString(i) + " requerido\"}");
                }
                i++;
            }
            User user = dbUser.get();
            user.setName(userDTO.getNombre());
            user.setEmail(userDTO.getCorreo());
            user.setPassword(userDTO.getContrasena());
            List<Phone> userPhones = new ArrayList<>();
            for (PhoneDTO phone : userDTO.getTelefonos()) {
                Phone userPhone = new Phone();
                userPhone.setNumber(phone.getNumero());
                userPhone.setCityCode(phone.getCodigoCiudad());
                userPhone.setCountryCode(phone.getCodigoPais());
                userPhone.setUser(user);
                userPhones.add(userPhone);
            }
            user.setPhones(userPhones);
            user.setModifiedAt(new Date());
            user.setIsActive(userDTO.isActivo());
            if (user.getToken().equals(userDTO.getToken()))
            {
                usersDAO.save(user);
                UserResponseDTO userResponseDTO = new UserResponseDTO();
                userResponseDTO.setId(user.getId());
                userResponseDTO.setCreado(user.getCreatedAt());
                userResponseDTO.setModificado(user.getModifiedAt());
                userResponseDTO.setUltimoLogin(user.getLastLogin());
                userResponseDTO.setToken(user.getToken());
                userResponseDTO.setActivo(user.getIsActive());
                return userResponseDTO;
            }
            else
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "{\"mensaje\": \"Token inválido\"}");
            }
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "{\"mensaje\": \"Usuario no encontrado\"}");
        }
    }

    @PatchMapping("/users/{userId}")
    public UserResponseDTO patchUser(@RequestBody UserUpdateDTO userDTO, @PathVariable Long userId) throws JsonProcessingException, ResponseStatusException {
        Optional<User> dbUser = usersDAO.findById(userId);
        if (dbUser.isPresent())
        {
            if (Objects.isNull(userDTO.getNombre()) || userDTO.getNombre().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Nombre requerido\"}");
            }
            if (Objects.isNull(userDTO.getCorreo()) || userDTO.getCorreo().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Email requerido\"}");
            }
            if (userDTO.getCorreo().matches("\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Email no sigue el formato correcto\"}");
            }
            List<User> users = (List<User>) usersDAO.findAll();
            for (User userDb : users) {
                if (userDTO.getCorreo().equals(userDb.getEmail()) && !userDb.getEmail().equals(dbUser.get().getEmail())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"El correo ya está registrado\"}");
                }
            }
            if (Objects.isNull(userDTO.getContrasena()) || userDTO.getContrasena().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Contraseña requerida\"}");
            }
            if (userDTO.getCorreo().matches("[A-Za-z0-9]+[0-9][0-9]")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Contraseña no sigue el formato correcto\"}");
            }
            if (Objects.isNull(userDTO.getTelefonos()) || userDTO.getTelefonos().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Teléfonos requeridos\"}");
            }
            int i = 1;
            for (PhoneDTO phone : userDTO.getTelefonos()) {
                if (Objects.isNull(phone.getNumero()) || phone.getNumero() == 0L) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Número de teléfono " + Integer.toString(i) + " requerido\"}");
                }
                if (Objects.isNull(phone.getCodigoCiudad()) || phone.getCodigoCiudad() == 0L) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "{\"mensaje\": \"Código de ciudad de teléfono " + Integer.toString(i) + " requerido\"}");
                }
                if (Objects.isNull(phone.getCodigoPais()) || phone.getCodigoPais() == 0L) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "mensaje\": \"Código de país de teléfono " + Integer.toString(i) + " requerido\"}");
                }
                i++;
            }
            User user = dbUser.get();
            if (!userDTO.getNombre().isEmpty())
            {
                user.setName(userDTO.getNombre());
            }
            if (!userDTO.getCorreo().isEmpty())
            {
                user.setEmail(userDTO.getCorreo());
            }
            if (!userDTO.getContrasena().isEmpty())
            {
                user.setPassword(userDTO.getContrasena());
            }
            List<Phone> userPhones = new ArrayList<>();
            for (PhoneDTO phone : userDTO.getTelefonos()) {
                if (phone.getNumero() != 0L && phone.getCodigoCiudad() != 0L && phone.getCodigoPais() != 0L)
                {
                    Phone userPhone = new Phone();
                    userPhone.setNumber(phone.getNumero());
                    userPhone.setCityCode(phone.getCodigoCiudad());
                    userPhone.setCountryCode(phone.getCodigoPais());
                    userPhone.setUser(user);
                    userPhones.add(userPhone);
                }
            }
            if (!userPhones.isEmpty())
            {
                user.setPhones(userPhones);
            }
            user.setModifiedAt(new Date());
            user.setIsActive(userDTO.isActivo());
            if (user.getToken().equals(userDTO.getToken()))
            {
                usersDAO.save(user);
                UserResponseDTO userResponseDTO = new UserResponseDTO();
                userResponseDTO.setId(user.getId());
                userResponseDTO.setCreado(user.getCreatedAt());
                userResponseDTO.setModificado(user.getModifiedAt());
                userResponseDTO.setUltimoLogin(user.getLastLogin());
                userResponseDTO.setToken(user.getToken());
                userResponseDTO.setActivo(user.getIsActive());
                return userResponseDTO;
            }
            else
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "{\"mensaje\": \"Token inválido\"}");
            }
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "{\"mensaje\": \"Usuario no encontrado\"}");
        }
    }
    
    @DeleteMapping("/users/{userId}")
    void deleteUser(@PathVariable Long userId) throws ResponseStatusException {
        Optional<User> dbUser = usersDAO.findById(userId);
        if (dbUser.isPresent())
        {
            usersDAO.deleteById(userId);
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "{\"mensaje\": \"Usuario no encontrado\"}");
        }
    }
}
