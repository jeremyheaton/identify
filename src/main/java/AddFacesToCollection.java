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

import java.util.List;

/**
 * Created by jerem on 3/26/2019.
 */
public class AddFacesToCollection {
    public static final String collectionId = "MyCollection";
    public static final String photo = "input.jpg";

    public static void main(String[] args) throws Exception {

        String bucketName = "indentify-bucket";
        BasicAWSCredentials creds = new BasicAWSCredentials("AKIAIGYT2SIZS7MUOFCQ", "oRPFUCGUEjskOVitHzzD8fCHHp5zLgzAtefEaDFe");

        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard()
                .withRegion("us-east-1")
                .withCredentials(new AWSStaticCredentialsProvider(creds))
                .build();

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion("us-east-1")
                .withCredentials(new AWSStaticCredentialsProvider(creds))
                .build();

        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(bucketName);
        ObjectListing objectListing = s3Client.listObjects(listObjectsRequest);

//        for (S3ObjectSummary summary : objectListing.getObjectSummaries()) {
//            Image image = new Image()
//                    .withS3Object(new S3Object()
//                            .withBucket(bucketName)
//                            .withName(summary.getKey()));
//
//            IndexFacesRequest indexFacesRequest = new IndexFacesRequest()
//                    .withImage(image)
//                    .withQualityFilter(QualityFilter.AUTO)
//                    .withMaxFaces(1)
//                    .withCollectionId(collectionId)
//                    .withExternalImageId(summary.getKey())
//                    .withDetectionAttributes("DEFAULT");
//
//             rekognitionClient.indexFaces(indexFacesRequest);
//        }



        ListFacesResult indexFacesResult = rekognitionClient.listFaces(new ListFacesRequest().withCollectionId(collectionId));

        System.out.println("Faces indexed:");
        List<Face> faceRecords = indexFacesResult.getFaces();
        for (Face faceRecord : faceRecords) {
            System.out.println("  Face ID: " + faceRecord);
        }
    }
}