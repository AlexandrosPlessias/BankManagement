/*
* Δημιουργία Κλάσης Πελάτης (Client).
*/

package Bank_Management;

import java.io.Serializable;
import java.util.ArrayList;

public class Client implements Serializable {

    private String name;                 // Όνομα πελάτη.
    private final int id;                // Μοναδικός κωδικός πελάτη.
    private ArrayList<Account> acounts;  // Η λίστα των λογαριασμών του πελάτη.
    
    // Static πεδίο το οποίο αυξάνεται όταν κατασκευάζεται κάθε νέο αντικείμενο αυτής της κλάσης.
    private static int lastId = 0;

    // Constractor.
    public Client(String name, ArrayList<Account> acounts) {
        lastId++;
        this.name = name;
        this.id = lastId;
        this.acounts = new ArrayList<>();
    }

    // Getters.
    public static int getLastId() {
        return lastId;
    }
    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
    public ArrayList<Account> getAcounts() {
        return acounts;
    }

    // Setters.
    public void setName(String name) {
        this.name = name;
    }
    public void setAcounts(Account acount) {
        this.acounts.add(acount);
    }

    // Methods.
    
    /**
     * Κάνει Override τη μέθοδο toString της κλάσης Object.
     * @return  Mόνο το String του ονόματος του πελάτη.
     */
    @Override
    public String toString() {

        return (""+this.name);

    }

    /**
     * Είναι ένα format για το πως θέλουμε ακριβώς να φαίνονται όλα τα στοιχεία του πελάτη.
     */
    public void printElement() {

        System.out.println("\nName: " + this.name);
        System.out.println("Id: " + this.id);
        System.out.print("Account/s List: ");

        if (this.acounts.isEmpty()) {
            System.out.print("Not yet.\n");
        } else {
            for (int i = 0; i < this.acounts.size(); i++) {
                if (i == 0) {
                    System.out.print("[");
                }
                if (i == this.acounts.size() - 1) {
                    System.out.print(this.acounts.get(i).getIban() + "");
                    System.out.print("]\n");
                    break;
                }
                System.out.print(this.acounts.get(i).getIban() + ", ");

            }

        }

    }

}
