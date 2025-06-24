package eu.kotlinBoilerplate.domain.pain

import eu.kotlinBoilerplate.domain.pain.model.CreditTransfer
import eu.kotlinBoilerplate.domain.pain.model.PaymentInfo
import eu.kotlinBoilerplate.domain.pain.model.PaymentInitiation
import org.springframework.stereotype.Component
import java.time.format.DateTimeFormatter

@Component
class PainGenerator {
    private val xmlHeader = """<?xml version="1.0" encoding="UTF-8"?>"""
    private val namespace = """xmlns="urn:iso:std:iso:20022:tech:xsd:pain.001.001.09" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance""""

    fun generateXml(paymentInitiation: PaymentInitiation): String {
        val messageId = paymentInitiation.messageId ?: generateMessageId()

        return buildString {
            appendLine(xmlHeader)
            appendLine("""<Document $namespace>""")
            appendLine("  <CstmrCdtTrfInitn>")

            appendGroupHeader(paymentInitiation, messageId)

            paymentInitiation.paymentInfos.forEach { paymentInfo ->
                appendPaymentInfo(paymentInfo)
            }

            appendLine("  </CstmrCdtTrfInitn>")
            appendLine("</Document>")
        }
    }

    private fun StringBuilder.appendGroupHeader(payment: PaymentInitiation, messageId: String) {
        appendLine("    <GrpHdr>")
        appendLine("      <MsgId>${escapeXml(messageId)}</MsgId>")
        appendLine("      <CreDtTm>${payment.creationDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)}</CreDtTm>")
        appendLine("      <NbOfTxs>${payment.getTotalTransactions()}</NbOfTxs>")
        appendLine("      <InitgPty>")
        appendLine("        <Nm>${escapeXml(payment.initiatingParty.name)}</Nm>")
        appendLine("      </InitgPty>")
        appendLine("    </GrpHdr>")
    }

    private fun StringBuilder.appendPaymentInfo(paymentInfo: PaymentInfo) {
        appendLine("    <PmtInf>")
        appendLine("      <PmtInfId>${escapeXml(paymentInfo.paymentInfoId)}</PmtInfId>")
        appendLine("      <PmtMtd>${paymentInfo.paymentMethod}</PmtMtd>")
        appendLine("      <ReqdExctnDt>")
        appendLine("        <Dt>${paymentInfo.requestedExecutionDate}</Dt>")
        appendLine("      </ReqdExctnDt>")

        appendLine("      <Dbtr>")
        appendLine("        <Nm>${escapeXml(paymentInfo.debtor.name)}</Nm>")
        paymentInfo.debtor.address?.let { address ->
            appendLine("        <PstlAdr>")
            appendLine("          <Ctry>${address.country}</Ctry>")
            address.addressLines.forEach { line ->
                appendLine("          <AdrLine>${escapeXml(line)}</AdrLine>")
            }
            appendLine("        </PstlAdr>")
        }
        appendLine("      </Dbtr>")

        appendLine("      <DbtrAcct>")
        appendLine("        <Id>")
        appendLine("          <IBAN>${paymentInfo.debtorAccount.iban}</IBAN>")
        appendLine("        </Id>")
        appendLine("      </DbtrAcct>")

        appendLine("      <DbtrAgt>")
        appendLine("        <FinInstnId>")
        if (paymentInfo.debtorAgent.isDomestic()) {
            appendLine("          <ClrSysMmbId>")
            appendLine("            <ClrSysId>")
            appendLine("              <Cd>PLKNR</Cd>")
            appendLine("            </ClrSysId>")
            appendLine("            <MmbId>${paymentInfo.debtorAgent.clearingSystemMemberId}</MmbId>")
            appendLine("          </ClrSysMmbId>")
        } else {
            appendLine("          <BICFI>${paymentInfo.debtorAgent.bic}</BICFI>")
        }
        appendLine("        </FinInstnId>")
        appendLine("      </DbtrAgt>")

        paymentInfo.creditTransfers.forEach { transfer ->
            appendCreditTransfer(transfer)
        }

        appendLine("    </PmtInf>")
    }

    private fun StringBuilder.appendCreditTransfer(transfer: CreditTransfer) {
        appendLine("      <CdtTrfTxInf>")
        appendLine("        <PmtId>")
        appendLine("          <InstrId>${escapeXml(transfer.instructionId)}</InstrId>")
        appendLine("          <EndToEndId>${escapeXml(transfer.endToEndId)}</EndToEndId>")
        appendLine("        </PmtId>")

        if (!transfer.isDomestic()) {
            appendLine("        <PmtTpInf>")
            transfer.instructionPriority?.let { priority ->
                appendLine("          <InstrPrty>${priority}</InstrPrty>")
            }
            transfer.serviceLevel?.let { level ->
                appendLine("          <SvcLvl>")
                appendLine("            <Cd>${level}</Cd>")
                appendLine("          </SvcLvl>")
            }
            transfer.categoryPurpose?.let { catPurp ->
                appendLine("          <CtgyPurp>")
                appendLine("            <Cd>${catPurp}</Cd>")
                appendLine("          </CtgyPurp>")
            }
            appendLine("        </PmtTpInf>")
        }

        appendLine("        <Amt>")
        appendLine("          <InstdAmt Ccy=\"${transfer.amount.currency}\">${transfer.amount.value}</InstdAmt>")
        appendLine("        </Amt>")

        transfer.chargeBearer?.let { bearer ->
            appendLine("        <ChrgBr>${bearer}</ChrgBr>")
        }

        appendLine("        <CdtrAgt>")
        appendLine("          <FinInstnId>")
        if (transfer.creditorAgent.isDomestic()) {
            appendLine("            <ClrSysMmbId>")
            appendLine("              <MmbId>${transfer.creditorAgent.clearingSystemMemberId}</MmbId>")
            appendLine("            </ClrSysMmbId>")
        } else {
            appendLine("            <BICFI>${transfer.creditorAgent.bic}</BICFI>")
        }
        appendLine("          </FinInstnId>")
        appendLine("        </CdtrAgt>")

        appendLine("        <Cdtr>")
        appendLine("          <Nm>${escapeXml(transfer.creditor.name)}</Nm>")
        transfer.creditor.address?.let { address ->
            appendLine("          <PstlAdr>")
            appendLine("            <Ctry>${address.country}</Ctry>")
            address.addressLines.forEach { line ->
                appendLine("            <AdrLine>${escapeXml(line)}</AdrLine>")
            }
            appendLine("          </PstlAdr>")
        }
        appendLine("        </Cdtr>")

        appendLine("        <CdtrAcct>")
        appendLine("          <Id>")
        appendLine("            <IBAN>${transfer.creditorAccount.iban}</IBAN>")
        appendLine("          </Id>")
        appendLine("        </CdtrAcct>")

        transfer.purpose?.let { purpose ->
            appendLine("        <Purp>")
            if (transfer.isDomestic() || purpose == "PLKR") {
                appendLine("          <Prtry>${purpose}</Prtry>")
            } else {
                appendLine("          <Cd>${purpose}</Cd>")
            }
            appendLine("        </Purp>")
        }

        transfer.remittanceInfo?.let { info ->
            appendLine("        <RmtInf>")
            appendLine("          <Ustrd>${escapeXml(info)}</Ustrd>")
            appendLine("        </RmtInf>")
        }

        appendLine("      </CdtTrfTxInf>")
    }

    private fun escapeXml(text: String): String {
        return text.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;")
    }

    private fun generateMessageId(): String {
        return "MSG${System.currentTimeMillis()}"
    }
}