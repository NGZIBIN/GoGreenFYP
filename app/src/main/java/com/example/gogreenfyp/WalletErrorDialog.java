package com.example.gogreenfyp;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WalletErrorDialog extends AlertDialog implements android.view.View.OnClickListener {

        public Activity activity;
        public AlertDialog alertDialog;
        public Button createWallet, importWallet;

        public WalletErrorDialog(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.activity = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_error_wallet);
            createWallet = (Button) findViewById(R.id.btnCreateWallet);
            importWallet = (Button) findViewById(R.id.btnImportWallet);
            createWallet.setOnClickListener(this);
            importWallet.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnCreateWallet:
                    dismiss();
                    break;
                case R.id.btnImportWallet:
                    Intent intent = new Intent(this.activity, ImportWalletActivity.class);
                    this.activity.startActivity(intent);
                    break;
                default:
                    break;
            }
            dismiss();
        }

}
