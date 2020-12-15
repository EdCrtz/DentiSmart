<template>
  <v-card
    flat
    class="mx-auto my-0"
    max-width="600"
    :style="[
      $vuetify.theme.dark ? { background: '#121212' } : { background: 'white' }
    ]"
  >
    <v-container>
      <v-card-title>
        Nueva Cita
      </v-card-title>
      <v-card-text>
        <v-form ref="form">
          <v-row>
            <v-col cols="12">
              <v-autocomplete
                v-model="paciente"
                :items="pacientes"
                label="Paciente"
                :item-text="
                  item =>
                    item.nombre +
                    ' ' +
                    item.apellidoPat +
                    ' ' +
                    item.apellidoMat
                "
                :item-value="item => item"
                placeholder="Buscar un paciente"
                outlined
                dense
                :menu-props="{ bottom: true, offsetY: true }"
                v-if="user.role != 'Paciente'"
                :rules="rules.paciente"
                required
                append-icon="mdi-magnify"
              >
              </v-autocomplete>
            </v-col>
            <v-col cols="12" v-if="user.role != 'Dentista'">
              <v-select
                v-model="consultorio"
                :items="consultorios"
                item-value="id"
                item-text="nombre"
                label="Consultorio"
                placeholder="Seleccione un consultorio"
                outlined
                dense
                :menu-props="{ bottom: true, offsetY: true }"
                @change="cargarDatosConsultorio"
                :rules="rules.consultorio"
                required
              ></v-select>
            </v-col>
            <v-col cols="12">
              <v-select
                v-model="servicioscita"
                :items="servicios"
                :item-value="item => item"
                item-text="nombre"
                label="Servicio"
                placeholder="Seleccione los servicios"
                outlined
                dense
                :menu-props="{ bottom: true, offsetY: true }"
                multiple
                :rules="rules.servicioscita"
                :disabled="!consultorio"
                required
                chips
                deletable-chips
              ></v-select>
            </v-col>
            <v-col cols="6" v-if="user.role != 'Dentista'">
              <v-select
                v-model="dentista"
                :items="dentistas"
                label="Dentista"
                :item-text="
                  item =>
                    item.nombre +
                    ' ' +
                    item.apellidoPat +
                    ' ' +
                    item.apellidoMat
                "
                :item-value="item => item"
                placeholder=" Seleccione un dentista"
                outlined
                dense
                :menu-props="{ bottom: true, offsetY: true }"
                :rules="rules.dentista"
                required
                :disabled="!consultorio"
              >
              </v-select>
            </v-col>
            <v-col cols="6">
              <v-datetime-picker
                label="Horario de cita"
                v-model="fechaCita"
                :text-field-props="{
                  appendIcon: 'mdi-calendar-check',
                  outlined: true,
                  dense: true,
                  placeholder: 'Selecione una fecha',
                  rules: this.rules.fechaCita,
                  required: true,
                  change: this.verificarDisponible()
                }"
                :time-picker-props="{
                  min: this.dentista.horaInicio,
                  max: this.dentista.horaFin,
                  'allowed-minutes': this.allowedMinutes
                }"
                :date-picker-props="{
                  min: new Date().toISOString().slice(0, 10)
                }"
                date-format="MM/dd/yyyy"
                :disabled="!dentista.id"
              >
                <template slot="dateIcon">
                  <v-icon>mdi-calendar</v-icon>
                </template>
                <template slot="timeIcon">
                  <v-icon>mdi-clock</v-icon>
                </template>
                <template slot="actions" slot-scope="{ parent }">
                  <v-btn text @click.native="parent.clearHandler"
                    >Cancelar</v-btn
                  >
                  <v-btn text @click="parent.okHandler">Confirmar</v-btn>
                </template>
              </v-datetime-picker>
            </v-col>
          </v-row>
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="primary" @click="guadarCita">
          Guardar
        </v-btn>
      </v-card-actions>
    </v-container>
  </v-card>
</template>
<script>
import { mapState } from "vuex";
import axios from "axios";
import { environment } from "../env/environment";
export default {
  data: () => ({
    fechaCita: null,
    consultorios: [],
    consultorio: null,
    disponible: false,
    dentista: {},
    dentistas: [],
    servicioscita: [],
    servicios: [],
    paciente: {},
    pacientes: [],
    rules: {
      fechaCita: [val => (val || "").length > 0 || "La hora es requerida"],
      dentista: [
        val => (val.id || "").length > 0 || " El dentista es requerido"
      ],
      paciente: [
        val => (val.id || "").length > 0 || " El paciente es requerido"
      ],
      consultorio: [
        val => (val || "").length > 0 || "El consultorio es requeirido"
      ],
      servicioscita: [val => val.length > 0 || " Los servicio son requeridos"]
    }
  }),
  created() {
    this.initialize();
  },
  computed: {
    ...mapState(["user"])
  },
  methods: {
    allowedMinutes: v => v % 30 == 0,
    agregarCita(cita) {
      this.$vloading.show();
      axios
        .post(environment.url + "cita", cita)
        .then(() => {
          this.$vloading.hide();
          this.$toast.success("Se agendo el cita correctamente");
          this.limpiarFormularios();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("Error: No se pudo agendar la cita ");
        });
    },
    obtenerDentistas() {
      this.$vloading.show();
      axios
        .get(environment.url + "usuario/dentista/" + this.consultorio)
        .then(res => {
          this.dentistas = res.data;
          this.$vloading.hide();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudieron obtener los Dentistas");
        });
    },
    obtenerConsultorios() {
      this.$vloading.show();
      axios
        .get(environment.url + "consultorio")
        .then(response => {
          this.consultorios = response.data;
          if (this.consultorios.length == 0) {
            this.$toast.info("No hay registros consultorios");
          }
          this.$vloading.hide();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudieron obtener los consultorios");
        });
    },
    verificarDisponible() {
      if (this.fechaCita) {
        axios
          .post(environment.url + "cita/disponible", {
            dentista: this.dentista,
            fechaCita: new Date(this.fechaCita + "UTC").toISOString()
          })
          .then(response => {
            this.$vloading.hide();
            if (response.data.status === 1) {
              this.$toast.success("¡la fecha que elegiste esta disponible!");
              this.disponible = true;
            } else {
              this.$toast.warning("¡la fecha que elegiste no esta disponible!");
              this.disponible = false;
            }
          })
          .catch(() => {
            this.$vloading.hide();
            this.$toast.error("Error no se pudo verificar la disponibilidad");
          });
      }
    },
    obtenerPacientes() {
      this.$vloading.show();
      axios
        .get(environment.url + "usuario/role/Paciente")
        .then(response => {
          this.pacientes = response.data;
          if (this.pacientes.length == 0) {
            this.$toast.info("No hay registros Pacientes");
          }
          this.$vloading.hide();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudieron obtener los Pacientes");
        });
    },
    obtenerServicios() {
      this.$vloading.show();
      axios
        .get(environment.url + "servicio/consultorio/" + this.consultorio)
        .then(response => {
          this.servicios = response.data;
          this.$vloading.hide();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudieron obtener servicios");
        });
    },
    cargarDatosConsultorio() {
      this.obtenerServicios();
      this.obtenerDentistas();
    },
    initialize() {
      //this.getProductos();
      if (this.user.role != "Dentista") {
        this.obtenerConsultorios();
      }
      if (this.user.role != "Paciente") {
        this.obtenerPacientes();
      }
      if (this.user.role == "Dentista") {
        this.dentista = this.user;
        this.consultorio = this.user.consultorio;
        this.obtenerServicios();
      }
      if (this.user.role == "Paciente") {
        this.paciente = this.user;
      }
    },
    guadarCita() {
      if (this.$refs.form.validate()) {
        let cita = {
          paciente: this.paciente,
          dentista: this.dentista,
          fechaCita: new Date(this.fechaCita + "UTC").toISOString(),
          servicios: this.servicioscita,
          status: "Pendiente"
        };
        if (this.disponible) {
          this.agregarCita(cita);
        } else {
          this.$toast.warning("¡la fecha que elegiste no esta disponible!");
        }
      }
    },
    limpiarFormularios() {
      this.$refs.form.reset();
      this.fechaCita = null;
      this.consultorios = [];
      this.consultorio = "";
      this.dentista = {};
      this.dentistas = [];
      this.servicioscita = [];
      this.servicios = [];
      this.paciente = {};
      this.pacientes = [];
      this.initialize();
    }
  }
};
</script>
<style lang="scss">
.v-autocomplete.v-select--is-menu-active
  .v-input__icon--append
  .v-icon.mdi-magnify {
  transform: none !important;
}
.v-select.v-input--dense .v-chip {
  margin: 4px !important;
}
</style>
