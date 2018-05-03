package hello;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import software.amazon.awssdk.core.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.S3Object;

@RestController
public class RecognitionController {

	@RequestMapping(path = "/detectLabels", method = RequestMethod.GET)
	public List<RecognitionLabel> test(@RequestParam(name = "imageName", defaultValue = "erro.png") String imageName) {

		RekognitionClient rekognition = RekognitionClient.builder()
				.region(Region.US_WEST_2)
				.build();
		
		DetectLabelsResponse detectLabelsResponse = 
				rekognition.detectLabels(
					DetectLabelsRequest.builder().image(
							Image.builder().s3Object(
									S3Object.builder()
									.bucket("caio-ps-recognition-service")
									.name(imageName)
									.build())
							.build())
					.build());
		
		List<RecognitionLabel> labels = detectLabelsResponse.labels().stream()
				.map((label) -> new RecognitionLabel(label.name(), label.confidence())).collect(Collectors.toList());

		return labels;
		
	}
}
