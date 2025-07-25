package com.guilherme.agendadortarefas.business;

import com.guilherme.agendadortarefas.business.dto.TarefasDTO;
import com.guilherme.agendadortarefas.business.mapper.TarefasConverter;
import com.guilherme.agendadortarefas.infrastructure.entity.TarefasEntity;
import com.guilherme.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import com.guilherme.agendadortarefas.infrastructure.repository.TarefasRepository;
import com.guilherme.agendadortarefas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefasService {

    private final TarefasRepository tarefasRepository;
    private final TarefasConverter tarefaConverter;
    private final JwtUtil jwtUtil;

    public TarefasDTO gravarTarefa(String token, TarefasDTO dto){
        String email = jwtUtil.extractUsername(token.substring(7));

        dto.setDataAlteracao(LocalDateTime.now());
        dto.setStatusNotificacaoEnum(StatusNotificacaoEnum.PENDENTE);
        dto.setEmailUsuario(email);

        return tarefaConverter.paraTarefaDTO(
                tarefasRepository.save(tarefaConverter.paraTarefaEntity(dto)));
    }

    public List<TarefasDTO> buscaTarefasAgendadasPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal){
        return tarefaConverter.paraListaTarefasDTO(
                tarefasRepository.findByDataEventoBetween(dataInicial, dataFinal));
    }

    public List<TarefasDTO> buscarTarefasPorEmail(String token){
        String email = jwtUtil.extractUsername(token.substring(7));

        return tarefaConverter.paraListaTarefasDTO(tarefasRepository.findByEmailUsuario(email));

    }



}
