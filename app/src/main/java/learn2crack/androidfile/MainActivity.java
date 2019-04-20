package learn2crack.androidfile;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

//TODO : Investigate android permission groups

public class MainActivity extends Activity {
    private static final int PERMISSIONS_WRITE_EXTERNAL_STORAGE_CODE = 1;
    private static final int PERMISSIONS_READ_EXTERNAL_STORAGE_CODE = 2;
    EditText fname, fcontent, fnameread;
    Button write, read;
    TextView filecon;
    private String DEBUG_TAG = "Android_File_Access";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fname = findViewById(R.id.fname);
        fnameread = findViewById(R.id.fnameread);
        fcontent = findViewById(R.id.ftext);
        write = findViewById(R.id.btnwrite);
        filecon = findViewById(R.id.filecon);
        read = findViewById(R.id.btnread);
        write.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                PERMISSIONS_WRITE_EXTERNAL_STORAGE_CODE);
                    }
                } else {
                    // Permission has already been granted
                    writeFile();
                }

//                if (ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    permissionWriteOK();
//                } else {
//                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSIONS_WRITE_EXTERNAL_STORAGE_CODE);
//                }
            }
        });
        read.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                PERMISSIONS_READ_EXTERNAL_STORAGE_CODE);
                    }
                } else {
                    // Permission has already been granted
                    readFile();
                }

//                if (ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    permissionReadOK();
//                } else {
//                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{READ_EXTERNAL_STORAGE}, PERMISSIONS_READ_EXTERNAL_STORAGE_CODE);
//                }
            }
        });
    }

    private void permissionReadOK() {
        Log.d(DEBUG_TAG, "Permission OK");
        readFile();
    }

    private void readFile() {
        String readfilename = fnameread.getText().toString();
        FileOperations fop = new FileOperations();
        String text = fop.read(readfilename);
        if (text != null) {
            filecon.setText(text);
        } else {
            Toast.makeText(getApplicationContext(), "File not Found", Toast.LENGTH_SHORT).show();
            filecon.setText(null);
        }
    }

    private void writeFile() {
        String filename = fname.getText().toString();
        String filecontent = fcontent.getText().toString();
        FileOperations fop = new FileOperations();
        fop.write(filename, filecontent);
        if (fop.write(filename, filecontent)) {
            Toast.makeText(getApplicationContext(), filename + ".txt created", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "I/O error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_READ_EXTERNAL_STORAGE_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    readFile();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d(DEBUG_TAG, "Error : No Read permission");
                }
                break;
            }
            case PERMISSIONS_WRITE_EXTERNAL_STORAGE_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    writeFile();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d(DEBUG_TAG, "Error : No Write permission");
                }
                break;
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }

//        switch (requestCode) {
//            case PERMISSIONS_READ_EXTERNAL_STORAGE_CODE:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    permissionReadOK();
//                } else {
//
//                    Log.d(DEBUG_TAG, "Permission Not OK");
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
//                            showMessageOKCancel("You need to allow read access to external storage",
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            requestPermissions(new String[]{READ_EXTERNAL_STORAGE},
//                                                    PERMISSIONS_READ_EXTERNAL_STORAGE_CODE);
//                                        }
//                                    });
//                        }
//                    }
//                }
//                break;
//
//            case PERMISSIONS_WRITE_EXTERNAL_STORAGE_CODE:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    permissionWriteOK();
//                } else {
//
//                    Log.d(DEBUG_TAG, "Permission Not OK");
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
//                            showMessageOKCancel("You need to allow write access to external storage",
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE},
//                                                    PERMISSIONS_WRITE_EXTERNAL_STORAGE_CODE);
//                                        }
//                                    });
//                        }
//                    }
//                }
//                break;
//        }
    }

    private void permissionWriteOK() {
        Log.d(DEBUG_TAG, "Permission OK");
        writeFile();
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}