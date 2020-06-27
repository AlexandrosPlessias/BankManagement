/*
* Δημιουργία Κλάσης Εξαίρεσης Κατάθεσης (DepositException).
*/
package Bank_Management;

public class DepositException extends Exception {
    
     private int iban; // Ο μοναδικός κωδικός του λογαριασμού που έγινε η εξαίρεση.

    // Constractors.
    public DepositException() { 
    }
    public DepositException(int iban) {
        super();      
        this.iban = iban;    
    }

    //Method.
    
    /**
     * Εμφάνηση μηνύματος σχετικά με την εξαίρεση που έχει γίνει. 
     */
    public void printErrorMessage(){
        System.err.println("[BANKER]: The account with IBAN "+this.iban+" can't Deposit because is Reserve Acount !!!");
    }
}
