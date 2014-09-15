package com.eftimoff.fonts.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class AlertDialogFragment extends DialogFragment {

	private String mMessage;
	private String mPositive;
	private String mNegative;
	private DialogInterface.OnClickListener mPositiveOnClickListener;
	private DialogInterface.OnClickListener mNegativeOnClickListener;

	public AlertDialogFragment() {
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(mMessage)
				.setPositiveButton(mPositive, mPositiveOnClickListener)
				.setNegativeButton(mNegative, mNegativeOnClickListener);
		return builder.create();
	}

	public static class Builder {

		private String mMessage;
		private String mPositive;
		private String mNegative;
		private DialogInterface.OnClickListener mPositiveOnClickListener;
		private DialogInterface.OnClickListener mNegativeOnClickListener;

		public Builder message(final String message) {
			mMessage = message;
			return this;
		}

		public Builder positive(final String positive) {
			mPositive = positive;
			return this;
		}

		public Builder negative(final String negative) {
			mNegative = negative;
			return this;
		}

		public Builder positiveClickListener(final DialogInterface.OnClickListener clickListener) {
			mPositiveOnClickListener = clickListener;
			return this;
		}

		public Builder negativeClickListener(final DialogInterface.OnClickListener clickListener) {
			mNegativeOnClickListener = clickListener;
			return this;
		}

		public AlertDialogFragment build() {
			AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
			alertDialogFragment.mMessage = this.mMessage;
			alertDialogFragment.mPositive = this.mPositive;
			alertDialogFragment.mNegative = this.mNegative;
			alertDialogFragment.mPositiveOnClickListener = this.mPositiveOnClickListener;
			alertDialogFragment.mNegativeOnClickListener = this.mNegativeOnClickListener;
			return alertDialogFragment;
		}

	}
}