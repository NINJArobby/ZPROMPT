package gh.com.zenith.ZPROMPT;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class Start extends Activity
{
    SQLiteDatabase MyDb;
    String TableName = "NotificationLogsDB";
    Intent _signup;
    Button _enter;
    Boolean PassWordOption;
    Intent records;
    public static Activity _start;
    Bundle extras;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = Start.class.getName();
    private static final String FILENAME = "zLogs.txt";


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(isOnline())
        {
            //checkPlayServices();
                MyDb = this.openOrCreateDatabase("ZapNotification", MODE_PRIVATE, null);
                ActionBar actionBar = getActionBar();
                actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.zenithred));
                actionBar.setHomeButtonEnabled(true);
                actionBar.setTitle("Welcome...");
                actionBar.setLogo(R.drawable.logo3);

                records = new Intent(this,Signup.class);
                CreateTables();
                checkPassword();
                if(SignedUp())
                {
                    if(PassWordOption)
                    {
                        ShowPass();
                    }
                    else
                    {
                        startActivity(records);
                    }
                }
                else
                {
                    setContentView(R.layout.main);

                    _signup = new Intent(this,SelectSignup.class);
                    _enter = (Button)findViewById(R.id.btnEnter2);
                    _start = this;
                    _enter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            startActivity(_signup);
                        }
                    });
                }
        }

        else
        {
            new AlertDialog.Builder(this)
                    .setTitle("OOPS!!!")
                    .setMessage("No Internet Connectivity. You need connectivity to run this application. " +
                            "Please check internet and try again.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Start.this.finish();
                        }
                    })
                    .setIcon(R.drawable.alert)
                    .show();
        }



    }

    void checkPassword()
    {
        String name = "";
        try {
            Cursor cursor = MyDb.rawQuery("select DROIDPASS from CUSTOMER", null);
            int count = cursor.getCount();
            cursor.moveToFirst();
            //name = cursor.getString(0).trim();
            if (count < 1)
            {
                PassWordOption = false;
                cursor.close();
            }
            else
            {
                PassWordOption=true;
                cursor.close();
            }

        }
        catch (Exception ex)
        {
            ex.toString();
        }


    }

    void CreateTables()
    {
        //create table
        MyDb.execSQL("CREATE TABLE IF NOT EXISTS "
                + TableName +
                " (ID integer primary key autoincrement not null," +
                "TRAN_DATE integer, " +
                "ACCOUNT VARCHAR, " +
                "AMOUNT  VARCHAR," +
                "TYPE VARCHAR, " +
                "DESCRIPTION VARCHAR, " +
                "CURRENCY VARCHAR(3), " +
                "BRANCH VARCHAR );");

        MyDb.execSQL("CREATE TABLE IF NOT EXISTS CUSTOMER" +
            "(ACCESSCODE VARCHAR,USERNAME VARCHAR,PASSWORD VARCHAR, DROIDPASS VARCHAR,SIGNEDUP VARCHAR);");

        MyDb.execSQL("CREATE TABLE IF NOT EXISTS ACCOUNTS" +
                "(ACCT_NO VARCHAR);");
    }

    void ShowPass()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Password");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);
        builder.setIcon(R.drawable.icon);
        // Set up the buttons
        builder.setPositiveButton("Log In", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                try
                {
                    Login(input.getText().toString().trim());
                    startActivity(records);
                }
                catch (Exception ex)
                {
                    ex.getMessage();
                }
            }
        });
        builder.show();
    }

    boolean Login(String pass)
    {
        boolean IsMember = false;
        String name = "";
        try {
            Cursor cursor = MyDb.rawQuery("select DROIDPASS from CUSTOMER", null);
            int count = cursor.getCount();
            cursor.moveToFirst();
            name = cursor.getString(0).trim();
            if (name.equals(pass))
            {
               IsMember = true;
                cursor.close();
            }
        }
        catch (Exception ex)
        {
            ex.toString();
        }

            return  IsMember;
    }

    boolean SignedUp()
    {
        boolean IsMember = false;
        String name = "";
        try {
            Cursor cursor = MyDb.rawQuery("select SIGNEDUP from CUSTOMER", null);
            int count = cursor.getCount();
            cursor.moveToFirst();
            name = cursor.getString(0).trim();
            if (name.equals("YES"))
            {
                IsMember = true;
                cursor.close();
            }
        }
        catch (Exception ex)
        {
            ex.toString();
        }

        return  IsMember;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }



}



