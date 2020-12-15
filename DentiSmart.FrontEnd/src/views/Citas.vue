<template>
  <div>
    <v-tabs
      centered
      icons-and-text
      dark
      background-color="#3FA7DB"
      show-arrows
      color="white"
    >
      <v-tab :to="{ path: '/citas/nueva' }">
        Agendar Nueva Cita
        <v-icon left>
          mdi-calendar-plus
        </v-icon>
      </v-tab>
      <v-tab to="/citas/proximas">
        Ver Proximas Citas
        <v-icon left>
          mdi-calendar-check
        </v-icon>
      </v-tab>
      <v-tab to="/citas/tratamientos">
        Ver mis Tratamientos
        <v-icon left>
          mdi-human
        </v-icon>
      </v-tab>
      <v-tab to="/citas/pagos">
        Ver mis Pagos
        <v-icon left>
          mdi-credit-card-outline
        </v-icon>
      </v-tab>
    </v-tabs>
    <router-view></router-view>
  </div>
</template>
<style lang="scss" scoped>
.compact-form {
  transform: scale(0.875);
  transform-origin: center;
}
</style>
