package com.example.mehealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*  Fragment TabLayout yläpalkkiin
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        //Set up the ViewPager with the sections adapter
        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);

        //Tekee tabLayoutin ylä valikkoon
        TabLayout tabLayout = findViewById(R.id.toolbarTop);
        tabLayout.setupWithViewPager(mViewPager);

        //Kolme palikkaa valikkoon
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_attach_money);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_attach_money);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_settings);
         */


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBar);

        //Asettaa nykyisen välilehden ikonin valituksi
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        //Tarkistaa jos jotain alavalikon ikonia painetaan
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.ic_home:
                        //Nykyisen välilehden ikonin klikkaaminen ei tee mitään
                        break;
                    case R.id.ic_attach_money:
                        //Riippuen ikonista uusi intent ohjautuu oikeaan activityyn
                        Intent paino = new Intent(MainActivity.this, PainoActivity.class);
                        startActivity(paino.addFlags(paino.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_local_drink:
                        Intent vesi = new Intent(MainActivity.this, VesiActivity.class);
                        startActivity(vesi.addFlags(vesi.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_directions_run:
                        Intent liikunta = new Intent(MainActivity.this, LiikuntaActivity.class);
                        startActivity(liikunta.addFlags(liikunta.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_insert_emoticon:
                        Intent mieliala = new Intent(MainActivity.this, MielialaActivity.class);
                        startActivity(mieliala.addFlags(mieliala.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                }
                return false;
            }
        });
    }

    public void btnSettings_onClick(View view) {
        Intent intent = new Intent(this, AsetuksetActivity.class);
        startActivity(intent);
    }

    public void btnRead_onClick(View view) {

    }


    //Metodi ylävalikon viewpagerin alustamiseen, ylin addFragment on alkusivu
    /*private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment());
        adapter.addFragment(new Tab2Fragment());
        adapter.addFragment(new AsetuksetFragment());
        viewPager.setAdapter(adapter);
    }*/


}

