package br.com.touchhealth.reactapp.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Usuario {
    @Id @GeneratedValue
    Long id
    String login
    String nome
    String email
    String senha
    Boolean ativo
    String papel

    @Override
    String toString() {
        return login
    }
}
