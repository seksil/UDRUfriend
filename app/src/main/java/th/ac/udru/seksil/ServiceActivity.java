package th.ac.udru.seksil;

import android.content.Intent;

import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ServiceActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

//        Create Toolbar
        createToolbar();

//        Create ListView
        createListView();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.layoutMainFragment, new ServiceFragment()).commit();

        }

    }   // Main Method

    private void createListView() {
        ListView listView = findViewById(R.id.listViewMenu);
        MyConstant myConstant = new MyConstant();
        DrawerMenuAdapter drawerMenuAdapter = new DrawerMenuAdapter(ServiceActivity.this,
                myConstant.getIconInts(), myConstant.getTitleStrings());
        listView.setAdapter(drawerMenuAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        signOutService();
                        break;
                }
                drawerLayout.closeDrawers();

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.itemExit) {
            signOutService();
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOutService() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        startActivity(new Intent(ServiceActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_service, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void createToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarService);
        setSupportActionBar(toolbar);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        getSupportActionBar().setSubtitle(firebaseUser.getDisplayName());

        drawerLayout = findViewById(R.id.layoutDrawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(ServiceActivity.this,
                drawerLayout, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_ham);

    }   // toolbar

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
}   // Main Class