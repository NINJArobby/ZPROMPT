package gh.com.zenith.ZPROMPT;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Robby on 1/25/2015.
 */
public class SelectSignup extends Activity
{
    Button iBank;
    Button Token;
    Intent signup;
    public static Activity _SelectSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_select);

        Start._start.finish();
        _SelectSignUp = this;

        iBank = (Button)findViewById(R.id.btnIbankSignup);
        Token = (Button)findViewById(R.id.btnDirectSignup);
        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.zenithred));
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Registration Selection");
        actionBar.setLogo(R.drawable.logo3);

        iBank.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signup = new Intent(getBaseContext(), Signup.class);
                startActivity(signup);
            }
        });

        Token.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signup = new Intent(getBaseContext(),Signup2.class);
                startActivity(signup);
            }
        });

        Alert();

    }


    void Alert()
    {
        // Locate the TextView
        new AlertDialog.Builder(SelectSignup.this)
                .setTitle("   ")
                .setMessage("Please choose a method for signup. if you are registered on internet banking,"+
                        "you can signup using your login credentials. Otherwise, you would need to request "+
                        "for a signup token.")
                .setNegativeButton("Proceed", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // TODO Auto-generated method stub

                    }
                }).setIcon(R.drawable.alert).show();
    }

    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
    }

}
