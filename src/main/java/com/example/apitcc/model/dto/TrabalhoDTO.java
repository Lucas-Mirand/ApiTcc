package com.example.apitcc.model.dto;

public class TrabalhoDTO {
    private String id;
    private String idEmpresa;
    private String nomeEmpresa;
    private String descricaoTrabalho;
    private String quantidadeDeVagas;
    private String tipoTrabalho;
    private String habilidadesNecessarias;
    private String descricao;
    private String dataCadastro;
    
    // Construtor vazio
    public TrabalhoDTO() {}
    
    // Construtor completo
    public TrabalhoDTO(String id, String idEmpresa, String nomeEmpresa, String descricaoTrabalho,
                      String quantidadeDeVagas, String tipoTrabalho, String habilidadesNecessarias,
                      String descricao, String dataCadastro) {
        this.id = id;
        this.idEmpresa = idEmpresa;
        this.nomeEmpresa = nomeEmpresa;
        this.descricaoTrabalho = descricaoTrabalho;
        this.quantidadeDeVagas = quantidadeDeVagas;
        this.tipoTrabalho = tipoTrabalho;
        this.habilidadesNecessarias = habilidadesNecessarias;
        this.descricao = descricao;
        this.dataCadastro = dataCadastro;
    }
    
    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getIdEmpresa() { return idEmpresa; }
    public void setIdEmpresa(String idEmpresa) { this.idEmpresa = idEmpresa; }
    
    public String getNomeEmpresa() { return nomeEmpresa; }
    public void setNomeEmpresa(String nomeEmpresa) { this.nomeEmpresa = nomeEmpresa; }
    
    public String getDescricaoTrabalho() { return descricaoTrabalho; }
    public void setDescricaoTrabalho(String descricaoTrabalho) { this.descricaoTrabalho = descricaoTrabalho; }
    
    public String getQuantidadeDeVagas() { return quantidadeDeVagas; }
    public void setQuantidadeDeVagas(String quantidadeDeVagas) { this.quantidadeDeVagas = quantidadeDeVagas; }
    
    public String getTipoTrabalho() { return tipoTrabalho; }
    public void setTipoTrabalho(String tipoTrabalho) { this.tipoTrabalho = tipoTrabalho; }
    
    public String getHabilidadesNecessarias() { return habilidadesNecessarias; }
    public void setHabilidadesNecessarias(String habilidadesNecessarias) { this.habilidadesNecessarias = habilidadesNecessarias; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public String getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(String dataCadastro) { this.dataCadastro = dataCadastro; }
}