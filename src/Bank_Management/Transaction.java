/*
 * Δημιουργία Κλάσης Συναλλαγή (Transaction).
 */
package Bank_Management;

import java.io.Serializable;

public class Transaction implements Serializable {      // Use for ALL transaction.

    private final int date;       // Ημέρα. 
    private final double amount;  // Ποσό.

    // Constractor.
    public Transaction(int date, double amount) {
        this.date = date;
        this.amount = amount;
    }

    // Getters .
    public int getDate() {
        return date;
    }
    public double getAmount() {
        return amount;
    }

    // Methods.
    
    /**
     * Κάνει Override τη μέθοδο toString της κλάσης Object.
     * @return  Το String με όλα τα στοιχεία της συναλλαγής(μέρα και ποσό).
     */
    @Override
    public String toString() {
        String str = String.format("%.02f", this.amount);
        return ("Day " + this.date + " -> $" + str + "\n");
    }
}
