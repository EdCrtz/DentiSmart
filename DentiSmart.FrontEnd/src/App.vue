<template>
  <v-app>
    <v-navigation-drawer v-model="drawer" temporary app>
      <v-list nav dense>
        <v-subheader>Menu</v-subheader>
        <v-list-item-group active-class="blue--text text--accent-4">
          <v-list-item
            v-for="(opcion, i) in menu"
            v-bind:key="i"
            :to="opcion.ruta"
          >
            <v-list-item-icon>
              <v-icon>{{ opcion.icono }}</v-icon>
            </v-list-item-icon>
            <v-list-item-title>{{ opcion.nombre }}</v-list-item-title>
          </v-list-item>
        </v-list-item-group>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar
      color="#3FA7DB"
      prominent
      shrink-on-scroll
      scroll-threshold="50"
      fade-img-on-scroll
      dark
      v-if="isAuthenticated"
      src="./assets/fondo.png"
      app
    >
      <v-app-bar-nav-icon @click="drawer = true"></v-app-bar-nav-icon>
      <v-spacer></v-spacer>
      <v-toolbar-title class="font-weight-bold"
        >Portal de dentista</v-toolbar-title
      >
    </v-app-bar>

    <v-main>
      <v-container fluid v-bind:class="{ 'pa-0': !isAuthenticated }">
        <router-view></router-view>
      </v-container>
    </v-main>
    <!-- 
    <v-footer app> </v-footer> -->
  </v-app>
</template>

<script>
export default {
  name: "App",

  components: {},

  data: () => ({
    drawer: false,
    menu: [
      {
        nombre: "Inicio",
        icono: "mdi-tooth",
        ruta: "home"
      },
      {
        nombre: "Catalogo de Productos",
        icono: "mdi-border-color",
        ruta: "productos"
      },
      {
        nombre: "Mi cuenta",
        icono: "mdi-account",
        ruta: "about"
      },
      {
        nombre: "Citas",
        icono: "mdi-clipboard-list-outline",
        ruta: "citas"
      }
    ]
  }),
  computed: {
    isAuthenticated() {
      console.log(this.$route.path);

      return this.$route.path != "/"; // sacar datos de la sesion en la proxima
    }
  }
};
</script>
