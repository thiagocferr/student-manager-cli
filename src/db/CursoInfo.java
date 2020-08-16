package db;

import java.util.List;

import model.*;

// Class that aggregates various informations about a course
public class CursoInfo {
    private Curso curso;
    private Professor professor;
    private List<Aluno> alunos;

    public CursoInfo () {}

    public CursoInfo(Curso curso, Professor professor) {
        this.curso = curso;
        this.professor = professor;
    }

    public Curso getCurso() {
        return this.curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Professor getProfessor() {
        return this.professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public List<Aluno> getAlunos() {
        return this.alunos;
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }
}