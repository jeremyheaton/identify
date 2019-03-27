import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.IOException;
import java.util.List;

/**
 * Created by jerem on 3/26/2019.
 */
public class trainer {


    public static void main(String[] args) throws IOException {

        String photo = "input.jpg";
        String bucket = "bucket";

        String fileName = "C:\\Users\\jerem\\Downloads\\sd18\\sd18\\single\\f1_p0\\";
        String bucketName = "indentify-bucket";
        BasicAWSCredentials creds = new BasicAWSCredentials("AKIAIGYT2SIZS7MUOFCQ", "oRPFUCGUEjskOVitHzzD8fCHHp5zLgzAtefEaDFe");

        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard()
                .withRegion("us-east-1")
                .withCredentials(new AWSStaticCredentialsProvider(creds))
                .build();

        String collectionId = "MyCollection";
        System.out.println("Creating collection: " +
                collectionId );

        CreateCollectionRequest request2 = new CreateCollectionRequest()
                .withCollectionId(collectionId);

        CreateCollectionResult createCollectionResult = rekognitionClient.createCollection(request2);
        System.out.println("CollectionArn : " +
                createCollectionResult.getCollectionArn());
        System.out.println("Status code : " +
                createCollectionResult.getStatusCode().toString());

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion("us-east-1")
                .withCredentials(new AWSStaticCredentialsProvider(creds))
                .build();

        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(bucketName);
        ObjectListing objectListing = s3Client.listObjects(listObjectsRequest);

        for (S3ObjectSummary summary : objectListing.getObjectSummaries()) {
            DetectLabelsRequest request = new DetectLabelsRequest()
                    .withImage(new Image()
                            .withS3Object(new S3Object()
                                    .withName(photo).withBucket(bucket)))
                    .withMaxLabels(10)
                    .withMinConfidence(75F);

            try {
                DetectLabelsResult result = rekognitionClient.detectLabels(request);
                List<Label> labels = result.getLabels();

                System.out.println("Detected labels for " + photo);
                for (Label label : labels) {
                    System.out.println(label.getName() + ": " + label.getConfidence().toString());
                }
            } catch (AmazonRekognitionException e) {
                e.printStackTrace();
            }
        }

    }
}
