package com.wetongji_android.data;

public class Banner {

	public static final String DEFAULT_BG_COLOR = "#0cc2cb";

	private int Id;
	private String Title;
	private String BgColor;
	private String Publisher;
	private String Image;
	private String URL;
	private Object content;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getImage() {
		return Image;
	}

	public void setImage(String image) {
		Image = image;
	}

	public String getPublisher() {
		return Publisher;
	}

	public void setPublisher(String publisher) {
		Publisher = publisher;
	}

	public String getBgColor() {
		return BgColor;
	}

	public void setBgColor(String bgColor) {
		BgColor = bgColor;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

}
