# Camel example: S3 -> Kinesis -> Back to Camel

This project shows how to wire S3 and Kinesis by reading a CSV file from some S3 bucket, reading every line and then sending a corresponding JSON to Kinesis. 

## How to run it

```
./gradlew bootRun -Pargs=--aws.access-key=<AWS ACCESS KEY> --aws.secret-key=<AWS SECRET KEY> --aws.region=<AWS REGION> --s3.bucket=<AWS SOURCE BUCKET NAME> --kinesis.stream=<AWS KINESIS STREAM NAME>
```

Once the project starts up, if the credentials are correct and the bucket exists, Apache Camel will be waiting for a file on the bucket. This code has been written expecting a CSV file like the one contained in (src/main/resources/csv)[src/main/resources/csv]. Should you want to update it to your own needs (and you will surely want to), you will need to change the class [Record](src/main/java/com/github/alesaudate/camelexamples/Record.java) and the function `toJSON` that is contained in the bottom of the file [Routes.kt](src/main/kotlin/com/github/alesaudate/camelexamples/Routes.kt). 
