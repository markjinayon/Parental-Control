package com.parentalcontrol.seesharp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.parentalcontrol.seesharp.R;
import com.parentalcontrol.seesharp.entity.User;

import java.util.ArrayList;

public class UserViewAdapter extends ArrayAdapter<User> {
    public UserViewAdapter(@NonNull Context context, ArrayList<User> arrayList) {
        super(context, R.layout.account_item, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User user = getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.account_item, parent, false);
        }

        ImageView profile = convertView.findViewById(R.id.imageViewProfile);
        TextView accountName = convertView.findViewById(R.id.textViewAccountName);
        TextView accountId = convertView.findViewById(R.id.textViewAccountId);

        accountName.setText(user.fullName);
        accountId.setText(user.accountId);

        return convertView;
    }
}
