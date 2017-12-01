package com.example.sujanshrestha.unkcoeapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.logging.*;
import com.jcraft.jsch.*;

/**
 Created by Sujan Shrestha on 9/12/17.
 */
public class MainActivity extends AppCompatActivity {
    Button login;
    EditText username;
    EditText password;
    ProgressBar progressBar;


    int assigned_port = 0;
    final int local_port = 3306;

    // Remote host and port
    final int remote_port = 3306;
    final String remote_host = "db-csit.unk.edu";

    //Declaring connection variables
   public static Connection conn = null;
   String userName, passWord, dataBase, iPAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting the values by the ID
        username = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.passWord);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        //Declaring server login information

        userName = "UNKCOEADMIN";
        passWord = "UNKCOEADMIN";
        dataBase = "UNKCOEInventory";
        iPAddress = "localhost";

    }

    /**
     * Adding the click effect to the Login button
     *
     * @param view
     */
    @SuppressLint("NewApi")
    public void addClickEffect(View view) {
        Drawable drawableNormal = view.getBackground();

        Drawable drawablePressed = view.getBackground().getConstantState().newDrawable();
        drawablePressed.mutate();
        drawablePressed.setColorFilter(Color.argb(100, 0, 0, 0), PorterDuff.Mode.SRC_ATOP);

        StateListDrawable listDrawable = new StateListDrawable();
        listDrawable.addState(new int[]{android.R.attr.state_pressed}, drawablePressed);
        listDrawable.addState(new int[]{}, drawableNormal);
        view.setBackground(listDrawable);
    }


    /**
     * Log in button onClick event
     *
     * @param view
     */
    public void onClickLogIn(View view) {
        addClickEffect(view);


        //Instantiating the database connector
        Log.w("Connection", "inside before database");


        // this is the Asynctask which is used to process
        CheckLogin checkLogin = new CheckLogin();
        //run background to reduce the load on the app process
        checkLogin.execute("");

    }


    protected class CheckLogin extends AsyncTask<String, String, String> {
        String message = "";
        Boolean isSuccessful = false; //For checking if the login has failed or not

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @SuppressWarnings("WrongThread")
        @Override
        protected String doInBackground(String... params) {
            String userN = username.getText().toString();
            String passW = password.getText().toString();
            isSuccessful = true;

            try {

                JSch jsch = new JSch();

                // Create SSH session. Port 22 is your SSH port which
                // is open in your fire-wall setup.
                Session session = jsch.getSession(userN, remote_host, 22);
                String passwordString = new String(passW);
                session.setPassword(passwordString);

                // Additional SSH options. See your ssh_config manual for
                // more options. Set options according to your requirements.
                java.util.Properties config = new java.util.Properties();
                config.put("StrictHostKeyChecking", "no");
                config.put("Compression", "yes");
                config.put("ConnectionAttempts", "2");

                session.setConfig(config);
                Log.e("Connection", "trying to connect");
                // Connect
                session.connect();
                // System.out.println("Host: "+session.getHost());

                // Create the tunnel through port forwarding.
                // This is basically instructing jsch session to send
                // data received from local_port in the local machine to
                // remote_port of the remote_host
                // assigned_port is the port assigned by jsch for use,
                // it may not always be the same as
                // local_port.

                assigned_port = session.setPortForwardingL(local_port, "127.0.0.1", remote_port);

            } catch (JSchException e1) {
                Log.e("Connection", Level.SEVERE + " " + e1.getMessage());
                System.exit(0);
            }

            if (assigned_port == 0) {
                Log.e("Connection",Level.SEVERE + " " + "Port forwarding failed !");
                System.exit(0);
                return message;
            }
            message = "Login Successfull!";
            Log.e("Connection","Server Connected!!");

                try {
                    conn = connectionclass(userName, passWord, dataBase, iPAddress);
                    if (conn == null) {
                        message = "Error in connection with SQL server!";
                        isSuccessful = false;
                    } else {
                        isSuccessful = true;
                        message = "Database Connected";
                    }
                }
                catch (Exception e) {
                    isSuccessful = false;
                    message = e.getMessage();
                }

            Log.w("Connection", "HERE");
            return message;
        }


        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            if (isSuccessful) {
                Toast.makeText(MainActivity.this, "Login Successfull!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, homePage.class);
                startActivity(intent);
                finish();
            }
        }
    }

    /**
     *
     * @param userName
     * @param passWord
     * @param dataBase
     * @param iPAddress
     * @return connection
     */
    public Connection connectionclass(String userName, String passWord, String dataBase, String iPAddress)
    {

        StrictMode.ThreadPolicy policy  =  new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection =  null;
        String connectionURL =  null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
//            Class.forName("com.mysql.jdbc.Driver").newInstance();
            try {
//                connectionURL = ("jdbc:jtds:sqlserver://"+ iPAddress + dataBase+";user= "+userName+ ";password= "+ passWord+ ";");
//                connection =  DriverManager.getConnection(connectionURL);
//                Log.w("Connection", "open");

                StringBuilder url1 =
                        new StringBuilder("jdbc:jtds:sqlserver://");
                url1.append(iPAddress).append(":").append(local_port).append ("/").append(dataBase).append("?zeroDateTimeBehavior=convertToNull");
              Log.i("Connection",url1.toString());
                connection = DriverManager.getConnection(url1.toString(),userName,passWord);
                Log.i("Connection","Should be connected");

            }
            catch (Exception e)
            {
                Log.e("Connection",e.getMessage());
            }
//            connectionURL = ("jdbc:jtds:sqlserver://"+ iPAddress + dataBase+";user= "+userName+ ";password= "+ passWord+ ";");
//            connection =  DriverManager.getConnection(connectionURL);
//            Log.w("Connection", "open");
        }
        catch (ClassNotFoundException e) {
            Log.e("Connection", e.getMessage());
        } catch (InstantiationException e) {
            Log.e("Connection",e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e("Connection",e.getMessage());
        }

        return connection;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
