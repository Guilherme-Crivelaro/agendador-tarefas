package com.guilherme.agendadortarefas.infrastructure.client;


import com.guilherme.agendadortarefas.business.dto.UsuarioDTORecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "usuario", url = "${usuario.url}")
public interface UsuarioClient {

    @GetMapping("/usuario")
    UsuarioDTORecord buscaUsuarioPorEmail(@RequestParam("email") String email,
                                          @RequestHeader("Authorization") String token);


}
