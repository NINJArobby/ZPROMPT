package gh.com.zenith.ZPROMPT;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Robby on 1/26/2015.
 */
public class Search extends Activity {
    Button _search;
    TextView _fromDate;
    TextView _toDate;
    Spinner accntSpinner;
    ProgressDialog progressDialog;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    String SOAP_ACTION_AUTHENTICATE = "http://tempuri.org/searchTransactions";
    String SOAP_METHOD_NAME_AUTHENTICATE = "searchTransactions";
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "NeWregistrationID_za";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    String SOAP_URL = "http://196.216.180.26:85/ibankService/Service1.asmx";
    String SOAP_NAMESPACE = "http://tempuri.org/";
    static final String KEY_DESC = "song"; // parent node
    static final String KEY_AMOUNT = "id";
    static final String KEY_ACCUONT= "title";
    static final String KEY_DATE= "artist";
    static final String KEY_BRANCH = "duration";
    static final String KEY_TYPE = "thumb_url";

    String[] splitStr;

    int xl;

    String fromddate;
    String todate;
    List<HelperClass.Profile> profile;
    final Gson gson = new Gson();
    LinearLayout _searchHolder;
    ListView _list;

    LazyAdapter _adapter;
    ArrayList<HashMap<String, String>> transactionList;
    //SlidingMenu slidingMenu;

    LinearLayout _listHolder;

   @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.look);
           _fromDate = (TextView)findViewById(R.id.txtFromdate1);
            _toDate = (TextView)findViewById(R.id.txtEndDatae1);
            _search = (Button) findViewById(R.id.btnSearch11);
            accntSpinner = (Spinner)findViewById(R.id.SpnAccts11);
        _searchHolder = (LinearLayout)findViewById(R.id.searchHolder1);
       _listHolder = (LinearLayout)findViewById(R.id.listHolder1);
        transactionList = new  ArrayList<HashMap<String, String>>();


        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.zenithred));
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Search for transaction...");
        actionBar.setLogo(R.drawable.search);


        _list = (ListView)findViewById(R.id.listTrsx1);

        String[] values = new String[]{"Choose account","6090108987",
                "4090113849"
                };

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,values);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        accntSpinner.setAdapter(dataAdapter);


        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        Calendar newCalendar = Calendar.getInstance();

        _fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });

        _toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDatePickerDialog.show();
            }
        });

        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                _fromDate.setText(dateFormatter.format(newDate.getTime()));
                YoYo.with(Techniques.Bounce).duration(1000)
                        .playOn(_fromDate);

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                _toDate.setText(dateFormatter.format(newDate.getTime()));
                YoYo.with(Techniques.Bounce).duration(1000)
                        .playOn(_toDate);

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        _search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromddate = _fromDate.getText().toString().trim();
                todate = _toDate.getText().toString().trim();
                progressDialog = ProgressDialog.show(Search.this,
                        "Fetching Transactions...", "Please Wait...");
                new doSearch().execute();
                //_test();
            }
        });
    }

    class doSearch extends AsyncTask<String, String, String>
    {
        boolean done = false;
        String msg;
        ArrayList  _results = new ArrayList();
        SoapObject response;

        @Override
        protected String doInBackground(String... arg0)
        {
            // TODO Auto-generated method stub

            String newFromDate = _fromDate.getText().toString().replace("-", "");
            String new2date = _toDate.getText().toString().replace("-", "");
            String _msg = null;
            SoapObject request = new SoapObject(SOAP_NAMESPACE, SOAP_METHOD_NAME_AUTHENTICATE);
            PropertyInfo pi1 = new PropertyInfo();

            pi1.setName("fromDate");
            pi1.setValue(newFromDate);//get the string that is to be sent to the web service
            pi1.setType(String.class);
            request.addProperty(pi1);

            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("toDate");
            pi2.setValue(new2date);//get the string that is to be sent to the web
            pi2.setType(String.class);
            request.addProperty(pi2);

            PropertyInfo pi3 = new PropertyInfo();
            pi3.setName("acctNo");
            pi3.setValue(accntSpinner.getSelectedItem().toString());//get the string that is to be sent to the web
            pi3.setType(String.class);
            request.addProperty(pi3);

            try {
                SoapSerializationEnvelope envp = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envp.dotNet = true;
                envp.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
                androidHttpTransport.call(SOAP_ACTION_AUTHENTICATE, envp);
                response = (SoapObject)envp.getResponse();
            }
            catch (Exception ex)
            {
                ex.toString();
                progressDialog.dismiss();
            }

            return msg;
        }
        @Override
        protected void onPostExecute(String msg)
        {

            final int count = response.getPropertyCount();
              for(int x=0;x<count;x++)
            {
                xl=x;
                HashMap<String, String> map = new HashMap<String, String>();
                String text =response.getProperty(xl).toString().trim();
                String delimit = ";";
                StringTokenizer ress = new StringTokenizer(text, delimit);
                splitStr = new String[ress.countTokens()];
                int index = 0;
                while (ress.hasMoreElements())
                {
                    splitStr[index++] = ress.nextToken();
                }
                map.put(KEY_DESC,splitStr[4].trim());
                map.put(KEY_ACCUONT, splitStr[1].trim() + " " + splitStr[6].trim());
                map.put(KEY_AMOUNT,splitStr[5].trim()+" "+splitStr[2].trim());
                map.put(KEY_TYPE,splitStr[3].trim());
                transactionList.add(map);
            }
            _adapter = new LazyAdapter(Search.this,transactionList);
            runOnUiThread(new Runnable()
            {
                @Override
                public void run() {

                    _list.setAdapter(_adapter);
                }
            });

            Display getOrient = getWindowManager().getDefaultDisplay();
            int orientation = Configuration.ORIENTATION_UNDEFINED;
            if(getOrient.getWidth()==getOrient.getHeight()){
                orientation = Configuration.ORIENTATION_SQUARE;
                _searchHolder.setVisibility(View.GONE);
            }
            else
            {
                if(getOrient.getWidth() < getOrient.getHeight())
                {
                    orientation = Configuration.ORIENTATION_PORTRAIT;
                    _searchHolder.setVisibility(View.GONE);
                }
                else
                {
                    orientation = Configuration.ORIENTATION_LANDSCAPE;
                }
            }
            progressDialog.dismiss();
            YoYo.with(Techniques.SlideInUp).duration(800)
                    .playOn(_listHolder);
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_search:
                //LAUNCH IBANK APP OR OPEN BROWSER
                showLayout();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void showLayout()
    {
        _searchHolder.setVisibility(View.VISIBLE);
    }


}
