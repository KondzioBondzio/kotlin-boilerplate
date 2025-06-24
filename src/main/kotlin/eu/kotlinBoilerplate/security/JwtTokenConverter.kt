package eu.kotlinBoilerplate.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.convert.converter.Converter
import java.util.*

class JwtTokenConverter(
    private val objectMapper: ObjectMapper
): Converter<String, JwtToken> {

    override fun convert(source: String): JwtToken? {
        val decodedToken = if (source.startsWith(BEARER_TOKEN_PREFIX)) {
            source.substring(BEARER_TOKEN_PREFIX.length)
        } else {
            return null
        }
        val jwtBody = decodedToken.split('.')[1]

        val decodedBody = Base64.getDecoder().decode(jwtBody) as ByteArray

        return objectMapper.readValue(decodedBody, JwtToken::class.java)
    }

    companion object {
        private const val BEARER_TOKEN_PREFIX = "Bearer "
    }
}