package example;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.Modification;
import com.unboundid.ldap.sdk.ModificationType;
import com.unboundid.ldap.sdk.Attribute;
import com.unboundid.ldif.LDIFReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class LdifImportService {

    private final InMemoryDirectoryServer directoryServer;

    public LdifImportService() throws LDAPException {
        InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig("dc=test,dc=bpab,dc=internal");
        config.setListenerConfigs(InMemoryListenerConfig.createLDAPConfig("default", 0));
        directoryServer = new InMemoryDirectoryServer(config);
        directoryServer.startListening();
    }

    @jakarta.annotation.PostConstruct
    public void importLdif() throws IOException, LDAPException {
        // Добавление атрибута 'department' в схему
        // String attributeType = "( 1.3.6.1.4.1.1466.115.121.1.11 NAME 'department' DESC 'Department' EQUALITY caseIgnoreMatch SUBSTR caseIgnoreSubstringsMatch SYNTAX 1.3.6.1.4.1.1466.115.121.1.15 )";

        // Modification mod = new Modification(ModificationType.ADD, "attributeTypes", attributeType);
        // directoryServer.modify("cn=schema", mod);

        // Импорт LDIF файла
        ClassPathResource resource = new ClassPathResource("users.ldif");
        try (InputStream inputStream = resource.getInputStream()) {
            LDIFReader ldifReader = new LDIFReader(inputStream);
            directoryServer.importFromLDIF(true, ldifReader);
        }
    }

    public int getPort() {
        return directoryServer.getListenPort();
    }
}