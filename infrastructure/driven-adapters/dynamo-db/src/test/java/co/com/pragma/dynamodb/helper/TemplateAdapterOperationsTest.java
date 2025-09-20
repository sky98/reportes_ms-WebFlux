package co.com.pragma.dynamodb.helper;

import co.com.pragma.dynamodb.DynamoDBTemplateAdapter;
import co.com.pragma.dynamodb.ModelEntity;
import co.com.pragma.model.reporte.Reporte;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TemplateAdapterOperationsTest {

    @Mock
    private DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private DynamoDbAsyncTable<ModelEntity> customerTable;

    private ModelEntity modelEntity;

    private Reporte reporte = Reporte.builder()
            .id("123456")
            .cantidad(1)
            .monto(BigDecimal.TEN)
            .build();

    @BeforeEach
    void setUp() {
        when(dynamoDbEnhancedAsyncClient.table(any(String.class), any(TableSchema.class)))
                .thenReturn(customerTable);

        modelEntity = new ModelEntity();
        modelEntity.setId("id");
        modelEntity.setCantidad(1);
    }

    @Test
    @DisplayName("Debe guardar un reporte exitosamente")
    void testSave() {
        DynamoDBTemplateAdapter dynamoDBTemplateAdapter =
                new DynamoDBTemplateAdapter(dynamoDbEnhancedAsyncClient, mapper);

        when(mapper.map(reporte, ModelEntity.class)).thenReturn(modelEntity);
        when(customerTable.putItem(modelEntity)).thenReturn(CompletableFuture.completedFuture(null));

        Mono<Reporte> result = dynamoDBTemplateAdapter.guardar(reporte);

        StepVerifier.create(result)
                .expectNext(reporte)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe obtener un reporte por ID exitosamente")
    void testGetById() {
        DynamoDBTemplateAdapter dynamoDBTemplateAdapter =
                new DynamoDBTemplateAdapter(dynamoDbEnhancedAsyncClient, mapper);

        String id = "id";
        when(customerTable.getItem(any(Key.class))).thenReturn(CompletableFuture.completedFuture(modelEntity));
        when(mapper.map(any(ModelEntity.class), any())).thenReturn(reporte);

        Mono<Reporte> result = dynamoDBTemplateAdapter.getById(id);

        StepVerifier.create(result)
                .expectNext(reporte)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe eliminar un reporte exitosamente")
    void testDelete() {
        DynamoDBTemplateAdapter dynamoDBTemplateAdapter =
                new DynamoDBTemplateAdapter(dynamoDbEnhancedAsyncClient, mapper);

        when(mapper.map(reporte, ModelEntity.class)).thenReturn(modelEntity);
        when(customerTable.deleteItem(modelEntity)).thenReturn(CompletableFuture.completedFuture(modelEntity));
        when(mapper.map(modelEntity, Reporte.class)).thenReturn(reporte);

        Mono<Reporte> result = dynamoDBTemplateAdapter.delete(reporte);

        StepVerifier.create(result)
                .expectNext(reporte)
                .verifyComplete();
    }
}