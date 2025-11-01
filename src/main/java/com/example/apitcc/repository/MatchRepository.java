package com.example.apitcc.repository;

import com.example.apitcc.model.entity.Match;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MatchRepository {
    
    /**
     * Busca todos os matches
     */
    public List<Match> findAll() {
        return Match.matches;
    }
    
    /**
     * Busca match por ID
     */
    public Optional<Match> findById(String id) {
        return Match.matches.stream()
                .filter(match -> match.getId().equals(id))
                .findFirst();
    }
    
    /**
     * Busca matches por ID do usuário
     */
    public List<Match> findByIdUsuario(String idUsuario) {
        return Match.matches.stream()
                .filter(match -> match.getIdUsuario() != null && match.getIdUsuario().equals(idUsuario))
                .collect(Collectors.toList());
    }
    
    /**
     * Busca matches por ID do trabalho
     */
    public List<Match> findByIdTrabalho(String idTrabalho) {
        return Match.matches.stream()
                .filter(match -> match.getIdTrabalho() != null && match.getIdTrabalho().equals(idTrabalho))
                .collect(Collectors.toList());
    }
    
    /**
     * Busca matches por status
     */
    public List<Match> findByStatus(String status) {
        return Match.matches.stream()
                .filter(match -> match.getStatus() != null && 
                        match.getStatus().toLowerCase().equals(status.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    /**
     * Busca matches por combinação usuário e trabalho
     */
    public Optional<Match> findByIdUsuarioAndIdTrabalho(String idUsuario, String idTrabalho) {
        return Match.matches.stream()
                .filter(match -> match.getIdUsuario() != null && match.getIdUsuario().equals(idUsuario) &&
                                match.getIdTrabalho() != null && match.getIdTrabalho().equals(idTrabalho))
                .findFirst();
    }
    
    /**
     * Busca matches por faixa de avaliação
     */
    public List<Match> findByAvaliacaoGreaterThanEqual(String avaliacaoMinima) {
        return Match.matches.stream()
                .filter(match -> {
                    if (match.getAvaliacao() == null) return false;
                    try {
                        int avaliacaoMatch = Integer.parseInt(match.getAvaliacao());
                        int avaliacaoMin = Integer.parseInt(avaliacaoMinima);
                        return avaliacaoMatch >= avaliacaoMin;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Busca matches por faixa de horas trabalhadas
     */
    public List<Match> findByHorasTrabalhasGreaterThanEqual(String horasMinimas) {
        return Match.matches.stream()
                .filter(match -> {
                    if (match.getHorasTrabalhas() == null) return false;
                    try {
                        float horasMatch = Float.parseFloat(match.getHorasTrabalhas());
                        float horasMin = Float.parseFloat(horasMinimas);
                        return horasMatch >= horasMin;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Busca matches entre datas
     */
    public List<Match> findByDataMatchBetween(String dataInicio, String dataFim) {
        return Match.matches.stream()
                .filter(match -> match.getDataMatch() != null &&
                                match.getDataMatch().compareTo(dataInicio) >= 0 &&
                                match.getDataMatch().compareTo(dataFim) <= 0)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca matches ativos (com status específicos)
     */
    public List<Match> findActiveMatches() {
        return Match.matches.stream()
                .filter(match -> match.getStatus() != null && 
                        (match.getStatus().equalsIgnoreCase("ativo") || 
                         match.getStatus().equalsIgnoreCase("em_andamento") ||
                         match.getStatus().equalsIgnoreCase("pendente")))
                .collect(Collectors.toList());
    }
    
    /**
     * Busca matches finalizados
     */
    public List<Match> findCompletedMatches() {
        return Match.matches.stream()
                .filter(match -> match.getStatus() != null && 
                        (match.getStatus().equalsIgnoreCase("finalizado") || 
                         match.getStatus().equalsIgnoreCase("concluido") ||
                         match.getStatus().equalsIgnoreCase("terminado")))
                .collect(Collectors.toList());
    }
    
    /**
     * Salva um match (adiciona à lista)
     */
    public Match save(Match match) {
        // Se é um novo match (sem ID ou ID novo)
        if (match.getId() == null || match.getId().isEmpty()) {
            match.setId(String.valueOf(Match.proximoId++));
            Match.matches.add(match);
        } else {
            // Atualiza match existente
            Optional<Match> existingMatch = findById(match.getId());
            if (existingMatch.isPresent()) {
                int index = Match.matches.indexOf(existingMatch.get());
                Match.matches.set(index, match);
            } else {
                // Se não encontrou, adiciona como novo
                Match.matches.add(match);
            }
        }
        return match;
    }
    
    /**
     * Deleta match por ID
     */
    public boolean deleteById(String id) {
        return Match.matches.removeIf(match -> match.getId().equals(id));
    }
    
    /**
     * Deleta todos os matches de um usuário
     */
    public boolean deleteByIdUsuario(String idUsuario) {
        return Match.matches.removeIf(match -> match.getIdUsuario() != null && 
                                              match.getIdUsuario().equals(idUsuario));
    }
    
    /**
     * Deleta todos os matches de um trabalho
     */
    public boolean deleteByIdTrabalho(String idTrabalho) {
        return Match.matches.removeIf(match -> match.getIdTrabalho() != null && 
                                              match.getIdTrabalho().equals(idTrabalho));
    }
    
    /**
     * Verifica se existe match com o ID
     */
    public boolean existsById(String id) {
        return Match.matches.stream()
                .anyMatch(match -> match.getId().equals(id));
    }
    
    /**
     * Verifica se existe match entre usuário e trabalho
     */
    public boolean existsByIdUsuarioAndIdTrabalho(String idUsuario, String idTrabalho) {
        return Match.matches.stream()
                .anyMatch(match -> match.getIdUsuario() != null && match.getIdUsuario().equals(idUsuario) &&
                                  match.getIdTrabalho() != null && match.getIdTrabalho().equals(idTrabalho));
    }
    
    /**
     * Conta total de matches
     */
    public long count() {
        return Match.matches.size();
    }
    
    /**
     * Conta matches de um usuário
     */
    public long countByIdUsuario(String idUsuario) {
        return Match.matches.stream()
                .filter(match -> match.getIdUsuario() != null && match.getIdUsuario().equals(idUsuario))
                .count();
    }
    
    /**
     * Conta matches de um trabalho
     */
    public long countByIdTrabalho(String idTrabalho) {
        return Match.matches.stream()
                .filter(match -> match.getIdTrabalho() != null && match.getIdTrabalho().equals(idTrabalho))
                .count();
    }
    
    /**
     * Deleta todos os matches (usado para testes)
     */
    public void deleteAll() {
        Match.matches.clear();
        Match.proximoId = 1; // Reset do contador
    }
}