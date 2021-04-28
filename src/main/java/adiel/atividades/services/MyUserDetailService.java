package adiel.atividades.services;

import java.util.Optional;

import adiel.atividades.MyUserPrincipal;
import adiel.atividades.entities.User;
import adiel.atividades.exceptions.UserAlreadyExistsException;
import adiel.atividades.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service(value = "usuarioService")
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository usuarioRepo;    

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> opt = usuarioRepo.findByUsername(username);
        User user = null;
        if(opt.isPresent()){
            user = opt.get();
        }
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new MyUserPrincipal(user);
    }

    public User save(User usuario) throws UserAlreadyExistsException {
        Optional<User> opt = usuarioRepo.findByUsername(usuario.username);
        if(opt.isPresent()){
            throw new UserAlreadyExistsException(usuario.username);
        }

        usuario.setPassword(bcryptEncoder.encode(usuario.getPassword()));
        return usuarioRepo.save(usuario);
    }

    public User findByLogin(String login) {
        Optional<User> opt = usuarioRepo.findByUsername(login);
        User user = null;
        if(opt.isPresent()){
            user = opt.get();
        }
        return user;
    }
}