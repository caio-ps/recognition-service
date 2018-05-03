package hello;

public class RecognitionLabel {

	private String name;
	private Float confidence;

	public RecognitionLabel(String name, Float confidence) {
		super();
		this.name = name;
		this.confidence = confidence;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getConfidence() {
		return confidence;
	}

	public void setConfidence(Float confidence) {
		this.confidence = confidence;
	}

}
