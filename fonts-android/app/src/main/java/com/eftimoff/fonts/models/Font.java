package com.eftimoff.fonts.models;

import java.util.List;
import java.util.Map;

public class Font {
	private String family;
	private String category;
	private List<String> variants;
	private List<String> subsets;
	private String version;
	private String lastModified;
	private Map<String, String> files;

	public Map<String, String> getFiles() {
		return files;
	}

	public void setFiles(Map<String, String> files) {
		this.files = files;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<String> getSubsets() {
		return subsets;
	}

	public void setSubsets(List<String> subsets) {
		this.subsets = subsets;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<String> getVariants() {
		return variants;
	}

	public void setVariants(List<String> variants) {
		this.variants = variants;
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

}
