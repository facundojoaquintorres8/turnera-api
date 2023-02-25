package com.f8.turnera.security.services;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.f8.turnera.config.SecurityConstants;
import com.f8.turnera.security.entities.Permission;
import com.f8.turnera.security.entities.Profile;
import com.f8.turnera.security.entities.User;
import com.f8.turnera.security.models.LoginDTO;
import com.f8.turnera.security.models.SessionUserDTO;
import com.f8.turnera.security.models.UserDTO;
import com.f8.turnera.security.repositories.IUserRepository;
import com.f8.turnera.util.EmailValidation;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public SessionUserDTO login(LoginDTO authDTO) {

        SessionUserDTO result = new SessionUserDTO();

        Optional<User> user = userRepository.findByUsername(authDTO.getUsername());

        if (user.isPresent() && !user.get().getActive()) {
            throw new RuntimeException("El Usuario no está activado, revise sus Correo Electrónico para activarlo.");
        }

        if (!user.isPresent() || !bCryptPasswordEncoder.matches(authDTO.getPassword(), user.get().getPassword())) {
            throw new RuntimeException("Revise sus credenciales. Usuario o contraseña inválido.");
        }

        if (!EmailValidation.validateEmail(authDTO.getUsername())) {
            throw new RuntimeException("El Correo Electrónico es inválido.");
        }

        ModelMapper modelMapper = new ModelMapper();
        result.setUser(modelMapper.map(user.get(), UserDTO.class));

        // get permissions
        Set<Set<Permission>> setPermissions = user.get().getProfiles().stream().map(Profile::getPermissions)
                .collect(Collectors.toSet());
        Set<Stream<String>> streamPermissions = setPermissions.stream().map(x -> x.stream().map(Permission::getCode))
                .collect(Collectors.toSet());
        Set<String> permissions = streamPermissions.stream().flatMap(x -> x.distinct()).collect(Collectors.toSet());
        String authorities = permissions.stream().collect(Collectors.joining(","));

        String token = Jwts.builder()
                .claim(SecurityConstants.AUTHORITIES_KEY, authorities)
                .setIssuer("f8")
                .setSubject(authDTO.getUsername())

                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET).compact();
        result.setToken(token);

        return result;
    }

}
