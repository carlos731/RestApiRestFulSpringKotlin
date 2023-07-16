package br.com.api.services

import br.com.api.controller.PersonController
import br.com.api.data.vo.v1.PersonVO
import br.com.api.exceptions.RequiredObjectIsNullException
import br.com.api.data.vo.v2.PersonVO as PersonVOV2
import br.com.api.exceptions.ResourceNotFoundException
import br.com.api.mapper.DozerMapper
import br.com.api.mapper.custom.PersonMapper
import br.com.api.model.Person
import br.com.api.repository.PersonRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicLong
import java.util.logging.Logger

@Service
class PersonService {

    @Autowired
    private lateinit var repository: PersonRepository

    @Autowired
    private lateinit var mapper: PersonMapper

    @Autowired
    private lateinit var assembler: PagedResourcesAssembler<PersonVO>

    private val logger = Logger.getLogger(PersonService::class.java.name)

    fun findAll(pageable: Pageable): PagedModel<EntityModel<PersonVO>> {
        logger.info("Finding all people!")
        val persons =  repository.findAll(pageable)
        val vos = persons.map { p -> DozerMapper.parseObject(p, PersonVO::class.java) }
        vos.map { p-> p.add(linkTo(PersonController::class.java).slash(p.id).withSelfRel()) }
        return assembler.toModel(vos)
    }

    fun findPersonByName(firstName: String, pageable: Pageable): PagedModel<EntityModel<PersonVO>> {
        logger.info("Finding person by name!")
        val persons =  repository.findPersonByName(firstName, pageable)
        val vos = persons.map { p -> DozerMapper.parseObject(p, PersonVO::class.java) }
        vos.map { p-> p.add(linkTo(PersonController::class.java).slash(p.id).withSelfRel()) }
        return assembler.toModel(vos)
    }

    fun findById(id: Long): PersonVO {
        logger.info("Finding one PersonVO with ID $id!")
        var person =  repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this ID!")}
        val personVO: PersonVO = DozerMapper.parseObject(person, PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.id).withSelfRel()
        personVO.add(withSelfRel)
        return personVO
    }

    fun create(person: PersonVO?) : PersonVO{
        if(person == null) throw RequiredObjectIsNullException()
        logger.info("Creating one PersonVO with name ${person.firstName}!")
        var entity: Person = DozerMapper.parseObject(person, Person::class.java)
        val personVO: PersonVO = DozerMapper.parseObject(repository.save(entity), PersonVO::class.java) // Salva e converte para personVO para retornar reposta
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.id).withSelfRel()
        personVO.add(withSelfRel)
        return personVO
    }

    fun createV2(personVO: PersonVOV2) : PersonVOV2{
        logger.info("Creating one PersonVO with name ${personVO.firstName}!")
        var entity: Person = mapper.mapVOToEntity(personVO)
        return mapper.mapEntityToVO(repository.save(entity)) // Salva e converte para personVO para retornar reposta
    }

    fun update(personVO: PersonVO?) : PersonVO {
        if(personVO == null) throw RequiredObjectIsNullException()

        logger.info("Updating one PersonVO with id ${personVO.id}!")

        var entity = repository.findById(personVO.id)
            .orElseThrow { ResourceNotFoundException("No records found for this ID!")}

        entity.firstName = personVO.firstName
        entity.lastName = personVO.lastName
        entity.address = personVO.address
        entity.gender = personVO.gender
        val person: PersonVO = DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.id).withSelfRel()
        personVO.add(withSelfRel)
        return personVO
    }

    @Transactional
    fun disablePerson(id: Long): PersonVO {
        logger.info("disabling one PersonVO with ID $id!")
        repository.disablePerson(id)
        var person =  repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this ID!")}
        val personVO: PersonVO = DozerMapper.parseObject(person, PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.id).withSelfRel()
        personVO.add(withSelfRel)
        return personVO
    }

    fun delete(id: Long) {
        logger.info("Deleting one personVO with id ${id}!")

        var entity =  repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this ID!")}
        repository.delete(entity)
    }

}