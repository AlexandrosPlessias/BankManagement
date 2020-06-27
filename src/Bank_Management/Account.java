/*
* Δημιουργία Κλάσης Λογαριασμός (Account).
*/

package Bank_Management;

import java.io.Serializable;
import java.util.ArrayList;
import File_Management.TransactionLoader;


public abstract class Account implements Serializable, Cloneable {

    private final int iban;                        // Μοναδικός αριθμός του λογαριασμού.
    private final ArrayList<Client> owners;        // Η λίστα ιδιοκτητών του λογαριασμού.
    private final Transaction open;                // Η συναλλαγή του ανοίγματος λογαριασμού.
//    private ArrayList<Transaction> transactions; // Η λίστα συναλαγών μαζί με αυτή του ανοίγματος.
    private double balance;                        // Το υπόλοιπο του λογαριασμού.
    private double rate;                           // Το επιτόκιο του λογαριασμού.

    // Static πεδίο το οποίο αυξάνεται όταν κατασκευάζεται κάθε νέο αντικείμενο αυτής της κλάσης.
    private static int lastIban = 100000;

    // Constractor, περιέχει την λίστα με τους κατόχους και την συναλλαγή ανοίγματος.
    public Account(ArrayList<Client> owners, Transaction open) {
        this.lastIban++; // Αύξηση Static πεδίου κατά ένα. 
        this.iban = lastIban; 
        this.owners = owners; 
        this.open=open;
        // create 
        //this.transactions = new ArrayList<>(); 
        //this.transactions.add(open); 
        this.balance = open.getAmount(); 
    }

    // Getters.
    public int getIban() {
        return iban;
    }
    public ArrayList<Client> getOwners() {
        return owners;
    }
    public ArrayList<Transaction> getTransactionsFromFile() {
        
        try {
            return TransactionLoader.allTransactions((Account) super.clone()); 
        } catch (CloneNotSupportedException ex) {
            // Αυτό δεν πρεπεί να συμβεί διότι κάνουμε επισφαλή αντιγραφή (Account implements Cloneable).
            System.err.println("[System]: Cloneable error, please do Class Account Cloneable !!!");
        }
        return null;
    }
    public double getBalance() {
        return balance;
    }
    public double getRate() {
        return rate;
    }
    public Transaction getOpen() {
        return open;
    }

    // Setters.
    public void setBalance(double balance) {
        this.balance = balance;
    }
    public void setRate(double rate) {
        this.rate = rate;
    }

    // Methods Abstract.
    public abstract void deposit(Transaction tran) throws DepositException;
    public abstract void withdraw(Transaction tran) throws WithdrawException;
    public abstract void payInterest(int date);
    public abstract void close(int date);

    @Override
    public abstract String toString();
}
