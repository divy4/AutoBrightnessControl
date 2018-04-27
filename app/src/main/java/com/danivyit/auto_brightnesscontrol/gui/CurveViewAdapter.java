package com.danivyit.auto_brightnesscontrol.gui;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.danivyit.auto_brightnesscontrol.R;

import java.util.List;

public class CurveViewAdapter extends RecyclerView.Adapter {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;

        /**
         * Creates a new ViewHolder.
         * @param itemView
         */
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        /**
         * Returns the view of the item;
         * @return
         */
        public View getView() {
            return itemView;
        }
    }

    private List<String> names;

    /**
     * Creates a new CurveViewAdapter.
     * @param names The names of the profiles.
     */
    public CurveViewAdapter(List<String> names) {
        this.names = names;
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.profile_view, parent, false);
        return new ViewHolder(layout);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        View view = ((ViewHolder)holder).getView();
        TextView text = (TextView)view.findViewById(R.id.profileName);
        text.setText(names.get(position));
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return names.size();
    }
}
