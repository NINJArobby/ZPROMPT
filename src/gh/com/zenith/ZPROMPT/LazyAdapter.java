package gh.com.zenith.ZPROMPT;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Robby on 1/30/2015.
 */
public class LazyAdapter extends BaseAdapter
{
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    Random rand = new Random();
    int r = rand.nextInt(255);
    int _g = rand.nextInt(255);
    int b = rand.nextInt(255);


    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position)
    {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row, null);

        TextView desc = (TextView)vi.findViewById(R.id.txtListDesc); // title
        TextView amount = (TextView)vi.findViewById(R.id.txtListAmount); // artist name
        TextView account = (TextView)vi.findViewById(R.id.txtListAccount); // duration
        TextView date = (TextView)vi.findViewById(R.id.txtListDate);
        TextView type=(TextView)vi.findViewById(R.id.list_image); // thumb image

        HashMap<String,String> song = new HashMap<String, String>();
        song = data.get(position);

        // Setting all values in listview
        desc.setText(song.get(Search.KEY_DESC));
        amount.setText(song.get(Search.KEY_AMOUNT));
        account.setText(song.get(Search.KEY_ACCUONT));
        date.setText(song.get(Search.KEY_DATE));
        String _type = song.get(Search.KEY_TYPE);
        if(_type.equals("credit"))
        {
            type.setText("CR");
            type.setTextColor(Color.WHITE);
            int randomColor = Color.rgb(r, _g, b);
            type.setBackgroundColor(randomColor);
        }
        else
        {
            type.setText("DR");
            type.setTextColor(Color.WHITE);
            int randomColor = Color.rgb(r, _g, b);
            type.setBackgroundColor(randomColor);
        }

        return vi;
    }
}
