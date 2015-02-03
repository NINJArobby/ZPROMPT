package gh.com.zenith.ZPROMPT;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ParseException;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by Robby on 6/20/2014.
 */
public class ReceiveActivity extends Activity {

    TextView date;
    TextView account;
    TextView amount;
    TextView description;
    TextView typee;
    TextView type;
    TextView currency;
    TextView branch;
    ArrayList<String> Res;
    SQLiteDatabase MyDb;
    String TableName = "NotificationLogsDB";
    Intent _summary;
    String[] splitStr;
    String TableName3 = "PassWORD";
    View promptsView;
    Intent _records;
    public static Activity receive;
    //jeSQLiteDatabase MyDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received);
        receive = this;
        Intent intent = getIntent();
        _records = new Intent(this,Records.class);
        _records.putExtra("FLAG","REC");

        date = (TextView) findViewById(R.id.txtDate);
        account = (TextView) findViewById(R.id.txtAcctNo);
        amount = (TextView) findViewById(R.id.txtAmount);
        type = (TextView) findViewById(R.id.txttype);
        description = (TextView) findViewById(R.id.txtDescription);
        currency = (TextView) findViewById(R.id.txtCurrency);
        branch = (TextView) findViewById(R.id.txtBranch);

        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.zenithred));
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Welcome...");
        actionBar.setLogo(R.drawable.logo3);

        String message = intent.getExtras().getString("message");
        //save2DB(message);
        String delimit = ";";
        StringTokenizer ress = new StringTokenizer(message, delimit);
        splitStr = new String[ress.countTokens()];
        int index = 0;
        while (ress.hasMoreElements()) {
            splitStr[index++] = ress.nextToken();
        }

        date.setText(splitStr[0]);
        account.setText(splitStr[1]);
        amount.setText(splitStr[3]);
        type.setText(splitStr[4]);
        description.setText(splitStr[5]);
        currency.setText(splitStr[6]);
        branch.setText(splitStr[7]);

       // SEND_ALERT();
    }

    @Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage("Would you like to view your accounts history?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        startActivity(_records);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ReceiveActivity.this.finish();
                    }
                })

                .setIcon(R.drawable.alert)
                .show();
        //
        // ReceiveActivity.this.finish();
        //super.onBackPressed();
        //startActivity(_summary);

    }


    void Pass() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Alert");
        alert.setMessage("Password Required");

// Set an EditText view to get user input
        final EditText input = new EditText(getApplicationContext());
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                // Do something with value!

                if (IsMember(value))
                {
                    date.setText(splitStr[0]);
                    account.setText(splitStr[1]);
                    amount.setText(splitStr[3]);
                    type.setText(splitStr[4]);
                    description.setText(splitStr[5]);
                    currency.setText(splitStr[6]);
                    branch.setText(splitStr[7]);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Wrong Password", Toast.LENGTH_LONG).show();
                    Pass();

                }
            }

        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                ReceiveActivity.this.finish();
            }
        });

        alert.show();
    }


    boolean IsMember(String pass) {
        MyDb = this.openOrCreateDatabase("ZapNotification", MODE_PRIVATE, null);
        boolean result = false;
        String res = "";
        try {
            Cursor cursor = MyDb.rawQuery("select * from " + TableName3, null);
            cursor.moveToFirst();
            res = cursor.getString(0);

            if (res.equals(pass)) {
                result = true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

    void Pass2() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Alert");
        alert.setMessage("Password Required");

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                // Do something with value!

                if (IsMember(value)) {
                    try {
                        date.setText(ConvertDate(splitStr[0]));
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }
                    account.setText(splitStr[2]);
                    amount.setText(splitStr[3]);
                    type.setText(splitStr[4]);
                    description.setText(splitStr[5]);
                    currency.setText(splitStr[6]);
                    branch.setText(splitStr[7]);
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong Password", Toast.LENGTH_LONG).show();
                }
            }

        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                ReceiveActivity.this.finish();
            }
        });

        alert.show();
    }

    void save2DB(String data) {
        //String message = intent.getExtras().getString("message");
        String message = data;
        String delimit = ";";
        StringTokenizer ress = new StringTokenizer(message, delimit);
        String[] splitStr = new String[ress.countTokens()];
        int index = 0;
        while (ress.hasMoreElements()) {

            splitStr[index++] = ress.nextToken();

        }

        //create DB in not exist
        try {
            MyDb = this.openOrCreateDatabase("ZapNotification", MODE_PRIVATE, null);

            //create table
            MyDb.execSQL("CREATE TABLE IF NOT EXISTS "
                    + TableName +
                    "(ID primary key autoincrement not null," +
                    "TRAN_DATE INT, " +
                    "ACCOUNT VARCHAR, " +
                    "AMOUNT  VARCHAR," +
                    "TYPE VARCHAR, " +
                    "DESCRIPTION VARCHAR, " +
                    "CURRENCY VARCHAR(3), " +
                    "BRANCH VARCHAR );");

            //POPULATE DB
            //insert data into db
           /* MyDb.execSQL("INSERT INTO "
                    + TableName +
                    " (TRAN_DATE," +
                    "ACCOUNT," +
                    "AMOUNT," +
                    "TYPE, "+
                    "DESCRIPTION," +
                    "CURRENCY," +
                    "BRANCH) " +
                    "VALUES (" +
                    "'" + splitStr[0] + "'," +
                    "'" + splitStr[1] + "'," +
                    "'" + splitStr[3] + "'," +
                    "'" + splitStr[4] + "'," +
                    "'" + splitStr[5] + "'," +
                    "'" + splitStr[6] + "'," +
                    "'" + splitStr[7] + "');");
*/
            MyDb.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String ConvertDate(String InDAte) throws java.text.ParseException {

        String delimit = "/";
        StringTokenizer ress = new StringTokenizer(InDAte, delimit);
        splitStr = new String[ress.countTokens()];
        int index = 0;
        while (ress.hasMoreElements()) {
            splitStr[index++] = ress.nextToken();
        }


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = InDAte;
        Date date = null;
        String date2 = "";
        try {
            date = formatter.parse(dateInString);
            date2 = date.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date2;
    }

    void SEND_ALERT()
    {

    }


}
