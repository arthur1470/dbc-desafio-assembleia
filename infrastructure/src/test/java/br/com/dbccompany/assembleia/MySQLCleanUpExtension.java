package br.com.dbccompany.assembleia;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class MySQLCleanUpExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(final ExtensionContext context) {
        SpringExtension.getApplicationContext(context)
                .getBeansOfType(CrudRepository.class)
                .values()
                .forEach(CrudRepository::deleteAll);
    }
}
