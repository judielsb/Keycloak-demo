package game.sandbox.keycloak.builtinwalletauthenticator;

import game.sandbox.keycloak.builtinwalletauthenticator.util.ChallengeUtils;
import org.jboss.logging.Logger;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.utils.StringUtil;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.security.SignatureException;
import java.util.Random;

public class BuiltInWalletAuthenticator implements Authenticator {
    public static final BuiltInWalletAuthenticator SINGLETON = new BuiltInWalletAuthenticator();
    public static final String BLOCKCHAIN_PAGE = "blockchain-wallet-page.ftl";
    public static final String BLOCKCHAIN_ADDRESS_CUSTOM_ATTRIBUTE = "wallet";

    private static final Random random = new Random();

    private static final Logger LOG = Logger.getLogger(BuiltInWalletAuthenticator.class);

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        LOG.info("####### authenticate");
        String error = "";
        Response challengeResponse = null;

        if (StringUtil.isNotBlank(error)) {
            challengeResponse = this.challenge(context, error);
        } else {
            challengeResponse = this.challenge(context, new MultivaluedMapImpl<>());
        }
        context.challenge(challengeResponse);
    }


    @Override
    public void action(AuthenticationFlowContext context) {
        LOG.info("####### action");

        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();

        if (formData.containsKey("cancel")) {
            context.cancelLogin();
            return;
        }

        final String ethData = formData.getFirst("blockchain-data");
        final String ethSig = formData.getFirst("blockchain-sig");
        String ethAddress = formData.getFirst("blockchain-address");

        try {
            ChallengeUtils.recover(ethData, ethSig, ethAddress);
        } catch (SignatureException e) {
            Response challengeResponse = this.challenge(context, "unableToRecover");
            context.challenge(challengeResponse);
            context.attempted();
            return;
        }

        UserModel userModel = context.getSession().users()
                .searchForUserByUserAttributeStream(context.getRealm(), BLOCKCHAIN_ADDRESS_CUSTOM_ATTRIBUTE, ethAddress)
                .findFirst()
                .orElse(null);
        LOG.info("aca");
        context.setUser(userModel);
        context.success();

        if (userModel == null) {
            LOG.info("user is null");
            context.attempted();
            return;
        }
        LOG.infof("user logged in %s", userModel.getEmail());
        return;
    }


    @Override
    public boolean requiresUser() {
        return false;
    }

    @Override
    public boolean configuredFor(KeycloakSession keycloakSession, RealmModel realmModel, UserModel user) {
        if (user != null) {
            final String userBlockchainAddress = user.getAttributeStream(BLOCKCHAIN_ADDRESS_CUSTOM_ATTRIBUTE).findFirst().orElse("");
            return StringUtil.isNotBlank(userBlockchainAddress);
        }
        return false;
    }

    @Override
    public void setRequiredActions(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {

    }

    @Override
    public void close() {

    }

    protected Response challenge(AuthenticationFlowContext context, MultivaluedMap<String, String> formData) {
        LoginFormsProvider forms = context.form();
        if (formData.size() > 0) {
            forms.setFormData(formData);
        }
        forms.setAttribute("isAuthentication", true);
        forms.setAttribute("challenge", Integer.toHexString(random.nextInt()));
        return forms.createForm(BLOCKCHAIN_PAGE);
    }

    protected Response challenge(AuthenticationFlowContext context, String error, Object... parameters) {
        LoginFormsProvider form = context.form();
        if (error != null) {
            form.setError(error, parameters);
        }
        form.setAttribute("isAuthentication", true);
        form.setAttribute("challenge", Integer.toHexString(random.nextInt()));
        return form.createForm(BLOCKCHAIN_PAGE);
    }

}
