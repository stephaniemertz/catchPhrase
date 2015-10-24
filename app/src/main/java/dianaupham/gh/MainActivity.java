package dianaupham.gh;
import java.util.Random;

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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

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

        buttonLogIn = (Button)findViewById(R.id.LogIn); //set login button
        buttonLogIn.setOnClickListener(this); //sets an onClickListener on buttonLogIn
        //if (prefs.getBoolean("NotLoggedIn", false))
        //     buttonLogIn.setVisibility(View.GONE); //removes this button from view, and makes space for others

        editor.commit();
        new CountDownTimer(100,100) {
            public void onTick(long millisUntilFinished)
            {
                if (millisUntilFinished / 500 == 0)
                {
                    onFinish();
                }
                else
                {}
            }
            public void onFinish()
            {
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                if(prefs.getBoolean("NotLoggedIn", true) && prefs.getBoolean("AskLogin", true) ) {
                    LoginScreen();
                }
            }
        }.start();

        editor.commit();
        TextView login_message = (TextView)findViewById(R.id.LoginMessage);
        NotLogged = prefs.getBoolean("NotLoggedIn", true);
        if(NotLogged){ login_message.setText("Not logged in"); }
        else { login_message.setText("Welcome " + prefs.getString("PlayerName", "Player")); }

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
        graph.addSeries(barSeries);  //draw 'barSeries'
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
        graph.addSeries(barSeries);  //draw 'barSeries'
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