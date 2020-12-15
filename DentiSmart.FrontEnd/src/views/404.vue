<!-- views/NotFound.vue -->

<template>
  <div class="NotFound">
    <v-container>
      <v-layout row wrap>
        <v-flex xs12>
          <v-card height="400" color="transparent" flat>
            <v-img contain height="200" src="../assets/diente_sad.png"></v-img>
            <div class="display-3 mt-5">¡Pagina no encontrada!</div>
            <div class="lighten-5">
              ¿La página a la que intentas acceder nunca existió en esta
              realidad o ha migrado a un universo paralelo.
            </div>
            <div class="paragraph-text mt-2">
              <br />Intente volver a la página de inicio y repetir su acción. O,
              póngase en contacto con el servicio de asistencia técnica para
              obtener asistencia.
            </div>
          </v-card>
        </v-flex>
      </v-layout>
    </v-container>
  </div>
</template>

<style lang="scss" scoped>
.paragraph-text {
  font-size: 18px;
}
</style>
