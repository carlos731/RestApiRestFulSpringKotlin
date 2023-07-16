package br.com.api.integrationtests.vo.wrappers

import br.com.api.integrationtests.vo.BookVO
import com.fasterxml.jackson.annotation.JsonProperty

class BookEmbeddedVO {

    @JsonProperty("bookVOList")
    var books: List<BookVO>? = null
}