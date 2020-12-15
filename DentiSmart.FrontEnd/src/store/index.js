import Vue from "vue";
import Vuex from "vuex";
import axios from "axios";
import { environment } from "../env/environment";
Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    status: "",
    token: localStorage.getItem("token") || "",
    user: JSON.parse(localStorage.getItem("usuario")) || ""
  },
  mutations: {
    auth_request(state) {
      state.status = "loading";
    },
    auth_success(state, token) {
      state.status = "success";
      state.token = token;
    },
    auth_user(state, user) {
      state.status = "success";
      state.user = user;
    },
    auth_error(state) {
      state.status = "error";
    },
    logout(state) {
      state.status = "";
      state.token = "";
    }
  },
  actions: {
    login({ commit }, user) {
      return new Promise((resolve, reject) => {
        commit("auth_request");
        axios({
          url: environment.url + "Login",
          data: user,
          method: "POST"
        })
          .then(resp => {
            const token = resp.data.token;
            const user = resp.data;
            localStorage.setItem("token", token);
            localStorage.setItem("usuario", JSON.stringify(user));
            axios.defaults.headers.common["Authorization"] = token;
            commit("auth_success", token);
            commit("auth_user", user);
            Vue.prototype.$http = axios;
            if (token) {
              Vue.prototype.$http.defaults.headers.common = {
                Authorization: `bearer ${token}`
              };
            }
            resolve(resp);
          })
          .catch(err => {
            commit("auth_error");
            localStorage.removeItem("token");
            reject(err);
          });
      });
    },
    loginFacebook({ commit }, user) {
      return new Promise((resolve, reject) => {
        commit("auth_request");
        axios({
          url: environment.url + "Login/facebook",
          data: user,
          method: "POST"
        })
          .then(resp => {
            const token = resp.data.token;
            const user = resp.data;
            localStorage.setItem("token", token);
            localStorage.setItem("usuario", JSON.stringify(user));
            axios.defaults.headers.common["Authorization"] = token;
            commit("auth_success", token);
            commit("auth_user", user);
            Vue.prototype.$http = axios;
            if (token) {
              Vue.prototype.$http.defaults.headers.common = {
                Authorization: `bearer ${token}`
              };
            }
            resolve(resp);
          })
          .catch(err => {
            commit("auth_error");
            localStorage.removeItem("token");
            reject(err);
          });
      });
    },
    loginGoogle({ commit }, user) {
      return new Promise((resolve, reject) => {
        commit("auth_request");
        axios({
          url: environment.url + "Login/Google",
          data: user,
          method: "POST"
        })
          .then(resp => {
            const token = resp.data.token;
            const user = resp.data;
            localStorage.setItem("token", token);
            localStorage.setItem("usuario", JSON.stringify(user));
            axios.defaults.headers.common["Authorization"] = token;
            commit("auth_success", token);
            commit("auth_user", user);
            Vue.prototype.$http = axios;
            if (token) {
              Vue.prototype.$http.defaults.headers.common = {
                Authorization: `bearer ${token}`
              };
            }
            resolve(resp);
          })
          .catch(err => {
            commit("auth_error");
            localStorage.removeItem("token");
            reject(err);
          });
      });
    },
    logout({ commit }) {
      return new Promise(resolve => {
        commit("logout");
        localStorage.removeItem("token");
        localStorage.removeItem("usuario");
        delete axios.defaults.headers.common["Authorization"];
        resolve();
      });
    }
  },
  modules: {},
  getters: {
    isLoggedIn: state => !!state.token,
    authStatus: state => state.status,
    role: state => state.user.role
  }
});
