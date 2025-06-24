package eu.kotlinBoilerplate.entrypoint.api.controller

import eu.kotlinBoilerplate.domain.PayoutService
import eu.kotlinBoilerplate.domain.model.PayoutTransactionStatus
import eu.kotlinBoilerplate.entrypoint.dto.PagedResponse
import eu.kotlinBoilerplate.entrypoint.dto.PayoutTransactionResponse
import eu.kotlinBoilerplate.security.JwtToken
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.util.UUID

@RestController
@RequestMapping(
    "/backoffice/api/v1",
    consumes = [MediaType.APPLICATION_JSON_VALUE],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
@Tag(name = "Payout Transactions")
class PayoutsBackofficeController(
    private val payoutService: PayoutService
) {
    @GetMapping("/payouts/{id}", consumes = [MediaType.ALL_VALUE])
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Transfer found",
                content = [Content(schema = Schema(implementation = PayoutTransactionResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = [Content()]
            )
        ]
    )
    fun fetchPayoutTransaction(
        @PathVariable("id")
        id: UUID,
        @Schema(hidden = true)
        @RequestHeader(JwtToken.TOKEN_HEADER_NAME)
        jwt: JwtToken
    ): ResponseEntity<PayoutTransactionResponse> {
        val payoutTransaction = payoutService.getPayoutTransactionById(id, jwt.organizationCode, jwt.superAdmin)
        val response = PayoutTransactionResponse.fromModel(payoutTransaction)

        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    @GetMapping("/payouts", consumes = [MediaType.ALL_VALUE])
    fun fetchPayoutTransactions(
        @Parameter(description = "Filter by payout transaction id", example = "123e4567-e89b-12d3-a456-426614174000")
        @RequestParam("payout_transaction_id", required = false)
        payoutTransactionId: UUID?,

        @Parameter(description = "Filter by transfer id", example = "123e4567-e89b-12d3-a456-426614174000")
        @RequestParam("transfer_id", required = false)
        transferId: UUID?,

        @Parameter(description = "Filter by transaction date from", example = "2024-01-01T23:59:59Z")
        @RequestParam("from_date", required = false)
        fromDate: Instant?,

        @Parameter(description = "Filter by transaction date to", example = "2099-12-31T23:59:59Z")
        @RequestParam(required = false)
        toDate: Instant?,

        @Parameter(description = "Filter by organization")
        @RequestParam("organization_code", required = false)
        organizationCode: String?,

        @Parameter(
            description = "Filter by transfer status", schema = Schema(
                type = "string",
                allowableValues = ["PENDING", "CREATED", "FAILED", "COMPLETED"]
            )
        )
        @RequestParam("status", required = false)
        status: PayoutTransactionStatus?,

        @Parameter(
            description = "Field to sort by, e.g., `status`, `id`, `createdAt`", example = "status", schema = Schema(
                type = "string", allowableValues = ["id", "status", "createdAt"]
            )
        )
        @RequestParam("sort_by", required = false, defaultValue = "status")
        sortBy: String?,

        @RequestParam(required = false, defaultValue = "1")
        page: Int?,

        @RequestParam("per_page", required = false, defaultValue = "50")
        perPage: Int?,

        @Parameter(
            description = "Sort order: `asc` for ascending, `desc` for descending", example = "asc", schema = Schema(
                type = "string", allowableValues = ["asc", "desc"]
            )
        )
        @RequestParam("sort_order", required = false, defaultValue = "ASC")
        sortOrder: String?,

        @Schema(hidden = true)
        @RequestHeader(JwtToken.TOKEN_HEADER_NAME)
        jwt: JwtToken
    ): ResponseEntity<PagedResponse<PayoutTransactionResponse>> {
        val sortField = sortBy ?: "status"
        val sortDirection = if (sortOrder.equals("asc", ignoreCase = true)) {
            Sort.Direction.ASC
        } else {
            Sort.Direction.DESC
        }
        val pageable: Pageable = PageRequest.of((page ?: 1) - 1, perPage ?: 50, Sort.by(sortDirection, sortField))
        var filterOrganizationCode: String? = null;

        if(jwt.superAdmin!!)
        {
            if (organizationCode != null) {
                filterOrganizationCode = organizationCode
            }
        }
        else
        {
            filterOrganizationCode = jwt.organizationCode
        }

        val pages = payoutService.getPayoutTransactions(
            payoutTransactionId,
            transferId,
            fromDate,
            toDate,
            organizationCode ?: filterOrganizationCode,
            status,
            pageable
        )

        val response = PagedResponse(
            currentPage = pages.number + 1,
            lastPage = pages.totalPages,
            total = pages.totalElements,
            data = pages.content.map { PayoutTransactionResponse.fromModel(it) }
        )

        return ResponseEntity.ok(response)
    }
}