package com.generation.infostore.controller;

import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.infostore.model.Cliente;
import com.generation.infostore.repository.ClienteRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ClienteController {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
 
	@GetMapping
	public ResponseEntity<List<Cliente>> getAll(){
		
		return ResponseEntity.ok(clienteRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Cliente>getById(@PathVariable Long id) {
		return clienteRepository.findById(id)
				.map(r -> ResponseEntity.ok(r))
				.orElse(ResponseEntity.notFound().build());
		
		
		
	}
	
	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Cliente>> getByNome(@PathVariable String nome){
		
		return ResponseEntity.ok(clienteRepository.findAllByNomeContainingIgnoreCase(nome));	
	}
	
	@PostMapping
	public ResponseEntity<Cliente> post(@Valid @RequestBody Cliente cliente) {
	     
	    cliente.setId(null); 
	    
	    // Salva e devolve HTTP 201 CREATED
	    return ResponseEntity.status(HttpStatus.CREATED)
	            .body(clienteRepository.save(cliente));
	}
	
	@PutMapping
	public ResponseEntity<Cliente> put(@Valid @RequestBody Cliente cliente) {
	    
	     
	    if (cliente.getId() == null) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O ID do Cliente não pode ser nulo para atualização!", null);
	    }

 
	    if (clienteRepository.existsById(cliente.getId())) {
	        
	        
	        return ResponseEntity.status(HttpStatus.OK)
	                .body(clienteRepository.save(cliente));
	    }
	    
	 
	    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "O Cliente não existe!", null);
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id){
		
		Optional<Cliente> cliente = clienteRepository.findById(id);
		
		if(cliente.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		clienteRepository.deleteById(id);
		
		// DELETE FROM tb_postagens WHERE id = ?;
	}
	
}
