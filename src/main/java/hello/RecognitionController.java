package hello;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import software.amazon.awssdk.core.auth.ProfileCredentialsProvider;
import software.amazon.awssdk.core.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.S3Object;

@RestController
public class RecognitionController {

	@RequestMapping(path = "/detectLabels", method = RequestMethod.GET)
	public DetectLabelsResponse test() {

		RekognitionClient rekognition = RekognitionClient.builder()
				.region(Region.US_WEST_2)
				.credentialsProvider(ProfileCredentialsProvider.builder()
						.profileName("adminuser")
						.build())
				.build();
		
		DetectLabelsResponse detectLabelsResponse = 
				rekognition.detectLabels(
					DetectLabelsRequest.builder().image(
							Image.builder().s3Object(
									S3Object.builder()
									.bucket("caio-ps-recognition-service")
									.name("test1.png")
									.build())
							.build())
					.build());

		return detectLabelsResponse;
		
	}
}
