package ulaladoongdoong.sunwaykasanyamuk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Heartless on 08/12/2017.
 */


public class OrderanIO {
    private SharedPreferences mSharedPreferences;
    private Context mContext;

    public OrderanIO(Context mContext) {
        this.mContext = mContext;
    }

    void saveOrderan(List<KasaNyamuk> mKS, String key, boolean notify) {
        mSharedPreferences = mContext.getSharedPreferences(mContext.getPackageName() + "." + mContext.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        String mJSON = new Gson().toJson(mKS);
        mEditor.putString(key, mJSON);
        mEditor.apply();

        //-----Pop toast up to notify the user.------------
        if (notify) {
            Toast.makeText(mContext, "Orderan telah disimpan", Toast.LENGTH_SHORT).show();
        }

        return;
    }

    List<KasaNyamuk> loadOrderan(String key, boolean notify) {
        mSharedPreferences = mContext.getSharedPreferences(mContext.getPackageName() + "." + mContext.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String getPreferences = mSharedPreferences.getString(key, null);

        Type type = new TypeToken<List<KasaNyamuk>>() {
        }.getType();

        List<KasaNyamuk> mKS = new Gson().fromJson(getPreferences, type);
        Log.d("orderanIO", key + "");
        //----------------------------Pop toast up to notify the user.-----------------------
        if (notify) {
            Toast.makeText(mContext, "Orderan telah diloaded", Toast.LENGTH_SHORT).show();
        }
        //-----------------------------------------------------------------------------------
        return mKS;
    }

    void deleteOrderan(String key) {
        mSharedPreferences = mContext.getSharedPreferences(mContext.getPackageName() + "." + mContext.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.remove(key).apply();

        //----------------------------Pop toast up to notify the user.-----------------------
        Toast.makeText(mContext, "save file has been deleted", Toast.LENGTH_SHORT).show();
        //-----------------------------------------------------------------------------------
        return;
    }

    // This function is to retrieve all saved preferences in shared preferences
    ArrayList<String[]> loadSemuaOrderan() {
        mSharedPreferences = mContext.getSharedPreferences(mContext.getPackageName() + "." + mContext.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        ArrayList<String[]> loadFile = new ArrayList<String[]>();

        Map<String, ?> allEntries = mSharedPreferences.getAll();
        Type type = new TypeToken<ArrayList<KasaNyamuk>>() {
        }.getType();
        //converting the Map object to List<String[3]> 0:key, 1:name 2: address
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            //Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            //entry.getValue() is to get the saved object which is in JSON file in this case
            ArrayList<KasaNyamuk> tempMKS = new Gson().fromJson(entry.getValue().toString(), type);
            KasaNyamuk mKS = tempMKS.get(0);
            String[] temp = {entry.getKey(), mKS.getName(), mKS.getAddress()};
            loadFile.add(temp);
        }
        //----------------------------Pop toast up to notify the user.-----------------------
        Toast.makeText(mContext, String.format(Locale.US, "%d entry telah di load", loadFile.size()), Toast.LENGTH_SHORT).show();
        //-----------------------------------------------------------------------------------

//        Log.d("user", loadFile.get(0)[1]);
        return loadFile;
    }

    String TAG = "ORDERAN IO";

    void backupEverything() {
        mSharedPreferences = mContext.getSharedPreferences(mContext.getPackageName() + "." + mContext.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        Map<String, ?> allEntries = mSharedPreferences.getAll();
        //converting the Map object to List<String[3]> 0:key, 1:name 2: address


        File appPath = Environment.getExternalStoragePublicDirectory("Sunway Kasanyamuk");
        if (appPath.mkdirs()) {
            Log.d(TAG, "folder was created");
        } else {
            if (appPath.isDirectory()) {
                String[] oldFiles = appPath.list();
                for (String oldfile : oldFiles) {
                    new File(appPath, oldfile).delete();
                }
            }
        }

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            //Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            //entry.getValue() is to get the saved object which is in JSON file in this

            try {
                FileOutputStream stream = new FileOutputStream(new File(appPath, entry.getKey()));
                stream.write(entry.getValue().toString().getBytes());
            } catch (Exception e) {
                Log.d(TAG, e.getMessage() + "line 120");
            }

        }
        //----------------------------Pop toast up to notify the user.-----------------------
        Toast.makeText(mContext, "Backup telah dibuat", Toast.LENGTH_SHORT).show();
        //-----------------------------------------------------------------------------------

//        Log.d("user", loadFile.get(0)[1]);
        return;
    }

    void loadAllBackup() {
        File appPath = Environment.getExternalStoragePublicDirectory("Sunway Kasanyamuk");
        if (appPath.exists()) {
            Log.d(TAG, appPath.listFiles().length + "");
            for (File file : appPath.listFiles()) {
                //read file to string
                String contents = "";
                try {
                    contents = Files.toString(file, StandardCharsets.UTF_8);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mSharedPreferences = mContext.getSharedPreferences(mContext.getPackageName() + "." + mContext.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                mEditor.putString(file.getName(), contents);
                mEditor.apply();

                //-----Pop toast up to notify the user.------------
            }
            Toast.makeText(mContext, "Backup telah di restore", Toast.LENGTH_SHORT).show();
        }
    }
}
