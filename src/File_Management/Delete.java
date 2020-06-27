/*
 * Δημιουργία Κλάσης Διαγραφής (Delete).
 */
package File_Management;

import java.io.File;

public class Delete {

    // Methods.
    
    /**
     * Διαγράφει όλα τα περιεχόμενα του φακέλου και τον ίδιο.
     * @param directory Ο φάκελος που θέλουμε να διαγραφθεί.
     */
    public static void allFolder(File directory) {
        File[] files = directory.listFiles();
        if (null != files && files.length > 0) {
            int filesCount = files.length;
            for (int i = 0; i < filesCount; i++) {
                allFolder(files[i]);
            }
        }
        directory.delete();
    }

}
