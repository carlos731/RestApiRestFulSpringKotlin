package br.com.api.controller

import br.com.api.data.vo.v1.PersonVO
import br.com.api.services.PersonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.concurrent.atomic.AtomicLong

@RestController
@RequestMapping("/person")
class PersonController {

    val counter: AtomicLong = AtomicLong()

    @Autowired
    private lateinit var service: PersonService
    //var service: PersonService = PersonService() // Igual ao código de cima so que não precisa mais.

    @GetMapping(
        //method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findAll(): List<PersonVO> {
        return service.findAll()
    }

    @GetMapping(
        value = ["/{id}"],
        //method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findById(@PathVariable(value = "id") id: Long): PersonVO {
        return service.findById(id)
    }

    @PostMapping(
        //method = [RequestMethod.POST],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun create(@RequestBody personVO: PersonVO): PersonVO {
        return service.create(personVO)
    }

    @PutMapping(
        //method = [RequestMethod.PUT],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun update(@RequestBody personVO: PersonVO): PersonVO {
        return service.update(personVO)
    }

    @DeleteMapping(
        //method = [RequestMethod.DELETE],
        value = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun delete(@PathVariable(value = "id") id: Long) : ResponseEntity<*>{
        service.delete(id)
        return ResponseEntity.noContent().build<Any>()
    }

}