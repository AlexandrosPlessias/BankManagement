/*
* Δημιουργία Κλάσης Διμιουργεία (Create) για το φτιάξιμο και το γέμισμα διαφόρων φακέλων.
*/

package File_Management;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import Bank_Management.Account;
import Bank_Management.Client;



public class Create {
    
    
    //Methods.
    /**
     * Με το που φτιάχνετε ένας νέος λογαριασμός, ανοίγεις το φάκελο της διαδρομής: BANK_FILES\Bank_Database\Accounts\Active\IBAN_xxxxxx (εάν δεν υπάρχει τότε δημιουργείτε) 
     * και φτιάχνει τα Details.txt, ΧΧΧΧΧΧ.txt(Γράψιμο πρώτου Transaction & φτιάξιμο Header αρχείου) , Account.bin και ΧΧΧΧΧΧ.bin(Γράψιμο πρώτου Transaction).
     * @param account Ο λογαριασμός που θέλουμε να εφαρμοστούν οι παραπάνω ενέργειες.
     */
    public static void ActiveAccountFolder(Account account) {

        File ftemp = new File("BANK_FILES" + File.separator + "Bank_Database" + File.separator + "Accounts" + File.separator + "Active" + File.separator + "IBAN_" + account.getIban());
        ftemp.mkdirs();

        // Details.txt
        try (FileWriter fw = new FileWriter(ftemp + File.separator + "Details.txt")) {
            fw.append(account.toString().replaceAll("\n", System.getProperty("line.separator")));
            fw.flush();
            fw.close();
        } catch (IOException ex) {
            System.err.println("Could Write to: " + ftemp + File.separator + "Details.txt");
        }

        // Accounts.bin
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ftemp + File.separator + "Account.bin"))) {
            oos.writeObject(account);
            oos.flush();
            oos.close();
        } catch (IOException ex) {
            System.err.println("Could Write to: " + ftemp + File.separator + "Account.bin");
        }

        // ΧΧΧΧΧΧ.txt(Transactions) για γράψιμο πρώτου Transaction και φτιαξιμο Header αρχείου.  
        try (FileWriter fw = new FileWriter(ftemp + File.separator + account.getIban() + ".txt")) {

            String line = "+---------------------------------------------+";
            String header = "| Day  | Transaction | ($)Amount | ($)Balance |";
            String leftAlignFormat = "| %-4s | %-11s | %9s | %10s |";

            int Day = account.getOpen().getDate();
            String DayString = Day + "";
            double Amount = account.getOpen().getAmount();
            String AmountString = String.format("%.02f", Amount);
            double Balance = account.getBalance();
            String BalanceString = String.format("%.02f", Balance);
            String Transaction = String.format(leftAlignFormat, DayString, "Open", AmountString, BalanceString);

            fw.append(line + System.getProperty("line.separator") + header + System.getProperty("line.separator") + line + System.getProperty("line.separator") + Transaction + System.getProperty("line.separator"));
            fw.flush();
            fw.close();
        } catch (IOException ex) {
            System.err.println("Could Write to: " + ftemp + File.separator + "Transactions.txt");
        }
        
        // XXXXXX.bin για γράψιμο πρώτου Transactions.      
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ftemp + File.separator + account.getIban() + ".bin"))) {
            oos.writeObject(account.getOpen());
            oos.flush();
            oos.close();
        } catch (IOException ex) {
            System.err.println("Could Write to: " + ftemp + File.separator + account.getIban() + ".txt");
        }

    }
    
    /**
     * Με το που φτιάχνετε ένας νέος πελάτης, ανοίγεις το φάκελο της διαδρομής: BANK_FILES\Bank_Database\Clients\\Id__xx (εαν δεν υπάρχει δημιουργείτε) και φτιάχνεις τα Details.txt και Clients.bin .
     * Επίσεις καλείτε κάθε φορά που ανοίγει ή κλείνει ένας λογαριασμός για να ανανεώσει τα στοιχεία του (έμμεσο UPDATE με Overwrite).
     * @param allClients 
     */
    public static void ClientFolder(ArrayList<Client> allClients) {

        for (Client clt : allClients) {
            File ftemp = new File("BANK_FILES"+File.separator+"Bank_Database"+File.separator+"Clients"+File.separator+"Id_" + clt.getId());
            ftemp.mkdirs();
            
            // Details.txt
            try (FileWriter fw = new FileWriter(ftemp +File.separator+ "Details.txt")) {
                fw.append("Id: " + clt.getId() + System.getProperty("line.separator") + "Name: " + clt.getName() + System.getProperty("line.separator"));
                if (clt.getAcounts().isEmpty()) {
                    fw.append("Account/s List: Not yet." + System.getProperty("line.separator"));
                } else {
                    fw.append("Account/s List: [ ");
                    for (Account ac : clt.getAcounts()) {
                        fw.append(ac.getIban() + " ");
                    }
                    fw.append("]" + System.getProperty("line.separator"));
                }
            } catch (IOException ex) {
                System.err.println("Could Write to: " + ftemp +File.separator+ "Details.txt");
            }

            //Client.bin
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ftemp +File.separator+ "Client.bin"))) {
                oos.writeObject(clt);
                oos.flush();
                oos.close();
            } catch (IOException ex) {
                System.err.println("Could Write to:" + ftemp + File.separator+"Client.bin");
            }

        }

    }
    
    /**
     * Με το που διαγράφεται ένας  λογαριασμός, ανοίγεις το φάκελο της διαδρομής: BANK_FILES\Bank_Database\Accounts\Closed\IBAN_xxxxxx (εάν δεν υπάρχει τότε δημιουργείτε) 
     * και φτιάχνει τα Details.txt, ΧΧΧΧΧΧ.txt(Με όλα τα Transactions). 
     * To ΧΧΧΧΧΧ.txt δημιουργείται ανοίγοντας το αντίστοιχο active και κάνοντας αντιγραφή (line by line) και έπεται διαγράφεται ο φάκελος από τον active φάκελο.
     * @param account Ο λογαριασμός που θέλουμε να εφαρμοστούν οι παραπάνω ενέργειες.
     */
    public static void ClosedAccountsFolder(Account account) {

        File ftemp = new File("BANK_FILES"+File.separator+"Bank_Database"+File.separator+"Accounts"+File.separator+"Closed"+File.separator+"IBAN_" + account.getIban());
        ftemp.mkdirs();

        // Details.txt    
        try (FileWriter fw = new FileWriter(ftemp + File.separator+"Details.txt")) {
            fw.append(account.toString().replaceAll("\n", System.getProperty("line.separator")));
        } catch (IOException ex) {
            System.err.println("Could Write to: " + ftemp +File.separator+ "Details.txt");
        }

        // ΧΧΧΧΧΧ.txt(Transactions) για γράψιμο πρώτου Transaction και φτιαξιμο Header αρχείου.
        try (FileWriter fw = new FileWriter(ftemp + File.separator + account.getIban() + ".txt"); Scanner sc = new Scanner(new FileReader("BANK_FILES"+File.separator+"Bank_Database"+File.separator+"Accounts"+File.separator+"Active"+File.separator+"IBAN_" + account.getIban() +File.separator+account.getIban()+".txt"))) {
            while (sc.hasNextLine()) {
                String Temp=sc.nextLine();
                fw.append(Temp + System.getProperty("line.separator"));
            }
            fw.append("+---------------------------------------------+"+ System.getProperty("line.separator"));
            sc.close();
            fw.close();
        }catch (IOException ex) {
            System.err.println("Could Write to: " + ftemp + "\\Trancastions.txt");
        }

            // Διαφραφή απο τον Active Φάκελο
            Delete.allFolder(new File("BANK_FILES"+File.separator+"Bank_Database"+File.separator+"Accounts"+File.separator+"Active"+File.separator+"IBAN_" + account.getIban()));

        }

    
    
}
