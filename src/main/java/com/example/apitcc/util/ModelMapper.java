package com.example.apitcc.util;

import com.example.apitcc.model.dto.EmpresaDTO;
import com.example.apitcc.model.dto.UsuarioDTO;
import com.example.apitcc.model.dto.TrabalhoDTO;
import com.example.apitcc.model.dto.MatchDTO;
import com.example.apitcc.model.entity.Empresa;
import com.example.apitcc.model.entity.Usuario;
import com.example.apitcc.model.entity.Trabalho;
import com.example.apitcc.model.entity.Match;
import com.example.apitcc.model.entity.ItemBase;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe utilitÃ¡ria para conversÃ£o entre Entities e DTOs
 * ContÃ©m mappers para todas as entidades: Empresa, Usuario, Trabalho e Match
 */
public class ModelMapper {
    
    // =================== EMPRESA MAPPERS ===================
    
    /**
     * Converte Empresa Entity para EmpresaDTO
     */
    public static EmpresaDTO toEmpresaDTO(Empresa empresa) {
        if (empresa == null) {
            return null;
        }
        
        return new EmpresaDTO(
                empresa.getId(),
                empresa.getNome(),
                empresa.getEmail(),
                empresa.getCnpj(),
                empresa.getTelefone(),
                empresa.getEndereco(),
                empresa.getAreaAtuacao(),
                empresa.getPublicoAlvo(),
                empresa.getSenha(),
                empresa.getFoto(),
                empresa.getDescricao(),
                empresa.getDataCadastro()
        );
    }
    
    /**
     * Converte EmpresaDTO para Empresa Entity
     */
    public static Empresa toEmpresaEntity(EmpresaDTO dto) {
        if (dto == null) {
            return null;
        }
        
        String dataCadastro = dto.getDataCadastro() != null ? 
                            dto.getDataCadastro() : ItemBase.getCurrentDateTime();
        
        return new Empresa(
                dto.getId(),
                dto.getDescricao(),
                dto.getNome(),
                dto.getEmail(),
                dto.getCnpj(),
                dto.getTelefone(),
                dto.getEndereco(),
                dto.getAreaAtuacao(),
                dto.getPublicoAlvo(),
                dto.getSenha(),
                dto.getFoto(),
                dataCadastro
        );
    }
    
    /**
     * Converte lista de Empresa Entity para lista de EmpresaDTO
     */
    public static List<EmpresaDTO> toEmpresaDTOList(List<Empresa> empresas) {
        return empresas.stream()
                .map(ModelMapper::toEmpresaDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Converte lista de EmpresaDTO para lista de Empresa Entity
     */
    public static List<Empresa> toEmpresaEntityList(List<EmpresaDTO> dtos) {
        return dtos.stream()
                .map(ModelMapper::toEmpresaEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * Atualiza uma entidade Empresa com dados do DTO (apenas campos nÃ£o nulos)
     */
    public static void updateEmpresaFromDTO(Empresa empresa, EmpresaDTO dto) {
        if (empresa == null || dto == null) {
            return;
        }
        
        if (dto.getNome() != null && !dto.getNome().trim().isEmpty()) {
            empresa.setNome(dto.getNome());
        }
        if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
            empresa.setEmail(dto.getEmail());
        }
        if (dto.getCnpj() != null && !dto.getCnpj().trim().isEmpty()) {
            empresa.setCnpj(dto.getCnpj());
        }
        if (dto.getTelefone() != null) {
            empresa.setTelefone(dto.getTelefone());
        }
        if (dto.getEndereco() != null) {
            empresa.setEndereco(dto.getEndereco());
        }
        if (dto.getAreaAtuacao() != null) {
            empresa.setAreaAtuacao(dto.getAreaAtuacao());
        }
        if (dto.getPublicoAlvo() != null) {
            empresa.setPublicoAlvo(dto.getPublicoAlvo());
        }
        if (dto.getSenha() != null && !dto.getSenha().trim().isEmpty()) {
            empresa.setSenha(dto.getSenha());
        }
        if (dto.getFoto() != null) {
            empresa.setFoto(dto.getFoto());
        }
        if (dto.getDescricao() != null) {
            empresa.setDescricao(dto.getDescricao());
        }
    }
    
    // =================== USUARIO MAPPERS ===================
    
    /**
     * Converte Usuario Entity para UsuarioDTO
     */
    public static UsuarioDTO toUsuarioDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getDataDeNascimento(),
                usuario.getSenha(),
                usuario.getHabilidades(),
                usuario.getHoras(),
                usuario.getNome(),
                usuario.getFoto(),
                usuario.getDescricao(),
                usuario.getDataCadastro()
        );
    }
    
    /**
     * Converte UsuarioDTO para Usuario Entity
     */
    public static Usuario toUsuarioEntity(UsuarioDTO dto) {
        if (dto == null) {
            return null;
        }
        
        String dataCadastro = dto.getDataCadastro() != null ? 
                            dto.getDataCadastro() : ItemBase.getCurrentDateTime();
        
        return new Usuario(
                dto.getId(),
                dto.getDescricao(),
                dto.getEmail(),
                dto.getTelefone(),
                dto.getDataDeNascimento(),
                dto.getSenha(),
                dto.getHabilidades(),
                dto.getHoras(),
                dto.getNome(),
                dto.getFoto(),
                dataCadastro
        );
    }
    
    /**
     * Converte lista de Usuario Entity para lista de UsuarioDTO
     */
    public static List<UsuarioDTO> toUsuarioDTOList(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(ModelMapper::toUsuarioDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Converte lista de UsuarioDTO para lista de Usuario Entity
     */
    public static List<Usuario> toUsuarioEntityList(List<UsuarioDTO> dtos) {
        return dtos.stream()
                .map(ModelMapper::toUsuarioEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * Atualiza uma entidade Usuario com dados do DTO (apenas campos nÃ£o nulos)
     */
    public static void updateUsuarioFromDTO(Usuario usuario, UsuarioDTO dto) {
        if (usuario == null || dto == null) {
            return;
        }
        
        if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
            usuario.setEmail(dto.getEmail());
        }
        if (dto.getTelefone() != null) {
            usuario.setTelefone(dto.getTelefone());
        }
        if (dto.getDataDeNascimento() != null) {
            usuario.setDataDeNascimento(dto.getDataDeNascimento());
        }
        if (dto.getSenha() != null && !dto.getSenha().trim().isEmpty()) {
            usuario.setSenha(dto.getSenha());
        }
        if (dto.getHabilidades() != null) {
            usuario.setHabilidades(dto.getHabilidades());
        }
        if (dto.getHoras() != null) {
            usuario.setHoras(dto.getHoras());
        }
        if (dto.getNome() != null && !dto.getNome().trim().isEmpty()) {
            usuario.setNome(dto.getNome());
        }
        if (dto.getFoto() != null) {
            usuario.setFoto(dto.getFoto());
        }
        if (dto.getDescricao() != null) {
            usuario.setDescricao(dto.getDescricao());
        }
    }
    
    // =================== TRABALHO MAPPERS ===================
    
    /**
     * Converte Trabalho Entity para TrabalhoDTO
     */
    public static TrabalhoDTO toTrabalhoDTO(Trabalho trabalho) {
        if (trabalho == null) {
            return null;
        }
        
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
    
    /**
     * Converte TrabalhoDTO para Trabalho Entity
     */
    public static Trabalho toTrabalhoEntity(TrabalhoDTO dto) {
        if (dto == null) {
            return null;
        }
        
        String dataCadastro = dto.getDataCadastro() != null ? 
                            dto.getDataCadastro() : ItemBase.getCurrentDateTime();
        
        return new Trabalho(
                dto.getId(),
                dto.getDescricao(),
                dto.getIdEmpresa(),
                dto.getNomeEmpresa(),
                dto.getDescricaoTrabalho(),
                dto.getQuantidadeDeVagas(),
                dto.getTipoTrabalho(),
                dto.getHabilidadesNecessarias(),
                dataCadastro
        );
    }
    
    /**
     * Converte lista de Trabalho Entity para lista de TrabalhoDTO
     */
    public static List<TrabalhoDTO> toTrabalhoDTOList(List<Trabalho> trabalhos) {
        return trabalhos.stream()
                .map(ModelMapper::toTrabalhoDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Converte lista de TrabalhoDTO para lista de Trabalho Entity
     */
    public static List<Trabalho> toTrabalhoEntityList(List<TrabalhoDTO> dtos) {
        return dtos.stream()
                .map(ModelMapper::toTrabalhoEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * Atualiza uma entidade Trabalho com dados do DTO (apenas campos nÃ£o nulos)
     */
    public static void updateTrabalhoFromDTO(Trabalho trabalho, TrabalhoDTO dto) {
        if (trabalho == null || dto == null) {
            return;
        }
        
        if (dto.getIdEmpresa() != null && !dto.getIdEmpresa().trim().isEmpty()) {
            trabalho.setIdEmpresa(dto.getIdEmpresa());
        }
        if (dto.getNomeEmpresa() != null && !dto.getNomeEmpresa().trim().isEmpty()) {
            trabalho.setNomeEmpresa(dto.getNomeEmpresa());
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
    
    // =================== MATCH MAPPERS ===================
    
    /**
     * Converte Match Entity para MatchDTO
     */
    public static MatchDTO toMatchDTO(Match match) {
        if (match == null) {
            return null;
        }
        
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
    
    /**
     * Converte MatchDTO para Match Entity
     */
    public static Match toMatchEntity(MatchDTO dto) {
        if (dto == null) {
            return null;
        }
        
        String dataCadastro = dto.getDataCadastro() != null ? 
                            dto.getDataCadastro() : ItemBase.getCurrentDateTime();
        
        return new Match(
                dto.getId(),
                dto.getDescricao(),
                dto.getIdUsuario(),
                dto.getIdTrabalho(),
                dto.getStatus(),
                dto.getDataMatch(),
                dto.getDataInicio(),
                dto.getDataTermino(),
                dto.getHorasTrabalhas(),
                dto.getAvaliacao(),
                dataCadastro
        );
    }
    
    /**
     * Converte lista de Match Entity para lista de MatchDTO
     */
    public static List<MatchDTO> toMatchDTOList(List<Match> matches) {
        return matches.stream()
                .map(ModelMapper::toMatchDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Converte lista de MatchDTO para lista de Match Entity
     */
    public static List<Match> toMatchEntityList(List<MatchDTO> dtos) {
        return dtos.stream()
                .map(ModelMapper::toMatchEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * Atualiza uma entidade Match com dados do DTO (apenas campos nÃ£o nulos)
     */
    public static void updateMatchFromDTO(Match match, MatchDTO dto) {
        if (match == null || dto == null) {
            return;
        }
        
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
}