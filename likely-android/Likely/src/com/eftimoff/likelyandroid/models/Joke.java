package com.eftimoff.likelyandroid.models;

public class Joke {

	private int id;
	private String text;
	private String category;
	private int backgroundColor;
	private float rating;
	private int likes;
	private int votes;
	private int seen;
	private String date;

	public enum Seen {
		NOT_SEEN(0), SEEN(1);

		private int number;

		private Seen(int number) {
			this.setNumber(number);
		}

		public int getNumber() {
			return number;
		}

		public void setNumber(int number) {
			this.number = number;
		}
	}

	public enum Likes {
		DONT_LIKE(0), LIKES(1);

		private int number;

		private Likes(int number) {
			this.setNumber(number);
		}

		public int getNumber() {
			return number;
		}

		public void setNumber(int number) {
			this.number = number;
		}
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the backgroundColor
	 */
	public int getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * @param backgroundColor
	 *            the backgroundColor to set
	 */
	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * @return the rating
	 */
	public float getRating() {
		return rating;
	}

	/**
	 * @param rating
	 *            the rating to set
	 */
	public void setRating(float rating) {
		this.rating = rating;
	}

	/**
	 * @return the likes
	 */
	public int getLikes() {
		return likes;
	}

	/**
	 * @param likes
	 *            the likes to set
	 */
	public void setLikes(int likes) {
		this.likes = likes;
	}

	/**
	 * @return the votes
	 */
	public int getVotes() {
		return votes;
	}

	/**
	 * @param votes
	 *            the votes to set
	 */
	public void setVotes(int votes) {
		this.votes = votes;
	}

	/**
	 * @return the seen
	 */
	public int getSeen() {
		return seen;
	}

	/**
	 * @param seen
	 *            the seen to set
	 */
	public void setSeen(int seen) {
		this.seen = seen;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + backgroundColor;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((date == null) ? 0 : getDate().hashCode());
		result = prime * result + id;
		result = prime * result + likes;
		result = prime * result + Float.floatToIntBits(rating);
		result = prime * result + seen;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + votes;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Joke other = (Joke) obj;
		if (backgroundColor != other.backgroundColor)
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (getDate() == null) {
			if (other.getDate() != null)
				return false;
		} else if (!getDate().equals(other.getDate()))
			return false;
		if (id != other.id)
			return false;
		if (likes != other.likes)
			return false;
		if (Float.floatToIntBits(rating) != Float.floatToIntBits(other.rating))
			return false;
		if (seen != other.seen)
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (votes != other.votes)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Joke [id=" + id + ", text=" + text + ", category=" + category
				+ ", backgroundColor=" + backgroundColor + ", rating=" + rating + ", likes="
				+ likes + ", votes=" + votes + ", seen=" + seen + ", date=" + date + "]";
	}
}
