/*
* Δημιουργία Κλάσης Λογαριασμών Ταμιευτηρίου (CreditAccount).
*/
package Bank_Management;


import java.util.ArrayList;
import File_Management.Update;

abstract public class CreditAccount extends Account {

   private int interestPeriod; /* Ημέρες τοκισμού, Δεν χρησιμοποιήται για την υλοποίηση αυτή,
   εαν το χρησιμοποιούσαμε: getTransactions().get(getTransactions.size()-1).getDate() - getTransactions().get(0).getDate(). */
	
    // Constractor.
    public CreditAccount(ArrayList<Client> owners, Transaction open) {
        super(owners, open);
    }

    // Getter.
    public int getInterestPeriod() {
        return interestPeriod;
    }

    //Methods Abstract.    
    @Override
    public abstract void withdraw(Transaction tran) throws WithdrawException;
    @Override
    public abstract void payInterest(int date);
    @Override
    public abstract String toString();
    
    //Methods.
    
    /**
     * Κάνει Override τη μέθοδο deposit της Account, υλοποιεί τη λειτουργεία της κατάθεσης και ενημερώνει το υπόλοιπο του λογαριασμού(balance).
     * χρησιμοποιούμε τη μέθοδο super.clone() για να πάρουμε το αντίγραφο του τωρινού λογαριασμού ετσι ώστε να ενημερώνουμε τα αντοίστιχα αρχεία μετά από κάθε κατάθεση.
     * @param tran Η συναλλαγή που περιέχει ημερομηνία και ποσό.
     */
    @Override
    public void deposit(Transaction tran){
        // Ενημερώνει το υπόλοιπο του λογαριασμού.
        setBalance(getBalance() + tran.getAmount());
        
        // Ενημέρωση αρχείων μετά από κάθε κατάθεση (γράψιμο στο τέλος του Transaction file).
        try {
            Update.ActiveAccountFolder((Account) super.clone(),"Deposit",tran);
        } catch (CloneNotSupportedException cnse) {
            // Αυτό δεν πρεπεί να συμβεί διότι κάνουμε επισφαλή αντιγραφή (Account implements Cloneable).
            System.err.println("[System]: Cloneable error, please do Class Account Cloneable !!!");
        }
        
        // Μήνυμα τραπεζίτη ότι όλα πήγαν καλά.
        String strAmount = String.format("%.02f", tran.getAmount() );
        String strBalance = String.format("%.02f", getBalance());      
        System.out.println("[Banker]: Successfully DEPOSIT $"+strAmount+" in account with IBAN "+getIban()+" the balance now is $"+strBalance+".\n");
    }
    
    /**
     * Κάνει Override τη μέθοδο close της Account, υλοποιεί τη λειτουργεία του κλεισίματος και τέλος αφαιρεί το λογαριασμό αυτό απο τους ιδιοκτήτες του.
     * Χρησιμοποιούμε τη μέθοδο super.clone() για να πάρουμε το αντίγραφο του τωρινού λογαριασμού ετσι ώστε να ενημερώνουμε τα αντοίστιχα αρχεία πριν το κλείσιμο του λογαριασμού.
     * @param date Η ημερομηνία που θέλουμε να κλείσουμε το λογαριασμό.
     */
    @Override
    public void close(int date) {

        // Αν εφαρμόζαμε υλοποίηση με χρήση interestPeriod τοτε  interestPeriod = date - getOpen().getDate()+1;
        // Κληση συνάρτησης υπολογισμού τόκων για τις extra μέρες.
        payInterest(date);

        // Ενημερώνει το υπόλοιπο του λογαριασμού και γίνεται ίσο με 0.
        Transaction tran = new Transaction(date, -getBalance());
        setBalance(0);

        // Ενημέρωση αρχείων μετά από κάθε κλείσιμο (γράψιμο στο τέλος του Transaction file).
        try {
            Update.ActiveAccountFolder((Account) super.clone(),"Close",tran);
        } catch (CloneNotSupportedException cnse) {
            // Αυτό δεν πρεπεί να συμβεί διότι κάνουμε επισφαλή αντιγραφή (Account implements Cloneable).
            System.err.println("[System]: Cloneable error, please do Class Account Cloneable !!!");
        }

        // Αφαιρεί τον λογαριασμό αυτον από τις λίστες λογαριασμών των ιδιοκτητών του.
        for (int i = 0; i < getOwners().size(); i++) {
            for (int j = 0; j < getOwners().get(i).getAcounts().size(); j++) {
                if (getOwners().get(i).getAcounts().get(j).getIban() == getIban()) {
                    getOwners().get(i).getAcounts().remove(j);
                }
            }
        }
        
        // Αφαιρεί από τον λογαριασμό την λίστα με τους ιδιοκτήτες του.
        getOwners().clear();
        
        // Μήνυμα τραπεζίτη ότι όλα πήγαν καλά.
        String strBalance = String.format("%.02f", getBalance()); 
        System.out.print("[Banker]: Successfully account with IBAN "+getIban()+" CLOSED the balance now is $"+strBalance+".\n");
    }
    
}
