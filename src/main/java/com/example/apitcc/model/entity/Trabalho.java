package com.example.apitcc.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Trabalho extends ItemBase {
    private String idEmpresa; // Foreign Key para Empresa
    private String nomeEmpresa;
    private String descricaoTrabalho;
    private String quantidadeDeVagas; // INT como String para compatibilidade
    private String tipoTrabalho;
    private String habilidadesNecessarias;
    
    public Trabalho(String id, String descricao, String idEmpresa, String nomeEmpresa,
                   String descricaoTrabalho, String quantidadeDeVagas, String tipoTrabalho,
                   String habilidadesNecessarias, String dataCadastro) {
        super(id, descricao, dataCadastro);
        this.idEmpresa = idEmpresa;
        this.nomeEmpresa = nomeEmpresa;
        this.descricaoTrabalho = descricaoTrabalho;
        this.quantidadeDeVagas = quantidadeDeVagas;
        this.tipoTrabalho = tipoTrabalho;
        this.habilidadesNecessarias = habilidadesNecessarias;
    }
    
    // Getters e Setters
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
    
    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = super.toMap();
        map.put("idEmpresa", this.idEmpresa);
        map.put("nomeEmpresa", this.nomeEmpresa);
        map.put("descricaoTrabalho", this.descricaoTrabalho);
        map.put("quantidadeDeVagas", this.quantidadeDeVagas);
        map.put("tipoTrabalho", this.tipoTrabalho);
        map.put("habilidadesNecessarias", this.habilidadesNecessarias);
        return map;
    }
    
    // Lista estática para armazenamento em memória (para desenvolvimento)
    public static List<Trabalho> trabalhos = new ArrayList<>();
    public static int proximoId = 1;
}