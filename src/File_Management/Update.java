/*
* Δημιουργία Κλάσης Ανανέωσης (Update) για το περιεχόμενο των φακέλων(IBAN_XXXXXX) του φακέλου Active.
*/

package File_Management;

import java.io.*;
import Bank_Management.Account;
import Bank_Management.Transaction;

public class Update {
    
    // Methods.
    
    /**
     * Aνοίγει το φάκελο της διαδρομής: BANK_FILES\Bank_Database\Accounts\Active\IBAN_xxxxxx  και ανανεώνει 
     * τα Details.txt, ΧΧΧΧΧΧ.txt(Γράψιμο του τελευταίου Transaction ) , Account.bin και ΧΧΧΧΧΧ.bin(Γράψιμο του τελευταίου Transaction). 
     *  Επίσεις το αρχειο Details.txt και Account.bin για λόγους ανανέωσεις ξαναγράφονται (έμμεσο UPDATE με Overwrite).
     * @param account Ο λογαριασμός που θέλουμε να εφαρμοστούν οι παραπάνω ενέργειες.
     * @param function Απο που καλειτε ωστε να το αναφερουμε στο  ΧΧΧΧΧΧ.txt .
     */
    public static void ActiveAccountFolder(Account account, String function, Transaction tran) {

        File ftemp = new File("BANK_FILES" + File.separator + "Bank_Database" + File.separator + "Accounts" + File.separator + "Active" + File.separator + "IBAN_" + account.getIban());
        ftemp.mkdirs();

        // Details.txt , ξαναγράφεται για έμμεσο UPDATE με Overwrite του balance.
        try (FileWriter fw = new FileWriter(ftemp + File.separator + "Details.txt")) {
            fw.append(account.toString().replaceAll("\n", System.getProperty("line.separator")));
            fw.flush();
            fw.close();
        } catch (IOException ex) {
            System.err.println("Could Write to:" + ftemp + File.separator + "Details.txt");
        }

        // Accounts.bin , ξαναγράφεται για έμμεσο UPDATE με Overwrite του balance και των owners.
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ftemp + File.separator + "Account.bin"))) {
            oos.writeObject(account);
            oos.flush();
            oos.close();
        } catch (IOException ex) {
            System.err.println("Could Write to:" + ftemp + File.separator + "Account.bin");
        }

        // Trancastions.txt , Γράψιμο του τελευταίου Transaction.
        try (FileWriter fw = new FileWriter(ftemp + File.separator + account.getIban() + ".txt", true)) {

            
            
            String leftAlignFormat = "| %-4s | %-11s | %9s | %10s |";
            int Day = tran.getDate();
            String DayString = Day + "";
            double Amount = tran.getAmount();
            String AmountString = String.format("%.02f", Amount);
            double Balance = account.getBalance();
            String BalanceString = String.format("%.02f", Balance);
            String Transaction = String.format(leftAlignFormat, DayString, function, AmountString, BalanceString);

            fw.append(Transaction + System.getProperty("line.separator"));

            fw.flush();
            fw.close();
        } catch (IOException ex) {
            System.err.println("Could Write to:" + ftemp + File.separator + "Transactions.txt");
        }

        // Trancastions.bin , Γράψιμο του τελευταίου Transaction.
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ftemp + File.separator + account.getIban() + ".bin", true))) {
            oos.writeObject(tran);
            oos.flush();
            oos.close();
        } catch (IOException ex) {
            System.err.println("Could Write to:" + ftemp + File.separator + account.getIban() + ".txt");
        }

    }
    
}
