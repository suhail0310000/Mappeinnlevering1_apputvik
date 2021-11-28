package com.example.mappe1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ResultDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstaceState) {
        String message = getResources().getString(R.string.resultatDesc);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.resultTitle)
                .setMessage(message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        return;
                    }
                });

        return builder.create();
    }

    @Override
    public void onDestroy() {
        getActivity().finish();
        super.onDestroy();
    }
}
