/*
 * Δημιουργία Κλάσης Διαβάσματος (Read).
 */
package File_Management;

import Bank_Management.SimpleCreditAccount;
import Bank_Management.ReserveAccount;
import Bank_Management.SuperCreditAccount;
import Bank_Management.Transaction;
import Bank_Management.Client;
import Bank_Management.Account;
import java.io.*;
import java.util.*;
import static java.lang.System.exit;
import java.lang.reflect.Field;

public class Read {

    // Methods.
    /**
     * Διαβάζει το filePath και φτιάχνει τους πελάτες.
     * @param filePath Η διαδρομή του αρχείου που περιέχει την ουρά των πελατών.
     * @return Η λίστα με όλους τους πελάτες του αρχείου.
     */
    public static ArrayList<Client> clientsFromTxt(File filePath) {

        ArrayList<Client> allClients = new ArrayList<>();
        try (Scanner sc = new Scanner(new FileReader(filePath))) {
            while (sc.hasNextLine()) {
                allClients.add(new Client(sc.nextLine(), null));
            }
            sc.close();
        } catch (IOException ioex) {
            System.err.println("Could not find file " + filePath + ".");
            System.err.println("Please, try to retrieve queueCustomers.txt.");
            exit(0);
        }
        return allClients;
    }

    /**
     * Διαβάζει τους πελάτες από το clientsDirectory.
     * @param clientsDirectory Η διαδρομή του αρχείου που περιέχει τα
     * αντικείμενα των πελατών.
     * @return Η λίστα με όλους τους πελάτες του αρχείου.
     */
    public static ArrayList<Client> clientsFromFile(File clientsDirectory) {

        ArrayList<Client> allClients = new ArrayList<>();
        int size = clientsDirectory.listFiles().length; // Μεγέθος αρχείων στον clientDirectory.

        try {

            for (int i = 1; i <= size; i++) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(clientsDirectory + "\\Id_" + i + "\\Client.bin")));
                Client clt = (Client) ois.readObject();
                allClients.add(clt);
            }
        } catch (FileNotFoundException fnfe) {
            // Αυτό δεν πρεπεί να συμβεί διότι αυτή η εξαίρεση αν δεν υπάρχει το αρχείο δεν θα φτάσει ποτέ εδω "πιάνεται" στην restore.
            System.err.println("Could Read from:" + clientsDirectory);
        } catch (IOException | ClassNotFoundException cnfe) {
            //  Αυτό δεν πρεπεί να συμβεί διότι αυτή η εξαίρεση αν δεν υπάρχει το αρχείο δεν θα φτάσει ποτέ εδω "πιάνεται" στην restore.
            System.err.println("Could Read from:" + clientsDirectory + ". Or Wrong Class as input.");
        }

        return allClients;
    }

    /**
     * Πέρασμα όλων των Active λογαριασμών στην λίστα (γίνεται έλεγχος
     * ορθότητας) και επίσεις αλλάζει η τιμή του static πεδίου lastIban στο
     * οποίο δεν έχω πρόσβαση με reflaction ώστε να μην χαλάσει η αλληλουχία των
     * IBANS.
     *
     * @param ActiveAccountsDirectory Η διαδρομή του αρχείου που περιέχει τα
     * αντικείμενα των ενεργών λογαριασμών.
     * @param ClosedAccountsDirectory Η διαδρομή του αρχείου που περιέχει τα
     * αντικείμενα των λειστών λογαριασμών.
     * @return Η λίστα με όλους τους ενεργούς λογαριασμους των αρχείου.
     */
    public static ArrayList<Account> accountsFromFile(File ActiveAccountsDirectory, File ClosedAccountsDirectory) {

        ArrayList<Account> allAccounts = new ArrayList<>();
        int sizeActive = 0;
        if (ActiveAccountsDirectory.exists()) {
            sizeActive = ActiveAccountsDirectory.listFiles().length; // Αριθμός ανοιχτών λογαριασμών.
        }
        
        int sizeClosed = 0;
        if (ClosedAccountsDirectory.exists()) {
            sizeClosed = ClosedAccountsDirectory.listFiles().length; // Αριθμός κλειστών λογαριασμών.
        }

        try {
            // Πέρασμα όλων των λογαριασμών ώστε ΝΑ ΜΗΝ ΧΑΘΕΙ Η ΣΩΣΤΗ ΣΕΙΡΑ ΤΩΝ ΙΒΑΝs.
            for (int i = 100001; i <= (100000 + sizeActive + sizeClosed); i++) {
                int typeCode = -1;

                // Ελένχουμε αν υπάρχει στους Active ο λογαριασμός που είναι προς εξέταση.
                if (new File(ActiveAccountsDirectory + "\\IBAN_" + i).exists()) {

                    // Ελένχουμε τη τύπου είναι ο λογαριασμός απο την πρώρη σειρά των Details.txt .
                    Scanner sc = new Scanner(new FileReader(ActiveAccountsDirectory + "\\IBAN_" + i + "\\Details.txt"));
                    String type = sc.nextLine();
                    sc.close();
                    if (type.equalsIgnoreCase("Simple Credit Account")) {
                        typeCode = 0;
                    } else if (type.equalsIgnoreCase("Super Credit Account")) {
                        typeCode = 1;
                    } else if (type.equalsIgnoreCase("Reserved Account")) {
                        typeCode = 2;
                    }

                    // Διαβάζουμε το αντικείμενο που ξέρουμε πλέον τη κλάση είναι και το προσθέτουμε στην λίστα λογαριασμών.
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(ActiveAccountsDirectory + "\\IBAN_" + i + "\\Account.bin")));
                    if (typeCode == 0) {
                        SimpleCreditAccount simpleca = (SimpleCreditAccount) ois.readObject();
                        ois.close();
                        allAccounts.add(simpleca);
                    } else if (typeCode == 1) {
                        SuperCreditAccount superca = (SuperCreditAccount) ois.readObject();
                        ois.close();
                        allAccounts.add(superca);

                    } else {
                        ReserveAccount ra = (ReserveAccount) ois.readObject();
                        ois.close();
                        allAccounts.add(ra);
                    }

                    // Αν δεν υπάρχει στους Active δηλαδή είναι Closed τότε απλά φτιάχνουμε έναν "εικονικό" μόνο και μόνο για μην χαλάσει η σειρα των active λογαριασμων (static ΙΒΑΝ).
                } else {
                    Transaction tran = new Transaction(-100, 0);
                    SimpleCreditAccount ac = new SimpleCreditAccount(null, tran);
                }
            }
        } catch (FileNotFoundException e) {
            // Αυτό δεν πρεπεί να συμβεί διότι αυτή η εξαίρεση αν δεν υπάρχει το αρχείο δεν θα φτάσει ποτέ εδω "πιάνεται" στην restore.
            System.err.println("Could Read from:" + ActiveAccountsDirectory);
        } catch (IOException | ClassNotFoundException ex) {
            // Αυτό δεν πρεπεί να συμβεί διότι αυτή η εξαίρεση αν δεν υπάρχει το αρχείο δεν θα φτάσει ποτέ εδω "πιάνεται" στην restore.
            System.err.println("Could Read from:" + ActiveAccountsDirectory);
        }

        // Σβήνουμε τους λογαριασμούς που εχουν υπόλοιπο μηδέν.
        for (int i = allAccounts.size() - 1; i < 0; i++) {
            if (allAccounts.get(i).getOpen().getAmount() == 0) {
                allAccounts.remove(allAccounts.get(i));
            }
        }

        // Αλλαγή του static πεδίου lastIban στο οποίο δεν έχω πρόσβαση με reflaction και πέρασμα τιμής ίση με εκείνη που είχε σταματήσει το πρόγραμμα την τελευταία φορά +1.
        try {
            Field field = Account.class.getDeclaredField("lastIban");
            field.setAccessible(true);
            field.set(null, sizeActive + sizeClosed + 100000 - 1);
        } catch (NoSuchFieldException | SecurityException se) {
            // Αυτό δεν πρεπεί να συμβεί διότι αυτή η εξαίρεση αν δεν υπάρχει το αρχείο δεν θα φτάσει ποτέ εδω "πιάνεται" στην restore.
            se.getCause();
        } catch (IllegalArgumentException | IllegalAccessException iae) {
            // Αυτό δεν πρεπεί να συμβεί διότι αυτή η εξαίρεση αν δεν υπάρχει το αρχείο δεν θα φτάσει ποτέ εδω "πιάνεται" στην restore.
            iae.getCause();
        }

        // Εάν δεν θέλαμε να κρατήσουμε τους κλειστούς λογαριασμούς τότε: DeleteFolder.allFolder(ClosedAccountsDirectory);
        return allAccounts;
    }

}
