package eu.kotlinBoilerplate.domain.pain

import eu.kotlinBoilerplate.domain.pain.model.Account
import eu.kotlinBoilerplate.domain.pain.model.Address
import eu.kotlinBoilerplate.domain.pain.model.Amount
import eu.kotlinBoilerplate.domain.pain.model.CreditTransfer
import eu.kotlinBoilerplate.domain.pain.model.FinancialInstitution
import eu.kotlinBoilerplate.domain.pain.model.Party
import eu.kotlinBoilerplate.domain.pain.model.PaymentInfo
import eu.kotlinBoilerplate.domain.pain.model.PaymentInitiation
import eu.kotlinBoilerplate.entrypoint.dto.PaymentExportRequest
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class PaymentExportService(
    private val painGenerator: PainGenerator
) {

    fun generatePainXml(request: PaymentExportRequest): String {
        val paymentInitiation = mapToPaymentInitiation(request)
        return painGenerator.generateXml(paymentInitiation)
    }

    fun generatePainXmlAsResource(request: PaymentExportRequest, filename: String = "pain001.xml"): Resource {
        val xmlContent = generatePainXml(request)
        return ByteArrayResource(xmlContent.toByteArray(Charsets.UTF_8))
    }

    private fun mapToPaymentInitiation(request: PaymentExportRequest): PaymentInitiation {
        val initiatingPartyAddress = if (!request.initiatingPartyAddressLines.isNullOrEmpty()) {
            Address(
                country = request.initiatingPartyCountry,
                addressLines = request.initiatingPartyAddressLines
            )
        } else null

        val initiatingParty = Party(
            name = request.initiatingPartyName,
            identification = request.initiatingPartyId,
            address = initiatingPartyAddress
        )

        val creditTransfers = request.transfers.mapIndexed { index, transfer ->
            val creditorAddress = if (!transfer.creditorAddressLines.isNullOrEmpty()) {
                Address(
                    country = transfer.creditorCountry,
                    addressLines = transfer.creditorAddressLines
                )
            } else null

            CreditTransfer(
                instructionId = "TXN${String.format("%03d", index + 1)}",
                endToEndId = "E2E${String.format("%03d", index + 1)}",
                amount = Amount(transfer.amount, transfer.currency),
                creditor = Party(
                    name = transfer.creditorName,
                    address = creditorAddress
                ),
                creditorAccount = Account(iban = transfer.creditorIban),
                creditorAgent = FinancialInstitution(
                    bic = transfer.creditorBic,
                    clearingSystemMemberId = transfer.creditorClearingId
                ),
                remittanceInfo = transfer.remittanceInfo,
                purpose = transfer.purpose ?: if (transfer.currency == "PLN") "PLKR" else "ADCS",
                chargeBearer = transfer.chargeBearer,
                instructionPriority = transfer.instructionPriority,
                serviceLevel = transfer.serviceLevel,
                categoryPurpose = transfer.categoryPurpose
            )
        }

        val paymentInfo = PaymentInfo(
            paymentInfoId = "PMT001",
            requestedExecutionDate = request.requestedExecutionDate ?: LocalDate.now().plusDays(1),
            debtor = initiatingParty,
            debtorAccount = Account(iban = request.debtorIban),
            debtorAgent = FinancialInstitution(
                bic = request.debtorBic,
                clearingSystemMemberId = request.debtorClearingId
            ),
            creditTransfers = creditTransfers
        )

        return PaymentInitiation(
            initiatingParty = initiatingParty,
            paymentInfos = listOf(paymentInfo)
        )
    }
}