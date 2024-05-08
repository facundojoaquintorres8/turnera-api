package com.f8.turnera.security.domain.services.impl;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.HashMap;

import com.f8.turnera.config.SecurityConstants;
import com.f8.turnera.security.domain.dtos.LoginDTO;
import com.f8.turnera.security.domain.dtos.SessionUserDTO;
import com.f8.turnera.security.domain.dtos.UserDTO;
import com.f8.turnera.security.domain.entities.Permission;
import com.f8.turnera.security.domain.entities.Profile;
import com.f8.turnera.security.domain.entities.User;
import com.f8.turnera.security.domain.repositories.IUserRepository;
import com.f8.turnera.security.domain.services.IAuthService;
import com.f8.turnera.util.EmailValidation;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${jwt.secret.key}")
    private String secretKey;

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
        Set<String> permissionsSet = streamPermissions.stream().flatMap(Stream::distinct).collect(Collectors.toSet());

        HashMap<String, Object> claims = new HashMap<>();
		claims.put(SecurityConstants.ORGANIZATION_KEY, user.get().getOrganization().getId());
		claims.put(SecurityConstants.PERMISSIONS_KEY, permissionsSet.stream().collect(Collectors.joining(",")));
        
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer("f8")
                .setSubject(authDTO.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
        result.setToken(token);

        return result;
    }

}
