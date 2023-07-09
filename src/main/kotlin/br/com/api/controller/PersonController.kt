package br.com.api.controller

import br.com.api.data.vo.v1.PersonVO
import br.com.api.data.vo.v2.PersonVO as PersonVOV2
import br.com.api.services.PersonService
import br.com.api.util.MediaType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.concurrent.atomic.AtomicLong

@RestController
@RequestMapping("/api/person/v1")
class PersonController {

    val counter: AtomicLong = AtomicLong()

    @Autowired
    private lateinit var service: PersonService
    //var service: PersonService = PersonService() // Igual ao código de cima so que não precisa mais.

    @GetMapping(
        //method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML]
    )
    fun findAll(): List<PersonVO> {
        return service.findAll()
    }

    @GetMapping(
        value = ["/{id}"],
        //method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML])
    fun findById(@PathVariable(value = "id") id: Long): PersonVO {
        return service.findById(id)
    }

    @PostMapping(
        //method = [RequestMethod.POST],
        consumes = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML],
        produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML]
    )
    fun create(@RequestBody personVO: PersonVO): PersonVO {
        return service.create(personVO)
    }

    @PostMapping(
        value = ["/v2"],
        //method = [RequestMethod.POST],
        consumes = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML],
        produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML]
    )
    fun createV2(@RequestBody personVO: PersonVOV2): PersonVOV2 {
        return service.createV2(personVO)
    }

    @PutMapping(
        //method = [RequestMethod.PUT],
        consumes = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML],
        produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML]
    )
    fun update(@RequestBody personVO: PersonVO): PersonVO {
        return service.update(personVO)
    }

    @DeleteMapping(
        //method = [RequestMethod.DELETE],
        value = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML]
    )
    fun delete(@PathVariable(value = "id") id: Long) : ResponseEntity<*>{
        service.delete(id)
        return ResponseEntity.noContent().build<Any>()
    }

}