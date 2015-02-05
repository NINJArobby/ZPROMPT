package gh.com.zenith.ZPROMPT;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by Robby on 1/25/2015.
 */
public class Signup2 extends Activity
{
    Intent records;
    SQLiteDatabase MyDb;
    EditText code;
    Button doSignup;
    public static Activity Signup2;

    GoogleCloudMessaging gcm;
    String SOAP_ACTION_VERIFY_ACCT = "http://tempuri.org/Signup2_Verify_Acct";
    String SOAP_METHOD_NAME_AUTHENTICATE = "Signup2_Verify_Acct";
    String SOAP_URL = "http://196.216.180.26:85/ibank/Service1.asmx";

    String SOAP_NAMESPACE = "http://tempuri.org/";
    String regid;

    Intent _verifyToken;
    ProgressDialog progressDialog;
    String SENDER_ID = "623887383809";
    public static Activity home;
    String Password = "";
    static final String TAG = "ZPROMPT";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.token_signup);
        ActionBar actionBar = getActionBar();

        records = new Intent(this, Records.class);
        records.putExtra("FLAG","SIN2");
        MyDb = this.openOrCreateDatabase("ZapNotification", MODE_PRIVATE, null);
        code = (EditText)findViewById(R.id.edTxtToken);
        doSignup = (Button)findViewById(R.id.btnregister);
        Signup2=this;
        _verifyToken = new Intent(this,VerifyToken.class);


        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.zenithred));
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Token Registration");
        actionBar.setLogo(R.drawable.logo3);

        doSignup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            { progressDialog = ProgressDialog.show(Signup2.this,
                    "Requesting Token", "Please Wait...");
                new RegisterBackground().execute();
            }
        });
    }

    class RegisterBackground extends AsyncTask<String, String, String>
    {
        boolean done = false;
        String msg;
        @Override
        protected String doInBackground(String... arg0)
        {
                String _msg = null;
                SoapObject request = new SoapObject(SOAP_NAMESPACE, SOAP_METHOD_NAME_AUTHENTICATE);
                PropertyInfo pi1 = new PropertyInfo();

                pi1.setName("Acct_no");
                pi1.setValue(code.getText().toString().trim());//get the string that is to be sent to the web service
                pi1.setType(String.class);
                request.addProperty(pi1);
                try
                {
                    SoapSerializationEnvelope envp = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envp.dotNet = true;
                    envp.setOutputSoapObject(request);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
                    androidHttpTransport.call(SOAP_ACTION_VERIFY_ACCT, envp);
                    //edit made here
                    Object response = envp.getResponse();
                    String result = response.toString();
                    if (result.equals("true"))
                    {
                        done = true;
                    }
                }
                catch (Exception ex)
                {
                    ex.toString();

                }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg)
        {
            if(done)
            {
                progressDialog.dismiss();
                Alert();
            }
            else
            {
                AlertError();
            }
        }

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    void Alert()
    {
        // Locate the TextView
        new AlertDialog.Builder(Signup2.this)
                .setTitle("   ")
                .setMessage("we will send you a token shortly. Please use that to finish the signup.")
                .setNegativeButton("Proceed", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // TODO Auto-generated method stub
                        startActivity(_verifyToken);
                    }
                }).setIcon(R.drawable.alert).show();
    }

    void AlertError()
    {
        // Locate the TextView
        new AlertDialog.Builder(Signup2.this)
                .setTitle("Error!!!")
                .setMessage("Something went wrong. please wait a while and try again")
                .setNegativeButton("Proceed", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // TODO Auto-generated method stub

                    }
                }).setIcon(R.drawable.alert).show();
    }

    private void askPass() {
        new AlertDialog.Builder(this)
                .setTitle("Action Required")
                .setMessage("Provide Password to Protect Data?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        ShowPass();
                    }
                })
                .setNegativeButton("NO",new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        startActivity(records);
                    }
                })
                .setIcon(R.drawable.zenith)
                .show();
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
        builder.setIcon(R.drawable.alert);
        // Set up the buttons
        builder.setPositiveButton("Log In", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                try
                {
                    savePass(input.getText().toString().trim());
                    startActivity(records);
                }
                catch (Exception ex)
                {

                }
            }
        });
        builder.show();
    }

    void savePass(String pass)
    {
        MyDb.execSQL("UPDATE CUSTOMER"+
                " SET DROIDPASS ='"+pass+"';");
    }

    void setSinedUp()
    {
        MyDb.execSQL("UPDATE CUSTOMER"+
                " SET SIGNEDUP ='YES';");
    }

    public String Encrypt(String data)
    {
        String fixedText;
        String output= "";

        try {
            CryptLib _crypt = new CryptLib();
            String plainText = data;
            String key = CryptLib.SHA256("T0T015G00D4B0Y5ANDG1RL5", 32); //32 bytes = 256 bit
            String iv = CryptLib.generateRandomIV(16); //16 bytes = 128 bit
            output = _crypt.encrypt(plainText, key, iv); //encrypt
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return output;
    }

    public String dEcrypt(String data)
    {
        String fixedText;
        String output= "";
        try {
            CryptLib _crypt = new CryptLib();
            String plainText = data;
            String key = CryptLib.SHA256("T0T015G00D4B0Y5ANDG1RL5", 32); //32 bytes = 256 bit
            String iv = CryptLib.generateRandomIV(16); //16 bytes = 128 bit
            output = _crypt.decrypt(data, key,iv); //decrypt
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return output;
    }

}
