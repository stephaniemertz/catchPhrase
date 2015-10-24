package dianaupham.gh;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.util.Log;
import android.widget.PopupWindow;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final Random RANDOM = new Random();
    private LineGraphSeries<DataPoint> series;
    private int lastX = 0;

    PopupWindow popupWindow;
    Button buttonLogIn, buttonSignOut;
    Button buttonProfile;
    Button Sign_Out, BACK;
    Button LOG_IN, REG, NoThanks;      // For Login screen
    public static final String PREFS_NAME = "PREFS_FILE";  //LOCAL STORAGE
    boolean NotLogged, create = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.commit();
        boolean firstTime = prefs.getBoolean("FirstTimeSetup", true);
        if (firstTime) {
            editor.putString("PlayerName", "Player");// For player name
            editor.putInt("HighScore", 0);           // For player High Score
            editor.putBoolean("FirstTimeSetup", false);
            editor.commit();
        }
        // If not logged in send out login popup action
        boolean NotLoggedIn = prefs.getBoolean("NotLoggedIn", true);
        boolean AskLogin = prefs.getBoolean("AskLogin", true);
        editor.putBoolean("NotLoggedIn", NotLoggedIn);
        editor.putBoolean("AskLogin", AskLogin);

        buttonLogIn = (Button) findViewById(R.id.LogIn); //set login button
        buttonLogIn.setOnClickListener(this); //sets an onClickListener on buttonLogIn
        //if (prefs.getBoolean("NotLoggedIn", false))
        //     buttonLogIn.setVisibility(View.GONE); //removes this button from view, and makes space for others

        editor.commit();
        new CountDownTimer(100, 100) {
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished / 500 == 0) {
                    onFinish();
                } else {
                }
            }

            public void onFinish() {
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                if (prefs.getBoolean("NotLoggedIn", true) && prefs.getBoolean("AskLogin", true)) {
                    LoginScreen();
                }
            }
        }.start();

        editor.commit();
        TextView login_message = (TextView) findViewById(R.id.LoginMessage);
        NotLogged = prefs.getBoolean("NotLoggedIn", true);
        if (NotLogged) {
            login_message.setText("Not logged in");
        } else {
            login_message.setText("Welcome " + prefs.getString("PlayerName", "Player"));
        }

        // get graph view instance
        GraphView graph = (GraphView) findViewById(R.id.graph);
        // set data points
        BarGraphSeries<DataPoint> barSeries = new BarGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(1, 6),
                new DataPoint(2, 3),
                new DataPoint(3, 3),
                new DataPoint(4, 2),
                new DataPoint(5, 1)
        });

     if (NotLogged == false) {
        // draw the graph to screen
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Words");
        gridLabel.setVerticalAxisTitle("Frequency");
        graph.setTitle("Your Speech Patterns");
        // GraphView 4.x
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
//        staticLabelsFormatter.setHorizontalLabels(new String[] {"old", "middle", "new"});
//        staticLabelsFormatter.setVerticalLabels(new String[] {"low", "middle", "high"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        graph.addSeries(barSeries);  //draw 'barSeries'

        buttonLogIn.setVisibility(View.GONE);
    }
}


    public void DrawGraph() {
        // get graph view instance
        GraphView graph = (GraphView) findViewById(R.id.graph);
        // set data points
        BarGraphSeries<DataPoint> barSeries = new BarGraphSeries<DataPoint>(new DataPoint []{
                new DataPoint(1,6),
                new DataPoint(2,3),
                new DataPoint(3,3),
                new DataPoint(4,2),
                new DataPoint(5,1)
        });
        // draw the graph to screen
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Words");
        gridLabel.setVerticalAxisTitle("Frequency");
        graph.setTitle("Your Speech Patterns");
        // GraphView 4.x
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
//        staticLabelsFormatter.setHorizontalLabels(new String[] {"old", "middle", "new"});
//        staticLabelsFormatter.setVerticalLabels(new String[] {"low", "middle", "high"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        graph.addSeries(barSeries);  //draw 'barSeries'
    }

    //++++++++++++++++++++++++++++++++WEBSITE JSON TO ANDROID+++++++++++++++++++++++++++++++//
    //http://www.codeproject.com/Articles/267023/Send-and-receive-json-between-android-and-php
    public void GetTranscription (View v) {
        try {
            // http://androidarabia.net/quran4android/phpserver/connecttoserver.php

            // Log.i(getClass().getSimpleName(), "send  task - start");
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 500);
            HttpConnectionParams.setSoTimeout(httpParams, 500);
            HttpParams p = new BasicHttpParams();
            // p.setParameter("name", pvo.getName());
            p.setParameter("user", "1");

            // Instantiate an HttpClient
            HttpClient httpclient = new DefaultHttpClient(p);
            String url = "http://10.0.2.2:8080/sample1/" +
                    "webservice1.php?user=1&format=json";
            HttpPost httppost = new HttpPost(url);

            // Instantiate a GET HTTP method
            try {
                Log.i(getClass().getSimpleName(), "send  task - start");
                //
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);
                nameValuePairs.add(new BasicNameValuePair("user", "1"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String responseBody = httpclient.execute(httppost,
                        responseHandler);
                // Parse
                JSONObject json = new JSONObject(responseBody);
                JSONArray jArray = json.getJSONArray("posts");
                ArrayList<HashMap<String, String>> mylist =
                        new ArrayList<HashMap<String, String>>();

                for (int i = 0; i < jArray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject e = jArray.getJSONObject(i);
                    String s = e.getString("post");
                    JSONObject jObject = new JSONObject(s);

                    map.put("idusers", jObject.getString("idusers"));
                    map.put("UserName", jObject.getString("UserName"));
                    map.put("FullName", jObject.getString("FullName"));

                    mylist.add(map);
                }
                Toast.makeText(this, responseBody, Toast.LENGTH_LONG).show();

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // Log.i(getClass().getSimpleName(), "send  task - end");

        } catch (Throwable t) {
            Toast.makeText(this, "Request failed: " + t.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void LogInClick() {   //LogIn method
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        NotLogged = prefs.getBoolean("NotLoggedIn", true);
        if( NotLogged ) {
            Intent login = new Intent(MainActivity.this, MainActivity.class);
            startActivity(login);
            LoginScreen();
            //finish();
            //buttonSignOut.setVisibility(View.VISIBLE);
        }else {
            finish();
        }

    }

    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.login:
                //LogInClick();
                LoginScreen();
                break;
            case R.id.LogIn:
            //    LogInClick();
                LoginScreen();
                break;
            case R.id.sign_out:
                LogInClick(); //###################
                break;
        }
    }

    // * For login screen +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    public void LoginScreen() {
        try {
            LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.activity_welcome_screen, (ViewGroup)findViewById(R.id.login_Layout));
            popupWindow = new PopupWindow(layout, getWindow().getAttributes().width, getWindow().getAttributes().height, true);
            popupWindow.setAnimationStyle(-1);
            popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
            LOG_IN = (Button)findViewById(R.id.login1);
            REG = (Button)findViewById(R.id.register1);
            LOG_IN.setOnClickListener(this);
            REG.setOnClickListener(this);
            NoThanks.setOnClickListener(this);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void register(View v) {
        Intent i = new Intent(getApplicationContext(),Register.class);
        startActivity(i);
        popupWindow.dismiss();
    }

    public  void login(View v) {
        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
        popupWindow.dismiss();
    }

    public  void no_thanks(View v) {
        popupWindow.dismiss();
    }

    public  void stop_asking(View v) {
        CheckBox StopLogin = (CheckBox) v.findViewById(R.id.checkBox1);
        //StopLogin.toggle();
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if (StopLogin.isChecked() == true) {
            editor.putBoolean("AskLogin", false);
            editor.commit();
        }
        else {
            editor.putBoolean("AskLogin", true);
            editor.commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}