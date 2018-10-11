package my.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@DynamoDBTable(tableName = "MySampleData")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MySampleData {
    @DynamoDBHashKey
    private long time = 0;
    private long number;
}
