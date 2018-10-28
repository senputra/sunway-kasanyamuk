package ulaladoongdoong.sunwaykasanyamuk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.print.PdfPrint;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    List<KasaNyamuk> mKasaNyamuk;
    OrderanIO mOrderan = new OrderanIO(this);
    CustomListAdapter mAdapter;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent mIntent = getIntent();
        this.key = mIntent.getStringExtra("userID");
        setmKasaNyamuk(key);

        //Setting up the floating action buttons
        FloatingActionButton fabExport = findViewById(R.id.fabExportPdf);
        FloatingActionButton fabPrint = findViewById(R.id.fabPrint);
        FloatingActionButton fabDelete = findViewById(R.id.fabDelete);
        FloatingActionButton fabSave = findViewById(R.id.fabSave);

        //-------Delete FAB--------------------------------------
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
                dialog.setTitle("Delete data orderan?");
                dialog.setCanceledOnTouchOutside(false);
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mOrderan.deleteOrderan(key);
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                dialog.show();

            }
        });

        //------------Print fab--------------------------------
        fabPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doWebPrint();

            }
        });

        //---------------------Export to pdf fab----------------
        fabExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportToPdf();

            }
        });

        //-------------------------Save fab--------------------
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
                dialog.setTitle("Save orderan?");
                dialog.setCanceledOnTouchOutside(false);
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mOrderan.saveOrderan(mKasaNyamuk, key, true);
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                dialog.show();

            }
        });

        //--------------Setting up the ListVIew-----------------------------
        this.mAdapter = new CustomListAdapter(mKasaNyamuk, this);
        final ListView mLV = findViewById(R.id.listView);
        mLV.setAdapter(mAdapter);
        mLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                //the 'if' function prevents the header and footer from being deleted
                if (position == 0) {
                    editHeaderDialog();
                    return false;
                } else if ((position == mKasaNyamuk.size() - 1)) {
                    editFooterDialog();
                    return false;
                }

//                Log.v("long clicked", "pos: " + position + " " + l);
                mAdapter.itemDialogue(MainActivity.this, position, view);
                return false;
            }
        });
        //-----------------------------------------------------------------

        //------------Setting up 'add item' buton--------------------------
        Button btnAddItem = (Button) findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.popDialogue(MainActivity.this);
            }
        });

        //-----------------------------------------------------------------


        //---------------Setting up 'Panjar Button'-------------------------
        Button btnPanjar = findViewById((R.id.btnPanjar));
        btnPanjar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                @SuppressLint("InflateParams") final View dialogView = MainActivity.this.getLayoutInflater().inflate(R.layout.dialog_panjar, null);
                final EditText et = dialogView.findViewById(R.id.etPanjar);
                et.setText(String.format(Locale.US, "%.0f", mKasaNyamuk.get(0).getPanjar()));

                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                final AlertDialog dialog = builder.setView(dialogView)
                        .setPositiveButton("OK", null)
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        button.setTextColor(MainActivity.this.getResources().getColor(R.color.dialogButton));
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (et.getText().toString().equals("")) {
                                    mKasaNyamuk.get(0).setPanjar(0f);
                                    Snackbar.make(MainActivity.this.findViewById(R.id.parent), "Panjar belum dibayar", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    mKasaNyamuk.get(0).setPanjar(Float.parseFloat(et.getText().toString()));
                                    mOrderan.saveOrderan(mKasaNyamuk, key, false);
                                    Snackbar.make(MainActivity.this.findViewById(R.id.parent), "Panjar sebesar Rp" + Integer.parseInt(mKasaNyamuk.get(0).getPanjar() + "") + " telah dibayar", Snackbar.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                });
                dialog.show();
            }
        });
    }

    //-----------------------------------------------------------------
    //-----------------------------------------------------------------
    private void editHeaderDialog() {
        final View dialogView = this.getLayoutInflater().inflate(R.layout.dialog_buyersdetail, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.setView(dialogView)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", null)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                final EditText etName = dialogView.findViewById(R.id.etName);
                final EditText etAddress = dialogView.findViewById(R.id.etAddress);

                etName.setText(mKasaNyamuk.get(0).getName());
                etAddress.setText(mKasaNyamuk.get(0).getAddress());

                Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(etName.getText().toString().equals("") || etAddress.getText().toString().equals("")){
                            Toast.makeText(MainActivity.this, "Isi nama dan alamat yang benar", Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            mKasaNyamuk.get(0).setName(etName.getText().toString());
                            mKasaNyamuk.get(0).setAddress(etAddress.getText().toString());
                            Snackbar.make(MainActivity.this.findViewById(R.id.parent),"nama dan alamat telah diganti",Snackbar.LENGTH_SHORT).show();
                            dialog.dismiss();
                            return;
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    public void exportToPdf() {

        // load the HTML String to webview
        final WebView webView = new WebView(this);
        String htmlDocument = printToHTML(mKasaNyamuk);

        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //the file path
                final File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + "/Sunway/" + mKasaNyamuk.get(0).getDate("MMMM yy"));
                final String jobName = "Document";
                final PrintAttributes attributes = new PrintAttributes.Builder()
                        .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                        .setResolution(new PrintAttributes.Resolution("pdf", "pdf", 600, 600))
                        .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                        .build();

                String filename = mKasaNyamuk.get(0).getName() + " " + mKasaNyamuk.get(0).getAddress();
                View dialogView = MainActivity.this.getLayoutInflater().inflate(R.layout.dialog_exporttopdf, null);
                final EditText etFilename = dialogView.findViewById(R.id.etFileName);
                assert etFilename != null;
                etFilename.setText(filename);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final AlertDialog dialog = builder.setView(dialogView)
                        .setPositiveButton("EXPORT", null)
                        .setNegativeButton("CANCEL", null)
                        .create();

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button btnOK = (Button) dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        btnOK.setTextColor(MainActivity.this.getResources().getColor(R.color.dialogButton));
                        btnOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!etFilename.getText().toString().equals("")) {
                                    //Intercepting the printing process of the webview.
                                    PdfPrint pdfPrint = new PdfPrint(attributes);
                                    pdfPrint.print(webView.createPrintDocumentAdapter(jobName), path, etFilename.getText().toString() + ".pdf");

                                    Toast.makeText(MainActivity.this, etFilename.getText().toString() + " has been exported.", Toast.LENGTH_SHORT).show();

                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(MainActivity.this, "Name file tidak boleh kosong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

                dialog.show();
            }
        });

        webView.loadData(htmlDocument, "text/HTML", "UTF-8");
//        webView.loadUrl("http://www.google.com");
    }


    @NonNull
    private String printToHTML(List<KasaNyamuk> mKS) {
        KasaNyamuk mKS0 = mKS.get(0);
        StringBuilder html = new StringBuilder();


        //--------Header part------------------------------------------------------
        html.append(String.format(Locale.US, getResources().getString(R.string.header),
                String.format(Locale.US, "%s", mKS0.getDate("dd MMMM yyyy")),
                mKS0.getName(),
                mKS0.getAddress()));

        Log.d("mainActivity", "size of MKS" + mKS.size());


        //-----------Content------------------------------------------------------
        for (int i = 1; i < mKS.size() - 1; i++) {
            KasaNyamuk temp = mKS.get(i);

            if (temp.getAdditionalPerimeter() == 0) {
                html.append(String.format(Locale.US, getResources().getString(R.string.contentNoAdditionalPerimeter),
                        temp.getLocation(),
                        temp.getLength() * 100f,
                        temp.getWidth() * 100f,
                        temp.getQuantity(),
                        temp.getPerimeterWithoutAdditionalPerimeter(),
                        NumberFormat.getInstance(Locale.US).format((Math.ceil(temp.getPrice(mKS0.pricePerMeter))))));
            } else {
                html.append(String.format(Locale.US, getResources().getString(R.string.content),
                        temp.getLocation(),
                        temp.getLength() * 100f,
                        temp.getWidth() * 100f,
                        temp.getQuantity(),
                        temp.getPerimeterWithoutAdditionalPerimeter(),
                        temp.getAdditionalPerimeter()*temp.getQuantity(),
                        NumberFormat.getInstance(Locale.US).format((Math.ceil(temp.getPrice(mKS0.pricePerMeter))))));
            }
        }


        //-------------Footer------------------------------------------------------
        float totalPerimeter = 0;
        //start at 1 to skip the header
        for (int i = 1; i < mKasaNyamuk.size() - 1; i++) {
            totalPerimeter += mKasaNyamuk.get(i).getPerimeter();
        }

        float totalQuantity = 0;
        //start at 1 to skip the header
        for (int i = 1; i < mKasaNyamuk.size() - 1; i++) {
            totalQuantity += mKasaNyamuk.get(i).getQuantity();
        }

        float totalPrice = 0;
        //start at 1 to skip the header
        for (int i = 1; i < mKasaNyamuk.size() - 1; i++) {
            totalPrice += mKasaNyamuk.get(i).getPrice(mKS0.pricePerMeter);
        }
        totalPrice = (float) (Math.ceil(totalPrice / 100) * 100);
        html.append(String.format(Locale.US, getResources().getString(R.string.footer),
                totalQuantity,
                totalPerimeter,
                NumberFormat.getInstance(Locale.US).format(Math.ceil(totalPrice / 100) * 100),
                mKS0.getDll(),
                NumberFormat.getInstance(Locale.US).format(mKS0.getDllPrice()),
                mKS0.screenPortPrice,
                mKS0.getScreenPortQuantity(),
                NumberFormat.getInstance(Locale.US).format(mKS0.getTotalScreenPortPrice()),
                NumberFormat.getInstance(Locale.US).format(Math.ceil(totalPrice + mKS0.getDllPrice()+ (mKS0.getTotalScreenPortPrice()))),
                NumberFormat.getInstance(Locale.US).format(Math.ceil(mKS0.getPanjar())),
                NumberFormat.getInstance(Locale.US).format(Math.ceil(totalPrice + mKS0.getTotalScreenPortPrice() + mKS0.getDllPrice() - mKS0.getPanjar())),
                mKS0.pricePerMeter,
                mKS0.getVariance()));

        return html.toString();
    }

    void doWebPrint() {
        // Create a WebView object specifically for printing
        WebView webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // Get a PrintManager instance
                PrintManager printManager = (PrintManager) MainActivity.this.getSystemService(Context.PRINT_SERVICE);
                // Get a print adapter instance
                PrintDocumentAdapter printAdapter = view.createPrintDocumentAdapter("print penawaran harga");
                // Create a print job with name and adapter instance
                String jobName = getString(R.string.app_name) + " Document";
                PrintJob printJob = printManager.print(jobName, printAdapter,
                        new PrintAttributes.Builder().build());
            }
        });

        // Generate an HTML document on the fly:
        String htmlDocument = printToHTML(mKasaNyamuk);
        webView.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null);

        // Keep a reference to WebView object until you pass the PrintDocumentAdapter
        // to the PrintManager
//        mWebView = webView;
    }

    private void setmKasaNyamuk(String key) {


        mOrderan = new OrderanIO(this);

        try {
            this.mKasaNyamuk = mOrderan.loadOrderan(key, false);

            Log.d("asd", "berhasil" + (this.mKasaNyamuk == null) + key);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (mKasaNyamuk == null) {
            mKasaNyamuk = new ArrayList<>();
            mKasaNyamuk.add(new KasaNyamuk()); // for header

            //---------------------Add Name and Address Dialog------------------
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater mInflater = this.getLayoutInflater();
            final View dialogView = mInflater.inflate(R.layout.dialog_nameandaddress, null);
            builder.setView(dialogView);

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mKasaNyamuk = null;
                    return;
                }
            }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    EditText etNama = dialogView.findViewById(R.id.etName);
                    mKasaNyamuk.get(0).setName(etNama.getText().toString());

                    EditText etAddress = dialogView.findViewById(R.id.etAddress);
                    mKasaNyamuk.get(0).setAddress(etAddress.getText().toString());

                    EditText etColor = dialogView.findViewById(R.id.etVariance);
                    mKasaNyamuk.get(0).setVariance(etColor.getText().toString());

                    EditText etBasePrice = dialogView.findViewById(R.id.etBasePrice);
                    mKasaNyamuk.get(0).pricePerMeter = Float.parseFloat(etBasePrice.getText().toString());

                    return;
                }
            });

            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            //--------------------------------------------------------------------

            mKasaNyamuk.get(0).setKey();

            mKasaNyamuk.add(new KasaNyamuk()); // for footer
            Log.d("error", "it is empty");
        } else {
            this.mAdapter = new CustomListAdapter(mKasaNyamuk, this);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void editFooterDialog() {

        final KasaNyamuk mKS = mKasaNyamuk.get(0);
        //--------------------------------------------------------------------
        //---------------------Add Name and Address Dialog------------------
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final View dialogView = this.getLayoutInflater().inflate(R.layout.dialog_nameandaddress, null);

        builder.setView(dialogView);
        builder.setTitle("Edit");

        //--------Showing the previous name in the EditText--------
        EditText etNama = dialogView.findViewById(R.id.etName);
        etNama.setText(mKS.getName());

        EditText etAddress = dialogView.findViewById(R.id.etAddress);
        etAddress.setText(mKS.getAddress());

        EditText etColor = dialogView.findViewById(R.id.etVariance);
        etColor.setText(mKS.getVariance());

        EditText etBasePrice = dialogView.findViewById(R.id.etBasePrice);
        etBasePrice.setText(String.format(Locale.US, "%.0f", mKS.pricePerMeter));

        EditText etScreenPort = dialogView.findViewById(R.id.etScreenPort);
        etScreenPort.setText(String.format(Locale.US, "%.0f", mKS.screenPortPrice));

        EditText etScreenPortQuantity = dialogView.findViewById(R.id.etScreenPortQuantity);
        etScreenPortQuantity.setText(String.format(Locale.US, "%.0f", mKS.getScreenPortQuantity()));

        EditText etDll = dialogView.findViewById(R.id.etDll);
        etDll.setText(mKS.getDll());

        EditText etDllPrice = dialogView.findViewById(R.id.etDllPrice);
        etDllPrice.setText(String.format(Locale.US, "%.0f", mKS.getDllPrice()));
        //------------------------------------------------------

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText etNama = dialogView.findViewById(R.id.etName);
                mKS.setName(etNama.getText().toString());

                EditText etAddress = dialogView.findViewById(R.id.etAddress);
                mKS.setAddress(etAddress.getText().toString());

                EditText etColor = dialogView.findViewById(R.id.etVariance);
                mKS.setVariance(etColor.getText().toString());

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

                //----------------------------Pop toast up to notify the user.-----------------------
                Toast.makeText(MainActivity.this, "Editan telah disimpan", Toast.LENGTH_SHORT).show();
                //-----------------------------------------------------------------------------------

                mOrderan.saveOrderan(mKasaNyamuk, key, false);

                return;
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        //--------------------------------------------------------------------
        //--------------------------------------------------------------------

        mAdapter.notifyDataSetChanged();
        return;


    }
}
