package co.com.pragma.dynamodb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class ModelEntity {

    @Setter
    private String id;
    @Getter
    @Setter
    private int cantidad;
    @Getter
    @Setter
    private BigDecimal monto;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

}
