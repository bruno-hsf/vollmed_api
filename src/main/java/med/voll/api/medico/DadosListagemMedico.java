package med.voll.api.medico;

//DTO para exibir dados de consulta de medico
public record DadosListagemMedico(String nome, String email, String crm, Especialidade especialidade) {

    public DadosListagemMedico(Medico medico) {
        //agora temos que chamar o proprio construtor do Record passando os dados de medico
        this(medico.getNome(), medico.getEmail(), medico.getCrm(), medico.getEspecialidade());

        //explicacao - Exemplo
//        public record Pessoa(String nome, int idade) {
//            public Pessoa(Aluno aluno) {
//                this(aluno.getNome(), aluno.getIdade());
//            }
//        }
//        Explicação:
//
//        this(…) chama o construtor do record (Pessoa(String, int))
//
//        mas passando os dados de outro objeto (Aluno)
    }
}
