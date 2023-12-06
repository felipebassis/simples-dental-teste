package br.com.simplesdental.entity.contato;

import br.com.simplesdental.entity.Result;
import br.com.simplesdental.entity.ResultSet;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
@AllArgsConstructor
class ContatoCustomRepositoryImpl implements ContatoCustomRepository {

    private final EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<ResultSet> findAllByFields(String filter, List<String> fields) {
        var parameters = "*";
        if (!fields.isEmpty()) {
            parameters = String.join("c.", fields);
        }
        if (!StringUtils.hasText(filter)) {
            filter = "";
        }
        var query = """
                SELECT %s FROM Contato c
                WHERE c.nome LIKE CONCAT('%%',:filter, '%%')
                OR c.contato LIKE CONCAT('%%',:filter, '%%')
                  """.formatted(parameters);

        return entityManager.createQuery(query)
                .setParameter("filter", filter)
                .getResultList()
                .stream()
                .map(result -> {
                    if (result instanceof Object[]) {
                        return toResultSet(fields, (Object[]) result);
                    } else {
                        return toResultSet(fields, result);
                    }
                })
                .toList();

    }

    private ResultSet toResultSet(List<String> fields, Object[] result) {
        var fieldsIterator = fields.iterator();
        var resultIterator = Arrays.asList(result).iterator();
        var resultList = new ArrayList<Result>();
        while (fieldsIterator.hasNext() || resultIterator.hasNext()) {
            resultList.add(new Result(fieldsIterator.next(), resultIterator.next().toString()));
        }
        return new ResultSet(resultList);
    }

    private ResultSet toResultSet(List<String> fields, Object result) {
        return new ResultSet(List.of(new Result(fields.get(0), result.toString())));
    }
}
