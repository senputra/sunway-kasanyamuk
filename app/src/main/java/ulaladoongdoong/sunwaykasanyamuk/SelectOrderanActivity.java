package ulaladoongdoong.sunwaykasanyamuk;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class SelectOrderanActivity extends AppCompatActivity {


    private CustomNameAndAddressAdapter mAdapter;
    private ArrayList<String[]> listKey;
    private OrderanIO mOrderanIO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_orderan);

        mOrderanIO = new OrderanIO(this);
        listKey = mOrderanIO.loadSemuaOrderan();


        //-----------------------Setting up the ListView---------------------
        this.mAdapter = new CustomNameAndAddressAdapter(this, listKey);
        final ListView mLV = findViewById(R.id.listView);
        mLV.setAdapter(mAdapter);
        mLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String key = listKey.get(i)[0];
                Intent mIntent = new Intent(SelectOrderanActivity.this, MainActivity.class);
                mIntent.putExtra("userID", key);
                startActivity(mIntent);
//                Log.d("SelectOrderanActivity", "clicked item position " + i + " " + l);
//                Log.d("SelectOrderanActivity", "Listkey in the same position " + key);
                return;
            }
        });

        mLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int itemPosition, long l) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(SelectOrderanActivity.this);
                final AlertDialog dialog = mBuilder.setTitle("Delete Orderan")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AlertDialog.Builder mBuilder = new AlertDialog.Builder(SelectOrderanActivity.this);
                                final AlertDialog dialog1 = mBuilder.setTitle("Yakin?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                mOrderanIO.deleteOrderan(listKey.get(itemPosition)[0]);
                                                listKey.remove(itemPosition);
                                                mAdapter.notifyDataSetChanged();
                                                return;
                                            }

                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                return;
                                            }
                                        })
                                        .create();
                                dialog1.show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        })
                        .create();
                dialog.show();
                return true;
            }
        });
        //-------------------------------------------------------------------
        //-------------------------------------------------------------------

        //---------------Setting up "Tambah Orderan" button------------------
        Button btnAddBuyer = findViewById(R.id.btnAddBuyer);
        btnAddBuyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.addUser(SelectOrderanActivity.this);
            }
        });
        //-------------------------------------------------------------------
        //-------------------------------------------------------------------

        final String TAG = "ulalaaaa";
        //---------------Setting up "Backup" button------------------
        Button btnBackup = findViewById(R.id.btnBackup);
        btnBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //the path will be /data/user/0/ulaladoongdoong.sunwaykasanyamuk/files
//                String appPath = "/Sunway Kasanyamuk";
                AlertDialog dialog = new AlertDialog.Builder(SelectOrderanActivity.this)
                        .setPositiveButton("Backup", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                doBackup();


                            }

                        })
                        .setNeutralButton("CANCEL", null)
                        .setNegativeButton("Restore", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                restoreBackup();
                            }
                        }).create();

                dialog.setTitle("Choose an action");
                dialog.setMessage("Mau Backup atau Restore data?");
                dialog.show();
            }
        });
    }
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------


    void restoreBackup() {
        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                //Check permission
                PermissionListener mPL = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        mOrderanIO.loadAllBackup();
                        listKey.clear();
                        listKey.addAll(mOrderanIO.loadSemuaOrderan());
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(SelectOrderanActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Toast.makeText(SelectOrderanActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                };

                TedPermission.with(SelectOrderanActivity.this)
                        .setPermissionListener(mPL)
                        .setDeniedMessage("If you reject permission,you can not backup your data \n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();
            }
        });
        mThread.run();
    }

    void doBackup() {
        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                //Check permission
                PermissionListener mPL = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        mOrderanIO.backupEverything();
                        Toast.makeText(SelectOrderanActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Toast.makeText(SelectOrderanActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                };

                TedPermission.with(SelectOrderanActivity.this)
                        .setPermissionListener(mPL)
                        .setDeniedMessage("If you reject permission,you can not backup your data \n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();
            }
        });
        mThread.run();
    }

}


