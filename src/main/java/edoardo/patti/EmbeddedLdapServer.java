package edoardo.patti;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldif.LDIFReader;
import java.io.File;
import java.io.IOException;

class EmbeddedLdapServer {

  static InMemoryDirectoryServer getInstance() throws LDAPException, IOException {

    var config = new InMemoryDirectoryServerConfig("dc=root,dc=app");

    var username = System.getProperty("username") != null
        ? System.getProperty("username") : "uid=application, ou=employees, dc=root, dc=app";

    var password = System.getProperty("password") != null
        ? System.getProperty("password") : "password123";

    var port = System.getProperty("port") != null
        ? Integer.parseInt(System.getProperty("port")) : 389;

    var ldifStream = EmbeddedLdapServer.class.getClassLoader().getResourceAsStream("initial.ldif");

    var ldif = System.getProperty("ldif") != null
        ? new LDIFReader(new File(System.getProperty("ldif"))) : new LDIFReader(ldifStream);

    config.addAdditionalBindCredentials(username, password);

    config.setListenerConfigs(new InMemoryListenerConfig(
        "server-listener", null, port, null,
        null, null));

    var server = new InMemoryDirectoryServer(config);
    server.importFromLDIF(true, ldif);
    System.out.println(
        "starting Server on port: " + port
            + "\nwith DN: " + username
            + "\npassword: " + password
            + "\ninitial ldif location: " + ldif.getRelativeBasePath()
    );
    return server;
  }
}