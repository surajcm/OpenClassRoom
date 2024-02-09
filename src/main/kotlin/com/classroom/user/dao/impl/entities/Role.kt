package com.classroom.user.dao.impl.entities

import jakarta.persistence.*

@Entity
@Table(name = "roles")
class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null

    @Column(name = "name", length = 40, nullable = false, unique = true)
    var name: String? = null

    @Column(name = "description", length = 150, nullable = false)
    var description: String? = null
}