package br.senac.sp.tads.dsw.exemplo5.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.senac.sp.tads.dsw.exemplo5.model.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    
}