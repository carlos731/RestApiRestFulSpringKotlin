package br.com.api.mapper.custom

import br.com.api.data.vo.v2.PersonVO
import br.com.api.model.Person
import org.springframework.stereotype.Service
import java.util.*

@Service
class PersonMapper {

    fun mapEntityToVO(person: Person): PersonVO {
        val vo = PersonVO()
        vo.id = person.id
        vo.address = person.address
        vo.birthDay = Date()
        vo.firstName = person.firstName
        vo.lastName = person.lastName
        vo.gender = person.gender
        return vo
    }

    fun mapVOToEntity(personVO: PersonVO): Person {
        val entity = Person()
        entity.id = personVO.id
        entity.address = personVO.address
        //entity.birthDay = personVO.birthDate
        entity.firstName = personVO.firstName
        entity.lastName = personVO.lastName
        entity.gender = personVO.gender
        return entity
    }
}