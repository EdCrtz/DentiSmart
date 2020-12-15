<template>
  <div>
    <v-toolbar dense color="#3FA7DB" flat dark>
      <v-toolbar-title>Historial de pagos</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-row>
        <v-col cols="12">
          <v-text-field
            v-model="search"
            append-icon="mdi-magnify"
            label="Buscar Pago..."
            single-line
            hide-details
          ></v-text-field>
        </v-col>
      </v-row>
      <v-spacer></v-spacer>
    </v-toolbar>

    <v-virtual-scroll
      :items="busquedaPago"
      :item-height="380"
      max-height="70vh"
      v-if="pagos.length > 0"
    >
      <template v-slot:default="{ item }">
        <v-card class="mx-auto my-6" max-width="600">
          <v-card-title>Pago {{ item.fechaPago }} </v-card-title>
          <v-card-subtitle>Cita {{ item.cita.fechaCita }}</v-card-subtitle>
          <v-card-text>
            <v-container>
              <v-list-item>
                <v-list-item-icon>
                  <v-icon color="indigo">
                    mdi-doctor
                  </v-icon>
                </v-list-item-icon>

                <v-list-item-content>
                  <v-list-item-title
                    >{{ item.cita.dentista.nombre }}
                    {{ item.cita.dentista.apellidoPat }}
                    {{ item.cita.dentista.apellidoMat }}
                  </v-list-item-title>
                  <v-list-item-subtitle>Dentista</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-list-item>
                <v-list-item-icon>
                  <v-icon color="green">
                    mdi-credit-card
                  </v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>${{ item.monto }}</v-list-item-title>
                  <v-list-item-subtitle>Monto</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-list-item>
                <v-list-item-icon>
                  <v-icon color="blue">
                    mdi-human-greeting
                  </v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title
                    >{{ item.cita.paciente.nombre }}
                    {{ item.cita.paciente.apellidoPat }}
                    {{ item.cita.paciente.apellidoMat }}
                  </v-list-item-title>
                  <v-list-item-subtitle>Paciente</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <h4>Servicios</h4>
              <v-chip
                v-for="(servicio, i) in item.cita.servicios"
                v-bind:key="i"
                >{{ servicio.nombre }}</v-chip
              >
            </v-container>
          </v-card-text>
        </v-card>
      </template>
    </v-virtual-scroll>
    <div v-if="pagos.length == 0 && cargo">
      <v-container class="mx-auto">
        <v-img contain height="200" src="../assets/diente_sad.png"></v-img>
        <h2 style="text-align: center;">
          No tienes ningun pago
        </h2>
      </v-container>
    </div>
  </div>
</template>
<script>
import axios from "axios";
import { environment } from "../env/environment";
import { mapState } from "vuex";
import moment from "moment";
export default {
  data: () => ({
    pagos: [],
    search: "",
    cargo: false
  }),
  created() {
    this.obtenerPagos();
  },
  computed: {
    ...mapState(["user"]),
    busquedaPago() {
      return this.pagos
        .filter(pa => {
          return (
            pa.cita.paciente.nombre
              .toLowerCase()
              .includes(this.search.toLowerCase()) ||
            pa.cita.paciente.apellidoPat
              .toLowerCase()
              .includes(this.search.toLowerCase()) ||
            pa.cita.paciente.apellidoMat
              .toLowerCase()
              .includes(this.search.toLowerCase()) ||
            pa.cita.dentista.nombre
              .toLowerCase()
              .includes(this.search.toLowerCase()) ||
            pa.cita.dentista.apellidoPat
              .toLowerCase()
              .includes(this.search.toLowerCase()) ||
            pa.cita.dentista.apellidoMat
              .toLowerCase()
              .includes(this.search.toLowerCase())
          );
        })
        .sort(function(a, b) {
          if (a.fechaPago > b.fechaPago) {
            return 1;
          }
          if (a.fechaPago < b.fechaPago) {
            return -1;
          }
          return 0;
        });
    }
  },
  methods: {
    obtenerPagos() {
      this.$vloading.show();
      let ruta = environment.url + "pago";
      if (this.user.role == "Dentista") ruta += "/dentista/" + this.user.id;
      else if (this.user.role == "Paciente")
        ruta += "/paciente/" + this.user.id;
      axios
        .get(ruta)
        .then(response => {
          this.$vloading.hide();
          for (let pago of response.data) {
            pago.fechaPago = moment(pago.fechaPago).format("DD/MM/YYYY");
            pago.cita.fechaCita = moment(pago.cita.fechaCita)
              .utc()
              .format("DD/MM/YYYY");
          }
          this.pagos = response.data;
          this.cargo = true;
        })
        .catch(() => {
          this.$toast.error("Error al cargar los pagos");
          this.$vloading.hide();
          this.cargo = true;
        });
    }
  }
};
</script>
<style scoped lang="scss">
.pagosScroll {
  max-height: 70vh;
  overflow-x: auto;
}
</style>
