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
