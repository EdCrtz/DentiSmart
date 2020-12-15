<template>
  <div>
    <v-toolbar dense color="#3FA7DB" flat dark>
      <v-toolbar-title>Historial de tratamientos</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-row>
        <v-col cols="12">
          <v-text-field
            v-model="search"
            append-icon="mdi-magnify"
            label="Buscar Tratamiento..."
            single-line
            hide-details
          ></v-text-field>
        </v-col>
      </v-row>
      <v-spacer></v-spacer>
    </v-toolbar>

    <v-virtual-scroll
      :items="busquedaTratamiento"
      :item-height="380"
      max-height="70vh"
      v-if="tratamientos.length > 0"
    >
      <template v-slot:default="{ item }">
        <v-card class="mx-auto my-6" max-width="600">
          <v-card-title>Tratamiento {{ item.fechaCreacion }} </v-card-title>
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
          <v-card-actions>
            <v-btn color="light-blue darken-4" @click="abrirDetalle(item)" dark
              >Detalle</v-btn
            >
          </v-card-actions>
        </v-card>
      </template>
    </v-virtual-scroll>
    <div v-if="tratamientos.length == 0 && cargo">
      <v-container class="mx-auto">
        <v-img contain height="200" src="../assets/diente_sad.png"></v-img>
        <h2 style="text-align: center;">
          No tienes ningun tratamiento
        </h2>
      </v-container>
    </div>
    <v-dialog v-model="dialogTratamiento">
      <v-card>
        <v-toolbar color="azulSecundario" dark flat>
          <span class="headline">Tratamiento</span>
          <v-spacer></v-spacer>
          <v-btn icon @click="dialogTratamiento = false">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-toolbar>
        <v-card-text>
          <v-stepper v-model="e1">
            <v-stepper-header>
              <span
                style="display: contents;"
                v-for="(item, i) in serviciostratamiento"
                v-bind:key="i"
              >
                <v-stepper-step :complete="e1 > i + 1" :step="i + 1">
                  {{ item.servicio.nombre }}
                </v-stepper-step>
                <v-divider></v-divider>
              </span>
              <v-stepper-step
                :complete="e1 > serviciostratamiento.length + 1"
                :step="serviciostratamiento.length + 1"
              >
                Odontograma
              </v-stepper-step>
            </v-stepper-header>
            <v-stepper-items>
              <v-stepper-content
                v-for="(item, i) in serviciostratamiento"
                v-bind:key="i"
                :step="i + 1"
              >
                <v-card flat>
                  <v-card-text>
                    <v-row>
                      <v-col cols="12">
                        <v-textarea
                          outlined
                          label="Sintomas"
                          placeholder="Escribir los sintomas del paciente..."
                          auto-grow
                          v-model="item.sintomas"
                          readonly
                        ></v-textarea>
                      </v-col>
                      <v-col cols="12">
                        <v-textarea
                          outlined
                          label="Observacion"
                          placeholder="Escribir los las observaciones del paciente..."
                          auto-grow
                          v-model="item.observacion"
                          readonly
                        ></v-textarea>
                      </v-col>
                      <v-col cols="12">
                        <v-textarea
                          outlined
                          label="Receta"
                          placeholder="Escribir la receta o recomendacione para el paciente..."
                          auto-grow
                          v-model="item.receta"
                          readonly
                        ></v-textarea>
                      </v-col>
                      <v-col cols="12" v-if="item.evidencia">
                        <v-img
                          class="mx-auto"
                          :src="item.evidencia"
                          contain
                          width="400px"
                        ></v-img>
                      </v-col>
                    </v-row>
                  </v-card-text>
                </v-card>

                <v-btn color="primary" @click="e1++">
                  Continuar
                </v-btn>

                <v-btn text @click="e1 > 1 ? e1-- : e1">
                  Regresar
                </v-btn>
              </v-stepper-content>
              <v-stepper-content :step="serviciostratamiento.length + 1">
                <v-card flat>
                  <v-card-text>
                    <v-row>
                      <v-col cols="12">
                        <div
                          class="diente"
                          v-for="(item, i) in dientesSuperior"
                          v-bind:key="i"
                        >
                          <!--item 1-->
                          <input name="valor" type="hidden" value="1" />
                          <div
                            class="cuadro click"
                            :style="[
                              item.norte == 1
                                ? { background: 'red' }
                                : item.norte == 2
                                ? { background: 'blue' }
                                : item.norte == 3
                                ? { background: 'yellow' }
                                : item.norte == 4
                                ? { background: 'tomato' }
                                : item.norte == 5
                                ? { background: 'brown' }
                                : item.norte == 6
                                ? { background: 'blueviolet' }
                                : item.norte == 7
                                ? { background: 'green' }
                                : item.norte == 8
                                ? { background: 'orange' }
                                : { background: 'white' }
                            ]"
                          >
                            <input name="cuadro" type="hidden" value="1" />
                          </div>
                          <div
                            class="cuadro izquierdo click"
                            :style="[
                              item.izquierdo == 1
                                ? { background: 'red' }
                                : item.izquierdo == 2
                                ? { background: 'blue' }
                                : item.izquierdo == 3
                                ? { background: 'yellow' }
                                : item.izquierdo == 4
                                ? { background: 'tomato' }
                                : item.izquierdo == 5
                                ? { background: 'brown' }
                                : item.izquierdo == 6
                                ? { background: 'blueviolet' }
                                : item.izquierdo == 7
                                ? { background: 'green' }
                                : item.izquierdo == 8
                                ? { background: 'orange' }
                                : { background: 'white' }
                            ]"
                          >
                            <input name="cuadro" type="hidden" value="2" />
                          </div>
                          <div
                            class="cuadro debajo click"
                            :style="[
                              item.sur == 1
                                ? { background: 'red' }
                                : item.sur == 2
                                ? { background: 'blue' }
                                : item.sur == 3
                                ? { background: 'yellow' }
                                : item.sur == 4
                                ? { background: 'tomato' }
                                : item.sur == 5
                                ? { background: 'brown' }
                                : item.sur == 6
                                ? { background: 'blueviolet' }
                                : item.sur == 7
                                ? { background: 'green' }
                                : item.sur == 8
                                ? { background: 'orange' }
                                : { background: 'white' }
                            ]"
                          >
                            <input name="cuadro" type="hidden" value="3" />
                          </div>
                          <div
                            class="cuadro derecha click"
                            :style="[
                              item.derecho == 1
                                ? { background: 'red' }
                                : item.derecho == 2
                                ? { background: 'blue' }
                                : item.derecho == 3
                                ? { background: 'yellow' }
                                : item.derecho == 4
                                ? { background: 'tomato' }
                                : item.derecho == 5
                                ? { background: 'brown' }
                                : item.derecho == 6
                                ? { background: 'blueviolet' }
                                : item.derecho == 7
                                ? { background: 'green' }
                                : item.derecho == 8
                                ? { background: 'orange' }
                                : { background: 'white' }
                            ]"
                          >
                            <input name="cuadro" type="hidden" value="4" />
                          </div>
                          <div
                            class="centro click"
                            :style="[
                              item.centro == 1
                                ? { background: 'red' }
                                : item.centro == 2
                                ? { background: 'blue' }
                                : item.centro == 3
                                ? { background: 'yellow' }
                                : item.centro == 4
                                ? { background: 'tomato' }
                                : item.centro == 5
                                ? { background: 'brown' }
                                : item.centro == 6
                                ? { background: 'blueviolet' }
                                : item.centro == 7
                                ? { background: 'green' }
                                : item.centro == 8
                                ? { background: 'orange' }
                                : { background: 'white' }
                            ]"
                          >
                            <input name="cuadro" type="hidden" value="5" />
                          </div>
                        </div>
                      </v-col>
                      <v-col cols="12">
                        <div
                          class="diente"
                          v-for="(item, i) in dientesInferior"
                          v-bind:key="i"
                        >
                          <!--item 1-->
                          <input name="valor" type="hidden" value="1" />
                          <div
                            class="cuadro click"
                            :style="[
                              item.norte == 1
                                ? { background: 'red' }
                                : item.norte == 2
                                ? { background: 'blue' }
                                : item.norte == 3
                                ? { background: 'yellow' }
                                : item.norte == 4
                                ? { background: 'tomato' }
                                : item.norte == 5
                                ? { background: 'brown' }
                                : item.norte == 6
                                ? { background: 'blueviolet' }
                                : item.norte == 7
                                ? { background: 'green' }
                                : item.norte == 8
                                ? { background: 'orange' }
                                : { background: 'white' }
                            ]"
                          >
                            <input name="cuadro" type="hidden" value="1" />
                          </div>
                          <div
                            class="cuadro izquierdo click"
                            :style="[
                              item.izquierdo == 1
                                ? { background: 'red' }
                                : item.izquierdo == 2
                                ? { background: 'blue' }
                                : item.izquierdo == 3
                                ? { background: 'yellow' }
                                : item.izquierdo == 4
                                ? { background: 'tomato' }
                                : item.izquierdo == 5
                                ? { background: 'brown' }
                                : item.izquierdo == 6
                                ? { background: 'blueviolet' }
                                : item.izquierdo == 7
                                ? { background: 'green' }
                                : item.izquierdo == 8
                                ? { background: 'orange' }
                                : { background: 'white' }
                            ]"
                          >
                            <input name="cuadro" type="hidden" value="2" />
                          </div>
                          <div
                            class="cuadro debajo click"
                            :style="[
                              item.sur == 1
                                ? { background: 'red' }
                                : item.sur == 2
                                ? { background: 'blue' }
                                : item.sur == 3
                                ? { background: 'yellow' }
                                : item.sur == 4
                                ? { background: 'tomato' }
                                : item.sur == 5
                                ? { background: 'brown' }
                                : item.sur == 6
                                ? { background: 'blueviolet' }
                                : item.sur == 7
                                ? { background: 'green' }
                                : item.sur == 8
                                ? { background: 'orange' }
                                : { background: 'white' }
                            ]"
                          >
                            <input name="cuadro" type="hidden" value="3" />
                          </div>
                          <div
                            class="cuadro derecha click"
                            :style="[
                              item.derecho == 1
                                ? { background: 'red' }
                                : item.derecho == 2
                                ? { background: 'blue' }
                                : item.derecho == 3
                                ? { background: 'yellow' }
                                : item.derecho == 4
                                ? { background: 'tomato' }
                                : item.derecho == 5
                                ? { background: 'brown' }
                                : item.derecho == 6
                                ? { background: 'blueviolet' }
                                : item.derecho == 7
                                ? { background: 'green' }
                                : item.derecho == 8
                                ? { background: 'orange' }
                                : { background: 'white' }
                            ]"
                          >
                            <input name="cuadro" type="hidden" value="4" />
                          </div>
                          <div
                            class="centro click"
                            :style="[
                              item.centro == 1
                                ? { background: 'red' }
                                : item.centro == 2
                                ? { background: 'blue' }
                                : item.centro == 3
                                ? { background: 'yellow' }
                                : item.centro == 4
                                ? { background: 'tomato' }
                                : item.centro == 5
                                ? { background: 'brown' }
                                : item.centro == 6
                                ? { background: 'blueviolet' }
                                : item.centro == 7
                                ? { background: 'green' }
                                : item.centro == 8
                                ? { background: 'orange' }
                                : { background: 'white' }
                            ]"
                          >
                            <input name="cuadro" type="hidden" value="5" />
                          </div>
                        </div>
                      </v-col>
                    </v-row>
                  </v-card-text>
                  <v-card-actions>
                    <v-btn color="#FF0000" dark>Caries</v-btn>
                    <v-btn color="#0000FF" dark>Amalgama</v-btn>
                    <v-btn color="#FFFF00" light>Endodoncia</v-btn>
                    <v-btn color="#FF6347" dark>Ausente</v-btn>
                    <v-btn color="#A52A2A" dark>Resina</v-btn>
                    <v-btn color="#8A2BE2" dark>Implante</v-btn>
                    <v-btn color="#008000" dark>Sellante</v-btn>
                    <v-btn color="#FFA500" dark>Corona</v-btn>
                    <v-btn color="white" light>Normal</v-btn>
                  </v-card-actions>
                </v-card>
                <v-btn text @click="e1 > 1 ? e1-- : e1">
                  Regresar
                </v-btn>
              </v-stepper-content>
            </v-stepper-items>
          </v-stepper>
        </v-card-text>
      </v-card>
    </v-dialog>
  </div>
</template>
<script>
import axios from "axios";
import { environment } from "../env/environment";
import { mapState } from "vuex";
import moment from "moment";
export default {
  data: () => ({
    tratamientos: [],
    search: "",
    cargo: false,
    e1: 1,
    dientesInferior: "",
    dientesSuperior: "",
    serviciostratamiento: [],
    estadoDiente: 1,
    dialogTratamiento: false
  }),
  created() {
    this.obtenerPagos();
  },
  computed: {
    ...mapState(["user"]),
    busquedaTratamiento() {
      return this.tratamientos
        .filter(tra => {
          return (
            tra.cita.paciente.nombre
              .toLowerCase()
              .includes(this.search.toLowerCase()) ||
            tra.cita.paciente.apellidoPat
              .toLowerCase()
              .includes(this.search.toLowerCase()) ||
            tra.cita.paciente.apellidoMat
              .toLowerCase()
              .includes(this.search.toLowerCase()) ||
            tra.cita.dentista.nombre
              .toLowerCase()
              .includes(this.search.toLowerCase()) ||
            tra.cita.dentista.apellidoPat
              .toLowerCase()
              .includes(this.search.toLowerCase()) ||
            tra.cita.dentista.apellidoMat
              .toLowerCase()
              .includes(this.search.toLowerCase())
          );
        })
        .sort(function(a, b) {
          if (a.fechaCreacion > b.fechaCreacion) {
            return 1;
          }
          if (a.fechaCreacion < b.fechaCreacion) {
            return -1;
          }
          return 0;
        });
    }
  },
  methods: {
    obtenerPagos() {
      this.$vloading.show();
      let ruta = environment.url + "tratamiento";
      if (this.user.role == "Dentista") ruta += "/dentista/" + this.user.id;
      else if (this.user.role == "Paciente")
        ruta += "/paciente/" + this.user.id;
      axios
        .get(ruta)
        .then(response => {
          this.$vloading.hide();
          for (let tratamiento of response.data) {
            tratamiento.fechaCreacion = moment(
              tratamiento.fechaCreacion
            ).format("DD/MM/YYYY");
            tratamiento.cita.fechaCita = moment(tratamiento.cita.fechaCita)
              .utc()
              .format("DD/MM/YYYY");
          }
          this.tratamientos = response.data;
          this.cargo = true;
        })
        .catch(() => {
          this.$toast.error("Error al cargar los tratamientos");
          this.$vloading.hide();
          this.cargo = true;
        });
    },
    abrirDetalle(item) {
      this.dientesSuperior = item.odontograma.dientesSuperior;
      this.dientesInferior = item.odontograma.dientesInferior;
      this.serviciostratamiento = item.detalle;
      this.dialogTratamiento = true;
      this.e1 = 1;
    }
  }
};
</script>
<style scoped lang="scss">
.pagosScroll {
  max-height: 70vh;
  overflow-x: auto;
}
.diente {
  width: 0px;
  height: auto;
  margin-left: 50px;
  display: inline-block;
}

.cuadro {
  background-color: #ffffff;
  border: 1px solid #ccc;
  position: relative;
  width: 20px;
  height: 15px;
  left: 45px;
  -webkit-border-radius: 80px 80px 0px 15px;
  -moz-border-radius: 80px 80px 0px 15px;
  border-radius: 80px 80px 0px 15px;
}
.cuadro:hover {
  background: rgba(117, 198, 243, 0.4);
  cursor: pointer;
}
.izquierdo {
  top: 1px !important;
  left: 29px !important;
  -webkit-transform: rotate(270deg);
  -moz-transform: rotate(270deg);
  -ms-transform: rotate(270deg);
  -o-transform: rotate(270deg);
  transform: rotate(270deg);
}
.debajo {
  top: 1px !important;
  left: 45px !important;
  -webkit-transform: rotate(180deg);
  -moz-transform: rotate(180deg);
  -ms-transform: rotate(180deg);
  -o-transform: rotate(180deg);
  transform: rotate(180deg);
}
.derecha {
  top: -29px !important;
  left: 61px !important;
  -webkit-transform: rotate(90deg);
  -moz-transform: rotate(90deg);
  -ms-transform: rotate(90deg);
  -o-transform: rotate(90deg);
  transform: rotate(90deg);
}
.centro {
  background: #f3f3f3;
  border: 1px solid #ccc;
  width: 20px;
  height: 18px;
  top: -46px;
  left: 45px;
  position: relative;
}
.centro:hover {
  border: 1px solid rgba(117, 198, 243, 0.4);
  background-color: rgba(117, 198, 243, 0.4);
  cursor: pointer;
}
.color {
  width: 200px;
  height: 200px;
  position: relative;
  margin: 0 auto;
  padding: 3px;
}
.select {
  width: 40px;
  height: 40px;
  margin: 2px;
  background-color: #ccc;
  display: inline-block;
  cursor: pointer;
}
.select:hover {
  border: 1px solid rgba(117, 198, 243, 0.4);
}
.content {
  position: absolute;
}
.ui-dialog {
  position: relative !important;
}
</style>
