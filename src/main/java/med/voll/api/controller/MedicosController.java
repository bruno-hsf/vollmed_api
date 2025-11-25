package med.voll.api.controller;


import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
//    public List<DadosListagemMedico> listar(){
//        //só que ele ta reclamando: olha, la dentro desse DTO não tem um construtor que recebe um objeto do tipo medico
//        //tivemos que criar o construtor em DadosCadastroMedico, pois DadosListagemMedico::new equivale a .map(medico -> new DadosListagemMedico(medico))
//        //ou seja, para cada objeto Medico, cria um DadosListagemMedico.
//        return repository.findAll().stream().map(DadosListagemMedico::new).toList();

    //retorno agora é um Page que tambem recebe generics<>

    //PageableDefault configuramos previamente qtde de itens por pagina qtde de paginas se não estier definido no parametro da url
    //url sobrescreve pageabledefault. Ele recebe um Array que posso ordenar por multiplos atributos
    //Spring, se não for informado os parametros de paginacao, o padrao é esse: 10 registros por pagina ordenados pelo nome
    public Page<DadosListagemMedico> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        //public Page<DadosListagemMedico> listar(Pageable paginacao){
        //mesmo metodo, mas agora usando paginacao
        //usar o Pageable de org.springframework
        //o metodo findall tem uma sobrecarga que recebe uma paginacao como parametro
        //podemos tirar o Stream e o tolist, pois o page já faz essa funcao
        return repository.findAll(paginacao).map(DadosListagemMedico::new);

        //no postman passar http://localhost:8080/medicos?size=1&page=2 siginifica que depois de ? temos dois parametros
        //size indica quantos elementos por pagina e page indica qual pagina deve ser mostrada
        //ordenar pelo nome: http://localhost:8080/medicos?sort=nome
        //ordenar pelo nome decrescente: http://localhost:8080/medicos?sort=nome,desc
        //ordenar pelo nome decrescente com um registro na pagina 2: http://localhost:8080/medicos?sort=nome,desc&size=1&page=2
    }

    @PutMapping
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados) {
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);
        /**
         * Processo de atualização de um médico:
         *
         * 1) O cliente envia um JSON com os novos dados — nome, telefone, endereço etc.
         *    O Spring converte esse JSON para o DTO DadosAtualizacaoMedico.
         *
         * 2) Para atualizar, primeiro precisamos carregar o médico atual que está salvo no banco.
         *    Fazemos isso usando repository.getReferenceById(dados.id()),
         *    que retorna uma referência gerenciada pela JPA.
         *
         * 3) Com a entidade carregada, precisamos sobrescrever seus campos antigos
         *    com os novos valores enviados no DTO.
         *    Para manter o código organizado, delegamos essa lógica para o método
         *    medico.atualizarInformacoes(dados), implementado dentro da classe Medico.
         *
         * 4) Como o método está dentro de uma transação (@Transactional),
         *    ao final da requisição o Hibernate detecta automaticamente
         *    quais atributos da entidade foram alterados e executa o UPDATE no banco.
         *
         * Em resumo:
         * - Carregamos o médico existente no banco.
         * - Aplicamos as alterações vindas do DTO.
         * - A JPA faz o UPDATE automaticamente ao final da transação.
         */

    }
}
