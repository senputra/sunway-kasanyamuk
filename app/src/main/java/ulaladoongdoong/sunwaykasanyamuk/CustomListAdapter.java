package ulaladoongdoong.sunwaykasanyamuk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by Heartless on 05/12/2017.
 */

public class CustomListAdapter extends ArrayAdapter<KasaNyamuk> {

    private List<KasaNyamuk> mKasaNyamuk;
    private final Activity context;

    public CustomListAdapter(List<KasaNyamuk> mKasanyamuk, Activity context) {

        super(context, R.layout.row_listview, mKasanyamuk);

        this.mKasaNyamuk = mKasanyamuk;
        this.context = context;

    }

    @NonNull
    @Override
    public View getView(final int position, View view, @NonNull ViewGroup parent) {

        KasaNyamuk mKS = mKasaNyamuk.get(0);

        if (position == 0) { //header
            LayoutInflater mInflater = context.getLayoutInflater();

            View rowView = mInflater.inflate(R.layout.row_headerlistview, null, true);

            TextView tvName = rowView.findViewById(R.id.tvName);
            TextView tvAddress = rowView.findViewById(R.id.tvAddress);
            TextView tvDate = rowView.findViewById(R.id.tvDate);

            tvName.setText(String.format("Pembeli : %s", mKS.getName()));
            tvAddress.setText(String.format("Alamat : %s", mKS.getAddress()));
            //this is to get the date from the key
            tvDate.setText(String.format(Locale.US, "%s", mKS.getDate("dd MMMM yyyy")));

            return rowView;

        }
        if (position == mKasaNyamuk.size() - 1) { //footer
            LayoutInflater mInflater = context.getLayoutInflater();

            View rowView = mInflater.inflate(R.layout.row_footerlistview, null, true);

            TextView tvHargaDasar = rowView.findViewById(R.id.tvPricePerMeter);
            TextView tvVariant = rowView.findViewById(R.id.tvColor);
            TextView tvTotalPrice = rowView.findViewById(R.id.tvTotalPrice);
            TextView tvSubTotal = rowView.findViewById(R.id.tvSubTotal);
            TextView tvScreenPortPrice = rowView.findViewById(R.id.tvScreenPortPrice);
            TextView tvScreenPortQuantity = rowView.findViewById(R.id.tvScreenPortQuantity);
            TextView tvTotalScreenPort = rowView.findViewById(R.id.tvTotalScreenPort);
            TextView tvTotalPerimeter = rowView.findViewById(R.id.tvTotalPerimeter);
            TextView tvPanjar = rowView.findViewById(R.id.tvPanjar);
            TextView tvSisa = rowView.findViewById(R.id.tvSisa);
            TextView tvDll = rowView.findViewById(R.id.tvDll);
            TextView tvDllPrice = rowView.findViewById(R.id.tvDllPrice);
            TextView tvTotalQuantity = rowView.findViewById(R.id.tvTotalQuantity);


            tvDll.setText(mKS.getDll());
            tvDllPrice.setText(String.format(Locale.US, "%.0f", mKS.getDllPrice()));
            float totalPerimeter = 0;
            //start at 1 to skip the header
            for (int i = 1; i < mKasaNyamuk.size() - 1; i++) {
                totalPerimeter += mKasaNyamuk.get(i).getPerimeter();
            }
            tvTotalPerimeter.setText(String.format("%.2f", totalPerimeter));
            tvScreenPortPrice.setText(String.format(Locale.US, "Screenport @Rp%.0f/pc", mKS.screenPortPrice));
            tvScreenPortQuantity.setText(String.format(Locale.US, "%.0f pc(s)", mKS.getScreenPortQuantity()));
            tvTotalScreenPort.setText(NumberFormat.getInstance(Locale.US).format(mKS.getTotalScreenPortPrice()));
            tvHargaDasar.setText(String.format(Locale.US, "Harga Dasar @Rp %.0f/m", mKS.pricePerMeter));
            tvVariant.setText(String.format(Locale.US, "Tipe : %s", (String) mKS.getVariance()));
            float totalPrice = 0;
            //start at 1 to skip the header
            for (int i = 1; i < mKasaNyamuk.size() - 1; i++) {
                totalPrice += mKasaNyamuk.get(i).getPrice(mKS.pricePerMeter);
            }
            totalPrice = (float) (Math.ceil(totalPrice / 1000) * 1000);
            tvSubTotal.setText(NumberFormat.getInstance(Locale.US).format(totalPrice));
            totalPrice += mKS.getTotalScreenPortPrice();
            totalPrice += mKS.getDllPrice();
            tvTotalPrice.setText(NumberFormat.getInstance(Locale.US).format(Math.ceil(totalPrice)));
            tvPanjar.setText(String.format("- %s", NumberFormat.getInstance(Locale.US).format(mKS.getPanjar())));
            tvSisa.setText(NumberFormat.getInstance(Locale.US).format(totalPrice - mKS.getPanjar()));
            float totalQuantity = 0;
            //start at 1 to skip the header
            for (int i = 1; i < mKasaNyamuk.size() - 1; i++) {
                totalQuantity += mKasaNyamuk.get(i).getQuantity();
            }
            tvTotalQuantity.setText(String.format("%.0f", totalQuantity));


            return rowView;
        }
        //Log.d("length KS", "" + mKasaNyamuk.size());
        LayoutInflater mInflater = context.getLayoutInflater();

        View rowView = mInflater.inflate(R.layout.row_listview, null, true);

        //this code gets references to objects in the row_listview.xml file
        TextView tvDimension = rowView.findViewById(R.id.tvDimesion);
        TextView tvPerimeter = rowView.findViewById(R.id.tvPerimeter);
        TextView tvLocation = rowView.findViewById(R.id.tvLocation);
        TextView tvTotalPrice = rowView.findViewById(R.id.tvTotalPrice);
        TextView tvQuantity = rowView.findViewById(R.id.tvQuantity);

        KasaNyamuk currentKS = mKasaNyamuk.get(position);
        //this code sets the values of the objects to values from the arrays
        tvLocation.setText(String.format(Locale.US, "%s", currentKS.getLocation()));
        tvDimension.setText(String.format(Locale.US, "%.1f X %.1f", currentKS.getLength() * 100f, currentKS.getWidth() * 100f, currentKS.getQuantity()));
        tvTotalPrice.setText(NumberFormat.getInstance(Locale.US).format(Math.ceil(currentKS.getPrice(mKS.pricePerMeter))));
        Log.d("LKJ", "PRICE PER METER " + mKS.pricePerMeter);
        if (currentKS.getAdditionalPerimeter() == 0) {
            tvPerimeter.setText(String.format(Locale.US, "%.2f", (currentKS.getPerimeterWithoutAdditionalPerimeter())));
        } else {
            tvPerimeter.setText(String.format(Locale.US, "%.2f + %.2f", currentKS.getPerimeterWithoutAdditionalPerimeter(), currentKS.getAdditionalPerimeter()*currentKS.getQuantity()));
        }
        tvQuantity.setText(String.format("%.0f", currentKS.getQuantity()));
        tvLocation.setOnLongClickListener(getOnLongClickListener(tvLocation, currentKS));
        tvPerimeter.setOnLongClickListener(getOnLongClickListener(tvPerimeter, currentKS));
        return rowView;
    }

    private View.OnLongClickListener getOnLongClickListener(final TextView textView, final KasaNyamuk mKS) {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                View dialogView = context.getLayoutInflater().inflate(R.layout.dialog_universal, null);

                TextView tvDesc = dialogView.findViewById(R.id.tvDesc);
                TextView tvUnit = dialogView.findViewById(R.id.tvUnit);
                final TextView etContent = dialogView.findViewById(R.id.etContent);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final AlertDialog dialog = builder
                        .setNegativeButton(context.getResources().getString(R.string.cancel), null)
                        .setPositiveButton(context.getResources().getString(R.string.ok), null)
                        .setView(dialogView)
                        .create();

                switch (textView.getId()) {
                    case R.id.tvLocation:
                        tvDesc.setText("Lokasi");
                        tvUnit.setText(" ");
                        etContent.setText(mKS.getLocation());
                        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialogInterface) {
                                Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                                button.setTextColor(context.getResources().getColor(R.color.dialogButton));
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (etContent.getText().toString().equals("")) {
                                            Snackbar.make(context.findViewById(R.id.parent), "Lokasi Tidak Diubah", Snackbar.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        } else {
                                            mKS.setLocation(etContent.getText().toString());
                                            Snackbar.make(context.findViewById(R.id.parent), "Lokasi diubah menjadi " + mKS.getLocation(), Snackbar.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }
                                });
                            }
                        });
                        break;

                    case R.id.tvPerimeter:
                        tvDesc.setText("Tambahan Perimeter");
                        tvUnit.setText("cm");
                        etContent.setText(String.format(Locale.US, "%.0f", mKS.getAdditionalPerimeter() * 100));
                        etContent.setInputType(InputType.TYPE_CLASS_NUMBER);
                        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialogInterface) {
                                Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                                button.setTextColor(context.getResources().getColor(R.color.dialogButton));
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (etContent.getText().toString().equals("")) {
                                            mKS.setAdditionalPerimeter(0f);
                                            Snackbar.make(context.findViewById(R.id.parent), "Tidak ada tambah keliling", Snackbar.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        } else {
                                            mKS.setAdditionalPerimeter(Float.parseFloat(etContent.getText().toString()) / 100);
                                            Snackbar.make(context.findViewById(R.id.parent), "Tambah keliling " + mKS.getAdditionalPerimeter() + " cm", Snackbar.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }
                                });
                            }
                        });
                        break;
                }

                dialog.show();

                return true;
            }
        };

    }


    void popDialogue(final Activity context) {
        //when addItem button is pressed a dialog will pop
        // this dialog requests for data from user.

        LayoutInflater mInflater = context.getLayoutInflater();

        final View dialogView = mInflater.inflate(R.layout.dialog_additem, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.setView(dialogView)
                .setPositiveButton("OK", null
                )
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                })
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btnPositive.setTextColor(context.getResources().getColor(R.color.dialogButton));
                btnPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        float length, width, quantity, additionalPerimeter;
                        String location;
                        try {
                            EditText et = dialogView.findViewById(R.id.etPanjang);
                            length = (Float.parseFloat(et.getText().toString()));

                            et = dialogView.findViewById(R.id.etLebar);
                            width = (Float.parseFloat(et.getText().toString()));

                            et = dialogView.findViewById(R.id.etQuantity);
                            quantity = (Float.parseFloat(et.getText().toString()));

                            et = dialogView.findViewById(R.id.etTempat);
                            location = (et.getText().toString());

                            et = dialogView.findViewById(R.id.etTambahKeliling);
                            String temp = et.getText().toString();
                            if (temp.equals("")) {
                                additionalPerimeter = 0;
                            } else {
                                additionalPerimeter = Float.parseFloat(temp);
                            }

                            addItem(length, width, quantity, location, additionalPerimeter);
                        } catch (Exception e) {
                            Log.e("eror customlist adapter", Arrays.toString(e.getStackTrace()));
                            Toast.makeText(context, "Ada yang kosong", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        dialog.dismiss();
                        return;
                    }
                });
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    private void addItem(float length, float width, float quantity, String location, float additionalPerimeter) {

        KasaNyamuk mKS = new KasaNyamuk(location, length / 100f, width / 100f, quantity, additionalPerimeter);

        Log.d("length KS", "" + mKasaNyamuk.size());

        mKasaNyamuk.set(mKasaNyamuk.size() - 1, mKS);
        mKasaNyamuk.add(new KasaNyamuk());
        notifyDataSetChanged();

        new OrderanIO(context).saveOrderan(mKasaNyamuk, mKasaNyamuk.get(0).getKey(), false);

        Log.d("done", "done" + additionalPerimeter);
        return;
    }

    private void deleteItemDialogue(Context context, final int position) {
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle("Delete listing");
        dialog.setMessage("Hapus Item??");
        //---- Button made without the builder style Because I am not using customized dialogue
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Delete",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mKasaNyamuk.remove(position);
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                });


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mKasaNyamuk.remove(position);
                dialog.dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    void itemDialogue(final Activity context, final int position, final View view) {
        final AlertDialog dialog = new AlertDialog.Builder(context).create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle("Choose an action");
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteItemDialogue(context, position);
                dialog.dismiss();
            }
        });

        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editItemDialogue(context, position);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void editItemDialogue(final Activity context, final int position) {


        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // 2. Chain together various setter methods to set the dialog characteristics
        LayoutInflater mInflater = context.getLayoutInflater();
        final View dialogView = mInflater.inflate(R.layout.dialog_additem, null);
        builder.setView(dialogView);

        //--------Get all the EditTexts in the dialogue---------------
        final EditText etLength = dialogView.findViewById(R.id.etPanjang);
        final EditText etWidth = dialogView.findViewById(R.id.etLebar);
        final EditText etQuantity = dialogView.findViewById(R.id.etQuantity);
        final EditText etLocation = dialogView.findViewById(R.id.etTempat);
        final EditText etAditionalPerimeter = dialogView.findViewById(R.id.etTambahKeliling);


        etLength.setText(String.format(Locale.US, "%.1f", mKasaNyamuk.get(position).getLength() * 100f));
        etWidth.setText(String.format(Locale.US, "%.1f", mKasaNyamuk.get(position).getWidth() * 100f));
        etQuantity.setText(String.format(Locale.US, "%.0f", mKasaNyamuk.get(position).getQuantity()));
        etLocation.setText(String.format(Locale.US, "%s", mKasaNyamuk.get(position).getLocation()));
        etAditionalPerimeter.setText(String.format(Locale.US, "%.0f", mKasaNyamuk.get(0).getAdditionalPerimeter() * 100f));

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mKasaNyamuk.get(position).setLength(Float.parseFloat(etLength.getText().toString()) / 100f);
                mKasaNyamuk.get(position).setWidth(Float.parseFloat(etWidth.getText().toString()) / 100f);
                mKasaNyamuk.get(position).setAdditionalPerimeter(Float.parseFloat(etAditionalPerimeter.getText().toString()) / 100f);
                mKasaNyamuk.get(position).setQuantity(Float.parseFloat(etQuantity.getText().toString()));
                mKasaNyamuk.get(position).setLocation(etLocation.getText().toString());

                new OrderanIO(context).saveOrderan(mKasaNyamuk, mKasaNyamuk.get(0).getKey(), false);

                return;
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });


        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        return;
    }
}
