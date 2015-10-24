package dianaupham.gh;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;
import android.util.Log;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutionException;
import org.apache.http.HttpStatus;

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

    View thisactivity = this.findViewById(android.R.id.content).getRootView();
    clickbuttonRecieve(thisactivity);
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
    public void clickbuttonRecieve(View v) {
        try {
            JSONObject json = new JSONObject();
            json.put("UserName", "test2");
            json.put("FullName", "1234567");
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams,
                    500);
            HttpConnectionParams.setSoTimeout(httpParams, 500);
            HttpClient client = new DefaultHttpClient(httpParams);
            //
            String url3 = "http://stephaniemertz.com/backend/signup.php?" +
                         "json={\"UserName\":1,\"FullName\":2}";
            String url2 = "http://10.0.2.2:8080/sample1/webservice2.php";
            String url = "http://stephaniemertz.com/backend/signup.php?message=username&email=secretpassword";

            //String sendCommand = "http://" + remoteIPAddress + ":1999/ST:" +
                    //partID.get(current.getDPID()) + current.getDeviceID() + ":END";
            String receive = new connectTask().execute(url).get();

            /*HttpPost request = new HttpPost(url);
            request.setEntity(new ByteArrayEntity(json.toString().getBytes(
                    "UTF8")));
            request.setHeader("json", json.toString());
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            // If the response does not enclose an entity, there is no need
            if (entity != null) {
                InputStream instream = entity.getContent();

                String result = convertStreamToString(instream);
                Log.i("Read from server", result);
                Toast.makeText(this,  result,
                        Toast.LENGTH_LONG).show();
            }*/
            Toast.makeText(this, receive, Toast.LENGTH_LONG).show();
        } catch (Throwable t) {
            Toast.makeText(this, "Request failed: " + t.toString(),
                    Toast.LENGTH_LONG).show();

        }
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
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

    /* This class conducts networking that needs to be separate from the Main thread
	 * The AsyncTask runs when new connectTask.execute(String message) is called
	 * It creates a new HttpURLConnection to send the request and
	 * returns a String of the received message from the server
	 * The returned string can be retrieved by calling get() on the connectTask object
	 */
    public class connectTask extends AsyncTask<String,String,String> {
        protected String doInBackground(String... message) {
            HttpURLConnection urlConnection = null;
            String receive = "";

            try {
                URL url = new URL(message[0]);
                Log.d("Sending:", message[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int responseCode = urlConnection.getResponseCode();

                if(responseCode == HttpStatus.SC_OK){
                    receive = readStream(urlConnection.getInputStream());
                    Log.d("Receive: ", receive);
                }else{
                    Log.v("connectTask", "Response code:"+ responseCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return receive;
        }

        /* Helper method for processing the server return input
         * from http://syntx.io/how-to-send-an-http-request-from-android-using-httpurlconnection/
         */
        private String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuffer response = new StringBuffer();
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return response.toString();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
}