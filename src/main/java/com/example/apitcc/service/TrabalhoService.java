package com.example.apitcc.service;

import com.example.apitcc.config.DatabaseConfig;
import com.example.apitcc.model.dto.TrabalhoDTO;
import com.example.apitcc.model.entity.Trabalho;
import com.example.apitcc.model.entity.Empresa;
import com.example.apitcc.model.entity.ItemBase;
import com.example.apitcc.exception.ResourceNotFoundException;
import com.example.apitcc.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrabalhoService {
    
    @Autowired(required = false)
    private DatabaseConfig.DatabaseOperations databaseOperations;
    
    public List<TrabalhoDTO> listarTodosTrabalhos() {
        if (databaseOperations != null) {
            // Produção: PostgreSQL
            return databaseOperations.findAllTrabalhos().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } else {
            // Desenvolvimento: Memória
            return Trabalho.trabalhos.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
    }
    
    public TrabalhoDTO buscarTrabalhoPorId(String id) {
        if (databaseOperations != null) {
            // Produção: PostgreSQL
            Trabalho trabalho = databaseOperations.findTrabalhoById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Trabalho não encontrado com ID: " + id));
            return convertToDTO(trabalho);
        } else {
            // Desenvolvimento: Memória
            Trabalho trabalho = Trabalho.trabalhos.stream()
                    .filter(t -> t.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Trabalho não encontrado com ID: " + id));
            return convertToDTO(trabalho);
        }
    }
    
    public TrabalhoDTO criarTrabalho(TrabalhoDTO trabalhoDTO) {
        // Validações
        validarDadosTrabalho(trabalhoDTO);
        
        if (databaseOperations != null) {
            // Produção: PostgreSQL
            // Verificar se a empresa existe
            Optional<Empresa> empresaExistente = databaseOperations.findEmpresaById(trabalhoDTO.getIdEmpresa());
            if (!empresaExistente.isPresent()) {
                throw new BadRequestException("Empresa não encontrada com ID: " + trabalhoDTO.getIdEmpresa());
            }
            
            String nomeEmpresa = empresaExistente.get().getNome();
            
            Trabalho trabalho = new Trabalho(
                    null, // ID será gerado pelo banco
                    trabalhoDTO.getDescricao() != null ? trabalhoDTO.getDescricao() : "",
                    trabalhoDTO.getIdEmpresa(),
                    nomeEmpresa,
                    trabalhoDTO.getDescricaoTrabalho(),
                    trabalhoDTO.getQuantidadeDeVagas() != null ? trabalhoDTO.getQuantidadeDeVagas() : "1",
                    trabalhoDTO.getTipoTrabalho() != null ? trabalhoDTO.getTipoTrabalho() : "",
                    trabalhoDTO.getHabilidadesNecessarias() != null ? trabalhoDTO.getHabilidadesNecessarias() : "",
                    ItemBase.getCurrentDateTime()
            );
            
            Trabalho trabalhoSalvo = databaseOperations.saveTrabalho(trabalho);
            return convertToDTO(trabalhoSalvo);
            
        } else {
            // Desenvolvimento: Memória
            // Verificar se a empresa existe
            boolean empresaExiste = Empresa.empresas.stream()
                    .anyMatch(e -> e.getId().equals(trabalhoDTO.getIdEmpresa()));
            
            if (!empresaExiste) {
                throw new BadRequestException("Empresa não encontrada com ID: " + trabalhoDTO.getIdEmpresa());
            }
            
            // Buscar nome da empresa para armazenar
            String nomeEmpresa = Empresa.empresas.stream()
                    .filter(e -> e.getId().equals(trabalhoDTO.getIdEmpresa()))
                    .map(Empresa::getNome)
                    .findFirst()
                    .orElse("");
            
            String id = String.valueOf(Trabalho.proximoId++);
            String dataCadastro = ItemBase.getCurrentDateTime();
            
            Trabalho trabalho = new Trabalho(
                    id,
                    trabalhoDTO.getDescricao() != null ? trabalhoDTO.getDescricao() : "",
                    trabalhoDTO.getIdEmpresa(),
                    nomeEmpresa,
                    trabalhoDTO.getDescricaoTrabalho(),
                    trabalhoDTO.getQuantidadeDeVagas() != null ? trabalhoDTO.getQuantidadeDeVagas() : "1",
                    trabalhoDTO.getTipoTrabalho() != null ? trabalhoDTO.getTipoTrabalho() : "",
                    trabalhoDTO.getHabilidadesNecessarias() != null ? trabalhoDTO.getHabilidadesNecessarias() : "",
                    dataCadastro
            );
            
            Trabalho.trabalhos.add(trabalho);
            return convertToDTO(trabalho);
        }
    }
    
    public TrabalhoDTO atualizarTrabalho(String id, TrabalhoDTO trabalhoDTO) {
        if (databaseOperations != null) {
            // Produção: PostgreSQL
            Trabalho trabalhoExistente = databaseOperations.findTrabalhoById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Trabalho não encontrado com ID: " + id));
            
            // Validar dados se fornecidos
            if (trabalhoDTO.getIdEmpresa() != null && !trabalhoDTO.getIdEmpresa().isEmpty()) {
                Optional<Empresa> empresa = databaseOperations.findEmpresaById(trabalhoDTO.getIdEmpresa());
                if (!empresa.isPresent()) {
                    throw new BadRequestException("Empresa não encontrada com ID: " + trabalhoDTO.getIdEmpresa());
                }
            }
            
            // Validar quantidade de vagas se fornecida
            if (trabalhoDTO.getQuantidadeDeVagas() != null && !trabalhoDTO.getQuantidadeDeVagas().isEmpty()) {
                try {
                    int vagas = Integer.parseInt(trabalhoDTO.getQuantidadeDeVagas());
                    if (vagas <= 0) {
                        throw new BadRequestException("Quantidade de vagas deve ser um número positivo");
                    }
                } catch (NumberFormatException e) {
                    throw new BadRequestException("Formato de quantidade de vagas inválido");
                }
            }
            
            // Atualizar campos
            atualizarCamposTrabalho(trabalhoExistente, trabalhoDTO);
            
            // Atualizar nome da empresa se o ID mudou
            if (trabalhoDTO.getIdEmpresa() != null && !trabalhoDTO.getIdEmpresa().isEmpty()) {
                Optional<Empresa> empresa = databaseOperations.findEmpresaById(trabalhoDTO.getIdEmpresa());
                if (empresa.isPresent()) {
                    trabalhoExistente.setNomeEmpresa(empresa.get().getNome());
                }
            }
            
            Trabalho trabalhoAtualizado = databaseOperations.saveTrabalho(trabalhoExistente);
            return convertToDTO(trabalhoAtualizado);
            
        } else {
            // Desenvolvimento: Memória
            Trabalho trabalhoExistente = Trabalho.trabalhos.stream()
                    .filter(t -> t.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Trabalho não encontrado com ID: " + id));
            
            // Validar dados se fornecidos
            if (trabalhoDTO.getIdEmpresa() != null && !trabalhoDTO.getIdEmpresa().isEmpty()) {
                boolean empresaExiste = Empresa.empresas.stream()
                        .anyMatch(e -> e.getId().equals(trabalhoDTO.getIdEmpresa()));
                
                if (!empresaExiste) {
                    throw new BadRequestException("Empresa não encontrada com ID: " + trabalhoDTO.getIdEmpresa());
                }
            }
            
            // Validar quantidade de vagas se fornecida
            if (trabalhoDTO.getQuantidadeDeVagas() != null && !trabalhoDTO.getQuantidadeDeVagas().isEmpty()) {
                try {
                    int vagas = Integer.parseInt(trabalhoDTO.getQuantidadeDeVagas());
                    if (vagas <= 0) {
                        throw new BadRequestException("Quantidade de vagas deve ser um número positivo");
                    }
                } catch (NumberFormatException e) {
                    throw new BadRequestException("Formato de quantidade de vagas inválido");
                }
            }
            
            // Atualizar campos
            atualizarCamposTrabalho(trabalhoExistente, trabalhoDTO);
            
            // Atualizar nome da empresa se o ID mudou
            if (trabalhoDTO.getIdEmpresa() != null && !trabalhoDTO.getIdEmpresa().trim().isEmpty()) {
                String nomeEmpresa = Empresa.empresas.stream()
                        .filter(e -> e.getId().equals(trabalhoDTO.getIdEmpresa()))
                        .map(Empresa::getNome)
                        .findFirst()
                        .orElse("");
                trabalhoExistente.setNomeEmpresa(nomeEmpresa);
            }
            
            return convertToDTO(trabalhoExistente);
        }
    }
    
    public void excluirTrabalho(String id) {
        if (databaseOperations != null) {
            // Produção: PostgreSQL
            boolean removido = databaseOperations.deleteTrabalhoById(id);
            if (!removido) {
                throw new ResourceNotFoundException("Trabalho não encontrado com ID: " + id);
            }
        } else {
            // Desenvolvimento: Memória
            boolean removido = Trabalho.trabalhos.removeIf(t -> t.getId().equals(id));
            if (!removido) {
                throw new ResourceNotFoundException("Trabalho não encontrado com ID: " + id);
            }
        }
    }
    
    public List<TrabalhoDTO> buscarTrabalhosPorEmpresa(String idEmpresa) {
        if (databaseOperations != null) {
            // Produção: PostgreSQL
            return databaseOperations.findTrabalhosByEmpresa(idEmpresa).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } else {
            // Desenvolvimento: Memória
            return Trabalho.trabalhos.stream()
                    .filter(t -> t.getIdEmpresa() != null && t.getIdEmpresa().equals(idEmpresa))
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
    }
    
    public List<TrabalhoDTO> buscarPorTipoTrabalho(String tipoTrabalho) {
        List<Trabalho> trabalhos;
        
        if (databaseOperations != null) {
            trabalhos = databaseOperations.findAllTrabalhos();
        } else {
            trabalhos = Trabalho.trabalhos;
        }
        
        return trabalhos.stream()
                .filter(t -> t.getTipoTrabalho() != null && 
                            t.getTipoTrabalho().toLowerCase().contains(tipoTrabalho.toLowerCase()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<TrabalhoDTO> buscarPorHabilidadesNecessarias(String habilidade) {
        List<Trabalho> trabalhos;
        
        if (databaseOperations != null) {
            trabalhos = databaseOperations.findAllTrabalhos();
        } else {
            trabalhos = Trabalho.trabalhos;
        }
        
        return trabalhos.stream()
                .filter(t -> t.getHabilidadesNecessarias() != null && 
                            t.getHabilidadesNecessarias().toLowerCase().contains(habilidade.toLowerCase()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<TrabalhoDTO> buscarPorQuantidadeVagas(String quantidadeMinima) {
        try {
            int minVagas = Integer.parseInt(quantidadeMinima);
            List<Trabalho> trabalhos;
            
            if (databaseOperations != null) {
                trabalhos = databaseOperations.findAllTrabalhos();
            } else {
                trabalhos = Trabalho.trabalhos;
            }
            
            return trabalhos.stream()
                    .filter(t -> {
                        if (t.getQuantidadeDeVagas() == null) return false;
                        try {
                            return Integer.parseInt(t.getQuantidadeDeVagas()) >= minVagas;
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    })
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            throw new BadRequestException("Formato de quantidade de vagas inválido");
        }
    }
    
    public List<TrabalhoDTO> buscarPorDescricao(String descricao) {
        List<Trabalho> trabalhos;
        
        if (databaseOperations != null) {
            trabalhos = databaseOperations.findAllTrabalhos();
        } else {
            trabalhos = Trabalho.trabalhos;
        }
        
        return trabalhos.stream()
                .filter(t -> t.getDescricaoTrabalho() != null && 
                            t.getDescricaoTrabalho().toLowerCase().contains(descricao.toLowerCase()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public void excluirTrabalhosPorEmpresa(String idEmpresa) {
        if (databaseOperations != null) {
            // Produção: PostgreSQL - implementar método específico se necessário
            List<Trabalho> trabalhos = databaseOperations.findTrabalhosByEmpresa(idEmpresa);
            if (trabalhos.isEmpty()) {
                throw new ResourceNotFoundException("Nenhum trabalho encontrado para a empresa com ID: " + idEmpresa);
            }
            
            for (Trabalho trabalho : trabalhos) {
                databaseOperations.deleteTrabalhoById(trabalho.getId());
            }
        } else {
            // Desenvolvimento: Memória
            boolean removido = Trabalho.trabalhos.removeIf(t -> t.getIdEmpresa() != null && 
                                                                t.getIdEmpresa().equals(idEmpresa));
            
            if (!removido) {
                throw new ResourceNotFoundException("Nenhum trabalho encontrado para a empresa com ID: " + idEmpresa);
            }
        }
    }
    
    private void atualizarCamposTrabalho(Trabalho trabalho, TrabalhoDTO dto) {
        if (dto.getIdEmpresa() != null && !dto.getIdEmpresa().trim().isEmpty()) {
            trabalho.setIdEmpresa(dto.getIdEmpresa());
        }
        if (dto.getDescricaoTrabalho() != null && !dto.getDescricaoTrabalho().trim().isEmpty()) {
            trabalho.setDescricaoTrabalho(dto.getDescricaoTrabalho());
        }
        if (dto.getQuantidadeDeVagas() != null) {
            trabalho.setQuantidadeDeVagas(dto.getQuantidadeDeVagas());
        }
        if (dto.getTipoTrabalho() != null) {
            trabalho.setTipoTrabalho(dto.getTipoTrabalho());
        }
        if (dto.getHabilidadesNecessarias() != null) {
            trabalho.setHabilidadesNecessarias(dto.getHabilidadesNecessarias());
        }
        if (dto.getDescricao() != null) {
            trabalho.setDescricao(dto.getDescricao());
        }
    }
    
    private void validarDadosTrabalho(TrabalhoDTO trabalhoDTO) {
        if (trabalhoDTO.getIdEmpresa() == null || trabalhoDTO.getIdEmpresa().trim().isEmpty()) {
            throw new BadRequestException("ID da empresa é obrigatório");
        }
        
        if (trabalhoDTO.getDescricaoTrabalho() == null || trabalhoDTO.getDescricaoTrabalho().trim().isEmpty()) {
            throw new BadRequestException("Descrição do trabalho é obrigatória");
        }
        
        if (trabalhoDTO.getTipoTrabalho() == null || trabalhoDTO.getTipoTrabalho().trim().isEmpty()) {
            throw new BadRequestException("Tipo de trabalho é obrigatório");
        }
        
        // Validar quantidade de vagas se fornecida
        if (trabalhoDTO.getQuantidadeDeVagas() != null && !trabalhoDTO.getQuantidadeDeVagas().isEmpty()) {
            try {
                int vagas = Integer.parseInt(trabalhoDTO.getQuantidadeDeVagas());
                if (vagas <= 0) {
                    throw new BadRequestException("Quantidade de vagas deve ser um número positivo");
                }
            } catch (NumberFormatException e) {
                throw new BadRequestException("Formato de quantidade de vagas inválido");
            }
        }
    }
    
    private TrabalhoDTO convertToDTO(Trabalho trabalho) {
        return new TrabalhoDTO(
                trabalho.getId(),
                trabalho.getIdEmpresa(),
                trabalho.getNomeEmpresa(),
                trabalho.getDescricaoTrabalho(),
                trabalho.getQuantidadeDeVagas(),
                trabalho.getTipoTrabalho(),
                trabalho.getHabilidadesNecessarias(),
                trabalho.getDescricao(),
                trabalho.getDataCadastro()
        );
    }
}