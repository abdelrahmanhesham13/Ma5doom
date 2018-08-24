package sma.tech.ma5doom.bankAccounts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import sma.tech.ma5doom.R;
import sma.tech.ma5doom.bankAccounts.model.Bank;

public class BankAccountAdapter extends RecyclerView.Adapter<BankAccountAdapter.BankViewHolder> {


    private Context mContext;
    private ArrayList<Bank> data;

    public BankAccountAdapter(Context context) {
        mContext = context;
    }

    public void updateData(ArrayList<Bank> data){
        this.data=data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BankViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull BankViewHolder holder, int position) {

        if (data==null || data.size()==0)
            return;
        Bank bankAccount = data.get(position);
        holder.bankName.setText(bankAccount.getName());
//        holder.accountName.setText());
        holder.accountNumber.setText(bankAccount.getAccount());

    }

    @Override
    public int getItemCount() {
        if (data != null)
            return data.size();

        return 0;
    }


    static class BankViewHolder extends RecyclerView.ViewHolder {
        public TextView bankName;
        public TextView accountName;
        public TextView accountNumber;

        public BankViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_bank_account, parent, false));
            bankName = (TextView) itemView.findViewById(R.id.bank_name);
            accountName = (TextView) itemView.findViewById(R.id.account_name);
            accountNumber = (TextView) itemView.findViewById(R.id.account_number);


        }

    }
}
