package edoardo.patti;

import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchScope;
import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

  public static void main(String[] args) throws LDAPException, IOException {

    var server = EmbeddedLdapServer.getInstance();
    server.startListening();
    var conn = server.getConnection();

    conn.getEntry("uid=admin1, ou=employees, dc=root, dc=app");
    conn.search(new SearchRequest("dc=root,dc=app", SearchScope.SUB, "mail=admin1@root.app"));
    conn.close();

    System.out.println("server ready to use");
  }
}