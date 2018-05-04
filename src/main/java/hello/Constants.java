package hello;

public enum Constants {
	
	S3_BUCKET("caio-ps-recognition-service"),
	CONTENT_TYPE_TEMPLATE("image/%s"),
	KEY_TEMPLATE("%s.%s");
	
	private String value;

	private Constants(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

}
