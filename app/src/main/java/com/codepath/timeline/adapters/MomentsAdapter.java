package com.codepath.timeline.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


public class MomentsAdapter extends RecyclerView.Adapter<MomentsAdapter.ViewHolder>{
  @Override
  public MomentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return null;
  }

  @Override
  public void onBindViewHolder(MomentsAdapter.ViewHolder holder, int position) {

  }

  @Override
  public int getItemCount() {
    return 0;
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    public ViewHolder(View itemView) {
      super(itemView);
    }
  }
}
