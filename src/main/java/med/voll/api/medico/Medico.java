package med.voll.api.medico;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.endereco.Endereco;

//classe medico é a entidade JPA, ou seja, classe que representa a tabela no banco de dados
@Table(name = "medicos")
@Entity(name = "Medico")
//metodos do Lombo para criar getters,setters,equals(), hashcode()
@Getter
//gerar o construtor default sem argumentos que a JPA exige em todas as entidades
@NoArgsConstructor
//um construtor que recebe todos os campos
@AllArgsConstructor
//para gerar o equals e hascode em cima do Id e não em cima de todos os atributos
@EqualsAndHashCode(of = "id")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    //telefone foi adicionado depois para testar a migration V2__
    private String telefone;
    private String crm;

    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;


    //Embeddable Attibute
    //fica numa classe separada, mas no BD ele considera que os campos dessa classe endereco fazem parte da mesma tabela de medicos
    @Embedded
    private Endereco endereco;

    public Medico(DadosCadastroMedico dados) {
        this.nome = dados.nome();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.crm = dados.crm();
        this.especialidade = dados.especialidade();
        this.endereco = new Endereco(dados.endereco());
    }

    public void atualizarInformacoes(DadosAtualizacaoMedico dados) {
        //vou subistituir o nome do medico atual pelo nome do medico que chega no DTO
        if (dados.nome() != null) {
            this.nome = dados.nome();
        }
        if (dados.telefone() != null) {
            this.telefone = dados.telefone();
        }
        if (dados.endereco() != null) {
            this.endereco.atualizarInformacoes(dados.endereco());
        }
    }
    /**
     * Atualiza seletivamente os dados do médico com base nas informações recebidas no DTO.
     *
     * Nem todos os campos precisam ser enviados no JSON de atualização.
     * Quando um campo NÃO é enviado pelo cliente, o Spring o interpreta como null.
     *
     * Por isso, NÃO podemos simplesmente sobrescrever todos os atributos:
     *  - se fizéssemos this.nome = dados.nome(), por exemplo,
     *    e o nome não fosse enviado no JSON, ele seria substituído por null.
     *
     * Para evitar isso, fazemos verificações:
     *  - Apenas atualizamos um campo se o valor correspondente NÃO for nulo no DTO.
     *  - Assim, cada atributo é atualizado somente quando o cliente realmente envia esse campo.
     *
     * Exemplo:
     *  if (dados.nome() != null) {
     *      this.nome = dados.nome();
     *  }
     *
     * Essa lógica deve ser repetida para cada campo atualizável:
     * nome, telefone, endereço, etc.
     *
     * Dessa forma, garantimos uma atualização parcial (partial update)
     * preservando os valores anteriores quando o cliente não deseja alterá-los.
     */

}
