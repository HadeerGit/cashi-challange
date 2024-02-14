fun main(args: Array<String>) {
    val transactionId = args[0]
    val fee = args[1].trim().toDouble()
    chargeFee(transactionId, fee)
}

fun chargeFee(transactionId: String, fee: toDouble) {
  // Integrate with a payment gateway or financial service provider to charge the fee
  return ChargeResponse(transaction.transaction_id, true, "Fee charged successfully")
}

@Serializable
data class ChargeResponse(
    val transaction_id: String,
    val success: Boolean,
    val message: String? = null
)