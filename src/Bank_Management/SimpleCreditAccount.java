/*
* Δημιουργία Κλάσης Απλού Λογαριασμού Ταμιευτηρίου (SimpleCreditAccount).
*/
package Bank_Management;

import java.util.ArrayList;
import File_Management.Update;



public class SimpleCreditAccount extends CreditAccount {

     private final double maxWithdraw = 1000; // Το ανώτατο όριο ανάληψης.

    // Constractor.
    public SimpleCreditAccount(ArrayList<Client> owners, Transaction open) {
        super(owners, open);
        // Ενημέρωση επιτοκίου σε 1%.
        setRate(0.01);
    }

    //Methods.
    
     /**
     * Κάνει Override τη μέθοδο withdraw της Account, υλοποιεί τη λειτουργεία της ανάληψης(αφού κάνει τους απαραίτητους ελέγχους) και ενημερώνει το υπόλοιπο του λογαριασμού(balance).
     * Χρησιμοποιούμε τη μέθοδο super.clone() για να πάρουμε το αντίγραφο του τωρινού λογαριασμού ετσι ώστε να ενημερώνουμε τα αντοίστιχα αρχεία μετά από κάθε ανάληψη.
     * @param tran Η συναλλαγή που περιέχει ημερομηνία και ποσό.
     * @throws WithdrawException Αν το ποσό είναι περισσότερο απο 1000 ή αν δεν υπάρχει διαθέσιμο υπόλοιπο και εμφανίζουμε τα αντίστοιχα μηνύματα λάθους.
     */
    @Override
    public void withdraw(Transaction tran) throws WithdrawException {

        // Έλεγχος εάν το ποσό είναι άνω των 1000.
        if (this.maxWithdraw >= ((-1) * (tran.getAmount()))) {
            // Έλεγχος εάν δεν υπάρχει διαθέσιμο υπόλοιπο στον λογαριασμό.
            if (getBalance() >= ((-1) * (tran.getAmount()))) {
                // Ενημερώνει το υπόλοιπο του λογαριασμού.
                setBalance(getBalance() + tran.getAmount());

                // Ενημέρωση αρχείων μετά από κάθε ανάληψη (γράψιμο στο τέλος του Transaction file).
                try {
                    Update.ActiveAccountFolder((Account) super.clone(),"Withdraw",tran);
                } catch (CloneNotSupportedException cnse) {
                   // Αυτό δεν πρεπεί να συμβεί διότι κάνουμε επισφαλή αντιγραφή (Account implements Cloneable).
                   System.err.println("[System]: Cloneable error, please do Class Account Cloneable !!!");
                }

                // Μήνυμα τραπεζίτη ότι όλα πήγαν καλά.
                String strAmount = String.format("%.02f", tran.getAmount());
                String strBalance = String.format("%.02f", getBalance());
                System.out.println("[Banker]: Successfully WITHDRAW $" + strAmount + " in account with IBAN " + getIban() + " the balance now is $" + strBalance + ".\n");

            } else {
                throw new WithdrawException(1, getIban(), tran.getAmount());    // Κωδικός σφάλματος 1 για μη επαρκές διαθέσιμο υπόλοιπο.  
            }
        } else {
            throw new WithdrawException(2, getIban(), tran.getAmount());        // Κωδικός σφάλματος 1 για υπέρβαση ορίου.
        }

    }

    /**
     * Κάνει Override τη μέθοδο payInterest της Account, υλοποιεί τη λειτουργεία του τοκισμού και ενημερώνει το υπόλοιπο του λογαριασμού(balance) αφού υπολογίσει το ποσό του τόκου.
     * Χρησιμοποιούμε τη μέθοδο super.clone() για να πάρουμε το αντίγραφο του τωρινού λογαριασμού ετσι ώστε να ενημερώνουμε τα αντοίστιχα αρχεία μετά τον τοκισμό του λογαριασμού.
     * @param date Η ημερομηνία του τοκισμού.
     */
    @Override
    public void payInterest(int date) {
        double meanMoney = 0, allMoney = 0;

        // ΤΥΠΟΣ: meanMoney= meanMoney +( date(i+1) - date(i)+1 ) * (  allmoney(=amount(i)+allmoney) ).
        // Πέρασμα όλων των συναλλαγών.
        for (int i = 0; i < (getTransactionsFromFile().size()); i++) {
            if ((i == 0) && (getTransactionsFromFile().size() == 1)) { 
                meanMoney = meanMoney + ((date - getTransactionsFromFile().get(i).getDate() + 1) * (getTransactionsFromFile().get(0).getAmount()));
            } else if (i == getTransactionsFromFile().size() - 1) { 
                allMoney = allMoney + getTransactionsFromFile().get(i).getAmount();
                meanMoney = meanMoney + ((date - getTransactionsFromFile().get(i).getDate() + 1) * allMoney);
            } else {
                allMoney = allMoney + getTransactionsFromFile().get(i).getAmount();
                meanMoney = meanMoney + ((getTransactionsFromFile().get(i + 1).getDate() - getTransactionsFromFile().get(i).getDate() + 1) * (allMoney));
            }
        }
        
        // Πραγματικό meanMoney(δηλαδή μέση τιμή χρημάτων).
        meanMoney = meanMoney / (date - getOpen().getDate() + 1);
        // Υπολογισμός τόκου.
        double interest = meanMoney * getRate() / 360 * (date - getOpen().getDate() + 1);
        Transaction tran = new Transaction(date, interest);
        
        // Ενημερώνει το υπόλοιπο του λογαριασμού.
        setBalance(getBalance() + tran.getAmount());

        // Ενημέρωση αρχείων μετά από κάθε τοκίσμο (γράψιμο στο τέλος του Transaction file).
        try {
            Update.ActiveAccountFolder((Account) super.clone(),"Interest",tran);
        } catch (CloneNotSupportedException ex) {
            // Αυτό δεν πρεπεί να συμβεί διότι κάνουμε επισφαλή αντιγραφή (Account implements Cloneable).
            System.err.println("[System]: Cloneable error, please do Class Account Cloneable !!!");
        }

        // Μήνυμα τραπεζίτη ότι όλα πήγαν καλά.
        String strInterest = String.format("%.02f", interest);
        String strBalance = String.format("%.02f", getBalance());
        System.out.println("[Banker]: Today, from INTEREST deposit $" + strInterest + " in account with IBAN " + getIban() + " the balance now is $" + strBalance + ".");
    }

    /**
     * Κάνει Override τη μέθοδο toString της κλάσης Object.
     * @return  Το String με όλα τα στοιχεία του λογαριασμού.
     */
    @Override
    public String toString() {
        String str = String.format("%.02f", getBalance());
        return ("Simple Credit Account\nIBAN: " + getIban() + "\nOwner/s List: " + getOwners() + "\nRate: " + getRate() + "\nCurrent Balance: $" + str+"\n");
    }

}
