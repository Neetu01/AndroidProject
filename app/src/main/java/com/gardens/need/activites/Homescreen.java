package com.gardens.need.activites;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.gardens.need.R;
import com.gardens.need.adapters.ProductList;
import com.gardens.need.fragments.AboutUs;
import com.gardens.need.fragments.ContactUs;
import com.gardens.need.fragments.Dashboard;
import com.gardens.need.fragments.Enquiry;
import com.gardens.need.fragments.ProductDetails;
import com.gardens.need.fragments.SubCategory;
import com.gardens.need.helpers.Constants;

import java.util.ArrayList;
import java.util.List;

public class Homescreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fm;

    public int parentId = 0;
    public int subParentId = 0;
    public int detailsParentId = 0;
    public List<Integer>  selectedProductsId = new ArrayList<>();
    public List<ProductList> selectedProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Initialise with Dashboard
        openFragmentByTag(Constants.Dashboard, false);

        // Send Enquiry actionbar button
        ImageView b_sendEnquiry = (ImageView) findViewById(R.id.b_sendEnquiry);
        b_sendEnquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Enquiry enquiry = (Enquiry) getSupportFragmentManager().findFragmentByTag(Constants.Enquiry);
                if (enquiry == null || !enquiry.isVisible())
                    openFragmentByTag(Constants.Enquiry, true);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            // Handle the camera action
            clearBackStack(null);
            openFragmentByTag(Constants.Dashboard, false);
        } else if (id == R.id.category) {
            clearBackStack(null);
            openFragmentByTag(Constants.Dashboard, true);
        } else if (id == R.id.contact) {
            clearBackStack(null);
            openFragmentByTag(Constants.ContactUs, true);
        } else if (id == R.id.enquiry) {
            clearBackStack(null);
            openFragmentByTag(Constants.Enquiry, true);
        } else if (id == R.id.about) {
            clearBackStack(null);
            openFragmentByTag(Constants.AboutUs, true);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /*
    * .................Open a fragment from tag.................
    * */
    public void openFragmentByTag(String fragTag, boolean addToBackStack) {
        Fragment frag = getFragmentFromTag(fragTag);

        if (frag != null) {

            fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.content_home_screen, frag, fragTag);

            if (addToBackStack) {
                ft.addToBackStack(Constants.Dashboard);
            }

            ft.commit();

        }
    }


    /*
    * .................Returns fragment from its Tag.................
    * */
    public Fragment getFragmentFromTag(String fragTag) {
        Fragment frag = null;

        switch (fragTag) {

            case Constants.Dashboard:
                frag = new Dashboard();
                break;
            case Constants.ContactUs:
                frag = new ContactUs();
                break;
            case Constants.AboutUs:
                frag = new AboutUs();
                break;
            case Constants.Enquiry:
                frag = new Enquiry();
                break;
            case Constants.SubCategory:
                frag = new SubCategory();
                break;
            case Constants.ProductDetails:
                frag = new ProductDetails();
                break;
        }

        return frag;
    }

    /*
    * .................Clears the fragment from backstack.................
    * */
    public void clearBackStack(@Nullable String fragTag) {

        if (fragTag != null) {
            fm.popBackStackImmediate(fragTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
//            fm.popBackStackImmediate();
            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStackImmediate();
            }

        }
    }

    /*
    * Creates String of Selected Items
    * */
    public String getSelectedProducts() {
        String message = "";

        for (int i = 0; i < selectedProducts.size(); i++) {
            if (i == 0) {
                message += selectedProducts.get(i).Name;
            } else {
                message += ", " + selectedProducts.get(i).Name;
            }
        }

        return message;
    }

}
