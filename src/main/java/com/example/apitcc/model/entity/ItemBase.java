package com.example.apitcc.model.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public abstract class ItemBase {
    protected String id;
    protected String descricao;
    protected String dataCadastro;
    
    public ItemBase(String id, String descricao, String dataCadastro) {
        this.id = id;
        this.descricao = descricao;
        this.dataCadastro = dataCadastro;
    }
    
    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public String getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(String dataCadastro) { this.dataCadastro = dataCadastro; }
    
    // MÃ©todo estÃ¡tico para obter data/hora atual
    public static String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
    
    // MÃ©todo para converter para Map (pode ser sobrescrito)
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("id", this.id);
        map.put("descricao", this.descricao);
        map.put("dataCadastro", this.dataCadastro);
        return map;
    }
}