package br.com.api.data.vo.v1

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.github.dozermapper.core.Mapping
import jakarta.persistence.Column
import org.springframework.hateoas.RepresentationModel
import java.util.*

@JsonPropertyOrder("id", "author", "launchDate", "price", "title")
data class BookVO (

    //@Mapping("id") // DozerMapper
    //@field:@JsonProperty("id")
    var id: Long = 0,
    var author: String = "",
    var launchDate: Date? = null,
    var price: Double = 0.0,
    var title: String = "",
): RepresentationModel<BookVO>()