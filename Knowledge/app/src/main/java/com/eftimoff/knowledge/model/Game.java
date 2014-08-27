package com.eftimoff.knowledge.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Game implements Parcelable {

	public enum GameStatus {
		COMPLETE, FAILED, PENDING, RUNNING, NOT_SPECIFIED;
	}

	public Game() {

	}

	private String id;
	private String startingDate;
	private String endDate;
	private GameStatus status = GameStatus.NOT_SPECIFIED;

	@Override
	public String toString() {
		return "Game{" +
				"id='" + id + '\'' +
				", startingDate='" + startingDate + '\'' +
				", endDate='" + endDate + '\'' +
				", status=" + status +
				'}';
	}

	public GameStatus getStatus() {
		return status;
	}

	public void setStatus(GameStatus status) {
		this.status = status;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(String startingDate) {
		this.startingDate = startingDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.startingDate);
		dest.writeString(this.endDate);
		dest.writeInt(this.status == null ? -1 : this.status.ordinal());
	}


	private Game(Parcel in) {
		this.id = in.readString();
		this.startingDate = in.readString();
		this.endDate = in.readString();
		int tmpStatus = in.readInt();
		this.status = tmpStatus == -1 ? null : GameStatus.values()[tmpStatus];
	}

	public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {
		public Game createFromParcel(Parcel source) {
			return new Game(source);
		}

		public Game[] newArray(int size) {
			return new Game[size];
		}
	};
}
