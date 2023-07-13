import Vue from 'vue'
import App from './App.vue'
import VueLogger from 'vuejs-logger';
import Keycloak from 'keycloak-js';

Vue.use(VueLogger);

let initOptions = {
  url: 'http://sandbox.local/auth', realm: 'keycloak-demo', clientId: 'app-vue', onLoad: 'login-required', checkLoginIframe: false
  //url: 'https://keycloak-dev.sandbox.game/', realm: 'keycloak-demo', clientId: 'app-vue', onLoad: 'login-required', checkLoginIframe: false
}

let keycloak = new Keycloak(initOptions);

keycloak.init({ onLoad: initOptions.onLoad }).then((auth) => {
  if (!auth) {
    window.location.reload();
  } else {
    Vue.$log.info("Authenticated");

    new Vue({
      el: '#app',
      render: h => h(App, { props: { keycloak: keycloak } })
    })
  }


//Token Refresh
  setInterval(() => {
    keycloak.updateToken(70).then((refreshed) => {
      if (refreshed) {
        Vue.$log.info('Token refreshed' + refreshed);
      } else {
        Vue.$log.warn('Token not refreshed, valid for '
          + Math.round(keycloak.tokenParsed.exp + keycloak.timeSkew - new Date().getTime() / 1000) + ' seconds');
      }
    }).catch(() => {
      Vue.$log.error('Failed to refresh token');
    });
  }, 6000)

}).catch(() => {
  Vue.$log.error("Authenticated Failed");
});


