package aula7exemplos.aula6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author coelho
 */
public class ProgramaPrincipal {
    ///////////////////////MAIN MENU/////////////////////////
    private static final int MENU_ALUNOS = 1;
    private static final int MENU_PROFESSOR = 2;
    private static final int MENU_ENSINO = 3;
    private static final int MAX_ALUNOS = 30;
    private static final int OPCAO_SAIR = 4;
    //////////////////////BOSSES////////////////////////////
    public static final String CORDENADOR = "Pâmela Perini";
    public static final String DIRETOR = "Vitor Valente";
    /////////////////////MENU ALUNOS////////////////////////
    private static final int VER_CURSOS = 1;
    private static final int VER_CURSOS_PARTICULAR = 2;
    /////////////////////MENU PROFESSOR/////////////////////
    private static final int NOVA_AREA = 1;
    private static final int ALTERAR_NOTA = 2;
    private static final int DAR_NOTA = 3;
    private static final int NOVA_AREA = 4;
    
    
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Aluno[] alunos = new Aluno[MAX_ALUNOS];
        SetorEnsino ensino = new SetorEnsino(CORDENADOR, DIRETOR);
        int opcao;
        do {
            opcao = menu("MENU 1: \n [1] Aluno \n [2] Professor \n [3] Setor de Ensino \n [4] Sair", br);
            switch (opcao) {
                case MENU_ALUNOS:
                    menu_alunos("MENU 2: \n [1] Ver Cursos [2] Ver notas",
                            ensino,
                            alunos,
                            br);
                    break;
                case MENU_PROFESSOR:
                    System.out.println("Qual o seu número de siape, professor?");
                    int siape = Integer.parseInt(br.readLine());
                    int posicao_professor = login_professor(siape, ensino, br);

                    if (posicao_professor != -1) {
                        menu_professor("MENU 2: \n [1] Dar Notas de uma disciplina [2] Alterar uma nota [3] Adicionar Área [4] Remover Área",
                                posicao_professor,
                                ensino,
                                br);
                    } else {
                        System.err.println("SIAPE inválido.");
                    }
                    break;
                case MENU_ENSINO:
                    menu_ensino("MENU 2: \n [1] Adicionar Disciplina ao Curso [2] Cadastrar Aluno [3] Cadastrar Curso [4] Cadastrar Professor",
                            ensino,
                            alunos,
                            br);
            }
        } while (opcao != OPCAO_SAIR);
    }

    private static int menu(String opcoes, BufferedReader br) throws IOException {
        System.out.println(opcoes);
        String texto = br.readLine();
        int opcao = Integer.parseInt(texto);
        return opcao;
    }

    ////////////////// ALUNO ///////////////////////////////////////////
    private static void menu_alunos(String opcoes, SetorEnsino ensino, Aluno alunos[], BufferedReader br) throws IOException {
        int opcao = menu(opcoes, br);

        switch (opcao) {
            case VER_CURSOS:
                ver_cursos(ensino);
                break;
            case VER_CURSOS_PARTICULAR:
                System.out.println("Qual a sua matrícula, caro discente?");
                long matricula = Long.parseLong(br.readLine());

                ver_notas(ensino, alunos, matricula);
                break;
        }
    }

    private static void novo_aluno(SetorEnsino ensino, Aluno[] alunos, BufferedReader br) throws IOException {
        Aluno a = cadastra_aluno(ensino, br, alunos);

        cadastra_disciplinas_aluno(br, ensino, a);
    }

    private static void ver_notas(SetorEnsino ensino, Aluno alunos[], long matricula) {
        boolean aluno_nao_encontrado = true;

        if (alunos != null) {
            for (Aluno aluno : alunos) {
                if (aluno != null && aluno.getMatricula() == matricula) {//aluno matriculado
                    aluno_nao_encontrado = false;
                    Curso cursos[] = ensino.getCursos();

                    for (Curso curso : cursos) {
                        Disciplina disciplinas[] = curso.getDisciplinas();

                        for (Disciplina disciplina : disciplinas) {
                            Aluno a[] = disciplina.getAlunos();
                            int i = 0;

                            while (i != a.length && a[i].getMatricula() != matricula) {
                                i++;
                            }
                            float nota = disciplina.getNotas()[i];

                            System.out.println("A nota do aluno " + a[i].getNome() + " é de " + nota + " na disciplina " + disciplina.getNome());
                            break;
                        }
                    }
                }
            }
            if (aluno_nao_encontrado) {
                System.err.println("Aluno não matriculado no sistema.");
            }
        } else {
            System.err.println("Ainda não existem alunos no sistema.");
        }
    }

    private static Aluno cadastra_aluno(SetorEnsino ensino, BufferedReader br, Aluno[] alunos) throws IOException {
        Aluno a = cria_aluno(ensino, br);

        for (int i = 0; i < alunos.length; i++) {
            Aluno aluno = alunos[i];

            if (aluno == null) {
                alunos[i] = a;
            }
        }
        return a;
    }

    private static Aluno cria_aluno(SetorEnsino ensino, BufferedReader br) throws IOException {
        Aluno a = new Aluno();

        System.out.println("Nome:");
        a.setNome(br.readLine());
        System.out.println("Curso:");
        String nome_curso = br.readLine();
        Curso c = encontra_curso(ensino, nome_curso);

        a.setCurso(c);
        System.out.println("Matricula:");
        a.setMatricula(Long.parseLong(br.readLine()));
        System.out.println("Ingresso:");
        a.setAnoIngresso(Integer.parseInt(br.readLine()));
        a.setEhFormado(false);
        return a;
    }

    ////////////////// PROFESSOR ///////////////////////////////////////////
    private static void menu_professor(String opcoes, int posicao_professor, SetorEnsino ensino, BufferedReader br) throws IOException {
        int opcao = menu(opcoes, br);

        switch (opcao) {
            case 1:
                System.out.println("Qual a nova área, professor?");
                String area = br.readLine();

                if (nova_area(posicao_professor, ensino, area)) {
                    System.out.println("Área " + area + " cadastrada para o professor " + ensino.getProfessores()[posicao_professor].getNome());
                } else {
                    System.err.println("O limite de áreas foi atingido para o professor com siape " + ensino.getProfessores()[posicao_professor].getSiape());
                }
                break;
            case 2:
                System.out.println("Qual a disciplina?");
                String disciplina = br.readLine();
                System.out.println("Qual o curso?");
                String curso = br.readLine();
                System.out.println("Qual o nome do aluno?");
                String nome_aluno = br.readLine();
                System.out.println("Qual a sua nova nota?");
                float nova_nota = Float.parseFloat(br.readLine());

                if (alterar_nota(ensino, disciplina, curso, nome_aluno, nova_nota)) {
                    System.out.println("Nota "
                            + nova_nota
                            + " alterada para o aluno "
                            + nome_aluno
                            + " na disciplina "
                            + disciplina
                            + " do curso "
                            + curso
                            + ".");
                } else {
                    System.err.println("Aluno "
                            + nome_aluno
                            + " do curso "
                            + curso
                            + " não foi encontrado. Ele não está matriculado na disciplina "
                            + disciplina);
                }
                break;
            case 3:
                System.out.println("Qual a disciplina?");
                disciplina = br.readLine();
                System.out.println("Qual o curso?");
                curso = br.readLine();
                dar_notas(ensino, disciplina, curso, br);
                break;
            case 4:
                System.out.println("Qual a nova área, professor?");
                area = br.readLine();

                if (remover_area(posicao_professor, ensino, area)) {
                    System.out.println("Área " + area + " foi removido com sucesso para o professor " + ensino.getProfessores()[posicao_professor].getNome());
                } else {
                    System.err.println("A área " + area + " não estava cadastrada para o professor com siape " + ensino.getProfessores()[posicao_professor].getSiape());
                }
                break;
        }
    }

    private static Professor cadastra_professor(BufferedReader br, SetorEnsino ensino) throws IOException {
        Professor p;

        p = cria_professor(br);
        if (ensino.novoProfessor(p)) {
            System.out.println("Professor " + p.getNome() + " cadastrado.");
        } else {
            System.err.println("O limite de professores foi alcançado.");
        }
        return p;
    }

    private static Professor encontra_professor(SetorEnsino ensino, String nome_professor) {
        if (ensino.getProfessores() != null) {
            for (Professor p : ensino.getProfessores()) {
                if (p != null && p.getNome().equals(nome_professor)) {
                    return p;
                }
            }
        }
        return null;
    }

    private static Professor cria_professor(BufferedReader br) throws IOException {
        Professor p = new Professor();
        System.out.println("Nome:");
        p.setNome(br.readLine());
        System.out.println("SIAPE:");
        p.setSiape(Long.parseLong(br.readLine()));
        System.out.println("Quantas áreas?");
        int quantAreas = Integer.parseInt(br.readLine());

        p.setAreas(new String[quantAreas]);
        System.out.println("Informe as áreas:");
        for (int i = 0; i < quantAreas; i++) {
            if (p.getAreas() != null && p.getAreas()[i] != null) {
                p.getAreas()[i] = br.readLine();
            }
        }
        return p;
    }

    private static int login_professor(int siape, SetorEnsino ensino, BufferedReader br) {
        for (int i = 0; ensino.getProfessores() != null
                && i < ensino.getProfessores().length; i++) {
            if (ensino.getProfessores()[i] != null
                    && ensino.getProfessores()[i].getSiape() == siape) {
                return i;
            }
        }
        return -1;
    }

    private static boolean nova_area(int pos_professor, SetorEnsino ensino, String area) {
        if (ensino.getProfessores() != null) {
            String areas[] = ensino.getProfessores()[pos_professor].getAreas();

            for (int i = 0; areas != null && i < areas.length; i++) {
                if (areas[i] == null) {
                    areas[i] = area;
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean remover_area(int pos_professor, SetorEnsino ensino, String area) {
        if (ensino.getProfessores() != null) {
            String areas[] = ensino.getProfessores()[pos_professor].getAreas();

            for (int i = 0; areas != null && i < areas.length; i++) {
                if (areas[i] != null && areas[i].equals(area)) {
                    areas[i] = null;
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean alterar_nota(SetorEnsino ensino, String nome_disciplina, String nome_curso, String nome_aluno, float nova_nota) {
        return ensino.alterarNota(nome_disciplina, nome_curso, nome_aluno, nova_nota);
    }

    private static void dar_notas(SetorEnsino ensino, String disciplina, String nome_curso, BufferedReader br) throws IOException {
        Curso curso = encontra_curso(ensino, nome_curso);

        if (curso == null) {
            System.err.println("Curso " + nome_curso + " não encontrado.");
        } else {
            Disciplina d = encontra_disciplina(curso, disciplina);

            if (d == null) {
                System.err.println("Disciplina " + disciplina + " não encontrada.");
            } else {
                if (d != null && d.getAlunos() != null) {
                    System.out.println("Informe as notas dos alunos: ");
                    float notas[] = new float[d.getAlunos().length];
                    int i = 0;

                    while (i < d.getAlunos().length) {
                        System.out.println("Nota do aluno " + d.getAlunos()[i].getNome());
                        notas[i] = Float.parseFloat(br.readLine());
                        i++;
                    }
                    ensino.salvaNotas(notas, curso, d);
                }
            }
        }
    }

    ////////////////// ENSINO ///////////////////////////////////////////
    private static void menu_ensino(String opcoes, SetorEnsino ensino, Aluno[] alunos, BufferedReader br) throws IOException {
        int opcao = menu(opcoes, br);

        switch (opcao) {
            case 1:
                System.out.println("Qual o curso?");
                String nome_curso = br.readLine();
                Curso c = encontra_curso(ensino, nome_curso);

                if (c == null) {
                    System.err.println("Curso não encontrado. Cadastre-o.");
                    c = cadastra_curso(ensino, br);
                }
                System.out.println("Qual o professor?");
                String nome_professor = br.readLine();
                Professor p = encontra_professor(ensino, nome_professor);

                if (p == null) {
                    System.err.println("Professor não encontrado. Cadastre-o.");
                    p = cadastra_professor(br, ensino);
                }
                if (cadastra_disciplina(ensino, c, p, br)) {
                    System.out.println("Disciplina cadastrada com sucesso.");
                } else {
                    System.err.println("O limite de disciplinas foi excedido.");
                }
                break;
            case 2:
                novo_aluno(ensino, alunos, br);
                break;
            case 3:
                cadastra_curso(ensino, br);
                break;
            case 4:
                cadastra_professor(br, ensino);
                break;
        }
    }

    ///////////////////// Disciplinas //////////////////////////////////////////
    private static boolean cadastra_disciplina(SetorEnsino ensino, Curso c, Professor p, BufferedReader br) throws IOException {
        Disciplina d = cria_disciplina(br, p);

        return ensino.novaDisciplina(d, c);
    }

    private static Disciplina cria_disciplina(BufferedReader br, Professor p) throws IOException, NumberFormatException {
        System.out.println("Quantos alunos tem na turma?");
        int quantAlunos = Integer.parseInt(br.readLine());

        System.out.println("Qual o nome da disciplina?");
        String nome_disciplina = br.readLine();
        System.out.println("Qual o ano/semestre da disciplina?");
        int ano = Integer.parseInt(br.readLine());
        Disciplina d = new Disciplina(quantAlunos, p, nome_disciplina, ano);

        return d;
    }

    private static Disciplina[] recebe_disciplinas(SetorEnsino ensino, BufferedReader br) throws IOException {
        Disciplina[] disciplinas = new Disciplina[40];

        System.out.println("Digite [1] para terminar e [2] para cadastrar disciplina");
        int op = Integer.parseInt(br.readLine());

        for (int i = 0; op != 1 && i < disciplinas.length; i++) {
            System.out.println("Qual o nome do professor da disciplina?");
            String nome_professor = br.readLine();
            Professor professor = encontra_professor(ensino, nome_professor);

            if (professor == null) {
                System.err.println("O professor ainda não foi cadastrado. Informe seus dados.");
                professor = cria_professor(br);
                ensino.novoProfessor(professor);
            }
            disciplinas[i] = cria_disciplina(br, professor);
            System.out.println("\n Digite [1] para terminar e [2] para cadastrar disciplina");
            op = Integer.parseInt(br.readLine());
        }
        return disciplinas;
    }

    private static void cadastra_disciplinas_aluno(BufferedReader br, SetorEnsino ensino, Aluno a) throws IOException {
        if (ensino.matricularAluno(a)) {
            System.out.println("Aluno matriculado nas disciplinas do curso.");
        } else {
            System.err.println("Curso não encontrado. Cadastre-o.");
            Curso c = cadastra_curso(ensino, br);

            a.setCurso(c);
        }
    }

    private static Disciplina encontra_disciplina(Curso curso, String disciplina) throws IOException {
        if (curso != null && curso.getDisciplinas() != null) {
            for (Disciplina c : curso.getDisciplinas()) {
                if (c != null && c.getNome().equals(disciplina)) {
                    return c;
                }
            }
        }
        return null;
    }

    /////////////////////////// Curso //////////////////////////////////////////
    private static Curso cadastra_curso(SetorEnsino ensino, BufferedReader br) throws IOException {
        Curso c;

        c = cria_curso(ensino, br);
        if (ensino.novoCurso(c)) {
            System.out.println("Curso " + c.getNome());
        } else {
            System.out.println("O limite de cursos foi alcançado.");
        }
        return c;
    }

    private static Curso cria_curso(SetorEnsino ensino, BufferedReader br) throws IOException {
        Curso a = new Curso();

        System.out.println("Nome:");
        a.setNome(br.readLine());
        System.out.println("PPC:");
        a.setPpc(br.readLine());
        Disciplina[] disciplinas = recebe_disciplinas(ensino, br);

        a.setDisciplinas(disciplinas);
        return a;
    }

    private static Curso encontra_curso(SetorEnsino ensino, String nome) throws IOException {
        if (ensino.getCursos() != null) {
            for (Curso curso : ensino.getCursos()) {
                if (curso != null && curso.getNome().equals(nome)) {
                    return curso;
                }
            }
        }
        return null;
    }

    private static void ver_cursos(SetorEnsino ensino) {
        Curso cursos[] = ensino.getCursos();

        if (cursos != null) {
            for (Curso curso : cursos) {
                if (curso != null) {
                    System.out.println("Curso " + curso.getNome());
                    System.out.println("PPC: " + curso.getPpc());
                    System.out.println("Disciplinas: ");
                    if (curso.getDisciplinas() != null) {
                        for (Disciplina d : curso.getDisciplinas()) {
                            if (d != null) {
                                System.out.println(d.getNome());
                            }
                        }
                    }
                }
            }
        } else {
            System.err.println("Não existem cursos cadastrados.");
        }
    }
}
