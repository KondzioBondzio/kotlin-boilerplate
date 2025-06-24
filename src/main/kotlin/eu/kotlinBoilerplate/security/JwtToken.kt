package eu.kotlinBoilerplate.security

import com.fasterxml.jackson.annotation.JsonProperty

data class JwtToken(
    val id: String?,
    val email: String?,
    val username: String?,
    @JsonProperty("first_name") val firstName: String?,
    @JsonProperty("last_name") val lastName: String?,
    @JsonProperty("organization_code") val organizationCode: String,
    @JsonProperty("is_super_admin") val superAdmin: Boolean?,
    val exp: Int
) {
    companion object {
        const val TOKEN_HEADER_NAME = "Authorization"
    }
}
