package my.service;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spark.SparkLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

import java.util.Random;

import static spark.Spark.before;
import static spark.Spark.get;


public class StreamLambdaHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {

    private boolean initialized = false;
    private MyRepository myRepository = new MyRepository();
    private Random random = new Random(System.currentTimeMillis());

    private SparkLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler =
            SparkLambdaContainerHandler.getAwsProxyHandler();

    public StreamLambdaHandler() throws ContainerInitializationException {
    }

    public AwsProxyResponse handleRequest(AwsProxyRequest awsProxyRequest, Context context) {
        if (!initialized) {
            defineRoutes();
            initialized = true;
        }
        return handler.proxy(awsProxyRequest, context);
    }

    private void defineRoutes() {
        Gson gson = new Gson();
        before((request, response) -> response.type("application/json"));
        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
        before((request, response) -> response.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT"));
        get("/add", (req, res) -> addSample(), gson::toJson);
        get("/", (req, res) -> myRepository.listSamples(), gson::toJson);
    }

    private MySampleData addSample() {
        MySampleData newSampleDate = new MySampleData(System.currentTimeMillis(), random.nextLong());
        myRepository.storeData(newSampleDate);
        return newSampleDate;
    }

}