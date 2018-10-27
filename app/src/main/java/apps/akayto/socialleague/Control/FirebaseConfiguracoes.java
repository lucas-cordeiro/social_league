package apps.akayto.socialleague.Control;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by LUCASGABRIELALVESCOR on 12/03/2018.
 */

public class FirebaseConfiguracoes {

    private static FirebaseAuth firebaseAuth;

    private static DatabaseReference firebaseDatabase;

    public static FirebaseAuth getFirebaseAuth() {
        if(firebaseAuth==null)
            firebaseAuth = FirebaseAuth.getInstance();

        return firebaseAuth;
    }

    public static DatabaseReference getFirebaseDatabase() {
        if(firebaseDatabase == null)
            firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        return firebaseDatabase;
    }
}
