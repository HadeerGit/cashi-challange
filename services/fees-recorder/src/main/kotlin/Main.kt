fun main(args: Array<String>) {
    recordFee(args[0], args[1])
}

fun recordFee(transactionId: String, fee: String) {
    print(fee)
    println(transactionId)
    // Fee recording logic
    // Persist fee information to database or storage
}

