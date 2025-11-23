package med.voll.api.controller;


import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.medico.DadosCadastroMedico;
import med.voll.api.medico.DadosListagemMedico;
import med.voll.api.medico.Medico;
import med.voll.api.medico.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("medicos")
public class MedicosController {

    //O Spring já sabe que é ele que vai instanciar e passar este atributo repository dentro da classe controller
    //estou injetando o repository como se fosse um atributo
    @Autowired
    private MedicoRepository repository;

    //se chegar uma requisicao do tipo post para a uril /medicos é para chamar o método cadastrar da classe medicoscontroller
    @PostMapping
    //como é um metodo de escrita, vou fazer um insert na tabela, preciso ter uma transação ativa com o BD
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroMedico dados) {
        //System.out.println(dados);
        //pergar o repository e mandar ele persistir médico no banco de dados
        // Ao cadastrar um médico, precisamos salvar um objeto do tipo Medico no banco.
        // Porém, o controller recebe apenas um DTO (DadosCadastroMedico), não a entidade JPA.
        // Por isso, convertemos o DTO em uma entidade usando o construtor da classe Medico.
        //
        // O fluxo fica assim:
        // 1) Criamos um novo Medico a partir dos dados do DTO: new Medico(dados)
        // 2) O Spring Data JPA salva a entidade no banco usando: repository.save(...)
        //
        // No construtor da classe Medico, fazemos a atribuição dos atributos:
        // this.nome = dados.nome();
        // this.email = dados.email();
        // this.crm = dados.crm();
        // this.especialidade = dados.especialidade();
        // this.endereco = new Endereco(dados.endereco()
        //repository, save, pois está aqui um novo objeto JPA do tipo Medico e eu passo os parametros que estão vindo do json da requisicao no construtor da entidade medico e lá dentro faz a atribuicao
        repository.save(new Medico(dados));
    }

    @GetMapping
    public List<DadosListagemMedico> listar(){
        //só que ele ta reclamando: olha, la dentro desse DTO não tem um construtor que recebe um objeto do tipo medico
        //tivemos que criar o construtor em DadosCadastroMedico, pois DadosListagemMedico::new equivale a .map(medico -> new DadosListagemMedico(medico))
        //ou seja, para cada objeto Medico, cria um DadosListagemMedico.
        return repository.findAll().stream().map(DadosListagemMedico::new).toList();
    }
}
