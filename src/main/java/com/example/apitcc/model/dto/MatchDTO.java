package com.example.apitcc.model.dto;

public class MatchDTO {
    private String id;
    private String idUsuario;
    private String idTrabalho;
    private String status;
    private String dataMatch;
    private String dataInicio;
    private String dataTermino;
    private String horasTrabalhas;
    private String avaliacao;
    private String descricao;
    private String dataCadastro;
    
    // Construtor vazio
    public MatchDTO() {}
    
    // Construtor completo
    public MatchDTO(String id, String idUsuario, String idTrabalho, String status,
                   String dataMatch, String dataInicio, String dataTermino,
                   String horasTrabalhas, String avaliacao, String descricao, String dataCadastro) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idTrabalho = idTrabalho;
        this.status = status;
        this.dataMatch = dataMatch;
        this.dataInicio = dataInicio;
        this.dataTermino = dataTermino;
        this.horasTrabalhas = horasTrabalhas;
        this.avaliacao = avaliacao;
        this.descricao = descricao;
        this.dataCadastro = dataCadastro;
    }
    
    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getIdUsuario() { return idUsuario; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }
    
    public String getIdTrabalho() { return idTrabalho; }
    public void setIdTrabalho(String idTrabalho) { this.idTrabalho = idTrabalho; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getDataMatch() { return dataMatch; }
    public void setDataMatch(String dataMatch) { this.dataMatch = dataMatch; }
    
    public String getDataInicio() { return dataInicio; }
    public void setDataInicio(String dataInicio) { this.dataInicio = dataInicio; }
    
    public String getDataTermino() { return dataTermino; }
    public void setDataTermino(String dataTermino) { this.dataTermino = dataTermino; }
    
    public String getHorasTrabalhas() { return horasTrabalhas; }
    public void setHorasTrabalhas(String horasTrabalhas) { this.horasTrabalhas = horasTrabalhas; }
    
    public String getAvaliacao() { return avaliacao; }
    public void setAvaliacao(String avaliacao) { this.avaliacao = avaliacao; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public String getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(String dataCadastro) { this.dataCadastro = dataCadastro; }
}