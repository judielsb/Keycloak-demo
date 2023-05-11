package game.sandbox.keycloak.builtinwalletauthenticator;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.authentication.ConfigurableAuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.ArrayList;
import java.util.List;

public class BuiltInWalletAuthenticatorFactory extends AbstractBlockchainAuthenticatorFactory implements AuthenticatorFactory, ConfigurableAuthenticatorFactory {
    private static final String PROVIDER_ID = "simple-public-key-authenticator";

    public static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED,
            AuthenticationExecutionModel.Requirement.ALTERNATIVE,
//            AuthenticationExecutionModel.Requirement.CONDITIONAL,
            AuthenticationExecutionModel.Requirement.DISABLED
    };
    public static final String METAMASK_TYPE = "METAMASK";
    public static final String CREATE_ETH_SSO_SESSION = "createEthSsoSession";
    protected static final List<ProviderConfigProperty> CONFIG_PROPERTIES = new ArrayList<ProviderConfigProperty>();

    static {
        CONFIG_PROPERTIES.add(
                new ProviderConfigProperty(
                        "walletType",
                        "Wallet type to use for user authentication",
                        "",
                        ProviderConfigProperty.LIST_TYPE,
                        METAMASK_TYPE,
                        METAMASK_TYPE
                ));
    }

    public String getId() {
        return PROVIDER_ID;
    }

    public String getDisplayType() {
        return "Blockchain Wallet Authenticator";
    }

    public String getHelpText() {
        return "A Wallet based blockchain Authenticator ";
    }

    public String getReferenceCategory() {
        return "Blockchain";
    }

    public boolean isConfigurable() {
        return true;
    }

    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    public boolean isUserSetupAllowed() {
        return false;
    }

    public Authenticator create(KeycloakSession keycloakSession) {
        return BuiltInWalletAuthenticator.SINGLETON;
    }

    public List<ProviderConfigProperty> getConfigProperties() {
        return CONFIG_PROPERTIES;
    }

    public void init(Config.Scope scope) {
        // NOOP
    }

    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {
        // NOOP
    }

    public void close() {
        // NOOP
    }


}
