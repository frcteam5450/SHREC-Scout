package org.frcteam5450.shrecscout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SHREC5450 on 1/13/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MatchHolder> {
    private ArrayList<MatchTeam> mTeamList;
    public static Context c;

    public RecyclerAdapter(ArrayList<MatchTeam> teams) {
        mTeamList = teams;
    }

    @Override
    public RecyclerAdapter.MatchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.team_item_row, parent, false);
        return new MatchHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.MatchHolder holder, int position) {
        MatchTeam itemTeam = mTeamList.get(position);
        holder.bindMatch(itemTeam);
    }

    @Override
    public int getItemCount() {
        return mTeamList.size();
    }

    public static class MatchHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView mTeamNumber;
        private TextView mTeamAlliance;
        private TextView mTeamPoints;
        private TextView mTeamPercent;
        private MatchTeam mMatchTeam;

        public static final String TEAM_KEY = "TEAM_NUMBER";
        public static final String MATCH_KEY = "MATCH";

        public MatchHolder(View v) {
            super(v);

            mTeamNumber = (TextView) v.findViewById(R.id.recyclerview_number);
            mTeamAlliance = (TextView) v.findViewById(R.id.recyclerview_alliance);
            mTeamPoints = (TextView) v.findViewById(R.id.recyclerview_points);
            mTeamPercent = (TextView) v.findViewById(R.id.recyclerview_percent);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        public void bindMatch(MatchTeam matchTeam) {
            mMatchTeam = matchTeam;
            mTeamNumber.setText("TEAM " + mMatchTeam.getTeamNumber());
            mTeamAlliance.setText("" + (mMatchTeam.getAlliance() ? "Red Alliance" : "Blue Alliance") + " " + mMatchTeam.getAlliancePosition());
            mTeamPoints.setText("" + (int)Math.round(mMatchTeam.getMatchPoints()) + "pts");
            mTeamPercent.setText("" + ((double)Math.round(10 * mMatchTeam.getPercentContribution()) / 10) + "%");
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(c, MatchTeamActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(TEAM_KEY, mMatchTeam);
            intent.putExtras(bundle);
            intent.putExtra(MATCH_KEY, MatchActivity.mTeamList.get(MatchActivity.currentTabIndex).getMatchNumber());
            c.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(c);
            builder.setTitle("Delete TEAM " + mMatchTeam.getTeamNumber());

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for (int i = 0; i < MatchActivity.mTeamList.get(MatchActivity.currentTabIndex).size(); i++) {
                        if (MatchActivity.mTeamList.get(MatchActivity.currentTabIndex).get(i).getTeamNumber() == mMatchTeam.getTeamNumber()) {
                            MatchActivity.mTeamList.get(MatchActivity.currentTabIndex).remove(i);
                            break;
                        }
                    }
                    MatchActivity.updateRecyclerView();
                    MatchActivity.saveToFiles();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
            return false;
        }
    }
}
