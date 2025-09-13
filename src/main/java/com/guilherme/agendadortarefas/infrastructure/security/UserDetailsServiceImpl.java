package com.guilherme.agendadortarefas.infrastructure.security;


import com.guilherme.agendadortarefas.business.dto.UsuarioDTORecord;
import com.guilherme.agendadortarefas.infrastructure.client.UsuarioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl {

    @Autowired
    private UsuarioClient client;



    public UserDetails carregaDadosUsuario(String email, String token){

        UsuarioDTORecord usuarioDTOdto = client.buscaUsuarioPorEmail(email, token);
        return User
                .withUsername(usuarioDTOdto.email()) // Define o nome de usuário como o e-mail
                .password(usuarioDTOdto.senha()) // Define a senha do usuário
                .build();

    }
}
