package gh.com.zenith.ZPROMPT;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Robby on 1/28/2015.
 */
public class Token extends Activity
{
    Intent intent;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        token = intent.getExtras().getString("TOKEN");
        new AlertDialog.Builder(this)
                .setTitle("Alert...")
                .setMessage("Your Token is: "+token)
                .setPositiveButton("Close", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Token.this.finish();
                    }
                })
                .setIcon(R.drawable.logo3)
                .show();
    }
}
