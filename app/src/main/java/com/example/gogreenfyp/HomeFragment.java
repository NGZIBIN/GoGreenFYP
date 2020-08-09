package com.example.gogreenfyp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.gogreenfyp.adapters.PageAdapter;
import com.example.gogreenfyp.wallet.Wallet;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;

public class HomeFragment extends Fragment {
    private static String walletAddress;
    AsyncTaskBalance asyncTaskBalance;
    private Wallet wallet = new Wallet();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        ViewPager2 viewPager2 = view.findViewById(R.id.viewPagerFrag);
        viewPager2.setAdapter(new PageAdapter(getActivity()));

        TextView tvETH = view.findViewById(R.id.tvEthBalance);
        if (getActivity() != null) {
            walletAddress = wallet.getWalletAddress(getActivity());
            asyncTaskBalance = new AsyncTaskBalance(tvETH);
            asyncTaskBalance.execute(walletAddress);
        }

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0: {
                        tab.setText("Transaction");
                        break;
                    }
                    case 1: {
                        tab.setText("Leaderboard");
                        break;
                    }

                }
            }
        }
        );
        tabLayoutMediator.attach();
        return view;
    }

    private static class AsyncTaskBalance extends AsyncTask<String, String, String> {

        private WeakReference<TextView> tv;

        public AsyncTaskBalance(TextView textView) {
            tv = new WeakReference<TextView>(textView);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String balance = "";
            try {
                balance = getETHBalance(walletAddress);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return balance;
        }

        @Override
        protected void onPostExecute(String balance) {
            if (tv != null) {
                tv.get().setText(balance);
            }
        }
    }
    private static String getETHBalance(String address) throws IOException {
        Web3j web3j = Web3j.build(new HttpService("https://ropsten.infura.io/v3/23d1c7856d664d41842c3e8f8c228fe8"));
        EthGetBalance balance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();

        BigDecimal ether = Convert.fromWei(String.valueOf(balance.getBalance()), Convert.Unit.ETHER);
        String etherBalance = ether.toString();

        if (etherBalance.length() >= 12) {
            etherBalance = etherBalance.substring(0, 13);
        }
        return etherBalance;
    }

}
