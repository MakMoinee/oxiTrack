package com.oxitrack.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.oxitrack.client.databinding.ActivityDashboardBinding;
import com.oxitrack.client.interfaces.LogoutListener;
import com.oxitrack.client.models.Users;
import com.oxitrack.client.preference.UserPref;

public class DashboardActivity extends AppCompatActivity implements LogoutListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityDashboardBinding binding;

    private NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarDashboard.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        View navView = binding.navView.getHeaderView(0);
        TextView txtName = navView.findViewById(R.id.txtName);
        Users users = new UserPref(DashboardActivity.this).getUsers();
        if (users != null) {
            txtName.setText(String.format("%s %s %s", users.getFirstName(), users.getMiddleName(), users.getLastName()));
        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_devices, R.id.nav_pulse, R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_dashboard);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_dashboard);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void finishCurrentActivity() {
        new UserPref(DashboardActivity.this).clearLogin();
        Toast.makeText(DashboardActivity.this, "Logut Successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(DashboardActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void cancelLogout() {
        navController.navigate(R.id.nav_home);
    }
}