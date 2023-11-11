class CashFlowMinimizer {
    private val balances = mutableMapOf<String, Int>()

    fun addPerson(name: String, balance: Int) {
        balances[name] = balance
    }

    fun addTransaction(borrower: String, lender: String, amount: Int): Boolean {
        if (isValidPerson(borrower) && isValidPerson(lender) && borrower != lender) {
            balances[borrower] = balances[borrower]!! - amount
            balances[lender] = balances[lender]!! + amount
            return true
        }
        return false
    }

    fun getBalances(): Map<String, Int> {
        return balances
    }

    fun isValidPerson(person: String): Boolean {
        return balances.containsKey(person)
    }

    fun clearBalances() {
        balances.clear()
    }
}
