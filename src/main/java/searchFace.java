import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.rekognition.model.SearchFacesByImageRequest;
import com.amazonaws.services.rekognition.model.SearchFacesByImageResult;

/**
 * Created by jerem on 3/26/2019.
 */
public class searchFace {

    public static final String collectionId = "MyCollection";

    public static void main(String ...args) {

        String bucketName = "indentify-bucket";
        BasicAWSCredentials creds = new BasicAWSCredentials("AKIAIGYT2SIZS7MUOFCQ", "oRPFUCGUEjskOVitHzzD8fCHHp5zLgzAtefEaDFe");

        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard()
                .withRegion("us-east-1")
                .withCredentials(new AWSStaticCredentialsProvider(creds))
                .build();

        Image image=new Image()
                .withS3Object(new S3Object()
                        .withBucket(bucketName)
                        .withName("01349_1_F.png"));

        // Search collection for faces similar to the largest face in the image.
        SearchFacesByImageRequest searchFacesByImageRequest = new SearchFacesByImageRequest()
                .withCollectionId(collectionId)
                .withImage(image)
                .withFaceMatchThreshold(45F)
                .withMaxFaces(5);

        SearchFacesByImageResult searchFacesByImageResult =
                rekognitionClient.searchFacesByImage(searchFacesByImageRequest);
    }
}
