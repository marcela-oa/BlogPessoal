package com.generation.blogpessoal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.model.Tema;

@Repository
public interface PostagemRepository extends JpaRepository<Postagem, Long> {

	public List<Postagem> findAllByTituloContainingIgnoreCase(String titulo); 
	// SELECT * FROM tb_postagens WHERE titulo LIKE = %x%

	public Optional<Postagem> findById(Tema tema);
}
