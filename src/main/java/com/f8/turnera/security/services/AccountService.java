package com.f8.turnera.security.services;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.f8.turnera.email.IEmailService;
import com.f8.turnera.entities.Organization;
import com.f8.turnera.repositories.IOrganizationRepository;
import com.f8.turnera.security.entities.Permission;
import com.f8.turnera.security.entities.Profile;
import com.f8.turnera.security.entities.User;
import com.f8.turnera.security.models.ActivateDTO;
import com.f8.turnera.security.models.PasswordChangeDTO;
import com.f8.turnera.security.models.PasswordResetDTO;
import com.f8.turnera.security.models.PasswordResetRequestDTO;
import com.f8.turnera.security.models.RegisterDTO;
import com.f8.turnera.security.models.UserDTO;
import com.f8.turnera.security.repositories.IPermissionRepository;
import com.f8.turnera.security.repositories.IProfileRepository;
import com.f8.turnera.security.repositories.IUserRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import net.bytebuddy.utility.RandomString;

@Service
public class AccountService implements IAccountService {

    @Autowired
    private IOrganizationRepository organizationRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IProfileRepository profileRepository;

    @Autowired
    private IPermissionRepository permissionRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private IEmailService emailService;

    @Override
    public UserDTO register(RegisterDTO registerDTO) {
        Optional<User> existingUser = userRepository.findByUsername(registerDTO.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("El Correo Electrónico ingresado ya está registrado. Por favor ingrese otro.");
        }

        // create organization
        Organization organization = new Organization();
        organization.setCreatedDate(LocalDateTime.now());
        organization.setActive(false);
        organization.setBusinessName(registerDTO.getBusinessName());
        organization.setDefaultEmail(registerDTO.getUsername());

        // create user
        User user = new User();
        user.setCreatedDate(LocalDateTime.now());
        user.setActive(false);
        user.setOrganization(organization);
        user.setLastName(registerDTO.getLastName());
        user.setFirstName(registerDTO.getFirstName());
        user.setUsername(registerDTO.getUsername());
        user.setIsAdmin(true);
        user.setActivationKey(RandomString.make(20));
        user.setPassword(RandomString.make(40));

        // save all
        try {
            organizationRepository.save(organization);
            userRepository.save(user);
        } catch (Exception e) {
            userRepository.delete(user);
            organizationRepository.delete(organization);

            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }

        emailService.sendOrganizationActivationEmail(user);

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO activate(ActivateDTO activateDTO) {
        Optional<User> user = userRepository.findByActivationKey(activateDTO.getActivationKey());
        if (!user.isPresent()) {
            throw new RuntimeException("La clave de activación no está asociada a ningún Usuario.");
        }

        user.ifPresent(u -> {
            if (u.getIsAdmin()) {
                u.getOrganization().setActive(true);
                addDefaultProfiles(u);
            }
            u.setActivationKey(null);
            u.setPassword(bCryptPasswordEncoder.encode(activateDTO.getPassword()));
            u.setActive(true);
        });

        try {
            profileRepository.saveAll(user.get().getProfiles());
            userRepository.save(user.get());
            organizationRepository.save(user.get().getOrganization());
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
        
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(user.get(), UserDTO.class);
    }

    private void addDefaultProfiles(User user) {
        Set<Permission> permissions = new HashSet<>(permissionRepository.findAll());
        Profile profile = new Profile(true, LocalDateTime.now(), user.getOrganization(), "Administrador", permissions);
        Set<Profile> profiles = new HashSet<>();
        profiles.add(profile);
        user.setProfiles(profiles);
    }

    @Override
    public UserDTO passwordResetRequest(PasswordResetRequestDTO passwordResetRequestDTO) {
        Optional<User> user = userRepository.findByUsername(passwordResetRequestDTO.getUsername());
        if (!user.isPresent()) {
            throw new RuntimeException("Usuario no encontrado.");
        }

        user.ifPresent(u -> {
            u.setResetDate(LocalDateTime.now());
            u.setResetKey(RandomString.make(20));
        });

        try {
            userRepository.save(user.get());
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }

        emailService.sendPasswordResetRequestEmail(user.get());

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(user.get(), UserDTO.class);
    }

    @Override
    public UserDTO passwordReset(PasswordResetDTO passwordResetDTO) {
        Optional<User> user = userRepository.findByResetKey(passwordResetDTO.getResetKey());
        if (!user.isPresent()) {
            throw new RuntimeException("La Clave de Reseteo no está asociada a ningún Usuario.");
        }

        if (user.get().getResetDate().isAfter(LocalDateTime.now().plusHours(3))) {
            throw new RuntimeException("Su Clave de Reseteo ha expirado.");
        }

        user.ifPresent(u -> {
            u.setResetDate(null);
            u.setResetKey(null);
            u.setPassword(bCryptPasswordEncoder.encode(passwordResetDTO.getPassword()));
        });

        try {
            userRepository.save(user.get());
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
        
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(user.get(), UserDTO.class);
    }

    @Override
    public UserDTO passwordChange(PasswordChangeDTO passwordChangeDTO) {
        Optional<User> user = userRepository.findByUsername(passwordChangeDTO.getUsername());
        if (!user.isPresent()) {
            throw new RuntimeException("Usuario no encontrado.");
        }

        if (!bCryptPasswordEncoder.matches(passwordChangeDTO.getCurrentPassword(), user.get().getPassword())) {
            throw new RuntimeException("Su Contraseña actual es inválida.");
        }

        user.get().setPassword(bCryptPasswordEncoder.encode(passwordChangeDTO.getPassword()));

        try {
            userRepository.save(user.get());
        } catch (Exception e) {
            throw new RuntimeException("Hubo un problema al guardar los datos. Por favor reintente nuevamente.");
        }
        
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(user.get(), UserDTO.class);
    }

}
