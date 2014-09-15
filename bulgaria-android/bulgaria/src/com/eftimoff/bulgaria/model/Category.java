package com.eftimoff.bulgaria.model;

public class Category {

	private String name;
	private boolean checked;

	public Category(String name, boolean checked) {
		this.name = name;
		this.checked = checked;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
