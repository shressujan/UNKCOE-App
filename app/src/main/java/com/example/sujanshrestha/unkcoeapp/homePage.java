package com.example.sujanshrestha.unkcoeapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Created by Sujan Shrestha on 9/11/17.
 */

public class homePage extends Activity implements Methods{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Opens app in portrait mode only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.homepage_layout);

    } /**
     * Log in button onClick event
     * @param view
     */


    public void onClickAdd(View view)
    {

        addClickEffect(view);
        Intent intent = new Intent(this, add.class);
        startActivity(intent);
    }

    /**
     * Log in button onClick event
     * @param view
     */
    public void onClickView(View view)
    {
        addClickEffect(view);

        Intent intent = new Intent(this, view.class);
        startActivity(intent);
    }



    /**
     * Log in button onClick event
     * @param view
     */
    public void onClickInfo(View view)
    {
        addClickEffect(view);

//        //Getting the the number of rows in the report table
//        int row = 0;
//        try {
//            Statement stmt1 = MainActivity.conn.createStatement();
//            String varSQL1 = "SELECT COUNT(*) FROM Report";
//            ResultSet rs = null;
//            try {
//                rs = stmt1.executeQuery(varSQL1);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            rs.next();
//            row = rs.getInt(1);
//            rs.close();
//            stmt1.close();
//        } catch (SQLException ex) {
//
//        }

        Intent intent = new Intent(this, info.class);
        startActivity(intent);
    }


    @SuppressLint("NewApi")
    public void addClickEffect(View view)
    {
        Drawable drawableNormal = view.getBackground();

        Drawable drawablePressed = view.getBackground().getConstantState().newDrawable();
        drawablePressed.mutate();
        drawablePressed.setColorFilter(Color.argb(100, 0, 0, 0), PorterDuff.Mode.SRC_ATOP);

        StateListDrawable listDrawable = new StateListDrawable();
        listDrawable.addState(new int[] {android.R.attr.state_pressed}, drawablePressed);
        listDrawable.addState(new int[] {}, drawableNormal);
        view.setBackground(listDrawable);
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
