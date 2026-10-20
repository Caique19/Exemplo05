package br.senac.sp.tads.dsw.exemplo5.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

@Entity 
public class Funcionario {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank 
    private String nome;

    //formato ano-mês-dia (sem horário)
    @PastOrPresent
    private LocalDate dataContratacao;

    @NotNull 
    private Boolean trabalhoRemoto;

    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;


    public Funcionario() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataContratacao() {
        return dataContratacao;
    }

    public void setDataContratacao(LocalDate dataContratacao) {
        this.dataContratacao = dataContratacao;
    }

    public Boolean getTrabalhoRemoto() {
        return trabalhoRemoto;
    }

    public void setTrabalhoRemoto(Boolean trabalhoRemoto) {
        this.trabalhoRemoto = trabalhoRemoto;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

}