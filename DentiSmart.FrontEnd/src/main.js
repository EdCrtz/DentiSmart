import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import vuetify from "./plugins/vuetify";
import Axios from "axios";
import jwt_decode from "jwt-decode";
Vue.config.productionTip = false;
vuetify.framework.theme.dark = localStorage.getItem("dark") == "true";
new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount("#app");
Vue.prototype.$http = Axios;
const token = localStorage.getItem("token");
if (token) {
  Vue.prototype.$http.defaults.headers.common = {
    Authorization: `bearer ${token}`
  };
}
if (token && jwt_decode(token).exp < Date.now() / 1000) {
  localStorage.removeItem("token");
  localStorage.removeItem("usuario");
  router.go();
}
