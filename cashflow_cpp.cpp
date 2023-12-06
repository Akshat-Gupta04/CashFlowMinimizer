#include <iostream>
#include <map>
#include <vector>
#include <limits>
#include <stdexcept>
#include <cctype> // For isdigit

using namespace std;

// Function to check if a string contains only digits
bool isNumeric(const string &str) {
    for (char c : str) {
        if (!isdigit(c)) {
            return false;
        }
    }
    return true;
}

int main() {
    int numPeople;
    cout << "Enter the number of people: ";
    while (!(cin >> numPeople) || numPeople <= 0) {
        cin.clear();
        cin.ignore(numeric_limits<streamsize>::max(), '\n');
        cout << "Invalid input. Please enter a positive integer: ";
    }

    map<string, int> balances; // To track outstanding balances

    for (int i = 0; i < numPeople; i++) {
        string name;
        cout << "Enter name for person " << i + 1 << ": ";
        cin >> name;

        while (isNumeric(name)) {
            cout << "Person name cannot be a number. Please enter a valid name: ";
            cin >> name;
        }

        balances[name] = 0;
    }

    int numTransactions;
    cout << "Enter the number of transactions: ";
    while (!(cin >> numTransactions) || numTransactions < 0) {
        cin.clear();
        cin.ignore(numeric_limits<streamsize>::max(), '\n');
        cout << "Invalid input. Please enter a non-negative integer: ";
    }

    for (int i = 0; i < numTransactions; i++) {
        string borrower, lender;
        int amount;

        cout << "Enter the name of the person who owes: ";
        cin >> borrower;

        if (balances.find(borrower) == balances.end()) {
            cout << "Person not found. Please check the name." << endl;
            continue;
        }

        cout << "Enter the name of the person who is owed: ";
        cin >> lender;

        if (balances.find(lender) == balances.end()) {
            cout << "Person not found. Please check the name." << endl;
            continue;
        }

        cout << "Enter the amount: ";
        while (!(cin >> amount) || amount <= 0) {
            cin.clear();
            cin.ignore(numeric_limits<streamsize>::max(), '\n');
            cout << "Invalid input. Please enter a positive integer: ";
        }

        // Update the balances
        balances[borrower] -= amount;
        balances[lender] += amount;
    }

    // Calculate and display payments to minimize cash flow
    cout << "Payments to minimize cash flow:" << endl;

    while (!balances.empty()) {
        string debtor = balances.begin()->first;
        string creditor = (--balances.end())->first;

        int debt = balances.begin()->second;
        int credit = (--balances.end())->second;

        int amount = min(-debt, credit);

        cout << debtor << " pays " << amount << " to " << creditor << endl;

        balances[debtor] += amount;
        balances[creditor] -= amount;

        if (balances[debtor] == 0) balances.erase(debtor);
        if (balances[creditor] == 0) balances.erase(creditor);
    }

    return 0;
}