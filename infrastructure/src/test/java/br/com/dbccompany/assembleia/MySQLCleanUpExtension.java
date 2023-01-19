package br.com.dbccompany.assembleia;

import br.com.dbccompany.assembleia.infrastructure.agenda.persistence.AgendaRepository;
import br.com.dbccompany.assembleia.infrastructure.associate.persistence.AssociateRepository;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.List;

public class MySQLCleanUpExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(final ExtensionContext context) {
        final var appContext = SpringExtension.getApplicationContext(context);

        cleanUp(List.of(
                appContext.getBean(AgendaRepository.class),
                appContext.getBean(AssociateRepository.class)
        ));
        SpringExtension.getApplicationContext(context)
                .getBeansOfType(CrudRepository.class)
                .values()
                .forEach(CrudRepository::deleteAll);
    }

    private void cleanUp(final Collection<CrudRepository> repositories) {
        repositories.forEach(CrudRepository::deleteAll);
    }
}
