package org.frcteam5450.shrecscout;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MatchActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private static MatchActivity currentActivity;
    private static SectionsPagerAdapter mSectionsPagerAdapter;
    private static final int REQUEST_FILE_PERMISSIONS = 0;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private static ViewPager mViewPager;
    public static int currentTabIndex = 0;

    private static RecyclerView mRecyclerView;
    private static LinearLayoutManager mLinearLayoutManager;
    private static RecyclerAdapter mAdapter;
    public  static List<Match> mTeamList;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        currentActivity = MatchActivity.this;
        RecyclerAdapter.c = MatchActivity.this;
        mTeamList = new ArrayList<>();
        requestPermissions();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), MatchActivity.this);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setOnTabSelectedListener( new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                currentTabIndex = tab.getPosition();
                updateRecyclerView();
            }
        });

        final FloatingActionButton fab_match = (FloatingActionButton) findViewById(R.id.fab_match);
        fab_match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add a new match to the current team

                AlertDialog.Builder builder = new AlertDialog.Builder(MatchActivity.this);
                builder.setTitle("Enter TEAM Number");

                final EditText mMatchNumber = new EditText(MatchActivity.this);
                mMatchNumber.setGravity(Gravity.CENTER);
                mMatchNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(mMatchNumber);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mMatchNumber.getText().toString().isEmpty()) {
                            dialog.cancel();
                        } else {
                            mTeamList.get(currentTabIndex).add(new MatchTeam(Integer.parseInt(mMatchNumber.getText().toString()), false, 1));
                            Toast.makeText(MatchActivity.this, "Added TEAM " + mMatchNumber.getText().toString() + " (" + mTeamList.get(currentTabIndex).size() + " Total)", Toast.LENGTH_SHORT).show();
                            updateMatchList();
                            //updateRecyclerView();

                            saveToFiles();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        final FloatingActionButton fab_team = (FloatingActionButton) findViewById(R.id.fab_team);
        fab_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MatchActivity.this);
                builder.setTitle("Enter MATCH Number");

                final EditText mMatchNumber = new EditText(MatchActivity.this);
                mMatchNumber.setGravity(Gravity.CENTER);
                mMatchNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(mMatchNumber);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mMatchNumber.getText().toString().isEmpty()) {
                            dialog.cancel();
                        } else {
                            mTeamList.add(new Match(Integer.parseInt(mMatchNumber.getText().toString())));
                            Toast.makeText(MatchActivity.this, "Added MATCH " + mMatchNumber.getText().toString() + " (" + mTeamList.size() + " Total)", Toast.LENGTH_SHORT).show();
                            mSectionsPagerAdapter.notifyDataSetChanged();
                            if (mTeamList.size() > 0) fab_match.setEnabled(true);

                            saveToFiles();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        if (mTeamList.size() == 0) fab_match.setEnabled(false);
    }

    private static void requestPermissions() {
        boolean needReadPermission = ContextCompat.checkSelfPermission(MatchActivity.currentActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED;
        boolean needWritePermission = ContextCompat.checkSelfPermission(MatchActivity.currentActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED;

        if (needReadPermission || needWritePermission) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MatchActivity.currentActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(MatchActivity.currentActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MatchActivity.currentActivity);
                builder.setTitle("Permission to READ and WRITE Files");

                final TextView mExplanation = new TextView(MatchActivity.currentActivity);
                mExplanation.setText("SHREC Scout uses your phone's filesystem to store TEAM and MATCH information. This app requires permission to READ and WRITE .csv files.");

                builder.setView(mExplanation);
                builder.setPositiveButton("I Understand", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

                List<String> mPermissions = new ArrayList<>();
                if (needReadPermission) mPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                if (needWritePermission) mPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                ActivityCompat.requestPermissions(MatchActivity.currentActivity,
                        (String[])mPermissions.toArray(),
                        REQUEST_FILE_PERMISSIONS);

            } else {
                List<String> mPermissions = new ArrayList<String>();
                if (needReadPermission) mPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                if (needWritePermission) mPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                ActivityCompat.requestPermissions(MatchActivity.currentActivity,
                        Arrays.copyOf(mPermissions.toArray(), mPermissions.size(), String[].class),
                        REQUEST_FILE_PERMISSIONS);
            }
        }  else {
            loadFromFiles();
        }
    }

    public static void updateMatchList() {
        mAdapter.notifyDataSetChanged();
    }

    public static void updateRecyclerView() {
        View rootView = mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem()).getView();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.match_list);
        mLinearLayoutManager = new LinearLayoutManager(rootView.getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new RecyclerAdapter(mTeamList.get(currentTabIndex));
        mRecyclerView.setAdapter(mAdapter);
    }

    public static void saveToFiles() {
        FileHandler.writeAllFiles(mTeamList);
    }

    public static void loadFromFiles() {
        mTeamList = FileHandler.readAllFiles();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_team, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_send) {
            if(mTeamList.size() > 0) {
                saveToFiles();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(FileHandler.getFileName(mTeamList.get(currentTabIndex)))));
                startActivity(intent);
            } else {
                Toast.makeText(MatchActivity.this, "You don't have any MATCHES to share, try adding one or touch HELP.", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (id == R.id.action_help) {
            Intent intent = new Intent(MatchActivity.this, HelpActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FILE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for(int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(MatchActivity.this, "SHREC Scout is unable to run without permission to READ and WRITE files.", Toast.LENGTH_LONG).show();
                            finish();
                            return;
                        }
                    }
                    loadFromFiles();
                } else {
                    Toast.makeText(MatchActivity.this, "SHREC Scout is unable to run without permission to READ and WRITE files.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }

    public static class TeamFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_select_team, container, false);

            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.match_list);
            mLinearLayoutManager = new LinearLayoutManager(rootView.getContext());
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            mAdapter = new RecyclerAdapter(mTeamList.get(currentTabIndex));
            mRecyclerView.setAdapter(mAdapter);

            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        List<TeamFragment> mFragmentList;

        public SectionsPagerAdapter(FragmentManager fm, AppCompatActivity a) {
            super(fm);
            mFragmentList = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            while (position >= mFragmentList.size()) mFragmentList.add(new TeamFragment());
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mTeamList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position < mTeamList.size()) {
                return "Match " + mTeamList.get(position).getMatchNumber();
            } else return null;
        }
    }
}
