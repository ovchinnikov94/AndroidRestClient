package com.example.dmitry.androidrestclient;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;



import com.example.dmitry.androidrestclient.db.DbService;
import com.example.dmitry.androidrestclient.fragment.CreditFragment;


public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://mighty-depths-10490.herokuapp.com/";
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static DbService dbService;


    private FloatingActionButton fab;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private CreditFragment creditFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbService = new DbService(getApplicationContext());
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();
        initNavigationView();
    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Toast.makeText(getApplicationContext(), "This is POTRACHENO!", Toast.LENGTH_SHORT);
                drawerLayout.closeDrawers();
                switch(item.getItemId()) {
                    case R.id.menu_item_credit:
                        Toast.makeText(getApplicationContext(), "This is POTRACHENO!", Toast.LENGTH_SHORT);
                        if (creditFragment == null)
                            creditFragment = new CreditFragment();
                        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, creditFragment).commit();
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Not implemented yet!", Toast.LENGTH_SHORT);
                }
                return true;
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.select_year:

        }

        return super.onOptionsItemSelected(item);
    }
}
