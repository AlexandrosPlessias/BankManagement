/**
 * Πληροφορίες για την εργασία θα βρείτε στο pdf με τίτλο "Περιγραφή Υλοποίησης 
 * & Χρόνοι Εκτέλεσης" στον φάκελο που είναι και το Students.txt .
 */
package Bank_Management;

import java.util.*;
import java.io.*;
import File_Management.Create;
import File_Management.Delete;
import File_Management.Read;
import static java.lang.System.exit;

/**
 * @author Πλέσσιας Αλέξανδρος (ΑΜ.:2025201100068).
 * @author Παπαγεωργόπουλος Νικόλαος (ΑΜ.:2025201200071).
 */
public class Main {

    public static void main(String[] args) {
        // Εμφάνιση Τext Art.
        logo();
        // Restore Ναί ή Όχι;
        restoreYesOrNot();
    }

/////////////////// Methods ////////////////////////////
    /**
     * Text Art Bank.
     */
    public static void logo() {

        System.out.println();
        System.out.println("   $$\\          $$$$$$$\\   $$$$$$\\  $$\\   $$\\ $$\\   $$\\          $$\\  ");
        System.out.println(" $$$$$$\\        $$  __$$\\ $$  __$$\\ $$$\\  $$ |$$ | $$  |       $$$$$$\\  ");
        System.out.println("$$  __$$\\       $$ |  $$ |$$ /  $$ |$$$$\\ $$ |$$ |$$  /       $$  __$$\\ ");
        System.out.println("$$ /  \\__|      $$$$$$$\\ |$$$$$$$$ |$$ $$\\$$ |$$$$$  /        $$ /  \\__|");
        System.out.println("\\$$$$$$\\        $$  __$$\\ $$  __$$ |$$ \\$$$$ |$$  $$<         \\$$$$$$\\  ");
        System.out.println(" \\___ $$\\       $$ |  $$ |$$ |  $$ |$$ |\\$$$ |$$ |\\$$\\          \\___ $$\\ ");
        System.out.println("$$\\  \\$$ |      $$$$$$$  |$$ |  $$ |$$ | \\$$ |$$ | \\$$\\        $$\\  \\$$ |");
        System.out.println("\\$$$$$$  |      \\_______/ \\__|  \\__|\\__|  \\__|\\__|  \\__|      \\$$$$$$  |");
        System.out.println(" \\_$$  _/                                                      \\_$$  _/ ");
        System.out.println("   \\__/                                                          \\__/ ");
        System.out.println("                    _._._                       _._._");
        System.out.println("                   _|   |_                     _|   |_");
        System.out.println("                   | ... |_._._._._._._._._._._| ... |");
        System.out.println("                   | ||| |  o NATIONAL BANK o  | ||| |");
        System.out.println("                   | ~~~ |  ~~~    ~~~    ~~~  | ~~~ |");
        System.out.println("              ())  |[-|-]| [-|-]  [-|-]  [-|-] |[-|-]|  ())");
        System.out.println("             (())) |     |---------------------|     | (()))");
        System.out.println("            (())())| ~~~ |  ~~~    ~~~    ~~~  | ~~~ |(())())");
        System.out.println("            (()))()|[-|-]|  :::   .-'-.   :::  |[-|-]|(()))()");
        System.out.println("            ()))(()|     | |~|~|  |_|_|  |~|~| |     |()))(()");
        System.out.println("               ||  |_____|_|_|_|__|_|_|__|_|_|_|_____|  ||");
        System.out.println("            ~ ~^^ @@@@@@@@@@@@@@/=======\\@@@@@@@@@@@@@@ ^^~ ~");
        System.out.println("\n                 ^~^~  cst12071           cst11068   ~^~^");
        System.out.println();

    }

    /**
     * Ανάλογα με την επιλογή δημιουργούνται ή ανακτούνται οι λίστες Λογαριασμών
     * και Πελατών. Καθώς και καθοριζονται ή διαβαζονται οι μέρες έναρξης και
     * τέλους της προσομοίωσης . Για τρέξιμο εξ'αρχής από την μέρα 1 εως 210.
     * Για τρέξιμο σε restore mode από την τελευταία μέρα της προηγούμενης
     * προσομοίωσης μέχρι +90 μέρες.
     */
    public static void restoreYesOrNot() {
        Scanner input = new Scanner(System.in);
        char backUp;
        
        System.out.print("   Please, read first \"Περιγραφή Υλοποίησης & Χρόνοι Εκτέλεσης.pdf\".\n\n");

        // Ερώτηση για Restore.
        do {
            System.out.print("   Want to Restore the last situation of the NATIONAL BANK?(Y/y)/(N/n): ");
            backUp = input.next().charAt(0);
        } while ((backUp != 'Y') && (backUp != 'y') && (backUp != 'N') && (backUp != 'n'));

        if ((backUp == 'Y') || (backUp == 'y')) {
            try (Scanner sc = new Scanner(new File("BANK_FILES" + File.separator + "Bank_Database" + File.separator + "restoreDay.txt"))) {
                int start = sc.nextInt();
                sc.close();
                int end;
                do {
                    System.out.print("   Please, enter the number of days you want to run the simulation: ");
                    Scanner scan = new Scanner(System.in);
                    end = scan.nextInt();
                } while ((end <= 1));

                System.out.println("   Loading ...");
                ArrayList<Client> allClients = Read.clientsFromFile(new File("BANK_FILES" + File.separator + "Bank_Database" + File.separator + "Clients"));
                ArrayList<Account> allAccounts = Read.accountsFromFile(new File("BANK_FILES" + File.separator + "Bank_Database" + File.separator + "Accounts" + File.separator + "Active"), new File("BANK_FILES" + File.separator + "Bank_Database" + File.separator + "Accounts" + File.separator + "Closed"));

                bankSimulation(start, end, allClients, allAccounts);
            } catch (InputMismatchException | NumberFormatException ex) {
                System.err.println("Wrong Input.");
            } catch (FileNotFoundException ex) {
                System.err.println("Can't Read from: BANK_FILES .");
                System.err.println("Please run again the program and select N/n.");
            }

        } else {
            Delete.allFolder(new File("BANK_FILES" + File.separator + "Bank_Database" + File.separator));
            ArrayList<Account> allAccounts = new ArrayList<>();
            ArrayList<Client> allClients = Read.clientsFromTxt(new File("BANK_FILES" + File.separator + "Resources" + File.separator + "queueCustomers.txt"));
            int start = 1;

            int end;
            do {
                System.out.print("   Please, enter the number of days you want to run the simulation: ");
                Scanner scan = new Scanner(System.in);
                end = scan.nextInt();
            } while ((end <= 1));

            bankSimulation(start, end, allClients, allAccounts);
        }

    }

    /**
     * Η προσομοίωση της τράπεζας για το διάστημα start-end με τυχαίες
     * συναλλαγές κάθε μερα και να καταβάλει τόκους στο τέλος της κάθε μέρας.
     * @param start Μέρα εναρξής προηγούμενης.
     * @param end Μέρα τέλους προηγούμενης.
     * @param allClients Η λίστα Πελατών.
     * @param allAccounts Η λίστα Λογαριασμών.
     */
    public static void bankSimulation(int start, int end, ArrayList<Client> allClients, ArrayList<Account> allAccounts) {

        int transactionCode;
        int day;

        // Ρολόι της προσομοίωσης.
        for (day = start; day < (start + end); day++) {

            System.out.println("\n================================================ DAY " + day + " ================================================");

            if (day == 1) {
                // Άνοιγμα 7 λογαριασμών κάθε πρώτη μέρα της προσομοίωσης.
                for (int i = 0; i <= 7; i++) {
                    allAccounts.add(openAccount(day, allClients)); // Generate a new account ( 40% for SimpleCreditAccount, 40% for SuperCreditAccount & 20% for ReservedAccount). 
                }
            }

            // Σε περίπτωση που η τράπεζα δεν έχουν λογαριασμούς ποια κλείνει και πτωχεύει.
            if (allAccounts.isEmpty()) {
                System.out.println("The bank became insolvent, all accounts have closed.\nCapitalism loss :-) . ");
                exit(0);
            }

            //Αριθμός ημερήσιων συναλλαγών.
            int numberOftransaction = new Random().nextInt(3) + 1;
            for (int i = 1; i <= numberOftransaction; i++) {
                transactionCode = new Random().nextInt(20);
                // Επιλογές από 0 εώς 19.
                if ((transactionCode >= 0) && (transactionCode <= 2)) { // ΑΝΟΙΓΜΑ ΛΟΓΑΡΙΑΣΜΟΥ - (3/20)15%
                    allAccounts.add(openAccount(day, allClients)); // Δημιουργία ενός νέου λογαριασμού ( 40% για SimpleCreditAccount, 40% για SuperCreditAccount & 20% για ReservedAccount).
                    // Ενημέρωση τους Πελάτες μετά από το Άνοιγμα.
                    Create.ClientFolder(allClients);
                } else if ((transactionCode >= 3) && (transactionCode <= 10)) { // ΚΑΤΑΘΕΣΗ - (8/20)40%
                    int randomAccountNummber = new Random().nextInt(allAccounts.size()); // Τυχαίο λογαριασμό από ArrayList.
                    deposit(day, allAccounts.get(randomAccountNummber));
                } else if ((transactionCode >= 11) && (transactionCode <= 18)) {// ΑΝΑΛΗΨΗ - (8/20)40%
                    int randomAccountNummber = new Random().nextInt(allAccounts.size()); // Τυχαίο λογαριασμό από ArrayList.
                    withdraw(day, allAccounts.get(randomAccountNummber));
                } else if (transactionCode == 19) { // ΚΛΕΙΣΙΜΟ - (1/20)5%
                    int randomAccountNummber = new Random().nextInt(allAccounts.size()); // Τυχαίο λογαριασμό από ArrayList.
                    closeAccount(day, allAccounts.get(randomAccountNummber));
                    // Ενημέρωση τους Πελάτες μετά από το Κλείσιμο.
                    Create.ClientFolder(allClients);
                }

            }

            // Καταβάλει τόκους.
            payInterest(day, allAccounts);

            // Αφαιρεί άδειους - κλειστούς λογαριασμούς στο φάκελο Closed.
            removeEmpty_ClosedAccounts(allAccounts);
        }

        // Δημιουργία ημέρας Επαναφοράς.
        try (FileWriter fw = new FileWriter("BANK_FILES" + File.separator + "Bank_Database" + File.separator + "restoreDay.txt")) {
            fw.append(Integer.toString(start + end));
            fw.flush();
            fw.close();
        } catch (IOException ex) {
            System.err.println("Could Write to: BANK_FILES" + File.separator + "restoreDay.txt");
        }
    }

    /**
     * Εδώ γίνεται το άνοιγμα κάθε νέου λογαριασμού(τυχαίος ), δημιουργεία
     * τυχαίας συναλλαγής ανοίγματος (απο 1.000 εώς 10.000) και χρήση
     * getRandomOwners.
     * @param day Ημέρα ανοίγματος του λογαριασμού.
     * @param allClients Η λίστα με όλους τους πελάτες.
     * @return Ο νέος λογαριασμός.
     */
    public static Account openAccount(int day, ArrayList<Client> allClients) {

        Transaction tran = new Transaction(day, 1000 + (10000 - 1000) * (new Random().nextDouble())); // Random double amount from 1.000$ to 10.000$.

        int probOfAccType = new Random().nextInt(10);
        ArrayList<Client> temp = getRandomOwners(allClients);

        if ((probOfAccType >= 0) && (probOfAccType <= 3)) { // Αριθμός από (0 εώς 3)/10= 40% πιθανότητα για SimpleCreditAccount.

            SimpleCreditAccount simpleca = new SimpleCreditAccount(temp, tran);
            // Προσθήκη λογαριασμού στη λίστα λογαριασμών του κάθε πελάτη.
            for (int i = 0; i < temp.size(); i++) {
                temp.get(i).setAcounts(simpleca);
            }
            System.out.println("[Banker]: OPEN a new " + simpleca);
            Create.ActiveAccountFolder(simpleca);
            return simpleca;

        } else if ((probOfAccType >= 4) && (probOfAccType <= 7)) { // Αριθμός από (4 εώς 7)/10= 40% πιθανότητα SuperCreditAccount.                       
            SuperCreditAccount superca = new SuperCreditAccount(temp, tran);
            // Προσθήκη λογαριασμού στη λίστα λογαριασμών του κάθε πελάτη.
            for (int i = 0; i < temp.size(); i++) {
                temp.get(i).setAcounts(superca);
            }
            System.out.println("[Banker]: OPEN a new " + superca);
            Create.ActiveAccountFolder(superca);
            return superca;

        } else if ((probOfAccType >= 8) && (probOfAccType <= 9)) { // Αριθμός από (8 εώς 9)/10= 20% πιθανότητα ReverseAccount.
            int probOfClosePeriod = new Random().nextInt(4);
            int period=-100;
            if (probOfClosePeriod == 0) {
                period = 3;
            } else if (probOfClosePeriod == 1) {
                period = 6;
            } else if (probOfClosePeriod == 2) {
                period = 9;
            } else if (probOfClosePeriod == 3) {
                period = 12;
            }
            ReserveAccount ra = new ReserveAccount(period, temp, tran);
            // Προσθήκη λογαριασμού στη λίστα λογαριασμών του κάθε πελάτη.
            for (int i = 0; i < temp.size(); i++) {
                temp.get(i).setAcounts(ra);
            }
            System.out.println("[Banker]: OPEN a new " + ra);
            Create.ActiveAccountFolder(ra);
            return ra;
        }
        return null;
    }

    /**
     * Εδώ γίνεται το κλέισιμο ενός λογαριασμού.
     * @param day Ημέρα κλεισίματος λογαριασμού.
     * @param curAccount Ο λογαριασμός που πρόκειτε να κλείσει.
     */
    public static void closeAccount(int day, Account curAccount) {
        String strBalance = String.format("%.02f", curAccount.getBalance());
        System.out.println("[Client]: I want to CLOSE the account with IBAN " + curAccount.getIban() + " and balance $" + strBalance + ".");
        curAccount.close(day);
    }

    /**
     * Εδώ γίνεται η κατάθεση σε ένα λογαριασμό, τυχαίο ποσο (100.00-500.00).
     * @param day Ημέρα κατάθεσης.
     * @param curAccount Ο λογαριασμός που πρόκειτε να γίνει κατάθεση.
     */
    public static void deposit(int day, Account curAccount) {
        Transaction tran = new Transaction(day, 100 + (500 - 100) * (new Random().nextDouble())); // Τυχαίο double ποσό από 100$ εώς 500$.

        String strAmount = String.format("%.02f", tran.getAmount());
        String strBalance = String.format("%.02f", curAccount.getBalance());
        System.out.println("[Client]: I want to make a DEPOSIT $" + strAmount + " in account with IBAN " + curAccount.getIban() + " and balance $" + strBalance + ".");

        try {
            curAccount.deposit(tran);
        } catch (DepositException de) {
            de.printErrorMessage();
        }

    }

    /**
     * Εδώ γίνεται η ανάληψη σε ένα λογαριασμό, τυχαίο ποσο (-100.00 εως
     * -500.00).
     * @param day Ημέρα ανάληψης.
     * @param curAccount Ο λογαριασμός που πρόκειτε να γίνει ανάληψη.
     */
    public static void withdraw(int day, Account curAccount) {
        Transaction tran = new Transaction(day, -500 + ((-100) - (500)) * (new Random().nextDouble())); // Τυχαίο double ποσό από -100$ εώε -500$.

        String strAmount = String.format("%.02f", tran.getAmount());
        String strBalance = String.format("%.02f", curAccount.getBalance());
        System.out.println("[Client]: I want to make a WITHDRAW $" + strAmount + " in account with IBAN " + curAccount.getIban() + " and balance $" + strBalance + ".");

        try {
            curAccount.withdraw(tran);
        } catch (WithdrawException we) {
            we.printErrorMessage();
        }

    }

    /**
     * Εδώ γίνεται ο τοκισμός όσων λογαριασμών χρειάζονται την ημέρα που πρέπει.
     * @param day Τρέχων ημέρα.
     * @param allAccounts Όλοι οι λογαριασμοί.
     */
    public static void payInterest(int day, ArrayList<Account> allAccounts) {
        for (Account ac : allAccounts) {
            if ((day % 30 == 0) && (ac instanceof SuperCreditAccount)) { // Καταβάλει τόκους κάθε μήνα.
                ac.payInterest(day);
            } else if ((day % 180 == 0) && (ac instanceof SimpleCreditAccount)) { // Καταβάλει τόκους κάθε 6-μηνο.
                ac.payInterest(day);
            } else if (ac instanceof ReserveAccount) { // Καταβάλει τόκους μετά από 3/6/9/12 μήνες από το άνοιγμα.
                ac.payInterest(day);
            }
        }
    }

    /**
     * Αφαιρεί άδειους - κλειστούς λογαριασμούς στο φάκελο Closed.
     * @param allAccounts Η λίστα με τους λογαρισμούς.
     */
    public static void removeEmpty_ClosedAccounts(ArrayList<Account> allAccounts) {
        for (int i = allAccounts.size() - 1; i >= 0; i--) {
            if (allAccounts.get(i).getBalance() == 0) {
                Create.ClosedAccountsFolder(allAccounts.get(i));
                allAccounts.remove(i);
            }
        }
    }

    /**
     * Απ' όλους τους πελάτες επιλέγει τυχαία 1-3 πελάτες και τους βάζει σε ένα
     * ArrayList, ελέγχει εαν κάποιος απο τους τυχαίους πελάτες είναι και 2η
     * φορά στη λίστα.
     * @param allClients Η λίστα με όλους τους πελάτες.
     * @return Τη λίστα με τους τυχαίους πελάτες(1-3).
     */
    public static ArrayList<Client> getRandomOwners(ArrayList<Client> allClients) {

        ArrayList<Client> owners = new ArrayList<>();
        int numOfOwners = new Random().nextInt(3) + 1;
        int counter = 0;
        do {
            counter++;
            int pos = new Random().nextInt(allClients.size());
            owners.add(allClients.get(pos));
        } while (!((numOfOwners == counter)));

        // Διαγραφή διπλότυπων αν υπάρχουν δηλαδή τυχαία επιλογή 2 ίδιων πελατών  μέσω του HashSet (το ποίο δεν επιτρέπει διπλότυπα εσωτερικά του).
        HashSet hs = new HashSet();
        hs.addAll(owners);
        
        owners.clear();
        owners.addAll(hs);

        return owners;
    }

}
