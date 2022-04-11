package com.generation.blogpessoal.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;
import com.generation.blogpessoal.repository.TemaRepository;

@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostagemController {

	@Autowired //injeção de dependência e só funciona dentro dessa classe. Para funcionar em qualquer classe, usar o bin
	private PostagemRepository postagemRepository;
	
	@Autowired
	private TemaRepository temaRepository;
	
	@GetMapping("/all")
	public ResponseEntity<List<Postagem>> getAll () {
		return ResponseEntity.ok(postagemRepository.findAll()); // = SELECT * FROM tb_postagens
	}
	
	@GetMapping("/{id}") //id entre chaves entende o valor como sendo variável. Tem que ser valor numérico
	public ResponseEntity<Postagem> getById (@PathVariable Long id) {
		return postagemRepository.findById(id)						// = SELECT * FROM tb_postagens WHERE id = x
				.map(resposta -> ResponseEntity.ok(resposta)) 		//caso o id exista, retorna a postagem
				.orElse(ResponseEntity.notFound().build());  		// caso o id não exista, seja nulo, responde Not Found 
		
//		lambda ^ é equivalente a esse bloco de if 		
//		Optional <Postagem> resposta = postagemRepository.findById(id);
//		if (resposta.isPresent()) {
//			return ResponseEntity.ok(resposta);
//		} else {
//			return ResponseEntity.notFound().build();
//		}
	}
	
	@GetMapping("/titulo/{titulo}") //id entre chaves entende o valor como sendo variável. Tem que ser valor numérico
	public ResponseEntity<List<Postagem>> getByTitulo (@PathVariable String titulo) {
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo)); // = SELECT * FROM tb_postagens WHERE titulo = %t%

	}
	
	@PostMapping
	public ResponseEntity<Postagem> postPostagem (@Valid @RequestBody Postagem postagem) {
		return temaRepository.findById(postagem.getTema().getId())
				.map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem)))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@PutMapping
	public ResponseEntity<Postagem> putPostagem (@Valid @RequestBody Postagem postagem) {
		if (postagemRepository.existsById(postagem.getId())) {
			if(temaRepository.existsById(postagem.getTema().getId())) {
				return ResponseEntity.status(HttpStatus.OK).body(postagemRepository.save(postagem));
			} else {
				return ResponseEntity.notFound().build();
			} 
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePostagem (@PathVariable Long id) {
		return postagemRepository.findById(id)
				.map(resposta ->{
					postagemRepository.deleteById(id);
					return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
				})
		.orElse(ResponseEntity.notFound().build());
	}

	
}
