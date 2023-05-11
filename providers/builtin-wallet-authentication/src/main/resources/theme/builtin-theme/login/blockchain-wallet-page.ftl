<#import "template-custom.ftl" as layout>
<@layout.registrationLayout; section>

    <#if section = "header">
            Login
    <#elseif section = "form">
        <script src="https://cdn.jsdelivr.net/npm/web3@1.5.2/dist/web3.min.js"></script>
        <source rel="stylesheet" src="https://metamask.io/css/metamask-staging-2.webflow.css"/>


        <form id="kc-wallet-form" class="${properties.kcFormClass!}" action="${url.loginAction}" method="post">
            <#if isAuthentication??>
                <input type="hidden" id="blockchain-data" name="blockchain-data"/>
                <input type="hidden" id="blockchain-sig" name="blockchain-sig"/>
            <#else>
                <input type="hidden" id="is-sso" name="is-sso" value="true"/>
            </#if>
            <input type="hidden" id="blockchain-address" name="blockchain-address"/>
            <div id="pluginOkDivs">
                <div class="${properties.kcFormGroupClass!}">
                    <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                        <!-- <input class="${properties.kcButtonClass!} ${properties.kcButtonDefaultClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}"
                               name="cancel" id="kc-cancel" type="submit" value="${msg("doCancel")}"/> -->
                        <#if isAuthentication??>
                            <div class="${properties.kcWalletButonClass!}">
                                <w3m-core-button></w3m-core-button>
                            </div>
                            
                            <!-- <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}"
                                   name="login" id="kc-login" type="button" value="Authenticate with MetaMask"
                                   onclick="javascript:authentication('${challenge}')"/> -->
                            
                        </#if>
                    </div>
                </div>
            </div>
        </form>

    <#elseif section = "socialProviders" >
        <#if realm.password && social.providers??>
            <div id="kc-social-providers" class="${properties.kcFormSocialAccountSectionClass!}">
                <hr/>
                <h4>${msg("identity-provider-login-label")}</h4>

                <ul class="${properties.kcFormSocialAccountListClass!} <#if social.providers?size gt 3>${properties.kcFormSocialAccountListGridClass!}</#if>">
                    <#list social.providers as p>
                        <a id="social-${p.alias}" class="${properties.kcFormSocialAccountListButtonClass!} <#if social.providers?size gt 3>${properties.kcFormSocialAccountGridItem!}</#if>"
                                type="button" href="${p.loginUrl}">
                            <#if p.iconClasses?has_content>
                                <i class="${properties.kcCommonLogoIdP!} ${p.iconClasses!}" aria-hidden="true"></i>
                                <span class="${properties.kcFormSocialAccountNameClass!} kc-social-icon-text">${p.displayName!}</span>
                            <#else>
                                <span class="${properties.kcFormSocialAccountNameClass!}">${p.displayName!}</span>
                            </#if>
                        </a>
                    </#list>
                </ul>
            </div>
        </#if>
    </#if>

</@layout.registrationLayout>
