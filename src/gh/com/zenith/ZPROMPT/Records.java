package gh.com.zenith.ZPROMPT;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.*;

/**
 * Created by Robby on 1/22/2015.
 */
public class Records extends FragmentActivity
{

    LinearLayout hook;
    ProgressDialog progressDialog;
    SQLiteDatabase MyDb;
    String TableName = "NotificationLogsDB";
    Intent Search;
    Calendar c = Calendar.getInstance();
    ListView _listRecTrsx;

    LayoutInflater inflater;
    TextView _name;
    Intent intent;

    Intent search;
    LazyAdapter _adapter;
    ArrayList<HashMap<String, String>> transactionList;

    static final String KEY_DESC = "song"; // parent node
    static final String KEY_AMOUNT = "id";
    static final String KEY_ACCUONT= "title";
    static final String KEY_DATE= "artist";
    static final String KEY_BRANCH = "duration";
    static final String KEY_TYPE = "thumb_url";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

       intent = getIntent();
        SelectSignup._SelectSignUp.finish();
        String _close = intent.getExtras().getString("FLAG");
        if(_close.equals("REC"))
        {
            ReceiveActivity.receive.finish();
        }
        if(_close.equals("SIN"))
        {
            Signup.home.finish();
        }
        if(_close.equals("SIN2"))
        {
            Signup2.Signup2.finish();
        }

        MyDb = this.openOrCreateDatabase("ZapNotification", MODE_PRIVATE, null);
        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.zenithred));
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Transaction History...");
        actionBar.setLogo(R.drawable.logo3);
        _name = (TextView)findViewById(R.id.textViewName);
       // _name.setText(getName());
        transactionList = new  ArrayList<HashMap<String, String>>();

        search = new Intent(this, Search.class);
        setContentView(R.layout.records);

        getAllData();
    }

    private String getName()
    {
        String name = "No Name";
        try
        {
            //getting data out
            Cursor cursor = MyDb.rawQuery("select USERNAME from CUSTOMER", null);
             name = cursor.getString(0);
            cursor.close();
        }
        catch (Exception ex)
        {
            ex.toString();
        }
        return  name;
    }

    void ShowPass() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Password");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);
        builder.setIcon(R.drawable.icon);
        // Set up the buttons
        builder.setPositiveButton("Log In", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    if (Login(input.getText().toString().trim())) {
                        setContentView(R.layout.records);
                        getAllData();
                    } else {
                        input.setText("");
                        Toast.makeText(getApplicationContext(), "Wrong Password", Toast.LENGTH_LONG);
                        ShowPass();
                    }
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
        });
        builder.show();
    }

    boolean Login(String pass) {
        boolean IsMember = false;
        String name = "";
        try {
            Cursor cursor = MyDb.rawQuery("select DROIDPASS from CUSTOMER", null);
            int count = cursor.getCount();
            cursor.moveToFirst();
            name = cursor.getString(0).trim();
            if (name.equals(pass)) {
                IsMember = true;
                cursor.close();
            }
        } catch (Exception ex) {
            ex.toString();
        }

        return IsMember;
    }

    void pulses() {
        // pulsing = true;
        Timer t = new Timer();
        //Set the schedule function and rate
        t.scheduleAtFixedRate(new TimerTask() {

                                  @Override
                                  public void run() {
                                      //Called each time when 5000 milliseconds (5 second) (the period parameter)

                                      YoYo.with(Techniques.Pulse).duration(10000)
                                              .playOn(hook);
                                  }

                              },
                //Set how long before to start calling the TimerTask (in milliseconds)
                0,
                //Set the amount of time between each execution (in milliseconds)
                5000);

    }

    //spool data from DroidDB
    void getAllData() {
        progressDialog = ProgressDialog.show(Records.this,
                "Loading Transactions...", "Loading...");
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        double ratio = ((float) (width)) / 300.0;
        int height = (int) (ratio * 50);


        try {
            //getting data out
            Cursor cursor = MyDb.rawQuery("select * from " + TableName, null);
            int xl = cursor.getCount();
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast())
                {
                    for (int x = 0; x < cursor.getCount(); x++) {
                        int c = 0;
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(KEY_DESC, cursor.getString(5).trim());
                        map.put(KEY_ACCUONT, cursor.getString(2).trim());
                        map.put(KEY_AMOUNT,cursor.getString(6).trim()+" "+cursor.getString(3).trim());
                        map.put(KEY_TYPE,cursor.getString(4).trim());
                        map.put(KEY_DATE,cursor.getString(1).trim());
                        transactionList.add(map);
                        cursor.moveToNext();
                    }

                }
                _adapter = new LazyAdapter(Records.this,transactionList);
                _listRecTrsx.setAdapter(_adapter);
                cursor.close();
            }
            else
            {
                new AlertDialog.Builder(this)
                        .setTitle(" ")
                        .setMessage("There are no Transactions In Your Logs Yet. Future Account Transactions" +
                                " Will Appear Here.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //LoginActivity.home.finish();
                                //Records.this.finish();

                            }
                        })
                        .setIcon(R.drawable.alert)
                        .show();
            }

        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
        }

        MyDb.close();
        progressDialog.dismiss();

        //pulses();
    }

    String ConvertfromDate(String DBdate) {
        String date = "";
        String day = "";
        String month = "";
        String test = DBdate;
        String year = "";

        if (test.length() < 8) {
            year = test.substring(0, 4);
            month = test.substring(4, 5);
            day = test.substring(5, 7);
            date = year + "/" + month + "/" + day;
        } else {
            year = test.substring(0, 4);
            month = test.substring(4, 6);
            day = test.substring(6, 8);
            date = year + "/" + month + "/" + day;
        }
        return date;

    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("  ")
                .setMessage("Close Application?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //LoginActivity.home.finish();
                        Records.this.finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(R.drawable.alert)
                .show();
    }

    @Override
    protected void onResume() {
        if (checkPassword()) {
            ShowPass();
        } else {
            super.onResume();
        }

    }

    boolean checkPassword() {
        boolean PassWordOption = false;
        String name = "";
        try {
            Cursor cursor = MyDb.rawQuery("select DROIDPASS from CUSTOMER", null);
            int count = cursor.getCount();
            cursor.moveToFirst();

            if (count < 1) {
                PassWordOption = false;
                cursor.close();
            } else {
                PassWordOption = true;
                cursor.close();
            }
        } catch (Exception ex) {
            ex.toString();
        }

        return PassWordOption;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_bank:
                  //LAUNCH IBANK APP OR OPEN BROWSER
                doInternetBaning();

                return true;
            case R.id.menu_reload:
                    //RUN GET ALL DATA
                doReload();
                return true;

            case R.id.menu_search:
                    //LAUNCH SEARCH ACTIVITY
                doSearch();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void doReload() {
        getAllData();
    }

    void doInternetBaning() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://ibank.zenithbank.com.gh/" +
                "internetbanking/um/default.jsp"));
        startActivity(browserIntent);
    }

    void doSearch()
    {
        startActivity(search);
    }


}
