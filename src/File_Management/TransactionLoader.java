/*
 * Δημιουργία Κλάσης Φορτόματος όλων των συναλλαγών σε ένα ArrayList  (TransactionLoader).
 */
package File_Management;

import java.io.*;
import java.util.ArrayList;
import Bank_Management.Account;
import Bank_Management.Transaction;



public class TransactionLoader {

    // Method.
    
    /**
     * ΄Ανοιγμα αρχείου XXXXXX.bin και διάβασμα μιας μιας τών συναλλαγών και πέρασμα στο ArrayList.
     * @param curAccount Ο λογαριασμός του οποίου θέλουμε τις συναλλαγές του.
     * @return ArrayList όλων των συναλλαγών.
     */
    public static ArrayList<Transaction> allTransactions(Account curAccount) {

        ArrayList<Transaction> allTransactions = new ArrayList<>();
        allTransactions.add(curAccount.getOpen());
        
        
        File ftemp = new File("BANK_FILES" + File.separator + "Bank_Database" + File.separator + "Accounts" + File.separator + "Active" + File.separator + "IBAN_" + curAccount.getIban() + File.separator + curAccount.getIban() + ".bin");
        ftemp.mkdirs();
        // Αρχικοποίηση.
        ObjectInputStream ois=null;

        try {
            FileInputStream fis = new FileInputStream(ftemp);
            if (fis.available() != 0) {
                ois = new ObjectInputStream(fis);
                // Όσο υπάρχουν.
                while (ois.available() != 0) {
                    // Προσθήκη στην λίστα με typeCasting πάντα.
                    allTransactions.add((Transaction) ois.readObject()) ;
                }
            }
        } catch (ClassNotFoundException | IOException cnfe) {
            // Αυτό δεν πρεπεί να συμβεί.
        } finally { // Στο τέλος κλείσιμο αρχείου.
            if (ois != null) {
                try {   
                    ois.close();
                } catch (IOException ex) {
                   // Αυτό δεν πρεπεί να συμβεί.
                   ex.getCause().toString();
                }
            }
        }
        
        return allTransactions;
    }

}
