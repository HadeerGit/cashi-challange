fun main(args: Array<String>) {
    val amount = args[0].trim().toDouble()
    calculateFee(amount,  args[1])
}

fun calculateFee(amount: Double, type: String): Double {

    return amount * 0.0015
}