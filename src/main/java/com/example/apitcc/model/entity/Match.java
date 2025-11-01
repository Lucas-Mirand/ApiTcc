package com.example.apitcc.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Match extends ItemBase {
    private String idUsuario; // Foreign Key para Usuario
    private String idTrabalho; // Foreign Key para Trabalho
    private String status;
    private String dataMatch;
    private String dataInicio;
    private String dataTermino;
    private String horasTrabalhas; // FLOAT como String para compatibilidade
    private String avaliacao; // INT como String para compatibilidade
    
    public Match(String id, String descricao, String idUsuario, String idTrabalho, 
                String status, String dataMatch, String dataInicio, String dataTermino, 
                String horasTrabalhas, String avaliacao, String dataCadastro) {
        super(id, descricao, dataCadastro);
        this.idUsuario = idUsuario;
        this.idTrabalho = idTrabalho;
        this.status = status;
        this.dataMatch = dataMatch;
        this.dataInicio = dataInicio;
        this.dataTermino = dataTermino;
        this.horasTrabalhas = horasTrabalhas;
        this.avaliacao = avaliacao;
    }
    
    // Getters e Setters
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
    
    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = super.toMap();
        map.put("idUsuario", this.idUsuario);
        map.put("idTrabalho", this.idTrabalho);
        map.put("status", this.status);
        map.put("dataMatch", this.dataMatch);
        map.put("dataInicio", this.dataInicio);
        map.put("dataTermino", this.dataTermino);
        map.put("horasTrabalhas", this.horasTrabalhas);
        map.put("avaliacao", this.avaliacao);
        return map;
    }
    
    // Lista estática para armazenamento em memória (para desenvolvimento)
    public static List<Match> matches = new ArrayList<>();
    public static int proximoId = 1;
}