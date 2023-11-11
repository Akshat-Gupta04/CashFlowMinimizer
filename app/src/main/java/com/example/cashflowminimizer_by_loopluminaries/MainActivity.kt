package com.example.cashflowminimizer_by_loopluminaries


import CashFlowMinimizer
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly

class MainActivity : AppCompatActivity() {

    private lateinit var numberOfPeopleEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var addNameButton: Button
    private lateinit var transactionTextView: TextView
    private lateinit var borrowerEditText: EditText
    private lateinit var lenderEditText: EditText
    private lateinit var amountEditText: EditText
    private lateinit var addTransactionButton: Button
    private lateinit var resultTextView: TextView
    private lateinit var divider: View
    private lateinit var divider2: View
    private lateinit var divider3: View
    private lateinit var divider4: View
    private lateinit var nextButton: Button
    private lateinit var backButton: Button
    private lateinit var startOverButton: Button
    private lateinit var transactionCountEditText: EditText
    private lateinit var transactionCountLayout: LinearLayout

    private var numberOfPeople = 0
    private var currentPerson = 1
    private var numberOfTransactions = 0
    private var currentTransaction = 1

    private val cashFlowMinimizer = CashFlowMinimizer()
    private val personNames = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberOfPeopleEditText = findViewById(R.id.numberOfPeopleEditText)
        nameEditText = findViewById(R.id.nameEditText)
        addNameButton = findViewById(R.id.addNameButton)
        transactionTextView = findViewById(R.id.transactionTextView)
        borrowerEditText = findViewById(R.id.borrowerEditText)
        lenderEditText = findViewById(R.id.lenderEditText)
        amountEditText = findViewById(R.id.amountEditText)
        addTransactionButton = findViewById(R.id.addTransactionButton)
        resultTextView = findViewById(R.id.resultTextView)
        divider = findViewById(R.id.divider)
        divider2 = findViewById(R.id.divider2)
        divider3 = findViewById(R.id.divider3)
        divider4 = findViewById(R.id.divider4)
        nextButton = findViewById(R.id.nextButton)
        backButton = findViewById(R.id.backButton)
        startOverButton = findViewById(R.id.startOverButton)
        transactionCountEditText = findViewById(R.id.transactionCountEditText)
        transactionCountLayout = findViewById(R.id.transactionCountLayout)
    }

    fun onNumberOfPeopleEntered(view: View) {
        val numberOfPeopleStr = numberOfPeopleEditText.text.toString()

        if (numberOfPeopleStr.isNotEmpty()) {
            numberOfPeople = numberOfPeopleStr.toInt()

            // Show name input UI
            findViewById<LinearLayout>(R.id.nameLayout).visibility = View.VISIBLE
            divider2.visibility = View.VISIBLE
            nextButton.visibility = View.GONE
            backButton.visibility = View.VISIBLE
        }
    }

    fun onAddNameClick(view: View) {
        val name = nameEditText.text.toString()

        if (name.isNotEmpty() && !name.isDigitsOnly()) {
            if (!personNames.contains(name)) {
                cashFlowMinimizer.addPerson(name, 0)
                personNames.add(name)
                nameEditText.text.clear()
                currentPerson++

                if (currentPerson > numberOfPeople) {
                    // Show transaction count input UI
                    transactionCountLayout.visibility = View.VISIBLE
                    divider3.visibility = View.VISIBLE
                }
            } else {
                Toast.makeText(this, "Person name already entered", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please enter a valid name", Toast.LENGTH_SHORT).show()
        }
    }

    fun onTransactionCountEntered(view: View) {
        val transactionCountStr = transactionCountEditText.text.toString()

        if (transactionCountStr.isNotEmpty()) {
            numberOfTransactions = transactionCountStr.toInt()

            // Show transaction input UI
            transactionTextView.visibility = View.VISIBLE
            divider4.visibility = View.VISIBLE
            borrowerEditText.visibility = View.VISIBLE
            lenderEditText.visibility = View.VISIBLE
            amountEditText.visibility = View.VISIBLE
            addTransactionButton.visibility = View.VISIBLE

            // Hide transaction count input UI
            transactionCountLayout.visibility = View.GONE
        }
    }

    fun onAddTransactionClick(view: View) {
        val borrower = borrowerEditText.text.toString()
        val lender = lenderEditText.text.toString()
        val amountText = amountEditText.text.toString()

        if (borrower.isNotEmpty() && lender.isNotEmpty() && amountText.isNotEmpty()) {
            val amount = amountText.toInt()

            if (cashFlowMinimizer.addTransaction(borrower, lender, amount)) {
                borrowerEditText.text.clear()
                lenderEditText.text.clear()
                amountEditText.text.clear()

                currentTransaction++

                if (currentTransaction > numberOfTransactions) {
                    // Display result when all transactions are entered
                    displayResult()
                }
            } else {
                Toast.makeText(this, "Invalid name or same borrower and lender", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayResult() {
        val balances = cashFlowMinimizer.getBalances()
        val resultStringBuilder = StringBuilder()

        for ((person, balance) in balances) {
            if (balance < 0) {
                for ((creditor, debt) in balances) {
                    if (debt > 0) {
                        val amountToPay = minOf(-balance, debt)
                        resultStringBuilder.append("$person needs to pay $amountToPay rupees to $creditor.\n")
                        cashFlowMinimizer.addTransaction(person, creditor, amountToPay) // Update balances
                    }
                }
            }
        }

        resultTextView.text = resultStringBuilder.toString()
        resultTextView.visibility = View.VISIBLE
        nextButton.isEnabled = false
        addNameButton.isEnabled = false
        addTransactionButton.isEnabled = false
        transactionCountEditText.isEnabled = false
        borrowerEditText.isEnabled = false
        lenderEditText.isEnabled = false
        amountEditText.isEnabled = false
        backButton.visibility = View.VISIBLE
        startOverButton.visibility = View.VISIBLE
    }

    fun onBackButtonClick(view: View) {
        // Reset all data and UI to start again
        numberOfPeopleEditText.text.clear()
        nameEditText.text.clear()
        transactionCountEditText.text.clear()
        borrowerEditText.text.clear()
        lenderEditText.text.clear()
        amountEditText.text.clear()
        resultTextView.text = ""

        numberOfPeople = 0
        currentPerson = 1
        numberOfTransactions = 0
        currentTransaction = 1

        cashFlowMinimizer.clearBalances()
        personNames.clear()

        findViewById<LinearLayout>(R.id.nameLayout).visibility = View.GONE
        transactionCountLayout.visibility = View.GONE
        transactionTextView.visibility = View.GONE
        borrowerEditText.visibility = View.GONE
        lenderEditText.visibility = View.GONE
        amountEditText.visibility = View.GONE
        addTransactionButton.visibility = View.GONE
        resultTextView.visibility = View.GONE
        nextButton.visibility = View.VISIBLE
        backButton.visibility = View.GONE
        divider.visibility = View.GONE
        divider2.visibility = View.GONE
        divider3.visibility = View.GONE
        divider4.visibility = View.GONE

        // Enable UI elements
        nextButton.isEnabled = true
        addNameButton.isEnabled = true
        addTransactionButton.isEnabled = true
        transactionCountEditText.isEnabled = true
        borrowerEditText.isEnabled = true
        lenderEditText.isEnabled = true
        amountEditText.isEnabled = true

        // Set minimum count of people to 2
        numberOfPeople = 2
        numberOfPeopleEditText.setText(numberOfPeople.toString())

        // Hide Start Over button
        startOverButton.visibility = View.GONE
    }
}
