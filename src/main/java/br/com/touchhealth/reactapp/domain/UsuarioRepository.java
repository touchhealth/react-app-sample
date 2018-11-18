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
