package com.guilherme.agendadortarefas.business;


import com.guilherme.agendadortarefas.business.dto.TarefasDTORecord;
import com.guilherme.agendadortarefas.business.mapper.TarefaUpdateConverter;
import com.guilherme.agendadortarefas.business.mapper.TarefasConverter;
import com.guilherme.agendadortarefas.infrastructure.entity.TarefasEntity;
import com.guilherme.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import com.guilherme.agendadortarefas.infrastructure.exception.ResourceNotFoundException;
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
    private final TarefaUpdateConverter tarefaUpdateConverter;

    public TarefasDTORecord gravarTarefa(String token, TarefasDTORecord dto){
        String email = jwtUtil.extractUsername(token.substring(7));
        TarefasDTORecord dtoFinal = new TarefasDTORecord(null, dto.nomeTarefa(),dto.descricao(),LocalDateTime.now(),
                dto.dataEvento(), email, null, StatusNotificacaoEnum.PENDENTE);

        return tarefaConverter.paraTarefaDTO(
                tarefasRepository.save(tarefaConverter.paraTarefaEntity(dtoFinal)));
    }

    public List<TarefasDTORecord> buscaTarefasAgendadasPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal){
        return tarefaConverter.paraListaTarefasDTORecord(
                tarefasRepository.findByDataEventoBetweenAndStatusNotificacaoEnum(dataInicial, dataFinal, StatusNotificacaoEnum.PENDENTE));
    }

    public List<TarefasDTORecord> buscarTarefasPorEmail(String token){
        String email = jwtUtil.extractUsername(token.substring(7));

        return tarefaConverter.paraListaTarefasDTORecord(tarefasRepository.findByEmailUsuario(email));

    }

    public void deletaTarefaPorId(String id){
        try {
            tarefasRepository.deleteById(id);
        } catch (RuntimeException e) {
            throw new ResourceNotFoundException("Erro ao deletar tarefa por id, id inexistente "+id+ e.getCause());
        }
    }

    public TarefasDTORecord alteraStatus(StatusNotificacaoEnum status, String id ){
        try {
            TarefasEntity entity = tarefasRepository.findById(id).orElseThrow(
                    () -> new ResourceNotFoundException("Tarefa não encontrado" + id));

            entity.setStatusNotificacaoEnum(status);
            return tarefaConverter.paraTarefaDTO(tarefasRepository.save(entity));
        }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("Erro ao alterar status da tarefa"+e.getCause());
        }
    }

    public TarefasDTORecord updateTarefas(TarefasDTORecord dto, String id){
        try {
            TarefasEntity entity = tarefasRepository.findById(id).orElseThrow(
                    () -> new ResourceNotFoundException("Tarefa não encontrado" + id));

            tarefaUpdateConverter.updateDeTarefas(dto, entity);
            return tarefaConverter.paraTarefaDTO(tarefasRepository.save(entity));

        }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("Erro ao alterar status da tarefa"+e.getCause());
        }
    }

}
