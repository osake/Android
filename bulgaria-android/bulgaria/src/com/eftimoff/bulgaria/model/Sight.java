package com.eftimoff.bulgaria.model;

import java.util.List;

public class Sight {

	/* Fields */

	private String label;
	private String description;
	private String category;
	private String region;
	private String thumbnail;
	private List<String> photos;
	private boolean showNotification;
	private float coordinateX;
	private float coordinateY;

	/* Public methods */

	public List<String> getPhotos() {
		return photos;
	}

	public void setPhotos(List<String> photos) {
		this.photos = photos;
	}

	public float getCoordinateX() {
		return coordinateX;
	}

	public void setCoordinateX(float coordinateX) {
		this.coordinateX = coordinateX;
	}

	public float getCoordinateY() {
		return coordinateY;
	}

	public void setCoordinateY(float coordinateY) {
		this.coordinateY = coordinateY;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public boolean isShowNotification() {
		return showNotification;
	}

	public void setShowNotification(boolean showNotification) {
		this.showNotification = showNotification;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((region == null) ? 0 : region.hashCode());
		result = prime * result + (showNotification ? 1231 : 1237);
		result = prime * result
				+ ((thumbnail == null) ? 0 : thumbnail.hashCode());
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
		Sight other = (Sight) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (region == null) {
			if (other.region != null)
				return false;
		} else if (!region.equals(other.region))
			return false;
		if (showNotification != other.showNotification)
			return false;
		if (thumbnail == null) {
			if (other.thumbnail != null)
				return false;
		} else if (!thumbnail.equals(other.thumbnail))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Sight [label=" + label + ", description=" + description
				+ ", category=" + category + ", region=" + region
				+ ", thumbnail=" + thumbnail + ", showNotification="
				+ showNotification + "]";
	}

}
