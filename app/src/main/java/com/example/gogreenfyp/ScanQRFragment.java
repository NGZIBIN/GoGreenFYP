package com.example.gogreenfyp;

import android.Manifest;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScanQRFragment extends Fragment {

    CodeScanner codeScanner;
    CodeScannerView scannerView;
    TextView walletAddress, tv_receiver, tv_amount;
    Button btn_dialog_yes, btn_dialog_no;
    Dialog dialog;
    String DIALOG_RESULT;
    EditText etAmount;

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
                            final Transaction transaction = new Transaction(amount, name, item, "", currentUserWallet, points);

                            dialog = new Dialog(getActivity());
                            configureDialogByAmount(amount, name);

                            btn_dialog_yes = dialog.findViewById(R.id.btnYes);
                            btn_dialog_no = dialog.findViewById(R.id.btnNo);

                            btn_dialog_yes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    startTransactionProcess(address, amount, transaction);
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
                            dialog.setCancelable(false);
                            dialog.show();
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

    private void configureDialogByAmount(double amt, String name) {
        if (amt > 0) {
            dialog.setContentView(R.layout.dialog_payment_confirmation);

            tv_amount = dialog.findViewById(R.id.tv_payment_amount);
            tv_receiver = dialog.findViewById(R.id.tv_payment_title_reciever);

            // Set merchant & amount
            tv_receiver.setText(name);
            tv_amount.setText(amt + "");
        } else {
            dialog.setContentView(R.layout.dialog_p2p);

            tv_receiver = dialog.findViewById(R.id.tvReceiever);
            tv_receiver.setText(name);
            etAmount = dialog.findViewById(R.id.etAmount);
        }
    }

    private void startTransactionProcess(String address, double amount, Transaction transaction) {
        AsyncTaskTransfer taskTransfer = new AsyncTaskTransfer(getActivity(), transaction);

        if (etAmount != null) {
            if (etAmount.getText().toString().length() < 1 || etAmount.getText().toString().equals("0")) {
                Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
                return;
            }
            taskTransfer.execute(address + "," + etAmount.getText().toString());
        }
        else {
            taskTransfer.execute(address + "," + amount);
        }
    }
}


