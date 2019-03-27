import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;

/**
 * Created by jerem on 3/26/2019.
 */
public class Upload {

    public static void main(String[] args) throws IOException {
        String fileName = "C:\\Users\\jerem\\Downloads\\sd18\\sd18\\single\\f1_p0\\";
        String bucketName = "indentify-bucket";
        BasicAWSCredentials creds = new BasicAWSCredentials("AKIAIGYT2SIZS7MUOFCQ", "oRPFUCGUEjskOVitHzzD8fCHHp5zLgzAtefEaDFe");


        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion("us-east-1")
                    .withCredentials(new AWSStaticCredentialsProvider(creds))
                    .build();

            ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                    .withBucketName(bucketName);
            ObjectListing objectListing;
            objectListing = s3Client.listObjects(listObjectsRequest);
            // Upload a text string as a new object.

            // Upload a file as a new object with ContentType and title specified.
            File file = new File(fileName);

            file.listFiles();

            for(File image : file.listFiles()) {
                if (image.getName().contains(".png")) {
                    PutObjectRequest request = new PutObjectRequest(bucketName, image.getName(), image );
                    ObjectMetadata metadata = new ObjectMetadata();
                    metadata.setContentType("plain/text");
                    metadata.addUserMetadata("x-amz-meta-title", image.getName());
                    request.setMetadata(metadata);
                    s3Client.putObject(request);
                }
            }

        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        }
        catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
    }
}
