package br.com.api.model

import jakarta.persistence.*

import org.springframework.security.core.GrantedAuthority

@Entity
@Table(name = "permission")
class Permission : GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0;

    @Column(name = "description", length = 255)
    var description: String? = ""
    override fun getAuthority(): String {
        return description!!
    }
}