<template>
  <v-app>
    <v-navigation-drawer v-model="drawer" v-if="isLoggedIn" temporary app>
      <v-toolbar color="azulSecundario" dark flat>
        <v-btn icon @click.stop="drawer = !drawer">
          <v-icon>mdi-arrow-left</v-icon>
        </v-btn>
      </v-toolbar>
      <v-list nav dense>
        <v-subheader>Menu</v-subheader>
        <v-list-item-group
          active-class="blue--text text--accent-4"
          v-for="(opcion, i) in menu"
          v-bind:key="i"
        >
          <v-list-item
            :to="opcion.ruta"
            v-if="opcion.pemitido.includes(user.role)"
            link
          >
            <v-list-item-icon>
              <v-icon>{{ opcion.icono }}</v-icon>
            </v-list-item-icon>
            <v-list-item-title>{{ opcion.nombre }}</v-list-item-title>
          </v-list-item>
        </v-list-item-group>
      </v-list>
      <v-list dense nav>
        <v-divider></v-divider>
        <v-list-item @click="logout()">
          <v-list-item-icon>
            <v-icon>mdi-logout</v-icon>
          </v-list-item-icon>
          <v-list-item-title>Cerrar Sesion</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar color="#3FA7DB" dark v-if="isLoggedIn" app>
      <v-app-bar-nav-icon @click="drawer = true"></v-app-bar-nav-icon>
      <router-link to="/inicio">
        <v-toolbar-title class="font-weight-bold icono"
          >DentiSmart
          <v-icon style="margin-left:5px;">mdi-tooth</v-icon>
        </v-toolbar-title>
      </router-link>

      <v-spacer></v-spacer>

      <template>
        <v-menu bottom min-width="200px" rounded offset-y>
          <template v-slot:activator="{ on }">
            <v-btn icon x-large v-on="on">
              <v-avatar :color="!user.foto ? 'orange' : ''" size="45">
                <span v-if="!user.foto" class="white--text headline">{{
                  iniciales
                }}</span>
                <v-img v-if="user.foto" :src="user.foto" />
              </v-avatar>
            </v-btn>
          </template>
          <v-card>
            <v-list-item-content class="justify-center">
              <div class="mx-auto text-center">
                <v-avatar :color="!user.foto ? 'orange' : ''" size="45">
                  <span v-if="!user.foto" class="white--text headline">{{
                    iniciales
                  }}</span>
                  <v-img v-if="user.foto" :src="user.foto" />
                </v-avatar>
                <h4>
                  {{ `${user.nombre} ${user.apellidoPat} ${user.apellidoMat}` }}
                </h4>
                <p class="caption mt-1">
                  {{ user.email }}
                </p>
                <v-divider></v-divider>
                <v-switch
                  hide-details
                  v-model="$vuetify.theme.dark"
                  class="labelswitch"
                  label="MODO NOCTURNO"
                  @click="darkMode"
                >
                </v-switch>
                <v-divider></v-divider>
                <v-btn small to="/perfil" depressed rounded text>
                  Editar Perfil
                </v-btn>
                <v-divider></v-divider>
                <v-btn small @click="logout()" depressed rounded text>
                  Cerrar sesion
                </v-btn>
              </div>
            </v-list-item-content>
          </v-card>
        </v-menu>
      </template>
    </v-app-bar>

    <v-main>
      <router-view></router-view>
    </v-main>

    <!-- 
    <v-footer app> </v-footer> -->
  </v-app>
</template>

<script>
import { mapState } from "vuex";
export default {
  name: "App",

  components: {},

  data: () => ({
    drawer: false,
    menu: [
      {
        nombre: "Inicio",
        icono: "mdi-tooth",
        ruta: "/inicio",
        pemitido: ["ADMIN", "Paciente", "Dentista"]
      },
      {
        nombre: "Inventario",
        icono: "mdi-dolly",
        ruta: "/inventario",
        pemitido: ["ADMIN", "Dentista"]
      },
      {
        nombre: "Citas",
        icono: "mdi-calendar-text",
        ruta: "/citas",
        pemitido: ["ADMIN", "Paciente", "Dentista"]
      },
      {
        nombre: "Reportes",
        icono: "mdi-chart-line",
        ruta: "/reportes",
        pemitido: ["ADMIN", "Dentista"]
      },
      {
        nombre: "Catalogo de Productos",
        icono: "mdi-basket",
        ruta: "/productos",
        pemitido: ["ADMIN", "Dentista"]
      },
      {
        nombre: "Catalogo de Servicios",
        icono: "mdi-file-document-edit-outline",
        ruta: "/servicios",
        pemitido: ["ADMIN", "Dentista"]
      },
      {
        nombre: "Dentistas",
        icono: "mdi-toothbrush-paste",
        ruta: "/dentistas",
        pemitido: ["ADMIN"]
      },
      {
        nombre: "Consultorios",
        icono: "mdi-home-city-outline",
        ruta: "/consultorios",
        pemitido: ["ADMIN"]
      },
      {
        nombre: "Pacientes",
        icono: "mdi-account-edit",
        ruta: "/pacientes",
        pemitido: ["ADMIN", "Dentista"]
      }
    ],
    titulo: ""
  }),
  computed: {
    ...mapState(["user"]),
    isLoggedIn: function() {
      return this.$store.getters.isLoggedIn && this.$route.path != "/";
    },
    iniciales() {
      return this.user.nombre || this.user.apellidoPat
        ? this.user.nombre.charAt(0) + this.user.apellidoPat.charAt(0)
        : "";
    }
  },
  methods: {
    darkMode() {
      localStorage.setItem("dark", this.$vuetify.theme.dark);
    },
    logout() {
      this.$vloading.show();
      this.$store
        .dispatch("logout")
        .then(() => {
          this.$vloading.hide();
          this.$router.push("/");
        })
        .catch(() => {
          this.$vloading.hide();
        });
    }
  }
};
</script>
<style>
.labelswitch > .v-input__control > .v-input__slot > .v-label {
  font-size: 13px !important;
  margin-right: 44px !important;
  display: inline-grid !important;
  text-align: center;
}
.labelswitch {
  margin-top: 0px !important;
}
.icono {
  color: white !important;
  text-decoration: none !important;
}
</style>
