package com.eftimoff.knowledge.adapters;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.eftimoff.knowledge.R;
import com.eftimoff.knowledge.model.Game;
import com.eftimoff.knowledge.utils.DateUtils;

import java.util.List;

public class GameAdapter extends ArrayAdapter<Game> {
	private LayoutInflater inflater;

	public GameAdapter(Context context, List<Game> objects) {
		super(context, 0, objects);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

/*	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return R.layout.game_adapter_item;
		} else {
			return R.layout.game_adapter_item;
		}
	}*/

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final View rowView = inflater.inflate(R.layout.game_adapter_item, parent, false);
		ViewHolder holder = new ViewHolder();
		// configure view holder
		holder.status = (TextView) rowView.findViewById(R.id.status);
		holder.time = (TextView) rowView.findViewById(R.id.time);

		// fill data
		final Game game = getItem(position);
		holder.status.setText(game.getStatus().toString());
		startTimer(DateUtils.getDifferenceFromNow(game.getStartingDate()), 100, holder.time);

		return rowView;
	}


	private void startTimer(final long time, final long interval, final TextView timeTextView) {
		new CountDownTimer(time, interval) {

			public void onTick(long millisUntilFinished) {
				if (timeTextView != null) {
					timeTextView.setText(DateUtils.formatMilliseconds(millisUntilFinished));
				}
			}

			public void onFinish() {
				if (timeTextView != null) {
					timeTextView.setText("done!");
				}
			}
		}.start();
	}

	private static class ViewHolder {
		public TextView status;
		public TextView time;
	}
}
