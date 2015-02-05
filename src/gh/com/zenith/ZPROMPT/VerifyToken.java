package gh.com.zenith.ZPROMPT;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class VerifyToken extends Activity
{
    EditText token;
    Button _doToken;
    String SOAP_ACTION_AUTHENTICATE = "http://tempuri.org/DoSignup2 ";
    String SOAP_METHOD_NAME_AUTHENTICATE = "DoSignup2 ";

    String SOAP_URL = "http://196.216.180.26:85/iBank/Service1.asmx";
    String SOAP_NAMESPACE = "http://tempuri.org/";
    String regid;

    ProgressDialog progressDialog;
    String SENDER_ID = "623887383809";
    public static Activity verifyHome;
    static final String TAG = "ZPROMPT";
    GoogleCloudMessaging gcm;
    SQLiteDatabase MyDb;

    Intent records;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_signup);
        token = (EditText)findViewById(R.id.edTxtTokenVerify);
        _doToken = (Button)findViewById(R.id.btnregisterToken);

        verifyHome=this;
        records = new Intent(this,Records.class);

        token.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                token.setText("");
            }
        });

        _doToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                progressDialog = ProgressDialog.show(VerifyToken.this,
                        "Finishing Signup", "Please Wait...");
                new DoVerifyToken().execute();
            }
        });

    }

    class DoVerifyToken extends AsyncTask<String, String, String>
    {
        boolean done = false;
        String msg;
        @Override
        protected String doInBackground(String... arg0)
        {
            try
            {
                gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                regid = gcm.register(SENDER_ID);
            }

            catch (UnsupportedOperationException ex)
            {
                Toast.makeText(getApplicationContext(), "SORRY, YOUR DEVICE DOES NOT SUPPORT" +
                                " PUSH NOTIFICATION",
                        Toast.LENGTH_LONG).show();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            if(!regid.isEmpty())
            {
                String _msg = null;
                SoapObject request = new SoapObject(SOAP_NAMESPACE, SOAP_METHOD_NAME_AUTHENTICATE);
                PropertyInfo pi1 = new PropertyInfo();

                pi1.setName("token");
                pi1.setValue(token.getText().toString().trim());//get the string that is to be sent to the web service
                pi1.setType(String.class);
                request.addProperty(pi1);

                PropertyInfo pi2 = new PropertyInfo();
                pi2.setName("devID");
                pi2.setValue(regid);
                pi2.setType(String.class);
                request.addProperty(pi2);

                try {
                    SoapSerializationEnvelope envp = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envp.dotNet = true;
                    envp.setOutputSoapObject(request);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
                    androidHttpTransport.call(SOAP_ACTION_AUTHENTICATE, envp);
                    //edit made here
                    Object response = envp.getResponse();
                    String result = response.toString();
                    if (result.equals("true")) {
                        done = true;
                        progressDialog.dismiss();
                    }
                } catch (Exception ex) {
                    ex.toString();
                    progressDialog.dismiss();
                }
            }
            Log.d("111", msg);
            return msg;
        }

        @Override
        protected void onPostExecute(String msg)
        {
            if(done)
            {
                askPass();
                setSinedUp();
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

    void AlertSuccess()
    {
        // Locate the TextView
        new AlertDialog.Builder(getApplicationContext())
                .setTitle("Success!!!")
                .setMessage("Press Button To Proceed")
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // TODO Auto-generated method stub
                        startActivity(records);
                    }
                }).setIcon(R.drawable.icon).show();
    }

    void AlertError()
    {
        // Locate the TextView
        new AlertDialog.Builder(getApplicationContext())
                .setTitle("Error!!!")
                .setMessage("Please Check Credentials and try again")
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
                " SET DROIDPASS ='"+Encrypt(pass)+"';");
    }

    void setSinedUp()
    {
        MyDb.execSQL("UPDATE CUSTOMER"+
                " SET SIGNEDUP ='YES';");
    }

    void saveName(String name)
    {
        MyDb.execSQL("UPDATE CUSTOMER SET USERNAME ='"+ name +"';");
    }

    void saveAccts(String acct)
    {
        MyDb.execSQL("INSERT INTO ACCOUNTS(ACCT_NO) VALUES('"+acct+"';");
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
