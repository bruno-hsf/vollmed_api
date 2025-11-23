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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
