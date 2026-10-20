package br.senac.sp.tads.dsw.exemplo5.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.senac.sp.tads.dsw.exemplo5.model.Departamento;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
    
}
