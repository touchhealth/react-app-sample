# Apresentação

[Voltar](ETAPA1.md)

## 2. SpringBoot muito básico

O Spring Boot irá gerar uma aplicação `react-app-sample.war` com um tomcat embutido, permitindo que ele seja executado com um simples `Run` na classe principal, ou posteriormente com um `java -jar react-app-sample.war`.

### Configurando a APP

Várias coisas parecerão mágicas com spring-boot, pois várias configurações serão feitas através de convenções e de parâmetros que podem ser definidos no arquivo `application.properties`, e pelo fato de existir ou não uma biblioteca no classpath.

`application.properties`:

~~~ini
## Configurações da aplicação ##

basedir=.
datadir=${basedir}/data

## Datasource ##

spring.datasource.url=jdbc:h2:${datadir}/app-sample;MVCC=TRUE;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.tomcat.max-active=10

spring.h2.console.enabled=true

## JPA ##

spring.jpa.hibernate.ddl-auto=update

## Server ##

server.port=8080
server.servlet.path=/api/
server.compression.enabled=true
server.compression.mime-types=text/html,text/xml,text/plain,text/css,text/javascript,application/javascript
~~~

A aplicação começa com uma classe inicial com um método main e pelo menos a anotação `@SpringBootApplication`:

~~~java
package br.com.touchhealth.reactapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories
@EnableWebMvc
@EnableSpringDataWebSupport
public class Main {

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }

}
~~~

Ela vai habilitar uma série de comportamentos, como escanear componentes a partir do pacote dessa classe `br.com.touchhealth.reactapp.*`, irá procurar também por `@Entity`, e montará o suporte para transações, spring-mvc e spring-data jpa repositories.

### Entidade + Repositório + Controller

Criamos uma entidade `domain/Usuario.groovy`:

~~~groovy
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
~~~

Criamos o repositorio (DAO) `domains/UsuarioRepository.java`:

~~~java
package br.com.touchhealth.reactapp.domain;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    Page<Usuario> findAll(Pageable pageable);

    Page<Usuario> findAll(Example<Usuario> searchDto, Pageable pageable);

}
~~~

Criamos o controller para expor uma api REST `domain/UsuarioController.groovy`:

~~~groovy
package br.com.touchhealth.reactapp.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK
import static org.springframework.web.bind.annotation.RequestMethod.GET
import static org.springframework.web.bind.annotation.RequestMethod.POST

@Controller
@ResponseBody
@RequestMapping(path = "/usuario", produces = "application/json;charset=UTF-8")
class UsuarioController {

    @Autowired
    UsuarioRepository usuarioRepository

    @RequestMapping(method = GET)
    Page<Usuario> findAll(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
    }

    @RequestMapping(path = "/{id}", method = GET)
    ResponseEntity<Usuario> findById(@PathVariable("id") Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id)
        if (!usuario.isPresent()) return new ResponseEntity<Usuario>(NOT_FOUND)
        return new ResponseEntity<Usuario>(usuario.get(), OK)
    }

    @RequestMapping(method = POST, consumes = "application/json")
    ResponseEntity<Usuario> save(@RequestBody usuario) {
        Usuario novoUsuario = usuarioRepository.save(usuario)
        return new ResponseEntity<Usuario>(novoUsuario, OK)
    }

}
~~~

Pronto servidor montando, vamos para o cliente?

[Próximo](ETAPA3.md)