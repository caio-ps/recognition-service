package hello;


import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import software.amazon.awssdk.core.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.CompareFacesMatch;
import software.amazon.awssdk.services.rekognition.model.CompareFacesRequest;
import software.amazon.awssdk.services.rekognition.model.CompareFacesResponse;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.S3Object;

@RestController
public class RecognitionController {

	@RequestMapping(path = "/detectLabelsFromStorage", method = RequestMethod.GET)
	public List<RecognitionLabel> detectLabelsFromStorage(@RequestParam(name = "imageName", defaultValue = "erro.png") String imageName) {

		RekognitionClient rekognition = RekognitionClient.builder()
				.region(Region.US_WEST_2)
				.build();
		
		DetectLabelsResponse detectLabelsResponse = 
				rekognition.detectLabels(
					DetectLabelsRequest.builder().image(
							Image.builder().s3Object(
									S3Object.builder()
									.bucket(Constants.S3_BUCKET.value())
									.name(imageName)
									.build())
							.build())
					.build());
		
		List<RecognitionLabel> labels = detectLabelsResponse.labels().stream()
				.map((label) -> new RecognitionLabel(label.name(), label.confidence())).collect(Collectors.toList());

		return labels;
		
	}
	
	@RequestMapping(path = "/matchFacesWithStorage", method = RequestMethod.POST)
	public CompareFacesResult detectFacesFromStorage(@RequestBody ImageUpload imageUpload) throws JsonProcessingException {

		RekognitionClient rekognition = RekognitionClient.builder()
				.region(Region.US_WEST_2)
				.build();
		
		CompareFacesResponse compareFacesResponse = 
				rekognition.compareFaces(
					CompareFacesRequest.builder()
						.sourceImage(
								Image.builder().bytes(
									ByteBuffer.wrap(Base64.getDecoder().decode(imageUpload.getContent())))
								.build())
						.targetImage(
							Image.builder().s3Object(
									S3Object.builder()
									.bucket(Constants.S3_BUCKET.value())
									.name(String.format(Constants.KEY_TEMPLATE.value(), imageUpload.getName(), imageUpload.getExtension()))
									.build())
							.build())
					.build());
		
		List<CompareFacesMatch> details = compareFacesResponse.faceMatches();
		
		if (details.size() != 1) {
			return new CompareFacesResult(0F);
		} else {
			return new CompareFacesResult(details.get(0).similarity());
		}
		
	}
	
	@RequestMapping(path = "/detectLabels", method = RequestMethod.POST)
	public List<RecognitionLabel> detectLabels(@RequestBody ImageUpload imageUpload) {

		RekognitionClient rekognition = RekognitionClient.builder()
				.region(Region.US_WEST_2)
				.build();
		
		DetectLabelsResponse detectLabelsResponse = 
				rekognition.detectLabels(
					DetectLabelsRequest.builder().image(
							Image.builder().bytes(
									ByteBuffer.wrap(Base64.getDecoder().decode(imageUpload.getContent())))
							.build())
					.build());
		
		List<RecognitionLabel> labels = detectLabelsResponse.labels().stream()
				.map((label) -> new RecognitionLabel(label.name(), label.confidence())).collect(Collectors.toList());

		return labels;
		
	}
}
