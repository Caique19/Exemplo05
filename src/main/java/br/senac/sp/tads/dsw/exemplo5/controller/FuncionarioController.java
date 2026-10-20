package br.senac.sp.tads.dsw.exemplo5.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.senac.sp.tads.dsw.exemplo5.model.Funcionario;
import br.senac.sp.tads.dsw.exemplo5.repository.FuncionarioRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

    private final FuncionarioRepository repository;

    public FuncionarioController(FuncionarioRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Funcionario> listarTodos() {
        return repository.findAll(); // Retorna 200 OK por padrão
    }
    
    @GetMapping("/{id}")
    public Funcionario buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionario não encontrado"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Funcionario criar(@RequestBody @Valid Funcionario funcionario) {
        return repository.save(funcionario);
    }
    
    @PutMapping("/{id}")
    public Funcionario atualizar(@PathVariable Long id,
                            @RequestBody @Valid Funcionario funcionarioAtualizado) {
    return repository.findById(id)
            .map(funcionarioExistente -> {
              funcionarioExistente.setNome(funcionarioAtualizado.getNome());
              funcionarioExistente.setDataContratacao(funcionarioAtualizado.getDataContratacao());
              funcionarioExistente.setTrabalhoRemoto(funcionarioAtualizado.getTrabalhoRemoto());
              funcionarioExistente.setDepartamento(funcionarioAtualizado.getDepartamento());
              return repository.save(funcionarioExistente);  
            })
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void apagar(@PathVariable Long id) {
        if(!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }
}
