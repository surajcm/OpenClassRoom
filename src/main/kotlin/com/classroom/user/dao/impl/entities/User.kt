package com.classroom.user.dao.impl.entities

import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
@Table(name = "member")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "first_name", length = 45, nullable = false)
    var firstName: String = "",

    @Column(name = "last_name", length = 45, nullable = false)
    var lastName: String = "",

    @Column(name = "email", length = 128, nullable = false, unique = true)
    var email: String = "",

    @Column(name = "password", length = 64, nullable = false)
    var password: String = "",

    @Column(length = 64)
    var photo: String? = null,

    @Column(name = "enabled")
    var enabled: Boolean = true,

    @Column(name = "createdOn")
    var createdOn: OffsetDateTime? = null,

    @Column(name = "modifiedOn")
    var modifiedOn: OffsetDateTime? = null,

    @Column(name = "createdBy")
    var createdBy: String? = null,

    @Column(name = "modifiedBy")
    var modifiedBy: String? = null,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "users_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: MutableSet<Role> = mutableSetOf()
) {
    @PrePersist
    @PreUpdate
    fun initializeDate() {
        val now = OffsetDateTime.now()
        if (id == null) {
            createdOn = now
        }
        modifiedOn = now
    }

    override fun toString(): String =
        "User(id=$id, firstName=$firstName, lastName=$lastName, email=$email, " +
                "photo=$photo, enabled=$enabled, createdOn=$createdOn, modifiedOn=$modifiedOn, " +
                "createdBy=$createdBy, modifiedBy=$modifiedBy, roles=$roles)"

    @get:Transient
    val photosImagePath: String
        get() = when {
            id == null || photo == null -> "/img/default-user.png"
            else -> "/user-photos/$id/$photo"
        }
}