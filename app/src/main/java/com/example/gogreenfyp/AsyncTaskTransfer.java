package com.example.gogreenfyp;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.HashMap;

import static org.web3j.tx.gas.DefaultGasProvider.GAS_LIMIT;
import static org.web3j.tx.gas.DefaultGasProvider.GAS_PRICE;

public class AsyncTaskTransfer extends AsyncTask<String, String, String> {

    private static CollectionReference transactionReference = FirebaseFirestore.getInstance().collection("Transactions");
    private static CollectionReference userReference = FirebaseFirestore.getInstance().collection("Users");
    private static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private WeakReference<Activity> activityWeakReference;
    private Transaction transaction;

    public AsyncTaskTransfer(Activity activity, Transaction transaction) {
        this.activityWeakReference = new WeakReference<Activity>(activity);
        this.transaction = transaction;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(activityWeakReference.get(), "Transaction being processed in background. You will be notified when it completes.", Toast.LENGTH_LONG).show();
    }

    @Override
    protected String doInBackground(String... strings) {
        String transactionHash = "";
        try {
            Web3j web3j = Web3j.build(new HttpService("https://ropsten.infura.io/v3/23d1c7856d664d41842c3e8f8c228fe8"));
            Credentials credentials = new Wallet().getWalletCredentials(activityWeakReference.get());
            TransactionManager transactionManager = new RawTransactionManager(web3j, credentials);
            Transfer transfer = new Transfer(web3j, transactionManager);
            String[] addressAmount = strings[0].split(",");
            double amount = Double.parseDouble(addressAmount[1]);
            TransactionReceipt receipt = transfer.sendFunds(addressAmount[0], BigDecimal.valueOf(amount), Convert.Unit.ETHER, GAS_PRICE, GAS_LIMIT).send();
            transactionHash = receipt.getTransactionHash();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return transactionHash;
    }

    @Override
    protected void onPostExecute(String hash) {
        if(hash.length() < 1){
            Toast.makeText(activityWeakReference.get(), "Error while processing transaction", Toast.LENGTH_SHORT).show();
            return;
        }
        transaction.setTransactionNo(hash);
        insertTransaction(transaction, activityWeakReference.get());
    }
    private void insertTransaction(Transaction transaction, Activity activity){
        transactionReference.add(transaction).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                if(user != null) {
                    updatePoints(transaction.getPoints(), user.getUid());
                }
                Toast.makeText(activity, "Transaction completed!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }
    private void updatePoints(final Integer points, String userID){

        userReference.whereEqualTo("userID", userID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    User currentUser = documentSnapshot.toObject(User.class);
                    String docPath = documentSnapshot.getId();
                    int newTotalPoints = currentUser.getPointsBalance()+points;
                    DocumentReference documentReference = userReference.document(docPath);

                    HashMap<String, Object> newPointsBalance = new HashMap<String, Object>();
                    newPointsBalance.put("pointsBalance", newTotalPoints);

                    documentReference.set(newPointsBalance, SetOptions.merge());
                    Log.d("Update", "Success");
                }
            }
        });
    }
}
