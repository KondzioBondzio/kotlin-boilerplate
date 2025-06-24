package eu.kotlinBoilerplate.entrypoint.api.controller

import eu.kotlinBoilerplate.domain.pain.PaymentExportService
import eu.kotlinBoilerplate.entrypoint.dto.PaymentExportRequest
import eu.kotlinBoilerplate.security.JwtToken
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/backoffice/api")
@Tag(name = "Payout Pain Export")
class PaymentExportController(
    private val paymentExportService: PaymentExportService
) {

    @PostMapping("/export/pain")
    fun exportToPain001(@RequestBody request: PaymentExportRequest,
                        @Schema(hidden = true)
                        @RequestHeader(JwtToken.TOKEN_HEADER_NAME)
                        jwt: JwtToken): ResponseEntity<String> {
        if (jwt.superAdmin != true) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

        val xmlContent = paymentExportService.generatePainXml(request)

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_XML)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pain001.xml")
            .body(xmlContent)
    }

    @PostMapping("/export/pain/download")
    fun downloadPain001(@RequestBody request: PaymentExportRequest,
                        @Schema(hidden = true)
                        @RequestHeader(JwtToken.TOKEN_HEADER_NAME)
                        jwt: JwtToken): ResponseEntity<ByteArray> {
        if (jwt.superAdmin != true) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

        val xmlContent = paymentExportService.generatePainXml(request)

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_XML)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pain001.xml")
            .body(xmlContent.toByteArray(Charsets.UTF_8))
    }
}