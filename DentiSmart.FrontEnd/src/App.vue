<template>
  <v-app>
    <v-navigation-drawer v-model="drawer" temporary app>
      <v-list nav dense>
        <v-list-item-group active-class="blue--text text--accent-4">
          <v-list-item
            v-for="opcion in menu"
            v-bind:key="opcion.id"
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
      scroll-threshold="500"
      fade-img-on-scroll
      dark
      src="https://www.artoispublicidade.com.br/wp-content/uploads/2020/06/1433.jpg"
      app
    >
      <v-app-bar-nav-icon @click="drawer = true"></v-app-bar-nav-icon>
      <v-spacer></v-spacer>
      <v-toolbar-title class="font-weight-bold"
        >Portal de dentista</v-toolbar-title
      >
    </v-app-bar>

    <v-main>
      <v-container fluid>
        <router-view></router-view>
      </v-container>
    </v-main>

    <v-footer app> </v-footer>
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
        id: 1,
        nombre: "Inicio",
        icono: "mdi-tooth",
        ruta: "/"
      },
      {
        id: 2,
        nombre: "Catalogo de Productos",
        icono: "mdi-border-color",
        ruta: "productos"
      },
      {
        id: 3,
        nombre: "Mi cuenta",
        icono: "mdi-account",
        ruta: "about"
      }
    ]
  })
};
</script>
