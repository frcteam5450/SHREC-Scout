package org.frcteam5450.shrecscout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class MatchTeamActivity extends AppCompatActivity {

    private static MatchTeam mTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        mTeam = (MatchTeam)bundle.getSerializable(RecyclerAdapter.MatchHolder.TEAM_KEY);

        setTitle("Match " + intent.getIntExtra(RecyclerAdapter.MatchHolder.MATCH_KEY, 0) + " | Team " + mTeam.getTeamNumber());

        final EditText mMatchPoints = (EditText) findViewById(R.id.match_points);
        mMatchPoints.setText(Integer.toString((int) mTeam.getMatchPoints()));
        mMatchPoints.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    try {
                        mTeam.setMatchPoints(Double.parseDouble(s.toString()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final Switch mAlliance = (Switch) findViewById(R.id.alliance);
        mAlliance.setChecked(mTeam.getAlliance());
        mAlliance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mTeam.setAlliance(isChecked);
            }
        });

        final Spinner mAlliancePosition = (Spinner) findViewById(R.id.alliance_position);
        mAlliancePosition.setSelection(mTeam.getAlliancePosition() - 1);
        mAlliancePosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mTeam.setAlliancePosition(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Switch mCrossed = (Switch) findViewById(R.id.crossed);
        mCrossed.setChecked(mTeam.getAutoCrossedLine());
        mCrossed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mTeam.setAutoCrossedLine(isChecked);
            }
        });

        final Switch mLifted = (Switch) findViewById(R.id.lifted);
        mLifted.setChecked(mTeam.getTeleopLifted());
        mLifted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mTeam.setTeleopLifted(isChecked);
            }
        });



        final TextView autoHighGoals = (TextView) findViewById(R.id.autoHighGoals);
        autoHighGoals.setText(Integer.toString(mTeam.getAutoHighGoalShots()));
        final ImageButton autoHighGoalsDown = (ImageButton) findViewById(R.id.autoHighGoalsDown);
        autoHighGoalsDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTeam.subtractAutoHighGoal();
                autoHighGoals.setText(Integer.toString(mTeam.getAutoHighGoalShots()));
            }
        });
        final ImageButton autoHighGoalsUp = (ImageButton) findViewById(R.id.autoHighGoalsUp);
        autoHighGoalsUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTeam.addAutoHighGoal();
                autoHighGoals.setText(Integer.toString(mTeam.getAutoHighGoalShots()));
            }
        });

        final TextView autoLowGoals = (TextView) findViewById(R.id.autoLowGoals);
        autoLowGoals.setText(Integer.toString(mTeam.getAutoLowGoalShots()));
        final ImageButton autoLowGoalsDown = (ImageButton) findViewById(R.id.autoLowGoalsDown);
        autoLowGoalsDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTeam.subtractAutoLowGoal();
                autoLowGoals.setText(Integer.toString(mTeam.getAutoLowGoalShots()));
            }
        });
        final ImageButton autoLowGoalsUp = (ImageButton) findViewById(R.id.autoLowGoalsUp);
        autoLowGoalsUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTeam.addAutoLowGoal();
                autoLowGoals.setText(Integer.toString(mTeam.getAutoLowGoalShots()));
            }
        });

        final TextView autoGears = (TextView) findViewById(R.id.autoGears);
        autoGears.setText(Integer.toString(mTeam.getAutoGearsDelivered()));
        final ImageButton autoGearsDown = (ImageButton) findViewById(R.id.autoGearsDown);
        autoGearsDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTeam.subtractAutoGearsDelivered();
                autoGears.setText(Integer.toString(mTeam.getAutoGearsDelivered()));
            }
        });
        final ImageButton autoGearsUp = (ImageButton) findViewById(R.id.autoGearsUp);
        autoGearsUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTeam.addAutoGearsDelivered();
                autoGears.setText(Integer.toString(mTeam.getAutoGearsDelivered()));
            }
        });

        final TextView teleopHighGoals = (TextView) findViewById(R.id.teleopHighGoals);
        teleopHighGoals.setText(Integer.toString(mTeam.getTeleopHighGoalShots()));
        final ImageButton teleopHighGoalsDown = (ImageButton) findViewById(R.id.teleopHighGoalsDown);
        teleopHighGoalsDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTeam.subtractTeleopHighGoal();
                teleopHighGoals.setText(Integer.toString(mTeam.getTeleopHighGoalShots()));
            }
        });
        final ImageButton teleopHighGoalsUp = (ImageButton) findViewById(R.id.teleopHighGoalsUp);
        teleopHighGoalsUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTeam.addTeleopHighGoal();
                teleopHighGoals.setText(Integer.toString(mTeam.getTeleopHighGoalShots()));
            }
        });

        final TextView teleopLowGoals = (TextView) findViewById(R.id.teleopLowGoals);
        teleopLowGoals.setText(Integer.toString(mTeam.getTeleopLowGoalShots()));
        final ImageButton teleopLowGoalsDown = (ImageButton) findViewById(R.id.teleopLowGoalsDown);
        teleopLowGoalsDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTeam.subtractTeleopLowGoal();
                teleopLowGoals.setText(Integer.toString(mTeam.getTeleopLowGoalShots()));
            }
        });
        final ImageButton teleopLowGoalsUp = (ImageButton) findViewById(R.id.teleopLowGoalsUp);
        teleopLowGoalsUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTeam.addTeleopLowGoal();
                teleopLowGoals.setText(Integer.toString(mTeam.getTeleopLowGoalShots()));
            }
        });

        final TextView teleopGears = (TextView) findViewById(R.id.teleopGears);
        teleopGears.setText(Integer.toString(mTeam.getTeleopGearsDelivered()));
        final ImageButton teleopGearsDown = (ImageButton) findViewById(R.id.teleopGearsDown);
        teleopGearsDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTeam.subtractTeleopGearsDelivered();
                teleopGears.setText(Integer.toString(mTeam.getTeleopGearsDelivered()));
            }
        });
        final ImageButton teleopGearsUp = (ImageButton) findViewById(R.id.teleopGearsUp);
        teleopGearsUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTeam.addTeleopGearsDelivered();
                teleopGears.setText(Integer.toString(mTeam.getTeleopGearsDelivered()));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_match, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_back) {
            finish();
            return true;
        } else if (id == R.id.action_save) {
            mTeam.calculatePercentContribution();
            for (int i = 0; i < MatchActivity.mTeamList.get(MatchActivity.currentTabIndex).size(); i++) {
                if (MatchActivity.mTeamList.get(MatchActivity.currentTabIndex).get(i).getTeamNumber() == mTeam.getTeamNumber()) {
                    MatchActivity.mTeamList.get(MatchActivity.currentTabIndex).set(i, mTeam);
                    break;
                }
            }
            MatchActivity.updateRecyclerView();
            MatchActivity.saveToFiles();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
