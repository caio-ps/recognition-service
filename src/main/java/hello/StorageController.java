package hello;


import java.util.Base64;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import software.amazon.awssdk.core.regions.Region;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@RestController
public class StorageController {

	@RequestMapping(path = "/uploadImage", method = RequestMethod.POST)
	public Object test(@org.springframework.web.bind.annotation.RequestBody ImageUpload imageUpload) {

		S3Client s3 = S3Client.builder()
				.region(Region.US_WEST_2)
				.build();
		
		s3.putObject(
				PutObjectRequest.builder()
					.bucket(Constants.S3_BUCKET.value())
					.contentType(String.format(Constants.CONTENT_TYPE_TEMPLATE.value(), imageUpload.getExtension()))
					.contentEncoding("base64")
					.key(String.format(Constants.KEY_TEMPLATE.value(), imageUpload.getName(), imageUpload.getExtension()))
					.build()
				, RequestBody.of(Base64.getDecoder().decode(imageUpload.getContent())));

		return null;
		
	}
}
