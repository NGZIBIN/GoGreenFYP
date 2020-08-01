package com.example.gogreenfyp;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;

import static org.web3j.tx.gas.DefaultGasProvider.GAS_LIMIT;
import static org.web3j.tx.gas.DefaultGasProvider.GAS_PRICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScanQRFragment extends Fragment {

    CodeScanner codeScanner;
    CodeScannerView scannerView;
    TextView walletAddress, tv_receiver, tv_amount;
    Button btn_dialog_yes, btn_dialog_no;
    Dialog dialog;
    private static CollectionReference transactionReference = FirebaseFirestore.getInstance().collection("Transactions");
    private FragmentActivity activity = getActivity();
    private static Transaction transaction;
    String DIALOG_RESULT;

    public ScanQRFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan_qr, container, false);

        scannerView = view.findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(getContext(), scannerView);
        walletAddress = view.findViewById(R.id.qr_textview);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject qrObject = new JSONObject(result.getText());
                            String address = qrObject.getString("walletAddress");
                            String currentUserWallet = new Wallet().getWalletAddress(getActivity());
                            String name = qrObject.getString("name");
                            String item = qrObject.getString("item");
                            double amount = qrObject.getDouble("amount");
                            int points = qrObject.getInt("points");

                            //DIALOG_RESULT = result.getText();
                            transaction = new Transaction(amount, name, item, "", currentUserWallet, points);
                            String addressAmount = address+","+amount;

                            dialog = new Dialog(getActivity());
                            dialog.setContentView(R.layout.dialog_payment_confirmation);
                            dialog.setCancelable(false);
                            dialog.show();

                            tv_amount = dialog.findViewById(R.id.tv_payment_amount);
                            tv_receiver = dialog.findViewById(R.id.tv_payment_title_reciever);
                            btn_dialog_yes = dialog.findViewById(R.id.btnYes);
                            btn_dialog_no = dialog.findViewById(R.id.btnNo);

                            // TODO: Split the string to get MERCHANT and AMOUNT

                            // Set merchant & amount
                            tv_receiver.setText(name);
                            tv_amount.setText(amount+"");

                            btn_dialog_yes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    AsyncTaskTransfer taskTransfer = new AsyncTaskTransfer(getActivity());
                                    taskTransfer.execute(addressAmount);
                                }
                            });

                            btn_dialog_no.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Dismiss dialog and start scanner again
                                    dialog.dismiss();
                                    codeScanner.startPreview();
                                }
                            });
                        } catch (JSONException e) {
                            codeScanner.startPreview();
                            Toast.makeText(getContext(), "Error while scanning QR code", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        codeScanner.startPreview();
        requestForCamera();
    }

    private void requestForCamera() {
        Dexter.withContext(getContext()).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                codeScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(getContext(), "Camera Permission Required", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    private static class AsyncTaskTransfer extends AsyncTask<String, String, String> {

        private WeakReference<Activity> activityWeakReference;

        public AsyncTaskTransfer(Activity activity) {
            this.activityWeakReference = new WeakReference<Activity>(activity);
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
    }
    private static void insertTransaction(Transaction transaction, Activity activity){
        transactionReference.add(transaction).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(activity, "Transaction completed!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }
    private void handleDifferentTransactions(JSONObject jsonObject){

    }
}


