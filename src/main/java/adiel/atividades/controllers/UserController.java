package adiel.atividades.controllers;

import adiel.atividades.dtos.UserDTO;
import adiel.atividades.entities.LoginResponse;
import adiel.atividades.entities.User;
import adiel.atividades.exceptions.UserAlreadyExistsException;
import adiel.atividades.providers.TokenProvider;
import adiel.atividades.services.MyUserDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//FIXME: #### quando tudo já estiver ok, e o usuário já criado, remover o mapeamento new-user ####

@Controller
public class UserController {

    @Autowired
    MyUserDetailService usuarioService;

    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@RequestBody UserDTO user) {
        User usuario = new User();
        usuario.setUsername(user.username);
        usuario.setPassword(user.password);
        try {
            usuarioService.save(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
        } catch (UserAlreadyExistsException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserDTO user) {
        UserDetails usuario = usuarioService.loadUserByUsername(user.username);
        LoginResponse lResponse= new LoginResponse();

        if(usuario.getUsername().equals("")) {
            lResponse.message="Combinação usuário/senha incorreta!";
            lResponse.user = null;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(lResponse);
        }

        if( !BCrypt.checkpw(user.password, usuario.getPassword())){
            lResponse.message="Combinação usuário/senha incorreta!";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(lResponse);
        }
        
        TokenProvider tokenProvider = new TokenProvider();
        String token = tokenProvider.generateToken(usuario);
        // String token = "TOken.a10je899q30r.1294u91r01w89.180414u801r4u1.12890170u12";

        lResponse.message="Sucesso! você está autenticado!";
        lResponse.user = usuario;
        lResponse.token = token;
        return ResponseEntity.status(HttpStatus.OK).body(lResponse);
    }

}