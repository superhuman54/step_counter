package com.navercorp.recruit.kimkihwan.stepcounter.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.navercorp.recruit.kimkihwan.stepcounter.R;
import com.navercorp.recruit.kimkihwan.stepcounter.logger.Log;
import com.navercorp.recruit.kimkihwan.stepcounter.provider.StepCounterContract;
import com.navercorp.recruit.kimkihwan.stepcounter.util.UnitUtils;

/**
 * Created by jamie on 10/2/16.
 */

public class HistoryFragment extends SectionFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int LOADER_ID_STEPCOUNTER = 1;

    private RecyclerView recyclerView;

    public static HistoryFragment newInstance(int position, String title) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_POSITION, position);
        args.putString(ARG_SECTION_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new HistoryCursorAdapter());
        getLoaderManager().initLoader(LOADER_ID_STEPCOUNTER, null, this);
        return rootView;
    }

    private void update() {
        getLoaderManager().restartLoader(LOADER_ID_STEPCOUNTER, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(this, "onResume()");
        update();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> loader = null;
        String[] projection = new String[]{
                StepCounterContract.StepCounter.STARTED_DATE,
                StepCounterContract.StepCounter.STEPS,
                StepCounterContract.StepCounter.DISTANCE,
                StepCounterContract.StepCounter.UPDATED_DATEIME
        };
        switch (id) {
            case LOADER_ID_STEPCOUNTER:
                loader = new CursorLoader(
                        getContext(),
                        StepCounterContract.StepCounter.CONTENT_URI,
                        projection,
                        null,
                        null,
                        StepCounterContract.StepCounter.STARTED_DATE + " DESC");
                break;
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.getCount() > 0) {
            ((HistoryCursorAdapter)recyclerView.getAdapter()).swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((HistoryCursorAdapter)recyclerView.getAdapter()).swapCursor(null);
    }

    private class HistoryCursorAdapter
            extends RecyclerView.Adapter<HistoryViewHolder> {

        private Cursor cursor;

        @Override
        public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_history, parent, false);

            return new HistoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(HistoryViewHolder holder, int position) {
            if (cursor != null && cursor.moveToPosition(position)) {
                String date = cursor.getString(cursor.getColumnIndex(
                        StepCounterContract.StepCounter.STARTED_DATE
                ));

                int steps = cursor.getInt(cursor.getColumnIndex(
                        StepCounterContract.StepCounter.STEPS
                ));

                double distance = cursor.getDouble(cursor.getColumnIndex(
                        StepCounterContract.StepCounter.DISTANCE
                ));
                holder.date.setText(date);
                holder.steps.setText(String.valueOf(steps));
                holder.distance.setText(UnitUtils.forDisplay(distance));
            }
        }

        @Override
        public int getItemCount() {
            return cursor != null ? cursor.getCount() : 0;
        }

        public void swapCursor(Cursor newCursor) {
            Log.d(this, "swapCursor()");
            if (cursor != null) {
                cursor.close();
            }

            cursor = newCursor;
            notifyDataSetChanged();
        }
    }

    private class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView steps;
        TextView distance;

        public HistoryViewHolder(View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.date);
            steps = (TextView) itemView.findViewById(R.id.steps);
            distance = (TextView) itemView.findViewById(R.id.distance);
        }
    }
}
