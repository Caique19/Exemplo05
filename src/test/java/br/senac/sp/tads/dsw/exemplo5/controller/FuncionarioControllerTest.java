package br.senac.sp.tads.dsw.exemplo5.controller;


import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import br.senac.sp.tads.dsw.exemplo5.model.Departamento;
import br.senac.sp.tads.dsw.exemplo5.model.Funcionario;
import br.senac.sp.tads.dsw.exemplo5.repository.DepartamentoRepository;
import br.senac.sp.tads.dsw.exemplo5.repository.FuncionarioRepository;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional 
public class FuncionarioControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Departamento departamentoPadrao;

    @BeforeEach
    void setUp() {
        Departamento departamento = new Departamento();
        departamento.setNome("Desenvolvimento");
        departamento.setOrcamento(200000.00);
        departamentoPadrao = departamentoRepository.save(departamento);
    }

    @Test
    void deveCriarFuncionarioComSucesso() throws Exception {
        // 1 - Criar o objeto que queremos enviar
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Maria Silva");
        funcionario.setDataContratacao(LocalDate.of(2024, 1, 15));
        funcionario.setTrabalhoRemoto(true);
        funcionario.setDepartamento(departamentoPadrao);

        // Converter o objeto JAVA em para uma String (texto JSON)
        String jsonRequisicao = objectMapper.writeValueAsString(funcionario);

        // 2 - Enviar o POST para a url da API
        mockMvc.perform(post("/api/funcionarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequisicao))
        // 3 - Verificar se a resposta é o esperado
                .andExpect(status().isCreated()) // HTTP 201
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Maria Silva"))
                .andExpect(jsonPath("$.dataContratacao").value("2024-01-15"))
                .andExpect(jsonPath("$.trabalhoRemoto").value(true))
                .andExpect(jsonPath("$.departamento.id").value(departamentoPadrao.getId().intValue()));
    }

    @Test
    void deveRetornarErro400AoCriarFuncionarioComDataFutura() throws Exception {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("João Souza");
        funcionario.setDataContratacao(LocalDate.now().plusDays(10)); // data no futuro (inválida)
        funcionario.setTrabalhoRemoto(false);
        funcionario.setDepartamento(departamentoPadrao);

        String jsonRequisicao = objectMapper.writeValueAsString(funcionario);

        mockMvc.perform(post("/api/funcionarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequisicao))
                .andExpect(status().isBadRequest()); // HTTP 400
    }

    @Test
    void deveListarTodosOsFuncionarios() throws Exception {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Ana Costa");
        funcionario.setDataContratacao(LocalDate.of(2024, 1, 15));
        funcionario.setTrabalhoRemoto(false);
        funcionario.setDepartamento(departamentoPadrao);
        funcionarioRepository.save(funcionario); // coloca 1 registro no banco de dados

        mockMvc.perform(get("/api/funcionarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Ana Costa")); // Posição zero do array
    }

    @Test
    void deveBuscarFuncionarioPorId() throws Exception {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Carlos Lima");
        funcionario.setDataContratacao(LocalDate.of(2024, 1, 15));
        funcionario.setTrabalhoRemoto(true);
        funcionario.setDepartamento(departamentoPadrao);
        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionario);

        mockMvc.perform(get("/api/funcionarios/" + funcionarioSalvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Carlos Lima"));
    }

    @Test
    void deveAtualizarFuncionario() throws Exception {
        // 1 - Prepara o registro antigo no banco de dados
        Funcionario funcionarioSalvo = new Funcionario();
        funcionarioSalvo.setNome("Paula Mendes");
        funcionarioSalvo.setDataContratacao(LocalDate.of(2024, 1, 15));
        funcionarioSalvo.setTrabalhoRemoto(false);
        funcionarioSalvo.setDepartamento(departamentoPadrao);
        funcionarioRepository.save(funcionarioSalvo); // coloca 1 registro no banco de dados

        // 2 - Cria os novos dados para o PUT
        funcionarioSalvo.setNome("Paula Mendes Santos");
        funcionarioSalvo.setTrabalhoRemoto(true);
        String jsonAtualizado = objectMapper.writeValueAsString(funcionarioSalvo);

        // 3 - Executa e verifica se atualizou
        mockMvc.perform(put("/api/funcionarios/" + funcionarioSalvo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonAtualizado))
                .andExpect(status().isOk()) // HTTP 200
                .andExpect(jsonPath("$.nome").value("Paula Mendes Santos"))
                .andExpect(jsonPath("$.trabalhoRemoto").value(true));
    }

    @Test 
    void deveApagarFuncionario() throws Exception {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Roberto Alves");
        funcionario.setDataContratacao(LocalDate.of(2024, 1, 15));
        funcionario.setTrabalhoRemoto(false);
        funcionario.setDepartamento(departamentoPadrao);
        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionario);

        // Deleta e espera 204 No Content
        mockMvc.perform(delete("/api/funcionarios/" + funcionarioSalvo.getId()))
                .andExpect(status().isNoContent());

        // Confirma que apagou tentando buscar (espera 404 Not Found)
        mockMvc.perform(get("/api/funcionarios/" + funcionarioSalvo.getId()))
                .andExpect(status().isNotFound());
    }
}
