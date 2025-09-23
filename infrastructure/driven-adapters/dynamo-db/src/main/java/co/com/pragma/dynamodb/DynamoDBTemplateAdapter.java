package co.com.pragma.dynamodb;

import co.com.pragma.dynamodb.helper.TemplateAdapterOperations;
import co.com.pragma.model.errores.ErrorPersistencia;
import co.com.pragma.model.reporte.Reporte;
import co.com.pragma.model.reporte.gateways.ReporteRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.List;
import java.util.Set;

@Slf4j
@Repository
public class DynamoDBTemplateAdapter extends TemplateAdapterOperations<Reporte, String, ModelEntity> implements ReporteRepository {

    public DynamoDBTemplateAdapter(DynamoDbEnhancedAsyncClient connectionFactory, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(connectionFactory, mapper, d -> mapper.map(d, Reporte.class), "reportes");
    }

    public Mono<List<Reporte>> getEntityBySomeKeys(String partitionKey, String sortKey) {
        QueryEnhancedRequest queryExpression = generateQueryExpression(partitionKey, sortKey);
        return query(queryExpression);
    }

    public Mono<List<Reporte>> getEntityBySomeKeysByIndex(String partitionKey, String sortKey) {
        QueryEnhancedRequest queryExpression = generateQueryExpression(partitionKey, sortKey);
        return queryByIndex(queryExpression, "id");
    }

    private QueryEnhancedRequest generateQueryExpression(String partitionKey, String sortKey) {
        return QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(Key.builder().partitionValue(partitionKey).build()))
                .queryConditional(QueryConditional.sortGreaterThanOrEqualTo(Key.builder().sortValue(sortKey).build()))
                .build();
    }

    @Override
    public Mono<List<Reporte>> obtenerReportes() {
        return super.scan()
                .flatMap(reportes -> {
                    return reportes.isEmpty()
                            ? Mono.defer(()-> Mono.error(new ErrorPersistencia("Se presento un error al obtener los resportes", Set.of("No se encontraron reportee"))))
                            : Mono.just(reportes);
                });
    }

    @Override
    public Mono<Reporte> guardar(Reporte reporte) {
        return super.save(reporte)
                .onErrorResume(e -> {
                    log.error("Se presento un error al guardar reporte : {}", e.getMessage());
                    return Mono.defer(() ->Mono.error(new ErrorPersistencia("Se presento un error al guardar el reporte.", Set.of(e.getMessage()))));
                });
    }
}
