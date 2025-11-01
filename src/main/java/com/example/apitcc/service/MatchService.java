package com.example.apitcc.service;

import com.example.apitcc.config.DatabaseConfig;
import com.example.apitcc.model.dto.MatchDTO;
import com.example.apitcc.model.entity.Match;
import com.example.apitcc.model.entity.Usuario;
import com.example.apitcc.model.entity.Trabalho;
import com.example.apitcc.model.entity.ItemBase;
import com.example.apitcc.repository.MatchRepository;
import com.example.apitcc.exception.ResourceNotFoundException;
import com.example.apitcc.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MatchService {
    
    @Autowired(required = false)
    private DatabaseConfig.DatabaseOperations databaseOperations;
    
    @Autowired
    private MatchRepository matchRepository;
    
    public List<MatchDTO> listarTodosMatches() {
        if (databaseOperations != null) {
            // Produção: PostgreSQL
            return databaseOperations.findAllMatches().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } else {
            // Desenvolvimento: Memória
            return matchRepository.findAll().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
    }
    
    public MatchDTO buscarMatchPorId(String id) {
        if (databaseOperations != null) {
            // Produção: PostgreSQL
            Match match = databaseOperations.findMatchById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Match não encontrado com ID: " + id));
            return convertToDTO(match);
        } else {
            // Desenvolvimento: Memória
            Match match = matchRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Match não encontrado com ID: " + id));
            return convertToDTO(match);
        }
    }
    
    public MatchDTO criarMatch(MatchDTO matchDTO) {
        // Validações
        validarDadosMatch(matchDTO);
        
        if (databaseOperations != null) {
            // Produção: PostgreSQL
            // Verificar se o usuário existe
            Optional<Usuario> usuarioExistente = databaseOperations.findUsuarioById(matchDTO.getIdUsuario());
            if (!usuarioExistente.isPresent()) {
                throw new BadRequestException("Usuário não encontrado com ID: " + matchDTO.getIdUsuario());
            }
            
            // Verificar se o trabalho existe
            Optional<Trabalho> trabalhoExistente = databaseOperations.findTrabalhoById(matchDTO.getIdTrabalho());
            if (!trabalhoExistente.isPresent()) {
                throw new BadRequestException("Trabalho não encontrado com ID: " + matchDTO.getIdTrabalho());
            }
            
            // Verificar se já existe um match entre este usuário e trabalho
            List<Match> matchesUsuario = databaseOperations.findMatchesByUsuario(matchDTO.getIdUsuario());
            boolean matchExiste = matchesUsuario.stream()
                    .anyMatch(m -> m.getIdTrabalho().equals(matchDTO.getIdTrabalho()));
            
            if (matchExiste) {
                throw new BadRequestException("Já existe um match entre este usuário e trabalho");
            }
            
            String dataMatch = matchDTO.getDataMatch() != null ? 
                              matchDTO.getDataMatch() : ItemBase.getCurrentDateTime();
            
            Match match = new Match(
                    null, // ID será gerado pelo banco
                    matchDTO.getDescricao() != null ? matchDTO.getDescricao() : "",
                    matchDTO.getIdUsuario(),
                    matchDTO.getIdTrabalho(),
                    matchDTO.getStatus() != null ? matchDTO.getStatus() : "pendente",
                    dataMatch,
                    matchDTO.getDataInicio(),
                    matchDTO.getDataTermino(),
                    matchDTO.getHorasTrabalhas() != null ? matchDTO.getHorasTrabalhas() : "0",
                    matchDTO.getAvaliacao(),
                    ItemBase.getCurrentDateTime()
            );
            
            Match matchSalvo = databaseOperations.saveMatch(match);
            return convertToDTO(matchSalvo);
            
        } else {
            // Desenvolvimento: Memória
            // Verificar se o usuário existe
            boolean usuarioExiste = Usuario.usuarios.stream()
                    .anyMatch(u -> u.getId().equals(matchDTO.getIdUsuario()));
            
            if (!usuarioExiste) {
                throw new BadRequestException("Usuário não encontrado com ID: " + matchDTO.getIdUsuario());
            }
            
            // Verificar se o trabalho existe
            boolean trabalhoExiste = Trabalho.trabalhos.stream()
                    .anyMatch(t -> t.getId().equals(matchDTO.getIdTrabalho()));
            
            if (!trabalhoExiste) {
                throw new BadRequestException("Trabalho não encontrado com ID: " + matchDTO.getIdTrabalho());
            }
            
            // Verificar se já existe um match entre este usuário e trabalho
            boolean matchExiste = matchRepository.existsByIdUsuarioAndIdTrabalho(
                    matchDTO.getIdUsuario(), matchDTO.getIdTrabalho());
            
            if (matchExiste) {
                throw new BadRequestException("Já existe um match entre este usuário e trabalho");
            }
            
            String dataMatch = matchDTO.getDataMatch() != null ? 
                              matchDTO.getDataMatch() : ItemBase.getCurrentDateTime();
            
            Match match = new Match(
                    null, // ID será gerado pelo repository
                    matchDTO.getDescricao() != null ? matchDTO.getDescricao() : "",
                    matchDTO.getIdUsuario(),
                    matchDTO.getIdTrabalho(),
                    matchDTO.getStatus() != null ? matchDTO.getStatus() : "pendente",
                    dataMatch,
                    matchDTO.getDataInicio(),
                    matchDTO.getDataTermino(),
                    matchDTO.getHorasTrabalhas() != null ? matchDTO.getHorasTrabalhas() : "0",
                    matchDTO.getAvaliacao(),
                    ItemBase.getCurrentDateTime()
            );
            
            Match matchSalvo = matchRepository.save(match);
            return convertToDTO(matchSalvo);
        }
    }
    
    public MatchDTO atualizarMatch(String id, MatchDTO matchDTO) {
        if (databaseOperations != null) {
            // Produção: PostgreSQL
            Match matchExistente = databaseOperations.findMatchById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Match não encontrado com ID: " + id));
            
            // Validar dados se fornecidos
            if (matchDTO.getIdUsuario() != null && !matchDTO.getIdUsuario().isEmpty()) {
                Optional<Usuario> usuario = databaseOperations.findUsuarioById(matchDTO.getIdUsuario());
                if (!usuario.isPresent()) {
                    throw new BadRequestException("Usuário não encontrado com ID: " + matchDTO.getIdUsuario());
                }
            }
            
            if (matchDTO.getIdTrabalho() != null && !matchDTO.getIdTrabalho().isEmpty()) {
                Optional<Trabalho> trabalho = databaseOperations.findTrabalhoById(matchDTO.getIdTrabalho());
                if (!trabalho.isPresent()) {
                    throw new BadRequestException("Trabalho não encontrado com ID: " + matchDTO.getIdTrabalho());
                }
            }
            
            // Validar avaliação se fornecida
            if (matchDTO.getAvaliacao() != null && !matchDTO.getAvaliacao().isEmpty()) {
                try {
                    int avaliacao = Integer.parseInt(matchDTO.getAvaliacao());
                    if (avaliacao < 1 || avaliacao > 5) {
                        throw new BadRequestException("Avaliação deve estar entre 1 e 5");
                    }
                } catch (NumberFormatException e) {
                    throw new BadRequestException("Formato de avaliação inválido");
                }
            }
            
            // Validar horas trabalhadas se fornecidas
            if (matchDTO.getHorasTrabalhas() != null && !matchDTO.getHorasTrabalhas().isEmpty()) {
                try {
                    float horas = Float.parseFloat(matchDTO.getHorasTrabalhas());
                    if (horas < 0) {
                        throw new BadRequestException("Horas trabalhadas não pode ser negativo");
                    }
                } catch (NumberFormatException e) {
                    throw new BadRequestException("Formato de horas trabalhadas inválido");
                }
            }
            
            // Atualizar campos
            atualizarCamposMatch(matchExistente, matchDTO);
            
            Match matchAtualizado = databaseOperations.saveMatch(matchExistente);
            return convertToDTO(matchAtualizado);
            
        } else {
            // Desenvolvimento: Memória
            Match matchExistente = matchRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Match não encontrado com ID: " + id));
            
            // Validar dados se fornecidos
            if (matchDTO.getIdUsuario() != null && !matchDTO.getIdUsuario().isEmpty()) {
                boolean usuarioExiste = Usuario.usuarios.stream()
                        .anyMatch(u -> u.getId().equals(matchDTO.getIdUsuario()));
                
                if (!usuarioExiste) {
                    throw new BadRequestException("Usuário não encontrado com ID: " + matchDTO.getIdUsuario());
                }
            }
            
            if (matchDTO.getIdTrabalho() != null && !matchDTO.getIdTrabalho().isEmpty()) {
                boolean trabalhoExiste = Trabalho.trabalhos.stream()
                        .anyMatch(t -> t.getId().equals(matchDTO.getIdTrabalho()));
                
                if (!trabalhoExiste) {
                    throw new BadRequestException("Trabalho não encontrado com ID: " + matchDTO.getIdTrabalho());
                }
            }
            
            // Validar avaliação se fornecida
            if (matchDTO.getAvaliacao() != null && !matchDTO.getAvaliacao().isEmpty()) {
                try {
                    int avaliacao = Integer.parseInt(matchDTO.getAvaliacao());
                    if (avaliacao < 1 || avaliacao > 5) {
                        throw new BadRequestException("Avaliação deve estar entre 1 e 5");
                    }
                } catch (NumberFormatException e) {
                    throw new BadRequestException("Formato de avaliação inválido");
                }
            }
            
            // Validar horas trabalhadas se fornecidas
            if (matchDTO.getHorasTrabalhas() != null && !matchDTO.getHorasTrabalhas().isEmpty()) {
                try {
                    float horas = Float.parseFloat(matchDTO.getHorasTrabalhas());
                    if (horas < 0) {
                        throw new BadRequestException("Horas trabalhadas não pode ser negativo");
                    }
                } catch (NumberFormatException e) {
                    throw new BadRequestException("Formato de horas trabalhadas inválido");
                }
            }
            
            // Atualizar campos
            atualizarCamposMatch(matchExistente, matchDTO);
            
            Match matchAtualizado = matchRepository.save(matchExistente);
            return convertToDTO(matchAtualizado);
        }
    }
    
    public void excluirMatch(String id) {
        if (databaseOperations != null) {
            // Produção: PostgreSQL
            boolean removido = databaseOperations.deleteMatchById(id);
            if (!removido) {
                throw new ResourceNotFoundException("Match não encontrado com ID: " + id);
            }
        } else {
            // Desenvolvimento: Memória
            boolean removido = matchRepository.deleteById(id);
            if (!removido) {
                throw new ResourceNotFoundException("Match não encontrado com ID: " + id);
            }
        }
    }
    
    public List<MatchDTO> buscarMatchesPorUsuario(String idUsuario) {
        if (databaseOperations != null) {
            // Produção: PostgreSQL
            return databaseOperations.findMatchesByUsuario(idUsuario).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } else {
            // Desenvolvimento: Memória
            return matchRepository.findByIdUsuario(idUsuario).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
    }
    
    public List<MatchDTO> buscarMatchesPorTrabalho(String idTrabalho) {
        if (databaseOperations != null) {
            // Produção: PostgreSQL
            return databaseOperations.findMatchesByTrabalho(idTrabalho).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } else {
            // Desenvolvimento: Memória
            return matchRepository.findByIdTrabalho(idTrabalho).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
    }
    
    public List<MatchDTO> buscarPorStatus(String status) {
        List<Match> matches;
        
        if (databaseOperations != null) {
            matches = databaseOperations.findAllMatches();
        } else {
            matches = matchRepository.findByStatus(status);
        }
        
        return matches.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<MatchDTO> buscarMatchesAtivos() {
        List<Match> matches;
        
        if (databaseOperations != null) {
            matches = databaseOperations.findAllMatches();
            return matches.stream()
                    .filter(m -> m.getStatus() != null && 
                                (m.getStatus().equalsIgnoreCase("ativo") || 
                                 m.getStatus().equalsIgnoreCase("em_andamento") ||
                                 m.getStatus().equalsIgnoreCase("pendente")))
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } else {
            return matchRepository.findActiveMatches().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
    }
    
    public List<MatchDTO> buscarMatchesFinalizados() {
        List<Match> matches;
        
        if (databaseOperations != null) {
            matches = databaseOperations.findAllMatches();
            return matches.stream()
                    .filter(m -> m.getStatus() != null && 
                                (m.getStatus().equalsIgnoreCase("finalizado") || 
                                 m.getStatus().equalsIgnoreCase("concluido") ||
                                 m.getStatus().equalsIgnoreCase("terminado")))
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } else {
            return matchRepository.findCompletedMatches().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
    }
    
    public List<MatchDTO> buscarPorAvaliacao(String avaliacaoMinima) {
        try {
            int minAvaliacao = Integer.parseInt(avaliacaoMinima);
            List<Match> matches;
            
            if (databaseOperations != null) {
                matches = databaseOperations.findAllMatches();
                return matches.stream()
                        .filter(m -> {
                            if (m.getAvaliacao() == null) return false;
                            try {
                                return Integer.parseInt(m.getAvaliacao()) >= minAvaliacao;
                            } catch (NumberFormatException e) {
                                return false;
                            }
                        })
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
            } else {
                return matchRepository.findByAvaliacaoGreaterThanEqual(avaliacaoMinima).stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
            }
        } catch (NumberFormatException e) {
            throw new BadRequestException("Formato de avaliação inválido");
        }
    }
    
    public MatchDTO buscarMatchPorUsuarioETrabalho(String idUsuario, String idTrabalho) {
        if (databaseOperations != null) {
            // Produção: PostgreSQL - buscar na lista e filtrar
            List<Match> matchesUsuario = databaseOperations.findMatchesByUsuario(idUsuario);
            Match match = matchesUsuario.stream()
                    .filter(m -> m.getIdTrabalho().equals(idTrabalho))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException(
                        "Match não encontrado entre usuário " + idUsuario + " e trabalho " + idTrabalho));
            return convertToDTO(match);
        } else {
            // Desenvolvimento: Memória
            Match match = matchRepository.findByIdUsuarioAndIdTrabalho(idUsuario, idTrabalho)
                    .orElseThrow(() -> new ResourceNotFoundException(
                        "Match não encontrado entre usuário " + idUsuario + " e trabalho " + idTrabalho));
            return convertToDTO(match);
        }
    }
    
    public void excluirMatchesPorUsuario(String idUsuario) {
        if (databaseOperations != null) {
            // Produção: PostgreSQL
            List<Match> matches = databaseOperations.findMatchesByUsuario(idUsuario);
            if (matches.isEmpty()) {
                throw new ResourceNotFoundException("Nenhum match encontrado para o usuário com ID: " + idUsuario);
            }
            
            for (Match match : matches) {
                databaseOperations.deleteMatchById(match.getId());
            }
        } else {
            // Desenvolvimento: Memória
            boolean removido = matchRepository.deleteByIdUsuario(idUsuario);
            if (!removido) {
                throw new ResourceNotFoundException("Nenhum match encontrado para o usuário com ID: " + idUsuario);
            }
        }
    }
    
    public void excluirMatchesPorTrabalho(String idTrabalho) {
        if (databaseOperations != null) {
            // Produção: PostgreSQL
            List<Match> matches = databaseOperations.findMatchesByTrabalho(idTrabalho);
            if (matches.isEmpty()) {
                throw new ResourceNotFoundException("Nenhum match encontrado para o trabalho com ID: " + idTrabalho);
            }
            
            for (Match match : matches) {
                databaseOperations.deleteMatchById(match.getId());
            }
        } else {
            // Desenvolvimento: Memória
            boolean removido = matchRepository.deleteByIdTrabalho(idTrabalho);
            if (!removido) {
                throw new ResourceNotFoundException("Nenhum match encontrado para o trabalho com ID: " + idTrabalho);
            }
        }
    }
    
    private void atualizarCamposMatch(Match match, MatchDTO dto) {
        if (dto.getIdUsuario() != null && !dto.getIdUsuario().trim().isEmpty()) {
            match.setIdUsuario(dto.getIdUsuario());
        }
        if (dto.getIdTrabalho() != null && !dto.getIdTrabalho().trim().isEmpty()) {
            match.setIdTrabalho(dto.getIdTrabalho());
        }
        if (dto.getStatus() != null) {
            match.setStatus(dto.getStatus());
        }
        if (dto.getDataMatch() != null) {
            match.setDataMatch(dto.getDataMatch());
        }
        if (dto.getDataInicio() != null) {
            match.setDataInicio(dto.getDataInicio());
        }
        if (dto.getDataTermino() != null) {
            match.setDataTermino(dto.getDataTermino());
        }
        if (dto.getHorasTrabalhas() != null) {
            match.setHorasTrabalhas(dto.getHorasTrabalhas());
        }
        if (dto.getAvaliacao() != null) {
            match.setAvaliacao(dto.getAvaliacao());
        }
        if (dto.getDescricao() != null) {
            match.setDescricao(dto.getDescricao());
        }
    }
    
    private void validarDadosMatch(MatchDTO matchDTO) {
        if (matchDTO.getIdUsuario() == null || matchDTO.getIdUsuario().trim().isEmpty()) {
            throw new BadRequestException("ID do usuário é obrigatório");
        }
        
        if (matchDTO.getIdTrabalho() == null || matchDTO.getIdTrabalho().trim().isEmpty()) {
            throw new BadRequestException("ID do trabalho é obrigatório");
        }
        
        // Validar avaliação se fornecida
        if (matchDTO.getAvaliacao() != null && !matchDTO.getAvaliacao().isEmpty()) {
            try {
                int avaliacao = Integer.parseInt(matchDTO.getAvaliacao());
                if (avaliacao < 1 || avaliacao > 5) {
                    throw new BadRequestException("Avaliação deve estar entre 1 e 5");
                }
            } catch (NumberFormatException e) {
                throw new BadRequestException("Formato de avaliação inválido");
            }
        }
        
        // Validar horas trabalhadas se fornecidas
        if (matchDTO.getHorasTrabalhas() != null && !matchDTO.getHorasTrabalhas().isEmpty()) {
            try {
                float horas = Float.parseFloat(matchDTO.getHorasTrabalhas());
                if (horas < 0) {
                    throw new BadRequestException("Horas trabalhadas não pode ser negativo");
                }
            } catch (NumberFormatException e) {
                throw new BadRequestException("Formato de horas trabalhadas inválido");
            }
        }
    }
    
    private MatchDTO convertToDTO(Match match) {
        return new MatchDTO(
                match.getId(),
                match.getIdUsuario(),
                match.getIdTrabalho(),
                match.getStatus(),
                match.getDataMatch(),
                match.getDataInicio(),
                match.getDataTermino(),
                match.getHorasTrabalhas(),
                match.getAvaliacao(),
                match.getDescricao(),
                match.getDataCadastro()
        );
    }
}