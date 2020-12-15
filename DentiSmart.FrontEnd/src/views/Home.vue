<template>
  <div class="home">
    <v-container>
      <v-row>
        <v-col>
          <v-row>
            <v-card
              v-if="(role === 'Paciente' || role === 'Dentista') && cargocitas"
              class="mx-5 my-5 "
              color="blue lighten-1"
            >
              <v-tabs
                v-model="tab"
                background-color="blue lighten-1"
                centered
                dark
                icons-and-text
                color="white"
              >
                <v-tabs-slider></v-tabs-slider>

                <v-tab href="#citas" @click="seccion = 'citas'">
                  Proximas Citas
                  <v-icon>mdi-calendar-text</v-icon>
                </v-tab>

                <v-tab
                  v-if="user.role === 'Paciente'"
                  href="#consultorios"
                  @click="seccion = 'consultorios'"
                >
                  Consultorios
                  <v-icon>mdi-home-city-outline</v-icon>
                </v-tab>
              </v-tabs>

              <v-tabs-items v-model="tab">
                <v-tab-item value="citas">
                  <v-list two-line>
                    <v-list-item-group v-if="hayCitas" class="cardCitas">
                      <template>
                        <v-list-item
                          v-for="(cita, j) in proximasCitasOrdenadas"
                          v-bind:key="j"
                          cols="12"
                        >
                          <template>
                            <v-list-item-content>
                              <v-list-item-title
                                class="font-weight-bold"
                                v-text="cita.fechaCita"
                              ></v-list-item-title>

                              <span>Servicio(s):</span>

                              <v-list-item-subtitle
                                class="text--primary"
                                v-for="(servicio, i) in cita.servicios"
                                v-bind:key="i"
                                v-text="servicio.nombre"
                              ></v-list-item-subtitle>
                              <span v-if="role === 'Dentista'">Paciente:</span>
                              <v-list-item-subtitle v-if="role === 'Dentista'"
                                >{{ cita.paciente.nombre }}
                                {{ cita.paciente.apellidoPat }}
                                {{ cita.paciente.apellidoMat }}
                              </v-list-item-subtitle>

                              <div class="pastilla">
                                <v-list-item-subtitle
                                  v-if="cita.pagada"
                                  class="pagado rounded-pill white--text"
                                >
                                  Servicio pagado
                                </v-list-item-subtitle>

                                <v-list-item-subtitle
                                  class="debe rounded-pill white--text"
                                  v-else
                                >
                                  Pago pendiente
                                </v-list-item-subtitle>
                              </div>
                              <v-divider></v-divider>
                            </v-list-item-content>
                          </template>
                        </v-list-item>
                      </template>
                    </v-list-item-group>

                    <v-list-item-group v-else class="text-center">
                      No tienes citas proximamente
                    </v-list-item-group>
                  </v-list>
                </v-tab-item>

                <v-tab-item value="consultorios">
                  <v-list two-line>
                    <v-list-item-group class="cardConsultorios">
                      <template>
                        <v-list-item
                          v-for="consultorio in consultorios"
                          :key="consultorio.id"
                          cols="12"
                        >
                          <template>
                            <v-list-item-content>
                              <span>Sucursal:</span>
                              <v-list-item-title
                                v-text="consultorio.nombre"
                              ></v-list-item-title>

                              <v-list-item-title
                                ><span>Direccion:</span><br />{{
                                  consultorio.direccionConsultorio.calle
                                }}
                                {{ consultorio.direccionConsultorio.numero }}
                                {{
                                  consultorio.direccionConsultorio.colonia
                                }}</v-list-item-title
                              >

                              <span>Teléfonos:</span>
                              <v-list-item-subtitle
                                class="text--primary"
                                v-for="(telefono, i) in consultorio.telefonos"
                                v-bind:key="i"
                                >{{ telefono.tel }}</v-list-item-subtitle
                              >

                              <v-divider></v-divider>
                            </v-list-item-content>
                          </template>
                        </v-list-item>
                      </template>
                    </v-list-item-group>
                  </v-list>
                </v-tab-item>
              </v-tabs-items>
              <!-- <v-card-title class="justify-space-between white--text">
                Proximas Citas
                <v-icon class="white--text">mdi-calendar-text</v-icon>
              </v-card-title> -->

              <v-card-actions
                color="blue"
                class="justify-space-between cardActions"
              >
                <v-btn
                  v-if="seccion === 'citas'"
                  class="blue-grey darken-2 white--text"
                  to="/citas/proximas"
                  >Ver todas</v-btn
                >
              </v-card-actions>
            </v-card>
          </v-row>
          <v-row>
            <!-- <v-card
              class="mx-5 my-5 "
              color="blue lighten-1"
              v-if="role === 'Paciente'"
            >
              <v-card-title class="justify-space-between white--text">
                <v-icon class="white--text">mdi-home-city-outline</v-icon>
                Nuestros Consultorios
              </v-card-title>

              <v-list two-line>
                <v-list-item-group class="cardConsultorios">
                  <template>
                    <v-list-item
                      v-for="consultorio in consultorios"
                      :key="consultorio.id"
                      cols="12"
                    >
                      <template>
                        <v-list-item-content>
                          <span>Sucursal:</span>
                          <v-list-item-title
                            v-text="consultorio.nombre"
                          ></v-list-item-title>

                          <v-list-item-title
                            ><span>Direccion:</span><br />{{
                              consultorio.direccionConsultorio.calle
                            }}
                            {{ consultorio.direccionConsultorio.numero }}
                            {{
                              consultorio.direccionConsultorio.colonia
                            }}</v-list-item-title
                          >

                          <span>Teléfonos:</span>
                          <v-list-item-subtitle
                            class="text--primary"
                            v-for="(telefono, i) in consultorio.telefonos"
                            v-bind:key="i"
                            >{{ telefono.tel }}</v-list-item-subtitle
                          >

                          <v-divider></v-divider>
                        </v-list-item-content>
                      </template>
                    </v-list-item>
                  </template>
                </v-list-item-group>
              </v-list>
              <v-card-actions color="blue"> </v-card-actions>
            </v-card> -->
          </v-row>
        </v-col>
        <v-col style="text-align: -webkit-center;" class="align-self-center">
          <template>
            <!-- pacientes -->
            <v-row v-if="role === 'Paciente'">
              <v-col cols="6" sm="12">
                <v-card
                  to="/citas/nueva"
                  class=" pt-3 ma-2 text-center white--text"
                  outlined
                  hover
                  color="blue"
                  width="200"
                >
                  <v-icon size="50" color="white">mdi-calendar-check</v-icon>
                  <v-card-title class="justify-center"
                    >Agendar cita</v-card-title
                  >
                </v-card>
              </v-col>

              <v-col cols="6" sm="12" class="">
                <v-card
                  to="/citas/tratamientos"
                  class="pt-3 ma-2 text-center white--text"
                  outlined
                  hover
                  color="blue"
                  width="200"
                >
                  <v-icon size="50" color="white"> mdi-human</v-icon>
                  <v-card-title class="justify-center "
                    >Tratamaientos</v-card-title
                  >
                </v-card>
              </v-col>

              <v-col cols="6" sm="12" class="">
                <v-card
                  to="/citas/pagos"
                  class="pt-3 ma-2 text-center white--text"
                  outlined
                  hover
                  color="blue"
                  width="200"
                >
                  <v-icon size="50" color="white">mdi-currency-usd</v-icon>
                  <v-card-title class="justify-center white--text"
                    >Pagos</v-card-title
                  >
                </v-card>
              </v-col>
            </v-row>

            <!-- dentista -->
            <v-row v-if="role === 'Dentista'">
              <v-col cols="6" sm="4">
                <v-card
                  to="/citas/nueva"
                  class=" pt-3 ma-2 text-center white--text"
                  outlined
                  hover
                  color="blue"
                  width="200"
                >
                  <v-icon size="50" color="white">mdi-calendar-check</v-icon>
                  <v-card-title class="justify-center"
                    >Agendar cita</v-card-title
                  >
                </v-card>
              </v-col>

              <v-col cols="6" sm="4" class="">
                <v-card
                  to="/citas/tratamientos"
                  class="pt-3 ma-2 text-center white--text"
                  outlined
                  hover
                  color="blue"
                  width="200"
                >
                  <v-icon size="50" color="white"> mdi-human</v-icon>
                  <v-card-title class="justify-center "
                    >Tratamaientos</v-card-title
                  >
                </v-card>
              </v-col>

              <v-col cols="6" sm="4" class="">
                <v-card
                  to="/citas/pagos"
                  class="pt-3 ma-2 text-center white--text"
                  outlined
                  hover
                  color="blue"
                  width="200"
                >
                  <v-icon size="50" color="white">mdi-currency-usd</v-icon>
                  <v-card-title class="justify-center white--text"
                    >Pagos</v-card-title
                  >
                </v-card>
              </v-col>
              <v-col cols="6" sm="4">
                <v-card
                  to="/inventario"
                  class="pa-2 ma-2 text-center"
                  outlined
                  hover
                  color="blue"
                  width="200"
                >
                  <v-icon size="50" color="white">mdi-dolly</v-icon>
                  <v-card-title class="justify-center white--text">
                    Inventario</v-card-title
                  >
                </v-card>
              </v-col>

              <v-col cols="6" sm="4" class="">
                <v-card
                  to="/productos"
                  class="pa-2 ma-2 text-center"
                  outlined
                  hover
                  color="blue"
                  width="200"
                >
                  <v-icon size="50" color="white">mdi-basket</v-icon>
                  <v-card-title class="justify-center white--text">
                    Productos</v-card-title
                  >
                </v-card>
              </v-col>

              <v-col cols="6" sm="4" class="">
                <v-card
                  to="/servicios"
                  class="pa-2 ma-2 text-center"
                  outlined
                  hover
                  color="blue"
                  width="200"
                >
                  <v-icon size="50" color="white"
                    >mdi-file-document-edit-outline</v-icon
                  >
                  <v-card-title class="justify-center white--text"
                    >Servicios</v-card-title
                  >
                </v-card>
              </v-col>

              <v-col cols="6" sm="4" class="">
                <v-card
                  to="/pacientes"
                  class="pa-2 ma-2 text-center"
                  outlined
                  hover
                  color="blue"
                  width="200"
                >
                  <v-icon size="50" color="white">mdi-account-edit</v-icon>
                  <v-card-title class="justify-center white--text"
                    >Pacientes</v-card-title
                  >
                </v-card>
              </v-col>

              <v-col cols="6" sm="4" class="">
                <v-card
                  to="/reportes"
                  class="pa-2 ma-2 text-center"
                  outlined
                  hover
                  color="blue"
                  width="200"
                >
                  <v-icon size="50" color="white">mdi-chart-line</v-icon>
                  <v-card-title class="justify-center white--text"
                    >Reportes</v-card-title
                  >
                </v-card>
              </v-col>
            </v-row>
          </template>
        </v-col>
      </v-row>

      <!-- admin -->
      <v-row v-if="role === 'ADMIN'" style="text-align: -webkit-center;">
        <v-col cols="6" sm="4">
          <v-card
            to="/inventario"
            class="pa-2 ma-2 text-center"
            outlined
            hover
            color="blue"
            width="200"
          >
            <v-icon size="50" color="white">mdi-dolly</v-icon>
            <v-card-title class="justify-center white--text">
              Inventario</v-card-title
            >
          </v-card>
        </v-col>

        <v-col cols="6" sm="4" class="">
          <v-card
            to="/productos"
            class="pa-2 ma-2 text-center"
            outlined
            hover
            color="blue"
            width="200"
          >
            <v-icon size="50" color="white">mdi-basket</v-icon>
            <v-card-title class="justify-center white--text">
              Productos</v-card-title
            >
          </v-card>
        </v-col>

        <v-col cols="6" sm="4" class="">
          <v-card
            to="/servicios"
            class="pa-2 ma-2 text-center"
            outlined
            hover
            color="blue"
            width="200"
          >
            <v-icon size="50" color="white"
              >mdi-file-document-edit-outline</v-icon
            >
            <v-card-title class="justify-center white--text"
              >Servicios</v-card-title
            >
          </v-card>
        </v-col>

        <v-col cols="6" sm="4" class="">
          <v-card
            to="/pacientes"
            class="pa-2 ma-2 text-center"
            outlined
            hover
            color="blue"
            width="200"
          >
            <v-icon size="50" color="white">mdi-account-edit</v-icon>
            <v-card-title class="justify-center white--text"
              >Pacientes</v-card-title
            >
          </v-card>
        </v-col>
        <v-col cols="6" sm="4" class="">
          <v-card
            to="/citas/nueva"
            class=" pt-3 ma-2 text-center"
            outlined
            hover
            color="blue"
            width="200"
          >
            <v-icon size="50" color="white">mdi-calendar</v-icon>
            <v-card-title class="justify-center white--text"
              >Citas</v-card-title
            >
          </v-card>
        </v-col>
        <v-col cols="6" sm="4" class="">
          <v-card
            to="/reportes"
            class="pa-2 ma-2 text-center"
            outlined
            hover
            color="blue"
            width="200"
          >
            <v-icon size="50" color="white">mdi-chart-line</v-icon>
            <v-card-title class="justify-center white--text"
              >Reportes</v-card-title
            >
          </v-card>
        </v-col>
        <v-col cols="6" sm="4" class="">
          <v-card
            to="/consultorios"
            class="pa-2 ma-2 text-center"
            outlined
            hover
            color="blue"
            width="200"
          >
            <v-icon size="50" color="white">mdi-home-city-outline</v-icon>
            <v-card-title class="justify-center white--text"
              >Consultorios</v-card-title
            >
          </v-card>
        </v-col>

        <v-col cols="6" sm="4" class="">
          <v-card
            v-if="role === 'ADMIN'"
            to="/dentistas"
            class="pa-2 ma-2 text-center"
            outlined
            hover
            color="blue"
            width="200"
          >
            <v-icon size="50" color="white">mdi-toothbrush-paste</v-icon>
            <v-card-title class="justify-center white--text"
              >Dentistas</v-card-title
            >
          </v-card>
        </v-col>
      </v-row>
    </v-container>

    <chatComponent />
  </div>
</template>

<script>
import axios from "axios";
import { environment } from "../env/environment";
import moment from "moment";
import { mapState } from "vuex";
import chatComponent from "./components/chat";
export default {
  name: "Home",
  components: {
    chatComponent
  },
  data() {
    return {
      cargocitas: false,
      citas: [],
      proximasCitas: [],
      proximasCitasOrdenadas: [],
      consultorios: [],
      proximasCitasOB: {
        id: "",
        fechaCita: "",
        paciente: {},
        servicios: [],
        pagada: null
      },
      fechaActual: "",
      noSeVe: false,

      hayCitas: null,
      role: "",
      tab: null,
      seccion: "citas"
    };
  },
  created() {
    this.initialize();
  },
  computed: {
    ...mapState(["user"])
  },
  methods: {
    // toggleCitas() {
    //   this.noSeVe = !this.noSeVe;

    //   if (this.noSeVe) {
    //     this.botonToggle = "Mostrar";
    //   } else {
    //     this.botonToggle = "Ocultar";
    //   }
    // },

    obtenerConsultorios() {
      axios
        .get(`${environment.url}Consultorio`)
        .then(response => {
          this.consultorios = response.data;

          this.$vloading.hide();
        })
        .catch(e => {
          console.log(e);
          this.$toast.error("Error al cargar los consultorios");
          this.$vloading.hide();
        });
    },
    obtenerCitas() {
      // let fa = this.fechaActual;
      let tamanioCitasOrd;
      let dt = new Date();

      let fechaActual = `${dt
        .getFullYear()
        .toString()
        .padStart(4, "0")}-${(dt.getMonth() + 1)
        .toString()
        .padStart(2, "0")}-${dt
        .getDate()
        .toString()
        .padStart(2, "0")}T${dt
        .getHours()
        .toString()
        .padStart(2, "0")}:${dt
        .getMinutes()
        .toString()
        .padStart(2, "0")}:${dt
        .getSeconds()
        .toString()
        .padStart(2, "0")}Z`;

      this.$vloading.show();
      let ruta = environment.url + "cita";
      if (this.user.role == "Dentista") ruta += "/dentista/" + this.user.id;
      else if (this.user.role == "Paciente")
        ruta += "/paciente/" + this.user.id;
      axios
        .get(ruta)
        .then(res => {
          this.citas = res.data;
          for (let cita of res.data) {
            if (cita.fechaCita >= fechaActual) {
              this.proximasCitas.push(
                (this.proximasCitasOB = {
                  id: cita.id,
                  fechaCita: cita.fechaCita,
                  paciente: cita.paciente,
                  servicios: cita.servicios,
                  pagada: cita.pagada
                })
              );
            }
          }
          this.proximasCitas.sort((a, b) =>
            a.fechaCita > b.fechaCita ? 1 : -1
          );
          moment.locale("es");
          for (let cita of this.proximasCitas) {
            this.proximasCitasOrdenadas.push(
              (this.proximasCitasOB = {
                id: cita.id,
                fechaCita: moment(cita.fechaCita)
                  .utc()
                  .format("llll"),
                paciente: cita.paciente,
                servicios: cita.servicios,
                pagada: cita.pagada
              })
            );
          }
          this.proximasCitasOrdenadas.splice(5);

          tamanioCitasOrd = this.proximasCitasOrdenadas.length;
          if (tamanioCitasOrd > 0) {
            this.hayCitas = true;
          } else {
            this.hayCitas = false;
          }

          this.$vloading.hide();
          this.cargocitas = true;
        })
        .catch(e => {
          console.log(e);
          this.$vloading.hide();
          this.$toast.error("No se pudieron obtener las citas");
        });
    },
    initialize() {
      this.role = this.user.role;
      if (this.user.role != "ADMIN") {
        this.obtenerCitas();
      }
      if (this.user.role == "Paciente") {
        this.obtenerConsultorios();
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.esconder {
  display: none;
}
.debe {
  background-color: #eb4f44;
  max-width: fit-content;
  font-size: 15px;
  padding: 5px;
}
.pagado {
  background-color: #70bf69;
  max-width: fit-content;
  font-size: 15px;
  padding: 5px;
}
.pastilla {
  text-align: -webkit-right;
}

.contenedorBotones {
  text-align: -webkit-center;
}

.tresBotones {
  text-align: center;
}

.cardConsultorios {
  max-height: 350px;
  overflow-x: auto;
}
.cardCitas {
  max-height: 350px;
  overflow-x: auto;
}

.cardActions {
  height: 52px;
}

span {
  font-weight: bold;
}
</style>
