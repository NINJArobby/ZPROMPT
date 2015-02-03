package gh.com.zenith.ZPROMPT;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import java.util.Random;
import java.util.StringTokenizer;

/**
 * Created by Robby on 8/5/2014.
 */
public class DBUpdate extends Activity {
    String[] splitStr;
    SQLiteDatabase MyDb;
    String TableName = "NotificationLogsDB";
    Uri noteSound;
    long[] vibrate = {0, 100, 200, 300};
    public static final int NOTIFICATION_ID = 1;

    String msg = "Account Activity";
    String payLoad = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String message = intent.getExtras().getString("message");
        payLoad = intent.getStringExtra("message");
        String delimit = ";";
        StringTokenizer ress = new StringTokenizer(message, delimit);
        splitStr = new String[ress.countTokens()];
        int index = 0;
        while (ress.hasMoreElements())
        {
            splitStr[index++] = ress.nextToken();
        }
        save2DB(message);
        //sendNotification(msg,payLoad);
        this.finish();
    }

    void save2DB(String data) {
        //create DB in not exist
        try {
            MyDb = this.openOrCreateDatabase("ZapNotification", MODE_PRIVATE, null);

            //create table
            MyDb.execSQL("CREATE TABLE IF NOT EXISTS "
                    + TableName +
                    "(ID integer primary key autoincrement not null," +
                    "TRAN_DATE integer, " +
                    "ACCOUNT VARCHAR, " +
                    "AMOUNT  VARCHAR," +
                    "TYPE VARCHAR, " +
                    "DESCRIPTION VARCHAR, " +
                    "CURRENCY VARCHAR(3), " +
                    "BRANCH VARCHAR );");

            //POPULATE DB
            //insert data into db
            MyDb.execSQL("INSERT INTO "
                    + TableName +
                    " (TRAN_DATE," +
                    "ACCOUNT," +
                    "AMOUNT," +
                    "TYPE, " +
                    "DESCRIPTION," +
                    "CURRENCY," +
                    "BRANCH) " +
                    "VALUES (" +
                    "'" + ConvertDate(splitStr[0]) + "'," +
                    "'" + splitStr[1] + "'," +
                    "'" + splitStr[3] + "'," +
                    "'" + splitStr[4] + "'," +
                    "'" + splitStr[5] + "'," +
                    "'" + splitStr[6] + "'," +
                    "'" + splitStr[7] + "');");

            MyDb.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static int randomBox() {

        Random rand = new Random();
        int pickedNumber = rand.nextInt(100);
        return pickedNumber;

    }

    private void sendNotification(String msg, String Payload) {
        //**add this line**
        int requestID = randomBox();
        NotificationManager mNotificationManager = (NotificationManager)
                getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // Notification note = new Notification(R.drawable.dialoge,"",System.currentTimeMillis());

        Intent myintent = new Intent(getApplicationContext(), ReceiveActivity.class);
        //Intent myIntent2 = new Intent(getApplicationContext(), DBUpdate.class);

        // **add this line**
        myintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        myintent.putExtra("message", Payload);
        //myIntent2.putExtra("message",Payload);
/*
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                myintent, PendingIntent.FLAG_UPDATE_CURRENT);*/

        PendingIntent contentIntent = PendingIntent.getActivity(this,
                requestID, myintent, PendingIntent.FLAG_ONE_SHOT);
        // requestID,myintent, PendingIntent.FLAG_UPDATE_CURRENT);
        //PendingIntent contentIntent2 = PendingIntent.getActivity(this,
        //requestID,myIntent2, PendingIntent.FLAG_UPDATE_CURRENT);


    /*    PendingIntent pintent =
                PendingIntent.getActivity(context,0,myintent,PendingIntent.FLAG_CANCEL_CURRENT);*/

       /* NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.zenith)
                        .setContentTitle("Account Activity")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg)
                        .setSound(noteSound)
                        .setVibrate(vibrate)
                        .setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
*/

    }

    public String ConvertDate(String InDAte) throws java.text.ParseException {
        String newDAte = InDAte.substring(0, 9);
        String delimit = "/";
        StringTokenizer ress = new StringTokenizer(newDAte, delimit);
        String[] splitStr = new String[ress.countTokens()];
        int index = 0;
        while (ress.hasMoreElements()) {
            splitStr[index++] = ress.nextToken();
        }
        String day = splitStr[1].toString();
        if (day.length() < 2) {
            day = "0" + day;
        }
        String month = splitStr[0].toString();
        if (month.length() < 2) {
            month = "0" + month;
        }
        String year = splitStr[2].toString();

        String newDate = year + month + day;
        return newDate;
    }

}
