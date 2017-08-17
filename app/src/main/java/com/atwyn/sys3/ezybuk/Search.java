package com.atwyn.sys3.ezybuk;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.androidnetworking.interfaces.Parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

public class Search extends AppCompatActivity {
    private EditText filterText;

    private ArrayAdapter<String> listAdapter;
    String urlAddress="http://10.0.2.2/android/searcher.php";
    SearchView sv;
    ListView lv;
    ImageView noDataImg,noNetworkImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

       /* filterText = (EditText) findViewById(R.id.editText);

        ListView itemList = (ListView) findViewById(R.id.listView);

        String[] listViewAdapterContent = {"School", "House", "Building", "Food", "Sports", "Dress", "Ring"};

        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listViewAdapterContent);

        itemList.setAdapter(listAdapter);

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
// make Toast when click
                Toast.makeText(getApplicationContext(), "Position " + position, Toast.LENGTH_LONG).show();
            }
        });
        filterText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Search.this.listAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}*/

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

        lv= (ListView) findViewById(R.id.lv);
        sv= (SearchView) findViewById(R.id.sv);
     //   noDataImg= (ImageView) findViewById(R.id.nodataImg);
      //  noNetworkImg= (ImageView) findViewById(R.id.noserver);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
@Override
public boolean onQueryTextSubmit(String query) {
        SenderReceiver sr=new SenderReceiver(Search.this,urlAddress,query,lv,noDataImg,noNetworkImg);
        sr.execute();
        return false;
        }
@Override
public boolean onQueryTextChange(String query) {
        SenderReceiver sr=new SenderReceiver(Search.this,urlAddress,query,lv,noDataImg,noNetworkImg);
        sr.execute();
        return false;
        }
        });
        }
    public class SenderReceiver extends AsyncTask<Void,Void,String> {
        Context c;
        String urlAddress;
        String query;
        ListView lv;
        ImageView noDataImg,noNetworkImg;
        ProgressDialog pd;
        public SenderReceiver(Context c, String urlAddress, String query, ListView lv,ImageView...imageViews) {
            this.c = c;
            this.urlAddress = urlAddress;
            this.query = query;
            this.lv = lv;
            this.noDataImg=imageViews[0];
            this.noNetworkImg=imageViews[1];
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(c);
            pd.setTitle("Search");
            pd.setMessage("Searching...Please wait");
            pd.show();
        }
        @Override
        protected String doInBackground(Void... params) {
            return this.sendAndReceive();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            //RESET LISTVIEW
            lv.setAdapter(null);
            if(s != null)
            {
                if(!s.contains("null"))
                {
                    Parser p=new Parser(c,s,lv);
                    p.execute();

                }else
                {

                }
            }else {


            }
        }
        private String sendAndReceive()
        {
            HttpURLConnection con=Connector.connect(urlAddress);
            if(con==null)
            {
                return null;
            }
            try
            {
                OutputStream os=con.getOutputStream();
                BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os));
                bw.write(new DataPackager(query).packageData());
                bw.flush();
                //RELEASE RES
                bw.close();
                os.close();
                //SOME RESPONSE ????
                int responseCode=con.getResponseCode();
                //DECODE
                if(responseCode==con.HTTP_OK)
                {
                    //RETURN SOME DATA stream
                    InputStream is=con.getInputStream();
                    //READ IT
                    BufferedReader br=new BufferedReader(new InputStreamReader(is));
                    String line;
                    StringBuffer response=new StringBuffer();
                    if(br != null)
                    {
                        while ((line=br.readLine()) != null)
                        {
                            response.append(line+"n");
                        }
                    }else {
                        return null;
                    }
                    return response.toString();
                }else {
                    return String.valueOf(responseCode);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public class DataPackager {
        String query;
        public DataPackager(String query) {
            this.query = query;
        }
        public String packageData()
        {
            JSONObject jo=new JSONObject();
            StringBuffer queryString=new StringBuffer();
            try
            {
                jo.put("Query",query);
                Boolean firstValue=true;
                Iterator it=jo.keys();
                do {
                    String key=it.next().toString();
                    String value=jo.get(key).toString();
                    if(firstValue)
                    {
                        firstValue=false;
                    }else {
                        queryString.append("&");
                    }
                    queryString.append(URLEncoder.encode(key,"UTF-8"));
                    queryString.append("=");
                    queryString.append(URLEncoder.encode(value,"UTF-8"));
                }while (it.hasNext());
                return queryString.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class Parser extends AsyncTask<Void,Void,Integer> {
        Context c;
        String data;
        ListView lv;
        ArrayList<String> names=new ArrayList<>();
        public Parser(Context c, String data, ListView lv) {
            this.c = c;
            this.data = data;
            this.lv = lv;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Integer doInBackground(Void... params) {
            return this.parse();
        }
        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(integer==1)
            {
                //BIND TO LISTVIEW
                ArrayAdapter adapter=new ArrayAdapter(c,android.R.layout.simple_list_item_1,names);
                lv.setAdapter(adapter);
            }else {
                Toast.makeText(c,"Unable to Parse",Toast.LENGTH_SHORT).show();
            }
        }
        private int parse()
        {
            try
            {
                JSONArray ja=new JSONArray(data);
                JSONObject jo=null;
                names.clear();
                for(int i=0;i<ja.length();i++)
                {
                    jo=ja.getJSONObject(i);
                    String name=jo.getString("Name");
                    names.add(name);
                }
                return 1;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }
        }