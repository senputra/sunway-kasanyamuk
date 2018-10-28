package ulaladoongdoong.sunwaykasanyamuk;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Heartless on 12/12/2017.
 */

public class CustomNameAndAddressAdapter extends ArrayAdapter {

    private Activity context;
    private ArrayList<String[]> listKey;

    public CustomNameAndAddressAdapter(@NonNull Activity context, ArrayList<String[]> listKey) {
        super(context, R.layout.row_nameandaddress, listKey);
        this.listKey = listKey;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        View rowView = context.getLayoutInflater().inflate(R.layout.row_nameandaddress, null, true);

        TextView tvName = rowView.findViewById(R.id.tvName);
        TextView tvAddress = rowView.findViewById(R.id.tvAddress);
        TextView tvDate = rowView.findViewById(R.id.tvDate);
        TextView tvID = rowView.findViewById(R.id.tvID);


        String[] temp = listKey.get(position);

        try {
            tvDate.setText(String.format(Locale.US, "%s", new SimpleDateFormat("dd MMMM yyyy",Locale.getDefault()).format(Long.parseLong(temp[0].substring(1)))));
        } catch (Exception e) {
            Log.e("CNAAadapter", "NO date set");
            tvDate.setText("Not Available");
        }
        tvName.setText(temp[1]);
        tvAddress.setText(temp[2]);
        tvID.setText(String.format(Locale.US, "ID: %s", temp[0]));
        return rowView;
    }

    void addUser(final Activity context) {
        final ArrayList<KasaNyamuk> mKasaNyamuk = new ArrayList<KasaNyamuk>();

        //---------------------Add Name and Address Dialog------------------
        LayoutInflater mInflater = context.getLayoutInflater();

        final View dialogView = mInflater.inflate(R.layout.dialog_nameandaddress, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.setView(dialogView)
                .setNegativeButton("Tutup", null)
                .setPositiveButton("OK", null)
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button btnCancel = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                btnCancel.setTextColor(context.getResources().getColor(R.color.dialogButton));
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                Button btnOK = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btnOK.setTextColor(context.getResources().getColor(R.color.dialogButton));
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mKasaNyamuk.add(new KasaNyamuk());// header
                        KasaNyamuk mKS = mKasaNyamuk.get(0);

                        try {

                            EditText etNama = dialogView.findViewById(R.id.etName);
                            mKS.setName(etNama.getText().toString());

                            EditText etAddress = dialogView.findViewById(R.id.etAddress);
                            mKS.setAddress(etAddress.getText().toString());

                            EditText etVariance = dialogView.findViewById(R.id.etVariance);
                            mKS.setVariance(etVariance.getText().toString());

                            EditText etBasePrice = dialogView.findViewById(R.id.etBasePrice);
                            mKS.pricePerMeter = Float.parseFloat(etBasePrice.getText().toString());

                            EditText etScreenPort = dialogView.findViewById(R.id.etScreenPort);
                            mKS.screenPortPrice = Float.parseFloat(etScreenPort.getText().toString());

                            EditText etScreenPortQuantity = dialogView.findViewById(R.id.etScreenPortQuantity);
                            mKS.setScreenPortQuantity(Float.parseFloat(etScreenPortQuantity.getText().toString()));

                            EditText etDll = dialogView.findViewById(R.id.etDll);
                            mKS.setDll(etDll.getText().toString());

                            EditText etDllPrice = dialogView.findViewById(R.id.etDllPrice);
                            mKS.setDllPrice(Float.parseFloat(etDllPrice.getText().toString()));

                            mKS.setKey();
                        } catch (Exception e) {
                            Log.d("error", "some input is wrong");
                            Log.e("error", e.getMessage());
                            Toast.makeText(context, "Salah input data", Toast.LENGTH_SHORT).show();
                            mKasaNyamuk.clear();
                            return;
                        }
                        dialog.dismiss();
                        mKasaNyamuk.add(new KasaNyamuk()); // for footer
                        Log.d("Adapter", "Succeed in adding user");


                        //----------------Saving the initial mKasaNyamuk----------------------
                        OrderanIO mOrderanIO = new OrderanIO(context);
                        mOrderanIO.saveOrderan(mKasaNyamuk, mKS.getKey(),false);
                        //--------------------------------------------------------------------

                        String[] temp = {mKS.getKey(), mKS.getName(), mKS.getAddress()};
                        listKey.add(temp);

                        notifyDataSetChanged();
                        return;
                    }
                });
            }
        });
        dialog.show();
        //--------------------------------------------------------------------


        return;
    }
}
