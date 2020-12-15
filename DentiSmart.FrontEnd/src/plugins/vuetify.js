import Vue from "vue";
import Vuetify, { VSnackbar, VBtn, VIcon } from "vuetify/lib";
import VuetifyToast from "vuetify-toast-snackbar-ng";
import { LoadingPlugin } from "vuetify-loading-overlay";
import DatetimePicker from "vuetify-datetime-picker";
import GoogleIcon from "@/components/google.vue";
Vue.use(Vuetify, {
  components: {
    VSnackbar,
    VBtn,
    VIcon
  }
});
Vue.use(LoadingPlugin, {
  //props
  spinnerProps: {},
  overlayProps: {}
});
Vue.use(VuetifyToast, {
  x: "right", // default
  y: "top" // default
});
Vue.use(DatetimePicker);
// Translation provided by Vuetify (javascript)
import es from "vuetify/es5/locale/es";
Vue.component("my-component", {
  methods: {
    changeLocale() {
      this.$vuetify.lang.current = "es";
    }
  }
});
export default new Vuetify({
  lang: {
    locales: { es },
    current: "es"
  },
  icons: {
    values: {
      custom: {
        // name of our custom icon
        component: GoogleIcon // our custom component
      }
    }
  },
  theme: {
    themes: {
      light: {
        primary: "#3FA7DB",
        secondary: "#b0bec5",
        accent: "#00ACE6",
        error: "#b71c1c",
        azulSecundario: "#00ACE6",
        grey: "#718792"
      },
      dark: {
        azulSecundario: "#00ACE6",
        grey: "#37474f"
      }
    }
  }
});
