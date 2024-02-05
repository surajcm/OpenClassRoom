package com.classroom.user.dao.impl.entities

import jakarta.persistence.*
import java.time.OffsetDateTime
import java.time.ZoneId

@Entity
//todo : add schema
@Table(name = "member")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var userId: Long? = null

    @Column(name = "first_name")
    var firstName: String? = null

    @Column(name = "last_name")
    var lastName: String? = null

    @Column(name = "email")
    var email: String? = null

    @Column(name = "password")
    var password: String? = null

    @Column(name = "role")
    var role: String? = null

    @Column(name = "createdOn")
    var createdOn: OffsetDateTime? = null

    @Column(name = "modifiedOn")
    var modifiedOn: OffsetDateTime? = null

    @Column(name = "createdBy")
    var createdBy: String? = null

    @Column(name = "modifiedBy")
    var modifiedBy: String? = null

    /**
     * initialize/update dates.
     */
    @PrePersist
    @PreUpdate
    fun initializeDate() {
        if (this.userId == null) {
            createdOn = OffsetDateTime.now(ZoneId.systemDefault())
        }
        modifiedOn = OffsetDateTime.now(ZoneId.systemDefault())
    }
}