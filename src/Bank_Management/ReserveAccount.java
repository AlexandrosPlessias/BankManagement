/*
 * Δημιουργία Κλάσης Προθεσμιακού Λογαριασμού (ReserveAccount).
 */
package Bank_Management;

import java.util.ArrayList;
import File_Management.Update;

public class ReserveAccount extends Account {

    private final double period;                            // Το χρονικό διάστημα που μένουν τα χρήματα
    int initialDate = getOpen().getDate();   // Ημέρα ανοίγματος λογαριασμού.

    // Constractor...
    public ReserveAccount(int period, ArrayList<Client> owners, Transaction open) {
        super(owners, open);
        this.period = period;
        // Ενημέρωση επιτοκίου σε 4% ή 4,2% ή 4,4% ή 4,6%.
        if (period == 3.0) {
            setRate(0.040);
        } else if (period == 6.0) {
            setRate(0.042);
        } else if (period == 9.0) {
            setRate(0.044);
        } else if (period == 12.0) {
            setRate(0.046);
        }
    }

    //Methods.
    /**
     * Κάνει Override τη μέθοδο deposit της Account.
     *
     * @param tran Η συναλλαγή που περιέχει ημερομηνία και ποσό.
     * @throws DepositException Πάντα, γιατί δεν επιτρέπετε η κατάθεση σε
     * λογαριασμό προθεσμίας και εμφανίζει αντίστοιχο μήνυμα λάθους.
     */
    @Override
    public void deposit(Transaction tran) throws DepositException {
        throw new DepositException(getIban());
    }

    /**
     * Κάνει Override τη μέθοδο withdraw της Account
     * @param tran Η συναλλαγή που περιέχει ημερομηνία και ποσό.
     * @throws WithdrawException Πάντα, γιατί δεν επιτρέπετε η ανάληψη σε
     * λογαριασμό προθεσμίας και εμφανίζει αντίστοιχο μήνυμα λάθους.
     */
    @Override
    public void withdraw(Transaction tran) throws WithdrawException {
        throw new WithdrawException(4, getIban(), tran.getAmount()); //Error code ...
    }

    /**
     * Κάνει Override τη μέθοδο payInterest της Account, υλοποιεί τη λειτουργεία του τοκισμού(ανάλογα με την περίοδο δέσμευσης του ποσού) και ενημερώνει το υπόλοιπο του λογαριασμού(balance) αφού υπολογίσει το ποσό του τόκου.
     * Χρησιμοποιούμε τη μέθοδο super.clone() για να πάρουμε το αντίγραφο του τωρινού λογαριασμού ετσι ώστε να ενημερώνουμε τα αντοίστιχα αρχεία μετά τον τοκισμό του λογαριασμού.
     * @param curDay Η ημερομηνία του τοκισμού.
     */
    @Override
    public void payInterest(int curDay) {
        
        // Υπολογισμός για το ποιά μέρα ανοίγει ο λογαριασμός.
        int activeDay = curDay - this.initialDate;
        // Υπολογισμός 3μήνου.
        double quarter = (double) activeDay / 90;

        // Εάν quarter == 1.0 && period==3.0 κ.ο.κ.
        if (((quarter == 1.0) && (this.period == 3.0)) || (quarter == 2.0) && (this.period == 6.0) || (quarter == 3.0) && (this.period == 9.0) || (quarter == 4.0) && (this.period == 12.0)) {
            // Υπολογισμός τόκου.
            double interest = getTransactionsFromFile().get(0).getAmount() * getRate() / 360 * (this.period * 30); 
            Transaction tran = new Transaction(curDay, interest);
            
            // Ενημερώνει το υπόλοιπο του λογαριασμού.
            setBalance(getBalance() + tran.getAmount());

            // Ενημέρωση αρχείων μετά από κάθε τοκίσμο (γράψιμο στο τέλος του Transaction file).
            try {
                Update.ActiveAccountFolder((Account) super.clone(), "Interest",tran);
            } catch (CloneNotSupportedException cnse) {
                // Αυτό δεν πρεπεί να συμβεί διότι κάνουμε επισφαλή αντιγραφή (Account implements Cloneable).
                System.err.println("[System]: Cloneable error, please do Class Account Cloneable !!!");
            }
            
            // Εικονικό κλείσιμο για την επόμενη μέρα το πρωί.
            close(curDay + 1);
            
            // Μήνυμα τραπεζίτη ότι όλα πήγαν καλά.
            String strInterest = String.format("%.02f", interest);
            String strBalance = String.format("%.02f", getBalance());
            System.out.println("[Banker]: Today, from Interest deposit $" + strInterest + " in account with IBAN " + getIban() + " the balance now is $" + strBalance + ".");
        }

    }

    /**
     * Κάνει Override τη μέθοδο close της Account, υλοποιεί τη λειτουργεία του κλεισίματος, μεταφέρει το υπόλοιπο(balance) σε κάποιο λογαριασμό ιδιοκτήτη σαν κατάθεση και τέλος αφαιρεί το λογαριασμό αυτό απο τους ιδιοκτήτες του.
     * Χρησιμοποιούμε τη μέθοδο super.clone() για να πάρουμε το αντίγραφο του τωρινού λογαριασμού ετσι ώστε να ενημερώνουμε τα αντοίστιχα αρχεία πριν το κλείσιμο του λογαριασμού.
     * @param date Η ημερομηνία που θέλουμε να κλείσουμε το λογαριασμό.
     */
    @Override
    public void close(int date) {

        // Αν εφαρμόζαμε υλοποίηση με χρήση interestPeriod τοτε  interestPeriod = date - getOpen().getDate()+1;
        // Κληση συνάρτησης υπολογισμού τόκων για τις extra μέρες.
        payInterest(date);

        // Μεταφέρει το υπόλοιπο(balance) σε κάποιο λογαριασμό ενός μόνο ιδιοκτήτη πρώτα στον πρώτο και εαν δεν έχει εξετάζω τους αλλούς.
        boolean counter = false;
        for (int i = 0; i < getOwners().size(); i++) {
            if (counter == true) {
                break;
            }
            for (int j = 0; j < getOwners().get(i).getAcounts().size(); j++) {
                if ((getOwners().get(i).getAcounts().get(j).getIban() != getIban())&&(!(getOwners().get(i).getAcounts().get(j) instanceof ReserveAccount))) {
                    Transaction tran = new Transaction(date, getBalance());            
                    getOwners().get(i).getAcounts().get(j).setBalance(getOwners().get(i).getAcounts().get(j).getBalance() + getBalance());
                    Update.ActiveAccountFolder((getOwners().get(i).getAcounts().get(j)), "From Close",tran);
                    counter = true;
                }
            }
        }
        
         //Πολύ μικρή πιθανότητα με βάση τις αρχικές παραμέτρους να συμβεί αυτό δηλαδή να μην έχει κανένας χρήστης ενεργό λογαριασμό.
        if(counter==false){
            System.err.println("There is no owner's account with active account");
            // Δεν δημιουργώ λογαριασμό γιατι θα χαλάσει την σειρά των IBANS.
        }

        Transaction tran = new Transaction(date, -getBalance());
        // Ενημερώνει το υπόλοιπο του λογαριασμού και γίνεται ίσο με 0.
        setBalance(0);
        
        // Ενημέρωση αρχείων μετά από κάθε κλείσιμο (γράψιμο στο τέλος του Transaction file).
        try {
            Update.ActiveAccountFolder((Account) super.clone(), "Close",tran);
        } catch (CloneNotSupportedException ex) {
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
        System.out.print("\n[Banker]: Successfully account with IBAN " + getIban() + " CLOSED the balance now is $" + strBalance + ".\n");

    }

    /**
     * Κάνει Override τη μέθοδο toString της κλάσης Object.
     * @return  Το String με όλα τα στοιχεία του λογαριασμού.
     */
    @Override
    public String toString() {
        String str = String.format("%.02f", getBalance());
        return ("Reserved Account\nIBAN:  " + getIban() + "\nOwner/s List: " + getOwners() + "\nReserved for: " + this.period + " months\nRate: " + getRate() + "\nCurrent Balance: $" + str+ "\n");
    }

}
