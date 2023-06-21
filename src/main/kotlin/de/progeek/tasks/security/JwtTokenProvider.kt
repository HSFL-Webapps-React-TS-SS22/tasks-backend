package de.progeek.tasks.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct


@Component
class JwtTokenProvider(val userDetailsService: UserDetailsService) {
    @Value("\${security.jwt.token.secret-key:tai0cie1saa8reigaoni8cayi4seing9ohphahkeipheeveip8eec9shoh6eWoog}")
    private var secretKey: String? = null

    @Value("\${security.jwt.token.expire-length:3600000}")
    private val validityInMilliseconds: Long = 3600000 // 1h


    @PostConstruct
    fun init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey!!.toByteArray())
    }

    fun createToken(username: String?): String? {
        val claims: Claims = Jwts.claims().setSubject(username)

        val now = Date()
        val validity = Date(now.time + validityInMilliseconds)
        return Jwts.builder() //
            .setClaims(claims) //
            .setIssuedAt(now) //
            .setExpiration(validity) //
            .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, secretKey) //
            .compact()
    }


//    fun getAuthentication(token: String?): Authentication? {
//        val userDetails: UserDetails = userDetailsService.findByUsername(getUsername(token))
//        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
//    }

    fun getUsername(token: String?): String? {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.subject
    }

//    fun resolveToken(req: ): String? {
//        val bearerToken: String = req.getHeader("Authorization")
//        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            bearerToken.substring(7)
//        } else null
//    }

    fun validateToken(token: String?): Boolean {
        return try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
            true
        } catch (e: JwtException) {
            throw RuntimeException("Expired or invalid JWT token")
        } catch (e: IllegalArgumentException) {
            throw RuntimeException("Expired or invalid JWT token")
        }
    }

}