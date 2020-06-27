/*
* Δημιουργία Κλάσης Εξαίρεσης Ανάληψης (WithdrawException).
*/
package Bank_Management;

public class WithdrawException extends Exception {

    private int errorCode; // Κωδικός σφάλματος, ανάλογος με τι εξαίρεση έχει γίνει
    private int iban;	// Ο μοναδικός κωδικός του λογαριασμού που έγινε η εξαίρεση.
    private double amount; // Το ποσό της συναλαγης που έγινε η εξαίρεση.

    // Constractors.
    public WithdrawException() {
    }
    public WithdrawException(int errorCode, int iban, double amount) {
        super();
        this.errorCode = errorCode;
        this.iban = iban;
        this.amount = amount;
    }

    //Method.
    
    /**
     * Εμφάνηση μηνύματος σχετικά με την εξαίρεση που έχει γίνει(1.Δεν έχει αρκετό υπόλοιπο, 2.Πανω απο 1000, 3.Πάνω απο 3 αναλήψεις το μήνα, 4. Δεν επιτρέπετε για είναι λογαριασμός προθεσμίας).
     */
    public void printErrorMessage() {
        String strAmount = String.format("%.02f", this.amount);
        
        if (errorCode == 1) {
            System.err.println("[BANKER]: The account with IBAN " + this.iban + " wasn't enough balance for withdraw $" + strAmount + " !!!");
        } else if (errorCode == 2) {
            System.err.println("[BANKER]: The account with IBAN " + this.iban + " can't withdraw $"+strAmount+" because the limit is $1000 (for Simple Credit Account)!!!");
        } else if(errorCode == 3){
            System.err.println("[BANKER]: The account with IBAN " + this.iban + " can't withdraw $"+strAmount+" because already have do 3 for this month ( for Super Credit Account) !!!");
        }else if(errorCode == 4){
            System.err.println("[BANKER]: The account with IBAN " + this.iban + " can't withdraw because is Reserve Account !!!");
        }        
    }

}
