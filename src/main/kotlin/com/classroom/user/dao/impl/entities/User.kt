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
    var id: Long? = null

    @Column(name = "first_name", length = 45, nullable = false)
    var firstName: String? = null

    @Column(name = "last_name", length = 45, nullable = false)
    var lastName: String? = null

    @Column(name = "email", length = 128, nullable = false, unique = true)
    var email: String? = null

    @Column(name = "password", length = 64, nullable = false)
    var password: String? = null

    @Column(length = 64)
    var photo: String? = null

    @Column(name = "enabled")
    var enabled: Boolean? = null

    @Column(name = "createdOn")
    var createdOn: OffsetDateTime? = null

    @Column(name = "modifiedOn")
    var modifiedOn: OffsetDateTime? = null

    @Column(name = "createdBy")
    var createdBy: String? = null

    @Column(name = "modifiedBy")
    var modifiedBy: String? = null

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "users_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: MutableSet<Role> = HashSet()
    /**
     * initialize/update dates.
     */
    @PrePersist
    @PreUpdate
    fun initializeDate() {
        if (this.id == null) {
            createdOn = OffsetDateTime.now(ZoneId.systemDefault())
        }
        modifiedOn = OffsetDateTime.now(ZoneId.systemDefault())
    }

    override fun toString(): String {
        return "User(id=$id, firstName=$firstName, lastName=$lastName, email=$email, password=$password," +
                " photo=$photo, enabled=$enabled, createdOn=$createdOn, modifiedOn=$modifiedOn, " +
                "createdBy=$createdBy, modifiedBy=$modifiedBy, roles=$roles)"
    }
}