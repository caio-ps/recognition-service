package hello;

public class CompareFacesResult {

	private Float similarity;
	private Boolean samePerson;
	
	public CompareFacesResult(Float similarity) {
		super();
		this.similarity = similarity;
		this.samePerson = similarity > 85.0;
	}

	public Float getSimilarity() {
		return similarity;
	}

	public Boolean getSamePerson() {
		return samePerson;
	}

}
